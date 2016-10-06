<html>
<head>
</head>
<%@ page import="JavaUtil.*"%>
<%@ page import="java.util.Collection"%>
<jsp:useBean id="BAN" class="DBUtilities.BANBean" scope="session"/>

<%!     String ButtonPressed="",Global_Customer_Id="",accountId="",invoiceNo="";
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
        <jsp:forward page="invoiceReversal.jsp">
          <jsp:param name="query" value="true"/>
          <jsp:param name="fromPage" value="invoiceReversal"/>
        </jsp:forward>
      <%
    }
    else if (ButtonPressed.equals("Refresh"))
    {
      BAN.setGlobalCustomerId("");
      BAN.setAccountId("");
      BAN.setClosedInvoiceGrid("");
      %>
	<jsp:forward page="invoiceReversal.jsp"/>
      <%
    }
    else if (ButtonPressed.equals("list_Reverse"))
    {
      if (BAN.checkTrialBills())
      {
        BAN.setMessage("<font color=red><b>There are trial bills open for this " +
          "customer. Please close them before proceeding.</b></font>");
      }
      else if (BAN.checkInvoiceTax(request.getParameter("invoiceNo")))
      {
        BAN.setMessage("<font color=red><b>This invoice has US tax and can only " +
          "be reversed by Systems Support. Please raise a query.</b></font>");
      }
      else
      {
        BAN.reverseInvoice(request.getParameter("invoiceNo"));
      }
      %>
        <jsp:forward page="invoiceReversal.jsp"/>
      <%
    }
    else if (ButtonPressed.equals("viewInvoice"))
    {
      BAN.setInvoiceNoAcc(request.getParameter("iRId"));
      String customerRadius = BAN.getCustomerRadius();
      if (customerRadius.equals("Managed Services"))
      {
        Collection data = BAN.getMSInvoiceDetails();
        if (data.isEmpty())
        {
          BAN.setMessage("<font color=blue><b>No trial invoice data found for " +
          "invoice number " + BAN.getInvoiceNo() + "</b></font>");
          %>
            <jsp:forward page="invoiceReversal.jsp"/>
          <%
        }
        else
        {
          %>
            <jsp:forward page="invoiceReversal.jsp">
              <jsp:param name="msInvoice" value="true"/>
            </jsp:forward>
          <%
        }
      }
      else
      {
        if (!BAN.checkInvoiceExists())
        {
          BAN.setMessage("<font color=blue><b>No trial invoice data found for " +
          "invoice number " + BAN.getInvoiceNo() + "</b></font>");
          %>
            <jsp:forward page="invoiceReversal.jsp"/>
          <%
        }
        else
        {
          if (customerRadius.equals("GM Billing"))
          {
            %>
              <jsp:forward page="invoiceReversal.jsp">
                <jsp:param name="standardInvoice" value="true"/>
              </jsp:forward>
            <%
          }
          else
          {
            %>
              <jsp:forward page="invoiceReversal.jsp">
                <jsp:param name="strategicInvoice" value="true"/>
              </jsp:forward>
            <%
          }
        }
      }
    }
    else
    {
      Global_Customer_Id=(String)request.getParameter("GCB_Customer2");
      accountId=(String)request.getParameter("GCB_Account");
      if (!Global_Customer_Id.equals(BAN.getGlobalCustomerId()))
      {
        BAN.setGlobalCustomerId(SU.isNull(Global_Customer_Id,""));
        BAN.setAccountId("");
        BAN.setClosedInvoiceGrid("");
      }
      else if (!accountId.equals(BAN.getAccountId()))
      {
        BAN.setAccountId(SU.isNull(accountId,""));
        BAN.setClosedInvoiceGrid("");
      }
      %>
        <jsp:forward page="invoiceReversal.jsp"/>
      <%
    }
      %>
</body>
</html>
