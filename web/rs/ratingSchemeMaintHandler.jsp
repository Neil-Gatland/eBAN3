<html>
<head>
</head>
<body>
<%@ page import="JavaUtil.StringUtil"%>
<%@ page import="java.util.Enumeration"%>

<jsp:useBean id="BAN" class="DBUtilities.BANBean" scope="session"/>
<jsp:useBean id="HB" class="HTMLUtil.HTMLBean" scope="session"/>
<jsp:useBean id="RAB" class="DBUtilities.RatingSchemeBean" scope="session"/>
<jsp:useBean id="RSB" class="DBUtilities.RevenueShareBean" scope="session"/>
<%! String ButtonPressed, banId, listType;%>
<%
    Enumeration FormFields = request.getParameterNames();
    String FormField = null;
    session.setAttribute("formname","ratingSchemeMaintHandler");
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
        <jsp:forward page="ratingSchemeMaint.jsp">
          <jsp:param name="query" value="true"/>
          <jsp:param name="fromPage" value="ratingSchemeMaint"/>
        </jsp:forward>
      <%
    }
    else if (ButtonPressed.equals("View All Processes"))
    {
      RSB.populateProcessList();
      %>
        <jsp:forward page="ratingSchemeMaint.jsp">
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
    else if (ButtonPressed.equals("Rating Scheme"))
    {
      %>
        <jsp:forward page="ratingSchemeMenu.jsp"/>
      <%
    }
    else if (ButtonPressed.equals("Refresh"))
    {
      RSB.getLatestProcessControl();
      String action = RAB.getAction();
      String rsId = RAB.getRatingSchemeId();
      if (action.equals("AddTo"))
      {
        RAB.setRangeStart("0");
        RAB.setRangeEnd("0");
        RAB.setDayRate("0");
        RAB.setEveningRate("0");
        RAB.setWeekendRate("0");
        RAB.setMode("Amend");
        RAB.setMessage("<font color=blue><b>Enter values as required and then select " +
          "'Submit' from the 'Options' menu above. Mandatory fields are shown in blue.</b></font>");
      }
      else
      {
        RAB.Reset();
        RAB.setAction(action);
        if (action.equals("Add"))
        {
          RAB.setMode("Create");
          RAB.setMessage("<font color=blue><b>Mandatory fields are shown in blue</b></font>");
        }
        else if (action.equals("Delete"))
        {
          RAB.setRatingSchemeId(rsId);
          RAB.getRatingSchemeFromDB();
          RAB.setMode("Delete");
          RAB.setMessage("<font color=blue><b>Select 'Delete' from the 'Options' menu above</b></font>");
        }
        else if (action.equals("Amend"))
        {
          RAB.setRatingSchemeId(rsId);
          RAB.getRatingSchemeFromDB();
          RAB.setMode("Amend");
          RAB.setMessage("<font color=blue><b>Amend as required and then select " +
            "'Update' from the 'Options' menu above. Mandatory fields are shown in blue.</b></font>");
        }
      }
      %>
	<jsp:forward page="ratingSchemeMaint.jsp"/>
      <%
    }
    else if (ButtonPressed.equals("Delete"))
    {
      RAB.setMode("Confirm");
      RAB.setMessage("<font color=blue><b>Select 'Confirm' from the " +
              "'Options' menu above to delete this Product or 'Cancel' to abort the operation</b></font>");
      %>
	<jsp:forward page="ratingSchemeMaint.jsp"/>
      <%
    }
    else if (ButtonPressed.equals("Confirm"))
    {
      String action = RAB.getAction();
      if ((action.equals("Add")) || (action.equals("AddTo")))
      {
        if (RAB.createRatingScheme())
        {
          RSB.setMessage(RAB.getMessage());
          RSB.setSignNameProviderIdAndAccountNumbers(RAB.getSignName(),
            RAB.getProviderId(), RAB.getMasterAccountNumber(),
            RAB.getAccountNumber(), true);
          //RSB.setMasterAccountNumber(request.getParameter("masterAccountNumber"));
          RSB.setProductGroup(RAB.getProductGroup());
          RSB.setProductCode(RAB.getProductCode());
          RSB.setRatingType(RAB.getRatingType());
          RSB.setFromCallMonth(RAB.getFromCallMonth());
          RSB.setRatingDuration(RAB.getRatingDuration());
          RSB.populateRatingSchemeList();
          %>
            <jsp:forward page="ratingSchemeMenu.jsp"/>
          <%
        }
        else
        {
          %>
            <jsp:forward page="ratingSchemeMaint.jsp"/>
          <%
        }
      }
      /*else if (action.equals("Delete"))
      {
        if (RAB.deleteRatingScheme())
        {
          RSB.setMessage(RAB.getMessage());

            <jsp:forward page="ratingSchemeMenu.jsp"/>

        }
        else
        {

            <jsp:forward page="ratingSchemeMaint.jsp"/>

        }
      }*/
      else if (action.equals("Amend"))
      {
        if (RAB.updateRatingScheme())
        {
          RSB.populateRatingSchemeList();
          RSB.setMessage(RAB.getMessage());
          %>
            <jsp:forward page="ratingSchemeMenu.jsp"/>
          <%
        }
        else
        {
          %>
            <jsp:forward page="ratingSchemeMaint.jsp"/>
          <%
        }
      }
    }
    else if (ButtonPressed.equals("Cancel"))
    {
      %>
	<jsp:forward page="ratingSchemeMenu.jsp"/>
      <%
    }
    else
    {
      //store form values in Bean
      while (FormFields.hasMoreElements())
      {
        FormField=(String)FormFields.nextElement();
        if (!FormField.equals("masterAccountNumber") && !FormField.equals("accountNumber") &&
          !FormField.equals("providerId") && !FormField.equals("signName"))
        {
          RAB.setParameter(FormField,request.getParameter(FormField));
        }
      }
      if ((RAB.getAction().equals("Add"))/* && (RAB.getMode().equals("Create"))*/)
      {
        RAB.setSignNameProviderIdAndAccountNumbers(request.getParameter("signName"),
          request.getParameter("providerId"), request.getParameter("masterAccountNumber"),
          request.getParameter("accountNumber"), false);
      }
      if ((ButtonPressed.equals("Submit")) || (ButtonPressed.equals("Update")))
      {
        if (RAB.isValid(ButtonPressed))
        {
          RAB.setMode("Confirm");
          if (ButtonPressed.equals("Update"))
          {
            RAB.setMessage("<font color=blue><b>Select 'Confirm' from the " +
              "'Options' menu above to commit your updates or 'Cancel' to abort the operation</b></font>");
          }
          else
          {
            RAB.setMessage("<font color=blue><b>Select 'Confirm' from the " +
              "'Options' menu above to create this Rating Scheme Range or 'Cancel' to amend them</b></font>");
          }
        }
        %>
        <jsp:forward page="ratingSchemeMaint.jsp"/>
        <%
      }
      else
      {//No button pressed, so must be a Refresh
        RSB.getLatestProcessControl();
        %>
        <jsp:forward page="ratingSchemeMaint.jsp"/>
        <%
      }
    }
%>
</body>
</html>
