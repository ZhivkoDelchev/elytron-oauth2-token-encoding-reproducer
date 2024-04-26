package com.test.security.example;

import io.quarkus.test.junit.QuarkusTestProfile;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SecuredEndpointTestProfile implements QuarkusTestProfile {

    @Override
    public Map<String, String> getConfigOverrides() {
        return new HashMap<>();
    }

    @Override
    public List<TestResourceEntry> testResources() {
        return Arrays.asList(new TestResourceEntry(IntrospectionMockServer.class));
    }
}
