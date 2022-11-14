package zw.co.cassavasmartech.auctionsystem.forms;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import zw.co.cassavasmartech.auctionsystem.model.Asset;
import zw.co.cassavasmartech.auctionsystem.model.Bid;
import zw.co.cassavasmartech.auctionsystem.model.User;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Slf4j
public class BidForm {

    private Long id;
    @NotNull(message = "Value is required")
    private BigDecimal value;

    @NotNull(message = "Asset is required")
    private Long assetId;

    public Bid getBid(Asset asset, User user) {
        Bid bid = new Bid();
        bid.setValue(value);
        bid.setUser(user);
        bid.setAsset(asset);
        return bid;
    }


    public static BidForm createBidForm(Bid bid) {
        if (bid == null) return BidForm.builder().build();
        BidForm bidForm = BidForm.builder()
                .assetId(bid.getAsset().getId())
                .value(bid.getValue())
                .build();
        return bidForm;
    }
}

