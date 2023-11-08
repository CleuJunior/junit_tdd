package br.com.cleonildojunior.integrationtests.controller;

import br.com.cleonildojunior.config.TestConfigs;
import br.com.cleonildojunior.integrationtests.testcontainers.AbstractIntegrationTest;
import br.com.cleonildojunior.model.Person;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

@TestMethodOrder(OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class PersonControllerIntegrationTest extends AbstractIntegrationTest {
    private static final String FIRST_NAME = "Leandro";
    private static final String LAST_NAME = "Costa";
    private static final String EMAIL = "leandro@erudio.com.br";
    private static final String ADDRESS = "Uberlândia - Minas Gerais - Brasil";
    private static final String GENDER = "Male";
    private static final String UPDATE_NAME = "Leonardo";
    private static final String UPDATE_EMAIL = "leonardo@erudio.com.br";
    private static RequestSpecification specification;
    private static ObjectMapper objectMapper;
    private static Person person;
    
    @BeforeAll
    public static void setup() {
        // Given / Arrange
        objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        
        specification = new RequestSpecBuilder()
                .setBasePath("/person")
                .setPort(TestConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        person = new Person(FIRST_NAME, LAST_NAME, EMAIL, ADDRESS, GENDER);
    }

    @Test
    @Order(1)
    @DisplayName("JUnit integration given Person Object when Create one Person should Return a Person Object")
    void integrationTestGivenPersonObject_when_CreateOnePerson_ShouldReturnAPersonObject() throws
            JsonProcessingException {
        
        String content = given()
                .spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .body(person)
                .when()
                .post()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();
        
        Person createdPerson = objectMapper.readValue(content, Person.class);
        
        person = createdPerson;
        
        Assertions.assertNotNull(createdPerson);
        
        Assertions.assertNotNull(createdPerson.getId());
        Assertions.assertNotNull(createdPerson.getFirstName());
        Assertions.assertNotNull(createdPerson.getLastName());
        Assertions.assertNotNull(createdPerson.getAddress());
        Assertions.assertNotNull(createdPerson.getGender());
        Assertions.assertNotNull(createdPerson.getEmail());
        
        Assertions.assertTrue(createdPerson.getId() > 0);
        assertEquals(FIRST_NAME, createdPerson.getFirstName());
        assertEquals(LAST_NAME, createdPerson.getLastName());
        assertEquals(ADDRESS, createdPerson.getAddress());
        assertEquals(GENDER, createdPerson.getGender());
        assertEquals(EMAIL, createdPerson.getEmail());
    }
    
    @Test
    @Order(2)
    @DisplayName("JUnit integration given Person Object when Update one Person should Return a Updated Person Object")
    void integrationTestGivenPersonObject_when_UpdateOnePerson_ShouldReturnAUpdatedPersonObject() throws
            JsonProcessingException {

        person.setFirstName(UPDATE_NAME);
        person.setEmail(UPDATE_EMAIL);
        
        String content = given()
                .spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .body(person)
                .when()
                .put()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();
        
        Person updatedPerson = objectMapper.readValue(content, Person.class);
        
        person = updatedPerson;
        
        Assertions.assertNotNull(updatedPerson);
        
        Assertions.assertNotNull(updatedPerson.getId());
        Assertions.assertNotNull(updatedPerson.getFirstName());
        Assertions.assertNotNull(updatedPerson.getLastName());
        Assertions.assertNotNull(updatedPerson.getAddress());
        Assertions.assertNotNull(updatedPerson.getGender());
        Assertions.assertNotNull(updatedPerson.getEmail());
        
        Assertions.assertTrue(updatedPerson.getId() > 0);
        assertEquals(UPDATE_NAME, updatedPerson.getFirstName());
        assertEquals(LAST_NAME, updatedPerson.getLastName());
        assertEquals(ADDRESS, updatedPerson.getAddress());
        assertEquals(GENDER, updatedPerson.getGender());
        assertEquals(UPDATE_EMAIL, updatedPerson.getEmail());
    }
    
    @Test
    @Order(3)
    @DisplayName("JUnit integration given Person Object when findById should Return a Person Object")
    void integrationTestGivenPersonObject_when_findById_ShouldReturnAPersonObject() throws JsonProcessingException {
        
        String content = given()
                .spec(specification)
                .pathParam("id", person.getId())
                .when()
                .get("{id}")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();
        
        Person foundPerson = objectMapper.readValue(content, Person.class);
        
        Assertions.assertNotNull(foundPerson);
        
        Assertions.assertNotNull(foundPerson.getId());
        Assertions.assertNotNull(foundPerson.getFirstName());
        Assertions.assertNotNull(foundPerson.getLastName());
        Assertions.assertNotNull(foundPerson.getAddress());
        Assertions.assertNotNull(foundPerson.getGender());
        Assertions.assertNotNull(foundPerson.getEmail());
        
        Assertions.assertTrue(foundPerson.getId() > 0);
        assertEquals(UPDATE_NAME, foundPerson.getFirstName());
        assertEquals(LAST_NAME, foundPerson.getLastName());
        assertEquals(ADDRESS, foundPerson.getAddress());
        assertEquals(GENDER, foundPerson.getGender());
        assertEquals(UPDATE_EMAIL, foundPerson.getEmail());
    }
    
    @Test
    @Order(4)
    @DisplayName("JUnit integration given Person Object when findAll should Return a Persons List")
    void integrationTest_when_findAll_ShouldReturnAPersonsList() throws JsonProcessingException {
        
        Person anotherPerson = new Person(
                "Gabriela",
                "Rodriguez",
                "gabi@erudio.com.br",
                "Uberlândia - Minas Gerais - Brasil",
                "Female"
            );
        
        given()
                .spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .body(anotherPerson)
                .when()
                .post()
                .then()
                .statusCode(200);
        
        String content = given()
                .spec(specification)
                .when()
                .get()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();
        
        Person[] myArray = objectMapper.readValue(content, Person[].class);
        List<Person> people = Arrays.asList(myArray);
        
        Person foundPersonOne = people.get(0);
        
        Assertions.assertNotNull(foundPersonOne);
        
        Assertions.assertNotNull(foundPersonOne.getId());
        Assertions.assertNotNull(foundPersonOne.getFirstName());
        Assertions.assertNotNull(foundPersonOne.getLastName());
        Assertions.assertNotNull(foundPersonOne.getAddress());
        Assertions.assertNotNull(foundPersonOne.getGender());
        Assertions.assertNotNull(foundPersonOne.getEmail());
        
        Assertions.assertTrue(foundPersonOne.getId() > 0);
        assertEquals(UPDATE_NAME, foundPersonOne.getFirstName());
        assertEquals(LAST_NAME, foundPersonOne.getLastName());
        assertEquals(ADDRESS, foundPersonOne.getAddress());
        assertEquals(GENDER, foundPersonOne.getGender());
        assertEquals(UPDATE_EMAIL, foundPersonOne.getEmail());
        
        Person foundPersonTwo = people.get(1);
        
        Assertions.assertNotNull(foundPersonTwo);
        
        Assertions.assertNotNull(foundPersonTwo.getId());
        Assertions.assertNotNull(foundPersonTwo.getFirstName());
        Assertions.assertNotNull(foundPersonTwo.getLastName());
        Assertions.assertNotNull(foundPersonTwo.getAddress());
        Assertions.assertNotNull(foundPersonTwo.getGender());
        Assertions.assertNotNull(foundPersonTwo.getEmail());
        
        Assertions.assertTrue(foundPersonTwo.getId() > 0);
        assertEquals("Gabriela", foundPersonTwo.getFirstName());
        assertEquals("Rodriguez", foundPersonTwo.getLastName());
        assertEquals("Uberlândia - Minas Gerais - Brasil", foundPersonTwo.getAddress());
        assertEquals("Female", foundPersonTwo.getGender());
        assertEquals("gabi@erudio.com.br", foundPersonTwo.getEmail());
    }
    
    @Test
    @Order(5)
    @DisplayName("JUnit integration given Person Object when delete should Return No Content")
    void integrationTestGivenPersonObject_when_delete_ShouldReturnNoContent() {
        
        given()
                .spec(specification)
                .pathParam("id", person.getId())
                .when()
                .delete("{id}")
                .then()
                .statusCode(204);
    }
}
