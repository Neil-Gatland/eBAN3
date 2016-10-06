<html>
<head>
</head>
<%@ page import="DBUtilities.*,JavaUtil.StringUtil"%>

<jsp:useBean id="BAN" class="DBUtilities.BANBean" scope="session"/>

<%! JavaUtil.StringUtil SU = new JavaUtil.StringUtil();
    String ButtonPressed,System;
%>
<%
    ButtonPressed=request.getParameter("ButtonPressed");
    System=(String)session.getAttribute("System");
    BAN.setExceptionTypeForList(request.getParameter("Exception_Type"));
    BAN.setExceptionStatusForList(request.getParameter("Exception_Status"));
    BAN.setIRINForList(request.getParameter("IRIN"));
    BAN.setGSR_for_List(request.getParameter("GSR"));

    if (ButtonPressed.startsWith("Desktop"))
    {
      %>
	<jsp:forward page="newDesktop.jsp"/>
      <%
    }
    else if (ButtonPressed.equals("list_Update"))
    {
      BAN.setExceptionId(Integer.parseInt(request.getParameter("exId")));
      BAN.setExceptionType(Integer.parseInt(request.getParameter("exType")));
      BAN.setExceptionIRIN(request.getParameter("exIRIN"));
      BAN.setExceptionStatus(request.getParameter("exStatus"));
      %>
        <jsp:forward page="updateException.jsp"/>
      <%
    }
    else if (ButtonPressed.equals("Log Off"))
    {
      %>
	<jsp:forward page="Login.jsp?type=l"/>
      <%
    }
    else if (ButtonPressed.equals("Raise Query"))
    {
      %>
        <jsp:forward page="ListExceptions.jsp">
         <jsp:param name="query" value="true"/>
          <jsp:param name="fromPage" value="ListExceptions"/>
        </jsp:forward>
      <%
    }
    else if (ButtonPressed.startsWith("Reset"))
    {
      BAN.setExceptionTypeForList("All");
      BAN.setExceptionStatusForList("All");
      BAN.setIRINForList("All");
      BAN.setGSR_for_List("All");
      %>
	<jsp:forward page="ListExceptions.jsp"/>
      <%
    }
		else
    {//All other options are from the List requests menu
      //if (ButtonPressed.compareTo("")!=0){BAN.setBANs_to_List(ButtonPressed);}
      %>
	<jsp:forward page="ListExceptions.jsp"/>
      <%
    }
    %>
</body>
</html>
