package zw.co.cassavasmartech.auctionsystem.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import zw.co.cassavasmartech.auctionsystem.common.BaseEntity;
import zw.co.cassavasmartech.auctionsystem.common.enums.UserRole;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import java.time.OffsetDateTime;
import java.util.Collection;

@Data
@NoArgsConstructor
@Inheritance
@Entity
public abstract class Person extends BaseEntity implements UserDetails {
    private String firstName;
    private String middleName;
    private String lastName;
    private String nationalId;
    private String passportNumber;
    private String address;
    private String email;
    private String phone;
    private String password;
    private String username;
    private OffsetDateTime dateOfBirth;

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public UserRole getType() {
        if(this instanceof User) {
            return UserRole.USER;
        } else if(this instanceof Admin) {
            return UserRole.ADMIN;
        } else {
            return UserRole.NONE;
        }
    }

    @Override
    public String toString() {
        return "Person{" +
                "firstName='" + firstName + '\'' +
                ", middleName='" + middleName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", nationalId='" + nationalId + '\'' +
                ", passportNumber='" + passportNumber + '\'' +
                ", address='" + address + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", password='" + password + '\'' +
                ", username='" + username + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                '}';
    }

    public String getFullName() {
        return String.format("%s %s", firstName, lastName);
    }
}
