package zw.co.cassavasmartech.auctionsystem.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import zw.co.cassavasmartech.auctionsystem.common.BaseEntity;
import zw.co.cassavasmartech.auctionsystem.common.enums.PaymentMethod;
import zw.co.cassavasmartech.auctionsystem.common.enums.PaymentStatus;
import zw.co.cassavasmartech.auctionsystem.common.enums.UserRole;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@Inheritance
@Entity

public class PaymentRequest extends BaseEntity {
    private BigDecimal value;
    @Column(columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private OffsetDateTime date;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    private String pollUrl;
    private String transactionStatus;
    private String item;
    private String mobileNumber;
    private String description;
    private String merchantReference;
    private String paynowReference;
    private String systemReference;

    @JsonIgnore
    @OneToOne(targetEntity = Asset.class)
    private Asset asset;

    @JsonIgnore
    @OneToOne(targetEntity = Bid.class)
    private Bid bid;

    @JsonIgnore
    @ManyToOne(targetEntity = User.class)
    private User user;

    @JsonIgnore
    @OneToMany(mappedBy = "paymentRequest")
    private List<StatusHistory> statusHistories;
}
