package zw.co.cassavasmartech.auctionsystem.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import zw.co.cassavasmartech.auctionsystem.common.enums.UserRole;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Created by alfred on 18 September 2020
 */
@Data
@NoArgsConstructor
@Entity
public class User extends Person {
    @OneToMany(mappedBy = "user")
    private List<Winner> winners;

    @OneToMany(mappedBy = "user")
    private List<PaymentRequest> paymentRequests;

    @OneToMany(mappedBy = "user")
    private List<Bid> bids;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays.asList(
                new SimpleGrantedAuthority("ROLE_" + UserRole.USER.name())
        );
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
