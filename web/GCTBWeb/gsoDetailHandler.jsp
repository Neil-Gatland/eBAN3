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
    BAN.setMessage("");
    ButtonPressed = request.getParameter("ButtonPressed");
    String type = request.getParameter("type");
    if (ButtonPressed.equals("Log Off"))
    {
      %>
	<jsp:forward page="Login.jsp?type=l"/>
      <%
    }
    else if (ButtonPressed.equals("Show Rejects"))
    {
      %>
	<jsp:forward page="gsoDetail.jsp?type=reject"/>
      <%
    }
    else if (ButtonPressed.equals("GSO Desktop"))
    {
      %>
	<jsp:forward page="gsoDesktop.jsp"/>
      <%
    }
    else if (ButtonPressed.equals("Show Requests"))
    {
      %>
        <jsp:forward page="gsoDetail.jsp?type=request"/>
      <%
    }
    else if (ButtonPressed.equals("Show Exceptions"))
    {
      %>
        <jsp:forward page="gsoDetail.jsp?type=exception"/>
      <%
    }
    else if (ButtonPressed.equals("Show Billing Log"))
    {
      BAN.setGSOJobId("");
      BAN.setGSOMessageDate("");
      %>
        <jsp:forward page="gsoDetail.jsp?type=log"/>
      <%
    }
    else if (ButtonPressed.equals("Raise Query"))
    {
      %>
        <jsp:forward page="gsoDetail.jsp">
          <jsp:param name="query" value="true"/>
          <jsp:param name="gso" value="true"/>
          <jsp:param name="fromPage" value="gsoDetail"/>
          <jsp:param name="type" value="<%=type%>"/>
        </jsp:forward>
      <%
    }
    else if (ButtonPressed.startsWith("list_"))
    {
      String rowId = request.getParameter("rowId");
      if (ButtonPressed.equals("list_Detail"))
      {
        %>
          <jsp:forward page="gsoDetail.jsp">
           <jsp:param name="gsoRejectDetail" value="true"/>
           <jsp:param name="rowId" value="<%=rowId%>"/>
           <jsp:param name="type" value="<%=type%>"/>
          </jsp:forward>
        <%
      }
    }
    else //refresh
    {
      if (type.equals("log"))
      {
        BAN.setGSOJobId(request.getParameter("gsoJobId"));
        BAN.setGSOMessageDate(request.getParameter("gsoMessageDate"));
      }
      %>
	<jsp:forward page="gsoDetail.jsp">
          <jsp:param name="type" value="<%=type%>"/>
        </jsp:forward>
      <%
    }
%>
</body>
</html>
