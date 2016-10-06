<html>
<head>
</head>
<%@ page import="JavaUtil.*,java.util.Enumeration"%>
<jsp:useBean id="BAN" class="DBUtilities.BANBean" scope="session"/>
<jsp:useBean id="gcBAN" class="DBUtilities.GCBCustomerBANBean" scope="session"/>
    <%!
        String ButtonPressed,Action;
	Enumeration FormFields;
	String FormField;
	boolean Errors,Success;
    %>
    <%
      ButtonPressed=(String)request.getParameter("ButtonPressed");
      session.setAttribute("Button",ButtonPressed);
      Action=gcBAN.getAction();
      FormFields=request.getParameterNames();
      //Deal with non submit actions first
      if (ButtonPressed.startsWith("Clear"))
      {
	while (FormFields.hasMoreElements())
	{
	  FormField=(String)FormFields.nextElement();
	  gcBAN.setParameter(FormField,"");
	}
        %>
        <jsp:forward page="GCBCustomerBAN.jsp"/>
        <%
      }
      else if (ButtonPressed.equals("Raise Query"))
      {
        %>
          <jsp:forward page="GCBCustomerBAN.jsp">
           <jsp:param name="mode" value="<%=gcBAN.getMode()%>"/>
           <jsp:param name="query" value="true"/>
            <jsp:param name="fromPage" value="GCBCustomerBAN"/>
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
	gcBAN.setUserId((String)session.getAttribute("User_Id"));
        if(gcBAN.AuthoriseCustomerBAN())
	{
	  //session.setAttribute("Message","You have successfully implemented BAN Id :-"+gcBAN.getBanIdentifier());
	  session.setAttribute("Message", gcBAN.getMessage());
          if (gcBAN.getIsDirect())
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
	    <jsp:forward page="GCBCustomerBAN.jsp"/>
          <%
	}
      }
      else if ((ButtonPressed.startsWith("Return")) || (ButtonPressed.startsWith("Reject")))
      {
	gcBAN.setBanStatus(ButtonPressed+"ed");
	gcBAN.setAction(ButtonPressed);
	gcBAN.setRejectReason((String)request.getParameter("RejectReason"));
	gcBAN.setUserId((String)session.getAttribute("User_Id"));
        if(gcBAN.updateCustomerBAN())
	{
	  session.setAttribute("Message",gcBAN.getMessage());
	  %>
	     <jsp:forward page="Welcome.jsp"/>
	  <%
	}
	else
	{
	  session.setAttribute("Message",gcBAN.getMessage());
	  %>
	     <jsp:forward page="GCBCustomerBAN.jsp"/>
	  <%
	}
      }
      else if (ButtonPressed.startsWith("Cancel"))
      {
	gcBAN.setBanStatus("Canceled");
	gcBAN.setUserId((String)session.getAttribute("User_Id"));
        if(gcBAN.updateCustomerBAN())
	{
	  session.setAttribute("Message",gcBAN.getMessage());
	  %>
	     <jsp:forward page="Welcome.jsp"/>
	  <%
	}
	else
	{
	  %>
	     <jsp:forward page="GCBCustomerBAN.jsp"/>
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
	  gcBAN.setParameter(FormField,request.getParameter(FormField));
	}
	gcBAN.setUserId((String)session.getAttribute("User_Id"));
	//Validation

	if ((!gcBAN.isValid(ButtonPressed)) ||
          (!gcBAN.setGlobalCustomerIdfromName(request.getParameter("Global_Customer_Name"))))
	{
	  %>
	  <jsp:forward page="GCBCustomerBAN.jsp"/>
	  <%
	}
	else
	{
	  if (ButtonPressed.startsWith("Save Draft"))
	  {
	    gcBAN.setBanStatus("Draft");
	  }
	  else
	  {
	    gcBAN.setBanStatus("Proposed");
	  }
	  //set non form values
	  gcBAN.setBanCreatedBy((String)session.getAttribute("User_Id"));
	  gcBAN.setUserId((String)session.getAttribute("User_Id"));

	  //Create or amend a BAN

          String banId = "";
	  if (gcBAN.getAction().startsWith("Create"))
	  {
	    Success=gcBAN.createCustomerBAN();
            banId = gcBAN.getBanIdentifier();
	  }
	  else
	  {
            banId = gcBAN.getBanIdentifier();
	    Success=gcBAN.updateCustomerBAN();
            if (gcBAN.getIsMANS()) //ban id might change on update
              banId = gcBAN.getBanIdentifier();
	  }
	  if (Success)
	  {
            BAN.setBanIdentifier(banId);
	    session.setAttribute("Message",gcBAN.getMessage());
	    %>
	      <jsp:forward page="Welcome.jsp"/>
	    <%
	  }
	  else
	  {//Failed
	    %>
	    <jsp:forward page="GCBCustomerBAN.jsp"/>
	    <%
	  }//end of create
	}//end of if valid
      }
      else
      {//no button pressed, so must have been an autorefresh from a dropdown
        gcBAN.setErrored("clear");

        while (FormFields.hasMoreElements())
        {
          FormField=(String)FormFields.nextElement();
          gcBAN.setParameter(FormField,request.getParameter(FormField));
        }
        %>
          <jsp:forward page="GCBCustomerBAN.jsp"/>
        <%
      }
    %>
</body>
</html>
