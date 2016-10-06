<html>
<head>
</head>
<%@ page import="JavaUtil.*,java.util.Enumeration"%>
<jsp:useBean id="BAN" class="DBUtilities.BANBean" scope="session"/>
<jsp:useBean id="nosaBAN" class="DBUtilities.NostroAccountBANBean" scope="session"/>

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
        nosaBAN.setParameter(FormField,"");
      }
      %>
      <jsp:forward page="AccountInitBAN.jsp"/>
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
    else if ((ButtonPressed.startsWith("Save Draft")) ||
            (ButtonPressed.startsWith("Submit")))
    {
      //store form values in Bean
      //FormFields=request.getParameterNames();

      while (FormFields.hasMoreElements())
      {
        FormField=(String)FormFields.nextElement();
        nosaBAN.setParameter(FormField,request.getParameter(FormField));
      }
      //Validation
      if (!nosaBAN.isValid(ButtonPressed))
      {
        %>
        <jsp:forward page="AccountInitBAN.jsp"/>
        <%
      }
      else
      {
        if (ButtonPressed.startsWith("Save Draft"))
        {
          nosaBAN.setBanStatus("Draft");
        }
        else
        {
          nosaBAN.setBanStatus("Proposed");
        }
        //set non form values
        nosaBAN.setBanCreatedBy((String)session.getAttribute("User_Name"));
        nosaBAN.setUserId((String)session.getAttribute("User_Id"));

        //Create or amend a BAN

        if (nosaBAN.getAction().startsWith("Amend"))
        {
          Success=nosaBAN.updateAccountBAN();
        }
        else
        {
          Success=nosaBAN.createAccountBAN();
        }
        session.setAttribute("Message",nosaBAN.getMessage());
        if (Success)
        {
            %>
            <jsp:forward page="Welcome.jsp"/>
            <%
        }
        else
        {//Failed
          %>
          <jsp:forward page="AccountInitBAN.jsp"/>
          <%
        }//end of create
      }//end of if valid
    }
    else if (ButtonPressed.startsWith("Authorise"))
    {
      nosaBAN.setUserId((String)session.getAttribute("User_Id"));
      if(nosaBAN.AuthoriseAccountBAN())
      {
        session.setAttribute("Message","You have successfully implemented BAN Id :-"+nosaBAN.getBanIdentifier());
        %>
           <jsp:forward page="ListBans.jsp"/>
        <%
      }
      else
      {
        %>
          <jsp:forward page="AccountInitBAN.jsp"/>
        <%
      }
    }
    else if ((ButtonPressed.startsWith("Return")) || (ButtonPressed.startsWith("Reject")))
    {
      nosaBAN.setBanStatus(ButtonPressed+"ed");
      nosaBAN.setAction(ButtonPressed);
      nosaBAN.setRejectReason((String)request.getParameter("RejectReason"));
      nosaBAN.setUserId((String)session.getAttribute("User_Id"));
      if(nosaBAN.updateAccountBAN())
      {
        session.setAttribute("Message",nosaBAN.getMessage());
        %>
           <jsp:forward page="Welcome.jsp"/>
        <%
      }
      else
      {
        session.setAttribute("Message",nosaBAN.getMessage());
        %>
           <jsp:forward page="AccountInitBAN.jsp"/>
        <%
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
        <jsp:forward page="AccountInitBAN.jsp"/>
      <%
    }
    %>
</body>
</html>
