/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package WS;

import DBUtilities.DBAccess;
import JavaUtil.StoredProcParamDescriptor;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.Response;

/**
 * REST Web Service
 *
 * @author taitchison
 */
@Path("logins/{loginId}/statuslist")
public class StatusList {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of Status
     */
    public StatusList() {
    }

    /**
     * Retrieves representation of an instance of WS.Status
     * @return an instance of java.lang.String
     */
    @GET
    @Produces("application/xml")
    public Response get(@PathParam("loginId") String loginId, @QueryParam("password") String loginPwd) {
        //TODO return proper representation object
        String status = "";
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
            return Response.status(Response.Status.BAD_REQUEST).entity("invalid parameters").build();
        } else {
            DBAccess dba = new DBAccess();
            //DBA.startDB();
            StringBuffer sb = new StringBuffer();
            Enumeration userDetails = dba.getGivnRefUser(loginId, loginPwd);
            if (userDetails.hasMoreElements()) {
                java.util.ArrayList<StoredProcParamDescriptor> al = new java.util.ArrayList<StoredProcParamDescriptor>();
                StoredProcParamDescriptor sppd = new StoredProcParamDescriptor(1, "String", loginId);
                al.add(sppd);
                ResultSet rs = dba.execSQLServerStoredProc("eBAN", "Get_Status_Test_List", al);
                try {
                    sb.append("<?xml version=\"1.0\"?><statuslist>");
                    while (rs.next()) {
                        sb.append(rs.getString(1));
                    }
                    sb.append("</statuslist>");
                } catch (SQLException ex) {
                    return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
                }
                dba.closeExt();
                return Response.status(Response.Status.ACCEPTED).entity(sb.toString()).build();
            } else {
                return Response.status(Response.Status.BAD_REQUEST).entity("invalid parameters").build();
            }
        }
    }

    /**
     * PUT method for updating or creating an instance of Status
     * @param content representation for the resource
     * @return an HTTP response with content of the updated or created resource.
     */
     /*@POST
     public Response post(@PathParam("loginId") String loginId, @FormParam("password") String loginPwd, 
        @FormParam("status") String newStatus) {
        if ((loginId.indexOf("*") != -1) || (loginId.indexOf(";") != -1) ||
            (loginId.indexOf("--") != -1) || (loginId.indexOf("'") != -1) ||
            (loginId.indexOf("&#") != -1) || (loginId.indexOf(":") != -1) ||
            (loginId.indexOf("<") != -1) || (loginId.indexOf(">") != -1) ||
            (loginId.indexOf("=") != -1) || (loginId.indexOf("\"") != -1) ||
            (loginPwd.indexOf(";") != -1) ||
            (loginPwd.indexOf("--") != -1) || (loginPwd.indexOf("'") != -1) ||
            (loginPwd.indexOf("&#") != -1) || (loginPwd.indexOf(":") != -1) ||
            (loginPwd.indexOf("<") != -1) || (loginPwd.indexOf(">") != -1) ||
            (loginPwd.indexOf("=") != -1) || (loginPwd.indexOf("\"") != -1) ||
            (newStatus.indexOf("*") != -1) || (newStatus.indexOf(";") != -1) ||
            (newStatus.indexOf("--") != -1) || (newStatus.indexOf("'") != -1) ||
            (newStatus.indexOf("&#") != -1) || (newStatus.indexOf(":") != -1) ||
            (newStatus.indexOf("<") != -1) || (newStatus.indexOf(">") != -1) ||
            (newStatus.indexOf("=") != -1) || (newStatus.indexOf("\"") != -1))
        {
            return Response.status(Response.Status.BAD_REQUEST).entity("invalid parameters").build();
        } else {
            DBAccess dba = new DBAccess();
            //DBA.startDB();
            Enumeration userDetails = dba.getGivnRefUser(loginId, loginPwd);
            if (userDetails.hasMoreElements()) {
                java.util.ArrayList<StoredProcParamDescriptor> al = new java.util.ArrayList<StoredProcParamDescriptor>();
                StoredProcParamDescriptor sppd = new StoredProcParamDescriptor(1, "String", loginId);
                al.add(sppd);
                sppd = new StoredProcParamDescriptor(2, "String", newStatus);
                al.add(sppd);
                dba.execSQLServerStoredProc("eBAN", "Update_Test_Status", al);
                dba.closeExt();
                return Response.status(Response.Status.ACCEPTED).entity("status updated").build();
            } else {
                return Response.status(Response.Status.BAD_REQUEST).entity("invalid parameters").build();
            }
        }
    }*/
}
