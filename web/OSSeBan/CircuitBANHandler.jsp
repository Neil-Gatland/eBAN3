<html>
<head>
</head>
<%@ page isThreadSafe="false" %>
<%@ page import="JavaUtil.*,java.util.Enumeration"%>
<%@ page import="DBUtilities.OSSChargeBANBean"%>
<jsp:useBean id="BAN" class="DBUtilities.BANBean" scope="session"/>
<jsp:useBean id="ctBAN" class="DBUtilities.CircuitBANBean" scope="session"/>
<!--jsp:useBean id="chBAN" class="DBUtilities.OSSChargeBANBean" scope="session"/-->
    <%
      String Error_Message,Message,ButtonPressed,Mode,Action;
      Enumeration FormFields;
      String FormField,PageSent="";
      boolean Errors;
      JavaUtil.UserData UD = new UserData();
      StringUtil SU = new StringUtil();

      String userId = (String)session.getAttribute("User_Id");
      OSSChargeBANBean chBAN = (OSSChargeBANBean)session.getAttribute("chBAN" + userId);
      if (chBAN == null)
      {
        chBAN = new OSSChargeBANBean();
        session.setAttribute("chBAN" + userId, chBAN);
      }
      Mode=ctBAN.getMode();
      Action=ctBAN.getAction();
      //PageSent=(String)session.getAttribute("PageSent");
      //session.setAttribute("PageSent",request.getRequestURI());
      ButtonPressed=(String)request.getParameter("ButtonPressed");
      if (ButtonPressed.startsWith("Clear"))
      {
        FormFields=request.getParameterNames();
	while (FormFields.hasMoreElements())
	{
	  FormField=(String)FormFields.nextElement();
	  ctBAN.setParameter(FormField,"");
	}
	if (PageSent.endsWith("BAN.jsp"))
	{
          %>
            <jsp:forward page="CircuitBAN.jsp"/>
          <%
	}
	else
	{
          %>
            <jsp:forward page="CircuitBAN2.jsp"/>
          <%
	}
      }
      if (ButtonPressed.startsWith("Create BAN Menu"))
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
      else if (ButtonPressed.equals("Raise Query"))
      {
        %>
          <jsp:forward page="CircuitBAN.jsp">
            <jsp:param name="mode" value="<%=Mode%>"/>
            <jsp:param name="query" value="true"/>
            <jsp:param name="fromPage" value="CircuitBAN"/>
          </jsp:forward>
        <%
      }
      //store form values in Bean
      FormFields=request.getParameterNames();
      while (FormFields.hasMoreElements())
      {
	FormField=(String)FormFields.nextElement();
	ctBAN.setParameter(FormField,request.getParameter(FormField));
      }
      if (ButtonPressed.startsWith("Page 1"))
      {
            %>
            <jsp:forward page="CircuitBAN.jsp"/>
            <%
      }
      if (ButtonPressed.startsWith("Page 2"))
      {
            %>
            <jsp:forward page="CircuitBAN2.jsp"/>
            <%
      }
      else if ((ButtonPressed.startsWith("Return")) || (ButtonPressed.startsWith("Reject")))
      {
	ctBAN.setBanStatus(ButtonPressed+"ed");
	ctBAN.setAction(ButtonPressed);
	ctBAN.setRejectReason((String)request.getParameter("RejectReason"));
	ctBAN.setUserId((String)session.getAttribute("User_Id"));
        if(ctBAN.updateCircuitBAN())
	{
	  session.setAttribute("Message",ctBAN.getMessage());
	  %>
	     <jsp:forward page="Welcome.jsp"/>
	  <%
	}
	else
	{
	  session.setAttribute("Message",ctBAN.getMessage());
	  %>
	     <jsp:forward page="CircuitBAN.jsp"/>
	  <%
	}
      }
      else if (ButtonPressed.startsWith("Cancel"))
      {
	ctBAN.setBanStatus("Canceled");
	//ctBAN.setRejectReason((String)request.getParameter("RejectReason"));
	ctBAN.setUserId((String)session.getAttribute("User_Id"));
        if(ctBAN.updateCircuitBAN())
	{
          BAN.setBanStatus("Canceled");
	  session.setAttribute("Message",ctBAN.getMessage());
	  %>
	     <jsp:forward page="Welcome.jsp"/>
	  <%
	}
	else
	{
	  session.setAttribute("Message",ctBAN.getMessage());
	  %>
	     <jsp:forward page="CircuitBAN.jsp"/>
	  <%
	}
      }
      if ((ButtonPressed.startsWith("Save Draft"))   ||
	  (ButtonPressed.startsWith("Submit"))
  	)
      {
        //Validation
        if (!ctBAN.isCircuitValid(ButtonPressed))
        {
            %>
            <jsp:forward page="CircuitBAN.jsp"/>
            <%
        }
        else
        {//Set non-form BAN values
	  /*if (ButtonPressed.startsWith("Submit"))
	  {
	    //ctBAN.setBanStatus("Proposed");
	    ctBAN.setBanStatus("Implemented");
	  }
	  else if (ButtonPressed.startsWith("Authorise"))
	  {
	    ctBAN.setBanStatus("Implemented");
	  }
	  else
	  {
	    ctBAN.setBanStatus("Draft");
	  }*/

	  ctBAN.setUserId((String)session.getAttribute("User_Id"));
	  //Create/Amend a BAN
          if (Action.startsWith("Create"))
	  {
            boolean success = false;
            if (ButtonPressed.startsWith("Submit"))
              success = ctBAN.authoriseCircuitBAN();
            else
              success = ctBAN.createCircuitBAN();

	    if (success)
	    {//Success - get the BAN ID
              BAN.setBanIdentifier(ctBAN.getBanIdentifier());
	      session.setAttribute("Message",ctBAN.getMessage());
	      %>
		<jsp:forward page="OSSBANMenu.jsp"/>
	      <%
	    }
	    else
	    {//Failed
	      %>
	      <jsp:forward page="CircuitBAN.jsp"/>
	      <%
	    }
	  }
	  else if (Action.compareTo("Amend")==0)
	  {
            String banId = ctBAN.getBanIdentifier();
	    if (ctBAN.updateCircuitBAN())
	    {//Success
              BAN.setBanIdentifier(banId);
              ctBAN.ImplementCircuitBAN();
	      session.setAttribute("Message",ctBAN.getMessage());
		%>
		  <jsp:forward page="Welcome.jsp"/>
		<%
	    }
	    else
	    {//Failed
	      %>
	        <jsp:forward page="CircuitBAN.jsp"/>
	      <%
	    }
	  }//end of if create
	}//end of If valid
      }//end of save button pressed
      else if (ButtonPressed.startsWith("Authorise"))
      {
	if (ctBAN.ImplementCircuitBAN())
	{
          if (ctBAN.getIsDirect())
          {
            BAN.setBanStatus("Implemented");
          }
	  if (ctBAN.getMode().compareTo("Create") == 0)
	  {
	    //ctBAN.getCircuit();
	    chBAN.setCircuit_Reference(ctBAN.getCircuit_Reference());
	    chBAN.setBanIdentifier("");

	    chBAN.setAction("Create");
	    chBAN.setMode("Create");

	    chBAN.setMessage("<font color=red>You have successfully created Circuit:-"+ctBAN.getCircuit_Reference()+"<br>Please create the appropriate charges for this circuit<br>");
	    chBAN.setGlobalCustomerId(ctBAN.getGlobalCustomerId());
	    chBAN.setGlobalCustomerName(ctBAN.getGlobalCustomerName());
	    chBAN.setAccount_Id(ctBAN.getAccount_Id());
	    chBAN.setCircuit_Reference(ctBAN.getCircuit_Reference());

	    chBAN.setCurrency_Desc(ctBAN.getCurrency_Desc());

	    chBAN.setCharge_Category("01 Recurring Charge");

	    //chBAN.setCharge_Entity("C&W");

            %>
              <jsp:forward page="ChargeBAN.jsp"/>
	    <%
	  }
	  else
	  {
	    Message = "<font color=red>Circuit Amended<br>";
	    session.setAttribute("Message",Message);
            if (ctBAN.getIsDirect())
            {
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
	}
	else
	{//Failed
	  %>
	    <jsp:forward page="CircuitBAN.jsp"/>
	  <%
	}
      }
      else
      {//no button pressed, so must have been an autorefresh from a dropdown
	if ((Mode.compareTo("Create") == 0) && (ctBAN.getMessage().startsWith("<font color=red><b>Invalid Circuit Reference")))
	{
	    ctBAN.setMessage("");
	}
        %>
          <jsp:forward page="CircuitBAN.jsp"/>
        <%
      }
      %>
</body>
</html>