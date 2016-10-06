<html>
<head>
</head>
<%@ page import="DBUtilities.*,JavaUtil.StringUtil"%>

<jsp:useBean id="BAN" class="DBUtilities.BANBean" scope="session"/>

<%! JavaUtil.StringUtil SU = new JavaUtil.StringUtil();
    String ButtonPressed, Action, System, GlobalCustomerId, User_Staff_Number, Surname_Filter, CLI, Page;
%>
<%
    Page="AppliedCreditMenuHandler.jsp";
    session.setAttribute("PageSent",Page);
    ButtonPressed=(String)request.getParameter("ButtonPressed");
    Action=(String)request.getParameter("Action");
    BAN.setAction(Action);
    GlobalCustomerId=(String)request.getParameter("AMEX_Customer");
    BAN.setGlobalCustomerId(GlobalCustomerId);
    Surname_Filter=(String)request.getParameter("Surname_Filter");
    BAN.setSurname_Filter(Surname_Filter);
    CLI=(String)request.getParameter("CLI");
    BAN.setCLI(CLI);
    User_Staff_Number=(String)request.getParameter("AMEX_Staff_Number");
    BAN.setUser_Staff_Number(User_Staff_Number);
    System=(String)session.getAttribute("System");
    //Has an entry been selected?
    if ((ButtonPressed.startsWith("Select")))
    {
      %>
	<jsp:forward page="ViewCredit.jsp"/>
      <%
    }
    else
    {
      if ((ButtonPressed.startsWith("Clear")))
      {
        BAN.setGlobalCustomerId("");
        BAN.setGlobalCustomerName("");
        BAN.setSurname_Filter("");
        BAN.setCLI("");
        BAN.setUser_Staff_Number("");
      }
      %>
	<jsp:forward page="AppliedCreditMenu.jsp"/>
      <%
    }
    %>
</body>
</html>