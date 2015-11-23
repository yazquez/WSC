package adages;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/adage2")
public class Adages2 {
    public Adages2() {
    }

    @GET
    @Produces({ MediaType.TEXT_PLAIN })
    @Path("/msg")
    public String getMsg() {
        return "Hello!!";
    }

}