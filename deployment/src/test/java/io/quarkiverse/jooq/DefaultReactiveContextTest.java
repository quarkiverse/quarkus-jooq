package io.quarkiverse.jooq;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.time.Duration;
import java.util.Map;

import jakarta.inject.Inject;
import jakarta.inject.Named;

import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Table;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.QuarkusTestProfile;
import io.quarkus.test.junit.TestProfile;
import io.r2dbc.spi.ConnectionFactory;
import reactor.core.publisher.Mono;

/**
 * The default context is generated through its own path — an unqualified producer method and a field carrying the
 * connection source without a name of its own — so a reactive default context needs an application of its own to be
 * wired at all. The named contexts are covered by {@link R2dbcJooqTest}.
 *
 * @author <a href="mailto:sigurd.sippel@channelpilot.com">Sigurd Sippel</a>
 */
//@Disabled
@QuarkusTest
@TestProfile(DefaultReactiveContextTest.DefaultReactiveProfile.class)
public class DefaultReactiveContextTest {

    private static final Duration TIMEOUT = Duration.ofSeconds(30);

    private static final Table<?> DEMO = DSL.table(DSL.name("default_reactive_demo"));
    private static final Field<String> ID = DSL.field(DSL.name("id"), SQLDataType.VARCHAR(32).nullable(false));

    /**
     * Naming a connection factory turns the default context reactive, which the shared
     * {@code application.properties} cannot express alongside its blocking default context.
     */
    public static class DefaultReactiveProfile implements QuarkusTestProfile {

        @Override
        public Map<String, String> getConfigOverrides() {
            return Map.of("quarkus.jooq.connection-factory", "reactiveConnectionFactory");
        }
    }

    @Inject
    DSLContext defaultDsl;

    @Inject
    @Named("reactiveConnectionFactory")
    ConnectionFactory connectionFactory;

    @Test
    public void producesTheDefaultContextOverTheConnectionFactory() {
        assertSame(connectionFactory, defaultDsl.configuration().connectionFactory());
    }

    @Test
    public void queriesThroughTheDefaultReactiveContext() {
        Mono.from(defaultDsl.createTableIfNotExists(DEMO)
                .column(ID)
                .constraint(DSL.primaryKey(ID))).block(TIMEOUT);
        Mono.from(defaultDsl.deleteFrom(DEMO)).block(TIMEOUT);

        assertEquals(1, Mono.from(defaultDsl.insertInto(DEMO, ID).values("id-1")).block(TIMEOUT));
        assertEquals(1, Mono.from(defaultDsl.selectCount().from(DEMO)).map(Record1::value1).block(TIMEOUT));
    }
}
