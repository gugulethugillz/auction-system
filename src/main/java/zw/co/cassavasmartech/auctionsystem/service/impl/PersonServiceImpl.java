package zw.co.cassavasmartech.auctionsystem.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import zw.co.cassavasmartech.auctionsystem.model.Admin;
import zw.co.cassavasmartech.auctionsystem.model.Person;
import zw.co.cassavasmartech.auctionsystem.model.User;
import zw.co.cassavasmartech.auctionsystem.repository.AdminRepository;
import zw.co.cassavasmartech.auctionsystem.repository.UserRepository;
import zw.co.cassavasmartech.auctionsystem.service.ifaces.PersonService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PersonServiceImpl implements PersonService {
    private final AdminRepository adminRepository;
    private final UserRepository userRepository;

    @Override
    public Optional<Person> findById(Long id) {
        log.info("Received request for user with id: {}", id);
        Optional<Admin> adminOptional = adminRepository.findById(id);
        if(adminOptional.isPresent()) {
            return Optional.of(adminOptional.get());
        }
        Optional<User> userOptional = userRepository.findById(id);
        if(userOptional.isPresent()) {
            return Optional.of(userOptional.get());
        }
        return Optional.empty();
    }

    @Override
    public Optional<Person> findByUsername(String username) {
        log.info("Received request for user with username: {}", username);
        Optional<Admin> adminOptional = adminRepository.findByUsername(username);
        if(adminOptional.isPresent()) {
            return Optional.of(adminOptional.get());
        }
        Optional<User> userOptional = userRepository.findByUsername(username);
        if(userOptional.isPresent()) {
            return Optional.of(userOptional.get());
        }
        return Optional.empty();
    }

    @Override
    public Optional<Person> createPerson(Person person) {
        if(person instanceof Admin) {
            Admin savedAdmin = adminRepository.save((Admin) person);
            return Optional.ofNullable(savedAdmin);
        }
        if(person instanceof User) {
            User savedUser = userRepository.save((User) person);
            return Optional.ofNullable(savedUser);
        }
        log.error("Unknown user type");
        return Optional.empty();
    }

    @Override
    public List<Person> findAll() {
        List<Person> personList = new ArrayList<>();
        personList.addAll(adminRepository.findAll());
        personList.addAll(userRepository.findAll());
        return personList;
    }

    @Override
    public List<Person> findAllAdmins() {
        return adminRepository.findAll().stream().map(admin -> (Person) admin).collect(Collectors.toList());
    }

    @Override
    public List<Person> findAllUsers() {
        return userRepository.findAll().stream().map(user -> (Person) user).collect(Collectors.toList());
    }

    @Override
    public List<Person> searchPersons(String searchPhrase) {
        String formattedPhrase = String.format("%s%s%s", "%", searchPhrase, "%");
        log.info("SEARCHING FOR PERSON WITH '{}'", formattedPhrase);
        List<Person> personList = new ArrayList<>();
        personList.addAll(adminRepository.search(formattedPhrase));
        personList.addAll(userRepository.search(formattedPhrase));
        return personList;
    }
}
