<html>
<head>
</head>
<body>
<%@ page import="JavaUtil.StringUtil"%>
<%@ page import="java.net.URLEncoder"%>
<%@ page import="java.util.Collection"%>
<jsp:useBean id="BAN" class="DBUtilities.BANBean" scope="session"/>
<jsp:useBean id="HB" class="HTMLUtil.HTMLBean" scope="session"/>
<jsp:useBean id="ccBAN" class="DBUtilities.ConglomCustomerBANBean" scope="session"/>
<%! String ButtonPressed, banId, listType;%>
<%
    ButtonPressed = request.getParameter("ButtonPressed");
    if (ButtonPressed.equals("Log Off"))
    {
      %>
	<jsp:forward page="Login.jsp?type=l"/>
      <%
    }
    else if (ButtonPressed.equals("Conglom Desktop"))
    {
      BAN.setRefreshCustomerGrid(true);
      %>
        <jsp:forward page="conglomDesktop.jsp"/>
      <%
    }
    else if (ButtonPressed.equals("Raise Query"))
    {
      %>
        <jsp:forward page="conglomBillingMenu.jsp">
          <jsp:param name="query" value="true"/>
          <jsp:param name="fromPage" value="conglomBillingMenu"/>
        </jsp:forward>
      <%
    }
    else if (ButtonPressed.equals("Generate Trial Bill"))
    {
      if (BAN.checkConglomSubmitEligibility(false))
      {
        ccBAN.Reset();
        ccBAN.setConglomCustomerId(BAN.getConglomCustomerId());
        ccBAN.setGlobalCustomerId(BAN.getGlobalCustomerId());
        ccBAN.setConglomInvoiceRef(BAN.getConglomInvoiceRef());
        ccBAN.getCustomer();
        ccBAN.setBillPeriodRef(BAN.getBillPeriodRef());
      %>
        <jsp:forward page="conglomBillingMenu.jsp">
          <jsp:param name="conglomBill" value="true"/>
          <jsp:param name="billType" value="Trial"/>
        </jsp:forward>
      <%
      }
      else
      {
        %>
          <jsp:forward page="conglomBillingMenu.jsp"/>
        <%
      }
    }
    else if (ButtonPressed.equals("View Trial Bill"))
    {
      BAN.setInvoiceNo(BAN.getConglomInvoiceRef());
      /*BAN.setAccountId(request.getParameter("accountId"));
      BAN.setAccountName(request.getParameter("accountName"));
      BAN.setTotalCharges(request.getParameter("total"));*/
      %>
        <jsp:forward page="listInvoiceReports.jsp?type=3"/>
      <%
      /*if (BAN.checkConglomSubmitEligibility(false))
      {
        ccBAN.Reset();
        ccBAN.setConglomCustomerId(BAN.getConglomCustomerId());
        ccBAN.setGlobalCustomerId(BAN.getGlobalCustomerId());
        ccBAN.setConglomInvoiceRef(BAN.getConglomInvoiceRef());
        ccBAN.getCustomer();
        ccBAN.setBillPeriodRef(BAN.getBillPeriodRef());*/
    }
    else if (ButtonPressed.equals("Generate Close Bill"))
    {
      if (BAN.checkConglomSubmitEligibility(true))
      {
        ccBAN.Reset();
        ccBAN.setConglomCustomerId(BAN.getConglomCustomerId());
        ccBAN.setGlobalCustomerId(BAN.getGlobalCustomerId());
        ccBAN.setConglomInvoiceRef(BAN.getConglomInvoiceRef());
        ccBAN.getCustomer();
        ccBAN.setBillPeriodRef(BAN.getBillPeriodRef());
        %>
        <jsp:forward page="conglomBillingMenu.jsp">
          <jsp:param name="conglomBill" value="true"/>
          <jsp:param name="billType" value="Close"/>
        </jsp:forward>
        <%
      }
      else
      {
        %>
          <jsp:forward page="conglomBillingMenu.jsp"/>
        <%
      }
    }
    else if (ButtonPressed.equals("Conglomerate Customer Profile Maintenance"))
    {
      ccBAN.Reset();
      ccBAN.setConglomCustomerId(BAN.getConglomCustomerId());
      ccBAN.getCustomer();
      ccBAN.checkTrialInvoice();
      ccBAN.setAction("Amend");
      ccBAN.setMode("Amend");
      ccBAN.setUserId((String)session.getAttribute("User_Id"));
      //ccBAN.setUserId(BAN.getActAsLogon());
      %>
        <jsp:forward page="conglomCustMaint.jsp"/>
      <%
    }
    else if (ButtonPressed.equals("Adjustments"))
    {
      %>
        <jsp:forward page="listConglomAdjustments.jsp"/>
      <%
    }
    else if (ButtonPressed.equals("Conglomerate Product Discounts"))
    {
      %>
        <jsp:forward page="listConglomDiscounts.jsp"/>
      <%
    }
    else if (ButtonPressed.equals("Manual input of Source Invoices"))
    {
      %>
        <jsp:forward page="listConglomManualInvoices.jsp"/>
      <%
    }
    else if (ButtonPressed.equals("Maintain Bill Prod"))
    {
      BAN.setBilledProductForList("All");
      BAN.setPeriodForList(BAN.getMaxConglomBillPeriod());
      BAN.setItemTypeForList("OPEN");
      %>
        <jsp:forward page="listConglomBillProd.jsp"/>
      <%
    }
    else if (ButtonPressed.equals("Re-pull Billing Data from Goldfish"))
    {
      if (BAN.checkConglomGoldfishQueue())
      {
        BAN.setMessage("<font color=red>This customer is already present " +
          "in the job queue and cannot be added again</font>");
        %>
          <jsp:forward page="conglomBillingMenu.jsp"/>
        <%
      }
      else
      {
        ccBAN.Reset();
        ccBAN.setConglomCustomerId(BAN.getConglomCustomerId());
        ccBAN.getCustomer();
        ccBAN.getMonthlyBillStartDate();
        %>
          <jsp:forward page="conglomBillingMenu.jsp">
            <jsp:param name="rePullGoldfish" value="true"/>
          </jsp:forward>
        <%
      }
    }
    else if (ButtonPressed.equals("Prior Period Set-Up"))
    {
      String invRef = BAN.checkForCurrentConglomInvoice();
      if (!invRef.equals("none"))
      {
        BAN.setMessage("<font color=red>This customer account already has " +
          "invoice " + invRef + " for the current period</font>");
        %>
          <jsp:forward page="conglomBillingMenu.jsp"/>
        <%
      }
      else
      {
        invRef = BAN.checkForTrialConglomInvoice();
        if (!invRef.equals("none"))
        {
          int pipe = invRef.indexOf("|");
          BAN.setMessage("<font color=red>This customer account has a trial " +
            "invoice " + invRef.substring(0, pipe) + " for the period " +
            invRef.substring(++pipe) + "</font>");
          %>
            <jsp:forward page="conglomBillingMenu.jsp"/>
          <%
        }
        else
        {
          ccBAN.Reset();
          ccBAN.setConglomCustomerId(BAN.getConglomCustomerId());
          ccBAN.getCustomer();
          Collection bp = ccBAN.findBilledProducts();
          if (bp.size() == 0)
          {
            BAN.setMessage("<font color=red>This customer has no products " +
              "to bill</font>");
            %>
              <jsp:forward page="conglomBillingMenu.jsp"/>
            <%
          }
          else
          {
            %>
              <jsp:forward page="conglomBillingMenu.jsp">
                <jsp:param name="priorPeriod" value="true"/>
              </jsp:forward>
            <%
          }
        }
      }
    }
    else if (ButtonPressed.equals("Exception Processing"))
    {
      BAN.setExceptionTypeForList("All");
      BAN.setExceptionStatusForList("All");
      BAN.setPeriodForList("All");
      BAN.setInvoiceDocketNoForList("All");
      BAN.setBilledProductForList("All");
      %>
        <jsp:forward page="listConglomExceptions.jsp?firstTime=true&fromBilling=true"/>
      <%
    }
    else //refresh
    {
      BAN.getConglomCustomerSummary();
      %>
	<jsp:forward page="conglomBillingMenu.jsp"/>
      <%
    }
%>
</body>
</html>
