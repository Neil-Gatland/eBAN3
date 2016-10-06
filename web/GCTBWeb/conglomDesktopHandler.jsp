<html>
<head>
</head>
<body>
<%@ page import="JavaUtil.StringUtil"%>
<%@ page import="java.net.URLEncoder"%>
<jsp:useBean id="BAN" class="DBUtilities.BANBean" scope="session"/>
<jsp:useBean id="HB" class="HTMLUtil.HTMLBean" scope="session"/>
<jsp:useBean id="ccBAN" class="DBUtilities.ConglomCustomerBANBean" scope="session"/>
<%! String ButtonPressed, banId, listType;%>
<%
    session.setAttribute("formname","conglomDesktopHandler");
    ButtonPressed = request.getParameter("ButtonPressed");
    BAN.setRefreshCustomerGrid(false);
    BAN.setActAsLogon(request.getParameter("Act_As_Logon"));
    BAN.setDesktopSortOrder(request.getParameter("sortOrder"));
    if (ButtonPressed.equals("Log Off"))
    {
      %>
	<jsp:forward page="Login.jsp?type=l"/>
      <%
    }
    else if (ButtonPressed.equals("Analyst Search"))
    {
      %>
        <jsp:forward page="conglomDesktop.jsp">
          <jsp:param name="analyst" value="true"/>
          <jsp:param name="conglom" value="true"/>
        </jsp:forward>
      <%
    }
    else if (ButtonPressed.equals("Raise Query"))
    {
      %>
        <jsp:forward page="conglomDesktop.jsp">
          <jsp:param name="query" value="true"/>
          <jsp:param name="fromPage" value="conglomDesktop"/>
        </jsp:forward>
      <%
    }
    else if (ButtonPressed.equals("Add Customer"))
    {
      ccBAN.Reset();
      //ccBAN.setConglomCustomerId(BAN.getConglomCustomerId());
      //ccBAN.getCustomer();
      //ccBAN.checkTrialInvoice();
      ccBAN.setAction("Add");
      ccBAN.setMode("Create");
      ccBAN.setUserId((String)session.getAttribute("User_Id"));
      //ccBAN.setUserId(BAN.getActAsLogon());
      %>
        <jsp:forward page="conglomCustMaint.jsp"/>
      <%
    }
    else if (ButtonPressed.equals("Check Queue"))
    {
      String grid = BAN.getConglomJobQueue();
      if (!grid.startsWith("<table"))
      {
      %>
        <jsp:forward page="conglomDesktop.jsp"/>
      <%
      }
      else
      {
      %>
        <jsp:forward page="conglomDesktop.jsp">
         <jsp:param name="queue" value="true"/>
         <jsp:param name="conglom" value="true"/>
        </jsp:forward>
      <%
      }
    }
    else if (ButtonPressed.startsWith("list_"))
    {
      //String conglomCustomerId = request.getParameter("gcId");
      BAN.setConglomCustomerId(Long.parseLong(request.getParameter("gcId")));
      BAN.getConglomCustomerSummary();
      if (ButtonPressed.equals("list_P"))
      {
        %>
          <jsp:forward page="conglomProduct.jsp"/>
        <%
      }
      else if (ButtonPressed.equals("list_B"))
      {
        //<jsp:forward page="desktopLogon.jsp"/>
        /*session.setAttribute("conglomGCID", BAN.getGlobalCustomerId());
        session.setAttribute("conglomCustName", BAN.getConglomCustomerName());
        session.setAttribute("conglomType", "ConglomB");
        session.setAttribute("actAsLogon", BAN.getActAsLogon());*/
        %>
          <jsp:forward page="conglomBillingMenu.jsp"/>
        <%
      }
      else if (ButtonPressed.equals("list_E"))
      {
        BAN.setExceptionTypeForList("All");
        BAN.setExceptionStatusForList("All");
        BAN.setPeriodForList("All");
        BAN.setInvoiceDocketNoForList("All");
        BAN.setBilledProductForList("All");
        %>
          <jsp:forward page="listConglomExceptions.jsp?firstTime=true&fromBilling=false"/>
        <%
      }
      else if (ButtonPressed.equals("list_S"))
      {
        //<jsp:forward page="desktopLogon.jsp"/>
        /*session.setAttribute("conglomGCID", BAN.getGlobalCustomerId());
        session.setAttribute("conglomCustName", BAN.getConglomCustomerName());
        session.setAttribute("conglomType", "ConglomB");
        session.setAttribute("actAsLogon", BAN.getActAsLogon());*/
        %>
          <jsp:forward page="conglomBillingMenu.jsp"/>
        <%
      }
      else if (ButtonPressed.equals("list_V"))
      {
        String grid = BAN.getLogMessages(true);
        if (!grid.startsWith("<table"))
        {
        %>
          <jsp:forward page="conglomDesktop.jsp"/>
        <%
        }
        else
        {
        %>
          <jsp:forward page="conglomDesktop.jsp">
           <jsp:param name="mblog" value="true"/>
           <jsp:param name="conglom" value="true"/>
          </jsp:forward>
        <%
        }
      }
      else if (ButtonPressed.equals("list_Pr"))
      {
        %>
          <jsp:forward page="conglomDesktop.jsp">
           <jsp:param name="billProfile" value="true"/>
           <jsp:param name="conglom" value="true"/>
          </jsp:forward>
        <%
      }
    }
    else //refresh
    {
      BAN.setRefreshCustomerGrid(true);
      %>
	<jsp:forward page="conglomDesktop.jsp"/>
      <%
    }
%>
</body>
</html>
