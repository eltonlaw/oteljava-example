package oteljava.example;

import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.exporter.otlp.trace.OtlpGrpcSpanExporter;
import io.opentelemetry.sdk.OpenTelemetrySdk;
import io.opentelemetry.sdk.autoconfigure.OpenTelemetryResourceAutoConfiguration;
import io.opentelemetry.sdk.trace.SdkTracerProvider;
import io.opentelemetry.sdk.trace.export.BatchSpanProcessor;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

/**
 * All SDK management takes place here, away from the instrumentation code, which should only access
 * the OpenTelemetry APIs.
 */
public final class Configuration {

  /**
   * Adds a BatchSpanProcessor initialized with OtlpGrpcSpanExporter to the TracerSdkProvider.
   *
   * @return a ready-to-use {@link OpenTelemetry} instance.
   */
  static OpenTelemetry initOpenTelemetry() {
    OtlpGrpcSpanExporter spanExporter =
        OtlpGrpcSpanExporter.builder().setTimeout(2, TimeUnit.SECONDS).build();
    BatchSpanProcessor spanProcessor =
        BatchSpanProcessor.builder(spanExporter)
            .setScheduleDelay(100, TimeUnit.MILLISECONDS)
            .build();

    SdkTracerProvider tracerProvider =
        SdkTracerProvider.builder()
            .addSpanProcessor(spanProcessor)
            .setResource(OpenTelemetryResourceAutoConfiguration.configureResource())
            .build();
    OpenTelemetrySdk openTelemetrySdk =
        OpenTelemetrySdk.builder().setTracerProvider(tracerProvider).buildAndRegisterGlobal();

    Runtime.getRuntime().addShutdownHook(new Thread(tracerProvider::close));

    return openTelemetrySdk;
  }
}
