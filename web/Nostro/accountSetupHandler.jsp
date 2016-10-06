<html>
<head>
</head>
<%@ page import="JavaUtil.*"%>
<jsp:useBean id="BAN" class="DBUtilities.BANBean" scope="session"/>
<jsp:useBean id="chBAN" class="DBUtilities.OSSChargeBANBean" scope="session"/>
<jsp:useBean id="ctBAN" class="DBUtilities.CircuitBANBean" scope="session"/>
<jsp:useBean id="cuBAN" class="DBUtilities.OSSCustomerBANBean" scope="session"/>
<jsp:useBean id="caBAN" class="DBUtilities.CarrierBANBean" scope="session"/>

<%!     String ButtonPressed="",Circuit="",Global_Customer_Id="",Account="";
        JavaUtil.StringUtil SU = new JavaUtil.StringUtil();
        JavaUtil.UserData UD = new UserData();
%>
<%
    //Clear out BAN beans
    chBAN.Reset();
    ctBAN.Reset();
    cuBAN.Reset();
    session.setAttribute("PageSent",request.getRequestURI());
    ButtonPressed=(String)request.getParameter("ButtonPressed");

    if (ButtonPressed.equals("Page 1 of 3"))
    {
      %>
	<jsp:forward page="accountSetup.jsp"/>
      <%
    }
    else if (ButtonPressed.equals("Page 2 of 3"))
    {
      %>
	<jsp:forward page="accountSetup2.jsp"/>
      <%
    }
    else if (ButtonPressed.equals("Page 3 of 3"))
    {
      %>
	<jsp:forward page="accountSetup3.jsp"/>
      <%
    }
    else
    {//No button pressed, so must be a Refresh
      %>
        <jsp:forward page="CustomerBANMenu.jsp"/>
      <%
    }
    %>
</body>
</html>
