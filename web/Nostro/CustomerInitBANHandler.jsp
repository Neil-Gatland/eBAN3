<html>
<head>
</head>
<%@ page import="JavaUtil.*,java.util.Enumeration"%>
<jsp:useBean id="BAN" class="DBUtilities.BANBean" scope="session"/>
<jsp:useBean id="noscBAN" class="DBUtilities.NostroCustomerBANBean" scope="session"/>

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
        noscBAN.setParameter(FormField,"");
      }
      %>
      <jsp:forward page="CustomerInitBAN.jsp"/>
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
      noscBAN.setUserId((String)session.getAttribute("User_Id"));
      if(noscBAN.AuthoriseCustomerBAN())
      {
        session.setAttribute("Message","You have successfully implemented BAN Id :-"+noscBAN.getBanIdentifier());
        %>
           <jsp:forward page="ListBans.jsp"/>
        <%
      }
      else
      {
        %>
          <jsp:forward page="CustomerInitBAN.jsp"/>
        <%
      }
    }
    else if ((ButtonPressed.startsWith("Return")) || (ButtonPressed.startsWith("Reject")))
    {
      noscBAN.setBanStatus(ButtonPressed+"ed");
      noscBAN.setAction(ButtonPressed);
      noscBAN.setRejectReason((String)request.getParameter("RejectReason"));
      noscBAN.setUserId((String)session.getAttribute("User_Id"));
      if(noscBAN.updateCustomerBAN())
      {
        session.setAttribute("Message",noscBAN.getMessage());
        %>
           <jsp:forward page="Welcome.jsp"/>
        <%
      }
      else
      {
        session.setAttribute("Message",noscBAN.getMessage());
        %>
           <jsp:forward page="CustomerInitBAN.jsp"/>
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
        noscBAN.setParameter(FormField,request.getParameter(FormField));
      }
      //Validation
      if (!noscBAN.isValid(ButtonPressed))
      {
        %>
        <jsp:forward page="CustomerInitBAN.jsp"/>
        <%
      }
      else
      {
        if (ButtonPressed.startsWith("Save Draft"))
        {
          noscBAN.setBanStatus("Draft");
        }
        else
        {
          noscBAN.setBanStatus("Proposed");
        }
        //set non form values
        noscBAN.setBanCreatedBy((String)session.getAttribute("User_Name"));
        noscBAN.setUserId((String)session.getAttribute("User_Id"));

        //Create or amend a BAN

        if (noscBAN.getAction().startsWith("Amend"))
        {
          Success=noscBAN.updateCustomerBAN();
        }
        else
        {
          Success=noscBAN.createCustomerBAN();
        }
        if (Success)
        {
          session.setAttribute("Message",noscBAN.getMessage());
            %>
            <jsp:forward page="Welcome.jsp"/>
            <%
          //}
        }
        else
        {//Failed
          %>
          <jsp:forward page="CustomerInitBAN.jsp"/>
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
        <jsp:forward page="CustomerInitBAN.jsp"/>
      <%
    }
    %>
</body>
</html>
