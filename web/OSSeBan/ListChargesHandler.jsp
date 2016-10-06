<html>
<head>
</head>
<%@ page isThreadSafe="false" %>
<%@ page import="DBUtilities.*,JavaUtil.StringUtil"%>

<jsp:useBean id="BAN" class="DBUtilities.BANBean" scope="session"/>
<!--jsp:useBean id="chBAN" class="DBUtilities.OSSChargeBANBean" scope="session"/-->
<jsp:useBean id="ctBAN" class="DBUtilities.CircuitBANBean" scope="session"/>

<%  JavaUtil.StringUtil SU = new JavaUtil.StringUtil();
    String ButtonPressed,System;
    String userId = (String)session.getAttribute("User_Id");
    OSSChargeBANBean chBAN = (OSSChargeBANBean)session.getAttribute("chBAN" + userId);
    if (chBAN == null)
    {
      chBAN = new OSSChargeBANBean();
      session.setAttribute("chBAN" + userId, chBAN);
    }

    ButtonPressed=(String)request.getParameter("ButtonPressed");
    System=(String)session.getAttribute("System");

    if (ButtonPressed.startsWith("Create BAN Menu"))
    {
      %>
	<jsp:forward page="OSSBANMenu.jsp"/>
      <%
    }
    if (ButtonPressed.startsWith("Most Recent Values"))
    {
      chBAN.setMRUValues();
      chBAN.setShowFilters("visible");
      %>
	<jsp:forward page="ListCharges.jsp"/>
      <%
    }
    else if (ButtonPressed.startsWith("Reset"))
    {
      chBAN.setGlobal_Customer_Id_for_List("All");
      chBAN.setAccount_for_List("All");
      chBAN.setGSR_for_List("All");
      //chBAN.setStatus_for_List("All");
      chBAN.setCharge_Type_for_List("All");
      chBAN.setBAN_Month_for_List("All");
      chBAN.setCharge_Description_for_List("All");
      chBAN.setShowFilters("visible");
      %>
	<jsp:forward page="ListCharges.jsp?View"/>
      <%
    }
    else if ((request.getParameter("Charge_Id") != null) && (!request.getParameter("Charge_Id").equals(" ")))
    {//a selection has been made
      String chargeCategory = chBAN.getCharge_Category() + " " +
        chBAN.getCharge_CategoryDisplay();
      chBAN.Reset();
      ctBAN.Reset();
      chBAN.setCharge_Category(chargeCategory);
      String chargeId = request.getParameter("Charge_Id");
      chBAN.setCharge_Id(chargeId);
      chBAN.setUserId((String)session.getAttribute("User_Id"));

      if (chBAN.getCharge())
      {
	ctBAN.setCircuit_Reference(chBAN.getCircuit_Reference());
	ctBAN.getCircuit();
        //chBAN.setMode("Amend");
        //chBAN.setAction("Create");
        chBAN.setChargeBilled();
        //chBAN.setToDateNullable();
        %>
	  <jsp:forward page="ChargeBAN.jsp">
            <jsp:param name="chargeId" value="<%=chargeId%>"/>
          </jsp:forward>
	<%
      }
      else if (chBAN.getCharge()) //have another go - don't ask!
      {
	ctBAN.setCircuit_Reference(chBAN.getCircuit_Reference());
	ctBAN.getCircuit();
        //chBAN.setMode("Amend");
        //chBAN.setAction("Create");
        chBAN.setChargeBilled();
        //chBAN.setToDateNullable();
        %>
	  <jsp:forward page="ChargeBAN.jsp">
            <jsp:param name="chargeId" value="<%=chargeId%>"/>
          </jsp:forward>
	<%
      }
      else
      {//Couldn't find Charge
chBAN.setMessage("Couldn't find Charge for id " + chargeId + " button " +
ButtonPressed);
        %>
         <jsp:forward page="ListCharges.jsp"/>
        <%
      }
    }//end of entry selected from list
    else if (ButtonPressed.compareTo("")==0)
    {//A filter value was selected, so get new values from request
/*      chBAN.setShowFilters("hidden");
      chBAN.setGlobal_Customer_Id_for_List(SU.isNull((String)request.getParameter("Global_Customer"),""));
      chBAN.setAccount_for_List(SU.isNull((String)request.getParameter("Account"),""));
      chBAN.setCharge_Type_for_List(SU.isNull((String)request.getParameter("OSS_Charge_Type"),""));*/
      chBAN.setBAN_Month_for_List(SU.isNull((String)request.getParameter("List_Month2"),""));
      chBAN.setCharge_Description_for_List(SU.isNull((String)request.getParameter("Description"),""));

      if (System.compareTo("OSS") == 0)
      {
//        chBAN.setGSR_for_List(SU.isNull((String)request.getParameter("Circuit"),""));
      }
      else
      {
        chBAN.setGSR_for_List((String)request.getParameter("Global_Service_Reference"));
      }
      %>
	<jsp:forward page="ListCharges.jsp"/>
      <%
    }
    else
    {//All other options are from the List requests menu
      if (ButtonPressed.compareTo("")!=0){BAN.setBANs_to_List(ButtonPressed);}
      chBAN.setShowFilters("hidden");
      %>
	<jsp:forward page="ListCharges.jsp"/>
      <%
    }
    %>
</body>
</html>