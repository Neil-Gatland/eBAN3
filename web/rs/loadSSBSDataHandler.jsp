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
    RSB.getLatestProcessRowByName("Load Summarised Call Data");
    String status = RSB.getNameProcessStatus();
    if ((status.equals("Requested")) || (status.equals("Running")))
    {
      RSB.setCanSubmit(false);
      RSB.setMessage("<font color=blue><b>The 'Load Summarised Call Data' process is currently running and cannot be re-submitted</b></font>");
    }
    else if (RSB.processRunning("All"))
    {
      RSB.setCanSubmit(false);
      RSB.setMessage("<font color=blue><b>Another process is currently running.  This prevents submission of the load process.</b></font>");
    }
    else if (RSB.getProcessCompletedByName("Summarised Call Data Rating"))
    {
      RSB.setCanSubmit(false);
      RSB.setMessage("<font color=blue><b>The 'Summarised Call Data Rating' process has been completed.  This prevents submission of the load process.</b></font>");
    }
    else
    {
      RSB.setCanSubmit(true);
      RSB.setMode(mode);
      if (mode.equals("Submit"))
      {
        RSB.setMessage("<font color=blue><b>To initiate the 'Load Summarised Call Data' process, select 'Submit' from the 'Options' menu above</b></font>");
      }
      else
      {
        RSB.setMessage("<font color=blue><b>Select 'Confirm' from the " +
          "'Options' menu above to initiate the 'Load Summarised Call Data' process or 'Cancel' to abort the operation</b></font>");
      }
    }
  }
%>
<%
    Enumeration FormFields = request.getParameterNames();
    String FormField = null;
    session.setAttribute("formname","loadSSBSDataHandler");
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
        <jsp:forward page="loadSSBSData.jsp">
          <jsp:param name="query" value="true"/>
          <jsp:param name="fromPage" value="loadSSBSData"/>
        </jsp:forward>
      <%
    }
    else if (ButtonPressed.equals("View Import Report"))
    {
      RSB.getLatestProcessRowByName("Load Summarised Call Data");
      String status = RSB.getNameProcessStatus();
      if (status.equals("Completed"))
      {
        %>
          <jsp:forward page="loadSSBSData.jsp">
           <jsp:param name="viewReport" value="import"/>
           <jsp:param name="reportType" value="audit"/>
          </jsp:forward>
        <%
      }
      else
      {
        if ((status.equals("Requested")) || (status.equals("Running")))
        {
          RSB.setCanSubmit(false);
        }
        else if (RSB.processRunning("All"))
        {
          RSB.setCanSubmit(false);
        }
        else if (RSB.getProcessCompletedByName("Summarised Call Data Rating"))
        {
          RSB.setCanSubmit(false);
        }
        else
        {
          RSB.setCanSubmit(true);
          RSB.setMode("Submit");
        }
        RSB.setMessage("<font color=blue><b>The Import Report is currently unavailable</b></font>");
        %>
          <jsp:forward page="loadSSBSData.jsp"/>
        <%
      }
    }
    else if (ButtonPressed.equals("View All Processes"))
    {
      checkProcesses(RSB, "Submit");
      RSB.populateProcessList();
      %>
        <jsp:forward page="loadSSBSData.jsp">
         <jsp:param name="viewProcess" value="true"/>
        </jsp:forward>
      <%
    }
    else if (ButtonPressed.equals("View This Process"))
    {
      checkProcesses(RSB, "Submit");
      RSB.populateProcessMessageList();
      %>
        <jsp:forward page="loadSSBSData.jsp">
         <jsp:param name="viewThisProcess" value="Load Summarised Call Data"/>
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
        <jsp:forward page="loadSSBSData.jsp"/>
      <%
    }
    else if (ButtonPressed.equals("Confirm"))
    {
      if (RSB.createProcessRow("Load Summarised Call Data", "Requested", null, null))
      {
        RSB.setCanSubmit(false);
        RSB.getLatestProcessRowByName("Load Summarised Call Data");
        %>
          <jsp:forward page="loadSSBSData.jsp"/>
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
      %>
      <jsp:forward page="loadSSBSData.jsp"/>
      <%
    }
    else
    {//No button pressed, so must be a Refresh
      checkProcesses(RSB, "Submit");
      %>
      <jsp:forward page="loadSSBSData.jsp"/>
      <%
    }
%>
</body>
</html>
