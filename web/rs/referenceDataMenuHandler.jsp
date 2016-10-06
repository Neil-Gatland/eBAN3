<html>
<head>
</head>
<%@ page import="JavaUtil.*"%>
<jsp:useBean id="BAN" class="DBUtilities.BANBean" scope="session"/>
<jsp:useBean id="DBA" class="DBUtilities.DBAccess" scope="session"/>
<jsp:useBean id="RSB" class="DBUtilities.RevenueShareBean" scope="session"/>
<jsp:useBean id="PGB" class="DBUtilities.ProductGroupBean" scope="session"/>
<jsp:useBean id="PRD" class="DBUtilities.ProductBean" scope="session"/>
<jsp:useBean id="PRO" class="DBUtilities.ProviderBean" scope="session"/>
<jsp:useBean id="MAC" class="DBUtilities.MasterAccountBean" scope="session"/>
<jsp:useBean id="ACC" class="DBUtilities.AccountBean" scope="session"/>
<jsp:useBean id="TRB" class="DBUtilities.ThresholdBean" scope="session"/>
<jsp:useBean id="NTS" class="DBUtilities.NTSBean" scope="session"/>

<%!     String ButtonPressed="",providerId="",productId="",masterAccountId="",accountId="",productGroupId="";
        JavaUtil.StringUtil SU = new JavaUtil.StringUtil();
        JavaUtil.UserData UD = new UserData();
