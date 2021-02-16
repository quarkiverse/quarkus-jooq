package io.quarkiverse.jooq.pro.runtime;

import java.util.Objects;

import org.jboss.logging.Logger;
import org.jooq.DSLContext;

import io.agroal.api.AgroalDataSource;
import io.quarkiverse.jooq.runtime.JooqCustomContext;

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

        if (customConfiguration == null || customConfiguration.isEmpty()) {
            return createDslContext(sqlDialect, dataSource, new JooqCustomContext() {
            });
        } else {
            ClassLoader cl = Thread.currentThread().getContextClassLoader();
            if (cl == null)
                cl = JooqCustomContext.class.getClassLoader();
            try {
                Class<?> clazz = cl.loadClass(customConfiguration);
                JooqCustomContext instance = (JooqCustomContext) clazz.getDeclaredConstructor().newInstance();
                return createDslContext(sqlDialect, dataSource, instance);
            } catch (Exception e) {
                log.error(customConfiguration, e);
                throw new RuntimeException(e);
            }
        }
    }

    public DSLContext createDslContext(String sqlDialect, AgroalDataSource dataSource, JooqCustomContext customConfiguration) {
        Objects.requireNonNull(sqlDialect, "sqlDialect");
        Objects.requireNonNull(dataSource, "dataSource");
        Objects.requireNonNull(customConfiguration, "customConfiguration");
        return DslContextFactory.create(sqlDialect, dataSource, customConfiguration);
    }
}
