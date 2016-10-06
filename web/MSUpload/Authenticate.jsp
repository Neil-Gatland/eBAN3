<HTML>
<HEAD>
<TITLE></TITLE>
</HEAD>
<BODY>
<%@ page import="DBUtilities.DBAccess,java.util.Enumeration,JavaUtil.*"%>
<jsp:useBean id="BAN" class="DBUtilities.BANBean" scope="session"/>
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
  String logonId = request.getParameter("username");
  BAN.setLoginId(logonId);
  User_Details=DBA.getGivnRefUser(logonId,request.getParameter("userpass"));
  if (User_Details.hasMoreElements())
  {
    session.setAttribute("User_Id",request.getParameter("username"));
    String billingTeam = (String)User_Details.nextElement(); 
    String displayName = (String)User_Details.nextElement();
    String logonGroup = (String)User_Details.nextElement();
    if ((logonGroup.equals("Managed Services"))||(logonGroup.equals("Devoteam")))
    {
        session.setAttribute("Error","");
        %>                      
            <jsp:forward page="Welcome.jsp"/>

        <%
    }   
    else
    {       
        session.setAttribute("Error","User not authorised to perform uploads");
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