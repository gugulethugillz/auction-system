package zw.co.cassavasmartech.auctionsystem.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import zw.co.cassavasmartech.auctionsystem.common.BaseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import zw.co.cassavasmartech.auctionsystem.common.BaseEntity;
import zw.co.cassavasmartech.auctionsystem.common.enums.UserRole;
import javax.persistence.Table;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

/**
 * Created by alfred on 18 September 2020
 * Create a view page for a single winner, view page for multiple winners, create Winner service interface and implementation
 */
@Data
@NoArgsConstructor
@Inheritance
@Entity
//@Table(name = "winners")
//Komborero, Tanaka
public class Winner extends BaseEntity {

    private String name;

    @OneToOne(targetEntity = Bid.class)
    private Bid bid;

    @ManyToOne(targetEntity = User.class)
    private User user;



}

