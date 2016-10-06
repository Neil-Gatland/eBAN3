<html>
<head>
</head>
<%@ page import="DBUtilities.*,JavaUtil.StringUtil"%>

<jsp:useBean id="BAN" class="DBUtilities.BANBean" scope="session"/>
<jsp:useBean id="ctBAN" class="DBUtilities.CircuitBANBean" scope="session"/>

<%! JavaUtil.StringUtil SU = new JavaUtil.StringUtil();
    String ButtonPressed, Action, System, Page;
    String GlobalCustomerId, C2_Ref, Service_Reference;
    %>
    <%
    Page="WelcomeHandler.jsp";
    session.setAttribute("PageSent",Page);
    ButtonPressed=(String)request.getParameter("ButtonPressed");
    Action=(String)request.getParameter("Action");
    BAN.setAction(Action);
    GlobalCustomerId=(String)request.getParameter("LLU_Customer");
    BAN.setGlobalCustomerId(GlobalCustomerId);
    C2_Ref=(String)request.getParameter("C2_Ref");
    BAN.setC2Ref(C2_Ref);
    Service_Reference=(String)request.getParameter("Service_Reference");
    BAN.setService_Reference(Service_Reference);
    System=(String)session.getAttribute("System");
    //Has an entry been selected?
    if (ButtonPressed.startsWith("Amend a Service"))
    {

      if (Service_Reference.length()==0)
      {
        %>
          <jsp:forward page="Welcome.jsp"/>
        <%
      }
      else
      {
        ctBAN.setCircuit_Reference(Service_Reference);

        %>
          <jsp:forward page="AmendService.jsp"/>
        <%
      }
    }
    else
    {
      if ((ButtonPressed.startsWith("Clear")))
      {
        BAN.setGlobalCustomerId("");
        BAN.setGlobalCustomerName("");
        BAN.setC2Ref("");
        BAN.setService_Reference("");
      }
      %>
	<jsp:forward page="Welcome.jsp"/>
      <%

    }
    %>
</body>
</html>