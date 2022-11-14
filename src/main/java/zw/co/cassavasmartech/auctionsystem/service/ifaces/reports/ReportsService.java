package zw.co.cassavasmartech.auctionsystem.service.ifaces.reports;

import zw.co.cassavasmartech.auctionsystem.forms.reports.AssetReportCriteriaForm;
import zw.co.cassavasmartech.auctionsystem.forms.reports.PaymentReportCriteriaForm;
import zw.co.cassavasmartech.auctionsystem.projections.AssetReportEntry;
import zw.co.cassavasmartech.auctionsystem.projections.PaymentReportEntry;

import java.time.OffsetDateTime;
import java.util.List;

/**
 * Created by alfred on 12 October 2020
 */
public interface ReportsService {
    List<PaymentReportEntry> getPaymentReportEntries(PaymentReportCriteriaForm criteriaForm);
    List<AssetReportEntry> getAssetReportEntries(AssetReportCriteriaForm criteriaForm);
}