%>
<%
    //Clear out BAN beans

    session.setAttribute("PageSent",request.getRequestURI());
    ButtonPressed=(String)request.getParameter("ButtonPressed");

    if (ButtonPressed.equals("Revenue Share Desktop"))
    {
      RSB.canCloseMonth();
      %>
        <jsp:forward page="rsDesktop.jsp"/>
      <%
    }
    else if (ButtonPressed.equals("Reset"))
    {
      RSB.Reset();
      %>
        <jsp:forward page="referenceDataMenu.jsp"/>
      <%
    }
    else if (ButtonPressed.equals("Missing Data Identification"))
    {
      %>
        <jsp:forward page="missingDataIdentification.jsp"/>
      <%
    }
    else if (ButtonPressed.equals("View All Processes"))
    {
      RSB.populateProcessList();
      %>
        <jsp:forward page="referenceDataMenu.jsp">
         <jsp:param name="viewProcess" value="true"/>
        </jsp:forward>
      <%
    }
    else if (ButtonPressed.equals("View Account Move History"))
    {
      if (RSB.canMoveAccount())
      {
        %>
          <jsp:forward page="referenceDataMenu.jsp">
            <jsp:param name="accountMoveHist" value="true"/>
          </jsp:forward>
        <%
      }
      else
      {
        RSB.setMessage("<font color=blue><b>This account is the master account and cannot be moved</b></font>");
        %>
          <jsp:forward page="referenceDataMenu.jsp"/>
        <%
      }
    }
    else if (ButtonPressed.equals("View Provider/Master Account Summary"))
    {
      PRO.Reset();
      PRO.setUserId((String)session.getAttribute("User_Id"));
      PRO.setProviderId(RSB.getProviderId());
      PRO.getProviderFromDB();
      MAC.Reset();
      MAC.setUserId((String)session.getAttribute("User_Id"));
      MAC.setMasterAccountId(RSB.getMasterAccountId());
      MAC.getMasterAccountFromDB();
      %>
        <jsp:forward page="referenceDataMenu.jsp">
         <jsp:param name="viewPMASummary" value="true"/>
        </jsp:forward>
      <%
    }
    else
    {
      //Global_Customer_Id=(String)request.getParameter("Product_Customer");
      providerId=(String)request.getParameter("rsProvider");
      accountId=(String)request.getParameter("rsAccount");
      masterAccountId=(String)request.getParameter("rsMasterAccount");
      productGroupId=(String)request.getParameter("rsProductGroup");
      productId=(String)request.getParameter("rsProduct");
      if (!providerId.equals(RSB.getProviderId()))
      {
	RSB.setProviderId(SU.isNull((String)request.getParameter("rsProvider"),""));
	RSB.setMasterAccountId("");
	RSB.setAccountId("");
	//RSB.setProductGroupId("");
	//RSB.setProductId("");
      }
      else if (!masterAccountId.equals(RSB.getMasterAccountId()))
      {
	RSB.setMasterAccountId(SU.isNull((String)request.getParameter("rsMasterAccount"),""));
	RSB.setAccountId("");
	//RSB.setProductGroupId("");
	//RSB.setProductId("");
      }
      else if (!productGroupId.equals(RSB.getProductGroupId()))
      {
	RSB.setProductGroupId(SU.isNull((String)request.getParameter("rsProductGroup"),""));
	RSB.setProductId("");
      }
      else
      {
	RSB.setAccountId(accountId);
	RSB.setProductId(productId);
      }

      if (ButtonPressed.equals("Raise Query"))
      {
        %>
          <jsp:forward page="referenceDataMenu.jsp">
            <jsp:param name="query" value="true"/>
            <jsp:param name="fromPage" value="referenceDataMenu"/>
          </jsp:forward>
        <%
      }
      else if (ButtonPressed.equals("Create Provider"))
      {
        PRO.Reset();
        PRO.setUserId((String)session.getAttribute("User_Id"));
        PRO.setAction("Add");
        PRO.setMode("Create");
        PRO.setMessage("<font color=blue><b>Mandatory fields are shown in blue</b></font>");
        %>
          <jsp:forward page="providerMaint.jsp"/>
        <%
      }
      else if (ButtonPressed.equals("Update Provider"))
      {
        PRO.Reset();
        PRO.setUserId((String)session.getAttribute("User_Id"));
        PRO.setProviderId(providerId);
        PRO.getProviderFromDB();
        PRO.setAction("Amend");
        PRO.setMode("Amend");
        PRO.setMessage("<font color=blue><b>Amend as required and then select " +
          "'Update' from the 'Options' menu above. Mandatory fields are shown in blue.</b></font>");
       %>
          <jsp:forward page="providerMaint.jsp"/>
        <%
      }
      else if (ButtonPressed.equals("Create Master Account"))
      {
        MAC.Reset();
        MAC.setUserId((String)session.getAttribute("User_Id"));
        MAC.setProviderId(providerId);
        MAC.getProviderFromDB();
        if (!RSB.getMissingAccount().equals(""))
        {
          MAC.setMasterAccountNumber(RSB.getMissingAccount());
          RSB.setMissingAccount("");
        }
        MAC.setAction("Add");
        MAC.setMode("Create");
        MAC.setMessage("<font color=blue><b>Mandatory fields are shown in blue</b></font>");
        %>
          <jsp:forward page="masterAccountMaint.jsp"/>
        <%
      }
      else if (ButtonPressed.equals("Update Master Account"))
      {
        MAC.Reset();
        MAC.setUserId((String)session.getAttribute("User_Id"));
        MAC.setMasterAccountId(masterAccountId);
        MAC.setProviderId(providerId);
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
      else if (ButtonPressed.equals("Delete Master Account"))
      {
        MAC.Reset();
        MAC.setUserId((String)session.getAttribute("User_Id"));
        MAC.setMasterAccountId(masterAccountId);
        if (MAC.canDelete())
        {
          MAC.setProviderId(providerId);
          MAC.getProviderFromDB();
          MAC.getMasterAccountFromDB();
          MAC.populateProductGroupList();
          MAC.setAction("Delete");
          MAC.setMode("Delete");
          MAC.setMessage("<font color=blue><b>Select 'Delete' from the 'Options' menu above</b></font>");
          %>
            <jsp:forward page="masterAccountMaint.jsp"/>
          <%
        }
        else
        {
          RSB.setMessage("<font color=red><b>Please delete all Accounts belonging to this Master Account first.</b></font>");
          %>
            <jsp:forward page="referenceDataMenu.jsp"/>
          <%
        }
      }
      else if (ButtonPressed.equals("Create Account"))
      {
        ACC.Reset();
        ACC.setUserId((String)session.getAttribute("User_Id"));
        ACC.setProviderId(providerId);
        ACC.setMasterAccountId(masterAccountId);
        ACC.getMasterAccountFromDB();
        if (!RSB.getMissingAccount().equals(""))
        {
          ACC.setAccountNumber(RSB.getMissingAccount());
          RSB.setMissingAccount("");
        }
        ACC.setAction("Add");
        ACC.setMode("Create");
        ACC.setMessage("<font color=blue><b>Mandatory fields are shown in blue</b></font>");
        %>
          <jsp:forward page="accountMaint.jsp"/>
        <%
      }
      else if (ButtonPressed.equals("Update Account"))
      {
        ACC.Reset();
        ACC.setUserId((String)session.getAttribute("User_Id"));
        ACC.setAccountId(accountId);
        ACC.setMasterAccountId(masterAccountId);
        ACC.setProviderId(providerId);
        ACC.getAccountFromDB();
        ACC.getMasterAccountFromDB();
        ACC.populateAccountProductList();
        ACC.setAction("Amend");
        ACC.setMode("Amend");
        ACC.setMessage("<font color=blue><b>Amend as required and then select " +
          "'Update' from the 'Options' menu above. Mandatory fields are shown in blue.</b></font>");
        %>
          <jsp:forward page="accountMaint.jsp"/>
        <%
      }
      else if (ButtonPressed.equals("Add Product to Account"))
      {
        ACC.Reset();
        ACC.setUserId((String)session.getAttribute("User_Id"));
        ACC.setAccountId(accountId);
        ACC.getAccountFromDB();
        ACC.setProductCode(productId);
        if (!ACC.accountProductExists())
        {
          ACC.getProductFromDB();
          ACC.createAccountProduct();
          RSB.setMessage(ACC.getMessage());
        }
        else
        {
          RSB.setMessage("<font color=blue><b>The Account already has this Product</b></font>");
        }
        %>
          <jsp:forward page="referenceDataMenu.jsp"/>
        <%
      }
      else if (ButtonPressed.equals("Delete Product from Account"))
      {
        ACC.Reset();
        ACC.setUserId((String)session.getAttribute("User_Id"));
        ACC.setAccountId(accountId);
        ACC.setProductCode(productId);
        if (ACC.accountProductExists())
        {
          ACC.getAccountFromDB();
          ACC.getProductFromDB();
          ACC.deleteAccountProduct();
          RSB.setMessage(ACC.getMessage());
        }
        else
        {
          RSB.setMessage("<font color=blue><b>This combination of Account and Product does not exist.</b></font>");
        }
        %>
          <jsp:forward page="referenceDataMenu.jsp"/>
        <%
      }
      else if (ButtonPressed.equals("Delete Account"))
      {
        ACC.Reset();
        ACC.setUserId((String)session.getAttribute("User_Id"));
        ACC.setAccountId(accountId);
        if (ACC.canDelete())
        {
          ACC.setMasterAccountId(masterAccountId);
          ACC.setProviderId(providerId);
          ACC.getAccountFromDB();
          ACC.getMasterAccountFromDB();
          ACC.populateAccountProductList();
          ACC.setAction("Delete");
          ACC.setMode("Delete");
          ACC.setMessage("<font color=blue><b>Select 'Delete' from the 'Options' menu above</b></font>");
          %>
            <jsp:forward page="accountMaint.jsp"/>
          <%
        }
        else
        {
          RSB.setMessage("<font color=red><b>This Account cannot be deleted until all Products associated with it have been removed.</b></font>");
          %>
            <jsp:forward page="referenceDataMenu.jsp"/>
          <%
        }
      }
      else if (ButtonPressed.equals("Create Product"))
      {
        PRD.Reset();
        PRD.setUserId((String)session.getAttribute("User_Id"));
        PRD.setProductGroup(RSB.getProductGroupId());
        PRD.setReturnTo("referenceDataMenu.jsp");
        if (!RSB.getMissingProduct().equals(""))
        {
          PRD.setProductCode(RSB.getMissingProduct());
          RSB.setMissingProduct("");
        }
        PRD.setAction("Add");
        PRD.setMode("Create");
        PRD.setMessage("<font color=blue><b>Mandatory fields are shown in blue</b></font>");
        %>
          <jsp:forward page="productMaint.jsp"/>
        <%
      }
      else if (ButtonPressed.equals("Delete Product"))
      {
        PRD.Reset();
        PRD.setUserId((String)session.getAttribute("User_Id"));
        PRD.setProductGroup(PGB.getProductGroup());
        PRD.setProductCode(productId);
        if (PRD.canDelete())
        {
          PRD.getProductFromDB();
          PRD.setReturnTo("referenceDataMenu.jsp");
          PRD.setAction("Delete");
          PRD.setMode("Delete");
          PRD.setMessage("<font color=blue><b>Select 'Delete' from the 'Options' menu above</b></font>");
          %>
            <jsp:forward page="productMaint.jsp"/>
          <%
        }
        else
        {
          RSB.setMessage("<font color=red><b>This Product cannot be deleted until it is no longer associated with any Accounts.</b></font>");
          %>
            <jsp:forward page="referenceDataMenu.jsp"/>
          <%
        }
      }
      else if (ButtonPressed.equals("Create Product Group"))
      {
        PGB.Reset();
        PGB.setUserId((String)session.getAttribute("User_Id"));
        PGB.setAction("Add");
        PGB.setMode("Create");
        PGB.setMessage("<font color=blue><b>Mandatory fields are shown in blue</b></font>");
        %>
          <jsp:forward page="productGroupMaint.jsp"/>
        <%
      }
      else if (ButtonPressed.equals("Delete Product Group"))
      {
        PGB.Reset();
        PGB.setUserId((String)session.getAttribute("User_Id"));
        PGB.setProductGroup(productGroupId);
        if (PGB.canDelete())
        {
          PGB.getProductGroupFromDB();
          PGB.setAction("Delete");
          PGB.setMode("Delete");
          PGB.setMessage("<font color=blue><b>Select 'Delete' from the 'Options' menu above</b></font>");
          %>
            <jsp:forward page="productGroupMaint.jsp"/>
          <%
        }
        else
        {
          RSB.setMessage("<font color=red><b>This Product Group cannot be deleted until all of its Products have been deleted.</b></font>");
          %>
            <jsp:forward page="referenceDataMenu.jsp"/>
          <%
        }
      }
       else if (ButtonPressed.equals("Update Product Group"))
      {
        PGB.Reset();
        PGB.setUserId((String)session.getAttribute("User_Id"));
        PGB.setProductGroup(productGroupId);
        PGB.getProductGroupFromDB();
        PGB.populateProductList();
        PGB.setAction("Amend");
        PGB.setMode("Amend");
        PGB.setMessage("<font color=blue><b>To create a new Product for " +
          "this Product Group, select 'Add New Product' " +
          "from the 'Options' menu above.</b></font>");
        //PGB.setMessage("<font color=blue><b>Amend as required and then select " +
          //"'Update' from the 'Options' menu above. Mandatory fields are shown in blue.</b></font>");
       %>
          <jsp:forward page="productGroupMaint.jsp"/>
        <%
      }
      else if (ButtonPressed.equals("Create Invoice/Master Threshold"))
      {
        TRB.Reset();
        TRB.setUserId((String)session.getAttribute("User_Id"));
        TRB.setAction("Add");
        TRB.setMode("Create");
        TRB.setThresholdType("Invoice");
        TRB.setMasterAccountId(RSB.getMasterAccountId());
        if (TRB.getThresholdFromDB())
        {
          RSB.setMessage("<font color=blue><b>Threshold exists already</b></font>");
          %>
            <jsp:forward page="referenceDataMenu.jsp"/>
          <%
        }
        else
        {
          TRB.getMasterAccountFromDB();
          TRB.setMessage("<font color=blue><b>Enter values as required and then select " +
            "'Submit' from the 'Options' menu above. Mandatory fields are shown in blue.</b></font>");
          %>
            <jsp:forward page="thresholdMaint.jsp"/>
          <%
        }
      }
      else if (ButtonPressed.equals("Update Invoice/Master Threshold"))
      {
        TRB.Reset();
        TRB.setUserId((String)session.getAttribute("User_Id"));
        TRB.setAction("Amend");
        TRB.setMode("Amend");
        TRB.setThresholdType("Invoice");
        TRB.setMasterAccountId(RSB.getMasterAccountId());
        if (TRB.getThresholdFromDB())
        {
          TRB.getMasterAccountFromDB();
          TRB.setMessage("<font color=blue><b>Amend as required and then select " +
            "'Update' from the 'Options' menu above. Mandatory fields are shown in blue.</b></font>");
          %>
            <jsp:forward page="thresholdMaint.jsp"/>
          <%
        }
        else
        {
          RSB.setMessage("<font color=blue><b>Threshold does not exist</b></font>");
          %>
            <jsp:forward page="referenceDataMenu.jsp"/>
          <%
        }
      }
      else if (ButtonPressed.equals("Delete Invoice/Master Threshold"))
      {
        TRB.Reset();
        TRB.setUserId((String)session.getAttribute("User_Id"));
        TRB.setAction("Delete");
        TRB.setMode("Delete");
        TRB.setThresholdType("Invoice");
        TRB.setMasterAccountId(RSB.getMasterAccountId());
        if (TRB.getThresholdFromDB())
        {
          TRB.getMasterAccountFromDB();
          TRB.setMessage("<font color=blue><b>Select 'Delete' from the 'Options' menu above</b></font>");
          %>
            <jsp:forward page="thresholdMaint.jsp"/>
          <%
        }
        else
        {
          RSB.setMessage("<font color=blue><b>Threshold does not exist</b></font>");
          %>
            <jsp:forward page="referenceDataMenu.jsp"/>
          <%
        }
      }
      else if (ButtonPressed.equals("Create Product Group/Master Threshold"))
      {
        TRB.Reset();
        TRB.setUserId((String)session.getAttribute("User_Id"));
        TRB.setAction("Add");
        TRB.setMode("Create");
        TRB.setThresholdType("Product Group");
        TRB.setProductGroup(RSB.getProductGroupId());
        TRB.setMasterAccountId(RSB.getMasterAccountId());
        if (TRB.getThresholdFromDB())
        {
          RSB.setMessage("<font color=blue><b>Threshold exists already</b></font>");
          %>
            <jsp:forward page="referenceDataMenu.jsp"/>
          <%
        }
        else
        {
          TRB.getMasterAccountFromDB();
          TRB.setMessage("<font color=blue><b>Enter values as required and then select " +
            "'Submit' from the 'Options' menu above. Mandatory fields are shown in blue.</b></font>");
          %>
            <jsp:forward page="thresholdMaint.jsp"/>
          <%
        }
      }
      else if (ButtonPressed.equals("Update Product Group/Master Threshold"))
      {
        TRB.Reset();
        TRB.setUserId((String)session.getAttribute("User_Id"));
        TRB.setAction("Amend");
        TRB.setMode("Amend");
        TRB.setThresholdType("Product Group");
        TRB.setProductGroup(RSB.getProductGroupId());
        TRB.setMasterAccountId(RSB.getMasterAccountId());
        if (TRB.getThresholdFromDB())
        {
          //TRB.getProductFromDB();
          TRB.getMasterAccountFromDB();
          TRB.setMessage("<font color=blue><b>Amend as required and then select " +
            "'Update' from the 'Options' menu above. Mandatory fields are shown in blue.</b></font>");
          %>
            <jsp:forward page="thresholdMaint.jsp"/>
          <%
        }
        else
        {
          RSB.setMessage("<font color=blue><b>Threshold does not exist</b></font>");
          %>
            <jsp:forward page="referenceDataMenu.jsp"/>
          <%
        }
      }
      else if (ButtonPressed.equals("Delete Product Group/Master Threshold"))
      {
        TRB.Reset();
        TRB.setUserId((String)session.getAttribute("User_Id"));
        TRB.setAction("Delete");
        TRB.setMode("Delete");
        TRB.setThresholdType("Product Group");
        TRB.setProductGroup(RSB.getProductGroupId());
        TRB.setMasterAccountId(RSB.getMasterAccountId());
        if (TRB.getThresholdFromDB())
        {
          //TRB.getProductFromDB();
          TRB.getMasterAccountFromDB();
          TRB.setMessage("<font color=blue><b>Select 'Delete' from the 'Options' menu above</b></font>");
          %>
            <jsp:forward page="thresholdMaint.jsp"/>
          <%
        }
        else
        {
          RSB.setMessage("<font color=blue><b>Threshold does not exist</b></font>");
          %>
            <jsp:forward page="referenceDataMenu.jsp"/>
          <%
        }
      }
/*      else if (ButtonPressed.equals("Create Product/System Threshold"))
      {
        TRB.Reset();
        TRB.setUserId((String)session.getAttribute("User_Id"));
        TRB.setAction("Add");
        TRB.setMode("Create");
        TRB.setThresholdType("Product");
        TRB.setProductCode(RSB.getProductId());
        if (TRB.getThresholdFromDB())
        {
          RSB.setMessage("<font color=blue><b>Threshold exists already</b></font>");

            <jsp:forward page="referenceDataMenu.jsp"/>

        }
        else
        {
          TRB.getProductFromDB();
          TRB.setMessage("<font color=blue><b>Enter values as required and then select " +
            "'Submit' from the 'Options' menu above. Mandatory fields are shown in blue.</b></font>");

            <jsp:forward page="thresholdMaint.jsp"/>

        }
      }
      else if (ButtonPressed.equals("Update Product/System Threshold"))
      {
        TRB.Reset();
        TRB.setUserId((String)session.getAttribute("User_Id"));
        TRB.setAction("Amend");
        TRB.setMode("Amend");
        TRB.setThresholdType("Product");
        TRB.setProductCode(RSB.getProductId());
        if (TRB.getThresholdFromDB())
        {
          TRB.getProductFromDB();
          TRB.setMessage("<font color=blue><b>Amend as required and then select " +
            "'Update' from the 'Options' menu above. Mandatory fields are shown in blue.</b></font>");

            <jsp:forward page="thresholdMaint.jsp"/>

        }
        else
        {
          RSB.setMessage("<font color=blue><b>Threshold does not exist</b></font>");

            <jsp:forward page="referenceDataMenu.jsp"/>

        }
      }
      else if (ButtonPressed.equals("Delete Product/System Threshold"))
      {
        TRB.Reset();
        TRB.setUserId((String)session.getAttribute("User_Id"));
        TRB.setAction("Delete");
        TRB.setMode("Delete");
        TRB.setThresholdType("Product");
        TRB.setProductCode(RSB.getProductId());
        if (TRB.getThresholdFromDB())
        {
          TRB.getProductFromDB();
          TRB.setMessage("<font color=blue><b>Select 'Delete' from the 'Options' menu above</b></font>");

            <jsp:forward page="thresholdMaint.jsp"/>

        }
        else
        {
          RSB.setMessage("<font color=blue><b>Threshold does not exist</b></font>");

            <jsp:forward page="referenceDataMenu.jsp"/>

        }
      }*/
      else if (ButtonPressed.equals("Update Invoice/System Threshold"))
      {
        TRB.Reset();
        TRB.setUserId((String)session.getAttribute("User_Id"));
        TRB.setAction("Amend");
        TRB.setMode("Amend");
        TRB.setThresholdType("Invoice");
        TRB.getThresholdFromDB();
        TRB.setMessage("<font color=blue><b>Amend as required and then select " +
          "'Update' from the 'Options' menu above. Mandatory fields are shown in blue.</b></font>");
        %>
          <jsp:forward page="thresholdMaint.jsp"/>
        <%
      }
      else if (ButtonPressed.equals("Update Product NTS Prefix List"))
      {
        NTS.Reset();
        NTS.setUserId((String)session.getAttribute("User_Id"));
        NTS.setAction("Add");
        NTS.setMode("Create");
        NTS.setProductGroup(productGroupId);
        NTS.setProductCode(productId);
        NTS.populateNTSPrefixList();
        NTS.setMessage("<font color=blue><b>Mandatory fields are shown in blue</b></font>");
        %>
          <jsp:forward page="ntsPrefixMaint.jsp"/>
        <%
      }
      else
      {//No button pressed, so must be a Refresh
        RSB.getLatestCallMonth();
        %>
          <jsp:forward page="referenceDataMenu.jsp"/>
        <%
      }
    }
      %>
</body>
</html>
