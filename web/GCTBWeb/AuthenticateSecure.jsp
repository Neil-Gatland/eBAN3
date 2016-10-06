<HTML>
<HEAD>
<TITLE></TITLE>
</HEAD>
<BODY>
<%@ page import="java.util.Enumeration,JavaUtil.*"%>
<%@ page import="java.util.GregorianCalendar"%>
<jsp:useBean id="BAN" class="DBUtilities.BANBean" scope="session"/>
<jsp:useBean id="DBA" class="DBUtilities.DBAccess" scope="session"/>
<%
  String SQL,System="";
  Enumeration User_Details;
  Enumeration dobDetails;
  JavaUtil.UserData UD = new UserData();
  StringUtil SU = new StringUtil();
  System=(String)session.getAttribute("System");
  String logonId = request.getParameter("username");
  User_Details=BAN.getGivnRefUserSecure(logonId,request.getParameter("userpass"));
  if ((User_Details != null) && (User_Details.hasMoreElements()))
  {
    session.setAttribute("User_Id",logonId);
    String billingTeam = (String)User_Details.nextElement();
    session.setAttribute("User_Name",User_Details.nextElement());
    String logonGroup = (String)User_Details.nextElement();
    String passwordStatus = (String)User_Details.nextElement();
    if ((passwordStatus.equals("a")) || (passwordStatus.equals("b")))
    {
      %>
      <jsp:forward page="Login.jsp">
        <jsp:param name="changePass" value="true"/>
        <jsp:param name="fromLogin" value="true"/>
        <jsp:param name="keepSession" value="true"/>
        <jsp:param name="passwordStatus" value="<%=passwordStatus%>"/>
      </jsp:forward>
      <%
    }
    else if ((passwordStatus.equals("d")) || (passwordStatus.equals("e")) ||
      (passwordStatus.equals("f")))
    {
      if (passwordStatus.equals("f"))
      {
        session.setAttribute("Error","<font color=red><b>User has been suspended. Please contact Systems Support</font>");
      }
      else
      {
        session.setAttribute("Error","<font color=red><b>Invalid User Name or Password</font>");
      }
      %>
        <jsp:forward page="Login.jsp"/>
      <%
    }
    else if (request.getParameter("chgPass") != null)
    {
      %>
      <jsp:forward page="Login.jsp">
        <jsp:param name="changePass" value="true"/>
        <jsp:param name="fromLogin" value="true"/>
        <jsp:param name="keepSession" value="true"/>
      </jsp:forward>
      <%
    }
    else
    {
      dobDetails = DBA.getDOBInfo(billingTeam);
      BAN.setGlobalCustomerId("");
      BAN.setBillingTeam(billingTeam);
      BAN.setLastDOB((String)dobDetails.nextElement());
      BAN.setSapDate((String)dobDetails.nextElement());
      BAN.setActAsLogon(logonId);
      BAN.setRefreshCustomerGrid(true);

      GregorianCalendar gc = new GregorianCalendar();
      String[] months = {"January", "February", "March", "April", "May", "June",
        "July", "August", "September", "October", "November", "December"};
      String billMonth = months[gc.get(gc.MONTH)] + " " + gc.get(gc.YEAR);
      BAN.setBillMonth(billMonth);
      BAN.setLogonGroup(logonGroup);
      session.setAttribute("Group_Name", "Account Business");
      //BAN.setRefreshCustomerGrid(true);
      session.setAttribute("Error","");
      session.setAttribute("Message","");
      if (request.getParameter("passChange") == null)
      {
        BAN.setMessage("");
      }
      else
      {
        BAN.setMessage("<font color=blue>Password updated successfully</font>");
      }
      //session.setAttribute("BAN", BAN);
      //session.setAttribute("DBA", DBA);
      //different screens for billing teams
      if (billingTeam.equals("Conglom"))
      {
        //<jsp:forward page="desktopLogon.jsp"/>
        session.setAttribute("billingTeam",billingTeam);
      %>
        <jsp:forward page="conglomDesktop.jsp"/>
      <%
      }
      else if (billingTeam.equals("GSO"))
      {
      %>
        <jsp:forward page="gsoDesktop.jsp"/>
      <%
      }
      else
      {
      %>
        <jsp:forward page="newDesktop.jsp"/>
      <%
      }
    }
  }
  else
  {
    session.setAttribute("Error",DBA.getMessage());
    %>
      <jsp:forward page="Login.jsp"/>
    <%
  }
  %>
</BODY>
</HTML>
