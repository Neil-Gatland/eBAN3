<html>
<head>
</head>
<%@ page import="JavaUtil.*"%>
<jsp:useBean id="BAN" class="DBUtilities.BANBean" scope="session"/>
<jsp:useBean id="noscBAN" class="DBUtilities.NostroCustomerBANBean" scope="session"/>
<jsp:useBean id="nosaBAN" class="DBUtilities.NostroAccountBANBean" scope="session"/>
<jsp:useBean id="nosuBAN" class="DBUtilities.NostroUserBANBean" scope="session"/>
<jsp:useBean id="nospBAN" class="DBUtilities.NostroPayGroupBANBean" scope="session"/>

<%!     String ButtonPressed="",Service="",Global_Customer_Id="",Account="",
          Division="",Product="",Account_Name="",Payment_Group_Id="",
          Nostro_User_Id="";
        JavaUtil.StringUtil SU = new JavaUtil.StringUtil();
        JavaUtil.UserData UD = new UserData();
%>
<%
    //Clear out BAN beans
    noscBAN.Reset();
    nosaBAN.Reset();
    nosuBAN.Reset();
    nospBAN.Reset();
    noscBAN.setBanCreatedBy((String)session.getAttribute("User_Name"));
    nosaBAN.setBanCreatedBy((String)session.getAttribute("User_Name"));
    nosuBAN.setBanCreatedBy((String)session.getAttribute("User_Name"));
    nospBAN.setBanCreatedBy((String)session.getAttribute("User_Name"));

    session.setAttribute("PageSent",request.getRequestURI());
    ButtonPressed=(String)request.getParameter("ButtonPressed");
    Global_Customer_Id=(String)request.getParameter("All_Customer");
    Account=(String)request.getParameter("Nostro_Account");
    Account_Name=(String)request.getParameter("Account_Name");
    Payment_Group_Id=(String)request.getParameter("Payment_Group");
    Nostro_User_Id=(String)request.getParameter("Nostro_User");

    if (ButtonPressed.startsWith("Connect"))
    {
      %>
	<jsp:forward page="GCBDesktopLogon.jsp"/>
      <%
    }
    if (ButtonPressed.startsWith("Service"))
    {//A  specific Service value has been entered
     //so don't use the current drop down settings for customer and account
     //as these will be derived from the Service

      Service=SU.isNull((String)request.getParameter("Service_Ref"),"");
      if (Service.compareTo("")!=0)
      {
	BAN.setService_Reference(Service);
      }
    }
    else
    {
      if (Global_Customer_Id.compareTo(BAN.getGlobalCustomerId())!=0)
      {
	BAN.setGlobalCustomerId(SU.isNull(Global_Customer_Id,""));
	BAN.setAccount_Id("");
      }
      if (Account.compareTo(BAN.getAccount_Id())!=0)
      {
	BAN.setAccount_Id(SU.isNull(Account,""));
      }
      if (Account_Name.compareTo(BAN.getAccountName())!=0)
      {
	BAN.setAccountName(SU.isNull(Account_Name,""));
      }
      if (Payment_Group_Id.compareTo(BAN.getPaymentGroupId())!=0)
      {
	BAN.setPaymentGroupId(SU.isNull(Payment_Group_Id,""));
      }
      if (Nostro_User_Id.compareTo(BAN.getNostroUserId())!=0)
      {
	BAN.setNostroUserId(SU.isNull(Nostro_User_Id,""));
      }
    }

    BAN.setErrored("clear");
    noscBAN.setErrored("clear");


      //Navigation

      if ((ButtonPressed.compareTo("") == 0) || (ButtonPressed.startsWith("Service")))
      {//A Refresh
        %>
          <jsp:forward page="NostroBANMenu.jsp"/>
        <%
      }
      else if (ButtonPressed.startsWith("Clear"))
      {
        BAN.setProduct("");
        BAN.setGlobalCustomerId("");
	BAN.setGlobalCustomerName("");
        BAN.setAccount_Id("");
	BAN.setDivision_Id("");
        BAN.setService_Reference("");
        %>
          <jsp:forward page="NostroBANMenu.jsp"/>
        <%
      }
      else if (ButtonPressed.startsWith("Use"))
      {
        if (SU.isNull((String)session.getAttribute("All_Customer"),"").compareTo("") != 0)
        {
          BAN.setGlobalCustomerId(SU.isNull((String)session.getAttribute("All_Customer"),""));
        }
        %>
        <jsp:forward page="NostroBANMenu.jsp"/>
        <%
      }
      else if (ButtonPressed.startsWith("Initiate a Customer"))
      {
        noscBAN.Reset();
	noscBAN.setAction("Create");
	noscBAN.setMode("Add");
	noscBAN.setGlobalCustomerId(BAN.getGlobalCustomerId());
	noscBAN.setGlobalCustomerName(BAN.getGlobalCustomerName());

        %>
          <jsp:forward page="CustomerInitBAN.jsp"/>
        <%
      }
      else if (ButtonPressed.startsWith("Amend a Customer"))
      {
        noscBAN.Reset();
	noscBAN.setAction("Create");
	noscBAN.setMode("Amend");
	noscBAN.setGlobalCustomerId(BAN.getGlobalCustomerId());
	noscBAN.setGlobalCustomerName(BAN.getGlobalCustomerName());
        //noscBAN.getc

        %>
          <jsp:forward page="CustomerInitBAN.jsp"/>
        <%
      }
      else if (ButtonPressed.startsWith("Initiate an Account"))
      {
        nosaBAN.Reset();
	nosaBAN.setAction("Create");
	nosaBAN.setMode("Add");
	nosaBAN.setGlobalCustomerId(BAN.getGlobalCustomerId());
	nosaBAN.setGlobalCustomerName(BAN.getGlobalCustomerName());
        %>
          <jsp:forward page="AccountInitBAN.jsp"/>
        <%
      }
      else if (ButtonPressed.startsWith("Amend an Account"))
      {
        nosaBAN.Reset();
	nosaBAN.setAction("Create");
	nosaBAN.setMode("Amend");
	nosaBAN.setGlobalCustomerId(BAN.getGlobalCustomerId());
	nosaBAN.setGlobalCustomerName(BAN.getGlobalCustomerName());
        nosaBAN.setAccount_Id(BAN.getAccount_Id());
        nosaBAN.getAccount();
        %>
          <jsp:forward page="AccountInitBAN.jsp"/>
        <%
      }
      else if (ButtonPressed.startsWith("Create an e-billing Payment Group"))
      {
        nospBAN.Reset();
	nospBAN.setAction("Create");
	nospBAN.setMode("Add");
	nospBAN.setGlobalCustomerId(BAN.getGlobalCustomerId());
	nospBAN.setGlobalCustomerName(BAN.getGlobalCustomerName());
        %>
          <jsp:forward page="PaymentGroupBAN.jsp"/>
        <%
      }
      else if (ButtonPressed.startsWith("Amend an e-billing Payment Group"))
      {
        nospBAN.Reset();
	nospBAN.setAction("Create");
	nospBAN.setMode("Amend");
	nospBAN.setGlobalCustomerId(BAN.getGlobalCustomerId());
	nospBAN.setGlobalCustomerName(BAN.getGlobalCustomerName());
        nospBAN.setPaymentGroupId(BAN.getPaymentGroupId());
        nospBAN.getPaymentGroup();
        %>
          <jsp:forward page="PaymentGroupBAN.jsp"/>
        <%
      }
      else if (ButtonPressed.startsWith("Create a User"))
      {
        nosuBAN.Reset();
	nosuBAN.setAction("Create");
	nosuBAN.setMode("Add");
	nosuBAN.setGlobalCustomerId(BAN.getGlobalCustomerId());
	nosuBAN.setGlobalCustomerName(BAN.getGlobalCustomerName());
        nosuBAN.setAccount_Id(Account);
        nosuBAN.setAccountName(Account_Name);
        nosuBAN.assignNewUserId();
        %>
          <jsp:forward page="UserBAN.jsp"/>
        <%
      }
      else if (ButtonPressed.startsWith("Amend a User"))
      {
        nosuBAN.Reset();
	nosuBAN.setAction("Create");
	nosuBAN.setMode("Amend");
	nosuBAN.setGlobalCustomerId(BAN.getGlobalCustomerId());
	nosuBAN.setGlobalCustomerName(BAN.getGlobalCustomerName());
        nosuBAN.setAccount_Id(Account);
        nosuBAN.setAccountName(Account_Name);
        nosuBAN.setNostroUserId(BAN.getNostroUserId());
        nosuBAN.getUser();
        %>
          <jsp:forward page="UserBAN.jsp"/>
        <%
      }
      else
      {//No button pressed, so must be a Refresh
        %>
          <jsp:forward page="NostroBANMenu.jsp"/>
        <%
      }
      %>
</body>
</html>
