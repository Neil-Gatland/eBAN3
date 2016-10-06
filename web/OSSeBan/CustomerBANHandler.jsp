<html>
<head>
</head>
<%@ page import="JavaUtil.*,java.util.Enumeration"%>
<jsp:useBean id="BAN" class="DBUtilities.BANBean" scope="session"/>
<jsp:useBean id="cuBAN" class="DBUtilities.OSSCustomerBANBean" scope="session"/>
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
        <jsp:forward page="CustomerBAN.jsp"/>
        <%
      }
      else if (ButtonPressed.startsWith("Create BAN Menu"))
      {
        %>
          <jsp:forward page="OSSBANMenu.jsp"/>
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
          if (cuBAN.getIsDirect())
          {
	  %>
            BAN.setBanStatus("Implemented");
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
	    <jsp:forward page="CustomerBAN.jsp"/>
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
	     <jsp:forward page="CustomerBAN.jsp"/>
	  <%
	}
      }
      else if (ButtonPressed.startsWith("Cancel"))
      {
	cuBAN.setBanStatus("Canceled");
	cuBAN.setUserId((String)session.getAttribute("User_Id"));
        if(cuBAN.updateCustomerBAN())
	{
          BAN.setBanStatus("Canceled");
	  session.setAttribute("Message",cuBAN.getMessage());
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
	  cuBAN.setParameter(FormField,request.getParameter(FormField));
	}
	//Validation

	if (!cuBAN.isValid(ButtonPressed))
	{
	  %>
	  <jsp:forward page="CustomerBAN.jsp"/>
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

          String banId = "";
	  if (cuBAN.getAction().startsWith("Create"))
	  {
	    Success=cuBAN.createCustomerBAN();
            banId = cuBAN.getBanIdentifier();
	  }
	  else
	  {
            banId = cuBAN.getBanIdentifier();
	    Success=cuBAN.updateCustomerBAN();
	  }
	  if (Success)
	  {
            BAN.setBanIdentifier(banId);
	    session.setAttribute("Message",cuBAN.getMessage());
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
        %>
          <jsp:forward page="CustomerBAN.jsp"/>
        <%
      }
    %>
</body>
</html>
