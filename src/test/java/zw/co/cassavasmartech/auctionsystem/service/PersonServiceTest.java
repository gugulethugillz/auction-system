package zw.co.cassavasmartech.auctionsystem.service;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import zw.co.cassavasmartech.auctionsystem.model.Admin;
import zw.co.cassavasmartech.auctionsystem.model.Person;
import zw.co.cassavasmartech.auctionsystem.model.User;
import zw.co.cassavasmartech.auctionsystem.repository.AdminRepository;
import zw.co.cassavasmartech.auctionsystem.repository.UserRepository;
import zw.co.cassavasmartech.auctionsystem.service.impl.PersonServiceImpl;

import javax.swing.text.html.Option;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * Created by alfred on 21 September 2020
 */
@ExtendWith(SpringExtension.class)
public class
PersonServiceTest {
    @InjectMocks
    private PersonServiceImpl personService;

    @Mock
    private AdminRepository adminRepository;

    @Mock
    private UserRepository userRepository;

    private Admin admin;
    private User user;

    @BeforeEach
    public void init() {
        admin = new Admin();
        admin.setId(1L);
        admin.setUsername("alfred.samanga@gmail.com");

        user = new User();
        user.setId(2L);
        user.setUsername("alfred@gmail.com");
    }

    @AfterEach
    public void cleanUp() {
        admin = null;
    }

    @Test
    public void givenAnId_whenFindPerson_shouldReturnPerson() throws Exception {
        when(adminRepository.findById(any())).thenReturn(Optional.of(admin));

        Optional<Person> personOptional = personService.findById(1L);
        assertTrue(personOptional.isPresent(), "Person not found");
        Person person = personOptional.get();
        assertEquals(1L, person.getId());
    }

    @Test
    public void givenAnExistingPerson_whenFindByUsername_thenShouldReturnThePerson() throws Exception {
        when(adminRepository.findByUsername(any())).thenReturn(Optional.of(admin));

        Optional<Person> personOptional = personService.findByUsername("alfred.samanga@gmail.com");
        assertTrue(personOptional.isPresent(), "Person not found");
        Person person = personOptional.get();
        assertEquals(1L, person.getId());
    }

    @Test
    public void givenANonExistingPerson_whenFindByUsername_thenShouldReturnEmptyOptional() throws Exception {
        when(adminRepository.findByUsername(any())).thenReturn(Optional.of(admin));

        Optional<Person> personOptional = personService.findByUsername("jabulani@gmail.com");
        assertTrue(personOptional.isPresent(), "Person not found");
        Person person = personOptional.get();
        assertEquals(1L, person.getId());
    }

    @Test
    public void givenUnsavedPerson_whenCreatePerson_thenShouldReturnCreatedPerson() throws Exception {
        when(adminRepository.save(any())).thenReturn(admin);

        Optional<Person> personOptional = personService.createPerson(admin);
        assertAll(
                () -> assertThat(personOptional).isNotNull(),
                () -> assertTrue(personOptional.isPresent()),
                () -> assertThat(personOptional.get()).isNotNull(),
                () -> assertEquals(admin.getUsername(), personOptional.get().getUsername())
        );
    }

    @Test
    public void givenSavedPerson_whenCreatePerson_thenShouldThrowExceptionThatUserAlreadyExists() throws Exception {
        when(adminRepository.save(any())).thenThrow(new RuntimeException("Username already taken."));

        RuntimeException runtimeException = assertThrows(RuntimeException.class, () -> {
            personService.createPerson(admin);
        });
        assertEquals("Username already taken.", runtimeException.getMessage());
    }

    @Test
    public void givenPersonRecords_whenFindAll_thenShouldReturnNonEmptyList() throws Exception {
        when(adminRepository.findAll()).thenReturn(List.of(admin));

        List<Person> personList = personService.findAll();
        assertAll(
                () -> assertThat(personList).isNotNull(),
                () -> assertTrue(personList.size() > 0),
                () -> assertThat(personList.get(0).getUsername()).isEqualTo(admin.getUsername())
        );
    }

    @Test
    public void givenNoPersonRecords_whenFindAll_thenShouldReturnEmptyList() throws Exception {
        when(adminRepository.findAll()).thenReturn(Collections.emptyList());

        List<Person> personList = personService.findAll();
        assertAll(
                () -> assertThat(personList).isNotNull(),
                () -> assertTrue(personList.size() == 0)
        );
    }

    @Test
    public void givenAdminRecords_whenFindAllAdmins_thenShouldReturnNonEmptyList() throws Exception {
        when(adminRepository.findAll()).thenReturn(List.of(admin));
        List<Person> personList = personService.findAllAdmins();
        assertAll(
                () -> assertThat(personList).isNotNull(),
                () -> assertTrue(personList.size() > 0),
                () -> assertThat(personList.get(0).getUsername()).isEqualTo(admin.getUsername())
        );
    }

    @Test
    public void givenNoAdminRecords_whenFindAllAdmins_thenShouldReturnEmptyList() throws Exception {
        List<Person> personList = personService.findAllAdmins();
        assertAll(
                () -> assertThat(personList).isNotNull(),
                () -> assertTrue(personList.size() == 0)
        );
    }

    @Test
    public void givenUserRecords_whenFindAllUsers_thenShouldReturnNonEmptyList() throws Exception {
        when(userRepository.findAll()).thenReturn(List.of(user));
        List<Person> personList = personService.findAllUsers();
        assertAll(
                () -> assertThat(personList).isNotNull(),
                () -> assertTrue(personList.size() > 0),
                () -> assertThat(personList.get(0).getUsername()).isEqualTo(user.getUsername())
        );
    }

    @Test
    public void givenNoUserRecords_whenFindAllUsers_thenShouldReturnEmptyList() throws Exception {
        List<Person> personList = personService.findAllUsers();
        assertAll(
                () -> assertThat(personList).isNotNull(),
                () -> assertTrue(personList.size() == 0)
        );
    }
}
