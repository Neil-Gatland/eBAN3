<html>
<head>
</head>
<%@ page import="JavaUtil.*,java.util.Enumeration"%>
<jsp:useBean id="BAN" class="DBUtilities.BANBean" scope="session"/>
<jsp:useBean id="acb" class="DBUtilities.AssureChargeBean" scope="session"/>

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
        if ((!FormField.equals("Account_Id")) &&
          (!FormField.equals("Account_Name")) &&
          (!FormField.equals("Invoice_Amount")))
          acb.setParameter(FormField,"");
      }
      %>
      <jsp:forward page="AssureCharge.jsp"/>
      <%
    }
    else if (ButtonPressed.startsWith("Back To"))
    {
      %>
        <jsp:forward page="ListBans.jsp"/>
      <%
    }
    else if (ButtonPressed.startsWith("Create a Reversal"))
    {
      acb.createReversal();
      %>
        <jsp:forward page="AssureCharge.jsp"/>
      <%
    }
    else if (ButtonPressed.startsWith("Create Invoice Menu"))
    {
      %>
        <jsp:forward page="AdHocMenu.jsp"/>
      <%
    }
    else if ((ButtonPressed.startsWith("Save Draft")) ||
            (ButtonPressed.startsWith("Save Exception")) ||
            (ButtonPressed.startsWith("Submit")))
    {
      //store form values in Bean
      //FormFields=request.getParameterNames();

      while (FormFields.hasMoreElements())
      {
        FormField=(String)FormFields.nextElement();
        acb.setParameter(FormField,request.getParameter(FormField));
      }
      //Validation
      if (!ButtonPressed.startsWith("Save Draft"))
      {
        if (!acb.isValid(ButtonPressed))
        {
          %>
          <jsp:forward page="AssureCharge.jsp"/>
          <%
        }
        else
        {
          if (ButtonPressed.startsWith("Submit"))
          {
            acb.setBanStatus("Proposed");
          }
          else
          {
            acb.setBanStatus("Exception");
          }
        }
      }
      //set non form values
      acb.setBanCreatedBy((String)session.getAttribute("User_Name"));
      acb.setUserId((String)session.getAttribute("User_Id"));

      //Create or amend a BAN

      if (acb.getAction().startsWith("Amend"))
      {
        Success=acb.updateAssureCharge();
      }
      else
      {
        Success=acb.createAssureCharge();
      }
      session.setAttribute("Message",acb.getMessage());
      if (Success)
      {
          %>
          <jsp:forward page="Welcome.jsp"/>
          <%
      }
      else
      {//Failed
        %>
        <jsp:forward page="AssureCharge.jsp"/>
        <%
      }//end of create
    }
    else if (ButtonPressed.startsWith("Authorise"))
    {
      acb.setUserId((String)session.getAttribute("User_Id"));
      acb.setBanCreatedBy((String)session.getAttribute("User_Name"));
      String groupName = (String)session.getAttribute("Group_Name");
      if (!groupName.equals("STP"))
      {
        acb.setAuthorising_Manager((String)request.getParameter("AuthManager"));
        acb.setStatus_Details((String)request.getParameter("AuthDetails"));
      }
      else
      {
        while (FormFields.hasMoreElements())
        {
          FormField=(String)FormFields.nextElement();
          acb.setParameter(FormField,request.getParameter(FormField));
        }
        if (!acb.isValid("Submit"))
        {
          %>
          <jsp:forward page="AssureCharge.jsp"/>
          <%
        }
        else
        {
          acb.setBanStatus("Proposed");
          if (!acb.createAssureCharge())
          {
            session.setAttribute("Message",acb.getMessage());
            %>
            <jsp:forward page="AssureCharge.jsp"/>
            <%
          }
        }
      }
      if(acb.AuthoriseAssureCharge())
      {
        session.setAttribute("Message","<font color=blue><b>You have successfully implemented Assure Charge :-"+acb.getBanIdentifier());
        if (!groupName.equals("STP"))
        {
          %>
             <jsp:forward page="ListBans.jsp"/>
          <%
        }
        else
        {
          %>
          <jsp:forward page="Welcome.jsp"/>
          <%
       }
      }
      else
      {
        %>
          <jsp:forward page="AssureCharge.jsp"/>
        <%
      }
    }
    else if ((ButtonPressed.startsWith("Return")) || (ButtonPressed.startsWith("Reject")))
    {
      acb.setBanStatus(ButtonPressed+"ed");
      acb.setAction(ButtonPressed);
      acb.setRejectReason((String)request.getParameter("RejectReason"));
      acb.setUserId((String)session.getAttribute("User_Id"));
      if(acb.updateAssureCharge())
      {
        session.setAttribute("Message",acb.getMessage());
        %>
           <jsp:forward page="Welcome.jsp"/>
        <%
      }
      else
      {
        session.setAttribute("Message",acb.getMessage());
        %>
           <jsp:forward page="AssureCharge.jsp"/>
        <%
      }
    }
    else
    {//No button pressed, so must be a Refresh
      refresh = true;
    }
    if (refresh)
    {
      while (FormFields.hasMoreElements())
      {
        FormField=(String)FormFields.nextElement();
        acb.setParameter(FormField,request.getParameter(FormField));
      }
      %>
        <jsp:forward page="AssureCharge.jsp"/>
      <%
    }
    %>
</body>
</html>
