<html>
<head>
</head>
<%@ page import="JavaUtil.*"%>
<jsp:useBean id="BAN" class="DBUtilities.BANBean" scope="session"/>
<jsp:useBean id="DBA" class="DBUtilities.DBAccess" scope="session"/>
<jsp:useBean id="RSB" class="DBUtilities.RevenueShareBean" scope="session"/>
<jsp:useBean id="RAB" class="DBUtilities.RatingSchemeBean" scope="session"/>

<%!     String ButtonPressed="",ratingSchemeId="";
        JavaUtil.StringUtil SU = new JavaUtil.StringUtil();
        JavaUtil.UserData UD = new UserData();
%>
<%
    //Clear out BAN beans

    session.setAttribute("PageSent",request.getRequestURI());
    ButtonPressed=(String)request.getParameter("ButtonPressed");
    RSB.setMinCallMonth("000000");

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
        <jsp:forward page="ratingSchemeMenu.jsp"/>
      <%
    }
    else if (ButtonPressed.equals("View All Processes"))
    {
      RSB.populateProcessList();
      %>
        <jsp:forward page="ratingSchemeMenu.jsp">
         <jsp:param name="viewProcess" value="true"/>
        </jsp:forward>
      <%
    }
    else if (ButtonPressed.equals("list_RSUpdate"))
    {
      RAB.Reset();
      RAB.setUserId((String)session.getAttribute("User_Id"));
      RAB.setRatingSchemeId(request.getParameter("ratingSchemeId"));
      RAB.getRatingSchemeFromDB();
      RAB.setSignNameProviderIdAndAccountNumbers(RSB.getSignName(),
        RSB.getProviderId(), RSB.getMasterAccountNumber(),
        RSB.getAccountNumber(), true);
      RAB.setProductGroup(RSB.getProductGroup());
      RAB.setProductCode(RSB.getProductCode());
      RAB.setRatingType(RSB.getRatingType());
      RAB.setRatingDuration(RSB.getRatingDuration());
      RAB.setFromCallMonth(RSB.getFromCallMonth());
      RAB.setAction("Amend");
      RAB.setMode("Amend");
      RAB.setMessage("<font color=blue><b>Amend as required and then select " +
        "'Update' from the 'Options' menu above. Mandatory fields are shown in blue.</b></font>");
      %>
        <jsp:forward page="ratingSchemeMaint.jsp"/>
      <%
    }
    else if (ButtonPressed.equals("Create New Rating Scheme"))
    {
      if (RSB.getFromCallMonth().equals(""))
      {
        RSB.getMinCallMonthFromDB();
      }
      RAB.Reset();
      RAB.setSignNameProviderIdAndAccountNumbers(RSB.getSignName(),
        RSB.getProviderId(), RSB.getMasterAccountNumber(),
        RSB.getAccountNumber(), true);
      //RAB.setMasterAccountNumber(RSB.getMasterAccountNumber());
      RAB.setProductGroup(RSB.getProductGroup());
      RAB.setProductCode(RSB.getProductCode());
      RAB.setRatingType(RSB.getRatingType());
      RAB.setRatingDuration(RSB.getRatingDuration());
      RAB.setFromCallMonth(RSB.getFromCallMonth());
      RAB.setUserId((String)session.getAttribute("User_Id"));
      RAB.setAction("Add");
      RAB.setMode("Create");
      RAB.setMessage("<font color=blue><b>Enter values as required and then select " +
        "'Submit' from the 'Options' menu above. Mandatory fields are shown in blue.</b></font>");
      %>
        <jsp:forward page="ratingSchemeMaint.jsp"/>
      <%
    }
    else if (ButtonPressed.equals("Add Range to Rating Scheme"))
    {
      RAB.Reset();
      RAB.setUserId((String)session.getAttribute("User_Id"));
      RAB.setSignNameProviderIdAndAccountNumbers(RSB.getSignName(),
        RSB.getProviderId(), RSB.getMasterAccountNumber(),
        RSB.getAccountNumber(), true);
      //RAB.setMasterAccountNumber(RSB.getMasterAccountNumber());
      RAB.setProductGroup(RSB.getProductGroup());
      RAB.setProductCode(RSB.getProductCode());
      RAB.setRatingType(RSB.getRatingType());
      RAB.setRatingDuration(RSB.getRatingDuration());
      RAB.setFromCallMonth(RSB.getFromCallMonth());
      //RAB.getRatingSchemeFromDB();
      RAB.setRangeStart("0");
      RAB.setRangeEnd("0");
      RAB.setDayRate("0");
      RAB.setEveningRate("0");
      RAB.setWeekendRate("0");
      RAB.setAction("AddTo");
      //RAB.setMode("Create");
      //RAB.setAction("Amend");
      RAB.setMode("Amend");
      RAB.setMessage("<font color=blue><b>Enter values as required and then select " +
        "'Submit' from the 'Options' menu above. Mandatory fields are shown in blue.</b></font>");
      %>
        <jsp:forward page="ratingSchemeMaint.jsp"/>
      <%
    }
    else
    {
      //ratingSchemeId=request.getParameter("rsRatingScheme");
      //RSB.setRatingSchemeId(ratingSchemeId);
      //String signName = request.getParameter("signName");
      //RSB.setSignName(signName);
      //RSB.setProviderId(request.getParameter("providerId"));
      RSB.setSignNameProviderIdAndAccountNumbers(request.getParameter("signName"),
        request.getParameter("providerId"), request.getParameter("masterAccountNumber"),
        request.getParameter("accountNumber"), false);
      //RSB.setMasterAccountNumber(request.getParameter("masterAccountNumber"));
      if (!RSB.getProductGroup().equals(request.getParameter("productGroup")))
      {
        RSB.setProductGroup(request.getParameter("productGroup"));
        RSB.setProductCode("");
      }
      else
      {
        RSB.setProductCode(request.getParameter("productCode"));
      }
      RSB.setRatingType(request.getParameter("ratingType"));
      RSB.setFromCallMonth(request.getParameter("fromCallMonth"));
      RSB.setRatingDuration(request.getParameter("ratingDuration"));
      RSB.populateRatingSchemeList();
      if (ButtonPressed.equals("Raise Query"))
      {
        %>
          <jsp:forward page="ratingSchemeMenu.jsp">
            <jsp:param name="query" value="true"/>
            <jsp:param name="fromPage" value="ratingSchemeMenu"/>
          </jsp:forward>
        <%
      }
      else if (ButtonPressed.equals("Advance From Call Month For Rating Scheme"))
      {
        if (RSB.canAdvanceCallMonth())
        {
          RSB.setNewFromCallMonth("");
          RSB.getMinCallMonthFromDB();
          RSB.setUserId((String)session.getAttribute("User_Id"));
          %>
            <jsp:forward page="ratingSchemeMenu.jsp">
              <jsp:param name="advanceCallMonth" value="true"/>
              <jsp:param name="fromPage" value="ratingSchemeMenu"/>
            </jsp:forward>
          <%
        }
        else
        {
          RSB.setMessage("<font color=blue><b>This Rating Scheme starts with the highest month available</b></font>");
          %>
            <jsp:forward page="ratingSchemeMenu.jsp"/>
          <%
        }
      }
      else if (ButtonPressed.equals("Close Rating Scheme"))
      {
        RSB.setMessage("<font color=blue><b>Select 'Confirm' from the " +
          "'Options' menu above to close this Rating Scheme or 'Cancel' to abort the operation</b></font>");
        RSB.setMode("Confirm");
        %>
          <jsp:forward page="ratingSchemeMenu.jsp"/>
        <%
      }
      else if (ButtonPressed.equals("Confirm"))
      {
        RSB.getLatestProcessControl();
        RSB.closeRatingScheme();
        RSB.populateRatingSchemeList();
        %>
          <jsp:forward page="ratingSchemeMenu.jsp"/>
        <%
      }
      else
      {//No button pressed, so must be a Refresh
        RSB.getLatestProcessControl();
        %>
          <jsp:forward page="ratingSchemeMenu.jsp"/>
        <%
      }
    }
      %>
</body>
</html>
