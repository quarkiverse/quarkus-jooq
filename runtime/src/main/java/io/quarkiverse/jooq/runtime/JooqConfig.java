package io.quarkiverse.jooq.runtime;

import java.util.Map;

import io.quarkus.runtime.annotations.ConfigPhase;
import io.quarkus.runtime.annotations.ConfigRoot;
import io.smallrye.config.ConfigMapping;
import io.smallrye.config.WithDefault;
import io.smallrye.config.WithParentName;

/**
 * Read from application.properties file
 *
 * @author <a href="mailto:leo.tu.taipei@gmail.com">Leo Tu</a>
 */
@ConfigRoot(phase = ConfigPhase.BUILD_TIME)
@ConfigMapping(prefix = "quarkus.jooq")
public interface JooqConfig {

    /**
     * The default config.
     */
    @WithParentName
    JooqItemConfig defaultConfig();

    /**
     * Whether to automatically register jOOQ records/POJOs
     * for reflection.
     */
    @WithDefault("true")
    boolean registerGeneratedClassesForReflection();

    /**
     * Regex used to determine classes to be registered for
     * reflection.
     */
    @WithDefault(".+\\.tables\\.(pojos|records)\\..+")
    String generatedClassesPattern();

    /**
     * Additional configs.
     */
    @WithParentName
    Map<String, JooqItemConfig> namedConfig();

}
