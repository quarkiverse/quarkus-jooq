package io.quarkiverse.jooq.it;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

import org.junit.jupiter.api.Test;

import io.quarkus.test.common.WithTestResource;
import io.quarkus.test.h2.H2DatabaseTestResource;
import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
@WithTestResource(H2DatabaseTestResource.class)
public class JooqResourceTest {

    @Test
    public void testHelloEndpoint() {
        given()
                .when().get("/jooq")
                .then()
                .statusCode(200);
    }

    /**
     * In native mode this is what proves the R2DBC driver is reachable at all: {@code ConnectionFactories.get} resolves
     * it through the service loader, which only works because the extension registers the providers.
     */
    @Test
    public void testReactiveEndpoint() {
        given()
                .when().get("/jooq/reactive")
                .then()
                .statusCode(200)
                .body(is("2"));
    }
}
