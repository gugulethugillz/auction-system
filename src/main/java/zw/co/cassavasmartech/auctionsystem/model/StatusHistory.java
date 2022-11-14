package zw.co.cassavasmartech.auctionsystem.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import zw.co.cassavasmartech.auctionsystem.common.BaseEntity;
import zw.co.cassavasmartech.auctionsystem.common.enums.EntityStatus;
import zw.co.paynow.core.Payment;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@Inheritance
@Entity
public class StatusHistory extends BaseEntity {
    private String entityStatus;
    private String otherStatus;
    private String description;

    @ManyToOne(targetEntity = Asset.class)
    private Asset asset;

    @ManyToOne(targetEntity = PaymentRequest.class)
    private PaymentRequest paymentRequest;
}
