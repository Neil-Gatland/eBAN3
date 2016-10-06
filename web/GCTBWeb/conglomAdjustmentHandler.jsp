<html>
<head>
</head>
<%@ page import="JavaUtil.*"%>
<%@ page import="java.util.Enumeration"%>
<jsp:useBean id="BAN" class="DBUtilities.BANBean" scope="session"/>
<jsp:useBean id="adjBAN" class="DBUtilities.ConglomAdjustmentBANBean" scope="session"/>

<%  String ButtonPressed=request.getParameter("ButtonPressed");
    JavaUtil.StringUtil SU = new JavaUtil.StringUtil();
		Enumeration FormFields = request.getParameterNames();
		String FormField = null;
//System.out.println(ButtonPressed);Conglom Adjustments

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
    else if (ButtonPressed.equals("Conglom Adjustments"))
    {
      %>
	<jsp:forward page="listConglomAdjustments.jsp"/>
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
        <jsp:forward page="conglomAdjustment.jsp">
         <jsp:param name="query" value="true"/>
          <jsp:param name="fromPage" value="conglomAdjustment"/>
        </jsp:forward>
      <%
    }
    else if (ButtonPressed.equals("Refresh"))
    {
      adjBAN.Reset();
      %>
	<jsp:forward page="conglomAdjustment.jsp"/>
      <%
    }
    else if (ButtonPressed.equals("Delete"))
    {
      adjBAN.setMode("Confirm");
      adjBAN.setMessage("<font color=blue><b>Select 'Confirm' from the " +
              "'Options' menu above to delete this adjustment or 'Cancel' to abort the operation</b></font>");
      %>
	<jsp:forward page="conglomAdjustment.jsp"/>
      <%
    }
    else if (ButtonPressed.equals("Confirm"))
    {
      String action = adjBAN.getAction();
      if (action.equals("Add"))
      {
        adjBAN.createAdjustment();
      }
      else if (action.equals("Delete"))
      {
        adjBAN.deleteAdjustment();
      }
      else if (action.equals("Amend"))
      {
        adjBAN.updateAdjustment();
      }
      BAN.setMessage(adjBAN.getMessage());
      %>
	<jsp:forward page="listConglomAdjustments.jsp"/>
      <%
    }
    else if (ButtonPressed.equals("Cancel"))
    {
      String action = adjBAN.getAction();
      if (action.equals("Add"))
      {
        adjBAN.setMode("Create");
        adjBAN.setMessage("<font color=blue><b>Mandatory fields are shown in blue</b></font>");
      }
      else if (action.equals("Delete"))
      {
        adjBAN.setMode("Delete");
        adjBAN.setMessage("<font color=blue><b>Select 'Delete' from the 'Options' menu above</b></font>");
      }
      else if (action.equals("Amend"))
      {
        adjBAN.setMode("Amend");
        adjBAN.setMessage("<font color=blue><b>Amend as required and then select " +
          "'Update' from the 'Options' menu above. Mandatory fields are shown in blue.</b></font>");
      }
      %>
	<jsp:forward page="conglomAdjustment.jsp"/>
      <%
    }
    else
    {
      //store form values in Bean
      while (FormFields.hasMoreElements())
      {
        FormField=(String)FormFields.nextElement();
        adjBAN.setParameter(FormField,request.getParameter(FormField));
      }
      if ((ButtonPressed.equals("Submit")) || (ButtonPressed.equals("Update")))
      {
        if (adjBAN.isValid(ButtonPressed))
        {
          //calculate tax
          //if (adjBAN.calculateTax())
          //{
            adjBAN.setMode("Confirm");
            if (ButtonPressed.equals("Update"))
            {
              adjBAN.setMessage("<font color=blue><b>Select 'Confirm' from the " +
                "'Options' menu above to amend this adjustment or 'Cancel' to abort the operation</b></font>");
            }
            else
            {
              adjBAN.setMessage("<font color=blue><b>Select 'Confirm' from the " +
                "'Options' menu above to create this adjustment or 'Cancel' to amend it</b></font>");
            }
          //}
        }
        %>
        <jsp:forward page="conglomAdjustment.jsp"/>
        <%
      }
      else
      {//No button pressed, so must be a Refresh
        %>
        <jsp:forward page="conglomAdjustment.jsp"/>
        <%
      }
    }
      %>
</body>
</html>
