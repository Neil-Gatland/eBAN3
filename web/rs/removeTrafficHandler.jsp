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
    Enumeration FormFields = request.getParameterNames();
    String FormField = null;
    session.setAttribute("formname","removeTrafficHandler");
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
        <jsp:forward page="removeTraffic.jsp">
          <jsp:param name="query" value="true"/>
          <jsp:param name="fromPage" value="removeTraffic"/>
        </jsp:forward>
      <%
    }
    else if (ButtonPressed.equals("View All Processes"))
    {
      RSB.populateProcessList();
      %>
        <jsp:forward page="removeTraffic.jsp">
         <jsp:param name="viewProcess" value="true"/>
        </jsp:forward>
      <%
    }
    else if (ButtonPressed.equals("Download List"))
    {
      RSB.populateTrafficList();
      %>
        <jsp:forward page="removeTraffic.jsp">
          <jsp:param name="downloadList" value="Incorrectly_Posted_Traffic"/>
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
    else if (ButtonPressed.equals("list_Remove"))
    {
      RSB.getLatestProcessRowByName("Summarised Call Data Rating");
      String status = RSB.getNameProcessStatus();
      String accountNumber = request.getParameter("accountNumber");
      String networkSource = request.getParameter("networkSource");
      if ((status.equals("Requested")) || (status.equals("Running")) ||
        (status.equals("Completed")))
      {
        RSB.setMessage("<font color=blue><b>This item can no longer be removed due to the running of the 'Summarised Call Data Rating' process.</b></font>");
      }
      else
      {
        RSB.updateIncorrectlyPostedIndicator(accountNumber, networkSource,
          request.getParameter("newValue"));
        RSB.populateTrafficList();
      }
      %>
      <jsp:forward page="removeTraffic.jsp">
        <jsp:param name="key" value="<%=accountNumber+networkSource%>"/>
      </jsp:forward>
      <%
    }
    else if (ButtonPressed.equals("Show Incorrectly Posted Only"))
    {
      RSB.setIncorrectlyPosted("Y");
      RSB.populateTrafficList();
      %>
	<jsp:forward page="removeTraffic.jsp"/>
      <%
    }
    else if (ButtonPressed.equals("Show Correctly Posted Only"))
    {
      RSB.setIncorrectlyPosted("N");
      RSB.populateTrafficList();
      %>
	<jsp:forward page="removeTraffic.jsp"/>
      <%
    }
    else if (ButtonPressed.equals("Show All"))
    {
      RSB.setIncorrectlyPosted("all");
      RSB.populateTrafficList();
      %>
	<jsp:forward page="removeTraffic.jsp"/>
      <%
    }
    else if (ButtonPressed.equals("Refresh"))
    {
      RSB.populateTrafficList();
      %>
	<jsp:forward page="removeTraffic.jsp"/>
      <%
    }
    else
    {//No button pressed, so must be a Refresh
      RSB.populateTrafficList();
      %>
      <jsp:forward page="removeTraffic.jsp"/>
      <%
    }
%>
</body>
</html>
