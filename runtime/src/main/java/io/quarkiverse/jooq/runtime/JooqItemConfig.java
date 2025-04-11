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
     * The jOOQ configuration
     */
    Optional<String> configuration();

    /**
     * The jOOQ configuration by inject named
     */
    Optional<String> configurationInject();
}
