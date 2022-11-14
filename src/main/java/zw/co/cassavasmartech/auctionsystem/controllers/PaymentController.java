package zw.co.cassavasmartech.auctionsystem.controllers;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import zw.co.cassavasmartech.auctionsystem.common.GenericResponse;
import zw.co.cassavasmartech.auctionsystem.common.enums.PaymentStatus;
import zw.co.cassavasmartech.auctionsystem.forms.PaymentInitiationForm;
import zw.co.cassavasmartech.auctionsystem.model.PaymentRequest;
import zw.co.cassavasmartech.auctionsystem.model.Person;
import zw.co.cassavasmartech.auctionsystem.service.ifaces.PaymentRequestService;
import zw.co.cassavasmartech.auctionsystem.service.ifaces.PersonService;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Controller
@Slf4j
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentRequestService paymentRequestService;
    private final PersonService personService;

    @PostMapping("/payments/paynow/initiate")
    public String initiatePayment(@Valid PaymentInitiationForm form, RedirectAttributes redirectAttributes) {
        final GenericResponse<PaymentRequest> paymentRequestGenericResponse = paymentRequestService.makePayment(form);
        redirectAttributes.addFlashAttribute("info", paymentRequestGenericResponse.getNarrative());
        return "redirect:/payments";
    }

    //Method for getting user paymentRequests
    //  localhost:8080/payments
    @GetMapping("/payments")
    public String getPaymentsByUser(@AuthenticationPrincipal Person person, Model model, @RequestParam(value = "keyword", required = false) String keyword) {
        model.addAttribute("authUser", person);
        model.addAttribute("message", "Welcome " + person.getFirstName());
        final Person user = personService.findByUsername(person.getUsername()).orElseThrow(() -> new RuntimeException("User not found"));
        String un = person.getUsername();
        // for searching specific paymentRequest
        if (!StringUtils.isEmpty(keyword)) {
            log.info("===========Requesting for all with Keyword{}", keyword);
            model.addAttribute("userPayments", paymentRequestService.findByKeyword(keyword));
            model.addAttribute("pendingPaymentRequests", new ArrayList<>());
            model.addAttribute("processingInProgressPaymentRequests", new ArrayList<>());
            model.addAttribute("successfulPaymentRequests", new ArrayList<>());
            model.addAttribute("failedPaymentRequests", new ArrayList<>());
        } else {
            final List<PaymentRequest> userPayments = paymentRequestService.getByUserId(user.getId());
            List<PaymentRequest> pendingPaymentRequests = filterPaymentRequestByStatus(userPayments, PaymentStatus.PENDING);
            List<PaymentRequest> processingInProgressPaymentRequests = filterPaymentRequestByStatus(userPayments, PaymentStatus.PROCESSING_IN_PROGRESS);
            List<PaymentRequest> successfulPaymentRequests = filterPaymentRequestByStatus(userPayments, PaymentStatus.SUCCESSFUL);
            List<PaymentRequest> failedPaymentRequests = filterPaymentRequestByStatus(userPayments, PaymentStatus.FAILED);
            model.addAttribute("pendingPaymentRequests", pendingPaymentRequests);
            model.addAttribute("processingInProgressPaymentRequests", processingInProgressPaymentRequests);
            model.addAttribute("successfulPaymentRequests", successfulPaymentRequests);
            model.addAttribute("failedPaymentRequests", failedPaymentRequests);
        }
        return "user_payments";
    }

    private List<PaymentRequest> filterPaymentRequestByStatus(List<PaymentRequest> paymentRequests, PaymentStatus status) {
        return paymentRequests.stream()
                .filter(paymentRequest -> paymentRequest.getPaymentStatus() == status)
                .collect(Collectors.toList());
    }


    // method is only for admins
    @GetMapping("/admin/payments")
    public String getPayments(@AuthenticationPrincipal Person person, Model model, String keyword) {
        model.addAttribute("authUser", person);
        model.addAttribute("message", "Welcome " + person.getFirstName());
//Searching specific paymentRequest record
        if (keyword != null) {
            log.info("===========Requesting for all with Keyword{}", keyword);
            model.addAttribute("payments", paymentRequestService.findByKeyword(keyword));
        } else {
            log.info("=============Requesting for all Payments==========");
            model.addAttribute("payments", paymentRequestService.findAll());
        }

        return "admin/payments";
    }


    @RequestMapping("/payments/findById")
    @ResponseBody
    public PaymentRequest findById(Long id) {
        log.info("=============Requesting for paymentRequest with id {}", id);
        return paymentRequestService.findById(id).orElse(null);
    }


}
