<html>
<head>
</head>
<%@ page import="DBUtilities.*,JavaUtil.StringUtil"%>

<jsp:useBean id="BAN" class="DBUtilities.BANBean" scope="session"/>
<jsp:useBean id="chBAN" class="DBUtilities.FixedChargeBANBean" scope="session"/>

<%! JavaUtil.StringUtil SU = new JavaUtil.StringUtil();
    String ButtonPressed,System;
%>
<%
    ButtonPressed=(String)request.getParameter("ButtonPressed");
    System=(String)session.getAttribute("System");

    if (ButtonPressed.startsWith("Create BAN Menu"))
    {
      %>
	<jsp:forward page="GDBANMenu.jsp"/>
      <%
    }
    if (ButtonPressed.startsWith("Most Recent Values"))
    {
      chBAN.setMRUValues();
      chBAN.setShowFilters("visible");
      %>
	<jsp:forward page="ListFixedCharges.jsp"/>
      <%
    }
    else if (ButtonPressed.startsWith("Reset"))
    {
      chBAN.setGlobal_Customer_Id_for_List("All");
      chBAN.setBAN_Month_for_List("All");
      chBAN.setCharge_Description_for_List("All");
      chBAN.setShowFilters("visible");
      %>
	<jsp:forward page="ListFixedCharges.jsp?View"/>
      <%
    }
    else if ((request.getParameter("Charge_Id") != null) && (!request.getParameter("Charge_Id").equals(" ")))
    {//a selection has been made
      chBAN.setCharge_Id(request.getParameter("Charge_Id"));
      chBAN.setUserId((String)session.getAttribute("User_Id"));

      if (chBAN.getCharge())
      {
        chBAN.setChargeBilled();
        %>
	  <jsp:forward page="FixedChargeBAN.jsp"/>
	<%
      }
      else
      {//Couldn't find Charge
        %>
         <jsp:forward page="ListFixedCharges.jsp"/>
        <%
      }
    }//end of entry selected from list
    else if (ButtonPressed.compareTo("")==0)
    {//A filter value was selected, so get new values from request
      chBAN.setBAN_Month_for_List(SU.isNull((String)request.getParameter("List_Month2"),""));
      chBAN.setCharge_Description_for_List(SU.isNull((String)request.getParameter("Description"),""));

      %>
	<jsp:forward page="ListFixedCharges.jsp"/>
      <%
    }
    else
    {//All other options are from the List requests menu
      if (ButtonPressed.compareTo("")!=0){BAN.setBANs_to_List(ButtonPressed);}
      chBAN.setShowFilters("hidden");
      %>
	<jsp:forward page="ListFixedCharges.jsp"/>
      <%
    }
    %>
</body>
</html>