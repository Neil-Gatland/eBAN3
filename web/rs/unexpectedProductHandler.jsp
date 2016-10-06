<html>
<head>
</head>
<body>
<%@ page import="JavaUtil.StringUtil"%>
<%@ page import="java.util.Enumeration"%>

<jsp:useBean id="BAN" class="DBUtilities.BANBean" scope="session"/>
<jsp:useBean id="HB" class="HTMLUtil.HTMLBean" scope="session"/>
<jsp:useBean id="RSB" class="DBUtilities.RevenueShareBean" scope="session"/>
<%! String ButtonPressed, banId, listType;%>
<%
    ButtonPressed = request.getParameter("ButtonPressed");
    if (ButtonPressed.equals("Log Off"))
    {
      %>
	<jsp:forward page="Login.jsp?type=l"/>
      <%
    }
    else if (ButtonPressed.equals("Raise Query"))
    {
      %>
        <jsp:forward page="unexpectedProduct.jsp">
          <jsp:param name="query" value="true"/>
          <jsp:param name="fromPage" value="unexpectedProduct"/>
        </jsp:forward>
      <%
    }
    else if (ButtonPressed.equals("Download List"))
    {
      //RSB.populateCustInvReportList();
      %>
        <jsp:forward page="unexpectedProduct.jsp">
          <jsp:param name="downloadList" value="Unexpected_Product_NTS"/>
        </jsp:forward>
      <%
    }
    else if (ButtonPressed.equals("Revenue Share Desktop"))
    {
      RSB.canCloseMonth();
      %>
        <jsp:forward page="rsDesktop.jsp"/>
      <%
    }
    else if (ButtonPressed.equals("Refresh"))
    {
      RSB.populateUnexpectedProductList();
      %>
	<jsp:forward page="unexpectedProduct.jsp"/>
      <%
    }
    else
    {//No button pressed, so must be a Refresh
      RSB.populateUnexpectedProductList();
      %>
      <jsp:forward page="unexpectedProduct.jsp"/>
      <%
    }
%>
</body>
</html>
