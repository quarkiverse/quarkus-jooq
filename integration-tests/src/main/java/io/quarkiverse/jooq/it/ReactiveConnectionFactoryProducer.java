package io.quarkiverse.jooq.it;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Named;
import jakarta.inject.Singleton;

import io.r2dbc.spi.ConnectionFactories;
import io.r2dbc.spi.ConnectionFactory;

/**
 * Quarkus has no R2DBC dataSource model, so the application owns the connection factory and its
 * pool. This is what a consumer of {@code quarkus.jooq.<name>.connection-factory} has to provide.
 *
 * @author <a href="mailto:sigurd.sippel@channelpilot.com">Sigurd Sippel</a>
 */
@ApplicationScoped
public class ReactiveConnectionFactoryProducer {

    /**
     * DB_CLOSE_DELAY=-1 keeps the in-memory database alive between the connections jOOQ acquires
     * per statement, which the R2DBC path does not pool.
     */
    static final String R2DBC_URL = "r2dbc:h2:mem:///reactive?options=DB_CLOSE_DELAY=-1";

    @Produces
    @Singleton
    @Named("reactiveConnectionFactory")
    public ConnectionFactory createConnectionFactory() {
        return ConnectionFactories.get(R2DBC_URL);
    }
}
