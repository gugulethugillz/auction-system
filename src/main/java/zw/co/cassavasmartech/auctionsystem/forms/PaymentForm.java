package zw.co.cassavasmartech.auctionsystem.forms;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import zw.co.cassavasmartech.auctionsystem.common.enums.PaymentMethod;
import zw.co.cassavasmartech.auctionsystem.model.*;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentForm {

    @NotBlank(message = "PaymentRequest value can't be empty")
    private BigDecimal value;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private OffsetDateTime date;

    @NotBlank(message = "PaymentRequest method can't be empty")
    private PaymentMethod paymentMethod;

    @NotNull(message = "Asset can't be empty")
    private Asset asset;

    @NotNull(message = "Field can't be empty")
    private Bid bid;

    @NotNull(message = "Field can't be empty")
    private User user;
    
    public PaymentRequest getPaymentMethod(){
        switch(paymentMethod) {
            case ZIPIT:
                return paymentTypeZipit();
            case ECOCASH:
               return paymentTypeEcocash();
            default:
                throw new IllegalArgumentException("Unknown paymentRequest method paymentRequest " + paymentMethod);
        }
    }

    private PaymentRequest paymentTypeEcocash() {
        return paymentDetails(new PaymentRequest());
    }

    private PaymentRequest paymentTypeZipit() {
        return paymentDetails(new PaymentRequest());
        
    }

    private <T extends PaymentRequest> T paymentDetails(T payment) {

        payment.setValue(value);
        payment.setDate(date);
        payment.setPaymentMethod(paymentMethod);
        payment.setAsset(asset);
        payment.setBid(bid);
        payment.setUser(user);

        return payment;
    }

    public static PaymentForm createPaymentForm(PaymentRequest paymentRequest){
        if(paymentRequest ==null) return  PaymentForm.builder().build();
        PaymentForm paymentForm = PaymentForm.builder()
                .value(paymentRequest.getValue())
                .date(paymentRequest.getDate())
                .paymentMethod(paymentRequest.getPaymentMethod())
                .asset(paymentRequest.getAsset())
                .bid(paymentRequest.getBid())
                .user(paymentRequest.getUser())
                .build();

        return paymentForm;


    }

}

