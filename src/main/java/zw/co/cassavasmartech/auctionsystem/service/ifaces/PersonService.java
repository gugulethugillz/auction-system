package zw.co.cassavasmartech.auctionsystem.service.ifaces;

import zw.co.cassavasmartech.auctionsystem.model.Person;

import java.util.List;
import java.util.Optional;

public interface PersonService {
    Optional<Person> findById(Long id);
    Optional<Person> findByUsername(String username);
    Optional<Person> createPerson(Person person);
    List<Person> findAll();
    List<Person> findAllAdmins();
    List<Person> findAllUsers();
    List<Person> searchPersons(String searchPhrase);
}
