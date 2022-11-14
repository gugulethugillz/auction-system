package zw.co.cassavasmartech.auctionsystem.service.impl.reports;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import zw.co.cassavasmartech.auctionsystem.forms.reports.AssetReportCriteriaForm;
import zw.co.cassavasmartech.auctionsystem.forms.reports.PaymentReportCriteriaForm;
import zw.co.cassavasmartech.auctionsystem.projections.AssetReportEntry;
import zw.co.cassavasmartech.auctionsystem.projections.PaymentReportEntry;
import zw.co.cassavasmartech.auctionsystem.repository.AssetRepository;
import zw.co.cassavasmartech.auctionsystem.repository.PaymentRequestRepository;
import zw.co.cassavasmartech.auctionsystem.service.ifaces.reports.ReportsService;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;

/**
 * Created by alfred on 12 October 2020
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ReportsServiceImpl implements ReportsService {
    private final PaymentRequestRepository paymentRequestRepository;
    private final AssetRepository assetRepository;

    @Override
    public List<PaymentReportEntry> getPaymentReportEntries(PaymentReportCriteriaForm criteriaForm) {
        final OffsetDateTime start = criteriaForm.getStartDate().atOffset(ZoneOffset.UTC);
        final OffsetDateTime end = criteriaForm.getEndDate().atOffset(ZoneOffset.UTC);
        final boolean useCreationDate = criteriaForm.isUseCreationDate();
        if(useCreationDate) {
            return paymentRequestRepository.getPaymentReportEntries(start, end);
        } else {
            return paymentRequestRepository.getPaymentReportEntriesByPaymentDate(start, end);
        }
    }

    @Override
    public List<AssetReportEntry> getAssetReportEntries(AssetReportCriteriaForm criteriaForm) {
        final OffsetDateTime start = criteriaForm.getStartDate().atOffset(ZoneOffset.UTC);
        final OffsetDateTime end = criteriaForm.getEndDate().atOffset(ZoneOffset.UTC);
        return assetRepository.getAssetReportEntries(start, end);
    }
}
