<html>
<head>
</head>
<%@ page import="JavaUtil.*"%>
<jsp:useBean id="BAN" class="DBUtilities.BANBean" scope="session"/>
<jsp:useBean id="gcBAN" class="DBUtilities.GCBCustomerBANBean" scope="session"/>
<jsp:useBean id="irBAN" class="DBUtilities.InvoiceRegionBANBean" scope="session"/>
<jsp:useBean id="DBA" class="DBUtilities.DBAccess" scope="session"/>

<%!     String ButtonPressed="",Service="",Global_Customer_Id="",Account="",Division="",Product="";
        JavaUtil.StringUtil SU = new JavaUtil.StringUtil();
        JavaUtil.UserData UD = new UserData();
%>
<%
    //Clear out BAN beans
    gcBAN.Reset();
    irBAN.Reset();
    gcBAN.setBanCreatedBy((String)session.getAttribute("User_Name"));
    irBAN.setBanCreatedBy((String)session.getAttribute("User_Name"));

    session.setAttribute("PageSent",request.getRequestURI());
    ButtonPressed=(String)request.getParameter("ButtonPressed");

    if (ButtonPressed.startsWith("Connect"))
    {
      %>
	<jsp:forward page="DesktopLogon.jsp"/>
      <%
    }
    else if (ButtonPressed.equals("Raise Query"))
    {
      %>
        <jsp:forward page="CustomerBANMenu.jsp">
          <jsp:param name="query" value="true"/>
          <jsp:param name="fromPage" value="CustomerBANMenu"/>
        </jsp:forward>
      <%
    }
    else if (ButtonPressed.equals("Account Administration Main Menu"))
    {
      %>
        <jsp:forward page="Welcome.jsp"/>
      <%
    }
    else if (ButtonPressed.startsWith("Service"))
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
      //Global_Customer_Id=(String)request.getParameter("Product_Customer");
      Global_Customer_Id=(String)request.getParameter("GCB_Customer");
      Account=(String)request.getParameter("GCB_Account");
      Division=(String)request.getParameter("Division");
      Product=(String)request.getParameter("Product");
      if (SU.isNull(BAN.getMessage(),"").endsWith("Invalid Service Reference"))
      {
	Service="";
      }
      else
      {
      	Service=(String)request.getParameter("Service");
      }
      if (Product.compareTo(BAN.getProduct())!=0)
      {
	BAN.setProduct(SU.isNull((String)request.getParameter("Product"),""));
	BAN.setGlobalCustomerId("");
	BAN.setAccount_Id("");
        BAN.setDivision_Id("");
      }
      else if (Global_Customer_Id.compareTo(BAN.getGlobalCustomerId())!=0)
      {
	BAN.setGlobalCustomerId(SU.isNull(Global_Customer_Id,""));
	BAN.setAccount_Id("");
        BAN.setDivision_Id("");
      }
      else if (Account.compareTo(BAN.getAccount_Id())!=0)
      {
        BAN.setAccount_Id(SU.isNull((String)request.getParameter("GCB_Account"),""));
      }
      else if (Division.compareTo(BAN.getDivision_Id())!=0)
      {
        BAN.setDivision_Id(SU.isNull((String)request.getParameter("Division"),""));
      }
      else if (Service.compareTo(BAN.getService_Reference())!=0)
      {
        BAN.setService_Reference(Service);
      }
    }

    BAN.setErrored("clear");
    gcBAN.setErrored("clear");
    irBAN.setErrored("clear");


      //Navigation

      if ((ButtonPressed.compareTo("") == 0) || (ButtonPressed.startsWith("Service")))
      {//A Refresh
        %>
          <jsp:forward page="CustomerBANMenu.jsp"/>
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
          <jsp:forward page="CustomerBANMenu.jsp"/>
        <%
      }
      else if (ButtonPressed.startsWith("Use"))
      {
        if (SU.isNull((String)session.getAttribute("Global_Customer"),"").compareTo("") != 0)
        {
          BAN.setGlobalCustomerId(SU.isNull((String)session.getAttribute("Global_Customer"),""));
        }
        if (SU.isNull((String)session.getAttribute("Account"),"").compareTo("") != 0)
        {
          BAN.setAccount_Id(SU.isNull((String)session.getAttribute("Account"),""));
        }
        if (SU.isNull((String)session.getAttribute("Division"),"").compareTo("") != 0)
        {
          BAN.setDivision_Id(SU.isNull((String)session.getAttribute("Division"),""));
        }
        if (SU.isNull((String)session.getAttribute("Service"),"").compareTo("") != 0)
        {
          BAN.setService_Reference((String)session.getAttribute("Service"));
        }
        %>
        <jsp:forward page="CustomerBANMenu.jsp"/>
        <%
      }
      else if (ButtonPressed.startsWith("Create an Account"))
      {
        irBAN.Reset();
        gcBAN.Reset();
	gcBAN.setGlobalCustomerId(BAN.getGlobalCustomerId());
        gcBAN.getCustomer();
        if (gcBAN.getIsMANS())
        {
          if (DBA.customerHasAccounts(BAN.getGlobalCustomerId()))
          {
            BAN.setMessage("<font color=red><b>This managed services customer already has an account");
            %>
              <jsp:forward page="CustomerBANMenu.jsp"/>
            <%
          }
          else
          {
            irBAN.setIsMANS(true);
          }
        }
	irBAN.setAction("Create");
	irBAN.setMode("Add");

	irBAN.setGlobalCustomerId(BAN.getGlobalCustomerId());
	irBAN.setGlobalCustomerName(BAN.getGlobalCustomerName());
	irBAN.setAccount_Id("");
	irBAN.setCurrency_Desc("");
	//gcBAN.getCustomer();

        %>
          <jsp:forward page="InvoiceRegionBAN.jsp"/>
	<%
      }
      else if (ButtonPressed.startsWith("Amend an Account"))
      {
        irBAN.Reset();
        gcBAN.Reset();
	gcBAN.setGlobalCustomerId(BAN.getGlobalCustomerId());
        gcBAN.getCustomer();
	irBAN.setAction("Create");
	irBAN.setMode("Amend");
        irBAN.setIsMANS(gcBAN.getIsMANS());

	irBAN.setGlobalCustomerId(BAN.getGlobalCustomerId());
	irBAN.setGlobalCustomerName(BAN.getGlobalCustomerName());
	irBAN.setAccount_Id(BAN.getAccount_Id());
	irBAN.setCurrency_Desc("");
        irBAN.getInvoiceRegion();
	//gcBAN.getCustomer();

        %>
          <jsp:forward page="InvoiceRegionBAN.jsp"/>
	<%
      }
      else if (ButtonPressed.startsWith("Create a Customer"))
      {
        gcBAN.Reset();
	gcBAN.setAction("Create");
	gcBAN.setMode("Add");
        %>
          <jsp:forward page="GCBCustomerBAN.jsp"/>
	<%
      }
      else if (ButtonPressed.startsWith("Amend a Customer"))
      {
        gcBAN.Reset();
	gcBAN.setAction("Create");
	gcBAN.setMode("Amend");
	gcBAN.setGlobalCustomerId(BAN.getGlobalCustomerId());
        gcBAN.getCustomer();
        %>
          <jsp:forward page="GCBCustomerBAN.jsp"/>
	<%
      }
      else if (ButtonPressed.startsWith("Deactivate a Customer"))
      {
        gcBAN.Reset();
	gcBAN.setAction("Create");
	gcBAN.setMode("Delete");
	gcBAN.setGlobalCustomerId(BAN.getGlobalCustomerId());
        gcBAN.getCustomer();
        %>
          <jsp:forward page="GCBCustomerBAN.jsp"/>
	<%
      }
      else
      {//No button pressed, so must be a Refresh
        %>
          <jsp:forward page="CustomerBANMenu.jsp"/>
        <%
      }
      %>
</body>
</html>
