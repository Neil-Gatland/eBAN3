/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package WS;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import DBUtilities.DBAccess;
import java.util.Enumeration;
/**
 * REST Web Service
 *
 * @author taitchison
 */
@Path("logins/{loginId}")
public class Login {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of Authenticate
     */
    public Login() {
    }

    /**
     * Retrieves representation of an instance of WS.Authenticate
     * @return an instance of java.lang.String
     
    @GET
    @Produces("application/xml")
    public String getXml() {
        //TODO return proper representation object
        throw new UnsupportedOperationException();
    }*/

    /**
     * PUT method for updating or creating an instance of Authenticate
     * @param content representation for the resource
     * @return an HTTP response with content of the updated or created resource.
     
    @PUT
    @Consumes("application/xml")
    public void putXml(String content) {
    }*/
    @POST
    //@Consumes("text/plain")
    public Response post(@PathParam("loginId") String loginId, @FormParam("password") String loginPwd) {
        if ((loginId.indexOf("*") != -1) || (loginId.indexOf(";") != -1) ||
            (loginId.indexOf("--") != -1) || (loginId.indexOf("'") != -1) ||
            (loginId.indexOf("&#") != -1) || (loginId.indexOf(":") != -1) ||
            (loginId.indexOf("<") != -1) || (loginId.indexOf(">") != -1) ||
            (loginId.indexOf("=") != -1) || (loginId.indexOf("\"") != -1) ||
            (loginPwd.indexOf(";") != -1) ||
            (loginPwd.indexOf("--") != -1) || (loginPwd.indexOf("'") != -1) ||
            (loginPwd.indexOf("&#") != -1) || (loginPwd.indexOf(":") != -1) ||
            (loginPwd.indexOf("<") != -1) || (loginPwd.indexOf(">") != -1) ||
            (loginPwd.indexOf("=") != -1) || (loginPwd.indexOf("\"") != -1))
        {
            return Response.status(Response.Status.BAD_REQUEST).entity("invalid login parameters").build();
        } else {
            DBAccess DBA = new DBAccess();
            //DBA.startDB();
            Enumeration userDetails = DBA.getGivnRefUser(loginId, loginPwd);
            if (userDetails.hasMoreElements()) {
                return Response.status(Response.Status.ACCEPTED).entity("login successful").build();
            } else {
                return Response.status(Response.Status.BAD_REQUEST).entity("invalid login parameters").build();
            }
        }
    }
}
