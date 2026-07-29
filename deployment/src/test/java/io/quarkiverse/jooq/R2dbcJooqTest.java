package io.quarkiverse.jooq;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.Duration;
import java.util.List;

import jakarta.inject.Inject;
import jakarta.inject.Named;

import org.jboss.logging.Logger;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Table;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import io.r2dbc.spi.ConnectionFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * A mocked unit test cannot catch a wrong Gizmo signature or a bad qualifier on the generated
 * R2DBC producer method, so this drives the real ArC-wired {@code DSLContext}.
 *
 * @author <a href="mailto:sigurd.sippel@channelpilot.com">Sigurd Sippel</a>
 */
//@Disabled
@QuarkusTest
public class R2dbcJooqTest {
    private static final Logger LOGGER = Logger.getLogger(R2dbcJooqTest.class);

    private static final Duration TIMEOUT = Duration.ofSeconds(30);

    private static final Table<?> DEMO = DSL.table(DSL.name("reactive_demo"));
    private static final Field<String> ID = DSL.field(DSL.name("id"), SQLDataType.VARCHAR(32).nullable(false));
    private static final Field<String> NAME = DSL.field(DSL.name("name"), SQLDataType.VARCHAR(128));

    /**
     * {@code configuration=<FQCN>}
     */
    @Inject
    @Named("reactive")
    DSLContext reactiveDsl;

    /**
     * {@code configuration-inject=<bean name>}
     */
    @Inject
    @Named("reactive2")
    DSLContext reactiveDsl2;

    @Inject
    @Named("reactiveConnectionFactory")
    ConnectionFactory connectionFactory;

    @BeforeEach
    void createEmptyTable() {
        Mono.from(reactiveDsl.createTableIfNotExists(DEMO)
                .column(ID)
                .column(NAME)
                .constraint(DSL.primaryKey(ID))).block(TIMEOUT);
        Mono.from(reactiveDsl.deleteFrom(DEMO)).block(TIMEOUT);
    }

    @Test
    public void producesContextBackedByTheInjectedConnectionFactory() {
        assertSame(connectionFactory, reactiveDsl.configuration().connectionFactory());
        assertSame(connectionFactory, reactiveDsl2.configuration().connectionFactory());
    }

    @Test
    public void insertsAndSelects() {
        Integer inserted = Mono.from(reactiveDsl.insertInto(DEMO, ID, NAME).values("id-1", "name-1")).block(TIMEOUT);
        assertEquals(1, inserted);

        List<String> names = Flux.from(reactiveDsl.select(NAME).from(DEMO))
                .map(Record1::value1)
                .collectList()
                .block(TIMEOUT);
        LOGGER.debugv("names: {0}", names);
        assertEquals(List.of("name-1"), names);
    }

    /**
     * The reason for the reactive context: unlike a {@code Uni} assembled inside
     * {@code transactionResultAsync}, this holds one connection across both statements.
     */
    @Test
    public void rollsBackAcrossStatements() {
        assertThrows(RuntimeException.class, () -> Flux.from(reactiveDsl.transactionPublisher(configuration -> Flux.concat(
                configuration.dsl().insertInto(DEMO, ID, NAME).values("id-tx", "first"),
                configuration.dsl().insertInto(DEMO, ID, NAME).values("id-tx", "duplicate primary key"))))
                .collectList()
                .block(TIMEOUT));

        assertEquals(0, countRows(reactiveDsl), "the first insert must not survive the failed transaction");
    }

    @Test
    public void commitsAcrossStatements() {
        List<Integer> rowCounts = Flux.from(reactiveDsl.transactionPublisher(configuration -> Flux.concat(
                configuration.dsl().insertInto(DEMO, ID, NAME).values("id-a", "a"),
                configuration.dsl().insertInto(DEMO, ID, NAME).values("id-b", "b"))))
                .collectList()
                .block(TIMEOUT);

        assertEquals(List.of(1, 1), rowCounts);
        assertEquals(2, countRows(reactiveDsl));
    }

    /**
     * Both contexts share one connection factory, so the second sees what the first wrote.
     */
    @Test
    public void appliesCustomContextFromInjectedBean() {
        Mono.from(reactiveDsl.insertInto(DEMO, ID, NAME).values("id-2", "name-2")).block(TIMEOUT);

        assertEquals(1, countRows(reactiveDsl2));
    }

    private static int countRows(DSLContext dsl) {
        return Mono.from(dsl.selectCount().from(DEMO)).map(Record1::value1).block(TIMEOUT);
    }
}
