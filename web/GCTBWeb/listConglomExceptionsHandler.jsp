<html>
<head>
</head>
<%@ page import="DBUtilities.*,JavaUtil.StringUtil"%>

<jsp:useBean id="BAN" class="DBUtilities.BANBean" scope="session"/>

<%! JavaUtil.StringUtil SU = new JavaUtil.StringUtil();
    String ButtonPressed,System;
%>
<%
    ButtonPressed=request.getParameter("ButtonPressed");
    System=(String)session.getAttribute("System");
    String fromBilling = request.getParameter("fromBilling");
    BAN.setExceptionTypeForList(request.getParameter("Conglom_Exception_Type"));
    BAN.setExceptionStatusForList(request.getParameter("Conglom_Exception_Status"));
    BAN.setPeriodForList(request.getParameter("Conglom_Period"));
    BAN.setInvoiceDocketNoForList(request.getParameter("Conglom_Inv_Doc"));
    BAN.setBilledProductForList(request.getParameter("Conglom_Billed_Product"));

    if (ButtonPressed.equals("Conglom Desktop"))
    {
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
    else if (ButtonPressed.equals("list_Update"))
    {
      BAN.setExceptionId(Integer.parseInt(request.getParameter("exId")));
      BAN.setInvoiceDocketNo(request.getParameter("invoiceDocketNo"));
      BAN.setExceptionStatus(request.getParameter("exStatus"));
      %>
        <jsp:forward page="updateException.jsp">
          <jsp:param name="conglom" value="true"/>
          <jsp:param name="fromBilling" value="<%=fromBilling%>"/>
        </jsp:forward>
      <%
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
        <jsp:forward page="listConglomExceptions.jsp">
          <jsp:param name="query" value="true"/>
          <jsp:param name="fromPage" value="listConglomExceptions"/>
          <jsp:param name="fromBilling" value="<%=fromBilling%>"/>
        </jsp:forward>
      <%
    }
    else if (ButtonPressed.startsWith("Reset"))
    {
      BAN.setExceptionTypeForList("All");
      BAN.setExceptionStatusForList("All");
      BAN.setPeriodForList("All");
      BAN.setInvoiceDocketNoForList("All");
      BAN.setBilledProductForList("All");
      %>
	<jsp:forward page="listConglomExceptions.jsp">
          <jsp:param name="fromBilling" value="<%=fromBilling%>"/>
        </jsp:forward>
      <%
    }
		else
    {//All other options are from the List requests menu
      //if (ButtonPressed.compareTo("")!=0){BAN.setBANs_to_List(ButtonPressed);}
      %>
	<jsp:forward page="listConglomExceptions.jsp">
          <jsp:param name="fromBilling" value="<%=fromBilling%>"/>
        </jsp:forward>
      <%
    }
    %>
</body>
</html>
