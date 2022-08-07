package me.spike;

import au.com.dius.pact.consumer.dsl.PactDslJsonBody;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.core.model.PactSpecVersion;
import au.com.dius.pact.core.model.RequestResponsePact;
import au.com.dius.pact.core.model.V4Pact;
import au.com.dius.pact.core.model.annotations.Pact;
import io.quarkus.test.junit.QuarkusTest;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.inject.Inject;

@QuarkusTest
@ExtendWith(PactConsumerTestExt.class)
@PactTestFor(providerName = "Provider", pactVersion = PactSpecVersion.V4)
public class ConsumerPactTest {

    @Inject
    @RestClient
    GreetingRestClient greetingService;

    @Pact(provider = "Provider", consumer = "Consumer")
    public V4Pact greet(PactDslWithProvider builder) {
        return builder
                .uponReceiving("greet")
                .path("/hello")
                .method("GET")
                .willRespondWith()
                .status(200)
                .body(new PactDslJsonBody()
                        .stringType("greeting")
                )
                .toPact(V4Pact.class); //Reference - https://github.com/pact-foundation/pact-jvm/issues/1488
    }

    @Test
    @PactTestFor(pactMethod = "greet", port = "8080")
    void testHelloEndpoint() {
        Greeting actualGreeting = greetingService.getGreeting();
        System.out.println(actualGreeting.greeting);
        Assertions.assertNotNull(actualGreeting.greeting);
    }

}
