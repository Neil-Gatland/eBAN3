<html>
<head>
</head>
<body>
<%@ page import="JavaUtil.StringUtil"%>
<%@ page import="java.util.Enumeration"%>

<jsp:useBean id="BAN" class="DBUtilities.BANBean" scope="session"/>
<jsp:useBean id="HB" class="HTMLUtil.HTMLBean" scope="session"/>
<jsp:useBean id="itmBAN" class="DBUtilities.InvoiceThresholdBean" scope="session"/>
<%! String ButtonPressed, banId, listType;%>
<%
    Enumeration FormFields = request.getParameterNames();
    String FormField = null;
    session.setAttribute("formname","invoiceThresholdMaintHandler");
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
        <jsp:forward page="invoiceThresholdMaint.jsp">
          <jsp:param name="query" value="true"/>
          <jsp:param name="fromPage" value="invoiceThresholdMaint"/>
        </jsp:forward>
      <%
    }
    else if (ButtonPressed.equals("Revenue Share Desktop"))
    {
      %>
        <jsp:forward page="rsDesktop.jsp"/>
      <%
    }
    else if (ButtonPressed.equals("Refresh"))
    {
      //itmBAN.Reset();
      itmBAN.setAction("Amend");
      itmBAN.setMode("Amend");
      itmBAN.getInvoiceThreshold();
      itmBAN.setMessage("<font color=blue><b>Amend as required and then select " +
        "'Update' from the 'Options' menu above. Mandatory fields are shown in blue.</b></font>");
      %>
	<jsp:forward page="invoiceThresholdMaint.jsp"/>
      <%
    }
    /*else if (ButtonPressed.equals("Delete"))
    {
      itmBAN.setMode("Confirm");
      itmBAN.setMessage("<font color=blue><b>Select 'Confirm' from the " +
              "'Options' menu above to delete this adjustment or 'Cancel' to abort the operation</b></font>");

	<jsp:forward page="invoiceThresholdMaint.jsp"/>

    }*/
    else if (ButtonPressed.equals("Confirm"))
    {
      String action = itmBAN.getAction();
      if (action.equals("Add"))
      {
        //itmBAN.createInvoiceThreshold();
      }
      else if (action.equals("Delete"))
      {
        //itmBAN.deleteInvoiceThreshold();
      }
      else if (action.equals("Amend"))
      {
        itmBAN.updateInvoiceThreshold();
      }
      BAN.setMessage(itmBAN.getMessage());
      if (itmBAN.getMessage().equals("<font color=blue><b>Invoice Threshold updated</b></font>"))
      {
        %>
          <jsp:forward page="rsDesktop.jsp"/>
        <%
      }
      else
      {
        %>
          <jsp:forward page="invoiceThresholdMaint.jsp"/>
        <%
      }
    }
    else if (ButtonPressed.equals("Cancel"))
    {
      String action = itmBAN.getAction();
      if (action.equals("Add"))
      {
        itmBAN.setMode("Create");
        itmBAN.setMessage("<font color=blue><b>Mandatory fields are shown in blue</b></font>");
      }
      else if (action.equals("Delete"))
      {
        itmBAN.setMode("Delete");
        itmBAN.setMessage("<font color=blue><b>Select 'Delete' from the 'Options' menu above</b></font>");
      }
      else if (action.equals("Amend"))
      {
        itmBAN.setMode("Amend");
        itmBAN.setMessage("<font color=blue><b>Amend as required and then select " +
          "'Update' from the 'Options' menu above. Mandatory fields are shown in blue.</b></font>");
      }
      %>
	<jsp:forward page="invoiceThresholdMaint.jsp"/>
      <%
    }
    else
    {
      //store form values in Bean
      while (FormFields.hasMoreElements())
      {
        FormField=(String)FormFields.nextElement();
        itmBAN.setParameter(FormField,request.getParameter(FormField));
      }
      if ((ButtonPressed.equals("Submit")) || (ButtonPressed.equals("Update")))
      {
        if (itmBAN.isValid(ButtonPressed))
        {
          itmBAN.setMode("Confirm");
          if (ButtonPressed.equals("Update"))
          {
            itmBAN.setMessage("<font color=blue><b>Select 'Confirm' from the " +
              "'Options' menu above to commit your updates or 'Cancel' to abort the operation</b></font>");
          }
          else
          {
            itmBAN.setMessage("<font color=blue><b>Select 'Confirm' from the " +
              "'Options' menu above to create these entries or 'Cancel' to amend them</b></font>");
          }
        }
        %>
        <jsp:forward page="invoiceThresholdMaint.jsp"/>
        <%
      }
      else
      {//No button pressed, so must be a Refresh
        %>
        <jsp:forward page="invoiceThresholdMaint.jsp"/>
        <%
      }
    }
%>
</body>
</html>
