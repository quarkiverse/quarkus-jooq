package io.quarkiverse.jooq;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Named;
import javax.inject.Singleton;

import org.jboss.logging.Logger;

import io.quarkiverse.jooq.runtime.JooqCustomContext;

/**
 *
 * @author <a href="mailto:leo.tu.taipei@gmail.com">Leo Tu</a>
 */
//@ApplicationScoped
@Singleton
public class MyCustomConfigurationFactory {
    private static final Logger LOGGER = Logger.getLogger(MyCustomConfigurationFactory.class);

    @PostConstruct
    void onPostConstruct() {
        LOGGER.debug("MyCustomConfigurationFactory: onPostConstruct");
    }

    @ApplicationScoped
    @Produces
    @Named("myCustomConfiguration2")
    public JooqCustomContext create() {
        LOGGER.debug("MyCustomConfigurationFactory: create");
        return new JooqCustomContext() {
        };
    }
}
