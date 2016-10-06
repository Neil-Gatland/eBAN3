<html>
<head>
</head>
<body>
<%@ page import="JavaUtil.StringUtil"%>
<%@ page import="java.net.URLEncoder"%>
<jsp:useBean id="BAN" class="DBUtilities.BANBean" scope="session"/>
<jsp:useBean id="HB" class="HTMLUtil.HTMLBean" scope="session"/>
<%! String ButtonPressed, banId, listType;%>
<%
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
        <jsp:forward page="gsoDesktop.jsp">
          <jsp:param name="query" value="true"/>
          <jsp:param name="gso" value="true"/>
          <jsp:param name="fromPage" value="gsoDesktop"/>
        </jsp:forward>
      <%
    }
    else if (ButtonPressed.equals("Change Password"))
    {
      %>
        <jsp:forward page="gsoDesktop.jsp">
         <jsp:param name="changePass" value="true"/>
        </jsp:forward>
      <%
    }
    else if (ButtonPressed.startsWith("list_"))
    {
      String spId = request.getParameter("spId");
      String spName = request.getParameter("spName");
      BAN.setServicePartnerName(spName);
      BAN.setServicePartnerId(Long.parseLong(spId));
      BAN.getGSOServicePartnerStatus();
      //BAN.setGlobalCustomerId(gcId);
      if (ButtonPressed.equals("list_AdHoc"))
      {
        String invoiceCurrency = BAN.getGSOInvoiceCurrency(spName);
        %>
          <jsp:forward page="gsoDesktop.jsp">
            <jsp:param name="createGSOInvoice" value="true"/>
            <jsp:param name="spId" value="<%=spId%>"/>
            <jsp:param name="spName" value="<%=URLEncoder.encode(spName)%>"/>
            <jsp:param name="invoiceCurrency" value="<%=invoiceCurrency%>"/>
          </jsp:forward>
        <%
      }
      else if (ButtonPressed.equals("list_Detail"))
      {
        %>
          <jsp:forward page="gsoDetail.jsp?type=request"/>
        <%
      }
      else if (ButtonPressed.equals("list_Exceptions"))
      {
        if (BAN.getGSOExceptions().equals("0"))
        {
          BAN.setMessage("<font color=blue>" + spName + " has no " +
            "exceptions to display.</font>");
          %>
            <jsp:forward page="gsoDesktop.jsp"/>
          <%
        }
        else
        {
          %>
            <jsp:forward page="gsoDetail.jsp?type=exception"/>
          <%
        }
      }
      else if (ButtonPressed.equals("list_Submit"))
      {
        %>
          <jsp:forward page="gsoDesktop.jsp">
            <jsp:param name="submitGSOBilling" value="true"/>
          </jsp:forward>
        <%
      }
      else if (ButtonPressed.equals("list_ViewBillLog"))
      {
        BAN.setGSOJobId("");
        BAN.setGSOMessageDate("");
        %>
          <jsp:forward page="gsoDetail.jsp?type=log"/>
        <%
      }
    }
    else //refresh
    {
      %>
	<jsp:forward page="gsoDesktop.jsp"/>
      <%
    }
%>
</body>
</html>
