package zw.co.cassavasmartech.auctionsystem.forms;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.jni.Local;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.thymeleaf.util.StringUtils;
import zw.co.cassavasmartech.auctionsystem.common.enums.UserRole;
import zw.co.cassavasmartech.auctionsystem.model.Admin;
import zw.co.cassavasmartech.auctionsystem.model.Person;
import zw.co.cassavasmartech.auctionsystem.model.User;

import javax.validation.constraints.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.OffsetTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Slf4j
public class PersonForm {
    @NotBlank(message = "First name is required")
    private String firstName;

    private String middleName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @Email
    @NotBlank(message = "Username name is required")
    private String username;

    @NotNull(message = "This field cannot be blank.")
//    @Min(value = 6, message = "Password should be at least 6 characters")
//    @Max(value = 20, message = "Password should be at most 20 characters")
   // @Size(min = 6, max = 20, message = "Password should be between 6 and 20 characters")
    private String password;

    @NotBlank(message = "Phone number is required")
    private String phone;

    @NotBlank(message = "National ID is required")
    private String nationalId;

    private String passportNumber;
    private String address;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;

    @NotNull(message = "User role is required")
    private UserRole userRole;

    public Person getPerson(PasswordEncoder passwordEncoder) {
        switch (userRole) {
            case ADMIN:
                return createAdmin(passwordEncoder);
            case USER:
                return createUser(passwordEncoder);
            default:
                throw new IllegalArgumentException("Unknown user role " + userRole);
        }
    }

    private User createUser(PasswordEncoder passwordEncoder) {
        return fillInDetails(new User(), passwordEncoder);
    }

    private Admin createAdmin(PasswordEncoder passwordEncoder) {
        return fillInDetails(new Admin(), passwordEncoder);
    }

    private <T extends Person> T fillInDetails(T person, PasswordEncoder passwordEncoder) {
        String encodedPassword = passwordEncoder.encode(password);
        log.info("Encoded password is: {}", encodedPassword);
        log.info("Password is: {}", password);
        person.setFirstName(firstName);
        person.setLastName(lastName);
        person.setUsername(username);
        person.setPhone(phone);
        person.setEmail(username);
        person.setDateOfBirth(Optional.ofNullable(dateOfBirth).map(dob -> dob.atTime(OffsetTime.now())).orElse(null));
        person.setNationalId(nationalId);
        person.setPassword(encodedPassword);
        person.setPassportNumber(StringUtils.isEmpty(passportNumber)? null: passportNumber);
        person.setAddress(address);
        return person;
    }

    public static PersonForm createPersonForm(Person person) {
        if (person == null) return PersonForm.builder().build();
        PersonForm personForm = PersonForm.builder()
                .address(person.getAddress())
                .dateOfBirth(person.getDateOfBirth().toLocalDate())
                .firstName(person.getFirstName())
                .lastName(person.getLastName())
                .phone(person.getPhone())
                .nationalId(person.getNationalId())
                .passportNumber(person.getPassportNumber())
                .userRole(person.getType())
                .build();
        return personForm;
    }
}
