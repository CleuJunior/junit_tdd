package br.com.cleonildojunior.services;

import br.com.cleonildojunior.exceptions.ResourceNotFoundException;
import br.com.cleonildojunior.model.Person;
import br.com.cleonildojunior.repositories.PersonRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@Service
public class PersonServices {
	private static final Logger LOG = Logger.getLogger(PersonServices.class.getName());
	private final PersonRepository repository;

	public PersonServices(PersonRepository repository) {
		this.repository = repository;
	}

	public List<Person> findAll() {
		LOG.info("Finding all people!");

		return this.repository.findAll();
	}

	public Person findById(Long id) {
		
		LOG.info("Finding one person!");
		
		return this.repository.findById(id)
			.orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));
	}
	
	public Person create(Person person) {

		LOG.info("Creating one person!");
		
		Optional<Person> savedPerson = this.repository.findByEmail(person.getEmail());

		if(savedPerson.isPresent()) {
		    throw new ResourceNotFoundException("Person already exist with given e-Mail: " + person.getEmail());
		}
		return this.repository.save(person);
	}
	
	public Person update(Person person) {
		
		LOG.info("Updating one person!");
		
		Person entity = this.repository.findById(person.getId())
			.orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));

		entity.setFirstName(person.getFirstName());
		entity.setLastName(person.getLastName());
		entity.setAddress(person.getAddress());
		entity.setGender(person.getGender());
		
		return this.repository.save(person);
	}
	
	public void delete(Long id) {
		
		LOG.info("Deleting one person!");
		
		Person entity = this.repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));

		this.repository.delete(entity);
	}
}
