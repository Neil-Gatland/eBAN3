/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package WS;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

/**
 * REST Web Service
 *
 * @author taitchison
 */
@Path("test")
public class Test {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of Test
     */
    public Test() {
    }

    /*@GET
    @Produces("text/html")
    public String getHtml() {
        return "<html lang=\"en\"><body><h1>In there!!</body></h1></html>";
    }*/
    @GET
    @Produces("text/html")
    public String getHtml(@QueryParam("loginId") String loginId, @QueryParam("password") String password) {
        return "<html lang=\"en\"><body><h1>loginId: " + loginId + ", password: " +
            password + "</body></h1></html>";
    }
    /**
     * PUT method for updating or creating an instance of HelloWorld
     * @param content representation for the resource
     * @return an HTTP response with content of the updated or created resource.
     */
    @PUT
    @Consumes("text/html")
    public String putHtml(String content) {
        return "<html lang=\"en\"><body><h1>" + content + " In there (put)!!</body></h1></html>";
    }
    @POST
    //@Consumes("text/plain")
    public String postHtml(@FormParam("loginId") String loginId, @FormParam("password") String password) {
        return "loginId: " + loginId + ", password: " +
            password;
    }
}
