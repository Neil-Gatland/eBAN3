<html>
<head>
</head>
<body>
<%! String ButtonPressed;%>
<%
    ButtonPressed=(String)request.getParameter("ButtonPressed");

    if (ButtonPressed.startsWith("Connect"))
    {
      %>
	<jsp:forward page="DesktopLogon.jsp"/>
      <%
    }
%>
</body>
</html>