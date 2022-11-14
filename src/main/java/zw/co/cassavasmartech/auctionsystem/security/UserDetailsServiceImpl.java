package zw.co.cassavasmartech.auctionsystem.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import zw.co.cassavasmartech.auctionsystem.model.Person;
import zw.co.cassavasmartech.auctionsystem.service.ifaces.PersonService;

import java.util.Optional;

@Slf4j
@Component
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private PersonService personService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Person> personOptional = personService.findByUsername(username);
        String message = String.format("User with username %s not found.", username);
        log.info("User found? {}", personOptional.isPresent());
        personOptional.ifPresent(person -> {
            log.info("Loaded person => {}", person);
        });
        return personOptional.orElseThrow(() -> new UsernameNotFoundException(message));
    }
}
