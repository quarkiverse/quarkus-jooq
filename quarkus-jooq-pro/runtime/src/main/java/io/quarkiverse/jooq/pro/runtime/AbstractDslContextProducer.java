package io.quarkiverse.jooq.pro.runtime;

import java.util.Objects;

import org.jboss.logging.Logger;
import org.jooq.DSLContext;

import io.agroal.api.AgroalDataSource;
import io.quarkiverse.jooq.runtime.JooqCustomContext;
import io.r2dbc.spi.ConnectionFactory;

/**
 * Produces DSLContext
 *
 * @author Tim King
 */
public abstract class AbstractDslContextProducer {
    private static final Logger log = Logger.getLogger(AbstractDslContextProducer.class);

    public DSLContext createDslContext(String sqlDialect, AgroalDataSource dataSource, String customConfiguration) {
        Objects.requireNonNull(sqlDialect, "sqlDialect");
        Objects.requireNonNull(dataSource, "dataSource");
        return createDslContext(sqlDialect, dataSource, createCustomContext(customConfiguration));
    }

    public DSLContext createDslContext(String sqlDialect, AgroalDataSource dataSource, JooqCustomContext customConfiguration) {
        Objects.requireNonNull(sqlDialect, "sqlDialect");
        Objects.requireNonNull(dataSource, "dataSource");
        Objects.requireNonNull(customConfiguration, "customConfiguration");
        return DslContextFactory.create(sqlDialect, dataSource, customConfiguration);
    }

    public DSLContext createDslContext(String sqlDialect, ConnectionFactory connectionFactory, String customConfiguration) {
        Objects.requireNonNull(sqlDialect, "sqlDialect");
        Objects.requireNonNull(connectionFactory, "connectionFactory");
        return createDslContext(sqlDialect, connectionFactory, createCustomContext(customConfiguration));
    }

    public DSLContext createDslContext(String sqlDialect, ConnectionFactory connectionFactory,
            JooqCustomContext customConfiguration) {
        Objects.requireNonNull(sqlDialect, "sqlDialect");
        Objects.requireNonNull(connectionFactory, "connectionFactory");
        Objects.requireNonNull(customConfiguration, "customConfiguration");
        return DslContextFactory.create(sqlDialect, connectionFactory, customConfiguration);
    }

    static private JooqCustomContext createCustomContext(String customConfiguration) {
        if (customConfiguration == null || customConfiguration.isEmpty()) {
            return new JooqCustomContext() {
            };
        }
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        if (cl == null)
            cl = JooqCustomContext.class.getClassLoader();
        try {
            Class<?> clazz = cl.loadClass(customConfiguration);
            return (JooqCustomContext) clazz.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            log.error(customConfiguration, e);
            throw new RuntimeException(e);
        }
    }
}
