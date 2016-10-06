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
    session.setAttribute("formname","ebillingUploadHandler");
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
        <jsp:forward page="ebillingUpload.jsp">
          <jsp:param name="query" value="true"/>
          <jsp:param name="fromPage" value="ebillingUpload"/>
        </jsp:forward>
      <%
    }
    else if (ButtonPressed.equals("View All Processes"))
    {
      RSB.getLatestProcessRowByName("Ebilling Upload");
      String status = RSB.getNameProcessStatus();
      if ((status.equals("Requested")) || (status.equals("Running")))
      {
        RSB.setCanSubmit(false);
        RSB.setMessage("<font color=blue><b>The 'Ebilling Upload' process is currently running and cannot be re-submitted</b></font>");
      }
      else if (!RSB.getProcessCompletedByName("Master Account Invoicing"))
      {
        RSB.setCanSubmit(false);
        RSB.setMessage("<font color=blue><b>The 'Master Account Invoicing' process has not yet completed successfully.  This prevents submission of the 'Ebilling Upload' process.</b></font>");
      }
      else if (RSB.processRunning("All"))
      {
        RSB.setCanSubmit(false);
        RSB.setMessage("<font color=blue><b>Another process is currently running.  This prevents submission of the 'Ebilling Upload' process.</b></font>");
      }
      else
      {
        RSB.setCanSubmit(true);
        RSB.setMode("Submit");
        RSB.setMessage("<font color=blue><b>To initiate the 'Ebilling Upload' process, select 'Submit' from the 'Options' menu above</b></font>");
      }
      RSB.populateProcessList();
      %>
        <jsp:forward page="ebillingUpload.jsp">
         <jsp:param name="viewProcess" value="true"/>
        </jsp:forward>
      <%
    }
    else if (ButtonPressed.equals("View This Process"))
    {
      RSB.getLatestProcessRowByName("Ebilling Upload");
      String status = RSB.getNameProcessStatus();
      if ((status.equals("Requested")) || (status.equals("Running")))
      {
        RSB.setCanSubmit(false);
        RSB.setMessage("<font color=blue><b>The 'Ebilling Upload' process is currently running and cannot be re-submitted</b></font>");
      }
      else if (!RSB.getProcessCompletedByName("Master Account Invoicing"))
      {
        RSB.setCanSubmit(false);
        RSB.setMessage("<font color=blue><b>The 'Master Account Invoicing' process has not yet completed successfully.  This prevents submission of the 'Ebilling Upload' process.</b></font>");
      }
      else if (RSB.processRunning("All"))
      {
        RSB.setCanSubmit(false);
        RSB.setMessage("<font color=blue><b>Another process is currently running.  This prevents submission of the 'Ebilling Upload' process.</b></font>");
      }
      else
      {
        RSB.setCanSubmit(true);
        RSB.setMode("Submit");
        RSB.setMessage("<font color=blue><b>To initiate the 'Ebilling Upload' process, select 'Submit' from the 'Options' menu above</b></font>");
      }
      RSB.populateProcessMessageList();
      %>
        <jsp:forward page="ebillingUpload.jsp">
         <jsp:param name="viewThisProcess" value="Master Account Invoicing"/>
        </jsp:forward>
      <%
    }
    else if (ButtonPressed.equals("Missing Data Identification"))
    {
      %>
        <jsp:forward page="missingDataIdentification.jsp"/>
      <%
    }
    else if (ButtonPressed.equals("Revenue Share Desktop"))
    {
      RSB.canCloseMonth();
      %>
        <jsp:forward page="rsDesktop.jsp"/>
      <%
    }
    else if ((ButtonPressed.equals("Refresh")) ||
      (ButtonPressed.equals("Cancel")))
    {
      RSB.getLatestProcessRowByName("Ebilling Upload");
      String status = RSB.getNameProcessStatus();
      if ((status.equals("Requested")) || (status.equals("Running")))
      {
        RSB.setCanSubmit(false);
        RSB.setMessage("<font color=blue><b>The 'Ebilling Upload' process is currently running and cannot be re-submitted</b></font>");
      }
      else if (!RSB.getProcessCompletedByName("Master Account Invoicing"))
      {
        RSB.setCanSubmit(false);
        RSB.setMessage("<font color=blue><b>The 'Master Account Invoicing' process has not yet completed successfully.  This prevents submission of the 'Ebilling Upload' process.</b></font>");
      }
      else if (RSB.processRunning("All"))
      {
        RSB.setCanSubmit(false);
        RSB.setMessage("<font color=blue><b>Another process is currently running.  This prevents submission of the 'Ebilling Upload' process.</b></font>");
      }
      else
      {
        RSB.setCanSubmit(true);
        RSB.setMode("Submit");
        RSB.setMessage("<font color=blue><b>To initiate the 'Ebilling Upload' process, select 'Submit' from the 'Options' menu above</b></font>");
      }
      %>
	<jsp:forward page="ebillingUpload.jsp"/>
      <%
    }
    else if (ButtonPressed.equals("Confirm"))
    {
      if (RSB.createProcessRow("Ebilling Upload", "Requested", null, null))
      {
        RSB.setCanSubmit(false);
        RSB.getLatestProcessRowByName("Ebilling Upload");
        %>
          <jsp:forward page="ebillingUpload.jsp"/>
        <%
      }
      else
      {
        %>
          <jsp:forward page="rsDesktop.jsp"/>
        <%
      }
    }
    else if (ButtonPressed.equals("Submit"))
    {
      RSB.getLatestProcessRowByName("Ebilling Upload");
      String status = RSB.getNameProcessStatus();
      if ((status.equals("Requested")) || (status.equals("Running")))
      {
        RSB.setCanSubmit(false);
        RSB.setMessage("<font color=blue><b>The 'Ebilling Upload' process is currently running and cannot be re-submitted</b></font>");
      }
      else if (!RSB.getProcessCompletedByName("Master Account Invoicing"))
      {
        RSB.setCanSubmit(false);
        RSB.setMessage("<font color=blue><b>The 'Master Account Invoicing' process has not yet completed successfully.  This prevents submission of the 'Ebilling Upload' process.</b></font>");
      }
      else if (RSB.processRunning("All"))
      {
        RSB.setCanSubmit(false);
        RSB.setMessage("<font color=blue><b>Another process is currently running.  This prevents submission of the 'Ebilling Upload' process.</b></font>");
      }
      else
      {
        RSB.setCanSubmit(true);
        RSB.setMode("Confirm");
        RSB.setMessage("<font color=blue><b>Select 'Confirm' from the " +
          "'Options' menu above to initiate the 'Ebilling Upload' process or 'Cancel' to abort the operation</b></font>");
      }
      %>
      <jsp:forward page="ebillingUpload.jsp"/>
      <%
    }
    else
    {//No button pressed, so must be a Refresh
      RSB.getLatestProcessRowByName("Ebilling Upload");
      String status = RSB.getNameProcessStatus();
      if ((status.equals("Requested")) || (status.equals("Running")))
      {
        RSB.setCanSubmit(false);
        RSB.setMessage("<font color=blue><b>The 'Ebilling Upload' process is currently running and cannot be re-submitted</b></font>");
      }
      else if (!RSB.getProcessCompletedByName("Master Account Invoicing"))
      {
        RSB.setCanSubmit(false);
        RSB.setMessage("<font color=blue><b>The 'Master Account Invoicing' process has not yet completed successfully.  This prevents submission of the 'Ebilling Upload' process.</b></font>");
      }
      else if (RSB.processRunning("All"))
      {
        RSB.setCanSubmit(false);
        RSB.setMessage("<font color=blue><b>Another process is currently running.  This prevents submission of the 'Ebilling Upload' process.</b></font>");
      }
      else
      {
        RSB.setCanSubmit(true);
        RSB.setMode("Submit");
        RSB.setMessage("<font color=blue><b>To initiate the 'Ebilling Upload' process, select 'Submit' from the 'Options' menu above</b></font>");
      }
       %>
      <jsp:forward page="ebillingUpload.jsp"/>
      <%
    }
%>
</body>
</html>
