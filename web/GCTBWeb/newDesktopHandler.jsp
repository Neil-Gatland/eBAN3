<html>
<head>
</head>
<body>
<%@ page import="JavaUtil.StringUtil"%>

<jsp:useBean id="BAN" class="DBUtilities.BANBean" scope="session"/>
<jsp:useBean id="HB" class="HTMLUtil.HTMLBean" scope="session"/>
<jsp:useBean id="adjBAN" class="DBUtilities.AdjustmentBANBean" scope="session"/>
<%! String ButtonPressed, banId, listType;%>
<%
    session.setAttribute("formname","newDesktopHandler");
    ButtonPressed = request.getParameter("ButtonPressed");
    if (request.getParameter("Act_As_Logon") != null)
    {
      if (request.getParameter("Act_As_Logon").equals("All"))
      {
        BAN.setMessage("<font color=#003399>This option is no longer available. Please use 'Analyst Search' from the 'Admin' menu.</font>");
        %>
          <jsp:forward page="newDesktop.jsp"/>
        <%
      }
      BAN.setActAsLogon(request.getParameter("Act_As_Logon"));
      BAN.setDesktopSortOrder(request.getParameter("sortOrder"));
    }
    BAN.setRefreshCustomerGrid(false);
    if (ButtonPressed.equals("Log Off"))
    {
      %>
	<jsp:forward page="Login.jsp?type=l"/>
      <%
    }
    else if (ButtonPressed.equals("Raise Query"))
    {
      %>
        <jsp:forward page="newDesktop.jsp">
          <jsp:param name="query" value="true"/>
          <jsp:param name="fromPage" value="newDesktop"/>
        </jsp:forward>
      <%
    }
    else if (ButtonPressed.equals("Change Password"))
    {
      %>
        <jsp:forward page="newDesktop.jsp">
         <jsp:param name="changePass" value="true"/>
        </jsp:forward>
      <%
    }
    else if (ButtonPressed.equals("Geocode Search"))
    {
      %>
        <jsp:forward page="newDesktop.jsp">
          <jsp:param name="geocode" value="true"/>
        </jsp:forward>
      <%
    }
    else if (ButtonPressed.equals("Invoice Search"))
    {
      %>
        <jsp:forward page="newDesktop.jsp">
          <jsp:param name="invoiceSearch" value="true"/>
        </jsp:forward>
      <%
    }
    else if (ButtonPressed.equals("financeInvoice"))
    {
    BAN.getAccountSummary();
    %>
        <jsp:forward page="listInvoiceReports.jsp?type=f"/>
    <%
    }
    else if (ButtonPressed.equals("GCB Billing Downloads"))
    {
      %>
        <jsp:forward page="newDesktop.jsp">
          <jsp:param name="gcbDownloads" value="true"/>
        </jsp:forward>
      <%
    }
    else if (ButtonPressed.equals("Analyst Search"))
    {
      %>
        <jsp:forward page="newDesktop.jsp">
          <jsp:param name="analyst" value="true"/>
        </jsp:forward>
      <%
    }
    else if (ButtonPressed.equals("One-Off Invoice"))
    {
      int allowed = BAN.allowBillSubmission2();
      if (allowed < 1)
      {
        if (allowed == 0)
        {
          BAN.setMessage("<font color=red><b>One-off invoice processing is currently disabled for operational reasons</b></font>");
        }
        %>
          <jsp:forward page="newDesktop.jsp"/>
        <%
      }
      else
      {
        %>
          <jsp:forward page="newDesktop.jsp">
          <jsp:param name="createOneOffInvoice" value="true"/>
          </jsp:forward>
        <%
      }
    }
    else if (ButtonPressed.equals("Customer Data Search"))
    {
      %>
        <jsp:forward page="newDesktop.jsp">
          <jsp:param name="dataSearch" value="true"/>
        </jsp:forward>
      <%
    }
    else if (ButtonPressed.equals("Check Queue"))
    {
      String grid = BAN.getGIVNJobQueue(true);
      if (!grid.startsWith("<table"))
      {
      %>
        <jsp:forward page="newDesktop.jsp"/>
      <%
      }
      else
      {
      %>
        <jsp:forward page="newDesktop.jsp">
         <jsp:param name="queue" value="true"/>
        </jsp:forward>
      <%
      }
    }
    else if (ButtonPressed.startsWith("list_"))
    {
      String gcId = request.getParameter("gcId");
      BAN.setGlobalCustomerId(gcId);
      if (ButtonPressed.equals("list_A"))
      {
	BAN.getAccountSummary();
        %>
          <jsp:forward page="listAccounts.jsp?accountType=i"/>
        <%
      }
      else if (ButtonPressed.equals("list_D"))
      {
        %>
          <jsp:forward page="ossebanLink.jsp"/>
        <%
      }
      else if (ButtonPressed.equals("list_E"))
      {
        BAN.getBillPeriodStartDate();
        BAN.setExceptionTypeForList("All");
        BAN.setExceptionStatusForList("All");
        BAN.setIRINForList("All");
        BAN.setGSR_for_List("All");
        %>
          <jsp:forward page="ListExceptions.jsp">
            <jsp:param name="firstTime" value="true"/>
          </jsp:forward>
        <%
      }
      else if (ButtonPressed.equals("list_S"))
      {
        int allowed = BAN.allowBillSubmission2();
        if (allowed < 1)
        {
          if (allowed == 0)
          {
            BAN.setMessage("<font color=red><b>One-off invoice processing is currently disabled for operational reasons</b></font>");
          }
          %>
            <jsp:forward page="newDesktop.jsp"/>
          <%
        }
        else
        {
          if (BAN.checkBPRCQueue(false))
          {
            BAN.setMessage("<font color=red>Bill submission request for " +
              BAN.getGlobalCustomerId() + " is already in the queue</font>");
            %>
              <jsp:forward page="newDesktop.jsp"/>
            <%
          }
          /*else if (BAN.checkBPRCQueue(true))
          {
            BAN.setMessage("<font color=red>Bill submission request for " +
              BAN.getGlobalCustomerId() + " is already in the overnight queue</font>");

              <jsp:forward page="newDesktop.jsp"/>

          }*/
          else
          {
            BAN.getAccountSummary();
            BAN.setRunType("trial");
            BAN.setRunTime("now");
            BAN.setBackdates("C");
            BAN.setScheduleCrystal(false);
            BAN.setJobQueue("");
            BAN.setRefreshCustomerGrid(true);
            %>
            <jsp:forward page="billSubmission.jsp"/>
            <%
          }
        }
      }
      else if (ButtonPressed.equals("list_V"))
      {
        String grid = BAN.getLogMessages(false);
        if (!grid.startsWith("<table"))
        {
        %>
          <jsp:forward page="newDesktop.jsp"/>
        <%
        }
        else
        {
        %>
          <jsp:forward page="newDesktop.jsp">
           <jsp:param name="mblog" value="true"/>
          </jsp:forward>
        <%
        }
      }
      else if (ButtonPressed.equals("list_R"))
      {
        BAN.reOpenBill();
        BAN.setRefreshCustomerGrid(true);
        %>
          <jsp:forward page="newDesktop.jsp"/>
        <%
      }
      else if (ButtonPressed.equals("list_G"))
      {
        if (BAN.checkCDSinglesQueue())
        {
          BAN.setMessage("<font color=red>Request for GCD extract for " +
            BAN.getGlobalCustomerId() + " already in the queue</font>");
          %>
            <jsp:forward page="newDesktop.jsp"/>
          <%
        }
        else
        {
          %>
          <jsp:forward page="newDesktop.jsp">
           <jsp:param name="extractConfirm" value="true"/>
          </jsp:forward>
          <%
        }
      }
    }
    else if (ButtonPressed.equals("DOB"))
    {
      BAN.setMessage("not yet available");
      %>
	<jsp:forward page="newDesktop.jsp"/>
      <%
    }
    else if (ButtonPressed.equals("Account Administration"))
    {
      session.setAttribute("System","GCB");
      //HB.setFromEdify(false);
      //HB.setGroupName("");
      session.setAttribute("billingTeam", "Data");
      session.setAttribute("Act_As_User",request.getParameter("Act_As_Logon"));
      BAN.setGlobalCustomerId("");
      //BAN.setMessage("not yet available");
			response.sendRedirect("../GCBeBan/CustomerBANMenu.jsp");
	//<jsp:forward page="../GCBeBan/CustomerBANMenu.jsp"/>

      %>
      <%
    }
    else if (ButtonPressed.equals("Account Discount"))
    {
      BAN.setGlobalCustomerId("");
      BAN.setAccount_Id("");
      BAN.setDiscountPlan("");
      %>
	<jsp:forward page="accountDiscount.jsp"/>
      <%
    }
    else if (ButtonPressed.equals("Ad Hoc Invoice"))
    {
      BAN.setGlobalCustomerId("");
      BAN.setInvoice_Region("");
      BAN.setInvoiceNo("");
      adjBAN.Reset();
      %>
	<jsp:forward page="adHocInvoice.jsp"/>
      <%
    }
    else if (ButtonPressed.equals("Invoice Reversal"))
    {
      BAN.setGlobalCustomerId("");
      BAN.setAccountId("");
      BAN.setClosedInvoiceGrid("");
      BAN.setRefreshCustomerGrid(true);
      %>
	<jsp:forward page="invoiceReversal.jsp"/>
      <%
    }
    else if (ButtonPressed.equals("One-off Invoice"))
    {
      BAN.setGlobalCustomerId("");
      BAN.setInvoice_Region("");
      BAN.setInvoiceNo("");
      %>
	<jsp:forward page="oneOffInvoice.jsp"/>
      <%
    }
    else if (ButtonPressed.equals("subExtract"))
    {
      int qp = BAN.insertCDSinglesQueue();
      if (qp == 0)
      {
        // error: display message set by db call
      }
      else if (qp == -1)
      {
        BAN.setMessage("<font color=red>Request for GCD extract for " +
          BAN.getGlobalCustomerId() + " already in the queue</font>");
      }
      else
      {
        BAN.setMessage("<font color=blue>GCD extract submitted - position in queue:" + qp + "</font>");
      }

      %>
	<jsp:forward page="newDesktop.jsp"/>
      <%
    }
    else //refresh
    {
      BAN.setRefreshCustomerGrid(true);
      %>
	<jsp:forward page="newDesktop.jsp"/>
      <%
    }
%>
</body>
</html>
