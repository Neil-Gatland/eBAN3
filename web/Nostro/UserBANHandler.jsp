<html>
<head>
</head>
<%@ page import="JavaUtil.*,java.util.Enumeration"%>
<jsp:useBean id="BAN" class="DBUtilities.BANBean" scope="session"/>
<jsp:useBean id="nosuBAN" class="DBUtilities.NostroUserBANBean" scope="session"/>

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
        nosuBAN.setParameter(FormField,"");
      }
      %>
      <jsp:forward page="UserBAN.jsp"/>
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
      nosuBAN.setUserId((String)session.getAttribute("User_Id"));
      if(nosuBAN.AuthoriseUserBAN())
      {
        session.setAttribute("Message","You have successfully implemented BAN Id :-"+nosuBAN.getBanIdentifier());
        %>
           <jsp:forward page="ListBans.jsp"/>
        <%
      }
      else
      {
        %>
          <jsp:forward page="UserBAN.jsp"/>
        <%
      }
    }
    else if ((ButtonPressed.startsWith("Return")) || (ButtonPressed.startsWith("Reject")))
    {
      nosuBAN.setBanStatus(ButtonPressed+"ed");
      nosuBAN.setAction(ButtonPressed);
      nosuBAN.setRejectReason((String)request.getParameter("RejectReason"));
      nosuBAN.setUserId((String)session.getAttribute("User_Id"));
      if(nosuBAN.updateUserBAN())
      {
        session.setAttribute("Message",nosuBAN.getMessage());
        %>
           <jsp:forward page="Welcome.jsp"/>
        <%
      }
      else
      {
        session.setAttribute("Message",nosuBAN.getMessage());
        %>
           <jsp:forward page="UserBAN.jsp"/>
        <%
      }
    }
    else if ((ButtonPressed.startsWith("Save Draft")) ||
            (ButtonPressed.startsWith("Submit")))
    {
      //store form values in Bean
      //FormFields=request.getParameterNames();

      while (FormFields.hasMoreElements())
      {
        FormField=(String)FormFields.nextElement();
        nosuBAN.setParameter(FormField,request.getParameter(FormField));
      }
      //Validation
      if (!nosuBAN.isValid(ButtonPressed))
      {
        %>
        <jsp:forward page="UserBAN.jsp"/>
        <%
      }
      else
      {
        if (ButtonPressed.startsWith("Save Draft"))
        {
          nosuBAN.setBanStatus("Draft");
        }
        else
        {
          nosuBAN.setBanStatus("Proposed");
        }
        //set non form values
        nosuBAN.setBanCreatedBy((String)session.getAttribute("User_Name"));
        nosuBAN.setUserId((String)session.getAttribute("User_Id"));

        //Create or amend a BAN

        if (nosuBAN.getAction().startsWith("Amend"))
        {
          Success=nosuBAN.updateUserBAN();
        }
        else
        {
          Success=nosuBAN.createUserBAN();
        }
        if (Success)
        {
          session.setAttribute("Message",nosuBAN.getMessage());
            %>
            <jsp:forward page="Welcome.jsp"/>
            <%
          //}
        }
        else
        {//Failed
          %>
          <jsp:forward page="UserBAN.jsp"/>
          <%
        }//end of create
      }//end of if valid
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
        <jsp:forward page="UserBAN.jsp"/>
      <%
    }
    %>
</body>
</html>
