package zw.co.cassavasmartech.auctionsystem.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import zw.co.cassavasmartech.auctionsystem.common.enums.UserRole;

import javax.persistence.Entity;
import java.util.Arrays;
import java.util.Collection;

/**
 * Created by alfred on 18 September 2020
 */
@Data
@NoArgsConstructor
@Entity
public class Admin extends Person {
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays.asList(
                new SimpleGrantedAuthority("ROLE_" + UserRole.ADMIN.name()),
                new SimpleGrantedAuthority("ROLE_" + UserRole.USER.name())
        );
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
