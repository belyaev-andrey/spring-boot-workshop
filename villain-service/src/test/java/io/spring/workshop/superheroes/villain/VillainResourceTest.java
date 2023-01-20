package io.spring.workshop.superheroes.villain;

import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static org.apache.http.entity.ContentType.APPLICATION_JSON;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.HttpHeaders.ACCEPT;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.HttpStatus.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:application-test.properties")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class VillainResourceTest {

    @LocalServerPort
    int port;

    private static final String JSON = "application/json;charset=UTF-8";

    private static final String DEFAULT_NAME = "Super Chocolatine";
    private static final String UPDATED_NAME = "Super Chocolatine (updated)";
    private static final String DEFAULT_OTHER_NAME = "Super Chocolatine chocolate in";
    private static final String UPDATED_OTHER_NAME = "Super Chocolatine chocolate in (updated)";
    private static final String DEFAULT_PICTURE = "super_chocolatine.png";
    private static final String UPDATED_PICTURE = "super_chocolatine_updated.png";
    private static final String DEFAULT_POWERS = "does not eat pain au chocolat";
    private static final String UPDATED_POWERS = "does not eat pain au chocolat (updated)";
    private static final int DEFAULT_LEVEL = 42;
    private static final int UPDATED_LEVEL = 43;

    private static final int NB_VILLAINS = 570;
    private static String villainId;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
    }

    @Test
    public void testHelloEndpoint() {
        given()
                .when().get("/api/villains/hello")
                .then()
                .statusCode(200)
                .body(is("Hello from villain"));
    }

    @Test
    void shouldNotGetUnknownVillain() {
        Long randomId = -1L;
        given()
                .pathParam("id", randomId)
                .when().get("/api/villains/{id}")
                .then()
                .statusCode(NO_CONTENT.value());
    }

    @Test
    void shouldGetRandomVillain() {
        given()
                .when().get("/api/villains/random")
                .then()
                .statusCode(OK.value())
                .contentType(APPLICATION_JSON.getMimeType());
    }

    @Test
    void shouldNotAddInvalidItem() {
        Villain villain = new Villain();
        villain.name = null;
        villain.otherName = DEFAULT_OTHER_NAME;
        villain.picture = DEFAULT_PICTURE;
        villain.powers = DEFAULT_POWERS;
        villain.level = 0;

        given()
                .body(villain)
                .header(CONTENT_TYPE, JSON)
                .header(ACCEPT, JSON)
                .when()
                .post("/api/villains")
                .then()
                .statusCode(BAD_REQUEST.value());
    }

    @Test
    @Order(1)
    void shouldGetInitialItems() {
        List<Villain> villains = get("/api/villains").then()
                                                     .statusCode(OK.value())
                                                     .contentType(APPLICATION_JSON.getMimeType())
                                                     .extract().body().as(getVillainTypeRef());
        assertEquals(NB_VILLAINS, villains.size());
    }

    @Test
    @Order(2)
    void shouldAddAnItem() {
        Villain villain = new Villain();
        villain.name = DEFAULT_NAME;
        villain.otherName = DEFAULT_OTHER_NAME;
        villain.picture = DEFAULT_PICTURE;
        villain.powers = DEFAULT_POWERS;
        villain.level = DEFAULT_LEVEL;

        String location = given()
                .body(villain)
                .header(CONTENT_TYPE, JSON)
                .header(ACCEPT, JSON)
                .when()
                .post("/api/villains")
                .then()
                .statusCode(CREATED.value())
                .extract().header("Location");
        assertTrue(location.contains("/api/villains"));

        // Stores the id
        String[] segments = location.split("/");
        villainId = segments[segments.length - 1];
        assertNotNull(villainId);

        given()
                .pathParam("id", villainId)
                .when().get("/api/villains/{id}")
                .then()
                .statusCode(OK.value())
                .contentType(APPLICATION_JSON.getMimeType());

        List<Villain> villains = get("/api/villains").then()
                                                     .statusCode(OK.value())
                                                     .contentType(APPLICATION_JSON.getMimeType())
                                                     .extract().body().as(getVillainTypeRef());
        assertEquals(NB_VILLAINS + 1, villains.size());
    }

    @Test
    @Order(3)
    void testUpdatingAnItem() {
        Villain villain = new Villain();
        villain.id = Long.valueOf(villainId);
        villain.name = UPDATED_NAME;
        villain.otherName = UPDATED_OTHER_NAME;
        villain.picture = UPDATED_PICTURE;
        villain.powers = UPDATED_POWERS;
        villain.level = UPDATED_LEVEL;

        given()
                .body(villain)
                .header(CONTENT_TYPE, JSON)
                .header(ACCEPT, JSON)
                .when()
                .put("/api/villains")
                .then()
                .statusCode(OK.value())
                .contentType(APPLICATION_JSON.getMimeType());

        List<Villain> villains = get("/api/villains").then()
                                                     .statusCode(OK.value())
                                                     .contentType(APPLICATION_JSON.getMimeType())
                                                     .extract().body().as(getVillainTypeRef());
        assertEquals(NB_VILLAINS + 1, villains.size());
    }

    @Test
    @Order(4)
    void shouldRemoveAnItem() {
        given()
                .pathParam("id", villainId)
                .when().delete("/api/villains/{id}")
                .then()
                .statusCode(NO_CONTENT.value());

        List<Villain> villains = get("/api/villains").then()
                                                     .statusCode(OK.value())
                                                     .contentType(APPLICATION_JSON.getMimeType())
                                                     .extract().body().as(getVillainTypeRef());
        assertEquals(NB_VILLAINS, villains.size());
    }

    private TypeRef<List<Villain>> getVillainTypeRef() {
        return new TypeRef<List<Villain>>() {
            // Kept empty on purpose
        };
    }
}
