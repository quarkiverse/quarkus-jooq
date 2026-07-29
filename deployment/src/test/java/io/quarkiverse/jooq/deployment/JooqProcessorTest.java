package io.quarkiverse.jooq.deployment;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.Test;

import io.quarkiverse.jooq.runtime.JooqItemConfig;
import io.quarkus.runtime.configuration.ConfigurationException;

/**
 * @author <a href="mailto:sigurd.sippel@channelpilot.com">Sigurd Sippel</a>
 */
public class JooqProcessorTest {

    @Test
    public void rejectsBothConnectionSources() {
        ConfigurationException thrown = assertThrows(ConfigurationException.class,
                () -> JooqProcessor.requireSingleConnectionSource("reactive",
                        itemConfig("datasource1", "mainConnectionFactory")));

        assertTrue(thrown.getMessage().contains("reactive"), thrown.getMessage());
        assertTrue(thrown.getMessage().contains("datasource1"), thrown.getMessage());
        assertTrue(thrown.getMessage().contains("mainConnectionFactory"), thrown.getMessage());
    }

    @Test
    public void acceptsASingleConnectionSource() {
        assertDoesNotThrow(() -> JooqProcessor.requireSingleConnectionSource("jdbc", itemConfig("datasource1", null)));
        assertDoesNotThrow(
                () -> JooqProcessor.requireSingleConnectionSource("reactive", itemConfig(null, "mainConnectionFactory")));
    }

    /**
     * The default context inherits the default datasource, so naming neither is valid there.
     */
    @Test
    public void acceptsNoConnectionSource() {
        assertDoesNotThrow(() -> JooqProcessor.requireSingleConnectionSource("default", itemConfig(null, null)));
    }

    private static JooqItemConfig itemConfig(final String datasource, final String connectionFactory) {
        return new JooqItemConfig() {

            @Override
            public String dialect() {
                return "H2";
            }

            @Override
            public Optional<String> datasource() {
                return Optional.ofNullable(datasource);
            }

            @Override
            public Optional<String> connectionFactory() {
                return Optional.ofNullable(connectionFactory);
            }

            @Override
            public Optional<String> configuration() {
                return Optional.empty();
            }

            @Override
            public Optional<String> configurationInject() {
                return Optional.empty();
            }
        };
    }
}
