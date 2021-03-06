/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package oteljava.example;

import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.context.Scope;

public class App {
    public String getGreeting() {
        return "Hello World!";
    }

    static private void trySleep(int ms) {
        try {
            Thread.sleep(ms);
        } catch(InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }

    public static void main(String[] args) {
        System.setProperty("otel.resource.attributes", "service.name=OtlpExporterExample");
        // it is important to initialize your SDK as early as possible in your application's lifecycle
        OpenTelemetry openTelemetry = Configuration.initOpenTelemetry();
        Tracer tracer = openTelemetry.getTracer("oteljava.example", "1.0.0");
        System.out.println(new App().getGreeting());

        for (int i = 0; i < 4; i++) {
            Span span = tracer.spanBuilder("exampleSpan").startSpan();
            System.out.format("new span: %d\n", i);
            try (Scope scope = span.makeCurrent()) {
                span.setAttribute("good", "true");
                span.setAttribute("exampleNumber", i);
                trySleep(300);
            } finally {
                span.end();
            }
        }
        trySleep(2000);
    }
}
