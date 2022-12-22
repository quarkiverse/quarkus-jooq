package io.quarkiverse.jooq;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Default;
import javax.enterprise.inject.Instance;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;
import javax.inject.Qualifier;
import javax.inject.Singleton;

import org.jboss.logging.Logger;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

/**
 *
 * @author <a href="mailto:leo.tu.taipei@gmail.com">Leo Tu</a>
 */
//@Disabled
@QuarkusTest
public class InjectTest {
    private static final Logger LOGGER = Logger.getLogger(InjectTest.class);

    @Inject
    TestServiceBean testBean;

    @Order(1)
    @Test
    public void test1() throws Exception {
        try {
            testBean.action();
        } catch (Exception e) {
            LOGGER.error("", e);
            throw e;
        }
    }

    @Target({ ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER, ElementType.TYPE })
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @Qualifier
    static public @interface ServiceType {

        String value();
    }

    @ApplicationScoped
    static public class ServiceBeanProducer {

        @Singleton
        @Produces
        @Default
        public ServiceInterface createDefault() {
            LOGGER.debug("createDefault");
            return new ServiceImpl("my-default");
        }

        @ApplicationScoped
        @Produces
        @Named("name1")
        @ServiceType("name1")
        public ServiceInterface createNamed1() {
            LOGGER.debug("createNamed1");
            return new ServiceImpl("my-name-1");
        }

        @ApplicationScoped
        @Produces
        @Named("name2")
        @ServiceType("name2")
        public ServiceInterface createNamed2() {
            LOGGER.debug("createNamed2");
            return new ServiceImpl("my-name-2");
        }

        @ApplicationScoped
        @Produces
        @Named("name3")
        @ServiceType("name3")
        public ServiceInterface createNamed3() {
            LOGGER.debug("createNamed3");
            return new ServiceImpl("my-name-3");
        }
    }

    static public interface ServiceInterface {

        public String getMyName();
    }

    static public class ServiceImpl implements ServiceInterface {
        private final String name;

        public ServiceImpl(String name) {
            this.name = name;
        }

        @Override
        public String getMyName() {
            LOGGER.debug("name:" + name);
            return name;
        }
    }

    @ApplicationScoped
    static public class TestServiceBean {

        @Inject
        // ServiceInterface defaultService;
        Instance<ServiceInterface> defaultService;

        @Inject
        @Named("name1")
        ServiceInterface service1;

        @Inject
        @Named("name2")
        Provider<ServiceInterface> service2;

        @Inject
        @Named("name3")
        Instance<ServiceInterface> service3;

        void action() throws Exception {
            LOGGER.debug(">> defaultService=" + defaultService.get().getMyName());
            LOGGER.debug(">> service1=" + service1.getMyName());
            LOGGER.debug(">> service2=" + service2.get().getMyName());
            LOGGER.debug(">> service3=" + service3.get().getMyName());
        }
    }
}
