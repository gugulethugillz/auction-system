package zw.co.cassavasmartech.auctionsystem.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import zw.co.cassavasmartech.auctionsystem.common.BaseEntity;
import zw.co.cassavasmartech.auctionsystem.common.enums.EntityStatus;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Data
@NoArgsConstructor
@Inheritance
@Entity

public class Asset extends BaseEntity {
    private String name;
    private String description;

    @ElementCollection
    private List<String> imageNames;
    private OffsetDateTime bidStartDate;
    private OffsetDateTime bidEndDate;
    private BigDecimal bidStartPrice;

    @JsonIgnore
    @ManyToOne(targetEntity = Category.class)
    private Category category;

    @JsonIgnore
    @OneToMany(mappedBy = "asset")
    private List<Bid> bids;

    @JsonIgnore
    @OneToMany(mappedBy = "asset")
    private List<StatusHistory> statusHistories;

    @PrePersist
    public void initialise() {
        this.setStatus(EntityStatus.PENDING_PROCESSING);
    }

    @Override
    public String toString() {
        return "Asset{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", imageNames=" + imageNames +
                ", bidStartDate=" + bidStartDate +
                ", bidEndDate=" + bidEndDate +
                ", bidStartPrice=" + bidStartPrice +
                '}';
    }

    @JsonIgnore
    @OneToOne(mappedBy = "asset")
    private PaymentRequest paymentRequest;

    public void addImageName(String imageNamem) {
        if (this.getImageNames() == null) {
            this.setImageNames(new ArrayList<>());
        }
        this.getImageNames().add(imageNamem);
    }

    public Integer getImageCount() {
        return Optional.ofNullable(imageNames).map(List::size).orElse(0);
    }

    public String getDefaultDisplayImage() {
        return Optional.ofNullable(imageNames).map(names -> names.stream().findAny().orElse("")).orElse("");
    }

    public BigDecimal getLatestPrice() {
        return Optional.ofNullable(bids).map(availableBids -> {
            return availableBids.stream().sorted(Comparator.comparing(Bid::getValue).reversed()).findFirst()
                    .map(Bid::getValue)
                    .orElse(bidStartPrice);
        }).orElse(bidStartPrice);
    }

    public BigDecimal getNextPrice() {
        return getLatestPrice().add(new BigDecimal(100));
    }

    public String getFormattedBidEndDate() {
        return DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm:ss a").format(bidEndDate);
    }
}
