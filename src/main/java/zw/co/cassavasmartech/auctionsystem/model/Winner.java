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

@Data
@NoArgsConstructor
@Inheritance
@Entity

public class Winner extends BaseEntity {

    private String name;

    @OneToOne(targetEntity = Bid.class)
    private Bid bid;

    @ManyToOne(targetEntity = User.class)
    private User user;



}

