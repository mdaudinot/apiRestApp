package com.soaint.apiRestApp;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class RestRoute extends RouteBuilder{
    @Override
    public void configure() {
        // Endpoint para recibir un path param
        rest("/api")
            .get("/path/{dato}")
            .to("direct:handlePathParam")
            .get("/query")
            .to("direct:handleQueryParam")
            .post("/json")
            .to("direct:handleJsonParam");

        // Manejo del path param
        from("direct:handlePathParam")
            .process(exchange -> {
                String dato = exchange.getIn().getHeader("dato", String.class);
                if (dato == null || dato.isEmpty()) {
                    exchange.getIn().setBody("{\"error\":\"Path param 'dato' es requerido\"}");
                } else {
                    exchange.getIn().setBody("{\"dato\":\"" + dato + "\"}");
                }
            });

        // Manejo del query param
        from("direct:handleQueryParam")
            .process(exchange -> {
                String parametro = exchange.getIn().getHeader("parametro", String.class);
                if (parametro == null || parametro.isEmpty()) {
                    exchange.getIn().setBody("{\"error\":\"Query param 'parametro' es requerido\"}");
                } else {
                    exchange.getIn().setBody("{\"parametro\":\"" + parametro + "\"}");
                }
            });

        // Manejo del POST con JSON
        from("direct:handleJsonParam")
            .process(exchange -> {
                String json = exchange.getIn().getBody(String.class);
                if (json == null || json.isEmpty()) {
                    exchange.getIn().setBody("{\"error\":\"JSON con datos es requerido\"}");
                } else {
                    exchange.getIn().setBody("{\"datos\":" + json + "}");
                }
            });
    }
}
