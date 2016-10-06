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
    session.setAttribute("formname","masterAccountInvoicingHandler");
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
        <jsp:forward page="masterAccountInvoicing.jsp">
          <jsp:param name="query" value="true"/>
          <jsp:param name="fromPage" value="masterAccountInvoicing"/>
        </jsp:forward>
      <%
    }
    else if (ButtonPressed.equals("View Invoice Report"))
    {
      RSB.getLatestProcessRowByName("Master Account Invoicing");
      String status = RSB.getNameProcessStatus();
      if (status.equals("Completed"))
      {
        %>
          <jsp:forward page="masterAccountInvoicing.jsp">
           <jsp:param name="viewReport" value="invoice"/>
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
        else if (!RSB.getProcessCompletedByName("Summarised Call Data Rating"))
        {
          RSB.setCanSubmit(false);
        }
        else if (RSB.processRunning("All"))
        {
          RSB.setCanSubmit(false);
        }
        else
        {
          RSB.setCanSubmit(true);
          RSB.setMode("Submit");
        }
        RSB.setMessage("<font color=blue><b>The Invoice Report is currently unavailable</b></font>");
        %>
          <jsp:forward page="masterAccountInvoicing.jsp"/>
        <%
      }
    }
    else if (ButtonPressed.equals("View Suspense Report"))
    {
      RSB.getLatestProcessRowByName("Master Account Invoicing");
      String status = RSB.getNameProcessStatus();
      if (status.equals("Completed"))
      {
        %>
          <jsp:forward page="masterAccountInvoicing.jsp">
           <jsp:param name="viewReport" value="suspense"/>
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
        else if (!RSB.getProcessCompletedByName("Summarised Call Data Rating"))
        {
          RSB.setCanSubmit(false);
        }
        else if (RSB.processRunning("All"))
        {
          RSB.setCanSubmit(false);
        }
        else
        {
          RSB.setCanSubmit(true);
          RSB.setMode("Submit");
        }
        RSB.setMessage("<font color=blue><b>The Suspense Report is currently unavailable</b></font>");
        %>
          <jsp:forward page="masterAccountInvoicing.jsp"/>
        <%
      }
    }
    else if (ButtonPressed.equals("View All Processes"))
    {
      RSB.getLatestProcessRowByName("Master Account Invoicing");
      String status = RSB.getNameProcessStatus();
      if ((status.equals("Requested")) || (status.equals("Running")))
      {
        RSB.setCanSubmit(false);
        RSB.setMessage("<font color=blue><b>The 'Master Account Invoicing' process is currently running and cannot be re-submitted</b></font>");
      }
      else if (!RSB.getProcessCompletedByName("Summarised Call Data Rating"))
      {
        RSB.setCanSubmit(false);
        RSB.setMessage("<font color=blue><b>The 'Summarised Call Data Rating' process has not yet completed successfully.  This prevents submission of the 'Master Account Invoicing' process.</b></font>");
      }
      else if (RSB.processRunning("All"))
      {
        RSB.setCanSubmit(false);
        RSB.setMessage("<font color=blue><b>Another process is currently running.  This prevents submission of the 'Master Account Invoicing' process.</b></font>");
      }
      else
      {
        RSB.setCanSubmit(true);
        RSB.setMode("Submit");
        RSB.setMessage("<font color=blue><b>To initiate the 'Master Account Invoicing' process, select 'Submit' from the 'Options' menu above</b></font>");
      }
      RSB.populateProcessList();
      %>
        <jsp:forward page="masterAccountInvoicing.jsp">
         <jsp:param name="viewProcess" value="true"/>
        </jsp:forward>
      <%
    }
    else if (ButtonPressed.equals("View This Process"))
    {
      RSB.getLatestProcessRowByName("Master Account Invoicing");
      String status = RSB.getNameProcessStatus();
      if ((status.equals("Requested")) || (status.equals("Running")))
      {
        RSB.setCanSubmit(false);
        RSB.setMessage("<font color=blue><b>The 'Master Account Invoicing' process is currently running and cannot be re-submitted</b></font>");
      }
      else if (!RSB.getProcessCompletedByName("Summarised Call Data Rating"))
      {
        RSB.setCanSubmit(false);
        RSB.setMessage("<font color=blue><b>The 'Summarised Call Data Rating' process has not yet completed successfully.  This prevents submission of the 'Master Account Invoicing' process.</b></font>");
      }
      else if (RSB.processRunning("All"))
      {
        RSB.setCanSubmit(false);
        RSB.setMessage("<font color=blue><b>Another process is currently running.  This prevents submission of the 'Master Account Invoicing' process.</b></font>");
      }
      else
      {
        RSB.setCanSubmit(true);
        RSB.setMode("Submit");
        RSB.setMessage("<font color=blue><b>To initiate the 'Master Account Invoicing' process, select 'Submit' from the 'Options' menu above</b></font>");
      }
      RSB.populateProcessMessageList();
      %>
        <jsp:forward page="masterAccountInvoicing.jsp">
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
      RSB.getLatestProcessRowByName("Master Account Invoicing");
      String status = RSB.getNameProcessStatus();
      if ((status.equals("Requested")) || (status.equals("Running")))
      {
        RSB.setCanSubmit(false);
        RSB.setMessage("<font color=blue><b>The 'Master Account Invoicing' process is currently running and cannot be re-submitted</b></font>");
      }
      else if (!RSB.getProcessCompletedByName("Summarised Call Data Rating"))
      {
        RSB.setCanSubmit(false);
        RSB.setMessage("<font color=blue><b>The 'Summarised Call Data Rating' process has not yet completed successfully.  This prevents submission of the 'Master Account Invoicing' process.</b></font>");
      }
      else if (RSB.processRunning("All"))
      {
        RSB.setCanSubmit(false);
        RSB.setMessage("<font color=blue><b>Another process is currently running.  This prevents submission of the 'Master Account Invoicing' process.</b></font>");
      }
      else
      {
        RSB.setCanSubmit(true);
        RSB.setMode("Submit");
        RSB.setMessage("<font color=blue><b>To initiate the 'Master Account Invoicing' process, select 'Submit' from the 'Options' menu above</b></font>");
      }
      %>
	<jsp:forward page="masterAccountInvoicing.jsp"/>
      <%
    }
    else if (ButtonPressed.equals("Confirm"))
    {
      if (RSB.createProcessRow("Master Account Invoicing", "Requested", null, null))
      {
        RSB.setCanSubmit(false);
        RSB.getLatestProcessRowByName("Master Account Invoicing");
        %>
          <jsp:forward page="masterAccountInvoicing.jsp"/>
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
      RSB.getLatestProcessRowByName("Master Account Invoicing");
      String status = RSB.getNameProcessStatus();
      if ((status.equals("Requested")) || (status.equals("Running")))
      {
        RSB.setCanSubmit(false);
        RSB.setMessage("<font color=blue><b>The 'Master Account Invoicing' process is currently running and cannot be re-submitted</b></font>");
      }
      else if (!RSB.getProcessCompletedByName("Summarised Call Data Rating"))
      {
        RSB.setCanSubmit(false);
        RSB.setMessage("<font color=blue><b>The 'Summarised Call Data Rating' process has not yet completed successfully.  This prevents submission of the 'Master Account Invoicing' process.</b></font>");
      }
      else if (RSB.processRunning("All"))
      {
        RSB.setCanSubmit(false);
        RSB.setMessage("<font color=blue><b>Another process is currently running.  This prevents submission of the 'Master Account Invoicing' process.</b></font>");
      }
      else
      {
        RSB.setCanSubmit(true);
        RSB.setMode("Confirm");
        RSB.setMessage("<font color=blue><b>Select 'Confirm' from the " +
          "'Options' menu above to initiate the 'Master Account Invoicing' process or 'Cancel' to abort the operation</b></font>");
      }
      %>
      <jsp:forward page="masterAccountInvoicing.jsp"/>
      <%
    }
    else
    {//No button pressed, so must be a Refresh
      RSB.getLatestProcessRowByName("Master Account Invoicing");
      String status = RSB.getNameProcessStatus();
      if ((status.equals("Requested")) || (status.equals("Running")))
      {
        RSB.setCanSubmit(false);
        RSB.setMessage("<font color=blue><b>The 'Master Account Invoicing' process is currently running and cannot be re-submitted</b></font>");
      }
      else if (!RSB.getProcessCompletedByName("Summarised Call Data Rating"))
      {
        RSB.setCanSubmit(false);
        RSB.setMessage("<font color=blue><b>The 'Summarised Call Data Rating' process has not yet completed successfully.  This prevents submission of the 'Master Account Invoicing' process.</b></font>");
      }
      else if (RSB.processRunning("All"))
      {
        RSB.setCanSubmit(false);
        RSB.setMessage("<font color=blue><b>Another process is currently running.  This prevents submission of the 'Master Account Invoicing' process.</b></font>");
      }
      else
      {
        RSB.setCanSubmit(true);
        RSB.setMode("Submit");
        RSB.setMessage("<font color=blue><b>To initiate the 'Master Account Invoicing' process, select 'Submit' from the 'Options' menu above</b></font>");
      }
       %>
      <jsp:forward page="masterAccountInvoicing.jsp"/>
      <%
    }
%>
</body>
</html>
