package zw.co.cassavasmartech.auctionsystem.integrations.camel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PersonEntry {
    private Long id;
    private String firstName;
    private String lastName;

    public HumanBeing getPerson() {
        final HumanBeing person = new HumanBeing();
        person.setId(id);
        person.setFirstName(firstName);
        person.setLastName(lastName);
        return person;
    }
}
