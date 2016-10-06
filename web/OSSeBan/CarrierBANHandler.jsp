<html>
<head>
</head>
<%@ page import="JavaUtil.*,java.util.Enumeration"%>
<jsp:useBean id="BAN" class="DBUtilities.BANBean" scope="session"/>
<jsp:useBean id="caBAN" class="DBUtilities.CarrierBANBean" scope="session"/>
    <%!
        String ButtonPressed,Action;
	Enumeration FormFields;
	String FormField;
	boolean Errors,Success;
    %>
    <%
      ButtonPressed=(String)request.getParameter("ButtonPressed");
      session.setAttribute("Button",ButtonPressed);
      Action=caBAN.getAction();
      FormFields=request.getParameterNames();
      //Deal with non submit actions first
      if (ButtonPressed.startsWith("Clear"))
      {
	while (FormFields.hasMoreElements())
	{
	  FormField=(String)FormFields.nextElement();
	  caBAN.setParameter(FormField,"");
	}
        %>
        <jsp:forward page="CarrierBAN.jsp"/>
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
	caBAN.setUserId((String)session.getAttribute("User_Id"));
        if(caBAN.ImplementCarrierBAN())
	{

	  session.setAttribute("Message","You have successfully implemented BAN Id :-"+caBAN.getBanIdentifier());
          if (caBAN.getIsDirect())
          {
            BAN.setBanStatus("Implemented");
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
	    <jsp:forward page="CarrierBAN.jsp"/>
          <%
	}
      }
      else if ((ButtonPressed.startsWith("Return")) || (ButtonPressed.startsWith("Reject")))
      {
	caBAN.setBanStatus(ButtonPressed+"ed");
	caBAN.setAction(ButtonPressed);
	caBAN.setRejectReason((String)request.getParameter("RejectReason"));
	caBAN.setUserId((String)session.getAttribute("User_Id"));
        if(caBAN.updateCarrierBAN())
	{
	  session.setAttribute("Message",caBAN.getMessage());
	  %>
	     <jsp:forward page="Welcome.jsp"/>
	  <%
	}
	else
	{
	  session.setAttribute("Message",caBAN.getMessage());
	  %>
	     <jsp:forward page="CarrierBAN.jsp"/>
	  <%
	}
      }
      else if (ButtonPressed.startsWith("Cancel"))
      {
	caBAN.setBanStatus("Canceled");
	caBAN.setUserId((String)session.getAttribute("User_Id"));
        if(caBAN.updateCarrierBAN())
	{
          BAN.setBanStatus("Canceled");
	  session.setAttribute("Message",caBAN.getMessage());
	  %>
	     <jsp:forward page="Welcome.jsp"/>
	  <%
	}
	else
	{
	  session.setAttribute("Message",caBAN.getMessage());
	  %>
	     <jsp:forward page="CarrierBAN.jsp"/>
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
	  caBAN.setParameter(FormField,request.getParameter(FormField));
	}
	//Validation

	if (!caBAN.isValid(ButtonPressed))
	{
	  %>
	  <jsp:forward page="CarrierBAN.jsp"/>
	  <%
	}
	else
	{
	  if (ButtonPressed.startsWith("Save Draft"))
	  {
	    caBAN.setBanStatus("Draft");
	  }
	  else
	  {
	    caBAN.setBanStatus("Proposed");
	  }
	  //set non form values
	  caBAN.setBanCreatedBy((String)session.getAttribute("User_Name"));
	  caBAN.setUserId((String)session.getAttribute("User_Id"));

	  //Create or amend a BAN

          String banId = "";
	  if (caBAN.getAction().startsWith("Create"))
	  {
	    Success=caBAN.createCarrierBAN();
            banId = caBAN.getBanIdentifier();
	  }
	  else
	  {
            banId = caBAN.getBanIdentifier();
	    Success=caBAN.updateCarrierBAN();
	  }
	  if (Success)
	  {
            BAN.setBanIdentifier(banId);
	    session.setAttribute("Message",caBAN.getMessage());
	    %>
	      <jsp:forward page="Welcome.jsp"/>
	    <%
	  }
	  else
	  {//Failed
	    %>
	    <jsp:forward page="CarrierBAN.jsp"/>
	    <%
	  }//end of create
	}//end of if valid
      }
      else
      {//no button pressed, so must have been an autorefresh from a dropdown
        %>
          <jsp:forward page="CarrierBAN.jsp"/>
        <%
      }
    %>
</body>
</html>
