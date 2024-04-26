package com.test.security.example;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.common.ConsoleNotifier;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;
import org.jboss.logging.Logger;

import java.util.HashMap;
import java.util.Map;

public class IntrospectionMockServer implements QuarkusTestResourceLifecycleManager {

    public static WireMockServer MOCK_SERVER;

    private static final Logger LOGGER = Logger.getLogger(IntrospectionMockServer.class);

    public static final String INTROSPECT = "/introspect";

    @Override
    public Map<String, String> start() {
        HashMap<String, String> config = new HashMap<>();

        MOCK_SERVER = new WireMockServer(WireMockConfiguration.options().dynamicPort().notifier(new ConsoleNotifier(true)));
        MOCK_SERVER.start();

        String idpURL = MOCK_SERVER.baseUrl();

        WireMock.configureFor(MOCK_SERVER.port());
        WireMock.stubFor(WireMock.post(IntrospectionMockServer.INTROSPECT)
                .willReturn(WireMock.aResponse().withBody("{\"active\":false}")));

        config.put("quarkus.oauth2.introspection-url", idpURL + INTROSPECT);

        return config;
    }

    @Override
    public void stop() {
        if (MOCK_SERVER != null) {
            MOCK_SERVER.stop();
        }
    }

}
