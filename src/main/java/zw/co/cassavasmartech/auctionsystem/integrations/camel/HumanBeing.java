package zw.co.cassavasmartech.auctionsystem.integrations.camel;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

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
