<html>
<head>
</head>
<%@ page import="DBUtilities.*,JavaUtil.StringUtil"%>

<jsp:useBean id="BAN" class="DBUtilities.BANBean" scope="session"/>
<jsp:useBean id="manBAN" class="DBUtilities.ConglomManualInvoiceBANBean" scope="session"/>

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
    else if (ButtonPressed.equals("Apply Lead Discount"))
    {
      if (BAN.getBilledProductForList().equals("All"))
      {
        BAN.setMessage("<font color=blue><b>Please select a Billed Product</b></font>");
      %>
	<jsp:forward page="listConglomManualInvoices.jsp"/>
      <%
      }
      else
      {
        BAN.applyConglomDiscount((String)session.getAttribute("User_Id"), "Lead",
          "N/A");
      %>
	<jsp:forward page="listConglomManualInvoices.jsp"/>
      <%
      }
    }
    else if (ButtonPressed.equals("list_Update"))
    {
      manBAN.Reset();
      manBAN.setInvoiceId(Long.parseLong(request.getParameter("invoiceId")));
      manBAN.setConglomCustomerId(BAN.getConglomCustomerId());
      manBAN.getConglomMonthlyBillStartDate();
      manBAN.getInvoice();
      manBAN.setAction("Amend");
      manBAN.setMode("Amend");
      manBAN.setMessage("<font color=blue><b>Amend as required and then select " +
        "'Update' from the 'Options' menu above. Mandatory fields are shown in blue.</b></font>");
      //manBAN.setUserId((String)session.getAttribute("User_Name"));
      manBAN.setUserId((String)session.getAttribute("User_Id"));
      //manBAN.setUserId(BAN.getActAsLogon());
      %>
        <jsp:forward page="conglomInvoice.jsp"/>
      <%
    }
    else if (ButtonPressed.equals("list_Delete"))
    {
      /*manBAN.Reset();
      manBAN.setInvoiceId(Long.parseLong(request.getParameter("invoiceId")));
      manBAN.setConglomCustomerId(BAN.getConglomCustomerId());
      manBAN.getInvoice();
      manBAN.setAction("Delete");
      manBAN.setMode("Delete");
      manBAN.setMessage("<font color=blue><b>Select 'Delete' from the 'Options' menu above</b></font>");
      manBAN.setUserId((String)session.getAttribute("User_Name"));*/
      %>
        <jsp:forward page="conglomInvoice.jsp"/>
      <%
    }
    else if (ButtonPressed.equals("Add"))
    {
      manBAN.Reset();
      manBAN.setConglomCustomerId(BAN.getConglomCustomerId());
      manBAN.getConglomMonthlyBillStartDate();
      manBAN.setAction("Add");
      manBAN.setMode("Create");
      manBAN.setBilledProduct(BAN.getBilledProductForList().equals("All")?"":
        BAN.getBilledProductForList());
      //manBAN.setConglomDiscountNetAmount(BAN.getConglomDiscountNetAmount());
      manBAN.setUserId((String)session.getAttribute("User_Id"));
      //manBAN.setUserId(BAN.getActAsLogon());
      %>
        <jsp:forward page="conglomInvoice.jsp"/>
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
        <jsp:forward page="listConglomManualInvoices.jsp">
         <jsp:param name="query" value="true"/>
          <jsp:param name="fromPage" value="listConglomManualInvoices"/>
        </jsp:forward>
      <%
    }
    else if (ButtonPressed.startsWith("Reset"))
    {
      BAN.setBilledProductForList("All");
      %>
	<jsp:forward page="listConglomManualInvoices.jsp"/>
      <%
    }
		else
    {//All other options are from the List requests menu
      //if (ButtonPressed.compareTo("")!=0){BAN.setBANs_to_List(ButtonPressed);}
      %>
	<jsp:forward page="listConglomManualInvoices.jsp"/>
      <%
    }
    %>
</body>
</html>
