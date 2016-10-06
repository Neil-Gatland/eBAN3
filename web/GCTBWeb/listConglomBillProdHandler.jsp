<html>
<head>
</head>
<%@ page import="DBUtilities.*,JavaUtil.StringUtil"%>

<jsp:useBean id="BAN" class="DBUtilities.BANBean" scope="session"/>
<jsp:useBean id="bpBAN" class="DBUtilities.ConglomManualInvoiceBANBean" scope="session"/>

<%! JavaUtil.StringUtil SU = new JavaUtil.StringUtil();
    String ButtonPressed,System;
%>
<%

    ButtonPressed=request.getParameter("ButtonPressed");
    System=(String)session.getAttribute("System");
    BAN.setBilledProductForList(request.getParameter("conglomBilledProduct4"));
    BAN.setItemTypeForList(request.getParameter("conglomItem"));
    BAN.setPeriodForList(request.getParameter("conglomPeriod"));

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
    else if (ButtonPressed.equals("Add CR/Docket Numbers"))
    {
      String list = BAN.getConglomCRDocketList();
      if (list.startsWith("<table border=0>"))
      {
        %>
          <jsp:forward page="listConglomCRDocket.jsp?refresh=false"/>
        <%
      }
      else
      {
        BAN.setMessage(list);
        %>
          <jsp:forward page="listConglomBillProd.jsp"/>
        <%
      }
    }
    else if (ButtonPressed.equals("Apply Lead Discount"))
    {
      if (BAN.getBilledProductForList().equals("All"))
      {
        BAN.setMessage("<font color=blue><b>Please select a Billed Product</b></font>");
      %>
	<jsp:forward page="listConglomBillProd.jsp"/>
      <%
      }
      else
      {
        BAN.applyConglomDiscount((String)session.getAttribute("User_Id"), "Lead",
          "N/A");
      %>
	<jsp:forward page="listConglomBillProd.jsp"/>
      <%
      }
    }
    else if (ButtonPressed.equals("list_Update"))
    {
      String itemId = request.getParameter("itemId");
      String product = request.getParameter("product");
      String sourceAccount = request.getParameter("sourceAccount");
      String status = request.getParameter("status");
      %>
        <jsp:forward page="listConglomBillProd.jsp">
         <jsp:param name="conglomItemStatus" value="true"/>
          <jsp:param name="itemId" value="<%=itemId%>"/>
          <jsp:param name="product" value="<%=product%>"/>
          <jsp:param name="sourceAccount" value="<%=sourceAccount%>"/>
          <jsp:param name="status" value="<%=status%>"/>
        </jsp:forward>
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
        <jsp:forward page="listConglomBillProd.jsp">
         <jsp:param name="query" value="true"/>
          <jsp:param name="fromPage" value="listConglomBillProd"/>
        </jsp:forward>
      <%
    }
    else if (ButtonPressed.startsWith("Reset"))
    {
      BAN.setBilledProductForList("All");
      %>
	<jsp:forward page="listConglomBillProd.jsp"/>
      <%
    }
		else
    {//All other options are from the List requests menu
      //if (ButtonPressed.compareTo("")!=0){BAN.setBANs_to_List(ButtonPressed);}
      %>
	<jsp:forward page="listConglomBillProd.jsp"/>
      <%
    }
    %>
</body>
</html>
