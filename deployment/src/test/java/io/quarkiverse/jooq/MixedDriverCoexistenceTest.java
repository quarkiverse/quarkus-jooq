package io.quarkiverse.jooq;

import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import jakarta.inject.Inject;
import jakarta.inject.Named;

import org.jooq.DSLContext;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

/**
 * R2DBC is additive: adding a connection-factory context leaves the blocking contexts of the same
 * application on JDBC, because a context is bound to whichever connection source its own
 * configuration named. The queries themselves are covered by {@link JooqTest} (blocking, in this
 * same application) and {@link R2dbcJooqTest} (reactive).
 *
 * @author <a href="mailto:sigurd.sippel@channelpilot.com">Sigurd Sippel</a>
 */
//@Disabled
@QuarkusTest
public class MixedDriverCoexistenceTest {

    /**
     * jOOQ parks the unused half of a Configuration on a no-op placeholder, and those classes are
     * not public, so the binding is asserted by name.
     */
    private static final String NO_CONNECTION_PROVIDER = "NoConnectionProvider";
    private static final String NO_CONNECTION_FACTORY = "NoConnectionFactory";

    @Inject
    DSLContext defaultJdbcDsl;

    @Inject
    @Named("dsl1")
    DSLContext namedJdbcDsl;

    @Inject
    @Named("reactive")
    DSLContext reactiveDsl;

    @Test
    public void bindsEachContextToItsOwnConnectionSource() {
        assertTrue(isJdbcBacked(defaultJdbcDsl), "the default context must stay on JDBC");
        assertTrue(isJdbcBacked(namedJdbcDsl), "a named datasource context must stay on JDBC");
        assertTrue(isR2dbcBacked(reactiveDsl), "the connection-factory context must be on R2DBC");

        assertNotSame(defaultJdbcDsl, reactiveDsl);
        assertNotSame(namedJdbcDsl, reactiveDsl);
    }

    private static boolean isJdbcBacked(DSLContext dsl) {
        return !NO_CONNECTION_PROVIDER.equals(simpleName(dsl.configuration().connectionProvider()))
                && NO_CONNECTION_FACTORY.equals(simpleName(dsl.configuration().connectionFactory()));
    }

    private static boolean isR2dbcBacked(DSLContext dsl) {
        return !NO_CONNECTION_FACTORY.equals(simpleName(dsl.configuration().connectionFactory()))
                && NO_CONNECTION_PROVIDER.equals(simpleName(dsl.configuration().connectionProvider()));
    }

    private static String simpleName(Object o) {
        return o == null ? null : o.getClass().getSimpleName();
    }
}
