package br.com.cleonildojunior.services;

import br.com.cleonildojunior.exceptions.ResourceNotFoundException;
import br.com.cleonildojunior.model.Person;
import br.com.cleonildojunior.repositories.PersonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.times;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PersonServicesTest {

    @Mock
    private PersonRepository repository;
    
    @InjectMocks
    private PersonServices services;
    public static final long ID = 1L;
    public static final String FIRST_NAME = "Leandro";
    public static final String LAST_NAME = "Costa";
    public static final String EMAIL = "leandro@erudio.com.br";
    public static final String ADDRESS = "Uberlândia - Minas Gerais - Brasil";
    public static final String GENDER = "Male";
    private Person person;
    
    @BeforeEach
    public void setup() {
        // Given / Arrange
        this.person = new Person(ID, FIRST_NAME, LAST_NAME, EMAIL, ADDRESS, GENDER);
    }
    
    @DisplayName("JUnit test for Given Person Object when Save Person then Return Person Object")
    @Test
    void testGivenPersonObject_WhenSavePerson_thenReturnPersonObject() {
        // Given / Arrange
        given(repository.findByEmail(anyString())).willReturn(Optional.empty());
        given(repository.save(person)).willReturn(person);
        
        // When / Act
        Person savedPerson = this.services.create(person);
        
        // Then / Assert
        assertNotNull(savedPerson);
        assertEquals("Leandro", savedPerson.getFirstName());
    }   
    
    @DisplayName("JUnit test for Given Existing Email when Save Person then throws Exception")
    @Test
    void testGivenExistingEmail_WhenSavePerson_thenThrowsException() {
        // Given / Arrange
        given(repository.findByEmail(anyString())).willReturn(Optional.of(person));
        
        // When / Act
        assertThrows(ResourceNotFoundException.class, () -> this.services.create(person));
        
        // Then / Assert
        verify(repository, never()).save(any(Person.class));
    }   
    
    @DisplayName("JUnit test for Given Persons List when findAll Persons then Return Persons List")
    @Test
    void testGivenPersonsList_WhenFindAllPersons_thenReturnPersonsList() {
        // Given / Arrange
        Person anotherPerson = new Person(FIRST_NAME, LAST_NAME, EMAIL, ADDRESS, GENDER);
        
        given(repository.findAll()).willReturn(List.of(person, anotherPerson));
        
        // When / Act
        List<Person> personsList = this.services.findAll();
        
        // Then / Assert
        assertNotNull(personsList);
        assertEquals(2, personsList.size());
    }   
    
    @DisplayName("JUnit test for Given Empty Persons List when findAll Persons then Return Empty Persons List")
    @Test
    void testGivenEmptyPersonsList_WhenFindAllPersons_thenReturnEmptyPersonsList() {
        // Given / Arrange
        given(repository.findAll()).willReturn(Collections.emptyList());
        
        // When / Act
        List<Person> personsList = this.services.findAll();
        
        // Then / Assert
        assertTrue(personsList.isEmpty());
        assertEquals(0, personsList.size());
    }   
    
    @DisplayName("JUnit test for Given PersonId when findById then Return Person Object")
    @Test
    void testGivenPersonId_WhenFindById_thenReturnPersonObject() {
        // Given / Arrange
        given(repository.findById(anyLong())).willReturn(Optional.of(person));
        
        // When / Act
        Person savedPerson = this.services.findById(1L);
        
        // Then / Assert
        assertNotNull(savedPerson);
        assertEquals("Leandro", savedPerson.getFirstName());
    }  
    
    @DisplayName("JUnit test for Given Person Object when Update Person then Return Updated Person Object")
    @Test
    void testGivenPersonObject_WhenUpdatePerson_thenReturnUpdatedPersonObject() {
        // Given / Arrange
        given(repository.findById(anyLong())).willReturn(Optional.of(person));
        
        this.person.setFirstName("Leonardo");
        this.person.setEmail("leonardo@erudio.com.br");
        
        given(repository.save(person)).willReturn(person);
        
        // When / Act
        Person updatedPerson = this.services.update(person);
        
        // Then / Assert
        assertNotNull(updatedPerson);
        assertEquals("Leonardo", updatedPerson.getFirstName());
        assertEquals("leonardo@erudio.com.br", updatedPerson.getEmail());
    }  
    
    @DisplayName("JUnit test for Given PersonID when Delete Person then do Nothing")
    @Test
    void testGivenPersonID_WhenDeletePerson_thenDoNothing() {
        // Given / Arrange
        given(repository.findById(anyLong())).willReturn(Optional.of(person));
        willDoNothing().given(repository).delete(person);
        
        // When / Act
        this.services.delete(person.getId());
        
        // Then / Assert
        verify(repository, times(1)).delete(person);
    }  
}
