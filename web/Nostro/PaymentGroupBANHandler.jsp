<html>
<head>
</head>
<%@ page import="JavaUtil.*,java.util.Enumeration"%>
<jsp:useBean id="BAN" class="DBUtilities.BANBean" scope="session"/>
<jsp:useBean id="nospBAN" class="DBUtilities.NostroPayGroupBANBean" scope="session"/>

<%!     String ButtonPressed="",Circuit="",Global_Customer_Id="",Account="",
          nostroRef="",Global_Customer_Name="";
        JavaUtil.StringUtil SU = new JavaUtil.StringUtil();
        JavaUtil.UserData UD = new UserData();
	Enumeration FormFields;
	String FormField;
	boolean Errors,Success;
%>
<%
    boolean refresh = false;
    //Clear out BAN beans
    //session.setAttribute("PageSent",request.getRequestURI());
    ButtonPressed=(String)request.getParameter("ButtonPressed");
    FormFields=request.getParameterNames();

    if (ButtonPressed.startsWith("Clear"))
    {
      while (FormFields.hasMoreElements())
      {
        FormField=(String)FormFields.nextElement();
        nospBAN.setParameter(FormField,"");
      }
      %>
      <jsp:forward page="PaymentGroupBAN.jsp"/>
      <%
    }
    else if (ButtonPressed.startsWith("Back To"))
    {
      %>
        <jsp:forward page="ListBans.jsp"/>
      <%
    }
    else if (ButtonPressed.startsWith("Create BAN Menu"))
    {
      %>
        <jsp:forward page="NostroBANMenu.jsp"/>
      <%
    }
    else if (ButtonPressed.startsWith("Authorise"))
    {
      nospBAN.setUserId((String)session.getAttribute("User_Id"));
      if(nospBAN.AuthorisePaymentGroupBAN())
      {
        session.setAttribute("Message","You have successfully implemented BAN Id :-"+nospBAN.getBanIdentifier());
        %>
           <jsp:forward page="ListBans.jsp"/>
        <%
      }
      else
      {
        %>
          <jsp:forward page="PaymentGroupBAN.jsp"/>
        <%
      }
    }
    else if ((ButtonPressed.startsWith("Return")) || (ButtonPressed.startsWith("Reject")))
    {
      nospBAN.setBanStatus(ButtonPressed+"ed");
      nospBAN.setAction(ButtonPressed);
      nospBAN.setRejectReason((String)request.getParameter("RejectReason"));
      nospBAN.setUserId((String)session.getAttribute("User_Id"));
      if(nospBAN.updatePaymentGroupBAN())
      {
        session.setAttribute("Message",nospBAN.getMessage());
        %>
           <jsp:forward page="Welcome.jsp"/>
        <%
      }
      else
      {
        session.setAttribute("Message",nospBAN.getMessage());
        %>
           <jsp:forward page="PaymentGroupBAN.jsp"/>
        <%
      }
    }
    else if ((ButtonPressed.startsWith("Save Draft")) ||
            (ButtonPressed.startsWith("Submit")) ||
            (ButtonPressed.startsWith("Account Add")) ||
            (ButtonPressed.startsWith("Account Remove")))
    {
      //store form values in Bean
      //FormFields=request.getParameterNames();

      while (FormFields.hasMoreElements())
      {
        FormField=(String)FormFields.nextElement();
        nospBAN.setParameter(FormField,request.getParameter(FormField));
      }
      if (ButtonPressed.startsWith("Account Add"))
      {
        nospBAN.addAccount((String)request.getParameter("New_Account"));
        %>
          <jsp:forward page="PaymentGroupBAN.jsp"/>
        <%
      }
      else if (ButtonPressed.startsWith("Account Remove"))
      {
        nospBAN.removeAccount((String)request.getParameter("Existing_Account"));
        %>
          <jsp:forward page="PaymentGroupBAN.jsp"/>
        <%
      }
      else
      {
        //Validation
        if (!nospBAN.isValid(ButtonPressed))
        {
          %>
            <jsp:forward page="PaymentGroupBAN.jsp"/>
          <%
        }
        else
        {
          if (ButtonPressed.startsWith("Save Draft"))
          {
            nospBAN.setBanStatus("Draft");
          }
          else
          {
            nospBAN.setBanStatus("Proposed");
          }
          //set non form values
          nospBAN.setBanCreatedBy((String)session.getAttribute("User_Name"));
          nospBAN.setUserId((String)session.getAttribute("User_Id"));

          //Create or amend a BAN

          if (nospBAN.getAction().startsWith("Amend"))
          {
            Success=nospBAN.updatePaymentGroupBAN();
          }
          else
          {
            Success=nospBAN.createPaymentGroupBAN();
          }
          if (Success)
          {
            session.setAttribute("Message",nospBAN.getMessage());
              %>
              <jsp:forward page="Welcome.jsp"/>
              <%
            //}
          }
          else
          {//Failed
            %>
            <jsp:forward page="PaymentGroupBAN.jsp"/>
            <%
          }//end of create
        }//end of if valid
      }
    }
    else
    {//No button pressed, so must be a Refresh
      refresh = true;
    }
    if (refresh)
    {
      /*if (Global_Customer_Id.compareTo(BAN.getGlobalCustomerId())!=0)
      {
	session.setAttribute("Global_Customer_Id",Global_Customer_Id);
	BAN.setGlobalCustomerId(Global_Customer_Id);
      }*/
      %>
        <jsp:forward page="PaymentGroupBAN.jsp"/>
      <%
    }
    %>
</body>
</html>
