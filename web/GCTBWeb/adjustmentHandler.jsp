<html>
<head>
</head>
<%@ page import="JavaUtil.*"%>
<%@ page import="java.util.Enumeration"%>
<jsp:useBean id="BAN" class="DBUtilities.BANBean" scope="session"/>
<jsp:useBean id="adjBAN" class="DBUtilities.AdjustmentBANBean" scope="session"/>

<%  String ButtonPressed=request.getParameter("ButtonPressed");
    JavaUtil.StringUtil SU = new JavaUtil.StringUtil();
		Enumeration FormFields = request.getParameterNames();
		String FormField = null;
//System.out.println(ButtonPressed);

    session.setAttribute("PageSent",request.getRequestURI());
    if (ButtonPressed.startsWith("Desktop"))
    {
      %>
	<jsp:forward page="newDesktop.jsp"/>
      <%
    }
    else if (ButtonPressed.equals("Ad Hoc Invoice"))
    {
      %>
	<jsp:forward page="adHocInvoice.jsp"/>
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
        <jsp:forward page="adjustment.jsp">
         <jsp:param name="query" value="true"/>
          <jsp:param name="fromPage" value="adjustment"/>
        </jsp:forward>
      <%
    }
    else if (ButtonPressed.equals("Refresh"))
    {
      adjBAN.Reset();
      %>
	<jsp:forward page="adjustment.jsp"/>
      <%
    }
    else if (ButtonPressed.equals("Delete"))
    {
      adjBAN.setMode("Confirm");
      adjBAN.setMessage("<font color=blue><b>Select 'Confirm' from the " +
              "'Options' menu above to delete this adjustment or 'Cancel' to abort the operation</b></font>");
      %>
	<jsp:forward page="adjustment.jsp"/>
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
	<jsp:forward page="adHocInvoice.jsp"/>
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
	<jsp:forward page="adjustment.jsp"/>
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
          if (adjBAN.calculateTax())
          {
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
          }
        }
        %>
        <jsp:forward page="adjustment.jsp"/>
        <%
      }
      else
      {//No button pressed, so must be a Refresh
        %>
        <jsp:forward page="adjustment.jsp"/>
        <%
      }
    }
      %>
</body>
</html>
