package zw.co.cassavasmartech.auctionsystem.integrations.camel;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Created by alfred on 22 October 2020
 */
@Data
@Entity
public class HumanBeing {
    @Id
    private Long id;
    private String firstName;
    private String lastName;

    public HumanBeing() {
    }
}
