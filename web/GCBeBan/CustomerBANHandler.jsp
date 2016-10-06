<html>
<head>
</head>
<%@ page import="JavaUtil.*,java.util.Enumeration"%>
<jsp:useBean id="ncBAN" class="DBUtilities.NewCustomerBANBean" scope="session"/>
    <%!
        String ButtonPressed,Action;
	Enumeration FormFields;
	String FormField;
	boolean Errors,Success;
    %>
    <%
      ButtonPressed=(String)request.getParameter("ButtonPressed");
      session.setAttribute("Button",ButtonPressed);
      Action=ncBAN.getAction();
      FormFields=request.getParameterNames();
      //Deal with non submit actions first
      if (ButtonPressed.startsWith("Clear"))
      {
	while (FormFields.hasMoreElements())
	{
	  FormField=(String)FormFields.nextElement();
	  ncBAN.setParameter(FormField,"");
	}
        %>
        <jsp:forward page="CustomerBAN.jsp"/>
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
	ncBAN.setUserId((String)session.getAttribute("User_Id"));
        if(ncBAN.AuthoriseCustomerBAN())
	{
	  session.setAttribute("Message","You have successfully implemented BAN Id :-"+ncBAN.getBanIdentifier());
	  %>
	     <jsp:forward page="ListBans.jsp"/>
	  <%
	}
	else
	{
          %>
	    <jsp:forward page="CustomerBAN.jsp"/>
          <%
	}
      }
      else if ((ButtonPressed.startsWith("Return")) || (ButtonPressed.startsWith("Reject")))
      {
	ncBAN.setBanStatus(ButtonPressed+"ed");
	ncBAN.setAction(ButtonPressed);
	ncBAN.setRejectReason((String)request.getParameter("RejectReason"));
	ncBAN.setUserId((String)session.getAttribute("User_Id"));
        if(ncBAN.updateCustomerBAN())
	{
	  session.setAttribute("Message",ncBAN.getMessage());
	  %>
	     <jsp:forward page="Welcome.jsp"/>
	  <%
	}
	else
	{
	  session.setAttribute("Message",ncBAN.getMessage());
	  %>
	     <jsp:forward page="CustomerBAN.jsp"/>
	  <%
	}
      }
      else if (ButtonPressed.startsWith("Cancel"))
      {
	ncBAN.setBanStatus("Canceled");
	ncBAN.setUserId((String)session.getAttribute("User_Id"));
        if(ncBAN.updateCustomerBAN())
	{
	  session.setAttribute("Message",ncBAN.getMessage());
	  %>
	     <jsp:forward page="Welcome.jsp"/>
	  <%
	}
	else
	{
	  %>
	     <jsp:forward page="CustomerBAN.jsp"/>
	  <%
	}
      }
      else if ((ButtonPressed.startsWith("Save Draft")) ||
	      (ButtonPressed.startsWith("Submit")))
      {
	//store form values in Bean
	FormFields=request.getParameterNames();
	while (FormFields.hasMoreElements())
	{
	  FormField=(String)FormFields.nextElement();
	  ncBAN.setParameter(FormField,request.getParameter(FormField));
	}
	//Validation

	if (!ncBAN.isValid(ButtonPressed))
	{
	  %>
	  <jsp:forward page="CustomerBAN.jsp"/>
	  <%
	}
	else
	{
	  if (ButtonPressed.startsWith("Save Draft"))
	  {
	    ncBAN.setBanStatus("Draft");
	  }
	  else
	  {
	    ncBAN.setBanStatus("Proposed");
	  }
	  //set non form values
	  ncBAN.setBanCreatedBy((String)session.getAttribute("User_Name"));
	  ncBAN.setUserId((String)session.getAttribute("User_Id"));

	  //Create or amend a BAN

	  if (ncBAN.getAction().startsWith("Create"))
	  {
	    Success=ncBAN.createCustomerBAN();
	  }
	  else
	  {
	    Success=ncBAN.updateCustomerBAN();
	  }
	  if (Success)
	  {
	    session.setAttribute("Message",ncBAN.getMessage());
	    %>
	      <jsp:forward page="Welcome.jsp"/>
	    <%
	  }
	  else
	  {//Failed
	    %>
	    <jsp:forward page="CustomerBAN.jsp"/>
	    <%
	  }//end of create
	}//end of if valid
      }
      else
      {//no button pressed, so must have been an autorefresh from a dropdown
        ncBAN.setErrored("clear");

        while (FormFields.hasMoreElements())
        {
          FormField=(String)FormFields.nextElement();
          ncBAN.setParameter(FormField,request.getParameter(FormField));
        }
        %>
          <jsp:forward page="CustomerBAN.jsp"/>
        <%
      }
    %>
</body>
</html>
