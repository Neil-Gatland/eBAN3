<html>
<head>
</head>
<body>
<%@ page import="JavaUtil.StringUtil"%>
<%@ page import="java.util.Enumeration"%>

<jsp:useBean id="BAN" class="DBUtilities.BANBean" scope="session"/>
<jsp:useBean id="HB" class="HTMLUtil.HTMLBean" scope="session"/>
<jsp:useBean id="PRO" class="DBUtilities.ProviderBean" scope="session"/>
<jsp:useBean id="MAC" class="DBUtilities.MasterAccountBean" scope="session"/>
<jsp:useBean id="RSB" class="DBUtilities.RevenueShareBean" scope="session"/>
<%! String ButtonPressed, banId, listType;%>
<%
    Enumeration FormFields = request.getParameterNames();
    String FormField = null;
    session.setAttribute("formname","providerMaintHandler");
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
        <jsp:forward page="providerMaint.jsp">
          <jsp:param name="query" value="true"/>
          <jsp:param name="fromPage" value="providerMaint"/>
        </jsp:forward>
      <%
    }
    else if (ButtonPressed.equals("View All Processes"))
    {
      RSB.populateProcessList();
      %>
        <jsp:forward page="providerMaint.jsp">
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
    else if (ButtonPressed.equals("Refresh"))
    {
      String action = PRO.getAction();
      PRO.Reset();
      PRO.setAction(action);
      if (action.equals("Add"))
      {
        PRO.setMode("Create");
        PRO.setMessage("<font color=blue><b>Mandatory fields are shown in blue</b></font>");
      }
      else if (action.equals("Delete"))
      {
        PRO.getProviderFromDB();
        PRO.setMode("Delete");
        PRO.setMessage("<font color=blue><b>Select 'Delete' from the 'Options' menu above</b></font>");
      }
      else if (action.equals("Amend"))
      {
        PRO.getProviderFromDB();
        PRO.setMode("Amend");
        PRO.setMessage("<font color=blue><b>Amend as required and then select " +
          "'Update' from the 'Options' menu above. Mandatory fields are shown in blue.</b></font>");
      }
      %>
	<jsp:forward page="providerMaint.jsp"/>
      <%
    }
    else if (ButtonPressed.equals("Delete"))
    {
      PRO.setMode("Confirm");
      PRO.setMessage("<font color=blue><b>Select 'Confirm' from the " +
              "'Options' menu above to delete this Provider or 'Cancel' to abort the operation</b></font>");
      %>
	<jsp:forward page="providerMaint.jsp"/>
      <%
    }
    else if (ButtonPressed.equals("Confirm"))
    {
      String action = PRO.getAction();
      if (action.equals("Add"))
      {
        if (PRO.createProvider())
        {
          RSB.setMessage(PRO.getMessage());
          RSB.setProviderId(PRO.getProviderId());
          %>
            <jsp:forward page="referenceDataMenu.jsp"/>
          <%
        }
        else
        {
          %>
            <jsp:forward page="providerMaint.jsp"/>
          <%
        }
      }
      else if (action.equals("Delete"))
      {
        if (PRO.deleteProvider())
        {
          RSB.setMessage(PRO.getMessage());
          %>
            <jsp:forward page="referenceDataMenu.jsp"/>
          <%
        }
        else
        {
          %>
            <jsp:forward page="providerMaint.jsp"/>
          <%
        }
      }
      else if (action.equals("Amend"))
      {
        if (PRO.updateProvider())
        {
          RSB.setMessage(PRO.getMessage());
          %>
            <jsp:forward page="referenceDataMenu.jsp"/>
          <%
        }
        else
        {
          %>
            <jsp:forward page="providerMaint.jsp"/>
          <%
        }
      }
    }
    else if (ButtonPressed.equals("Cancel"))
    {
      if (PRO.getAction().equals("Amend"))
      {
        PRO.setMode("Amend");
        PRO.setMessage("<font color=blue><b>Amend as required and then select " +
          "'Update' from the 'Options' menu above. Mandatory fields are shown in blue.</b></font>");
      }
      else if (PRO.getAction().equals("Add"))
      {
        PRO.setMode("Create");
        PRO.setMessage("<font color=blue><b>Mandatory fields are shown in blue</b></font>");
      }
      else if (PRO.getAction().equals("Delete"))
      {
        PRO.setMode("Delete");
        PRO.setMessage("<font color=blue><b>Select 'Delete' from the 'Options' menu above</b></font>");
      }
      %>
	<jsp:forward page="providerMaint.jsp"/>
      <%
    }
    else if (ButtonPressed.equals("Master Account Maintenance"))
    {
      MAC.Reset();
      MAC.setUserId((String)session.getAttribute("User_Id"));
      MAC.setProviderId(PRO.getProviderId());
      MAC.getProviderFromDB();
      MAC.setAction("Add");
      MAC.setMode("Create");
      MAC.setMessage("<font color=blue><b>Mandatory fields are shown in blue</b></font>");
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
        PRO.setParameter(FormField,request.getParameter(FormField));
      }
      if ((ButtonPressed.equals("Submit")) || (ButtonPressed.equals("Update")))
      {
        if (PRO.isValid(ButtonPressed))
        {
          PRO.setMode("Confirm");
          if (ButtonPressed.equals("Update"))
          {
            PRO.setMessage("<font color=blue><b>Select 'Confirm' from the " +
              "'Options' menu above to commit your updates or 'Cancel' to abort the operation</b></font>");
          }
          else
          {
            PRO.setMessage("<font color=blue><b>Select 'Confirm' from the " +
              "'Options' menu above to create this Provider or 'Cancel' to amend them</b></font>");
          }
        }
        %>
        <jsp:forward page="providerMaint.jsp"/>
        <%
      }
      else
      {//No button pressed, so must be a Refresh
        RSB.getLatestProcessControl();
        %>
        <jsp:forward page="providerMaint.jsp"/>
        <%
      }
    }
%>
</body>
</html>
