package zw.co.cassavasmartech.auctionsystem.common;

import org.springframework.web.multipart.MultipartFile;
import zw.co.cassavasmartech.auctionsystem.common.enums.EntityStatus;
import zw.co.cassavasmartech.auctionsystem.common.enums.ResourceType;
import zw.co.cassavasmartech.auctionsystem.model.Asset;
import zw.co.cassavasmartech.auctionsystem.model.PaymentRequest;
import zw.co.cassavasmartech.auctionsystem.model.StatusHistory;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import java.util.UUID;

public interface Utils {
    DateTimeFormatter DATE_ONLY_FORMATTER = DateTimeFormatter.ofPattern("dd MMMM yyyy");
    DateTimeFormatter TIME_ONLY_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");
    DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd MMMM yyyy HH:mm");
    DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#.##");


    static String generateFileName(String filename) {
        final String fileExtension = getFileExtension(filename);
        return String.format("%s%s", UUID.randomUUID().toString(), fileExtension);
    }

    static String formatAmount(BigDecimal amount) {
        return DECIMAL_FORMAT.format(amount.doubleValue());
    }

    static String getFileExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf("."));
    }
    static String guessMimeType(String fileName) {
        String extension = getFileExtension(fileName);
        switch (extension) {
            case ".jpg":
            case ".jpeg":
                return "image/jpeg";
            case ".png":
                return "image/png";
            case ".mp4":
                return "video/mp4";
            case ".pdf":
                return "application/pdf";
            default:
                return "application/octet-steam";
        }
    }

    static String formatToDateOnly(OffsetDateTime offsetDateTime) {
        return DATE_ONLY_FORMATTER.format(offsetDateTime);
    }

    static String formatToTimeOnly(OffsetDateTime offsetDateTime) {
        return TIME_ONLY_FORMATTER.format(offsetDateTime);
    }

    static String format(OffsetDateTime offsetDateTime) {
        return DATE_TIME_FORMATTER.format(offsetDateTime);
    }

    static String formatToDateOnly(LocalDateTime offsetDateTime) {
        return DATE_ONLY_FORMATTER.format(offsetDateTime);
    }

    static String formatToTimeOnly(LocalDateTime localDateTime) {
        return TIME_ONLY_FORMATTER.format(localDateTime);
    }

    static String format(LocalDateTime localDateTime) {
        return DATE_TIME_FORMATTER.format(localDateTime);
    }

    static String formatToDateOnly(LocalDate localDate) {
        return DATE_ONLY_FORMATTER.format(localDate);
    }

    static String formatToTimeOnly(LocalDate localDate) {
        return TIME_ONLY_FORMATTER.format(localDate);
    }

    static String format(LocalDate localDate) {
        return DATE_TIME_FORMATTER.format(localDate);
    }

    static String getRandomNumberString() {
        // It will generate 6 digit random Number.
        // from 0 to 999999
        Random rnd = new Random();
        int number = rnd.nextInt(999999);

        // this will convert any number sequence into 6 character.
        return String.format("%06d", number);
    }

    static String formatDescription(String description) {
        if(description == null) {
            return "";
        }
        if(description.length() < 50) {
            return description;
        }
        return description.substring(0, 50) + "...";
    }

    static ResourceType guessResourceType(MultipartFile resourceFile) {
        String originalFilename = resourceFile.getOriginalFilename();
        if(originalFilename.endsWith(".pdf")) return ResourceType.PDF_DOCUMENT;
        return ResourceType.OTHER_DOCUMENT;
    }

    static ResourceType guessResourceType(String fileName) {
        if(fileName.endsWith(".pdf")) return ResourceType.PDF_DOCUMENT;
        return ResourceType.OTHER_DOCUMENT;
    }

    static Double calculatePercentage(int value, int total) {
        if(total <= 0) return 0.0;
        return (value / (double) total) * 100.0;
    }

    static StatusHistory getPaymentRequestHelper(PaymentRequest paymentRequest, String description, String username) {
        StatusHistory statusHistory = new StatusHistory();
        statusHistory.setEntityStatus(paymentRequest.getStatus().name());
        statusHistory.setOtherStatus(paymentRequest.getTransactionStatus());
        statusHistory.setDescription(description);
        statusHistory.setPaymentRequest(paymentRequest);
        statusHistory.setDateCreated(OffsetDateTime.now());
        statusHistory.setLastUpdated(OffsetDateTime.now());
        statusHistory.setStatus(EntityStatus.ACTIVE);
        statusHistory.setCreatedBy(username);
        statusHistory.setLastUpdatedBy(username);
        return statusHistory;
    }

    static StatusHistory getPaymentRequestHelper(Asset asset, String description, String username) {
        StatusHistory statusHistory = new StatusHistory();
        statusHistory.setEntityStatus(asset.getStatus().name());
        statusHistory.setOtherStatus(description);
        statusHistory.setDescription(description);
        statusHistory.setAsset(asset);
        statusHistory.setDateCreated(OffsetDateTime.now());
        statusHistory.setLastUpdated(OffsetDateTime.now());
        statusHistory.setStatus(EntityStatus.ACTIVE);
        statusHistory.setCreatedBy(username);
        statusHistory.setLastUpdatedBy(username);
        return statusHistory;
    }
}
