<html>
<head>
</head>
<%@ page import="JavaUtil.*"%>
<jsp:useBean id="BAN" class="DBUtilities.BANBean" scope="session"/>
<jsp:useBean id="chBAN" class="DBUtilities.FixedChargeBANBean" scope="session"/>

<%!     String ButtonPressed="",Global_Customer_Id="";
        JavaUtil.StringUtil SU = new JavaUtil.StringUtil();
        JavaUtil.UserData UD = new UserData();
%>
<%
    //Clear out BAN beans
    chBAN.Reset();
    session.setAttribute("PageSent",request.getRequestURI());
    ButtonPressed=(String)request.getParameter("ButtonPressed");
    BAN.setSiteVisibility("hidden");

    if (ButtonPressed.startsWith("Connect"))
    {
      %>
	<jsp:forward page="DesktopLogon.jsp?type=GD"/>
      <%
    }

    Global_Customer_Id=(String)request.getParameter("Global_Customer");
    if (BAN.getEdifySetCustomer())
    {
      Global_Customer_Id=BAN.getGlobalCustomerId();
    }

    if (Global_Customer_Id.compareTo(BAN.getGlobalCustomerId())!=0)
    {
      session.setAttribute("Global_Customer_Id",Global_Customer_Id);
      BAN.setGlobalCustomerId(Global_Customer_Id);
      BAN.setDivision_Id("");
      BAN.setC2RefNo("");
    }

    BAN.setErrored("clear");
    chBAN.setErrored("clear");


      //Navigation

      if (ButtonPressed.equals(""))
      {//A Refresh
        %>
          <jsp:forward page="GDBANMenu.jsp"/>
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
          <jsp:forward page="GDBANMenu.jsp"/>
        <%
      }
      else if (ButtonPressed.startsWith("Create a Fixed Charge"))
      {


	chBAN.Reset();
	chBAN.setRequired_BAN_Effective_Date("Today");
	chBAN.setAction("Create");
	chBAN.setMode("Create");

	chBAN.setGlobalCustomerId(BAN.getGlobalCustomerId());
	chBAN.setGlobalCustomerName(BAN.getGlobalCustomerName());
	chBAN.setDivision_Id(BAN.getDivision_Id());
	chBAN.setCircuit_Reference(BAN.getCircuit_Reference());

	chBAN.setCurrency_Desc(BAN.getCurrency_Desc());

	//chBAN.setCharge_Category("01 Recurring Charge");

        %>
          <jsp:forward page="FixedChargeBAN.jsp"/>
	<%
      }
      else if ((ButtonPressed.startsWith("Amend a Fixed")) ||
        (ButtonPressed.startsWith("Delete a Fixed")))
      {

        chBAN.Reset();
	chBAN.setAction("Create");
	chBAN.setMode(ButtonPressed.startsWith("Amend")?"Amend":"Delete");

        chBAN.setGlobal_Customer_Id_for_List(BAN.getGlobalCustomerId());
        chBAN.setGlobalCustomerId(BAN.getGlobalCustomerId());
        chBAN.setCharge_Type_for_List("All");
        chBAN.setCharge_Description_for_List("All");
        chBAN.setBAN_Month_for_List("All");
        chBAN.setShowFilters("visible");
	//chBAN.setCharge_Category("01 Recurring Charge");

        %>
          <jsp:forward page="ListFixedCharges.jsp?firstTime=true"/>
        <%
      }
      else
      {//No button pressed, so must be a Refresh
        %>
          <jsp:forward page="GDBANMenu.jsp"/>
        <%
      }
      %>
</body>
</html>
