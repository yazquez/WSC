package com.mkyong.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

@Path("/hello")
public class HelloWorldService {

    @GET
    @Path("/{param}")
    public Response getMsg(@PathParam("param") String msg) {
        String output = "Jersey say : " + msg;

        return Response.status(200).entity(output).build();
    }

    @GET
    @Path("/query")
    public Response getUsers(@QueryParam("from") int from, @QueryParam("to") int to) {

        return Response.status(200).entity("getUsers is called, from : " + from + ", to : " + to).build();
    }

    @GET
    @Path("/add/{num1}/{num2}")
    public Response add(@PathParam("num1") int num1, @PathParam("num2") int num2) {
        String output = new Integer(num1 + num2).toString();
        return Response.status(200).entity("The result is : " + output).build();
    }

}
