<html>
<head>
</head>
<%@ page import="JavaUtil.*"%>
<%@ page import="java.util.Enumeration"%>
<jsp:useBean id="BAN" class="DBUtilities.BANBean" scope="session"/>
<jsp:useBean id="ccBAN" class="DBUtilities.ConglomCustomerBANBean" scope="session"/>
<jsp:useBean id="cbpBAN" class="DBUtilities.ConglomBilledProductBANBean" scope="session"/>
<jsp:useBean id="crcBAN" class="DBUtilities.ConglomReportConfigBANBean" scope="session"/>

<%  String ButtonPressed=request.getParameter("ButtonPressed");
    JavaUtil.StringUtil SU = new JavaUtil.StringUtil();
		Enumeration FormFields = request.getParameterNames();
		String FormField = null;
//System.out.println(ButtonPressed);
    ccBAN.setMessage("");
    BAN.setMessage("");
    session.setAttribute("PageSent",request.getRequestURI());
    if (ButtonPressed.startsWith("Conglom Desktop"))
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
    else if (ButtonPressed.equals("Log Off"))
    {
      %>
	<jsp:forward page="Login.jsp?type=l"/>
      <%
    }
    else if (ButtonPressed.equals("Raise Query"))
    {
      %>
        <jsp:forward page="conglomCustMaint.jsp">
         <jsp:param name="query" value="true"/>
          <jsp:param name="fromPage" value="conglomCustMaint"/>
        </jsp:forward>
      <%
    }
    else if (ButtonPressed.equals("Refresh"))
    {
      //ccBAN.Reset();
      %>
	<jsp:forward page="conglomCustMaint.jsp"/>
      <%
    }
    else if (ButtonPressed.equals("Delete"))
    {
      ccBAN.setMode("Confirm");
      ccBAN.setMessage("<font color=blue><b>Select 'Confirm' from the " +
              "'Options' menu above to delete this customer or 'Cancel' to abort the operation</b></font>");
      %>
	<jsp:forward page="conglomCustMaint.jsp"/>
      <%
    }
    else if (ButtonPressed.equals("Confirm"))
    {
      String action = ccBAN.getAction();
      if (action.equals("Add"))
      {
        ccBAN.createCustomer();
      }
      else if (action.equals("Delete"))
      {
        //ccBAN.deleteCustomer();
      }
      else if (action.equals("Amend"))
      {
        ccBAN.updateCustomer();
      }
      String message = ccBAN.getMessage();
      BAN.setMessage(ccBAN.getMessage());
      if (message.startsWith("<font color=blue><b>Customer created"))
      {
        BAN.setConglomCustomerId(ccBAN.getConglomCustomerId());
        BAN.getConglomCustomerSummary();
        ccBAN.getCustomer();
        ccBAN.setAction("Amend");
        ccBAN.setMode("Amend");
        %>
          <jsp:forward page="conglomCustMaint.jsp"/>
        <%
      }
      else
      {
        if (action.equals("Add"))
        {
          %>
            <jsp:forward page="conglomDesktop.jsp"/>
          <%
        }
        else
        {
          %>
            <jsp:forward page="conglomBillingMenu.jsp"/>
          <%
        }
      }
    }
    else if (ButtonPressed.equals("Cancel"))
    {
      String action = ccBAN.getAction();
      if (action.equals("Add"))
      {
        ccBAN.setMode("Create");
        ccBAN.setMessage("<font color=blue><b>Mandatory fields are shown in blue</b></font>");
      }
      else if (action.equals("Delete"))
      {
        ccBAN.setMode("Delete");
        ccBAN.setMessage("<font color=blue><b>Select 'Delete' from the 'Options' menu above</b></font>");
      }
      else if (action.equals("Amend"))
      {
        ccBAN.setMode("Amend");
        ccBAN.setMessage("<font color=blue><b>Amend as required and then select " +
          "'Update' from the 'Options' menu above. Mandatory fields are shown in blue.</b></font>");
      }
      %>
	<jsp:forward page="conglomCustMaint.jsp"/>
      <%
    }
    else
    {
      //store form values in Bean
      while (FormFields.hasMoreElements())
      {
        FormField=(String)FormFields.nextElement();
        ccBAN.setParameter(FormField,request.getParameter(FormField));
      }
      if (ButtonPressed.startsWith("list_"))
      {
        if (ButtonPressed.equals("list_Update"))
        {
          cbpBAN.Reset();
          cbpBAN.setUserId(BAN.getActAsLogon());
          cbpBAN.setConglomCustomerId(BAN.getConglomCustomerId());
          cbpBAN.setConglomCustProductId(Long.parseLong(request.getParameter("billedProductId")));
          cbpBAN.setBilledProductExtractFreq(request.getParameter("billedProductFreq"));
          cbpBAN.setBilledProductFeedSource(request.getParameter("billedProductSource"));
          cbpBAN.getBilledProduct();
          if (cbpBAN.checkBilledProductClosed())
          {
            ccBAN.setMessage("<font color=blue><b>This billed product has been ceased " +
              "and cannot be amended</b></font>");
            %>
              <jsp:forward page="conglomCustMaint.jsp"/>
            <%
          }
          cbpBAN.setAction("Amend");
          cbpBAN.setMode("Amend");
          cbpBAN.setMessage("<font color=blue><b>Amend as required and then select " +
            "'Update' from the 'Options' menu above. Mandatory fields are shown in blue.</b></font>");
          %>
          <jsp:forward page="conglomBilledProductMaint.jsp"/>
          <%
        }
        else if (ButtonPressed.equals("list_LD"))
        {
          crcBAN.Reset();
          crcBAN.setUserId(BAN.getActAsLogon());
          crcBAN.setConglomCustomerId(BAN.getConglomCustomerId());
          crcBAN.setConglomCustProductId(Long.parseLong(request.getParameter("billedProductId")));
          crcBAN.setBilledProductExtractFreq(request.getParameter("billedProductFreq"));
          crcBAN.setBilledProductFeedSource(request.getParameter("billedProductSource"));
          crcBAN.getBilledProductIdFromDB();
          crcBAN.setAction("Amend");
          crcBAN.setMode("Amend");
          crcBAN.setMessage("<font color=blue><b>Amend as required and then select " +
            "'Update' or 'Update & Reload' from the 'Options' menu above. Mandatory fields are shown in blue.</b></font>");
          %>
          <jsp:forward page="conglomReportConfigMaint.jsp"/>
          <%
        }
      }
      else if (ButtonPressed.equals("Add Product"))
      {
        cbpBAN.Reset();
        cbpBAN.setUserId(BAN.getActAsLogon());
        cbpBAN.setConglomCustomerId(BAN.getConglomCustomerId());
        cbpBAN.setAction("Add");
        cbpBAN.setMode("Add");
        %>
        <jsp:forward page="conglomBilledProductMaint.jsp"/>
        <%
      }
      else if ((ButtonPressed.equals("Submit")) || (ButtonPressed.equals("Update")))
      {
        if (ccBAN.isValid(ButtonPressed))
        {
          ccBAN.setMode("Confirm");
          if (ButtonPressed.equals("Update"))
          {
            ccBAN.setMessage("<font color=blue><b>Select 'Confirm' from the " +
              "'Options' menu above to amend this customer or 'Cancel' to abort the operation</b></font>");
          }
          else
          {
            ccBAN.setMessage("<font color=blue><b>Select 'Confirm' from the " +
              "'Options' menu above to create this customer or 'Cancel' to amend it</b></font>");
          }
        }
        %>
        <jsp:forward page="conglomCustMaint.jsp"/>
        <%
      }
      else
      {//No button pressed, so must be a Refresh
        %>
        <jsp:forward page="conglomCustMaint.jsp"/>
        <%
      }
    }
      %>
</body>
</html>
