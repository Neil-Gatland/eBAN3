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
    session.setAttribute("formname","adjustmentMaintHandler");
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
        <jsp:forward page="adjustmentMaint.jsp">
          <jsp:param name="query" value="true"/>
          <jsp:param name="fromPage" value="adjustmentMaint"/>
        </jsp:forward>
      <%
    }
    else if (ButtonPressed.equals("View All Processes"))
    {
      RSB.populateProcessList();
      %>
        <jsp:forward page="adjustmentMaint.jsp">
         <jsp:param name="viewProcess" value="true"/>
        </jsp:forward>
      <%
    }
    else if (ButtonPressed.equals("Revenue Share Desktop"))
    {
      RSB.canCloseMonth();
      %>
        <jsp:forward page="rsDesktop.jsp"/>
      <%
    }
    else if (ButtonPressed.equals("Manual Adjustment Invoicing"))
    {
      RSB.setAdjustmentStatus("");
      RSB.setInvoiceNumber("");
      RSB.setMode("");
      RSB.populateAdjustmentInvoiceList();
      %>
        <jsp:forward page="manualAdjustmentInvoicing.jsp"/>
      <%
    }
    else if (ButtonPressed.equals("Add Adjustment Line"))
    {
      ADJ.setAdjustmentLineAmount("");
      ADJ.setAdjustmentLineDescription("");
      ADJ.setLineAction("Add");
      ADJ.setLineMode("Create");
      %>
        <jsp:forward page="adjustmentMaint.jsp">
          <jsp:param name="adjustmentLineMaint" value="true"/>
        </jsp:forward>
      <%
    }
    else if (ButtonPressed.equals("list_AdjLineUpdate"))
    {
      ADJ.setLineAction("Amend");
      ADJ.setLineMode("Amend");
      ADJ.setAdjustmentLineNo(request.getParameter("lineNo"));
      ADJ.getAdjustmentInvoiceLine();
      %>
        <jsp:forward page="adjustmentMaint.jsp">
          <jsp:param name="adjustmentLineMaint" value="true"/>
        </jsp:forward>
      <%
    }
    else if (ButtonPressed.equals("lineMaint"))
    {
      %>
        <jsp:forward page="adjustmentMaint.jsp"/>
      <%
    }
    else if (ButtonPressed.equals("list_AdjLineDelete"))
    {
      if (ADJ.canDeleteInvoiceLine())
      {
        ADJ.setAction("Delete");
        ADJ.setMode("Confirm");
        ADJ.setAdjustmentLineNo(request.getParameter("lineNo"));
        ADJ.setMessage("<font color=blue><b>Select 'Confirm' from the " +
          "'Options' menu above to delete this Adjustment Line or 'Cancel' to abort the operation</b></font>");
      }
      %>
        <jsp:forward page="adjustmentMaint.jsp"/>
      <%
    }
    else if (ButtonPressed.equals("Refresh"))
    {
      String action = ADJ.getAction();
      String invNo = ADJ.getInvoiceNumber();
      ADJ.setAction(action);
      if (action.equals("Add"))
      {
        ADJ.Reset();
        ADJ.setMode("Create");
        ADJ.setMessage("<font color=blue><b>Mandatory fields are shown in blue</b></font>");
      }
      else if (action.equals("Amend"))
      {
        ADJ.populateAdjustmentLineList();

        //ADJ.setInvoiceNumber(invNo);
        //ADJ.setMode("Amend");
        //ADJ.setMessage("<font color=blue><b>Amend as required and then select " +
          //"'Update' from the 'Options' menu above. Mandatory fields are shown in blue.</b></font>");
      }
      %>
	<jsp:forward page="adjustmentMaint.jsp"/>
      <%
    }
    else if (ButtonPressed.equals("Confirm"))
    {
      String action = ADJ.getAction();
      if (action.equals("Add"))
      {
        if (ADJ.createAdjustmentInvoice())
        {
          RSB.setAdjustmentStatus("");
          RSB.setInvoiceNumber("");
          RSB.setMode("");
          RSB.populateAdjustmentInvoiceList();
          %>
            <jsp:forward page="manualAdjustmentInvoicing.jsp"/>
          <%
        }
        else
        {
          %>
            <jsp:forward page="adjustmentMaint.jsp"/>
          <%
        }
      }
      else if (action.equals("Delete"))
      {
        if (ADJ.deleteAdjustmentInvoiceLine())
        {
          ADJ.populateAdjustmentLineList();
          ADJ.updateAdjustmentInvoiceAmounts();
          ADJ.setMessage("<font color=blue><b>Adjustment Line deleted</b></font>");
        }
        ADJ.setAction("Amend");
        ADJ.setMode("Amend");
        %>
          <jsp:forward page="adjustmentMaint.jsp"/>
        <%
      }
    }
    else if (ButtonPressed.equals("Cancel"))
    {
      %>
	<jsp:forward page="adjustmentMaint.jsp"/>
      <%
    }
    else
    {
      //store form values in Bean
      while (FormFields.hasMoreElements())
      {
        FormField=(String)FormFields.nextElement();
        ADJ.setParameter(FormField,request.getParameter(FormField));
      }
      if (ButtonPressed.equals("Submit"))
      {
        if (ADJ.isValid(ButtonPressed))
        {
          ADJ.setMode("Confirm");
          ADJ.setMessage("<font color=blue><b>Select 'Confirm' from the " +
            "'Options' menu above to create this Adjustment Invoice or 'Cancel' to amend it</b></font>");
        }
        %>
        <jsp:forward page="adjustmentMaint.jsp"/>
        <%
      }
      else
      {//No button pressed, so must be a Refresh
        RSB.getLatestProcessControl();
        %>
        <jsp:forward page="adjustmentMaint.jsp"/>
        <%
      }
    }
%>
</body>
</html>
