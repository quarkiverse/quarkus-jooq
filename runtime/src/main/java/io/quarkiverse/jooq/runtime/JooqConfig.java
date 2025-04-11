package io.quarkiverse.jooq.runtime;

import java.util.Map;

import io.quarkus.runtime.annotations.ConfigPhase;
import io.quarkus.runtime.annotations.ConfigRoot;
import io.smallrye.config.ConfigMapping;
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
     * Additional configs.
     */
    @WithParentName
    Map<String, JooqItemConfig> namedConfig();

}
