<HTML>
<HEAD>
<TITLE></TITLE>
</HEAD>
<BODY>
<%@ page isThreadSafe="false" %>
<%@ page import="DBUtilities.*,java.util.Enumeration,JavaUtil.*" session="true"%>
<jsp:useBean id="BAN" class="DBUtilities.BANBean" scope="session"/>
<!--jsp:useBean id="chBAN" class="DBUtilities.OSSChargeBANBean" scope="session"/-->
<jsp:useBean id="ctBAN" class="DBUtilities.CircuitBANBean" scope="session"/>
<jsp:useBean id="cuBAN" class="DBUtilities.OSSCustomerBANBean" scope="session"/>
<jsp:useBean id="caBAN" class="DBUtilities.CarrierBANBean" scope="session"/>
<jsp:useBean id="siteBAN" class="DBUtilities.SiteBANBean" scope="session"/>
<%
    session.setAttribute("User_Id",request.getParameter("EWF_HIDDEN_username"));
    String SQL;
    Enumeration User_Details;
    DBAccess DBA = new DBAccess();
    String userId = request.getParameter("EWF_HIDDEN_username");
    OSSChargeBANBean chBAN = (OSSChargeBANBean)session.getAttribute("chBAN" + userId);
    if (chBAN == null)
    {
      chBAN = new OSSChargeBANBean();
      session.setAttribute("chBAN" + userId, chBAN);
    }
    chBAN.Reset();
    ctBAN.Reset();
    cuBAN.Reset();
    caBAN.Reset();
    siteBAN.Reset();
    session.setAttribute("System","OSS");
    session.setAttribute("Group_Name",request.getParameter("EWF_HIDDEN_groupname"));
    SQL="Select User_Name from eban..eBan_User where Login_Id = '"+(String)request.getParameter("EWF_HIDDEN_username")+"'";
    DBA.startDB();
    session.setAttribute("User_Name",DBA.getValue(SQL,"P5"));
    session.setAttribute("DBA",DBA);
%>
        <jsp:forward page="Welcome.jsp"/>
</BODY>
</HTML>
