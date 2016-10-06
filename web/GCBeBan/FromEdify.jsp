<HTML>
<HEAD>
<TITLE></TITLE>
</HEAD>
<BODY>
<%@ page import="DBUtilities.DBAccess,java.util.Enumeration,JavaUtil.*" session="true"%>
<jsp:useBean id="BAN" class="DBUtilities.BANBean" scope="session"/>
<jsp:useBean id="HB" class="HTMLUtil.HTMLBean" scope="session"/>
<jsp:useBean id="DBA" class="DBUtilities.DBAccess" scope="session"/>
<%!
  String SQL;
  Enumeration User_Details;
  String System = "GCB";
%>
<%
    session.setAttribute("System",System);
    session.setAttribute("User_Id",request.getParameter("EWF_HIDDEN_username"));
    session.setAttribute("Act_As_User",request.getParameter("EWF_HIDDEN_actasuser"));
    User_Details=DBA.getUser(System,(String)request.getParameter("EWF_HIDDEN_username"));
    if (User_Details.hasMoreElements())
    {
      session.setAttribute("User_Name",User_Details.nextElement());
      User_Details.nextElement(); //not used here
      session.setAttribute("Group_Name",User_Details.nextElement());
    }
    HB.setFromEdify(true);
    session.setAttribute("billingTeam", "Data");
    session.setAttribute("DBA", DBA);
%>
        <jsp:forward page="CustomerBANMenu.jsp"/>
</BODY>
</HTML>
