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
    session.setAttribute("formname","suspendedDataHandler");
    ButtonPressed = request.getParameter("ButtonPressed");
    RSB.setSuspendedSortOrder(request.getParameter("sortOrder"));
    if (ButtonPressed.equals("Log Off"))
    {
      %>
	<jsp:forward page="Login.jsp?type=l"/>
      <%
    }
    else if (ButtonPressed.equals("Raise Query"))
    {
      %>
        <jsp:forward page="suspendedData.jsp">
          <jsp:param name="query" value="true"/>
          <jsp:param name="fromPage" value="suspendedData"/>
        </jsp:forward>
      <%
    }
    else if (ButtonPressed.equals("View All Processes"))
    {
      RSB.populateProcessList();
      %>
        <jsp:forward page="suspendedData.jsp">
         <jsp:param name="viewProcess" value="true"/>
        </jsp:forward>
      <%
    }
    else if (ButtonPressed.equals("View As Pop-Up"))
    {
      RSB.setSuspendPopUp(true);
      %>
        <jsp:forward page="rsDesktop.jsp">
          <jsp:param name="suspendPopUp" value="true"/>
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
    else if (ButtonPressed.equals("list_UpdateWriteOff"))
    {
      //ACC.setProductCode(request.getParameter("accountNumber"));
      //ACC.setProductCode(request.getParameter("networkSource"));
      RSB.setSuspendedAccountNumber(request.getParameter("accountNumber"));
      RSB.setSuspendedProduct(request.getParameter("product"));
      RSB.setSuspendedInd(request.getParameter("suspended"));
      RSB.setSuspendedWriteOff(request.getParameter("writeOff"));
      RSB.setSuspendedDuration(request.getParameter("duration"));
      RSB.setMode("Confirm");
      RSB.setMessage("<font color=blue><b>Select 'Confirm' from the " +
        "'Options' menu above reverse the Written Off status, or 'Cancel' to abort the operation</b></font>");
      %>
        <jsp:forward page="suspendedData.jsp"/>
      <%
    }
    else if (ButtonPressed.equals("Cancel"))
    {
      RSB.setSuspendedAccountNumber(request.getParameter(""));
      RSB.setSuspendedProduct(request.getParameter(""));
      RSB.setSuspendedInd(request.getParameter(""));
      RSB.setSuspendedWriteOff(request.getParameter(""));
      RSB.setMode("");
      %>
	<jsp:forward page="suspendedData.jsp"/>
      <%
    }
    else if (ButtonPressed.equals("Confirm"))
    {
      RSB.updateRQR09SuspenseWrittenOff();
      String msg = RSB.getMessage();
      //RSB.setSuspendedAccountNumber(request.getParameter(""));
      //RSB.setSuspendedProduct(request.getParameter(""));
      RSB.setSuspendedInd(request.getParameter(""));
      RSB.setSuspendedWriteOff(request.getParameter(""));
      RSB.setMode("");
      //RSB.setRefreshSuspendedGrid(true);
      RSB.populateSuspendedDataList();
      RSB.setMessage(msg);
      %>
	<jsp:forward page="suspendedData.jsp"/>
      <%
    }
    else if (ButtonPressed.equals("Show Incorrectly Posted Only"))
    {
      RSB.setSuspendedType("X");
      //RSB.setRefreshSuspendedGrid(true);
      RSB.populateSuspendedDataList();
      %>
	<jsp:forward page="suspendedData.jsp"/>
      <%
    }
    else if (ButtonPressed.equals("Show Unrateable Only"))
    {
      RSB.setSuspendedType("R");
      //RSB.setRefreshSuspendedGrid(true);
      RSB.populateSuspendedDataList();
      %>
	<jsp:forward page="suspendedData.jsp"/>
      <%
    }
    else if (ButtonPressed.equals("Show Uninvoiceable Only"))
    {
      RSB.setSuspendedType("I");
      //RSB.setRefreshSuspendedGrid(true);
      RSB.populateSuspendedDataList();
      %>
	<jsp:forward page="suspendedData.jsp"/>
      <%
    }
    else if (ButtonPressed.equals("Show Written-Off Only"))
    {
      RSB.setSuspendedType("wro");
      //RSB.setRefreshSuspendedGrid(true);
      RSB.populateSuspendedDataList();
      %>
	<jsp:forward page="suspendedData.jsp"/>
      <%
    }
    else if (ButtonPressed.equals("Show All"))
    {
      RSB.setSuspendedType("all");
      //RSB.setRefreshSuspendedGrid(true);
      RSB.populateSuspendedDataList();
      %>
	<jsp:forward page="suspendedData.jsp"/>
      <%
    }
    else if (ButtonPressed.equals("Refresh"))
    {
      RSB.setSuspendedAccountNumber(request.getParameter(""));
      RSB.setSuspendedProduct(request.getParameter(""));
      RSB.setSuspendedInd(request.getParameter(""));
      RSB.setSuspendedWriteOff(request.getParameter(""));
      //RSB.setRefreshSuspendedGrid(true);
      RSB.populateSuspendedDataList();
      %>
	<jsp:forward page="suspendedData.jsp"/>
      <%
    }
    else
    {//No button pressed, so must be a Refresh
      RSB.setSuspendedAccountNumber(request.getParameter(""));
      RSB.setSuspendedProduct(request.getParameter(""));
      RSB.setSuspendedInd(request.getParameter(""));
      RSB.setSuspendedWriteOff(request.getParameter(""));
      //RSB.setRefreshSuspendedGrid(true);
      RSB.populateSuspendedDataList();
      %>
      <jsp:forward page="suspendedData.jsp"/>
      <%
    }
%>
</body>
</html>
