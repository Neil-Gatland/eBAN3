<html>
<head>
</head>
<%@ page import="DBUtilities.*,JavaUtil.StringUtil"%>

<jsp:useBean id="BAN" class="DBUtilities.BANBean" scope="session"/>
<jsp:useBean id="adjBAN" class="DBUtilities.ConglomAdjustmentBANBean" scope="session"/>

<%! JavaUtil.StringUtil SU = new JavaUtil.StringUtil();
    String ButtonPressed,System;
%>
<%
    ButtonPressed=request.getParameter("ButtonPressed");
    System=(String)session.getAttribute("System");
    BAN.setBilledProductForList(request.getParameter("conglomBilledProduct2"));

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
      adjBAN.Reset();
      adjBAN.setAdjustmentId(Integer.parseInt(request.getParameter("adjustmentId")));
      adjBAN.getAdjustment();
      adjBAN.setAction("Amend");
      adjBAN.setMode("Amend");
      adjBAN.setMessage("<font color=blue><b>Amend as required and then select " +
        "'Update' from the 'Options' menu above. Mandatory fields are shown in blue.</b></font>");
      adjBAN.setUserId((String)session.getAttribute("User_Name"));
      adjBAN.setConglomCustomerId(BAN.getConglomCustomerId());
      %>
        <jsp:forward page="conglomAdjustment.jsp"/>
      <%
    }
    else if (ButtonPressed.equals("list_Delete"))
    {
      adjBAN.Reset();
      adjBAN.setAdjustmentId(Integer.parseInt(request.getParameter("adjustmentId")));
      adjBAN.getAdjustment();
      adjBAN.setAction("Delete");
      adjBAN.setMode("Delete");
      adjBAN.setMessage("<font color=blue><b>Select 'Delete' from the 'Options' menu above</b></font>");
      adjBAN.setUserId((String)session.getAttribute("User_Name"));
      adjBAN.setConglomCustomerId(BAN.getConglomCustomerId());
      %>
        <jsp:forward page="conglomAdjustment.jsp"/>
      <%
    }
    else if (ButtonPressed.equals("Add"))
    {
      //BAN.setExceptionId(Integer.parseInt(request.getParameter("adjustmentId")));
      adjBAN.Reset();
      adjBAN.setAction("Add");
      adjBAN.setMode("Create");
      adjBAN.setUserId((String)session.getAttribute("User_Name"));
      adjBAN.setConglomCustomerId(BAN.getConglomCustomerId());
      adjBAN.setBilledProduct(BAN.getBilledProductForList().equals("All")?"":
        BAN.getBilledProductForList());
      %>
        <jsp:forward page="conglomAdjustment.jsp"/>
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
        <jsp:forward page="listConglomAdjustments.jsp">
         <jsp:param name="query" value="true"/>
          <jsp:param name="fromPage" value="listConglomAdjustments"/>
        </jsp:forward>
      <%
    }
    else if (ButtonPressed.startsWith("Reset"))
    {
      BAN.setBilledProductForList("All");
      %>
	<jsp:forward page="listConglomAdjustments.jsp"/>
      <%
    }
		else
    {//All other options are from the List requests menu
      //if (ButtonPressed.compareTo("")!=0){BAN.setBANs_to_List(ButtonPressed);}
      %>
	<jsp:forward page="listConglomAdjustments.jsp"/>
      <%
    }
    %>
</body>
</html>
