<html>
<head>
</head>
<body>
<%@ page import="JavaUtil.StringUtil"%>
<%@ page import="java.util.Enumeration"%>

<jsp:useBean id="BAN" class="DBUtilities.BANBean" scope="session"/>
<jsp:useBean id="HB" class="HTMLUtil.HTMLBean" scope="session"/>
<jsp:useBean id="RSB" class="DBUtilities.RevenueShareBean" scope="session"/>
<%! String ButtonPressed, banId, listType;
  private void checkProcesses(DBUtilities.RevenueShareBean RSB, String mode)
  {
    RSB.getLatestProcessRowByName("Summarised Call Data Rating");
    String status = RSB.getNameProcessStatus();
    if ((status.equals("Requested")) || (status.equals("Running")))
    {
      RSB.setCanSubmit(false);
      RSB.setMessage("<font color=blue><b>The 'Summarised Call Data Rating' process is currently running and cannot be re-submitted</b></font>");
    }
    else if (RSB.processRunning("Load Summarised Call Data"))
    {
      RSB.setCanSubmit(false);
      RSB.setMessage("<font color=blue><b>The 'Load Summarised Call Data' process is currently running.  This prevents submission of the rating process.</b></font>");
    }
    else if (RSB.processRunning("All"))
    {
      RSB.setCanSubmit(false);
      RSB.setMessage("<font color=blue><b>Another process is currently running.  This prevents submission of the rating process.</b></font>");
    }
    else
    {
      RSB.getLatestProcessRowByName("Load Summarised Call Data");
      if ((RSB.getNameProcessStatus().equals("Completed")) && (RSB.getNameProcessCompletedOK().equals("Y")))
      {
        RSB.setCanSubmit(true);
        RSB.setMode(mode);
        if (mode.equals("Confirm"))
        {
          RSB.setMessage("<font color=blue><b>Select 'Confirm' from the " +
            "'Options' menu above to initiate the 'Summarised Call Data Rating' process or 'Cancel' to abort the operation</b></font>");
        }
        else
        {
          RSB.setMessage("<font color=blue><b>To initiate the 'Summarised Call Data Rating' process, select 'Submit' from the 'Options' menu above</b></font>");
        }
      }
      else
      {
        RSB.setCanSubmit(false);
        RSB.setMessage("<font color=blue><b>The latest 'Load Summarised Call Data' process has not completed successfully.  This prevents submission of the rating process.</b></font>");
      }
    }
    RSB.getLatestProcessRowByName("Summarised Call Data Rating");
  }
%>
<%
    Enumeration FormFields = request.getParameterNames();
    String FormField = null;
    session.setAttribute("formname","callDataRatingHandler");
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
        <jsp:forward page="callDataRating.jsp">
          <jsp:param name="query" value="true"/>
          <jsp:param name="fromPage" value="callDataRating"/>
        </jsp:forward>
      <%
    }
    else if (ButtonPressed.equals("View Billable Report"))
    {
      RSB.getLatestProcessRowByName("Summarised Call Data Rating");
      String status = RSB.getNameProcessStatus();
      if (status.equals("Completed"))
      {
        %>
          <jsp:forward page="callDataRating.jsp">
           <jsp:param name="viewReport" value="billable"/>
           <jsp:param name="reportType" value="audit"/>
          </jsp:forward>
        <%
      }
      else
      {
        checkProcesses(RSB, "Submit");
        RSB.setMessage("<font color=blue><b>The Billable Report is currently unavailable</b></font>");
        %>
          <jsp:forward page="callDataRating.jsp"/>
        <%
      }
    }
    else if (ButtonPressed.equals("View Suspense Report"))
    {
      RSB.getLatestProcessRowByName("Summarised Call Data Rating");
      String status = RSB.getNameProcessStatus();
      if (status.equals("Completed"))
      {
        %>
          <jsp:forward page="callDataRating.jsp">
           <jsp:param name="viewReport" value="suspense"/>
           <jsp:param name="reportType" value="audit"/>
          </jsp:forward>
        <%
      }
      else
      {
        checkProcesses(RSB, "Submit");
        RSB.setMessage("<font color=blue><b>The Suspense Report is currently unavailable</b></font>");
        %>
          <jsp:forward page="callDataRating.jsp"/>
        <%
      }
    }
    else if (ButtonPressed.equals("View All Processes"))
    {
      checkProcesses(RSB, "Submit");
      RSB.populateProcessList();
      %>
        <jsp:forward page="callDataRating.jsp">
         <jsp:param name="viewProcess" value="true"/>
        </jsp:forward>
      <%
    }
    else if (ButtonPressed.equals("View This Process"))
    {
      checkProcesses(RSB, "Submit");
      RSB.populateProcessMessageList();
      %>
        <jsp:forward page="callDataRating.jsp">
         <jsp:param name="viewThisProcess" value="Summarised Call Data Rating"/>
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
      checkProcesses(RSB, "Submit");
      %>
	<jsp:forward page="callDataRating.jsp"/>
      <%
    }
    else if (ButtonPressed.equals("Confirm"))
    {
      if (RSB.createProcessRow("Summarised Call Data Rating", "Requested", null, RSB.getRerateRQR09()))
      {
        RSB.setCanSubmit(false);
        RSB.getLatestProcessRowByName("Summarised Call Data Rating");
        %>
          <jsp:forward page="callDataRating.jsp"/>
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
      checkProcesses(RSB, "Confirm");
      if (RSB.getCanSubmit())
      {
        RSB.setRerateRQR09(request.getParameter("rerateRQR09"));
      }
      %>
      <jsp:forward page="callDataRating.jsp"/>
      <%
    }
    else
    {//No button pressed, so must be a Refresh
      checkProcesses(RSB, null);
      %>
      <jsp:forward page="callDataRating.jsp"/>
      <%
    }
%>
</body>
</html>
