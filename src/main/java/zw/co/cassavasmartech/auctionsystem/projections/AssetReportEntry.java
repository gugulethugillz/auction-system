package zw.co.cassavasmartech.auctionsystem.projections;

import zw.co.cassavasmartech.auctionsystem.common.enums.EntityStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface AssetReportEntry {
    String getPicture();
    String getName();
    String getCategoryName();
    BigDecimal getStartBidValue();
    BigDecimal getWinningBid();
    EntityStatus getStatus();
    Integer getNumberOfBids();
    LocalDateTime getAssetBidStartDate();
    LocalDateTime getAssetBidEndDate();
}
