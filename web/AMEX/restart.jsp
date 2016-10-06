<html>
<%
  String System=(String)session.getAttribute("System");
  session.invalidate();
%>
<jsp:forward page="Login.jsp">
  <jsp:param name="type" value="r"/>
  <jsp:param name="system" value="<%=System%>"/>
</jsp:forward>
</html>

