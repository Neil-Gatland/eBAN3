<html>
<head>
</head>
<body>
<%@ page import="JavaUtil.StringUtil"%>
<%@ page import="java.util.Enumeration"%>

<jsp:useBean id="BAN" class="DBUtilities.BANBean" scope="session"/>
<jsp:useBean id="HB" class="HTMLUtil.HTMLBean" scope="session"/>
<jsp:useBean id="MAC" class="DBUtilities.MasterAccountBean" scope="session"/>
<jsp:useBean id="ACC" class="DBUtilities.AccountBean" scope="session"/>
<jsp:useBean id="PRO" class="DBUtilities.ProviderBean" scope="session"/>
<jsp:useBean id="RSB" class="DBUtilities.RevenueShareBean" scope="session"/>
<%! String ButtonPressed, banId, listType;%>
<%
    Enumeration FormFields = request.getParameterNames();
    String FormField = null;
    session.setAttribute("formname","accountMaintHandler");
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
        <jsp:forward page="accountMaint.jsp">
          <jsp:param name="query" value="true"/>
          <jsp:param name="fromPage" value="accountMaint"/>
        </jsp:forward>
      <%
    }
    else if (ButtonPressed.equals("View All Processes"))
    {
      RSB.populateProcessList();
      %>
        <jsp:forward page="accountMaint.jsp">
         <jsp:param name="viewProcess" value="true"/>
        </jsp:forward>
      <%
    }
    else if (ButtonPressed.equals("Missing Data Identification"))
    {
      %>
        <jsp:forward page="missingDataIdentification.jsp"/>
      <%
    }
    else if (ButtonPressed.equals("Add Product"))
    {
      %>
        <jsp:forward page="accountMaint.jsp">
          <jsp:param name="addProduct" value="true"/>
          <jsp:param name="fromPage" value="accountMaint"/>
        </jsp:forward>
      <%
    }
    else if (ButtonPressed.equals("Move Account"))
    {
      if (RSB.canMoveAccount())
      {
        %>
          <jsp:forward page="accountMaint.jsp">
            <jsp:param name="moveAccount" value="true"/>
          </jsp:forward>
        <%
      }
      else
      {
        ACC.setMessage("<font color=blue><b>This account is the master account and cannot be moved</b></font>");
        %>
          <jsp:forward page="accountMaint.jsp"/>
        <%
      }
    }
    else if (ButtonPressed.equals("View Account Move History"))
    {
      if (RSB.canMoveAccount())
      {
        %>
          <jsp:forward page="accountMaint.jsp">
            <jsp:param name="accountMoveHist" value="true"/>
          </jsp:forward>
        <%
      }
      else
      {
        ACC.setMessage("<font color=blue><b>This account is the master account and cannot be moved</b></font>");
        %>
          <jsp:forward page="accountMaint.jsp"/>
        <%
      }
    }
    else if (ButtonPressed.equals("Revenue Share Desktop"))
    {
      RSB.canCloseMonth();
      %>
        <jsp:forward page="rsDesktop.jsp"/>
      <%
    }
    else if (ButtonPressed.equals("Reference Data Menu"))
    {
      %>
        <jsp:forward page="referenceDataMenu.jsp"/>
      <%
    }
    else if (ButtonPressed.equals("list_ProductDelete"))
    {
      ACC.setProductCode(request.getParameter("productId"));
      ACC.getProductFromDB();
      ACC.deleteAccountProduct();
      ACC.populateAccountProductList();
      %>
        <jsp:forward page="accountMaint.jsp"/>
      <%
    }
    else if (ButtonPressed.equals("Refresh"))
    {
      String action = ACC.getAction();
      if (action.equals("Add"))
      {
        ACC.Reset();
        ACC.setAction(action);
        ACC.setMode("Create");
        ACC.setMessage("<font color=blue><b>Mandatory fields are shown in blue</b></font>");
      }
      else if (action.equals("Delete"))
      {
        ACC.getAccountFromDB();
        ACC.setMessage("<font color=blue><b>Select 'Delete' from the 'Options' menu above</b></font>");
      }
      else if (action.equals("Amend"))
      {
        ACC.getAccountFromDB();
        ACC.setMessage("<font color=blue><b>Amend as required and then select " +
          "'Update' from the 'Options' menu above. Mandatory fields are shown in blue.</b></font>");
      }
      ACC.populateAccountProductList();
      %>
	<jsp:forward page="accountMaint.jsp"/>
      <%
    }
    else if (ButtonPressed.equals("Delete"))
    {
      ACC.setMode("Confirm");
      ACC.setMessage("<font color=blue><b>Select 'Confirm' from the " +
              "'Options' menu above to delete this Account or 'Cancel' to abort the operation</b></font>");
      %>
	<jsp:forward page="accountMaint.jsp"/>
      <%
    }
    else if (ButtonPressed.equals("Confirm"))
    {
      String action = ACC.getAction();
      if (action.equals("Add"))
      {
        if (ACC.createAccount())
        {
          RSB.setMessage(ACC.getMessage());
          RSB.setAccountId(ACC.getAccountId());
          %>
            <jsp:forward page="referenceDataMenu.jsp"/>
          <%
        }
        else
        {
          %>
            <jsp:forward page="accountMaint.jsp"/>
          <%
        }
      }
      else if (action.equals("Delete"))
      {
        if (ACC.deleteAccount())
        {
          %>
            <jsp:forward page="referenceDataMenu.jsp"/>
          <%
        }
        else
        {
          %>
            <jsp:forward page="accountMaint.jsp"/>
          <%
        }
      }
      else if (action.equals("Amend"))
      {
        if (ACC.updateAccount())
        {
          RSB.setMessage(ACC.getMessage());
          %>
            <jsp:forward page="referenceDataMenu.jsp"/>
          <%
        }
        else
        {
          %>
            <jsp:forward page="accountMaint.jsp"/>
          <%
        }
      }
    }
    else if (ButtonPressed.equals("Cancel"))
    {
      if (ACC.getAction().equals("Amend"))
      {
        ACC.setMode("Amend");
        ACC.setMessage("<font color=blue><b>Amend as required and then select " +
          "'Update' from the 'Options' menu above. Mandatory fields are shown in blue.</b></font>");
      }
      else if (ACC.getAction().equals("Add"))
      {
        ACC.setMode("Create");
        ACC.setMessage("<font color=blue><b>Mandatory fields are shown in blue</b></font>");
      }
      else if (ACC.getAction().equals("Delete"))
      {
        ACC.setMode("Delete");
        ACC.setMessage("<font color=blue><b>Select 'Delete' from the 'Options' menu above</b></font>");
      }
      %>
	<jsp:forward page="accountMaint.jsp"/>
      <%
    }
    else if (ButtonPressed.equals("Provider Maintenance"))
    {
      PRO.Reset();
      PRO.setUserId((String)session.getAttribute("User_Id"));
      PRO.setProviderId(ACC.getProviderId());
      PRO.getProviderFromDB();
      PRO.setAction("Amend");
      PRO.setMode("Amend");
      PRO.setMessage("<font color=blue><b>Amend as required and then select " +
        "'Update' from the 'Options' menu above. Mandatory fields are shown in blue.</b></font>");
     %>
        <jsp:forward page="providerMaint.jsp"/>
      <%
    }
    else if (ButtonPressed.equals("Master Account Maintenance"))
    {
      MAC.Reset();
      MAC.setUserId((String)session.getAttribute("User_Id"));
      MAC.setMasterAccountId(ACC.getMasterAccountId());
      MAC.setProviderId(ACC.getProviderId());
      MAC.getProviderFromDB();
      MAC.getMasterAccountFromDB();
      MAC.populateProductGroupList();
      MAC.setAction("Amend");
      MAC.setMode("Amend");
      MAC.setMessage("<font color=blue><b>Amend as required and then select " +
        "'Update' from the 'Options' menu above. Mandatory fields are shown in blue.</b></font>");
      %>
        <jsp:forward page="masterAccountMaint.jsp"/>
      <%
    }
    else
    {
      //store form values in Bean
      while (FormFields.hasMoreElements())
      {
        FormField=(String)FormFields.nextElement();
        ACC.setParameter(FormField,request.getParameter(FormField));
      }
      if ((ButtonPressed.equals("Submit")) || (ButtonPressed.equals("Update")))
      {
        if (ACC.isValid(ButtonPressed))
        {
          ACC.setMode("Confirm");
          if (ButtonPressed.equals("Update"))
          {
            ACC.setMessage("<font color=blue><b>Select 'Confirm' from the " +
              "'Options' menu above to commit your updates or 'Cancel' to abort the operation</b></font>");
          }
          else
          {
            ACC.setMessage("<font color=blue><b>Select 'Confirm' from the " +
              "'Options' menu above to create this Account or 'Cancel' to amend it</b></font>");
          }
        }
        %>
        <jsp:forward page="accountMaint.jsp"/>
        <%
      }
      else
      {//No button pressed, so must be a Refresh
        RSB.getLatestProcessControl();
        %>
        <jsp:forward page="accountMaint.jsp"/>
        <%
      }
    }
%>
</body>
</html>
