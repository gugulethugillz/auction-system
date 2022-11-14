package zw.co.cassavasmartech.auctionsystem.integrations.camel;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;

/**
 * Created by alfred on 22 October 2020
 */
@Component
public class PersonRecordProcessor implements Processor {
    @Override
    public void process(Exchange exchange) throws Exception {
        final String body = exchange.getIn().getBody(String.class);
        String[] tokens = body.split(",");

        final HumanBeing person = new HumanBeing();
        person.setId(Long.parseLong(tokens[0]));
        person.setFirstName(tokens[1]);
        person.setLastName(tokens[2]);

        exchange.getIn().setBody(person);
    }
}
