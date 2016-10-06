<html>
<head>
</head>
<%@ page import="DBUtilities.*,JavaUtil.StringUtil"%>

<jsp:useBean id="BAN" class="DBUtilities.BANBean" scope="session"/>

<%! JavaUtil.StringUtil SU = new JavaUtil.StringUtil();
    String ButtonPressed,System;
%>
<%
    boolean isConglom = request.getParameter("conglom")!=null;
    ButtonPressed=request.getParameter("ButtonPressed");
    String fromBilling = isConglom?request.getParameter("fromBilling"):"false";
    System=(String)session.getAttribute("System");

    if (ButtonPressed.equals((isConglom?"Conglom ":"")+"Desktop"))
    {
      if (isConglom)
      {
        %>
          <jsp:forward page="conglomDesktop.jsp"/>
        <%
      }
      else
      {
        %>
          <jsp:forward page="newDesktop.jsp"/>
        <%
      }
    }
    else if (ButtonPressed.equals("Conglom Billing Menu"))
    {
      %>
	<jsp:forward page="conglomBillingMenu.jsp"/>
      <%
    }
    else if ((ButtonPressed.equals("Cancel")) ||
      (ButtonPressed.equals("Exception List")))
    {
      if (isConglom)
      {
        %>
          <jsp:forward page="listConglomExceptions.jsp">
            <jsp:param name="fromBilling" value="<%=fromBilling%>"/>
          </jsp:forward>
        <%
      }
      else
      {
        %>
          <jsp:forward page="ListExceptions.jsp"/>
        <%
      }
    }
    else if (ButtonPressed.equals("Update"))
    {
      BAN.updateException(request.getParameter((isConglom?"Conglom_":"") +
        "Exception_Status2"), request.getParameter("exceptionNote"),
        (String)session.getAttribute("User_Id"), isConglom);
      if (isConglom)
      {
        %>
          <jsp:forward page="listConglomExceptions.jsp">
            <jsp:param name="fromBilling" value="<%=fromBilling%>"/>
          </jsp:forward>
        <%
      }
      else
      {
        %>
          <jsp:forward page="ListExceptions.jsp"/>
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
      if (isConglom)
      {
        %>
          <jsp:forward page="updateException.jsp">
            <jsp:param name="query" value="true"/>
            <jsp:param name="fromPage" value="updateException"/>
            <jsp:param name="conglom" value="true"/>
            <jsp:param name="fromBilling" value="<%=fromBilling%>"/>
          </jsp:forward>
        <%
      }
      else
      {
        %>
          <jsp:forward page="updateException.jsp">
            <jsp:param name="query" value="true"/>
            <jsp:param name="fromPage" value="updateException"/>
          </jsp:forward>
        <%
      }
    }
    {//All other options are from the List requests menu
      //if (ButtonPressed.compareTo("")!=0){BAN.setBANs_to_List(ButtonPressed);}
      if (isConglom)
      {
        %>
          <jsp:forward page="updateException.jsp">
            <jsp:param name="conglom" value="true"/>
            <jsp:param name="fromBilling" value="<%=fromBilling%>"/>
          </jsp:forward>
        <%
      }
      else
      {
        %>
          <jsp:forward page="updateException.jsp"/>
        <%
      }
    }
    %>
</body>
</html>