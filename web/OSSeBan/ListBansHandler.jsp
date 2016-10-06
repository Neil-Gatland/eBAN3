<html>
<head>
</head>
<%@ page isThreadSafe="false" %>
<%@ page import="DBUtilities.*,JavaUtil.StringUtil"%>

<jsp:useBean id="BAN" class="DBUtilities.BANBean" scope="session"/>
<jsp:useBean id="cuBAN" class="DBUtilities.OSSCustomerBANBean" scope="session"/>
<!--jsp:useBean id="chBAN" class="DBUtilities.OSSChargeBANBean" scope="session"/-->
<jsp:useBean id="ctBAN" class="DBUtilities.CircuitBANBean" scope="session"/>
<jsp:useBean id="caBAN" class="DBUtilities.CarrierBANBean" scope="session"/>

<%  JavaUtil.StringUtil SU = new JavaUtil.StringUtil();
    String ButtonPressed,Action,System;
    String userId = (String)session.getAttribute("User_Id");
    OSSChargeBANBean chBAN = (OSSChargeBANBean)session.getAttribute("chBAN" + userId);
    if (chBAN == null)
    {
      chBAN = new OSSChargeBANBean();
      session.setAttribute("chBAN" + userId, chBAN);
    }

    ButtonPressed=(String)request.getParameter("ButtonPressed");
    System=(String)session.getAttribute("System");
    //Has an entry been selected?
    if (ButtonPressed.startsWith("Change"))
    {
      %>
	<jsp:forward page="BANFilter.jsp"/>
      <%
    }
    else if (ButtonPressed.startsWith("Most Recent Values"))
    {
      BAN.setMRUValues();
      BAN.setShowFilters("visible");
      %>
	<jsp:forward page="ListBans.jsp"/>
      <%
    }
    else if (ButtonPressed.startsWith("Reset"))
    {
      BAN.setGlobal_Customer_Id_for_List("All");
      BAN.setAccount_for_List("All");
      BAN.setGSR_for_List("All");
      BAN.setStatus_for_List("All");
      BAN.setBAN_Type_for_List("All");
      BAN.setBAN_Month_for_List("All");
      BAN.setShowFilters("visible");
      %>
	<jsp:forward page="ListBans.jsp"/>
      <%
    }
    else if ((request.getParameter("BAN_Id") != null) && (!request.getParameter("BAN_Id").equals(" ")))
    {//a selection has been made
      BAN.setBanIdentifier(request.getParameter("BAN_Id"));
      Action=BAN.getAction();
      BAN.setUserId((String)session.getAttribute("User_Id"));
      //Find out what type of BAN it is
      if (BAN.findBanType())
      {//Redirect the request according to the Ban Type
      	if ((!BAN.getBanType().startsWith("Customer")) &&
          (BAN.getGroupName().equals("MANS")))
        {
          BAN.setBanIdentifier("");
	  session.setAttribute("Message","<font color=red>You are not authorized to view this type of BAN</font>");
          %>
          <jsp:forward page="ListBans.jsp"/>
          <%
        }
        else if (BAN.getBanType().startsWith("OSS Charge"))
        {
	  chBAN.Reset();
	  chBAN.setBanIdentifier(request.getParameter("BAN_Id"));
	  //ctBAN.setCircuitRefFromBAN(request.getParameter("BAN_Id"));
  	  ctBAN.getCircuit(request.getParameter("BAN_Id"));
	  chBAN.getChargeBan();
	  chBAN.setCircuit_Reference(ctBAN.getCircuit_Reference());
	  chBAN.setAction(Action);
	  chBAN.setdefaultFieldModes();
	  if (Action.compareTo("Amend")==0)
	  {
            %>
            <jsp:forward page="ChargeBAN.jsp"/>
            <%
	  }
	  else
	  {
            %>
            <jsp:forward page="ChargeBAN.jsp"/>
            <%
	  }
        }
        else if (BAN.getBanType().startsWith("Circuit"))
        {
	  ctBAN.setBanIdentifier(request.getParameter("BAN_Id"));
  	  ctBAN.getCircuitBAN();
	  ctBAN.setAction(Action);
	  ctBAN.setdefaultFieldModes();
          %>
            <jsp:forward page="CircuitBAN.jsp"/>
          <%
        }
	else if (BAN.getBanType().startsWith("Customer"))
        {
	  cuBAN.setBanIdentifier(request.getParameter("BAN_Id"));
          cuBAN.setGroupName(BAN.getGroupName());
	  cuBAN.getCustomerBAN();
	  cuBAN.setAction(Action);
	  cuBAN.setdefaultFieldModes();
          if (((BAN.getGroupName().equals("MANS")) && (!cuBAN.getAccount_Id().startsWith("5"))) ||
            ((!BAN.getGroupName().equals("MANS")) && (cuBAN.getAccount_Id().startsWith("5"))))
          {
            BAN.setBanIdentifier("");
            session.setAttribute("Message","<font color=red>You are not authorized to view this BAN</font>");
            %>
            <jsp:forward page="ListBans.jsp"/>
            <%
          }
          else
          {
            %>
              <jsp:forward page="CustomerBAN.jsp"/>
            <%
          }
        }
	else if (BAN.getBanType().startsWith("Carrier"))
        {
	  caBAN.setBanIdentifier(request.getParameter("BAN_Id"));
	  caBAN.getCarrierBAN();
	  caBAN.setAction(Action);
	  caBAN.setdefaultFieldModes();
          %>
            <jsp:forward page="CarrierBAN.jsp"/>
          <%
        }
	else
	{//Unknown BAN Type
	  session.setAttribute("Message","Eh? "+BAN.getBanType());
          %>
          <jsp:forward page="ListBans.jsp"/>
          <%
	}
      }
      else
      {//Couldn't find BAN
	session.setAttribute("Message",BAN.getMessage());
        %>
         <jsp:forward page="ListBans.jsp"/>
        <%
      }
    }//end of entry selected from list
    else if (ButtonPressed.compareTo("")==0)
    {//A filter value was selected, so get new values from request
      BAN.setShowFilters("hidden");
      BAN.setGlobal_Customer_Id_for_List(SU.isNull((String)request.getParameter("BAN_Customers"),""));
      BAN.setAccount_for_List(SU.isNull((String)request.getParameter("BAN_Accounts"),""));

      if (System.compareTo("OSS") == 0)
      {
	BAN.setGSR_for_List((String)request.getParameter("BAN_Circuits"));
      }
      else
      {
        BAN.setGSR_for_List((String)request.getParameter("Global_Service_Reference"));
      }
      BAN.setStatus_for_List((String)request.getParameter("Ban_Status"));
      BAN.setBAN_Type_for_List((String)request.getParameter("BAN_Type"));
      BAN.setBAN_Month_for_List((String)request.getParameter("List_Month"));
      %>
	<jsp:forward page="ListBans.jsp"/>
      <%
    }
    else
    {//All other options are from the List requests menu
      if (ButtonPressed.compareTo("")!=0){BAN.setBANs_to_List(ButtonPressed);}
      BAN.setShowFilters("hidden");
      %>
	<jsp:forward page="ListBans.jsp"/>
      <%
    }
    %>
</body>
</html>