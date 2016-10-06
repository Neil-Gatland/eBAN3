<html>
<head>
</head>
<body>
<%@ page import="JavaUtil.StringUtil"%>
<%@ page import="java.util.Enumeration"%>

<jsp:useBean id="BAN" class="DBUtilities.BANBean" scope="session"/>
<jsp:useBean id="HB" class="HTMLUtil.HTMLBean" scope="session"/>
<jsp:useBean id="MAC" class="DBUtilities.MasterAccountBean" scope="session"/>
<jsp:useBean id="PRO" class="DBUtilities.ProviderBean" scope="session"/>
<jsp:useBean id="RSB" class="DBUtilities.RevenueShareBean" scope="session"/>
<%! String ButtonPressed, banId, listType;%>
<%
    Enumeration FormFields = request.getParameterNames();
    String FormField = null;
    session.setAttribute("formname","masterAccountMaintHandler");
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
        <jsp:forward page="masterAccountMaint.jsp">
          <jsp:param name="query" value="true"/>
          <jsp:param name="fromPage" value="masterAccountMaint"/>
        </jsp:forward>
      <%
    }
    else if (ButtonPressed.equals("View All Processes"))
    {
      RSB.populateProcessList();
      %>
        <jsp:forward page="masterAccountMaint.jsp">
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
    else if (ButtonPressed.equals("listApproveN"))
    {
      MAC.updateRatingSchemeApproved(request.getParameter("productGroup"), "N");
      MAC.populateProductGroupList();
      %>
        <jsp:forward page="masterAccountMaint.jsp"/>
      <%
    }
    else if (ButtonPressed.equals("listApproveY"))
    {
      MAC.updateRatingSchemeApproved(request.getParameter("productGroup"), "Y");
      MAC.populateProductGroupList();
      %>
        <jsp:forward page="masterAccountMaint.jsp"/>
      <%
    }
    else if (ButtonPressed.equals("Refresh"))
    {
      String action = MAC.getAction();
      if (action.equals("Add"))
      {
        MAC.Reset();
        MAC.setAction(action);
        MAC.setMode("Create");
        MAC.setMessage("<font color=blue><b>Mandatory fields are shown in blue</b></font>");
      }
      else if (action.equals("Delete"))
      {
        MAC.getMasterAccountFromDB();
        MAC.populateProductGroupList();
        MAC.setMessage("<font color=blue><b>Select 'Delete' from the 'Options' menu above</b></font>");
      }
      else if (action.equals("Amend"))
      {
        MAC.getMasterAccountFromDB();
        MAC.populateProductGroupList();
        MAC.setMessage("<font color=blue><b>Amend as required and then select " +
          "'Update' from the 'Options' menu above. Mandatory fields are shown in blue.</b></font>");
      }
      %>
	<jsp:forward page="masterAccountMaint.jsp"/>
      <%
    }
    else if (ButtonPressed.equals("Delete"))
    {
      MAC.setMode("Confirm");
      MAC.setMessage("<font color=blue><b>Select 'Confirm' from the " +
              "'Options' menu above to delete this Master Account or 'Cancel' to abort the operation</b></font>");
      %>
	<jsp:forward page="masterAccountMaint.jsp"/>
      <%
    }
    else if (ButtonPressed.equals("Confirm"))
    {
      String action = MAC.getAction();
      if (action.equals("Add"))
      {
        if (MAC.createMasterAccount())
        {
          RSB.setMessage(MAC.getMessage());
          RSB.setMasterAccountId(MAC.getMasterAccountId());
          %>
            <jsp:forward page="referenceDataMenu.jsp"/>
          <%
        }
        else
        {
          %>
            <jsp:forward page="masterAccountMaint.jsp"/>
          <%
        }
      }
      else if (action.equals("Delete"))
      {
        if (MAC.deleteMasterAccount())
        {
          RSB.setMessage(MAC.getMessage());
          %>
            <jsp:forward page="referenceDataMenu.jsp"/>
          <%
        }
        else
        {
          %>
            <jsp:forward page="masterAccountMaint.jsp"/>
          <%
        }
      }
      else if (action.equals("Amend"))
      {
        if (MAC.updateMasterAccount())
        {
          RSB.setMessage(MAC.getMessage());
          %>
            <jsp:forward page="referenceDataMenu.jsp"/>
          <%
        }
        else
        {
          %>
            <jsp:forward page="masterAccountMaint.jsp"/>
          <%
        }
      }
    }
    else if (ButtonPressed.equals("Cancel"))
    {
      if (MAC.getAction().equals("Amend"))
      {
        MAC.setMode("Amend");
        MAC.setMessage("<font color=blue><b>Amend as required and then select " +
          "'Update' from the 'Options' menu above. Mandatory fields are shown in blue.</b></font>");
      }
      else if (MAC.getAction().equals("Add"))
      {
        MAC.setMode("Create");
        MAC.setMessage("<font color=blue><b>Mandatory fields are shown in blue</b></font>");
      }
      else if (MAC.getAction().equals("Delete"))
      {
        MAC.setMode("Delete");
        MAC.setMessage("<font color=blue><b>Select 'Delete' from the 'Options' menu above</b></font>");
      }
      %>
	<jsp:forward page="masterAccountMaint.jsp"/>
      <%
    }
    else if (ButtonPressed.equals("Provider Maintenance"))
    {
      PRO.Reset();
      PRO.setUserId((String)session.getAttribute("User_Id"));
      PRO.setProviderId(MAC.getProviderId());
      PRO.getProviderFromDB();
      PRO.setAction("Amend");
      PRO.setMode("Amend");
      PRO.setMessage("<font color=blue><b>Amend as required and then select " +
        "'Update' from the 'Options' menu above. Mandatory fields are shown in blue.</b></font>");
      %>
        <jsp:forward page="providerMaint.jsp"/>
      <%
    }
    else
    {
      //store form values in Bean
      while (FormFields.hasMoreElements())
      {
        FormField=(String)FormFields.nextElement();
        MAC.setParameter(FormField,request.getParameter(FormField));
      }
      if ((ButtonPressed.equals("Submit")) || (ButtonPressed.equals("Update")))
      {
        if (MAC.isValid(ButtonPressed))
        {
          MAC.setMode("Confirm");
          if (ButtonPressed.equals("Update"))
          {
            MAC.setMessage("<font color=blue><b>Select 'Confirm' from the " +
              "'Options' menu above to commit your updates or 'Cancel' to abort the operation</b></font>");
          }
          else
          {
            MAC.setMessage("<font color=blue><b>Select 'Confirm' from the " +
              "'Options' menu above to create this Master Account or 'Cancel' to amend it</b></font>");
          }
        }
        %>
        <jsp:forward page="masterAccountMaint.jsp"/>
        <%
      }
      else
      {//No button pressed, so must be a Refresh
        RSB.getLatestCallMonth();
        MAC.populateProductGroupList();
        %>
        <jsp:forward page="masterAccountMaint.jsp"/>
        <%
      }
    }
%>
</body>
</html>
