<html>
<head>
</head>
<body>
<%@ page import="JavaUtil.StringUtil"%>
<%@ page import="java.util.Enumeration"%>

<jsp:useBean id="BAN" class="DBUtilities.BANBean" scope="session"/>
<jsp:useBean id="HB" class="HTMLUtil.HTMLBean" scope="session"/>
<jsp:useBean id="RSB" class="DBUtilities.RevenueShareBean" scope="session"/>
<jsp:useBean id="PRO" class="DBUtilities.ProviderBean" scope="session"/>
<%! String ButtonPressed, banId, listType;%>
<%
    Enumeration FormFields = request.getParameterNames();
    String FormField = null;
    session.setAttribute("formname","missingDataIdentificationHandler");
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
        <jsp:forward page="missingDataIdentification.jsp">
          <jsp:param name="query" value="true"/>
          <jsp:param name="fromPage" value="missingDataIdentification"/>
        </jsp:forward>
      <%
    }
    else if (ButtonPressed.equals("Download List"))
    {
      RSB.populateTrafficList();
      %>
        <jsp:forward page="missingDataIdentification.jsp">
          <jsp:param name="downloadList" value="Missing_Data"/>
        </jsp:forward>
      <%
    }
    else if (ButtonPressed.equals("View All Processes"))
    {
      RSB.populateProcessList();
      %>
        <jsp:forward page="missingDataIdentification.jsp">
         <jsp:param name="viewProcess" value="true"/>
        </jsp:forward>
      <%
    }
    else if (ButtonPressed.equals("Load Summarised Call Data from SSBS"))
    {
      RSB.getLatestProcessRowByName("Load Summarised Call Data");
      String status = RSB.getNameProcessStatus();
      if ((status.equals("Requested")) || (status.equals("Running")))
      {
        RSB.setCanSubmit(false);
        RSB.setMessage("<font color=blue><b>The 'Load Summarised Call Data' process is currently running and cannot be re-submitted</b></font>");
      }
      else if (RSB.processRunning("Summarised Call Data Rating"))
      {
        RSB.setCanSubmit(false);
        RSB.setMessage("<font color=blue><b>The 'Summarised Call Data Rating' process is currently running.  This prevents submission of the load process.</b></font>");
      }
      else
      {
        RSB.setCanSubmit(true);
        RSB.setMode("Submit");
        RSB.setMessage("<font color=blue><b>To initiate the 'Load Summarised Call Data' process, select 'Submit' from the 'Options' menu above</b></font>");
      }
      %>
        <jsp:forward page="loadSSBSData.jsp"/>
      <%
    }
    else if (ButtonPressed.equals("Revenue Share Desktop"))
    {
      RSB.canCloseMonth();
      %>
        <jsp:forward page="rsDesktop.jsp"/>
      <%
    }
    else if (ButtonPressed.equals("createProduct"))
    {
      RSB.setMissingProduct(request.getParameter("missingDataValue"));
      %>
        <jsp:forward page="referenceDataMenu.jsp"/>
      <%
    }
    else if (ButtonPressed.equals("createAccount"))
    {
      RSB.setMissingAccount(request.getParameter("missingDataValue"));
      RSB.findProviderIdForMissingAccount();
      %>
        <jsp:forward page="referenceDataMenu.jsp"/>
      <%
    }
    else if (ButtonPressed.equals("createProvider"))
    {
      //RSB.setMissingProvider(request.getParameter("missingDataValue"));
      PRO.Reset();
      PRO.setProviderName(request.getParameter("missingDataValue"));
      PRO.setUserId((String)session.getAttribute("User_Id"));
      PRO.setAction("Add");
      PRO.setMode("Create");
      PRO.setMessage("<font color=blue><b>Mandatory fields are shown in blue</b></font>");
      %>
        <jsp:forward page="providerMaint.jsp"/>
      <%
    }
    else if (ButtonPressed.equals("Refresh"))
    {
      %>
	<jsp:forward page="missingDataIdentification.jsp"/>
      <%
    }
    else
    {//No button pressed, so must be a Refresh
      %>
      <jsp:forward page="missingDataIdentification.jsp"/>
      <%
    }
%>
</body>
</html>
