<html>
<head>
</head>
<%@ page import="JavaUtil.*"%>
<%@ page import="java.util.Enumeration"%>
<jsp:useBean id="BAN" class="DBUtilities.BANBean" scope="session"/>
<jsp:useBean id="manBAN" class="DBUtilities.ConglomManualInvoiceBANBean" scope="session"/>

<%  String ButtonPressed=request.getParameter("ButtonPressed");
    JavaUtil.StringUtil SU = new JavaUtil.StringUtil();
		Enumeration FormFields = request.getParameterNames();
		String FormField = null;
//System.out.println(ButtonPressed);Conglom Discounts

    session.setAttribute("PageSent",request.getRequestURI());
    if (ButtonPressed.equals("Conglom Desktop"))
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
    else if (ButtonPressed.equals("Conglom Manual Source Invoices"))
    {
      %>
	<jsp:forward page="listConglomManualInvoices.jsp"/>
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
        <jsp:forward page="conglomInvoice.jsp">
          <jsp:param name="query" value="true"/>
          <jsp:param name="fromPage" value="conglomInvoice"/>
        </jsp:forward>
      <%
    }
    else if (ButtonPressed.equals("Refresh"))
    {
      manBAN.Reset();
      %>
	<jsp:forward page="conglomInvoice.jsp"/>
      <%
    }
    else if (ButtonPressed.equals("Delete"))
    {
      manBAN.setMode("Confirm");
      manBAN.setMessage("<font color=blue><b>Select 'Confirm' from the " +
              "'Options' menu above to delete this adjustment or 'Cancel' to abort the operation</b></font>");
      %>
	<jsp:forward page="conglomInvoice.jsp"/>
      <%
    }
    else if (ButtonPressed.equals("Confirm"))
    {
      String action = manBAN.getAction();
      if (action.equals("Add"))
      {
        if (manBAN.createInvoice() == 0)
        {
          BAN.applyConglomDiscount((String)session.getAttribute("User_Id"),
            "Individual", manBAN.getSourceInvoiceNo());
        }
      }
      else if (action.equals("Delete"))
      {
        manBAN.deleteInvoice();
      }
      else if (action.equals("Amend"))
      {
        if (manBAN.updateInvoice() == 0)
        {
          BAN.applyConglomDiscount((String)session.getAttribute("User_Id"),
            "Individual", manBAN.getSourceInvoiceNo());
        }
      }
      BAN.setMessage(manBAN.getMessage());
      %>
	<jsp:forward page="listConglomManualInvoices.jsp"/>
      <%
    }
    else if (ButtonPressed.equals("Cancel"))
    {
      String action = manBAN.getAction();
      if (action.equals("Add"))
      {
        manBAN.setMode("Create");
        manBAN.setMessage("<font color=blue><b>Mandatory fields are shown in blue</b></font>");
      }
      else if (action.equals("Delete"))
      {
        manBAN.setMode("Delete");
        manBAN.setMessage("<font color=blue><b>Select 'Delete' from the 'Options' menu above</b></font>");
      }
      else if (action.equals("Amend"))
      {
        manBAN.setMode("Amend");
        manBAN.setMessage("<font color=blue><b>Amend as required and then select " +
          "'Update' from the 'Options' menu above. Mandatory fields are shown in blue.</b></font>");
      }
      %>
	<jsp:forward page="conglomInvoice.jsp"/>
      <%
    }
    else
    {
      String prevBP = "";
      String newBP = "";
      if (!ButtonPressed.equals("Update"))
      {
        prevBP = manBAN.getBilledProduct();
        manBAN.setParameter("conglomBilledProduct3",request.getParameter("conglomBilledProduct3"));
        newBP = manBAN.getBilledProduct();
      }
      //store other form values in Bean,
      while (FormFields.hasMoreElements())
      {
        FormField=(String)FormFields.nextElement();
        if (!FormField.equals("conglomBilledProduct3"))
        {
          manBAN.setParameter(FormField,request.getParameter(FormField));
        }
      }
      if (prevBP.equals(newBP))
      {
        manBAN.sumCharges();
      }
      else
      {
        manBAN.resetCharges();
      }
      if ((ButtonPressed.equals("Submit")) || (ButtonPressed.equals("Update")))
      {
        if (manBAN.isValid(ButtonPressed))
        {
          manBAN.setMode("Confirm");
          if (ButtonPressed.equals("Update"))
          {
            manBAN.setMessage("<font color=blue><b>Select 'Confirm' from the " +
              "'Options' menu above to amend this invoice or 'Cancel' to abort the operation</b></font>");
          }
          else
          {
            manBAN.setMessage("<font color=blue><b>Select 'Confirm' from the " +
              "'Options' menu above to create this invoice or 'Cancel' to amend it</b></font>");
          }
        }
        %>
        <jsp:forward page="conglomInvoice.jsp"/>
        <%
      }
      else
      {//No button pressed, so must be a Refresh
        %>
        <jsp:forward page="conglomInvoice.jsp"/>
        <%
      }
    }
      %>
</body>
</html>
