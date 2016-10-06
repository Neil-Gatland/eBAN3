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
    else if (ButtonPressed.equals("Maintain Bill Prod"))
    {
      %>
	<jsp:forward page="listConglomBillProd.jsp"/>
      <%
    }
    else if (ButtonPressed.equals("list_Update"))
    {
      String interimNo = request.getParameter("itemId");
      String product = request.getParameter("product");
      String sourceAccount = request.getParameter("sourceAccount");
      %>
        <jsp:forward page="listConglomCRDocket.jsp">
         <jsp:param name="crDocket" value="true"/>
          <jsp:param name="interimNo" value="<%=interimNo%>"/>
          <jsp:param name="product" value="<%=product%>"/>
          <jsp:param name="sourceAccount" value="<%=sourceAccount%>"/>
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
        <jsp:forward page="listConglomCRDocket.jsp">
         <jsp:param name="query" value="true"/>
          <jsp:param name="fromPage" value="listConglomCRDocket"/>
        </jsp:forward>
      <%
    }
    else if (ButtonPressed.startsWith("Reset"))
    {
      BAN.setBilledProductForList("All");
      %>
	<jsp:forward page="listConglomCRDocket.jsp"/>
      <%
    }
		else
    {//All other options are from the List requests menu
      //if (ButtonPressed.compareTo("")!=0){BAN.setBANs_to_List(ButtonPressed);}
      %>
	<jsp:forward page="listConglomCRDocket.jsp"/>
      <%
    }
    %>
</body>
</html>
