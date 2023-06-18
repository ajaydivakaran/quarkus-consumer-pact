package me.spike;


import org.eclipse.microprofile.rest.client.annotation.RegisterProvider;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

@RegisterRestClient
@RegisterProvider(value = InvalidGreetingHandler.class)
public interface GreetingRestClient {

    @GET
    @Path("/hello")
    Greeting getGreeting(@QueryParam("firstName") String firstName);
}
