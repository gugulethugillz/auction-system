package zw.co.cassavasmartech.auctionsystem.integrations.camel;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import zw.co.cassavasmartech.auctionsystem.repository.HumanBeingRepository;

import java.util.List;

/**
 * Created by alfred on 22 October 2020
 */
@Component
public class HumanRecordResponseGenerator implements Processor {
    @Autowired
    private HumanBeingRepository humanBeingRepository;

    @Override
    public void process(Exchange exchange) throws Exception {
        List<HumanBeing> humanBeings = humanBeingRepository.findAll();
        exchange.getIn().setBody(humanBeings);
    }
}
