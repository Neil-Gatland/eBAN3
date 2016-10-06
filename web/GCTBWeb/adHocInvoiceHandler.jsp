<html>
<head>
</head>
<%@ page import="JavaUtil.*"%>
<%@ page import="java.util.Collection"%>
<jsp:useBean id="BAN" class="DBUtilities.BANBean" scope="session"/>
<jsp:useBean id="adjBAN" class="DBUtilities.AdjustmentBANBean" scope="session"/>

<%!     String ButtonPressed="",Global_Customer_Id="",Invoice_Region="",invoiceNo="";
        JavaUtil.StringUtil SU = new JavaUtil.StringUtil();
        JavaUtil.UserData UD = new UserData();
%>
<%
    session.setAttribute("PageSent",request.getRequestURI());
    ButtonPressed=(String)request.getParameter("ButtonPressed");
//System.out.println(ButtonPressed);
    if (ButtonPressed.startsWith("Desktop"))
    {
      %>
	<jsp:forward page="newDesktop.jsp"/>
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
        <jsp:forward page="adHocInvoice.jsp">
          <jsp:param name="query" value="true"/>
          <jsp:param name="fromPage" value="adHocInvoice"/>
        </jsp:forward>
      <%
    }
    else if (ButtonPressed.equals("Refresh"))
    {
      BAN.setGlobalCustomerId("");
      BAN.setInvoice_Region("");
      BAN.setInvoiceNo("");
      adjBAN.Reset();
      %>
	<jsp:forward page="adHocInvoice.jsp"/>
      <%
    }
    else if (ButtonPressed.equals("fromInvoice"))
    {
      adjBAN.Reset();
      adjBAN.setCustomer(BAN.getGlobalCustomerId());
      adjBAN.setInvoiceRegion(BAN.getInvoice_Region());
      adjBAN.setInvoiceNumber(BAN.getInvoiceNo());
      adjBAN.setUserId((String)session.getAttribute("User_Id"));
      String accountName = BAN.getAccountName();
      String accountId = accountName.substring(accountName.lastIndexOf("(")+1,
        accountName.lastIndexOf(")"));
      adjBAN.setAccountId(accountId);
      adjBAN.setAction("Add");
      adjBAN.setMode("Create");
      %>
        <jsp:forward page="adjustment.jsp"/>
      <%
    }
    else if (ButtonPressed.startsWith("list_"))
    {
      adjBAN.Reset();
      adjBAN.setCustomer(BAN.getGlobalCustomerId());
      adjBAN.setInvoiceRegion(BAN.getInvoice_Region());
      adjBAN.setInvoiceNumber(BAN.getInvoiceNo());
      String adjustmentId = request.getParameter("adjustmentId");
      String archived = request.getParameter("archived");
      adjBAN.setAdjustmentId(Long.parseLong(adjustmentId));
//System.out.println(adjustmentId);
     if (ButtonPressed.equals("list_AdjustmentView"))
      {
        adjBAN.setAction("View");
        adjBAN.setMode("View");
        adjBAN.getAdjustment(archived.equals("true"));
        %>
          <jsp:forward page="adjustment.jsp"/>
        <%
      }
      else if (ButtonPressed.equals("list_AdjustmentInvoice"))
      {
        BAN.setInvoiceNo(request.getParameter("iRId"));
        BAN.setAccountId(request.getParameter("accountId"));
        BAN.setAccountName(request.getParameter("accountName"));
        BAN.setTotalCharges(request.getParameter("total"));
        BAN.getAccountSummary();
        %>
          <jsp:forward page="listInvoiceReports.jsp?type=a"/>
        <%
      }
      else if (!adjBAN.getAdjustment(archived.equals("true")))
      {
        BAN.setMessage("<font color=blue><b>Adjustment has been archived and cannot be amended</b></font>");
      %>
        <jsp:forward page="adHocInvoice.jsp"/>
      <%
      }
      else if (ButtonPressed.equals("list_AdjustmentAmend"))
      {
        int allowed = BAN.allowBillSubmission2();
        if (allowed < 1)
        {
          if (allowed == 0)
          {
            BAN.setMessage("<font color=red><b>Ad Hoc invoice processing is currently disabled for operational reasons</b></font>");
          }
          %>
            <jsp:forward page="newDesktop.jsp"/>
          <%
        }
        else
        {
          adjBAN.setAction("Amend");
          adjBAN.setMode("Amend");
          adjBAN.setMessage("<font color=blue><b>Amend as required and then select " +
            "'Update' from the 'Options' menu above. Mandatory fields are shown in blue.</b></font>");
          %>
            <jsp:forward page="adjustment.jsp"/>
          <%
        }
      }
      else if (ButtonPressed.equals("list_AdjustmentDelete"))
      {
        if (!BAN.allowBillSubmission())
        {
          BAN.setMessage("<font color=red><b>Ad Hoc invoice processing is currently disabled for operational reasons</b></font>");
          %>
            <jsp:forward page="adHocInvoice.jsp"/>
          <%
        }
        else
        {
          adjBAN.setAction("Delete");
          adjBAN.setMode("Delete");
          adjBAN.setMessage("<font color=blue><b>Select 'Delete' from the 'Options' menu above</b></font>");
          %>
            <jsp:forward page="adjustment.jsp"/>
          <%
        }
      }
    }
    else
    {
      Global_Customer_Id=(String)request.getParameter("GCB_Customer2");
      Invoice_Region=(String)request.getParameter("GCB_Account2");
      invoiceNo=(String)request.getParameter("Ad_Hoc_Invoice_Number");
      if (!Global_Customer_Id.equals(BAN.getGlobalCustomerId()))
      {
	BAN.setGlobalCustomerId(SU.isNull(Global_Customer_Id,""));
	BAN.setInvoice_Region("");
        BAN.setInvoiceNo("");
        adjBAN.Reset();
      }
      else if (!Invoice_Region.equals(BAN.getInvoice_Region()))
      {
        BAN.setInvoice_Region(SU.isNull(request.getParameter("GCB_Account2"),""));
        BAN.setAccountName(SU.isNull(request.getParameter("accountName"),""));
        BAN.setInvoiceNo("");
        adjBAN.Reset();
      }
      else if (!invoiceNo.equals(BAN.getInvoiceNo()))
      {
        BAN.setInvoiceNo(SU.isNull((String)request.getParameter("Ad_Hoc_Invoice_Number"),""));
        adjBAN.Reset();
        adjBAN.setCustomer(Global_Customer_Id);
        adjBAN.setInvoiceRegion(Invoice_Region);
        adjBAN.setInvoiceNumber(invoiceNo);
        adjBAN.setUserId((String)session.getAttribute("User_Id"));
        String accountName = BAN.getAccountName();
        String accountId = accountName.substring(accountName.lastIndexOf("(")+1,
          accountName.lastIndexOf(")"));
        adjBAN.setAccountId(accountId);
      }

      if (ButtonPressed.startsWith("Create"))
      {
        if (!BAN.allowBillSubmission())
        {
          BAN.setMessage("<font color=red><b>Ad Hoc invoice processing is currently disabled for operational reasons</b></font>");
          %>
            <jsp:forward page="adHocInvoice.jsp"/>
          <%
        }
        else
        {
          %>
            <jsp:forward page="adHocInvoice.jsp">
              <jsp:param name="createInvoice" value="true"/>
              <jsp:param name="gcId" value="<%=Global_Customer_Id%>"/>
              <jsp:param name="invoiceRegion" value="<%=Invoice_Region%>"/>
            </jsp:forward>
          <%
        }
      }
      else if (ButtonPressed.startsWith("Add"))
      {
        if (!BAN.allowBillSubmission())
        {
          BAN.setMessage("<font color=red><b>Ad Hoc invoice processing is currently disabled for operational reasons</b></font>");
          %>
            <jsp:forward page="adHocInvoice.jsp"/>
          <%
        }
        else
        {
          /*adjBAN.Reset();
          adjBAN.setCustomer(BAN.getGlobalCustomerId());
          adjBAN.setInvoiceRegion(BAN.getInvoice_Region());
          adjBAN.setInvoiceNumber(BAN.getInvoiceNo());
          adjBAN.setAction("Add");
          adjBAN.setMode("Create");*/
          adjBAN.Reset();
          adjBAN.setCustomer(BAN.getGlobalCustomerId());
          adjBAN.setInvoiceRegion(BAN.getInvoice_Region());
          adjBAN.setInvoiceNumber(BAN.getInvoiceNo());
          adjBAN.setUserId((String)session.getAttribute("User_Id"));
          String accountName = BAN.getAccountName();
          String accountId = accountName.substring(accountName.lastIndexOf("(")+1,
            accountName.lastIndexOf(")"));
          adjBAN.setAccountId(accountId);
          adjBAN.setAction("Add");
          adjBAN.setMode("Create");
          %>
            <jsp:forward page="adjustment.jsp"/>
          <%
        }
      }
      else if (ButtonPressed.startsWith("Close"))
      {
        if (!BAN.allowBillSubmission())
        {
          BAN.setMessage("<font color=red><b>Ad Hoc invoice processing is currently disabled for operational reasons</b></font>");
          %>
            <jsp:forward page="adHocInvoice.jsp"/>
          <%
        }
        else
        {
          BAN.closeAdHocInvoice(invoiceNo);
          %>
            <jsp:forward page="adHocInvoice.jsp"/>
          <%
        }
      }
      else
      {//No button pressed, so must be a Refresh
        %>
          <jsp:forward page="adHocInvoice.jsp"/>
        <%
      }
    }
      %>
</body>
</html>
