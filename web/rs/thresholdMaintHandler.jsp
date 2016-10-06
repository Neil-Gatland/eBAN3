<html>
<head>
</head>
<body>
<%@ page import="JavaUtil.StringUtil"%>
<%@ page import="java.util.Enumeration"%>

<jsp:useBean id="BAN" class="DBUtilities.BANBean" scope="session"/>
<jsp:useBean id="HB" class="HTMLUtil.HTMLBean" scope="session"/>
<jsp:useBean id="TRB" class="DBUtilities.ThresholdBean" scope="session"/>
<jsp:useBean id="RSB" class="DBUtilities.RevenueShareBean" scope="session"/>
<%! String ButtonPressed, banId, listType;%>
<%
    Enumeration FormFields = request.getParameterNames();
    String FormField = null;
    session.setAttribute("formname","thresholdMaintHandler");
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
        <jsp:forward page="thresholdMaint.jsp">
          <jsp:param name="query" value="true"/>
          <jsp:param name="fromPage" value="thresholdMaint"/>
        </jsp:forward>
      <%
    }
    else if (ButtonPressed.equals("View All Processes"))
    {
      RSB.populateProcessList();
      %>
        <jsp:forward page="thresholdMaint.jsp">
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
    else if (ButtonPressed.equals("Reference Data Menu"))
    {
      %>
        <jsp:forward page="referenceDataMenu.jsp"/>
      <%
    }
    else if (ButtonPressed.equals("Refresh"))
    {
      RSB.getLatestProcessControl();
      String action = TRB.getAction();
      if (action.equals("Add"))
      {
        TRB.setMode("Create");
        TRB.setMessage("<font color=blue><b>Mandatory fields are shown in blue</b></font>");
      }
      else if (action.equals("Delete"))
      {
        TRB.getThresholdFromDB();
        TRB.setMode("Delete");
        TRB.setMessage("<font color=blue><b>Select 'Delete' from the 'Options' menu above</b></font>");
      }
      else if (action.equals("Amend"))
      {
        TRB.getThresholdFromDB();
        TRB.setMode("Amend");
        TRB.setMessage("<font color=blue><b>Amend as required and then select " +
          "'Update' from the 'Options' menu above. Mandatory fields are shown in blue.</b></font>");
      }
      %>
	<jsp:forward page="thresholdMaint.jsp"/>
      <%
    }
    else if (ButtonPressed.equals("Delete"))
    {
      TRB.setMode("Confirm");
      TRB.setMessage("<font color=blue><b>Select 'Confirm' from the " +
              "'Options' menu above to delete these values or 'Cancel' to abort the operation</b></font>");
      %>
	<jsp:forward page="thresholdMaint.jsp"/>
      <%
    }
    else if (ButtonPressed.equals("Confirm"))
    {
      String action = TRB.getAction();
      if (action.equals("Add"))
      {
        if (TRB.createThreshold())
        {
          RSB.setMessage(TRB.getMessage());
          %>
            <jsp:forward page="referenceDataMenu.jsp"/>
          <%
        }
        else
        {
          %>
            <jsp:forward page="thresholdMaint.jsp"/>
          <%
        }
      }
      else if (action.equals("Delete"))
      {
        if (TRB.deleteThreshold())
        {
          RSB.setMessage(TRB.getMessage());
          %>
            <jsp:forward page="referenceDataMenu.jsp"/>
          <%
        }
        else
        {
          %>
            <jsp:forward page="thresholdMaint.jsp"/>
          <%
        }
      }
      else if (action.equals("Amend"))
      {
        if (TRB.updateThreshold())
        {
          RSB.setMessage(TRB.getMessage());
          %>
            <jsp:forward page="referenceDataMenu.jsp"/>
          <%
        }
        else
        {
          %>
            <jsp:forward page="thresholdMaint.jsp"/>
          <%
        }
      }
    }
    else if (ButtonPressed.equals("Cancel"))
    {
      String action = TRB.getAction();
      if (action.equals("Add"))
      {
        TRB.setMode("Create");
        TRB.setMessage("<font color=blue><b>Mandatory fields are shown in blue</b></font>");
      }
      else if (action.equals("Delete"))
      {
        TRB.setMode("Delete");
        TRB.setMessage("<font color=blue><b>Select 'Delete' from the 'Options' menu above</b></font>");
      }
      else if (action.equals("Amend"))
      {
        TRB.setMode("Amend");
        TRB.setMessage("<font color=blue><b>Amend as required and then select " +
          "'Update' from the 'Options' menu above. Mandatory fields are shown in blue.</b></font>");
      }
      %>
	<jsp:forward page="thresholdMaint.jsp"/>
      <%
    }
    else
    {
      //store form values in Bean
      while (FormFields.hasMoreElements())
      {
        FormField=(String)FormFields.nextElement();
        TRB.setParameter(FormField,request.getParameter(FormField));
      }
      if ((ButtonPressed.equals("Submit")) || (ButtonPressed.equals("Update")))
      {
        if (TRB.isValid(ButtonPressed))
        {
          TRB.setMode("Confirm");
          if (ButtonPressed.equals("Update"))
          {
            TRB.setMessage("<font color=blue><b>Select 'Confirm' from the " +
              "'Options' menu above to commit your updates or 'Cancel' to abort the operation</b></font>");
          }
          else
          {
            TRB.setMessage("<font color=blue><b>Select 'Confirm' from the " +
              "'Options' menu above to create these entries or 'Cancel' to amend them</b></font>");
          }
        }
        %>
        <jsp:forward page="thresholdMaint.jsp"/>
        <%
      }
      else
      {//No button pressed, so must be a Refresh
        RSB.getLatestProcessControl();
        %>
        <jsp:forward page="thresholdMaint.jsp"/>
        <%
      }
    }
%>
</body>
</html>
