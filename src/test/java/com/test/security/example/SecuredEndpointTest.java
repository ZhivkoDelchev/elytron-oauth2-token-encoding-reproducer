package com.test.security.example;

import com.github.tomakehurst.wiremock.client.WireMock;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;
import io.restassured.RestAssured;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;


import java.net.*;
import java.nio.charset.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

@TestProfile(SecuredEndpointTestProfile.class)
@QuarkusTest
public class SecuredEndpointTest {

    static final String BEARER_TOKEN = ".-xVuwy-yKLuD9A1D/ZCN8+RT6ZoxP66ebAhw84Iy3Cs31XpSTVRRNc4jMNOPLkpFfdDW6mkDtjKYBXY~kFsBNl1EDvTaGnry1yYnK";

    @AfterEach
    void tearDown() {
        IntrospectionMockServer.MOCK_SERVER.resetAll();
    }

    @Test
    void testRolesAllowed() {
        mockIntrospectionEndpoint();

        String message = RestAssured.given()
                .when()
                .header("Authorization", "Bearer " + BEARER_TOKEN)
                .get(SecuredEndpoint.ROOT_PATH + SecuredEndpoint.INFO_METHOD_PATH)
                .then()
                .statusCode(200).extract().body().asString();

        assertThat(message, is(equalTo("Hello world")));
    }

    void mockIntrospectionEndpoint() {
        var response = "{\"sub\":\"7c96f42a-d88b-11ea-87d0-0242ac130003\",\"scope\":\"*\",\"roles\":\"ROLE_CUSTOMER ROLE_USER\",\"iss\":\"services.test.com\",\"active\":true,\"token_type\":\"bearer\",\"exp\":1975951594,\"username\":\"test-manager@foo.bar\",\"customer\":\"2a3937de-d885-11ea-87d0-0242ac130003\",\"info\":{\"accountId\":\"2a3937de-d885-11ea-87d0-0242ac130003\"}}";
        IntrospectionMockServer.MOCK_SERVER.stubFor(WireMock.post(IntrospectionMockServer.INTROSPECT)
                .withRequestBody(WireMock.equalTo("token_type_hint=access_token&token=" + URLEncoder.encode(BEARER_TOKEN, StandardCharsets.UTF_8)))
                .willReturn(WireMock.aResponse()
                        .withBody(response)));
    }
}
