<html>
<head>
</head>
<body>
<%@ page import="JavaUtil.StringUtil"%>

<jsp:useBean id="BAN" class="DBUtilities.BANBean" scope="session"/>
<jsp:useBean id="HB" class="HTMLUtil.HTMLBean" scope="session"/>
<jsp:useBean id="RSB" class="DBUtilities.RevenueShareBean" scope="session"/>
<%! String ButtonPressed, banId, listType;%>
<%
    session.setAttribute("formname","rsDesktopHandler");
    ButtonPressed = request.getParameter("ButtonPressed");
    RSB.getLatestProcessControl();
    RSB.canCloseMonth();
    if (ButtonPressed.equals("Log Off"))
    {
      %>
	<jsp:forward page="Login.jsp?type=l"/>
      <%
    }
    else if (ButtonPressed.equals("Raise Query"))
    {
      %>
        <jsp:forward page="rsDesktop.jsp">
          <jsp:param name="query" value="true"/>
          <jsp:param name="fromPage" value="rsDesktop"/>
        </jsp:forward>
      <%
    }
    else if (ButtonPressed.equals("Change Password"))
    {
      %>
        <jsp:forward page="rsDesktop.jsp">
         <jsp:param name="changePass" value="true"/>
        </jsp:forward>
      <%
    }
    else if (ButtonPressed.equals("Audit Reporting"))
    {
      if (RSB.processRunning("All"))
      {
        RSB.setMessage("<font color=blue><b>Audit Reports cannot be viewed " +
          "while processes are running</b></font>");
        %>
          <jsp:forward page="rsDesktop.jsp"/>
        <%
      }
      else
      {
        %>
          <jsp:forward page="rsDesktop.jsp">
           <jsp:param name="viewReport" value="none"/>
           <jsp:param name="reportType" value="audit"/>
          </jsp:forward>
        <%
      }
    }
    else if (ButtonPressed.equals("Customer Invoicing Reports"))
    {
      %>
        <jsp:forward page="rsDesktop.jsp">
         <jsp:param name="viewReport" value="none"/>
         <jsp:param name="reportType" value="archive"/>
        </jsp:forward>
      <%
    }
    else if (ButtonPressed.equals("Customer Rates and Flags"))
    {
      %>
        <jsp:forward page="rsDesktop.jsp">
         <jsp:param name="viewReport" value="none"/>
         <jsp:param name="reportType" value="rateflag"/>
        </jsp:forward>
      <%
    }
    else if (ButtonPressed.equals("View All Processes"))
    {
      RSB.populateProcessList();
      %>
        <jsp:forward page="rsDesktop.jsp">
         <jsp:param name="viewProcess" value="true"/>
        </jsp:forward>
      <%
    }
    else if (ButtonPressed.equals("Load Summarised Call Data from SSBS"))
    {
/*      RSB.getLatestProcessRowByName("Load Summarised Call Data");
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
      else if (RSB.processRunning("All"))
      {
        RSB.setCanSubmit(false);
        RSB.setMessage("<font color=blue><b>Another process is currently running.  This prevents submission of the load process.</b></font>");
      }
      else
      {
        RSB.setCanSubmit(true);
        RSB.setMode("Submit");
        RSB.setMessage("<font color=blue><b>To initiate the 'Load Summarised Call Data' process, select 'Submit' from the 'Options' menu above</b></font>");
      }*/
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
        RSB.setMode("Submit");
        RSB.setMessage("<font color=blue><b>To initiate the 'Load Summarised Call Data' process, select 'Submit' from the 'Options' menu above</b></font>");
      }
      %>
        <jsp:forward page="loadSSBSData.jsp"/>
      <%
    }
    else if (ButtonPressed.equals("Remove Incorrectly Posted Traffic"))
    {
      RSB.setIncorrectlyPosted("all");
      RSB.populateTrafficList();
      %>
        <jsp:forward page="removeTraffic.jsp"/>
      <%
    }
    else if (ButtonPressed.equals("Suspended Summarised CDR Data"))
    {
      RSB.setSuspendPopUp(false);
      RSB.setSuspendedAccountNumber("");
      RSB.setSuspendedProduct("");
      RSB.setSuspendedType("all");
      //RSB.setRefreshSuspendedGrid(true);
      RSB.populateSuspendedDataList();
      RSB.setSuspendedSortOrder("1");
      %>
        <jsp:forward page="suspendedData.jsp"/>
      <%
    }
    else if (ButtonPressed.equals("Summarised Call Data Rating"))
    {
      RSB.setRerateRQR09("N");
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
          RSB.setMode("Submit");
          RSB.setMessage("<font color=blue><b>To initiate the 'Summarised Call Data Rating' process, select 'Submit' from the 'Options' menu above</b></font>");
        }
        else
        {
          RSB.setCanSubmit(false);
          RSB.setMessage("<font color=blue><b>The latest 'Load Summarised Call Data' process has not completed successfully.  This prevents submission of the rating process.</b></font>");
        }
      }
      RSB.getLatestProcessRowByName("Summarised Call Data Rating");
      %>
        <jsp:forward page="callDataRating.jsp"/>
      <%
    }
    else if (ButtonPressed.equals("Master Account Invoicing"))
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
    else if (ButtonPressed.equals("Create Ebilling Upload Files"))
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
    else if (ButtonPressed.equals("Close Current Call Month"))
    {
      if (RSB.getCanClose())
      {
        RSB.setMode("Confirm");
        RSB.setMessage("<font color=blue><b>Select 'Confirm' from the " +
          "'Options' menu above initiate the 'Close Current Month' process or 'Cancel' to abort the operation</b></font>");
      }
      %>
        <jsp:forward page="rsDesktop.jsp"/>
      <%
    }
    else if (ButtonPressed.equals("Confirm"))
    {
      if (RSB.createProcessRow("Close Current Month", "Requested", null, null))
      {
        RSB.setCanClose(false);
      }
      %>
        <jsp:forward page="rsDesktop.jsp"/>
      <%
    }
    else if (ButtonPressed.equals("Manual Adjustment Invoicing"))
    {
      RSB.setAdjustmentStatus("");
      RSB.setInvoiceNumber("");
      RSB.setMode("");
      RSB.populateAdjustmentInvoiceList();
      %>
        <jsp:forward page="manualAdjustmentInvoicing.jsp"/>
      <%
    }
    else if (ButtonPressed.equals("Rating Scheme"))
    {
      RSB.Reset();
      %>
        <jsp:forward page="ratingSchemeMenu.jsp"/>
      <%
    }
    else if (ButtonPressed.equals("Unexpected Product NTS"))
    {
      RSB.populateUnexpectedProductList();
      %>
        <jsp:forward page="unexpectedProduct.jsp"/>
      <%
    }
    else if (ButtonPressed.equals("Provider"))
    {
      %>
        <jsp:forward page="rsDesktop.jsp">
         <jsp:param name="nYA" value="true"/>
        </jsp:forward>
      <%
    }
    else if (ButtonPressed.equals("Master Account"))
    {
      %>
        <jsp:forward page="rsDesktop.jsp">
         <jsp:param name="nYA" value="true"/>
        </jsp:forward>
      <%
    }
    else if (ButtonPressed.equals("Account"))
    {
      %>
        <jsp:forward page="rsDesktop.jsp">
         <jsp:param name="nYA" value="true"/>
        </jsp:forward>
      <%
    }
    else if (ButtonPressed.equals("Product Group/Product"))
    {
      %>
        <jsp:forward page="rsDesktop.jsp">
         <jsp:param name="nYA" value="true"/>
        </jsp:forward>
      <%
    }
    else if (ButtonPressed.equals("Provider/Account/Product/Threshold"))
    {
      RSB.Reset();
      %>
        <jsp:forward page="referenceDataMenu.jsp"/>
      <%
    }
    else if (ButtonPressed.equals("Upload"))
    {
      %>
        <jsp:forward page="single_upload.jsp"/>
      <%
    }
    else //refresh
    {
      String from = request.getParameter("from")==null?"":request.getParameter("from");
      if(from.equals("pwd"))
      {
        RSB.setMessage(BAN.getMessage());
      }
      %>
	<jsp:forward page="rsDesktop.jsp"/>
      <%
    }
%>
</body>
</html>
