package io.quarkiverse.jooq.it;

import static io.restassured.RestAssured.given;

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
}
