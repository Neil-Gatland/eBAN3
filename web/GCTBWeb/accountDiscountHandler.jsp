<html>
<head>
</head>
<%@ page import="JavaUtil.*"%>
<jsp:useBean id="BAN" class="DBUtilities.BANBean" scope="session"/>

<%!     String ButtonPressed="",Global_Customer_Id="",Account="",discountPlan="";
        JavaUtil.StringUtil SU = new JavaUtil.StringUtil();
        JavaUtil.UserData UD = new UserData();
%>
<%

    session.setAttribute("PageSent",request.getRequestURI());
    ButtonPressed=(String)request.getParameter("ButtonPressed");
    if (ButtonPressed.startsWith("Desktop"))
    {
      %>
	<jsp:forward page="newDesktop.jsp"/>
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
        <jsp:forward page="accountDiscount.jsp">
         <jsp:param name="query" value="true"/>
          <jsp:param name="fromPage" value="accountDiscount"/>
        </jsp:forward>
      <%
    }
		else if (ButtonPressed.equals("Reset"))
    {

	BAN.setGlobalCustomerId("");
	BAN.setAccount_Id("");
        BAN.setDiscountPlan("");
      %>
	<jsp:forward page="accountDiscount.jsp"/>
      <%
    }
		else
    {

      Global_Customer_Id=(String)request.getParameter("GCB_Customer2");
      Account=(String)request.getParameter("GCB_Account");
      discountPlan=(String)request.getParameter("Discount_Plan");
      if (!Global_Customer_Id.equals(BAN.getGlobalCustomerId()))
      {
	BAN.setGlobalCustomerId(SU.isNull(Global_Customer_Id,""));
	BAN.setAccount_Id("");
        BAN.setDiscountPlan("");
      }
      else if (!Account.equals(BAN.getAccount_Id()))
      {
        BAN.setAccount_Id(SU.isNull(request.getParameter("GCB_Account"),""));
        BAN.setAccountName(SU.isNull(request.getParameter("accountName"),""));
        BAN.setDiscountPlan("");
      }
      else if (!discountPlan.equals(BAN.getDiscountPlan()))
      {
        BAN.setDiscountPlan(SU.isNull((String)request.getParameter("Discount_Plan"),""));
      }

      if (ButtonPressed.equals("Create a Discount Plan"))
      {
        %>
          <jsp:forward page="accountDiscountPlan.jsp"/>
	<%
      }
      else if (ButtonPressed.startsWith("Amend a Discount Plan"))
      {
        %>
          <jsp:forward page="accountDiscountPlan.jsp"/>
	<%
      }
      else
      {//No button pressed, so must be a Refresh
        %>
          <jsp:forward page="accountDiscount.jsp"/>
        <%
      }
    }
      %>
</body>
</html>
