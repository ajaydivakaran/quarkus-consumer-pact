package me.spike;


import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@RegisterRestClient
public interface GreetingRestClient {

    @GET
    @Path("/hello")
    Greeting getGreeting();
}
