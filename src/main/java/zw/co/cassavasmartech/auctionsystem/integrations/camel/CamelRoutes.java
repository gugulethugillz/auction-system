package zw.co.cassavasmartech.auctionsystem.integrations.camel;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jackson.JacksonDataFormat;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CamelRoutes extends RouteBuilder {
    private final PersonRecordProcessor processor;
    private final HumanRecordResponseGenerator responseGenerator;

    @Override
    public void configure() throws Exception {
        JacksonDataFormat jsonDataFormat = new JacksonDataFormat();
        jsonDataFormat.useList();
        jsonDataFormat.setUnmarshalType(HumanBeing.class);

        JacksonDataFormat protocolFormat = new JacksonDataFormat();
        jsonDataFormat.setUnmarshalType(ProtocolMessage.class);

        from("rabbitmq:auction-exchange?queue=auction-queue&routingKey=auction-key&autoDelete=false")
                .unmarshal(jsonDataFormat)
                .to("direct:distribute");

//        from("file:/home/alfred/camel")
//                .unmarshal(protocolFormat)
//                .choice()
//                    .when(body().convertToString().contains("SMS"))
//                        .to("direct:sms-processor")
//                    .when(body().convertToString().contains("EMAIL"))
//                        .to("direct:email-processor ")
//                .endChoice()
//                .end();

        from("jetty:http://localhost:9000/people")
                .unmarshal(jsonDataFormat)
                .to("direct:distribute");

        from("file:/home/alfred/camel")
                .unmarshal(jsonDataFormat)
                .log(body().toString())
                .to("direct:distribute");

        from("direct:distribute")
                .multicast().to("direct:jpaout", "direct:log", "direct:fileout");

        from("direct:jpaout")
                .to("jpa:entityType=HumanBeing")
                .to("seda:process-response");

        from("seda:process-response")
                .removeHeaders("*")
                .process(responseGenerator)
                .log(body().toString())
                .marshal(jsonDataFormat)
                .to("rabbitmq:out-ex?queue=out-q&routingKey=out-q-key&autoDelete=false");

        from("direct:log")
                .log(body().toString());

        from("direct:fileout")
                .process(exchange -> {
                    final HumanBeing body = exchange.getIn().getBody(HumanBeing.class);
                    exchange.getIn().setBody(body.getFirstName());
                })
                .to("file:/home/alfred/target/person.txt");
    }
}
