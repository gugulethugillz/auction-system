package zw.co.cassavasmartech.auctionsystem.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import zw.co.cassavasmartech.auctionsystem.forms.reports.AssetReportCriteriaForm;
import zw.co.cassavasmartech.auctionsystem.forms.reports.PaymentReportCriteriaForm;
import zw.co.cassavasmartech.auctionsystem.model.Person;
import zw.co.cassavasmartech.auctionsystem.projections.AssetReportEntry;
import zw.co.cassavasmartech.auctionsystem.projections.PaymentReportEntry;
import zw.co.cassavasmartech.auctionsystem.service.ifaces.CategoryService;
import zw.co.cassavasmartech.auctionsystem.service.ifaces.PersonService;
import zw.co.cassavasmartech.auctionsystem.service.ifaces.reports.ReportsService;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;

/**
 * Created by alfred on 14 September 2020
 */
@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/reports")
public class ReportController {
    private final ReportsService reportsService;

    @GetMapping("/payments")
    public String paymentReportsPage(@AuthenticationPrincipal Person person, Model model) {
        final OffsetDateTime now = OffsetDateTime.now();
        final OffsetDateTime start = now.withDayOfMonth(1);
        final OffsetDateTime end = now.withDayOfMonth(now.getMonth().maxLength());
        final PaymentReportCriteriaForm criteriaForm = new PaymentReportCriteriaForm();
        criteriaForm.setStartDate(start.toLocalDateTime());
        criteriaForm.setEndDate(end.toLocalDateTime());
        criteriaForm.setUseCreationDate(false);
        return paymentsReportsPageHelper(criteriaForm, person, model);
    }

    @PostMapping("/payments")
    public String paymentReportsPageFilter(@AuthenticationPrincipal Person person, PaymentReportCriteriaForm form, Model model) {
        return paymentsReportsPageHelper(form, person, model);
    }

    private String paymentsReportsPageHelper(PaymentReportCriteriaForm form, Person person, Model model) {
        log.info("Report Criteria Form is: {}", form);
        final List<PaymentReportEntry> paymentReportEntries = reportsService.getPaymentReportEntries(form);
        model.addAttribute("authUser", person);
        model.addAttribute("criteriaForm", new PaymentReportCriteriaForm());
        model.addAttribute("paymentReportEntries", paymentReportEntries);
        return "admin/reports/payment_reports";
    }

    @GetMapping("/assets")
    public String assetReportsPage(@AuthenticationPrincipal Person person, Model model) {
        final OffsetDateTime now = OffsetDateTime.now();
        final OffsetDateTime start = now.withDayOfMonth(1);
        final OffsetDateTime end = now.withDayOfMonth(now.getMonth().maxLength());
        final AssetReportCriteriaForm criteriaForm = new AssetReportCriteriaForm();
        criteriaForm.setStartDate(start.toLocalDateTime());
        criteriaForm.setEndDate(end.toLocalDateTime());
        return assetsReportsPageHelper(criteriaForm, person, model);
    }

    @PostMapping("/assets")
    public String assetReportsPageFilter(@AuthenticationPrincipal Person person, AssetReportCriteriaForm form, Model model) {
        return assetsReportsPageHelper(form, person, model);
    }

    private String assetsReportsPageHelper(AssetReportCriteriaForm form, Person person, Model model) {
        log.info("Report Criteria Form is: {}", form);
        final List<AssetReportEntry> assetReportEntries = reportsService.getAssetReportEntries(form);
        model.addAttribute("authUser", person);
        model.addAttribute("criteriaForm", new PaymentReportCriteriaForm());
        model.addAttribute("assetReportEntries", assetReportEntries);
        return "admin/reports/asset_reports";
    }
}
