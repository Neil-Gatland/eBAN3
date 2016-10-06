<html>
<head>
</head>
<%@ page import="DBUtilities.*,JavaUtil.StringUtil"%>

<jsp:useBean id="BAN" class="DBUtilities.BANBean" scope="session"/>

<%! JavaUtil.StringUtil SU = new JavaUtil.StringUtil();
    String ButtonPressed, Action, System, Page;
    String Account_Id, Invoice_Total;
    %>
    <%
    Page="WelcomeHandler.jsp";
    session.setAttribute("PageSent",Page);
    ButtonPressed=(String)request.getParameter("ButtonPressed");
    Action=(String)request.getParameter("Action");
    BAN.setAction(Action);
    Account_Id=(String)request.getParameter("Account_Id");
    BAN.setAccountId(Account_Id);
    Invoice_Total=(String)request.getParameter("Invoice_Total");
    BAN.setAccountId(Invoice_Total);    
    System=(String)session.getAttribute("System");
    //Has an entry been selected?
    if (ButtonPressed.startsWith("Amend a Service"))
    {

      
    }
    else
    {
      if ((ButtonPressed.startsWith("Clear")))
      {
        BAN.setInvoiceTotal("");
        BAN.setAccountId("");
      }
      %>
	<jsp:forward page="Welcome.jsp"/>
      <%

    }
    %>
</body>
</html>