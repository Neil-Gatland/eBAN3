<html>
<head>
</head>
<%@ page import="JavaUtil.*"%>
<%@ page import="java.util.Enumeration"%>
<jsp:useBean id="BAN" class="DBUtilities.BANBean" scope="session"/>
<jsp:useBean id="cbpBAN" class="DBUtilities.ConglomBilledProductBANBean" scope="session"/>
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
        <jsp:forward page="conglomBilledProductMaint.jsp">
         <jsp:param name="query" value="true"/>
          <jsp:param name="fromPage" value="conglomBilledProductMaint"/>
        </jsp:forward>
      <%
    }
    else if (ButtonPressed.equals("Refresh"))
    {
      //cbpBAN.Reset();
      %>
	<jsp:forward page="conglomBilledProductMaint.jsp"/>
      <%
    }
    else if (ButtonPressed.equals("Delete"))
    {
      cbpBAN.setMode("Confirm");
      cbpBAN.setMessage("<font color=blue><b>Select 'Confirm' from the " +
              "'Options' menu above to delete this customer or 'Cancel' to abort the operation</b></font>");
      %>
	<jsp:forward page="conglomBilledProductMaint.jsp"/>
      <%
    }
    else if (ButtonPressed.equals("Confirm"))
    {
      String action = cbpBAN.getAction();
      if (action.equals("Add"))
      {
        cbpBAN.createCustomerProduct();
      }
      else if (action.equals("Delete"))
      {
        //cbpBAN.deleteAdjustment();
      }
      else if (action.equals("Amend"))
      {
        cbpBAN.updateCustomerProduct();
      }
      ccBAN.setMessage(cbpBAN.getMessage());
      %>
	<jsp:forward page="conglomCustMaint.jsp"/>
      <%
    }
    else if (ButtonPressed.equals("Cancel"))
    {
      String action = cbpBAN.getAction();
      if (action.equals("Add"))
      {
        cbpBAN.setMode("Create");
        cbpBAN.setMessage("<font color=blue><b>Mandatory fields are shown in blue</b></font>");
      }
      else if (action.equals("Delete"))
      {
        cbpBAN.setMode("Delete");
        cbpBAN.setMessage("<font color=blue><b>Select 'Delete' from the 'Options' menu above</b></font>");
      }
      else if (action.equals("Amend"))
      {
        cbpBAN.setMode("Amend");
        cbpBAN.setMessage("<font color=blue><b>Amend as required and then select " +
          "'Update' from the 'Options' menu above. Mandatory fields are shown in blue.</b></font>");
      }
      %>
	<jsp:forward page="conglomBilledProductMaint.jsp"/>
      <%
    }
    else
    {
      //store form values in Bean
      while (FormFields.hasMoreElements())
      {
        FormField=(String)FormFields.nextElement();
        cbpBAN.setParameter(FormField,request.getParameter(FormField));
      }
      if ((ButtonPressed.equals("Submit")) || (ButtonPressed.equals("Update")))
      {
        if (cbpBAN.isValid(ButtonPressed))
        {
          cbpBAN.setMode("Confirm");
          if (ButtonPressed.equals("Update"))
          {
            cbpBAN.setMessage("<font color=blue><b>Select 'Confirm' from the " +
              "'Options' menu above to amend this product or 'Cancel' to abort the operation</b></font>");
          }
          else
          {
            cbpBAN.setMessage("<font color=blue><b>Select 'Confirm' from the " +
              "'Options' menu above to add this product or 'Cancel' to amend it</b></font>");
          }
        }
        %>
        <jsp:forward page="conglomBilledProductMaint.jsp"/>
        <%
      }
      else
      {//No button pressed, so must be a Refresh
        %>
        <jsp:forward page="conglomBilledProductMaint.jsp"/>
        <%
      }
    }
      %>
</body>
</html>
