package zw.co.cassavasmartech.auctionsystem.forms;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import zw.co.cassavasmartech.auctionsystem.common.enums.PaymentMethod;
import zw.co.cassavasmartech.auctionsystem.model.Asset;
import zw.co.cassavasmartech.auctionsystem.model.Bid;
import zw.co.cassavasmartech.auctionsystem.model.User;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Slf4j
public class PaymentInitiationForm {
    @NotNull(message = "Payment Id is required")
    private Long paymentRequestId;

    @NotNull(message = "Payment method is required")
    private PaymentMethod paymentMethod;

    @NotNull(message = "Mobile number is required")
    private String mobileNumber;
}

