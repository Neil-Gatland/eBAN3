<html>
<head>
</head>
<%@ page isThreadSafe="false" %>
<%@ page import="JavaUtil.*,java.util.Enumeration"%>
<%@ page import="DBUtilities.OSSChargeBANBean"%>
<jsp:useBean id="BAN" class="DBUtilities.BANBean" scope="session"/>
<!--jsp:useBean id="chBAN" class="DBUtilities.OSSChargeBANBean" scope="session"/-->
<jsp:useBean id="ctBAN" class="DBUtilities.CircuitBANBean" scope="session"/>
    <%
        String ButtonPressed,Action,Mode;
	Enumeration FormFields;
	String FormField;
	boolean Errors,Success;

      String userId = (String)session.getAttribute("User_Id");
      OSSChargeBANBean chBAN = (OSSChargeBANBean)session.getAttribute("chBAN" + userId);
      if (chBAN == null)
      {
        chBAN = new OSSChargeBANBean();
        session.setAttribute("chBAN" + userId, chBAN);
      }
      String chargeId = request.getParameter("chargeId");
      ButtonPressed=(String)request.getParameter("ButtonPressed");
      session.setAttribute("Button",ButtonPressed);
      Action=chBAN.getAction();
      Mode=chBAN.getMode();
      FormFields=request.getParameterNames();
      //Deal with non submit actions first
      if (ButtonPressed.startsWith("Clear"))
      {
	while (FormFields.hasMoreElements())
	{
	  FormField=(String)FormFields.nextElement();
	  chBAN.setParameter(FormField,"");
	}
        %>
          <jsp:forward page="ChargeBAN.jsp">
            <jsp:param name="chargeId" value="<%=chargeId%>"/>
          </jsp:forward>
        <%
      }
      else if (ButtonPressed.equals("Raise Query"))
      {
        %>
          <jsp:forward page="ChargeBAN.jsp">
            <jsp:param name="mode" value="<%=Mode%>"/>
            <jsp:param name="query" value="true"/>
            <jsp:param name="fromPage" value="ChargeBAN"/>
            <jsp:param name="chargeId" value="<%=chargeId%>"/>
          </jsp:forward>
        <%
      }
      else if (ButtonPressed.startsWith("Re-Use"))
      {
        chBAN.getChargeBan();
        %>
          <jsp:forward page="ChargeBAN.jsp">
            <jsp:param name="chargeId" value="<%=chargeId%>"/>
          </jsp:forward>
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
          <jsp:forward page="ListCharges.jsp"/>
        <%
      }
      else if (ButtonPressed.startsWith("Authorise"))
      {
	//chBAN.setBanStatus("Authorised");
	chBAN.setUserId((String)session.getAttribute("User_Id"));
        if(chBAN.ImplementChargeBAN())
	{

	  session.setAttribute("Message","You have successfully implemented BAN Id :-"+chBAN.getBanIdentifier());
          if (chBAN.getIsDirect())
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
          <jsp:forward page="ChargeBAN.jsp">
            <jsp:param name="chargeId" value="<%=chargeId%>"/>
          </jsp:forward>
          <%
	}
      }
      else if ((ButtonPressed.startsWith("Return")) || (ButtonPressed.startsWith("Reject")))
      {
	chBAN.setBanStatus(ButtonPressed+"ed");
	chBAN.setAction(ButtonPressed);
	chBAN.setRejectReason((String)request.getParameter("RejectReason"));
	chBAN.setUserId((String)session.getAttribute("User_Id"));
        if(chBAN.updateChargeBAN())
	{
	  session.setAttribute("Message",chBAN.getMessage());
	  %>
	     <jsp:forward page="Welcome.jsp"/>
	  <%
	}
	else
	{
	  session.setAttribute("Message",chBAN.getMessage());
	  %>
            <jsp:forward page="ChargeBAN.jsp">
              <jsp:param name="chargeId" value="<%=chargeId%>"/>
            </jsp:forward>
	  <%
	}
      }
      else if (ButtonPressed.startsWith("Cancel"))
      {
	chBAN.setBanStatus("Canceled");
	chBAN.setUserId((String)session.getAttribute("User_Id"));
        if(chBAN.updateChargeBAN())
	{
          BAN.setBanStatus("Canceled");
	  session.setAttribute("Message",chBAN.getMessage());
	  %>
	     <jsp:forward page="Welcome.jsp"/>
	  <%
	}
	else
	{
	  session.setAttribute("Message",chBAN.getMessage());
	  %>
            <jsp:forward page="ChargeBAN.jsp">
              <jsp:param name="chargeId" value="<%=chargeId%>"/>
            </jsp:forward>
	  <%
	}
      }
      else if ((ButtonPressed.startsWith("Save Draft")) ||
	      (ButtonPressed.startsWith("Submit")))
      {
        //set non form values
        chBAN.setBanCreatedBy((String)session.getAttribute("User_Name"));
        chBAN.setUserId((String)session.getAttribute("User_Id"));
        String banId = "";
        if (Mode.equals("Delete"))
        {
          chBAN.setBanStatus("Proposed");
          chBAN.setParameter("BAN_Summary",request.getParameter("BAN_Summary"));
          banId = chBAN.getBanIdentifier();
          Success=chBAN.updateChargeBAN();
        }
        else
        {
          //store form values in Bean
          while (FormFields.hasMoreElements())
          {
            FormField=(String)FormFields.nextElement();
            chBAN.setParameter(FormField,request.getParameter(FormField));
          }
          //Validation
          if (!chBAN.isValid(ButtonPressed))
          {
            %>
            <jsp:forward page="ChargeBAN.jsp">
              <jsp:param name="chargeId" value="<%=chargeId%>"/>
            </jsp:forward>
            <%
          }
          else
          {
            if (ButtonPressed.startsWith("Save Draft"))
            {
              chBAN.setBanStatus("Draft");
            }
            else
            {
              chBAN.setBanStatus("Proposed");
            }

            //Create or amend a BAN

            if (Mode.equals("Amend"))
            {
              banId = chBAN.getBanIdentifier();
              Success=chBAN.updateChargeBAN();
            }
            else if (Mode.equals("Cease / "))
            {
              //Success=chBAN.createChargeBAN();
              Success=chBAN.updateChargeBANCR();
              banId = chBAN.getBanIdentifier();
            }
            else
            {
              //Success=chBAN.createChargeBAN();
              Success=chBAN.authoriseChargeBAN();
              banId = chBAN.getBanIdentifier();
            }
          }
	}
        if (Success)
        {
          BAN.setBanIdentifier(banId);
          session.setAttribute("Message",chBAN.getMessage());
            %>
            <jsp:forward page="OSSBANMenu.jsp"/>
            <%
        }
        else
        {//Failed
          %>
            <jsp:forward page="ChargeBAN.jsp">
              <jsp:param name="chargeId" value="<%=chargeId%>"/>
            </jsp:forward>
          <%
        }
      }
      else
      { //no button pressed, so must have been an autorefresh from a dropdown
	//..still need to store ban values
    	  while (FormFields.hasMoreElements())
    	  {
	    FormField=(String)FormFields.nextElement();
	    chBAN.setParameter(FormField,request.getParameter(FormField));
	  }
        %>
            <jsp:forward page="ChargeBAN.jsp">
              <jsp:param name="chargeId" value="<%=chargeId%>"/>
            </jsp:forward>
        <%
      }
    %>
</body>
</html>
