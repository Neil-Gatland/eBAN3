<html>
<head>
</head>
<%@ page import="DBUtilities.*,JavaUtil.StringUtil"%>

<jsp:useBean id="BAN" class="DBUtilities.BANBean" scope="session"/>
<jsp:useBean id="disBAN" class="DBUtilities.ConglomDiscountBANBean" scope="session"/>

<%! JavaUtil.StringUtil SU = new JavaUtil.StringUtil();
    String ButtonPressed,System;
%>
<%
    ButtonPressed=request.getParameter("ButtonPressed");
    System=(String)session.getAttribute("System");
    BAN.setBilledProductForList(request.getParameter("conglomBilledProduct2"));

    if (ButtonPressed.equals("Conglom Desktop"))
    {
      %>
	<jsp:forward page="conglomDesktop.jsp"/>
      <%
    }
    else if (ButtonPressed.equals("Conglom Billing Menu"))
    {
      %>
	<jsp:forward page="conglomBillingMenu.jsp"/>
      <%
    }
    else if (ButtonPressed.equals("list_Update"))
    {
      disBAN.Reset();
      disBAN.setDiscountId(Long.parseLong(request.getParameter("discountId")));
      disBAN.setConglomCustomerId(BAN.getConglomCustomerId());
      disBAN.getDiscount();
      disBAN.setAction("Amend");
      disBAN.setMode("Amend");
      disBAN.setMessage("<font color=blue><b>Amend as required and then select " +
        "'Update' from the 'Options' menu above. Mandatory fields are shown in blue.</b></font>");
      disBAN.setUserId((String)session.getAttribute("User_Name"));
      %>
        <jsp:forward page="conglomDiscount.jsp"/>
      <%
    }
    else if (ButtonPressed.equals("list_Delete"))
    {
      disBAN.Reset();
      disBAN.setDiscountId(Long.parseLong(request.getParameter("discountId")));
      disBAN.setConglomCustomerId(BAN.getConglomCustomerId());
      disBAN.getDiscount();
      disBAN.setAction("Delete");
      disBAN.setMode("Delete");
      disBAN.setMessage("<font color=blue><b>Select 'Delete' from the 'Options' menu above</b></font>");
      disBAN.setUserId((String)session.getAttribute("User_Name"));
      %>
        <jsp:forward page="conglomDiscount.jsp"/>
      <%
    }
    else if (ButtonPressed.equals("Add"))
    {
      disBAN.Reset();
      disBAN.setAction("Add");
      disBAN.setMode("Create");
      disBAN.setUserId((String)session.getAttribute("User_Name"));
      disBAN.setConglomCustomerId(BAN.getConglomCustomerId());
      disBAN.setBilledProduct(BAN.getBilledProductForList().equals("All")?"":
        BAN.getBilledProductForList());
      %>
        <jsp:forward page="conglomDiscount.jsp"/>
      <%
    }
    else if (ButtonPressed.equals("Log Off"))
    {
      %>
	<jsp:forward page="Login.jsp?type=l"/>
      <%
    }
    else if (ButtonPressed.equals("Raise Query"))
    {
      %>
        <jsp:forward page="listConglomDiscounts.jsp">
         <jsp:param name="query" value="true"/>
          <jsp:param name="fromPage" value="listConglomDiscounts"/>
        </jsp:forward>
      <%
    }
    else if (ButtonPressed.startsWith("Reset"))
    {
      BAN.setBilledProductForList("All");
      %>
	<jsp:forward page="listConglomDiscounts.jsp"/>
      <%
    }
		else
    {//All other options are from the List requests menu
      //if (ButtonPressed.compareTo("")!=0){BAN.setBANs_to_List(ButtonPressed);}
      %>
	<jsp:forward page="listConglomDiscounts.jsp"/>
      <%
    }
    %>
</body>
</html>
