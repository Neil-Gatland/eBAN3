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
    ButtonPressed=request.getParameter("ButtonPressed");
    System=(String)session.getAttribute("System");
    String accountType = request.getParameter("accountType");
    if (ButtonPressed.equals("Show All Accounts"))
    {
      accountType = "a";
    }
    else if (ButtonPressed.equals("Show Accounts With Invoices"))
    {
      accountType = "i";
    }

    if (ButtonPressed.startsWith("Desktop"))
    {
      %>
	<jsp:forward page="newDesktop.jsp"/>
      <%
    }
    else if (ButtonPressed.startsWith("list_"))
    {
      //BAN.setInvoice_Region(request.getParameter("iRId"));
      if (ButtonPressed.equals("list_BillProfile"))
      {
        BAN.setAccount_Id(request.getParameter("iRId"));
        %>
          <jsp:forward page="listAccounts.jsp">
           <jsp:param name="billProfile" value="true"/>
           <jsp:param name="accountType" value="<%=accountType%>"/>
          </jsp:forward>
        <%
      }
      else if (ButtonPressed.equals("list_ViewInvoice"))
      {
        BAN.setInvoiceNo(request.getParameter("iRId"));
        BAN.setAccountId(request.getParameter("accountId"));
        BAN.setAccountName(request.getParameter("accountName"));
        BAN.setTotalCharges(request.getParameter("total"));
        %>
          <jsp:forward page="listInvoiceReports.jsp?type=1"/>
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
        <jsp:forward page="listAccounts.jsp">
          <jsp:param name="query" value="true"/>
          <jsp:param name="fromPage" value="listAccounts"/>
          <jsp:param name="accountType" value="<%=accountType%>"/>
        </jsp:forward>
      <%
    }
    else
    {
      %>
	<jsp:forward page="listAccounts.jsp">
          <jsp:param name="accountType" value="<%=accountType%>"/>
        </jsp:forward>
      <%
    }
    %>
</body>
</html>
