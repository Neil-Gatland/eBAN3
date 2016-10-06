<html>
<head>
</head>
<%@ page import="DBUtilities.*,JavaUtil.StringUtil"%>
<%@ page import="java.util.Collection"%>

<jsp:useBean id="BAN" class="DBUtilities.BANBean" scope="session"/>

<%! JavaUtil.StringUtil SU = new JavaUtil.StringUtil();
    String ButtonPressed,System;
%>
<%
    String type = request.getParameter("type");
    ButtonPressed=request.getParameter("ButtonPressed");
    System=(String)session.getAttribute("System");
    if (ButtonPressed.equals("Desktop"))
    {
      %>
	<jsp:forward page="newDesktop.jsp"/>
      <%
    }
    else if (ButtonPressed.equals("Conglom Desktop"))
    {
      BAN.setRefreshCustomerGrid(true);
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
    else if (ButtonPressed.equals("Accounts"))
    {
      %>
        <jsp:forward page="listAccounts.jsp?accountType=i"/>
      <%
    }
    else if (ButtonPressed.equals("Ad Hoc"))
    {
      %>
        <jsp:forward page="adHocInvoice.jsp"/>
      <%
    }
    else if (ButtonPressed.startsWith("list_"))
    {
      if (ButtonPressed.equals("list_ViewReport"))
      {
        //BAN.setAccount_Id(request.getParameter("iRId"));
        String fileName = request.getParameter("fileName");
        %>
          <jsp:forward page="listInvoiceReports.jsp">
            <jsp:param name="viewPDF" value="true"/>
            <jsp:param name="fileName" value="<%=fileName%>"/>
            <jsp:param name="type" value="<%=type%>"/>
          </jsp:forward>
        <%
      }
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
        <jsp:forward page="listInvoiceReports.jsp">
          <jsp:param name="query" value="true"/>
          <jsp:param name="fromPage" value="listInvoiceReports"/>
            <jsp:param name="type" value="<%=type%>"/>
        </jsp:forward>
      <%
    }
    else
    {
      %>
        <jsp:forward page="listInvoiceReports.jsp">
          <jsp:param name="type" value="<%=type%>"/>
        </jsp:forward>
      <%
    }
    %>
</body>
</html>
