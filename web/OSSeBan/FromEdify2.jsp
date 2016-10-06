<HTML>
<HEAD>
<TITLE></TITLE>
</HEAD>
<BODY>
<%@ page import="java.util.Enumeration,JavaUtil.*,HTMLUtil.HTMLBean"%>
<jsp:useBean id="BAN" class="DBUtilities.BANBean" scope="session"/>
<jsp:useBean id="DBA" class="DBUtilities.DBAccess" scope="session"/>
<jsp:useBean id="HB" class="HTMLUtil.HTMLBean" scope="session"/>
<%
    /*session = request.getSession(false);
    if (session != null)
      session.invalidate();
    session = request.getSession(true);*/
    /*BAN = new BANBean();
    HB = new HTMLBean();
    chBAN = new OSSChargeBANBean();
    ctBAN = new CircuitBANBean();
    cuBAN = new OSSCustomerBANBean();
    caBAN = new CarrierBANBean();
    siteBAN = new SiteBANBean();
    DBA = new DBAccess();*/
    String SQL;
    Enumeration User_Details;
    String System = "OSS";
    session.setAttribute("System",System);
    session.setAttribute("User_Id",request.getParameter("EWF_HIDDEN_username"));
    //session.setAttribute("Group_Name",request.getParameter("EWF_HIDDEN_groupname"));
    //SQL="Select User_Name from eban..eBan_User where Login_Id = '"+(String)request.getParameter("EWF_HIDDEN_username")+"'";
    //DBA.startDB();
    //session.setAttribute("User_Name",DBA.getValue(SQL,"P5"));
    User_Details=DBA.getUser(System,(String)request.getParameter("EWF_HIDDEN_username"));
    if (User_Details.hasMoreElements())
    {
      session.setAttribute("User_Name",User_Details.nextElement());
      User_Details.nextElement(); //not used here
      session.setAttribute("Group_Name",User_Details.nextElement());
    }

    /*chBAN.Reset();
    ctBAN.Reset();
    cuBAN.Reset();
    caBAN.Reset();
    siteBAN.Reset();*/
    BAN.setGlobalCustomerId(request.getParameter("EWF_HIDDEN_gcid"));
    BAN.setEdifySetCustomer(true);
    HB.setFromEdify(true);
    BAN.setAccount_Id("");
    BAN.setDivision_Id("");

    session.setAttribute("PageSent",request.getRequestURI());
    String type = request.getParameter("EWF_HIDDEN_type");
    session.setAttribute("billingTeam", "Data");
    //session.setAttribute("DBA",DBA);
    if (type == null)
    {
%>
        <jsp:forward page="OSSBANMenu.jsp"/>
<%  }
    else if (type.equals("GD"))
    {
%>
        <jsp:forward page="GDBANMenu.jsp"/>
<%  }
    else if (type.equals("QUERY"))
    {
      session.setAttribute("billingTeam", request.getParameter("billingTeam"));
%>
        <jsp:forward page="../query/addQuery.jsp"/>
<%  }
%>
</BODY>
</HTML>
