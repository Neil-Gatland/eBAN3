<html>
<head>
</head>
<%@ page import="JavaUtil.StringUtil"%>

<jsp:useBean id="BAN" class="DBUtilities.BANBean" scope="session"/>
<jsp:useBean id="gcBAN" class="DBUtilities.GCBCustomerBANBean" scope="session"/>
<jsp:useBean id="irBAN" class="DBUtilities.InvoiceRegionBANBean" scope="session"/>

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
    else if (ButtonPressed.equals("Account Administration Main Menu"))
    {
      %>
        <jsp:forward page="Welcome.jsp"/>
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
	<jsp:forward page="ListBans.jsp?View"/>
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
	if (BAN.getBanType().startsWith("Customer"))
        {
          gcBAN.Reset();
	  gcBAN.setBanIdentifier(request.getParameter("BAN_Id"));
          gcBAN.setGroupName((String)session.getAttribute("Group_Name"));
	  gcBAN.getCustomerBAN();
	  gcBAN.setAction(Action);
	  gcBAN.setdefaultFieldModes();
          gcBAN.setMessage("");
          %>
            <jsp:forward page="GCBCustomerBAN.jsp"/>
          <%
        }
	else if (BAN.getBanType().startsWith("Invoice Region"))
        {
          irBAN.Reset();
	  irBAN.setBanIdentifier(request.getParameter("BAN_Id"));
	  irBAN.getInvoiceRegionBAN();
	  irBAN.setAction(Action);
	  irBAN.setdefaultFieldModes();
          %>
            <jsp:forward page="InvoiceRegionBAN.jsp"/>
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
      BAN.setGlobal_Customer_Id_for_List(SU.isNull((String)request.getParameter("GCB_BAN_Customers"),""));
      BAN.setInvoice_Region_for_List(SU.isNull((String)request.getParameter("GCB_BAN_Invoice_Regions"),""));
      BAN.setStatus_for_List((String)request.getParameter("Ban_Status"));
      BAN.setBAN_Type_for_List((String)request.getParameter("New_BAN_Type"));
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