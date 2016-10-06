<html>
<head>
</head>
<%@ page import="JavaUtil.*"%>
<%@ page import="java.util.Enumeration"%>
<jsp:useBean id="BAN" class="DBUtilities.BANBean" scope="session"/>
<jsp:useBean id="crcBAN" class="DBUtilities.ConglomReportConfigBANBean" scope="session"/>
<jsp:useBean id="ccBAN" class="DBUtilities.ConglomCustomerBANBean" scope="session"/>

<%  String ButtonPressed=request.getParameter("ButtonPressed");
    JavaUtil.StringUtil SU = new JavaUtil.StringUtil();
		Enumeration FormFields = request.getParameterNames();
		String FormField = null;
//System.out.println(ButtonPressed);

    session.setAttribute("PageSent",request.getRequestURI());
    if (ButtonPressed.startsWith("Conglom Desktop"))
    {
      %>
	<jsp:forward page="conglomDesktop.jsp"/>
      <%
    }
    else if (ButtonPressed.equals("Conglom Billing Menu"))
    {
      %>
	<jsp:forward page="conglomBillingMenu.jsp"/>
      <%
    }
    else if (ButtonPressed.equals("Conglom Cust Profile Maintenanance"))
    {
      %>
	<jsp:forward page="conglomCustMaint.jsp"/>
      <%
    }
    else if (ButtonPressed.equals("Log Off"))
    {
      %>
	<jsp:forward page="Login.jsp?type=l"/>
      <%
    }
    else if (ButtonPressed.equals("Raise Query"))
    {
      %>
        <jsp:forward page="conglomReportConfigMaint.jsp">
         <jsp:param name="query" value="true"/>
          <jsp:param name="fromPage" value="conglomReportConfigMaint"/>
        </jsp:forward>
      <%
    }
    else if (ButtonPressed.equals("Refresh"))
    {
      //crcBAN.Reset();
      %>
	<jsp:forward page="conglomReportConfigMaint.jsp"/>
      <%
    }
    else if (ButtonPressed.equals("Delete"))
    {
      crcBAN.setMode("Confirm");
      crcBAN.setMessage("<font color=blue><b>Select 'Confirm' from the " +
              "'Options' menu above to delete this customer or 'Cancel' to abort the operation</b></font>");
      %>
	<jsp:forward page="conglomReportConfigMaint.jsp"/>
      <%
    }
    else if (ButtonPressed.equals("Confirm"))
    {
      String action = crcBAN.getAction();
      if (action.equals("Add"))
      {
        //crcBAN.createCustomerProduct();
      }
      else if (action.equals("Delete"))
      {
        //crcBAN.deleteAdjustment();
      }
      else if (action.equals("Amend"))
      {
        crcBAN.updateReloadReportDetails();
      }
      ccBAN.setMessage(crcBAN.getMessage());
      crcBAN.setReload(false);
      %>
	<jsp:forward page="conglomCustMaint.jsp"/>
      <%
    }
    else if (ButtonPressed.equals("Cancel"))
    {
      crcBAN.setReload(false);
      String action = crcBAN.getAction();
      if (action.equals("Add"))
      {
        crcBAN.setMode("Create");
        crcBAN.setMessage("<font color=blue><b>Mandatory fields are shown in blue</b></font>");
      }
      else if (action.equals("Delete"))
      {
        crcBAN.setMode("Delete");
        crcBAN.setMessage("<font color=blue><b>Select 'Delete' from the 'Options' menu above</b></font>");
      }
      else if (action.equals("Amend"))
      {
        crcBAN.setMode("Amend");
        crcBAN.setMessage("<font color=blue><b>Amend as required and then select " +
          "'Update' or 'Update & Reload' from the 'Options' menu above. Mandatory fields are shown in blue.</b></font>");
      }
      %>
	<jsp:forward page="conglomReportConfigMaint.jsp"/>
      <%
    }
    else
    {
      //store form values in Bean
      //crcBAN.Reset();
      crcBAN.setErrored("clear");
      if (ButtonPressed.startsWith("Update"))
      {
        // do conglomReportType first as it gets data from tables which should
        // not overwrite updated screen entries
        crcBAN.setParameter("conglomReportType",request.getParameter("conglomReportType"));
        while (FormFields.hasMoreElements())
        {
          FormField=(String)FormFields.nextElement();
          if (!FormField.equals("conglomReportType"))
          {
            crcBAN.setParameter(FormField,request.getParameter(FormField));
          }
        }
      }
      else
      {
        while (FormFields.hasMoreElements())
        {
          FormField=(String)FormFields.nextElement();
          if (!FormField.equals("conglomReportType"))
          {
            crcBAN.setParameter(FormField,request.getParameter(FormField));
          }
        }
        // do conglomReportType last as it gets data from tables which should
        // overwrite screen entries
        crcBAN.setParameter("conglomReportType",request.getParameter("conglomReportType"));
      }
      if ((ButtonPressed.equals("Submit")) || (ButtonPressed.startsWith("Update")))
      {
        if (crcBAN.isValid(ButtonPressed))
        {
          crcBAN.setMode("Confirm");
          if (ButtonPressed.startsWith("Update"))
          {
//System.out.println(ButtonPressed);
            if (ButtonPressed.startsWith("Update &"))
            {
              crcBAN.setReload(true);
            }
            crcBAN.setMessage("<font color=blue><b>Select 'Confirm' from the " +
              "'Options' menu above to amend this product or 'Cancel' to abort the operation</b></font>");
          }
          else
          {
            crcBAN.setMessage("<font color=blue><b>Select 'Confirm' from the " +
              "'Options' menu above to add this product or 'Cancel' to amend it</b></font>");
          }
        }
        %>
        <jsp:forward page="conglomReportConfigMaint.jsp"/>
        <%
      }
      else
      {//No button pressed, so must be a Refresh
        %>
        <jsp:forward page="conglomReportConfigMaint.jsp"/>
        <%
      }
    }
      %>
</body>
</html>
