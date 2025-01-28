package hu.rmegyesi.rmbk.extension.userstorage.soap.client;

import io.opentelemetry.api.OpenTelemetry;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import org.apache.cxf.tracing.opentelemetry.OpenTelemetryClientFeature;

@Named
@ApplicationScoped
public class TracingSupport {

    public static final String INSTRUMENTATION_SCOPE = "io.quarkiverse.cxf";

    @Inject
    OpenTelemetry openTelemetry;

    @Getter
    private OpenTelemetryClientFeature clientFeature;

    @PostConstruct
    void init() {
        this.clientFeature = new OpenTelemetryClientFeature(openTelemetry, INSTRUMENTATION_SCOPE);
    }
}
