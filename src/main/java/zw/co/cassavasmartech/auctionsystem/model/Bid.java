package zw.co.cassavasmartech.auctionsystem.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import zw.co.cassavasmartech.auctionsystem.common.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

/**
 * Created by alfred on 18 September 2020
 * Create CRUD for bids
 */
@Data
@NoArgsConstructor
@Inheritance
@Entity
//Gugu, Cesias
public class Bid extends BaseEntity {
    private BigDecimal value;
    private OffsetDateTime paymentDate;

    @ManyToOne(targetEntity = Asset.class)
    private Asset asset;

    @ManyToOne(targetEntity = User.class)
    private User user;

    @OneToOne(mappedBy = "bid")
    private PaymentRequest paymentRequest;

    @OneToOne(mappedBy = "bid")
    private Winner winner;

    @Override
    public String toString() {
        return "Bid{" +
                "value=" + value +
                ", paymentDate=" + paymentDate +
                '}';
    }
}
