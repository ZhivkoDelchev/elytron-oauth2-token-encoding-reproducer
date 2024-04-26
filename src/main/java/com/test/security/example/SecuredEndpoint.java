package com.test.security.example;

import jakarta.enterprise.context.*;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

@ApplicationScoped
@Path(SecuredEndpoint.ROOT_PATH)
public class SecuredEndpoint {

    static final String ROOT_PATH = "/secured";
    static final String INFO_METHOD_PATH = "/info";

    @GET
    @Path(INFO_METHOD_PATH)
    @Produces(MediaType.TEXT_PLAIN)
    public String info() {
        return "Hello world";
    }
}
