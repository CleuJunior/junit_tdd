package br.com.cleonildojunior.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import br.com.cleonildojunior.integrationtests.testcontainers.AbstractIntegrationTest;
import br.com.cleonildojunior.model.Person;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PersonRepositoryTest extends AbstractIntegrationTest {
    private static final String FIRST_NAME = "Leandro";
    private static final String LAST_NAME = "Costa";
    private static final String EMAIL = "leandro@erudio.com.br";
    private static final String ADDRESS = "Uberlândia - Minas Gerais - Brasil";
    private static final String GENDER = "Male";
    @Autowired
    private PersonRepository repository;
    private Person person;
    
    @BeforeEach
    public void setup() {
        // Given / Arrange
        this.person = new Person(FIRST_NAME, LAST_NAME, EMAIL, ADDRESS, GENDER);
    }
    
    @DisplayName("JUnit test for Given Person Object when Save then Return Saved Person")
    @Test
    void testGivenPersonObject_whenSave_thenReturnSavedPerson() {
        // Given / Arrange
        
        // When / Act
        Person savedPerson = this.repository.save(person);
        
        // Then / Assert
        assertNotNull(savedPerson);
        assertTrue(savedPerson.getId() > 0);
    }
    
    @DisplayName("JUnit test for Given Person List when findAll then Return Person List")
    @Test
    void testGivenPersonList_whenFindAll_thenReturnPersonList() {
        // Given / Arrange
        Person anotherPerson = new Person(FIRST_NAME, LAST_NAME, EMAIL, ADDRESS, GENDER);
        
        this.repository.save(person);
        this.repository.save(anotherPerson);
        
        // When / Act
        List<Person> personList = this.repository.findAll();
        
        // Then / Assert
        assertNotNull(personList);
        assertEquals(2, personList.size());
    }
    
    @DisplayName("JUnit test for Given Person Object when findByID then Return Person Object")
    @Test
    void testGivenPersonObject_whenFindByID_thenReturnPersonObject() {
        // Given / Arrange
        this.repository.save(person);
        
        // When / Act
        Person savedPerson = this.repository.findById(person.getId()).get();
        
        // Then / Assert
        assertNotNull(savedPerson);
        assertEquals(person.getId(), savedPerson.getId());
    }
    
    @DisplayName("JUnit test for Given Person Object when findByEmail then Return Person Object")
    @Test
    void testGivenPersonObject_whenFindByEmail_thenReturnPersonObject() {
        // Given / Arrange
        this.repository.save(person);
        
        // When / Act
        Person savedPerson = this.repository.findByEmail(person.getEmail()).get();
        
        // Then / Assert
        assertNotNull(savedPerson);
        assertEquals(person.getId(), savedPerson.getId());
    }
    
    @DisplayName("JUnit test for Given Person Object when Update Person then Return Updated Person Object")
    @Test
    void testGivenPersonObject_whenUpdatePerson_thenReturnUpdatedPersonObject() {
        // Given / Arrange
        this.repository.save(person);
        
        // When / Act
        Person savedPerson = this.repository.findById(person.getId()).get();
        savedPerson.setFirstName("Leonardo");
        savedPerson.setEmail("leonardo@erudio.com.br");
        
        Person updatedPerson = this.repository.save(savedPerson);
        
        // Then / Assert
        assertNotNull(updatedPerson);
        assertEquals("Leonardo", updatedPerson.getFirstName());
        assertEquals("leonardo@erudio.com.br", updatedPerson.getEmail());
    }
    
    @DisplayName("JUnit test for Given Person Object when Delete then Remove Person")
    @Test
    void testGivenPersonObject_whenDelete_thenRemovePerson() {
        // Given / Arrange
        this.repository.save(person);
        
        // When / Act
        this.repository.deleteById(person.getId());
        
        Optional<Person> personOptional = this.repository.findById(person.getId());
        
        // Then / Assert
        assertTrue(personOptional.isEmpty());
    }
    
    @DisplayName("JUnit test for Given firstName and lastName when findJPQL then Return Person Object")
    @Test
    void testGivenFirstNameAndLastName_whenFindJPQL_thenReturnPersonObject() {
        // Given / Arrange
        this.repository.save(person);

        // When / Act
        Person savedPerson = this.repository.findByJPQL(FIRST_NAME, LAST_NAME);
        
        // Then / Assert
        assertNotNull(savedPerson);
        assertEquals(FIRST_NAME, savedPerson.getFirstName());
        assertEquals(LAST_NAME, savedPerson.getLastName());
    }
    
    @DisplayName("JUnit test for Given firstName and lastName when findByJPQLNamedParameters then Return Person Object")
    @Test
    void testGivenFirstNameAndLastName_whenFindByJPQLNamedParameters_thenReturnPersonObject() {
        // Given / Arrange
        this.repository.save(person);

        // When / Act
        Person savedPerson = this.repository.findByJPQLNamedParameters(FIRST_NAME, LAST_NAME);
        
        // Then / Assert
        assertNotNull(savedPerson);
        assertEquals(FIRST_NAME, savedPerson.getFirstName());
        assertEquals(LAST_NAME, savedPerson.getLastName());
    }
    
    @DisplayName("JUnit test for Given firstName and lastName when findByNativeSQL then Return Person Object")
    @Test
    void testGivenFirstNameAndLastName_whenFindByNativeSQL_thenReturnPersonObject() {
        // Given / Arrange
        this.repository.save(person);
        

        // When / Act
        Person savedPerson = this.repository.findByNativeSQL(FIRST_NAME, LAST_NAME);
        
        // Then / Assert
        assertNotNull(savedPerson);
        assertEquals(FIRST_NAME, savedPerson.getFirstName());
        assertEquals(LAST_NAME, savedPerson.getLastName());
    }
    
    @DisplayName("JUnit test for Given firstName and lastName when findByNativeSQLwithNamedParameters then Return Person Object")
    @Test
    void testGivenFirstNameAndLastName_whenFindByNativeSQLwithNamedParameters_thenReturnPersonObject() {
        // Given / Arrange
        this.repository.save(person);
        

        // When / Act
        Person savedPerson = this.repository.findByNativeSQLwithNamedParameters(FIRST_NAME, LAST_NAME);
        
        // Then / Assert
        assertNotNull(savedPerson);
        assertEquals(FIRST_NAME, savedPerson.getFirstName());
        assertEquals(LAST_NAME, savedPerson.getLastName());
    }
}
