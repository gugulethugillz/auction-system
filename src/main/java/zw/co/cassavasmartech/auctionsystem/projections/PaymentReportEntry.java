package zw.co.cassavasmartech.auctionsystem.projections;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface PaymentReportEntry {
    LocalDateTime getDatePaid();
    BigDecimal getAmount();
    String getAssetName();
    String getCategoryName();
    String getWinnerName();
    LocalDateTime getWinDate();

    default String toLog() {
        return String.format("Amount: %s\nDate Paid%s\nAsset Name%s\nCategory Name%s\nWinner Name%s\nWin Date%s",
                getAmount(), getDatePaid(), getAssetName(), getCategoryName(), getWinnerName(), getWinDate());
    }
}
