<html>
<head>
</head>
<%@ page import="JavaUtil.*"%>
<%@ page import="java.util.Enumeration"%>
<jsp:useBean id="BAN" class="DBUtilities.BANBean" scope="session"/>
<jsp:useBean id="disBAN" class="DBUtilities.ConglomDiscountBANBean" scope="session"/>

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
    else if (ButtonPressed.equals("Conglom Discounts"))
    {
      %>
	<jsp:forward page="listConglomDiscounts.jsp"/>
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
        <jsp:forward page="conglomDiscount.jsp">
          <jsp:param name="query" value="true"/>
          <jsp:param name="fromPage" value="conglomDiscount"/>
        </jsp:forward>
      <%
    }
    else if (ButtonPressed.equals("Exclude Accounts"))
    {
      if ((SU.hasNoValue(disBAN.getBilledProduct())) ||
        (SU.hasNoValue(disBAN.getDiscountType())))
      {
        disBAN.setMessage("<font color=blue><b>Please select billed product " +
          " and discount type before excluding accounts</b></font>");
      %>
	<jsp:forward page="conglomDiscount.jsp"/>
      <%
      }
      else
      {
      %>
        <jsp:forward page="conglomDiscount.jsp">
          <jsp:param name="conglomDiscExclude" value="true"/>
        </jsp:forward>
      <%
      }
    }
    else if (ButtonPressed.equals("Refresh"))
    {
      disBAN.Reset();
      %>
	<jsp:forward page="conglomDiscount.jsp"/>
      <%
    }
    else if (ButtonPressed.equals("Delete"))
    {
      disBAN.setMode("Confirm");
      disBAN.setMessage("<font color=blue><b>Select 'Confirm' from the " +
              "'Options' menu above to delete this adjustment or 'Cancel' to abort the operation</b></font>");
      %>
	<jsp:forward page="conglomDiscount.jsp"/>
      <%
    }
    else if (ButtonPressed.equals("Confirm"))
    {
      String action = disBAN.getAction();
      if (action.equals("Add"))
      {
        disBAN.createDiscount();
      }
      else if (action.equals("Delete"))
      {
        disBAN.deleteDiscount();
      }
      else if (action.equals("Amend"))
      {
        disBAN.updateDiscount();
      }
      BAN.setMessage(disBAN.getMessage());
      %>
	<jsp:forward page="listConglomDiscounts.jsp"/>
      <%
    }
    else if (ButtonPressed.equals("Cancel"))
    {
      String action = disBAN.getAction();
      if (action.equals("Add"))
      {
        disBAN.setMode("Create");
        disBAN.setMessage("<font color=blue><b>Mandatory fields are shown in blue</b></font>");
      }
      else if (action.equals("Delete"))
      {
        disBAN.setMode("Delete");
        disBAN.setMessage("<font color=blue><b>Select 'Delete' from the 'Options' menu above</b></font>");
      }
      else if (action.equals("Amend"))
      {
        disBAN.setMode("Amend");
        disBAN.setMessage("<font color=blue><b>Amend as required and then select " +
          "'Update' from the 'Options' menu above. Mandatory fields are shown in blue.</b></font>");
      }
      %>
	<jsp:forward page="conglomDiscount.jsp"/>
      <%
    }
    else
    {
      //store form values in Bean
      while (FormFields.hasMoreElements())
      {
        FormField=(String)FormFields.nextElement();
        disBAN.setParameter(FormField,request.getParameter(FormField));
      }
      if ((ButtonPressed.equals("Submit")) || (ButtonPressed.equals("Update")))
      {
        if (disBAN.isValid(ButtonPressed))
        {
          disBAN.setMode("Confirm");
          if (ButtonPressed.equals("Update"))
          {
            disBAN.setMessage("<font color=blue><b>Select 'Confirm' from the " +
              "'Options' menu above to amend this adjustment or 'Cancel' to abort the operation</b></font>");
          }
          else
          {
            disBAN.setMessage("<font color=blue><b>Select 'Confirm' from the " +
              "'Options' menu above to create this adjustment or 'Cancel' to amend it</b></font>");
          }
        }
        %>
        <jsp:forward page="conglomDiscount.jsp"/>
        <%
      }
      else
      {//No button pressed, so must be a Refresh
        %>
        <jsp:forward page="conglomDiscount.jsp"/>
        <%
      }
    }
      %>
</body>
</html>
