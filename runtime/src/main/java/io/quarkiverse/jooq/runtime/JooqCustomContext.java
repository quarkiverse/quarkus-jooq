package io.quarkiverse.jooq.runtime;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import org.jboss.logging.Logger;
import org.jooq.Configuration;
import org.jooq.ExecuteListenerProvider;
import org.jooq.conf.Settings;
import org.jooq.impl.DefaultConfiguration;

import io.quarkiverse.jooq.sql.SqlLoggerListener;

/**
 * Custom Configuration
 *
 * @author <a href="mailto:leo.tu.taipei@gmail.com">Leo Tu</a>
 */
public interface JooqCustomContext {
    Logger LOGGER = Logger.getLogger(JooqCustomContext.class);

    /**
     * File "jooq-settings.xml"
     */
    default void apply(Configuration configuration) {
        Settings settings = configuration.settings();
        settings.setRenderCatalog(false);
        settings.setRenderSchema(false);
        settings.setExecuteLogging(SqlLoggerListener.sqlLog.isTraceEnabled()); // LoggerListener
        settings.setRenderFormatted(false);
        settings.setQueryTimeout(60); // seconds

        if (settings.isExecuteLogging() && configuration instanceof DefaultConfiguration) {
            DefaultConfiguration defaultConfig = (DefaultConfiguration) configuration;
            if (configuration.executeListenerProviders() != null) {
                List<ExecuteListenerProvider> providers = new ArrayList<>(
                        Arrays.asList(configuration.executeListenerProviders()));
                providers.add(SqlLoggerListener::new);
                defaultConfig.setExecuteListenerProvider(providers.toArray(new ExecuteListenerProvider[0]));
            } else {
                defaultConfig.setExecuteListenerProvider(SqlLoggerListener::new);
            }

            Stream.of(configuration.executeListenerProviders()).forEach(p -> {
                LOGGER.debugv("executeListenerProvider: {0}", p);
            });
        }
    }
}
