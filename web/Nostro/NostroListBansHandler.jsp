<html>
<head>
</head>
<%@ page import="DBUtilities.*,JavaUtil.StringUtil"%>

<jsp:useBean id="BAN" class="DBUtilities.BANBean" scope="session"/>
<jsp:useBean id="noscBAN" class="DBUtilities.NostroCustomerBANBean" scope="session"/>
<jsp:useBean id="nosaBAN" class="DBUtilities.NostroAccountBANBean" scope="session"/>
<jsp:useBean id="nosuBAN" class="DBUtilities.NostroUserBANBean" scope="session"/>
<jsp:useBean id="nospBAN" class="DBUtilities.NostroPayGroupBANBean" scope="session"/>

<%! JavaUtil.StringUtil SU = new JavaUtil.StringUtil();
    String ButtonPressed,Action,System;
%>
<%
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
      BAN.setPG_for_List("All");
      BAN.setUser_for_List("All");
      BAN.setGSR_for_List("All");
      BAN.setStatus_for_List("All");
      BAN.setBAN_Type_for_List("All");
      BAN.setBAN_Month_for_List("All");
      BAN.setShowFilters("visible");
      %>
	<jsp:forward page="ListBans.jsp?View"/>
      <%
    }
    //Was a re-order requested?
    //else if ((request.getParameter("OrderBy")!=null) && (request.getParameter("OrderBy").compareTo("") !=0))
    //{
	//BAN.setOrderBy(request.getParameter("OrderBy"));
      //>
	//<jsp:forward page="ListBans.jsp"/>
      //%
    //}
    else if ((request.getParameter("BAN_Id") != null) && (!request.getParameter("BAN_Id").equals(" ")))
    {//a selection has been made
      BAN.setBanIdentifier(request.getParameter("BAN_Id"));
      Action=BAN.getAction();
      BAN.setUserId((String)session.getAttribute("User_Id"));
      //Find out what type of BAN it is
      if (BAN.findBanType())
      {//Redirect the request according to the Ban Type
	if (BAN.getBanType().startsWith("Nostro Customer"))
        {
	  noscBAN.setBanIdentifier(request.getParameter("BAN_Id"));
	  noscBAN.getCustomerBAN();
	  noscBAN.setAction(Action);
	  noscBAN.setdefaultFieldModes();
          %>
            <jsp:forward page="CustomerInitBAN.jsp"/>
          <%
        }
	else if (BAN.getBanType().startsWith("Nostro Account"))
        {
	  nosaBAN.setBanIdentifier(request.getParameter("BAN_Id"));
	  nosaBAN.getAccountBAN();
	  nosaBAN.setAction(Action);
	  nosaBAN.setdefaultFieldModes();
          %>
            <jsp:forward page="AccountInitBAN.jsp"/>
          <%
        }
	else if (BAN.getBanType().startsWith("Nostro Payment Group"))
        {
	  nospBAN.setBanIdentifier(request.getParameter("BAN_Id"));
	  nospBAN.getPaymentGroupBAN();
	  nospBAN.setAction(Action);
	  nospBAN.setdefaultFieldModes();
          %>
            <jsp:forward page="PaymentGroupBAN.jsp"/>
          <%
        }
	else if (BAN.getBanType().startsWith("Nostro User"))
        {
	  nosuBAN.setBanIdentifier(request.getParameter("BAN_Id"));
	  nosuBAN.getUserBAN();
	  nosuBAN.setAction(Action);
	  nosuBAN.setdefaultFieldModes();
          %>
            <jsp:forward page="UserBAN.jsp"/>
          <%
        }
	else
	{//Unknown BAN Type
	  session.setAttribute("Error","Eh? "+BAN.getBanType());
          %>
          <jsp:forward page="ListBans.jsp"/>
          <%
	}
      }
      else
      {//Couldn't find BAN
	session.setAttribute("Error",BAN.getMessage());
        %>
         <jsp:forward page="ListBans.jsp"/>
        <%
      }
    }//end of entry selected from list
    else if (ButtonPressed.compareTo("")==0)
    {//A filter value was selected, so get new values from request
      BAN.setShowFilters("hidden");
      BAN.setGlobal_Customer_Id_for_List(SU.isNull((String)request.getParameter("Nostro_BAN_Customers"),""));
      BAN.setAccount_for_List(SU.isNull((String)request.getParameter("Nostro_BAN_Accounts"),""));
      BAN.setPG_for_List(SU.isNull((String)request.getParameter("Nostro_BAN_Payment_Groups"),""));
      BAN.setUser_for_List(SU.isNull((String)request.getParameter("Nostro_BAN_Users"),""));

      BAN.setStatus_for_List((String)request.getParameter("Ban_Status"));
      BAN.setBAN_Type_for_List((String)request.getParameter("Nostro_BAN_Type"));
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