package zw.co.cassavasmartech.auctionsystem.integrations.camel;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
public class ProtocolMessage {
    private String channel;
    private String message;
}
