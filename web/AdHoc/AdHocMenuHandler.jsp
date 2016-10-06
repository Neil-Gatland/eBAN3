<html>
<head>
</head>
<%@ page import="JavaUtil.*"%>
<jsp:useBean id="BAN" class="DBUtilities.BANBean" scope="session"/>
<jsp:useBean id="acb" class="DBUtilities.AssureChargeBean" scope="session"/>

<%!     String ButtonPressed="",Service="",Customer_Id="",Account="";
        JavaUtil.StringUtil SU = new JavaUtil.StringUtil();
        JavaUtil.UserData UD = new UserData();
%>
<%
    //Clear out BAN beans
    acb.Reset();
    acb.setBanCreatedBy((String)session.getAttribute("User_Name"));

    session.setAttribute("PageSent",request.getRequestURI());
    ButtonPressed=(String)request.getParameter("ButtonPressed");
    Customer_Id=(String)request.getParameter("Ad_Hoc_Customer");
    Account=(String)request.getParameter("Account");
    String Account_Filter=(String)request.getParameter("Account_Filter");

    if (Customer_Id.compareTo(BAN.getGlobalCustomerId())!=0)
    {
      BAN.setGlobalCustomerId(SU.isNull(Customer_Id,""));
      BAN.setAccount_Id("");
      //BAN.setAccountName("");
      BAN.setAccountFilter("");
    }
    else if (Account.compareTo(BAN.getAccount_Id())!=0)
    {
      BAN.setAccount_Id(SU.isNull(Account,""));
      //if (BAN.getBillingSource().equals(""))
        BAN.setBillingSource();
      //BAN.setAccountName(SU.isNull(Account,""));
      BAN.setGlobalCustomerId("");
    }
    else if (Account_Filter.compareTo(BAN.getAccountFilter())!=0)
    {
      BAN.setAccountFilter(SU.isNull(Account_Filter,""));
      //filter has changed so reset account to blank
      BAN.setAccount_Id("");
      //BAN.setAccountName("");
      BAN.setGlobalCustomerId("");
    }

    BAN.setErrored("clear");


      //Navigation

      if ((ButtonPressed.compareTo("") == 0) || (ButtonPressed.startsWith("Service")))
      {//A Refresh
        %>
          <jsp:forward page="AdHocMenu.jsp"/>
        <%
      }
      else if (ButtonPressed.startsWith("Clear"))
      {
        BAN.setGlobalCustomerId("");
	BAN.setGlobalCustomerName("");
        BAN.setAccount_Id("");
        BAN.setAccountFilter("");
        %>
          <jsp:forward page="AdHocMenu.jsp"/>
        <%
      }
      else if (ButtonPressed.startsWith("Use"))
      {
        if (SU.isNull((String)session.getAttribute("All_Customer"),"").compareTo("") != 0)
        {
          BAN.setGlobalCustomerId(SU.isNull((String)session.getAttribute("All_Customer"),""));
        }
        %>
        <jsp:forward page="AdHocMenu.jsp"/>
        <%
      }
      else if (ButtonPressed.startsWith("Create an AssureCharge"))
      {
        acb.Reset();
	acb.setAction("Create");
	acb.setMode("Add");
        acb.setCustomerId(BAN.getGlobalCustomerId());
        acb.setBillingSource(BAN.getBillingSource());
        acb.setAccountDetails(BAN.getAccount_Id());
        //acb.setAccountName(BAN.getAccountName());
        acb.setCustomerName();
        acb.setBanCreatedBy((String)session.getAttribute("User_Name"));
        if (acb.getAccount_Id().equals(""))
          acb.setBanStatus("Provisional");
        if ((Customer_Id.equals("")) && (acb.getBillingSource().equals("Unknown")))
        {
          %>
          <jsp:forward page="AdHocMenu.jsp"/>
          <%
        }
        else
        {
          %>
            <jsp:forward page="AssureCharge.jsp"/>
          <%
        }
      }
      else
      {//No button pressed, so must be a Refresh
        %>
          <jsp:forward page="AdHocMenu.jsp"/>
        <%
      }
      /*else if (ButtonPressed.startsWith("Create a Customer"))
      {
        noscBAN.Reset();
	noscBAN.setAction("Create");
	noscBAN.setMode("Add");
	noscBAN.setGlobalCustomerId(BAN.getGlobalCustomerId());
	noscBAN.setGlobalCustomerName(BAN.getGlobalCustomerName());

        %>
          <jsp:forward page="AdHocCustomer.jsp"/>
        <%
      }
      //else if (ButtonPressed.startsWith("Amend a Customer"))
      {
        noscBAN.Reset();
	noscBAN.setAction("Create");
	noscBAN.setMode("Amend");
	noscBAN.setGlobalCustomerId(BAN.getGlobalCustomerId());
	noscBAN.setGlobalCustomerName(BAN.getGlobalCustomerName());
        //noscBAN.getc

        %>
          <jsp:forward page="AdHocCustomer.jsp"/>
        <%
      }
      //else if (ButtonPressed.startsWith("Create an Account"))
      {
        acb.Reset();
	acb.setAction("Create");
	acb.setMode("Add");
	acb.setGlobalCustomerId(BAN.getGlobalCustomerId());
	acb.setGlobalCustomerName(BAN.getGlobalCustomerName());
        %>
          <jsp:forward page="AdHocAccount.jsp"/>
        <%
      }
      //else if (ButtonPressed.startsWith("Amend an Account"))
      {
        acb.Reset();
	acb.setAction("Create");
	acb.setMode("Amend");
	acb.setGlobalCustomerId(BAN.getGlobalCustomerId());
	acb.setGlobalCustomerName(BAN.getGlobalCustomerName());
        acb.setAccount_Id(BAN.getAccount_Id());
        acb.getAccount();
        %>
          <jsp:forward page="AccountInitBAN.jsp"/>
        <%
      //}*/
      %>
</body>
</html>
