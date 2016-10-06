<html>
<head>
</head>
<%@ page import="JavaUtil.*,java.util.Enumeration"%>
<jsp:useBean id="BAN" class="DBUtilities.BANBean" scope="session"/>
<jsp:useBean id="irBAN" class="DBUtilities.InvoiceRegionBANBean" scope="session"/>
    <%
        String ButtonPressed,Action;
	Enumeration FormFields;
	String FormField;
	boolean Errors,Success;

      ButtonPressed=(String)request.getParameter("ButtonPressed");
      FormFields=request.getParameterNames();
      /* temporary measure to alert us to first Bahraini Dinar account - TA 17/06/2010 */
      if ((ButtonPressed.startsWith("Save Draft")) ||
	      (ButtonPressed.startsWith("Submit")))
      {
        String currencyDesc = request.getParameter("Currency_Desc");
        if (currencyDesc.equals("BHD"))
        {
          while (FormFields.hasMoreElements())
          {
            FormField=(String)FormFields.nextElement();
            irBAN.setParameter(FormField,request.getParameter(FormField));
          }
          irBAN.setParameter("Currency_Desc","");
          irBAN.setMessage("<font color=red><b>Bahrain Dinar is unavailable at this time. Please contact systems support quoting reference BHDSAP3DP.</b></font>");
          %>
            <jsp:forward page="InvoiceRegionBAN.jsp"/>
          <%
        }
      }
      /* end of temporary measure */
      session.setAttribute("Button",ButtonPressed);
      Action=irBAN.getAction();
      //Deal with non submit actions first
      if (ButtonPressed.startsWith("Clear"))
      {
	while (FormFields.hasMoreElements())
	{
	  FormField=(String)FormFields.nextElement();
	  irBAN.setParameter(FormField,"");
	}
        %>
        <jsp:forward page="InvoiceRegionBAN.jsp"/>
        <%
      }
      else if (ButtonPressed.equals("Raise Query"))
      {
        %>
          <jsp:forward page="InvoiceRegionBAN.jsp">
            <jsp:param name="mode" value="<%=irBAN.getMode()%>"/>
            <jsp:param name="query" value="true"/>
            <jsp:param name="fromPage" value="InvoiceRegionBAN"/>
          </jsp:forward>
        <%
      }
      else if (ButtonPressed.startsWith("Create BAN Menu"))
      {
        %>
          <jsp:forward page="CustomerBANMenu.jsp"/>
        <%
      }
      else if (ButtonPressed.startsWith("Back To"))
      {
        %>
          <jsp:forward page="ListBans.jsp"/>
        <%
      }
      else if (ButtonPressed.startsWith("Authorise"))
      {
	irBAN.setUserId((String)session.getAttribute("User_Id"));
        if(irBAN.AuthoriseInvoiceRegionBAN())
	{
	  session.setAttribute("Message", irBAN.getMessage());
          if (irBAN.getIsDirect())
          {
            BAN.setBanStatus("Implemented");
            BAN.setBanIdentifier("");
	  %>
	     <jsp:forward page="Welcome.jsp"/>
	  <%
          }
          else
          {
	  %>
	     <jsp:forward page="ListBans.jsp"/>
	  <%
          }
	}
	else
	{
          %>
	    <jsp:forward page="InvoiceRegionBAN.jsp"/>
          <%
	}
      }
      else if ((ButtonPressed.startsWith("Return")) || (ButtonPressed.startsWith("Reject")))
      {
	irBAN.setBanStatus(ButtonPressed+"ed");
	irBAN.setAction(ButtonPressed);
	irBAN.setRejectReason((String)request.getParameter("RejectReason"));
	irBAN.setUserId((String)session.getAttribute("User_Id"));
        if(irBAN.updateInvoiceRegionBAN())
	{
	  session.setAttribute("Message",irBAN.getMessage());
	  %>
	     <jsp:forward page="Welcome.jsp"/>
	  <%
	}
	else
	{
	  session.setAttribute("Message",irBAN.getMessage());
	  %>
	     <jsp:forward page="InvoiceRegionBAN.jsp"/>
	  <%
	}
      }
      else if (ButtonPressed.startsWith("Cancel"))
      {
	irBAN.setBanStatus("Canceled");
	irBAN.setUserId((String)session.getAttribute("User_Id"));
        if(irBAN.updateInvoiceRegionBAN())
	{
	  session.setAttribute("Message",irBAN.getMessage());
	  %>
	     <jsp:forward page="Welcome.jsp"/>
	  <%
	}
	else
	{
	  %>
	     <jsp:forward page="InvoiceRegionBAN.jsp"/>
	  <%
	}
      }
      else if ((ButtonPressed.startsWith("Save Draft")) ||
	      (ButtonPressed.startsWith("Submit")))
      {
	//store form values in Bean
	while (FormFields.hasMoreElements())
	{
	  FormField=(String)FormFields.nextElement();
	  irBAN.setParameter(FormField,request.getParameter(FormField));
	}
	//Validation

	if (!irBAN.isValid(ButtonPressed))
	{
	  %>
	  <jsp:forward page="InvoiceRegionBAN.jsp"/>
	  <%
	}
	else
	{
	  if (ButtonPressed.startsWith("Save Draft"))
	  {
	    irBAN.setBanStatus("Draft");
	  }
	  else
	  {
	    irBAN.setBanStatus("Proposed");
	  }
	  //set non form values
	  irBAN.setBanCreatedBy((String)session.getAttribute("User_Id"));
	  irBAN.setUserId((String)session.getAttribute("User_Id"));

	  //Create or amend a BAN

          String banId = "";
	  if (irBAN.getAction().startsWith("Create"))
	  {
	    Success=irBAN.createInvoiceRegionBAN();
            banId = irBAN.getBanIdentifier();
	  }
	  else
	  {
            banId = irBAN.getBanIdentifier();
	    Success=irBAN.updateInvoiceRegionBAN();
	  }
	  if (Success)
	  {
            BAN.setBanIdentifier(banId);
	    session.setAttribute("Message",irBAN.getMessage());
	    %>
	      <jsp:forward page="Welcome.jsp"/>
	    <%
	  }
	  else
	  {//Failed
	    %>
	    <jsp:forward page="InvoiceRegionBAN.jsp"/>
	    <%
	  }//end of create
	}//end of if valid
      }
      else
      {//no button pressed, so must have been an autorefresh from a dropdown
	//store form values in Bean
	while (FormFields.hasMoreElements())
	{
	  FormField=(String)FormFields.nextElement();
	  irBAN.setParameter(FormField,request.getParameter(FormField));
	}
        %>
          <jsp:forward page="InvoiceRegionBAN.jsp"/>
        <%
      }
    %>
</body>
</html>
