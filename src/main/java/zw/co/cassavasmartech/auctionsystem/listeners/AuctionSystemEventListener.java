package zw.co.cassavasmartech.auctionsystem.listeners;
import org.springframework.security.crypto.password.PasswordEncoder;
import zw.co.cassavasmartech.auctionsystem.common.enums.EntityStatus;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.authentication.event.LogoutSuccessEvent;
import org.springframework.stereotype.Component;
import zw.co.cassavasmartech.auctionsystem.model.Admin;
import zw.co.cassavasmartech.auctionsystem.model.Person;
import zw.co.cassavasmartech.auctionsystem.repository.AdminRepository;
import zw.co.cassavasmartech.auctionsystem.service.ifaces.PersonService;

import java.time.OffsetDateTime;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Component
public class AuctionSystemEventListener {
    private final PersonService personService;
    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;
    private static final String ADMIN_USERNAME = "admin@auctionsystem.co.zw";

    @EventListener
    public void onContextRefreshedEvent(ContextRefreshedEvent event) {
        Optional<Person> personOptional = personService.findByUsername(ADMIN_USERNAME);
        personOptional.ifPresent(person -> {
            log.info("Found user {}", person.getFullName());
        });

        if(personOptional.isPresent()) {
            log.info("Super admin profile already initialized, skipping...");
        } else {
            initilialiseAdminUser();
        }

//        Person initilisedProfile = personOptional.map(person -> {
//            log.info("Super admin profile already initialized, skipping...");
//            return person;
//        }).orElse(initilialiseAdminUser());
    }

    @EventListener
    public void onAuthenticationSuccessEvent(AuthenticationSuccessEvent event) {
        Person person = (Person) event.getAuthentication().getPrincipal();
        log.info("{} with username {} just logged in at {}", person.getFullName(), person.getUsername(), OffsetDateTime.now());
    }

    @EventListener
    public void onLogoutSuccessEvent(LogoutSuccessEvent event) {
        Person person = (Person) event.getAuthentication().getPrincipal();
        log.info("{} with username {} just logged out at {}", person.getFullName(), person.getUsername(), OffsetDateTime.now());
    }

    private Admin initilialiseAdminUser() {
        Admin admin = createAdminUser();
        Admin savedAdminUser = adminRepository.save(admin);
        log.info("Initialised profile for Administrator {}", savedAdminUser.getFullName());
        return admin;
    }

    private Admin createAdminUser() {
        Admin admin = new Admin();
        admin.setFirstName("Super");
        admin.setMiddleName("");
        admin.setLastName("Admin");
        admin.setAddress("Cassava Smartech, 1906 Borrowdale Road, Harare, Zimbabwe");
        admin.setEmail("admin@auctionsystem.co.zw");
        admin.setPhone("0772222499");
        admin.setPassword(passwordEncoder.encode("#pass123"));
        admin.setUsername(ADMIN_USERNAME);
        admin.setDateOfBirth(OffsetDateTime.now());
        admin.setDateCreated(OffsetDateTime.now());
        admin.setLastUpdated(OffsetDateTime.now());
        admin.setStatus(EntityStatus.ACTIVE);
        admin.setCreatedBy("system");
        admin.setLastUpdatedBy("system");
        return admin;
    }
}
