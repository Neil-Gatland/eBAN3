<HTML>
<HEAD>
<TITLE></TITLE>
</HEAD>
<BODY>
<%@ page import="DBUtilities.*,java.util.Enumeration,JavaUtil.*,HTMLUtil.HTMLBean"%>
<jsp:useBean id="BAN" class="DBUtilities.BANBean" scope="session"/>
<%
    //BANBean BAN = new BANBean();
    //HTMLBean HB = new HTMLBean();
    OSSChargeBANBean chBAN = new OSSChargeBANBean();
    CircuitBANBean ctBAN = new CircuitBANBean();
    OSSCustomerBANBean cuBAN = new OSSCustomerBANBean();
    CarrierBANBean caBAN = new CarrierBANBean();
    SiteBANBean siteBAN = new SiteBANBean();
    //BAN.setGlobalCustomerId(request.getParameter("EWF_HIDDEN_gcid"));
    BAN.setEdifySetCustomer(true);
//    HB.setFromEdify(true);
    BAN.setAccount_Id("");
    BAN.setDivision_Id("");
    chBAN.Reset();
    ctBAN.Reset();
    cuBAN.Reset();
    caBAN.Reset();
    siteBAN.Reset();

    session.setAttribute("PageSent",request.getRequestURI());
    //session.setAttribute("BAN", BAN);
    //session.setAttribute("HB", HB);
    session.setAttribute("chBAN", chBAN);
    session.setAttribute("ctBAN", ctBAN);
    session.setAttribute("cuBAN", cuBAN);
    session.setAttribute("caBAN", caBAN);
    session.setAttribute("siteBAN", siteBAN);
    session.setAttribute("billingTeam", "Data");
		response.sendRedirect("../OSSeBan/OSSBANMenu.jsp");
    //<jsp:forward page="../OSSeBan/OSSBANMenu.jsp"/>
%>
<%=BAN.getGlobalCustomerId()%>
</BODY>
</HTML>
