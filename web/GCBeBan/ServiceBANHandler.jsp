<html>
<head>
</head>
<%@ page import="JavaUtil.*,java.util.Enumeration"%>
<jsp:useBean id="svBAN" class="DBUtilities.ServiceBANBean" scope="session"/>
    <%!
        String Error_Message,Message,ButtonPressed,Mode,Action;
	Enumeration FormFields;
	String FormField;
	boolean Errors;
	StringUtil SU = new StringUtil();
    %>
    <%

      ButtonPressed=(String)request.getParameter("ButtonPressed");
      session.setAttribute("Button",ButtonPressed);
      Action=svBAN.getAction();
      FormFields=request.getParameterNames();

      if (ButtonPressed.startsWith("Clear"))
      {
        FormFields=request.getParameterNames();
	while (FormFields.hasMoreElements())
	{
	  FormField=(String)FormFields.nextElement();
	  svBAN.setParameter(FormField,"");
	}
      }
      if (ButtonPressed.startsWith("Create BAN Menu"))
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
      if ((ButtonPressed.startsWith("Return")) || (ButtonPressed.startsWith("Reject")))
      {
	svBAN.setBanStatus(ButtonPressed+"ed");
	svBAN.setAction(ButtonPressed);
	svBAN.setRejectReason((String)request.getParameter("RejectReason"));
	svBAN.setUserId((String)session.getAttribute("User_Id"));
        if(svBAN.updateServiceBAN())
	{
	  session.setAttribute("Message",svBAN.getMessage());
	  %>
	     <jsp:forward page="Welcome.jsp"/>
	  <%
	}
	else
	{
	  session.setAttribute("Message",svBAN.getMessage());
	  %>
	     <jsp:forward page="ServiceBAN.jsp"/>
	  <%
	}
      }
      else if (ButtonPressed.startsWith("Cancel"))
      {
	svBAN.setBanStatus("Canceled");
	svBAN.setUserId((String)session.getAttribute("User_Id"));
        if(svBAN.updateServiceBAN())
	{
	  session.setAttribute("Message",svBAN.getMessage());
	  %>
	     <jsp:forward page="Welcome.jsp"/>
	  <%
	}
	else
	{
	  session.setAttribute("Message",svBAN.getMessage());
	  %>
	     <jsp:forward page="ServiceBAN.jsp"/>
	  <%
	}
      }
      if ((ButtonPressed.startsWith("Save Draft"))   ||
	  (ButtonPressed.startsWith("Submit"))
  	)
      {
        //store form values in Bean
        while (FormFields.hasMoreElements())
        {
	  FormField=(String)FormFields.nextElement();
	  svBAN.setParameter(FormField,request.getParameter(FormField));
        }
        //Validation
        if (!svBAN.isServiceValid(ButtonPressed))
        {
            %>
            <jsp:forward page="ServiceBAN.jsp"/>
            <%
        }
        else
        {//Set non-form BAN values
	  if (ButtonPressed.startsWith("Submit"))
	  {
	    svBAN.setBanStatus("Proposed");
	  }
	  else if (ButtonPressed.startsWith("Authorise"))
	  {
	    svBAN.setBanStatus("Authorised");
	  }
	  else
	  {
	    svBAN.setBanStatus("Draft");
	  }

	  svBAN.setUserId((String)session.getAttribute("User_Id"));
	  //Create/Amend a BAN
          if (Action.startsWith("Create"))
	  {
	    if (svBAN.createServiceBAN())
	    {//Success
	      session.setAttribute("Message",svBAN.getMessage());
	      %>
		<jsp:forward page="Welcome.jsp"/>
	      <%
	    }
	    else
	    {//Failed
	      %>
	      <jsp:forward page="ServiceBAN.jsp"/>
	      <%
	    }
	  }
	  else if (Action.compareTo("Amend")==0)
	  {
	    if (svBAN.updateServiceBAN())
	    {//Success
	      session.setAttribute("Message",svBAN.getMessage());
		%>
		  <jsp:forward page="Welcome.jsp"/>
		<%
	    }
	    else
	    {//Failed
	      %>
	        <jsp:forward page="ServiceBAN.jsp"/>
	      <%
	    }
	  }//end of if create
	}//end of If valid
      }//end of save button pressed
      else if (ButtonPressed.startsWith("Authorise"))
      {
	if (svBAN.ImplementServiceBAN())
	{
	  if (svBAN.getMode().compareTo("Create") == 0)
	  {
	    session.setAttribute("Message","<font color=red>Service " +svBAN.getService_Reference()+ " Created<br>");
            %>
              <jsp:forward page="ListBans.jsp"/>
	    <%
	  }
	  else
	  {
	    Message = "<font color=red>Service Amended<br>";
	    session.setAttribute("Message",Message);
	    %>
	      <jsp:forward page="ListBans.jsp"/>
	    <%
	  }
	}
	else
	{//Failed
	  %>
	    <jsp:forward page="ServiceBAN.jsp"/>
	  <%
	}
      }
      else
      {//no button pressed, so must have been an autorefresh from a dropdown
	if ((Mode.compareTo("Create") == 0) && (svBAN.getMessage().startsWith("<font color=red><b>Invalid Service Reference")))
	{
	    svBAN.setMessage("");
	}
        %>
          <jsp:forward page="ServiceBAN.jsp"/>
        <%
      }
      %>
</body>
</html>