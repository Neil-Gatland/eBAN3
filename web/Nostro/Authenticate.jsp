<HTML>
<HEAD>
<TITLE></TITLE>
</HEAD>
<BODY>
<%@ page import="DBUtilities.DBAccess,java.util.Enumeration,JavaUtil.*"%>
<jsp:useBean id="BAN" class="DBUtilities.BANBean" scope="request"/>
<%!
  String SQL,System="";
  Enumeration User_Details;
  DBAccess DBA = new DBAccess();
  JavaUtil.UserData UD = new UserData();
  StringUtil SU = new StringUtil();
%>
<%
  System=(String)session.getAttribute("System");
  DBA.startDB();
  User_Details=DBA.getUser(System,(String)request.getParameter("username"),(String)request.getParameter("userpass"));
  if (User_Details.hasMoreElements())
  {
    session.setAttribute("User_Id",request.getParameter("username"));
    session.setAttribute("Group_Name",User_Details.nextElement());
    if (User_Details.hasMoreElements())
    {
      session.setAttribute("User_Name",User_Details.nextElement());
      session.setAttribute("Error","");
      %>
        <jsp:forward page="Welcome.jsp"/>
      <%
    }
    else
    {
      session.setAttribute("Error",DBA.getMessage());
      %>
        <jsp:forward page="eBAN.jsp"/>
      <%
    }
  }
  else
  {
    session.setAttribute("Error",DBA.getMessage());
    %>
      <jsp:forward page="eBAN.jsp"/>
    <%
  }
  %>
</BODY>
</HTML>
