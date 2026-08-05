package io.quarkiverse.jooq.runtime;

import java.util.Optional;

import io.quarkus.runtime.annotations.ConfigGroup;

/**
 * @author <a href="mailto:leo.tu.taipei@gmail.com">Leo Tu</a>
 */
@ConfigGroup
public interface JooqItemConfig {

    /**
     * The jOOQ dialect
     */
    String dialect();

    /**
     * The jOOQ dataSource
     */
    Optional<String> datasource();

    /**
     * The name of a CDI bean of type {@code io.r2dbc.spi.ConnectionFactory} to build a reactive
     * {@code DSLContext} from, instead of a blocking JDBC dataSource. Quarkus has no R2DBC
     * dataSource model, so the application owns the connection factory and its pool and exposes
     * it as a {@code @Named} bean.
     * <p>
     * Mutually exclusive with {@code datasource}.
     */
    Optional<String> connectionFactory();

    /**
     * The jOOQ configuration
     */
    Optional<String> configuration();

    /**
     * The jOOQ configuration by inject named
     */
    Optional<String> configurationInject();
}
