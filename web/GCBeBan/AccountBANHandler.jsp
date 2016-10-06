<html>
<head>
</head>
<%@ page import="JavaUtil.*,java.util.Enumeration"%>
<jsp:useBean id="cuBAN" class="DBUtilities.CustomerBANBean" scope="session"/>
    <%!
        String ButtonPressed,Action;
	Enumeration FormFields;
	String FormField;
	boolean Errors,Success;
    %>
    <%
      ButtonPressed=(String)request.getParameter("ButtonPressed");
      session.setAttribute("Button",ButtonPressed);
      Action=cuBAN.getAction();
      FormFields=request.getParameterNames();
      //Deal with non submit actions first
      if (ButtonPressed.startsWith("Clear"))
      {
	while (FormFields.hasMoreElements())
	{
	  FormField=(String)FormFields.nextElement();
	  cuBAN.setParameter(FormField,"");
	}
        %>
        <jsp:forward page="AccountBAN.jsp"/>
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
	cuBAN.setUserId((String)session.getAttribute("User_Id"));
        if(cuBAN.AuthoriseCustomerBAN())
	{
	  session.setAttribute("Message","You have successfully implemented BAN Id :-"+cuBAN.getBanIdentifier());
	  %>
	     <jsp:forward page="ListBans.jsp"/>
	  <%
	}
	else
	{
          %>
	    <jsp:forward page="AccountBAN.jsp"/>
          <%
	}
      }
      else if ((ButtonPressed.startsWith("Return")) || (ButtonPressed.startsWith("Reject")))
      {
	cuBAN.setBanStatus(ButtonPressed+"ed");
	cuBAN.setAction(ButtonPressed);
	cuBAN.setRejectReason((String)request.getParameter("RejectReason"));
	cuBAN.setUserId((String)session.getAttribute("User_Id"));
        if(cuBAN.updateCustomerBAN())
	{
	  session.setAttribute("Message",cuBAN.getMessage());
	  %>
	     <jsp:forward page="Welcome.jsp"/>
	  <%
	}
	else
	{
	  session.setAttribute("Message",cuBAN.getMessage());
	  %>
	     <jsp:forward page="AccountBAN.jsp"/>
	  <%
	}
      }
      else if (ButtonPressed.startsWith("Cancel"))
      {
	cuBAN.setBanStatus("Canceled");
	cuBAN.setUserId((String)session.getAttribute("User_Id"));
        if(cuBAN.updateCustomerBAN())
	{
	  session.setAttribute("Message",cuBAN.getMessage());
	  %>
	     <jsp:forward page="Welcome.jsp"/>
	  <%
	}
	else
	{
	  %>
	     <jsp:forward page="AccountBAN.jsp"/>
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
	  cuBAN.setParameter(FormField,request.getParameter(FormField));
	}
	//Validation

	if (!cuBAN.isValid(ButtonPressed))
	{
	  %>
	  <jsp:forward page="AccountBAN.jsp"/>
	  <%
	}
	else
	{
	  if (ButtonPressed.startsWith("Save Draft"))
	  {
	    cuBAN.setBanStatus("Draft");
	  }
	  else
	  {
	    cuBAN.setBanStatus("Proposed");
	  }
	  //set non form values
	  cuBAN.setBanCreatedBy((String)session.getAttribute("User_Name"));
	  cuBAN.setUserId((String)session.getAttribute("User_Id"));

	  //Create or amend a BAN

	  if (cuBAN.getAction().startsWith("Create"))
	  {
	    Success=cuBAN.createCustomerBAN();
	  }
	  else
	  {
	    Success=cuBAN.updateCustomerBAN();
	  }
	  if (Success)
	  {
	    session.setAttribute("Message",cuBAN.getMessage());
	    %>
	      <jsp:forward page="Welcome.jsp"/>
	    <%
	  }
	  else
	  {//Failed
	    %>
	    <jsp:forward page="AccountBAN.jsp"/>
	    <%
	  }//end of create
	}//end of if valid
      }
      else
      {//no button pressed, so must have been an autorefresh from a dropdown
        %>
          <jsp:forward page="AccountBAN.jsp"/>
        <%
      }
    %>
</body>
</html>
