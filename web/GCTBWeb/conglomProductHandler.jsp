<html>
<head>
</head>
<body>
<%@ page import="JavaUtil.StringUtil"%>
<%@ page import="java.net.URLEncoder"%>
<jsp:useBean id="BAN" class="DBUtilities.BANBean" scope="session"/>
<jsp:useBean id="HB" class="HTMLUtil.HTMLBean" scope="session"/>
<%! String ButtonPressed, banId, listType;%>
<%
    ButtonPressed = request.getParameter("ButtonPressed");
    if (ButtonPressed.equals("Log Off"))
    {
      %>
	<jsp:forward page="Login.jsp?type=l"/>
      <%
    }
    else if (ButtonPressed.equals("Conglom Desktop"))
    {
      BAN.setRefreshCustomerGrid(true);
      %>
        <jsp:forward page="conglomDesktop.jsp"/>
      <%
    }
    else if (ButtonPressed.equals("Raise Query"))
    {
      %>
        <jsp:forward page="conglomProduct.jsp">
         <jsp:param name="query" value="true"/>
          <jsp:param name="fromPage" value="conglomProduct"/>
        </jsp:forward>
      <%
    }
    else if (ButtonPressed.equals("Show Bill Profile"))
    {
      %>
        <jsp:forward page="conglomProduct.jsp">
         <jsp:param name="billProfile" value="true"/>
         <jsp:param name="conglom" value="true"/>
        </jsp:forward>
      <%
    }
    else //refresh
    {
      %>
	<jsp:forward page="conglomProduct.jsp"/>
      <%
    }
%>
</body>
</html>
