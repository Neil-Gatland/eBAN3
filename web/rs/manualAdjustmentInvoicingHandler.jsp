<html>
<head>
</head>
<body>
<%@ page import="JavaUtil.StringUtil"%>
<%@ page import="java.util.Enumeration"%>

<jsp:useBean id="BAN" class="DBUtilities.BANBean" scope="session"/>
<jsp:useBean id="HB" class="HTMLUtil.HTMLBean" scope="session"/>
<jsp:useBean id="RSB" class="DBUtilities.RevenueShareBean" scope="session"/>
<jsp:useBean id="ADJ" class="DBUtilities.AdjustmentBean" scope="session"/>
<%! String ButtonPressed, banId, listType;%>
<%
    Enumeration FormFields = request.getParameterNames();
    String FormField = null;
    session.setAttribute("formname","manualAdjustmentInvoicingHandler");
    ButtonPressed = request.getParameter("ButtonPressed");
    if (ButtonPressed.equals("Log Off"))
    {
      %>
	<jsp:forward page="Login.jsp?type=l"/>
      <%
    }
    else if (ButtonPressed.equals("Raise Query"))
    {
      %>
        <jsp:forward page="manualAdjustmentInvoicing.jsp">
          <jsp:param name="query" value="true"/>
          <jsp:param name="fromPage" value="manualAdjustmentInvoicing"/>
        </jsp:forward>
      <%
    }
    else if (ButtonPressed.equals("View All Processes"))
    {
      %>
        <jsp:forward page="manualAdjustmentInvoicing.jsp">
         <jsp:param name="viewProcess" value="true"/>
        </jsp:forward>
      <%
    }
    else if (ButtonPressed.equals("Revenue Share Desktop"))
    {
      %>
        <jsp:forward page="rsDesktop.jsp"/>
      <%
    }
    else if ((ButtonPressed.equals("Refresh")) ||
      (ButtonPressed.equals("Cancel")))
    {
      RSB.setMode("");
      RSB.setInvoiceNumber("");
      RSB.setAdjustmentStatus("");
      RSB.populateAdjustmentInvoiceList();
      %>
	<jsp:forward page="manualAdjustmentInvoicing.jsp"/>
      <%
    }
    else if (ButtonPressed.equals("Confirm"))
    {
      RSB.updateInvoiceAdjustmentStatus();
      String msg = RSB.getMessage();
      RSB.setInvoiceNumber("");
      RSB.setAdjustmentStatus("");
      RSB.setMode("");
      RSB.populateAdjustmentInvoiceList();
      RSB.setMessage(msg);
      %>
        <jsp:forward page="manualAdjustmentInvoicing.jsp"/>
      <%
    }
    else if (ButtonPressed.equals("Add New Adjustment Invoice"))
    {
      ADJ.Reset();
      ADJ.setUserId((String)session.getAttribute("User_Id"));
      ADJ.setLatestCallMonth(RSB.getLatestCallMonth());
      ADJ.setAction("Add");
      ADJ.setMode("Create");
      %>
      <jsp:forward page="adjustmentMaint.jsp"/>
      <%
    }
    else if (ButtonPressed.equals("list_Update"))
    {
      RSB.setInvoiceNumber(request.getParameter("invoiceNo"));
      ADJ.setUserId((String)session.getAttribute("User_Id"));
      ADJ.Reset();
      ADJ.setLatestCallMonth(RSB.getLatestCallMonth());
      ADJ.setInvoiceNumber(request.getParameter("invoiceNo"));
      ADJ.getAdjustmentInvoiceFromDB();
      ADJ.populateAdjustmentLineList();
      ADJ.setAction("Amend");
      ADJ.setMode("Amend");
      %>
      <jsp:forward page="adjustmentMaint.jsp"/>
      <%
    }
    else if (ButtonPressed.equals("list_Complete"))
    {
      String invNo = request.getParameter("invoiceNo");
      if (RSB.canCompleteInvoice(invNo))
      {
        RSB.setInvoiceNumber(invNo);
        RSB.setAdjustmentStatus("Completed");
        RSB.setMode("Confirm");
        RSB.setMessage("<font color=blue><b>Select 'Confirm' from the " +
          "'Options' menu above to set this Adjustment Invoice to 'Complete', or 'Cancel' to abort the operation</b></font>");
      }
      else
      {
        RSB.setMessage("<font color=red><b>This Adjustment Invoice cannot be set to 'Complete'.  Set-up and reference data is incomplete.</b></font>");
      }
      %>
      <jsp:forward page="manualAdjustmentInvoicing.jsp"/>
      <%
    }
    else if (ButtonPressed.equals("list_NotReqd"))
    {
      RSB.setInvoiceNumber(request.getParameter("invoiceNo"));
      RSB.setAdjustmentStatus("Not Required");
      RSB.setMode("Confirm");
      RSB.setMessage("<font color=blue><b>Select 'Confirm' from the " +
        "'Options' menu above to set this Adjustment Invoice to 'Not Required', or 'Cancel' to abort the operation</b></font>");
      %>
      <jsp:forward page="manualAdjustmentInvoicing.jsp"/>
      <%
    }
    else
    {//No button pressed, so must be a Refresh
      RSB.setMode("");
      RSB.setInvoiceNumber("");
      RSB.setAdjustmentStatus("");
      RSB.populateAdjustmentInvoiceList();
      %>
        <jsp:forward page="manualAdjustmentInvoicing.jsp"/>
      <%
    }
%>
</body>
</html>
