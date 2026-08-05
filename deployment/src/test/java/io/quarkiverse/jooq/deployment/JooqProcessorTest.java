package io.quarkiverse.jooq.deployment;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import io.quarkiverse.jooq.runtime.JooqConfig;
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

    /**
     * Two contexts sharing one {@code JooqCustomContext} bean must still get a field each: Gizmo hands back the
     * existing field for a repeated name and annotates it a second time, which is not valid bytecode.
     */
    @Test
    public void namesTheConfigurationInjectFieldPerContext() {
        assertNotEquals(JooqProcessor.configurationInjectFieldName("dsl2"),
                JooqProcessor.configurationInjectFieldName("reactive2"));
    }

    /**
     * The R2DBC driver is only registered as a native service provider when a context asks for one, and the build step
     * that does it cannot run outside a native build, so the condition is asserted here.
     */
    @Test
    public void detectsAReactiveContextInEitherPosition() {
        assertTrue(JooqProcessor.hasReactiveContext(
                jooqConfig(itemConfig(null, "mainConnectionFactory"), Map.of())));
        assertTrue(JooqProcessor.hasReactiveContext(
                jooqConfig(itemConfig(null, null), Map.of("reactive", itemConfig(null, "mainConnectionFactory")))));
        assertFalse(JooqProcessor.hasReactiveContext(
                jooqConfig(itemConfig(null, null), Map.of("dsl1", itemConfig("datasource1", null)))));
    }

    private static JooqConfig jooqConfig(final JooqItemConfig defaultConfig, final Map<String, JooqItemConfig> namedConfig) {
        return new JooqConfig() {

            @Override
            public JooqItemConfig defaultConfig() {
                return defaultConfig;
            }

            @Override
            public boolean registerGeneratedClassesForReflection() {
                return true;
            }

            @Override
            public String generatedClassesPattern() {
                return ".+";
            }

            @Override
            public Map<String, JooqItemConfig> namedConfig() {
                return namedConfig;
            }
        };
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
