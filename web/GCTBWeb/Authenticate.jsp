<HTML>
<HEAD>
<TITLE></TITLE>
</HEAD>
<BODY>
<%@ page import="java.util.Enumeration,JavaUtil.*"%>
<%@ page import="java.util.GregorianCalendar"%>
<jsp:useBean id="BAN" class="DBUtilities.BANBean" scope="session"/>
<jsp:useBean id="DBA" class="DBUtilities.DBAccess" scope="session"/>
<%!
  String SQL,System="";
  Enumeration User_Details;
  Enumeration dobDetails;
  //DBAccess DBA = new DBAccess();
  JavaUtil.UserData UD = new UserData();
  StringUtil SU = new StringUtil();
	//BANBean BAN = new BANBean();
%>
<%
//DBUtilities.DBAccess,DBUtilities.BANBean,
  System=(String)session.getAttribute("System");
  //DBA.startDB();
  String logonId = request.getParameter("username");
  if (request.getParameter("fromEdify") != null)
  {
    User_Details=DBA.getGivnRefUser(logonId);
  }
  else
  {
    User_Details=DBA.getGivnRefUser(logonId,request.getParameter("userpass"));
  }
  if (User_Details.hasMoreElements())
  {
    session.setAttribute("User_Id",request.getParameter("username"));
    String billingTeam = (String)User_Details.nextElement();
    Enumeration dobDetails = DBA.getDOBInfo(billingTeam);
    BAN.setGlobalCustomerId("");
    BAN.setBillingTeam(billingTeam);
    BAN.setLastDOB((String)dobDetails.nextElement());
    BAN.setSapDate((String)dobDetails.nextElement());
    if (request.getParameter("fromEdify") != null)
    {
      BAN.setActAsLogon(request.getParameter("actAsLogon"));
    }
    else
    {
      BAN.setActAsLogon(logonId);
    }
    BAN.setRefreshCustomerGrid(true);

    if (User_Details.hasMoreElements())
    {
      GregorianCalendar gc = new GregorianCalendar();
      String[] months = {"January", "February", "March", "April", "May", "June",
        "July", "August", "September", "October", "November", "December"};
      String billMonth = months[gc.get(gc.MONTH)] + " " + gc.get(gc.YEAR);
      BAN.setBillMonth(billMonth);
      session.setAttribute("User_Name",User_Details.nextElement());
      String logonGroup = (String)User_Details.nextElement();
      BAN.setLogonGroup(logonGroup);
      session.setAttribute("Group_Name", "Account Business");
      //BAN.setRefreshCustomerGrid(true);
      session.setAttribute("Error","");
      session.setAttribute("Message","");
      BAN.setMessage("");
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
      else if (billingTeam.equals("Revenue"))
      {
      %>
        <jsp:forward page="../rs/rsDesktop.jsp"/>
      <%
      }
      else
      {
      %>
        <jsp:forward page="newDesktop.jsp"/>
      <%
      }
    }
    else
    {
      session.setAttribute("Error",DBA.getMessage());
      %>
        <jsp:forward page="Login.jsp"/>
      <%
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
