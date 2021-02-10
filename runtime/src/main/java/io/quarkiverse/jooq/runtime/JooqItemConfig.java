package io.quarkiverse.jooq.runtime;

import java.util.Optional;

import io.quarkus.runtime.annotations.ConfigGroup;
import io.quarkus.runtime.annotations.ConfigItem;

/**
 * @author <a href="mailto:leo.tu.taipei@gmail.com">Leo Tu</a>
 */
@ConfigGroup
public class JooqItemConfig {

    /**
     * The jOOQ dialect
     */
    @ConfigItem
    public String dialect;

    /**
     * The jOOQ dataSource
     */
    @ConfigItem
    public Optional<String> datasource;

    /**
     * The jOOQ configuration
     */
    @ConfigItem
    public Optional<String> configuration;

    /**
     * The jOOQ configuration by inject named
     */
    @ConfigItem
    public Optional<String> configurationInject;

    @Override
    public String toString() {
        return super.toString() + "[dialect=" + dialect + ", datasource=" + datasource + ", configuration="
                + configuration + ", configurationInject=" + configurationInject + "]";
    }
}
