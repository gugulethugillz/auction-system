package zw.co.cassavasmartech.auctionsystem.integrations.camel;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Created by alfred on 22 October 2020
 */
@Data
public class ProtocolMessage {
    private String channel;
    private String message;
}
