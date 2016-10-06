<html>
<head>
</head>
<%@ page isThreadSafe="false" %>
<%@ page import="JavaUtil.*"%>
<%@ page import="DBUtilities.OSSChargeBANBean"%>
<!-- page import="java.util.Random-->
<jsp:useBean id="BAN" class="DBUtilities.BANBean" scope="session"/>
<!--jsp:useBean id="chBAN" class="DBUtilities.OSSChargeBANBean" scope="session"/-->
<jsp:useBean id="ctBAN" class="DBUtilities.CircuitBANBean" scope="session"/>
<jsp:useBean id="cuBAN" class="DBUtilities.OSSCustomerBANBean" scope="session"/>
<jsp:useBean id="caBAN" class="DBUtilities.CarrierBANBean" scope="session"/>
<jsp:useBean id="siteBAN" class="DBUtilities.SiteBANBean" scope="session"/>

<%     String ButtonPressed="",Circuit="",Global_Customer_Id="",Division="",
          C2_Ref_No="";
        JavaUtil.StringUtil SU = new JavaUtil.StringUtil();
        JavaUtil.UserData UD = new UserData();

    String userId = (String)session.getAttribute("User_Id");
    OSSChargeBANBean chBAN = (OSSChargeBANBean)session.getAttribute("chBAN" + userId);
    if (chBAN == null)
    {
      chBAN = new OSSChargeBANBean();
      session.setAttribute("chBAN" + userId, chBAN);
    }
    //Clear out BAN beans
    chBAN.Reset();
    ctBAN.Reset();
    cuBAN.Reset();
    caBAN.Reset();
    siteBAN.Reset();
    session.setAttribute("PageSent",request.getRequestURI());
    ButtonPressed=(String)request.getParameter("ButtonPressed");
    BAN.setSiteVisibility("hidden");

    if (ButtonPressed.startsWith("Connect"))
    {
      %>
	<jsp:forward page="DesktopLogon.jsp"/>
      <%
    }
    if (ButtonPressed.startsWith("Circuit"))
    {//A  specific circuit value has been entered
     //so don't use the current drop down settings for customer and Division
     //as these will be derived from the circuit

      Circuit=SU.isNull((String)request.getParameter("Circuit_Ref"),"");
      if (Circuit.compareTo("")!=0)
      {//set the 'ripple' parameter to true to ripple up to Customer
	BAN.setCircuit_Reference(Circuit,true);
      }
    }
    else
    {
      Global_Customer_Id=(String)request.getParameter("Global_Customer");
      if (BAN.getEdifySetCustomer())
      {
        Global_Customer_Id=BAN.getGlobalCustomerId();
      }
      Division=(String)request.getParameter("GCD_Id");
      C2_Ref_No=(String)request.getParameter("C2_Ref_No");
      if (SU.isNull(BAN.getMessage(),"").endsWith("Invalid Circuit Reference"))
      {
	Circuit="";
      }
      else
      {
      	Circuit=(String)request.getParameter("Circuit");
      }

      if (Global_Customer_Id.compareTo(BAN.getGlobalCustomerId())!=0)
      {
	session.setAttribute("Global_Customer_Id",Global_Customer_Id);
	BAN.setGlobalCustomerId(Global_Customer_Id);
	BAN.setDivision_Id("");
	BAN.setC2RefNo("");
      }
      else if (Division.compareTo(BAN.getDivision_Id())!=0)
      {
        BAN.setDivision_Id(Division);
	BAN.setC2RefNo("");
      }
      else if (C2_Ref_No.compareTo(BAN.getC2RefNo())!=0)
      {
        BAN.setC2RefNo(C2_Ref_No);
	BAN.setDivision_Id("");
      }
      else if (Circuit.compareTo(BAN.getCircuit_Reference())!=0)
      {
        BAN.setCircuit_Reference(Circuit);
      }
    }

    BAN.setCarrier_Name((String)request.getParameter("Carrier"));

    BAN.setErrored("clear");
    chBAN.setErrored("clear");
    cuBAN.setErrored("clear");
    ctBAN.setErrored("clear");


      //Navigation

      if ((ButtonPressed.compareTo("") == 0) || (ButtonPressed.startsWith("Circuit")))
      {//A Refresh
        %>
          <jsp:forward page="OSSBANMenu.jsp"/>
        <%
      }
      else if (ButtonPressed.equals("Raise Query"))
      {
          //jsp:forward page="OSSBANMenu.jsp?query=true&fromPage=OSSBANMenu&gcId=xxxx">
            //<jsp:param name="gcId" value="xxx"/>
        %>
          <jsp:forward page="OSSBANMenu.jsp">
            <jsp:param name="query" value="true"/>
            <jsp:param name="fromPage" value="OSSBANMenu"/>
          </jsp:forward>
        <%
      }
      else if (ButtonPressed.equals("Amend a Site"))
      {
        BAN.setSiteVisibility("visible");
        %>
          <jsp:forward page="OSSBANMenu.jsp"/>
        <%
      }
      else if (ButtonPressed.equals("Amend Site 2"))
      {
	siteBAN.setGlobalCustomerId(BAN.getGlobalCustomerId());
	siteBAN.setGlobalCustomerName(BAN.getGlobalCustomerName());
	siteBAN.setDivision_Id(BAN.getDivision_Id());
	siteBAN.setBanIdentifier("");
        siteBAN.setSiteReference((String)request.getParameter("Site_Reference"));
        siteBAN.getExistingSite();

	siteBAN.setAction("Create");
	siteBAN.setMode("Amend");
        %>
          <jsp:forward page="SiteBAN.jsp"/>
        <%
      }
      else if (ButtonPressed.startsWith("Create a Site"))
      {//values not inherited, so do it manually
	siteBAN.setGlobalCustomerId(BAN.getGlobalCustomerId());
	siteBAN.setGlobalCustomerName(BAN.getGlobalCustomerName());
	siteBAN.setDivision_Id(BAN.getDivision_Id());
	siteBAN.setBanIdentifier("");

	siteBAN.setAction("Create");
	siteBAN.setMode("Create");
        %>
          <jsp:forward page="SiteBAN.jsp"/>
	<%
      }
      else if (ButtonPressed.startsWith("Clear"))
      {
        BAN.setGlobalCustomerId("");
	BAN.setGlobalCustomerName("");
        BAN.setDivision_Id("");
        BAN.setCircuit_Reference("");
	BAN.setCarrier_Name("");
        %>
          <jsp:forward page="OSSBANMenu.jsp"/>
        <%
      }
      else if (ButtonPressed.startsWith("Use"))
      {
        if (SU.isNull((String)session.getAttribute("Global_Customer"),"").compareTo("") != 0)
        {
          BAN.setGlobalCustomerId(SU.isNull((String)session.getAttribute("Global_Customer"),""));
        }
        if (SU.isNull((String)session.getAttribute("GCD_Id"),"").compareTo("") != 0)
        {
          BAN.setDivision_Id(SU.isNull((String)session.getAttribute("GCD_Id"),""));
        }
        if (SU.isNull((String)session.getAttribute("Circuit"),"").compareTo("") != 0)
        {
          BAN.setCircuit_Reference((String)session.getAttribute("Circuit"));
        }
        if (SU.isNull((String)session.getAttribute("Carrier"),"").compareTo("") != 0)
        {
          BAN.setCarrier_Name(SU.isNull((String)session.getAttribute("Carrier"),""));
        }
        %>
        <jsp:forward page="OSSBANMenu.jsp"/>
        <%
      }
      //UD.store((String)session.getAttribute("User_Id"),"BAN",BAN);
      else if (ButtonPressed.startsWith("Create a Customer"))
      {
	cuBAN.setGlobalCustomerId(BAN.getGlobalCustomerId());
	cuBAN.setGlobalCustomerName(BAN.getGlobalCustomerName());
	cuBAN.setDivision_Id("");
	cuBAN.setCurrency_Desc("");
	cuBAN.getCustomer();

	cuBAN.setAction("Create");
	cuBAN.setMode("Add");
        %>
          <jsp:forward page="CustomerBAN.jsp"/>
	<%
      }
      else if (ButtonPressed.startsWith("Create a Carrier"))
      {
	caBAN.setAction("Create");
	caBAN.setMode("Create");
        %>
          <jsp:forward page="CarrierBAN.jsp"/>
	<%
      }
      else if (ButtonPressed.startsWith("Amend a Carrier"))
      {
	caBAN.getCarrier(SU.isNull((String)request.getParameter("Carrier"),""));

	caBAN.setAction("Create");
	caBAN.setMode("Amend");
        %>
          <jsp:forward page="CarrierBAN.jsp"/>
	<%
      }
      else if (ButtonPressed.startsWith("Amend a Customer"))
      {
	cuBAN.setAction("Create");
	cuBAN.setMode("Amend");

	cuBAN.setGlobalCustomerId(BAN.getGlobalCustomerId());
	cuBAN.setGlobalCustomerName(BAN.getGlobalCustomerName());
	cuBAN.setDivision_Id(BAN.getDivision_Id());
	cuBAN.getCustomer();
        cuBAN.setGroupName((String)session.getAttribute("Group_Name"));
        %>
          <jsp:forward page="CustomerBAN.jsp"/>
	<%
      }
      else if (ButtonPressed.startsWith("Add an Division to a Customer"))
      {
	cuBAN.setGlobalCustomerId(BAN.getGlobalCustomerId());
	cuBAN.setGlobalCustomerName(BAN.getGlobalCustomerName());
	cuBAN.setDivision_Id("");
	cuBAN.setCurrency_Desc("");
	cuBAN.getCustomer();

	cuBAN.setAction("Create");
	cuBAN.setMode("Add");
        %>
          <jsp:forward page="CustomerBAN.jsp"/>
	<%
      }
      else if (ButtonPressed.startsWith("Create an Adjustment"))
      {
	chBAN.Reset();
	chBAN.setRequired_BAN_Effective_Date("Today");
	chBAN.setGlobalCustomerId(BAN.getGlobalCustomerId());
	chBAN.setGlobalCustomerName(BAN.getGlobalCustomerName());
	chBAN.setDivision_Id(BAN.getDivision_Id());

	chBAN.setAction("Create");
	chBAN.setMode("Create");

	chBAN.setCurrency_Desc(BAN.getCurrency_Desc());

	chBAN.setOSS_Charge_Type("06");

	chBAN.setCharge_Category("05 Adjustment");
        //Random gen = new Random();
        //long chargeIdCheck = gen.nextLong();
        long chargeIdCheck = System.currentTimeMillis();
        chBAN.setChargeIdCheck(chargeIdCheck);
        %>
          <jsp:forward page="ChargeBAN.jsp">
            <jsp:param name="chargeId" value="<%=chargeIdCheck%>"/>
          </jsp:forward>
	<%
      }
      else if (ButtonPressed.startsWith("Create a Credit"))
      {
	ctBAN.setCircuit_Reference(BAN.getCircuit_Reference());
	ctBAN.getCircuit();
	chBAN.Reset();
	chBAN.setRequired_BAN_Effective_Date("Today");
	chBAN.setGlobalCustomerId(BAN.getGlobalCustomerId());
	chBAN.setGlobalCustomerName(BAN.getGlobalCustomerName());
	chBAN.setDivision_Id(BAN.getDivision_Id());
	chBAN.setCircuit_Reference(BAN.getCircuit_Reference());
	chBAN.setCurrency_Desc(BAN.getCurrency_Desc());

	chBAN.setAction("Create");
	chBAN.setMode("Create");

	chBAN.setCharge_Category("03 Credit");
	chBAN.setCurrency_Desc(BAN.getCurrency_Desc());
        //Random gen = new Random();
        //long chargeIdCheck = gen.nextLong();
        long chargeIdCheck = System.currentTimeMillis();
        chBAN.setChargeIdCheck(chargeIdCheck);
        %>
          <jsp:forward page="ChargeBAN.jsp">
            <jsp:param name="chargeId" value="<%=chargeIdCheck%>"/>
          </jsp:forward>
	<%
      }
      else if (ButtonPressed.startsWith("Create a One Off Charge"))
      {
	ctBAN.setCircuit_Reference(BAN.getCircuit_Reference());
	ctBAN.getCircuit();
	chBAN.Reset();
	chBAN.setRequired_BAN_Effective_Date("Today");
	chBAN.setGlobalCustomerId(BAN.getGlobalCustomerId());
	chBAN.setGlobalCustomerName(BAN.getGlobalCustomerName());
	chBAN.setDivision_Id(BAN.getDivision_Id());
	chBAN.setCircuit_Reference(BAN.getCircuit_Reference());

	chBAN.setAction("Create");
	chBAN.setMode("Create");
	chBAN.setCurrency_Desc(BAN.getCurrency_Desc());

	chBAN.setCharge_Category("02 One Off Charge");
        //Random gen = new Random();
        //long chargeIdCheck = gen.nextLong();
        long chargeIdCheck = System.currentTimeMillis();
        chBAN.setChargeIdCheck(chargeIdCheck);
        %>
          <jsp:forward page="ChargeBAN.jsp">
            <jsp:param name="chargeId" value="<%=chargeIdCheck%>"/>
          </jsp:forward>
	<%
      }
      else if (ButtonPressed.startsWith("Create a Recurring Charge"))
      {

	ctBAN.setCircuit_Reference(BAN.getCircuit_Reference());
	ctBAN.getCircuit();

	chBAN.Reset();
	chBAN.setRequired_BAN_Effective_Date("Today");
	chBAN.setAction("Create");
	chBAN.setMode("Create");

	chBAN.setGlobalCustomerId(BAN.getGlobalCustomerId());
	chBAN.setGlobalCustomerName(BAN.getGlobalCustomerName());
	chBAN.setDivision_Id(BAN.getDivision_Id());
	chBAN.setCircuit_Reference(BAN.getCircuit_Reference());

	chBAN.setCurrency_Desc(BAN.getCurrency_Desc());

	chBAN.setCharge_Category("01 Recurring Charge");
        //Random gen = new Random();
        //long chargeIdCheck = gen.nextLong();
        long chargeIdCheck = System.currentTimeMillis();
        chBAN.setChargeIdCheck(chargeIdCheck);
        %>
          <jsp:forward page="ChargeBAN.jsp">
            <jsp:param name="chargeId" value="<%=chargeIdCheck%>"/>
          </jsp:forward>
	<%
      }
      else if (ButtonPressed.startsWith("Cease a Recurring"))
      {

	ctBAN.setCircuit_Reference(BAN.getCircuit_Reference());

	chBAN.setAction("Create");
	chBAN.setMode("Cease");

        chBAN.setGlobal_Customer_Id_for_List(BAN.getGlobalCustomerId());
        chBAN.setDivision_for_List(BAN.getDivision_Id());
        chBAN.setGSR_for_List(BAN.getCircuit_Reference());
        chBAN.setCharge_Type_for_List("All");
        chBAN.setBAN_Month_for_List("All");
        chBAN.setShowFilters("visible");
        chBAN.setC2RefNo(BAN.getC2RefNo());
        chBAN.setC2RefNo_for_List(BAN.getC2RefNo());

        %>
          <jsp:forward page="ListCharges.jsp?firstTime=true"/>
        <%
      }
      else if (ButtonPressed.startsWith("Cease/Reprovide a Recurring Charge"))
      {

	ctBAN.setCircuit_Reference(BAN.getCircuit_Reference());

	chBAN.setAction("Create");
	chBAN.setMode("Cease / ");

        chBAN.setGlobal_Customer_Id_for_List(BAN.getGlobalCustomerId());
        chBAN.setDivision_for_List(BAN.getDivision_Id());
        chBAN.setGSR_for_List(BAN.getCircuit_Reference());
        chBAN.setCharge_Type_for_List("All");
        chBAN.setBAN_Month_for_List("All");
        chBAN.setShowFilters("visible");
        //chBAN.setReprovide(true);
	chBAN.setCharge_Category("01 Reprovide a Recurring Charge");
        chBAN.setGlobalCustomerId(BAN.getGlobalCustomerId());
        chBAN.setC2RefNo(BAN.getC2RefNo());
        chBAN.setC2RefNo_for_List(BAN.getC2RefNo());

        %>
          <jsp:forward page="ListCharges.jsp?firstTime=true"/>
        <%
      }
      else if ((ButtonPressed.startsWith("Amend a Recurring")) ||
        (ButtonPressed.startsWith("Delete a Recurring")))
      {
        /*ctBAN.Reset();
	ctBAN.setCircuit_Reference(BAN.getCircuit_Reference());
	ctBAN.getCircuit();
*/
        chBAN.Reset();
	chBAN.setAction("Create");
	chBAN.setMode(ButtonPressed.startsWith("Amend")?"Amend":"Delete");

        chBAN.setGlobal_Customer_Id_for_List(BAN.getGlobalCustomerId());
        chBAN.setGlobalCustomerId(BAN.getGlobalCustomerId());
        chBAN.setDivision_for_List(BAN.getDivision_Id());
        chBAN.setDivision_Id(BAN.getDivision_Id());
        chBAN.setGSR_for_List(BAN.getCircuit_Reference());
        chBAN.setCharge_Type_for_List("All");
        chBAN.setCharge_Description_for_List("All");
        chBAN.setBAN_Month_for_List("All");
        chBAN.setShowFilters("visible");
	chBAN.setCharge_Category("01 Recurring Charge");
        chBAN.setC2RefNo(BAN.getC2RefNo());
        chBAN.setC2RefNo_for_List(BAN.getC2RefNo());

        %>
          <jsp:forward page="ListCharges.jsp?firstTime=true"/>
        <%
      }
       else if ((ButtonPressed.startsWith("Amend a One Off Charge")) ||
        (ButtonPressed.startsWith("Delete a One Off Charge")))
      {
        ctBAN.Reset();
	ctBAN.setCircuit_Reference(BAN.getCircuit_Reference());
	ctBAN.getCircuit();

        chBAN.Reset();
	chBAN.setAction("Create");
	chBAN.setMode(ButtonPressed.startsWith("Amend")?"Amend":"Delete");

        chBAN.setGlobal_Customer_Id_for_List(BAN.getGlobalCustomerId());
        chBAN.setGlobalCustomerId(BAN.getGlobalCustomerId());
        chBAN.setDivision_for_List(BAN.getDivision_Id());
        chBAN.setDivision_Id(BAN.getDivision_Id());
        chBAN.setGSR_for_List(BAN.getCircuit_Reference());
        chBAN.setCharge_Type_for_List("All");
        chBAN.setBAN_Month_for_List("All");
        chBAN.setShowFilters("visible");
	chBAN.setCharge_Category("02 One Off Charge");
        chBAN.setC2RefNo(BAN.getC2RefNo());
        chBAN.setC2RefNo_for_List(BAN.getC2RefNo());

        %>
          <jsp:forward page="ListCharges.jsp?firstTime=true"/>
        <%
      }
       else if ((ButtonPressed.startsWith("Amend a Credit")) ||
        (ButtonPressed.startsWith("Delete a Credit")))
      {
        ctBAN.Reset();
	ctBAN.setCircuit_Reference(BAN.getCircuit_Reference());
	ctBAN.getCircuit();

        chBAN.Reset();
	chBAN.setAction("Create");
	chBAN.setMode(ButtonPressed.startsWith("Amend")?"Amend":"Delete");

        chBAN.setGlobal_Customer_Id_for_List(BAN.getGlobalCustomerId());
        chBAN.setGlobalCustomerId(BAN.getGlobalCustomerId());
        chBAN.setDivision_for_List(BAN.getDivision_Id());
        chBAN.setDivision_Id(BAN.getDivision_Id());
        chBAN.setGSR_for_List(BAN.getCircuit_Reference());
        chBAN.setCharge_Type_for_List("All");
        chBAN.setBAN_Month_for_List("All");
        chBAN.setShowFilters("visible");
	chBAN.setCharge_Category("03 Credit");
        chBAN.setC2RefNo(BAN.getC2RefNo());
        chBAN.setC2RefNo_for_List(BAN.getC2RefNo());

        %>
          <jsp:forward page="ListCharges.jsp?firstTime=true"/>
        <%
      }
      else if (ButtonPressed.startsWith("Amend Circuit"))
      {
	ctBAN.setGlobalCustomerName(BAN.getGlobalCustomerName());
	ctBAN.setGlobalCustomerId(BAN.getGlobalCustomerId());
	ctBAN.setDivision_Id(BAN.getDivision_Id());
	ctBAN.setCircuit_Reference(BAN.getCircuit_Reference());
	ctBAN.getCircuit();

	ctBAN.setAction("Create");
	ctBAN.setMode("Amend");
        %>
          <jsp:forward page="CircuitBAN.jsp"/>
	<%
      }
      else if (ButtonPressed.startsWith("Cease a Circuit"))
      {
	ctBAN.setGlobalCustomerName(BAN.getGlobalCustomerName());
	ctBAN.setGlobalCustomerId(BAN.getGlobalCustomerId());
	ctBAN.setDivision_Id(BAN.getDivision_Id());
	ctBAN.setCircuit_Reference(BAN.getCircuit_Reference());
	ctBAN.getCircuit();

	ctBAN.setAction("Create");
	ctBAN.setMode("Cease");
        %>
          <jsp:forward page="CircuitBAN.jsp"/>
	<%
      }
      else if (ButtonPressed.startsWith("Create a Circuit"))
      {//values not inherited, so do it manually
	ctBAN.setGlobalCustomerId(BAN.getGlobalCustomerId());
	ctBAN.setGlobalCustomerName(BAN.getGlobalCustomerName());
	ctBAN.setDivision_Id(BAN.getDivision_Id());
        ctBAN.setC2RefNo(BAN.getC2RefNo());
	ctBAN.setBanIdentifier("");

	ctBAN.setAction("Create");
	ctBAN.setMode("Create");
        %>
          <jsp:forward page="CircuitBAN.jsp"/>
	<%
      }
      else
      {//No button pressed, so must be a Refresh
        %>
          <jsp:forward page="OSSBANMenu.jsp"/>
        <%
      }
      %>
</body>
</html>
