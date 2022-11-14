package zw.co.cassavasmartech.auctionsystem.common;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/*
Created by alfred on 01 Sep 2020
*/
@Data
@Component
@ConfigurationProperties(prefix="auction.system")
public class AuctionSystemProperties {
    private String paynowIntegrationKey;
    private String paynowIntegrationId;
    private String fileBaseLocation;
    private String images;
    private String documents;
    private String pdfDocuments;
    private String videos;
    private String miscFiles;
    private String zipFiles;
    private String audio;
}
