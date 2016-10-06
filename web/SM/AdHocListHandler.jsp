<html>
<head>
</head>
<%@ page import="DBUtilities.*,JavaUtil.StringUtil"%>

<jsp:useBean id="BAN" class="DBUtilities.BANBean" scope="session"/>
<jsp:useBean id="acb" class="DBUtilities.AssureChargeBean" scope="session"/>

<%! JavaUtil.StringUtil SU = new JavaUtil.StringUtil();
    String ButtonPressed,Action,System;
%>
<%
    ButtonPressed=(String)request.getParameter("ButtonPressed");
    Action=(String)request.getParameter("Action");
    BAN.setAction(Action);
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
      BAN.setInvoice_for_List("All");
      BAN.setStatus_for_List("All");
      BAN.setBAN_Type_for_List("All");
      BAN.setCreated_Month_for_List("All");
      BAN.setVisit_Month_for_List("All");
      BAN.setShowFilters("visible");
      if (Action.equals("Processed"))
      {
      %>
	<jsp:forward page="ListBans.jsp?Processed"/>
      <%
      }
      else
      {
      %>
	<jsp:forward page="ListBans.jsp"/>
      <%
      }

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
	if (BAN.getBanType().startsWith("Assure Charge"))
        {
          acb.Reset();
	  acb.setBanIdentifier(request.getParameter("BAN_Id"));
	  acb.getAssureCharge();
	  acb.setAction(Action);
          acb.setMode("Add");
	  acb.setdefaultFieldModes();
          %>
            <jsp:forward page="AssureCharge.jsp"/>
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
      BAN.setGlobal_Customer_Id_for_List(SU.isNull((String)request.getParameter("Ad_Hoc_Customers"),""));
      BAN.setAccount_for_List(SU.isNull((String)request.getParameter("Ad_Hoc_Accounts"),""));
      BAN.setInvoice_for_List(SU.isNull((String)request.getParameter("Ad_Hoc_Invoices"),""));
      BAN.setStatus_for_List((String)request.getParameter("Ad_Hoc_Ban_Status"));
      BAN.setBAN_Type_for_List((String)request.getParameter("Ad_Hoc_Type"));
      BAN.setVisit_Month_for_List((String)request.getParameter("Visit_Month"));
      BAN.setCreated_Month_for_List((String)request.getParameter("Invoice_Month"));
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