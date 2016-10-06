<html>
<%@ page import="DBUtilities.DBAccess"%>
<%
  DBAccess DBA = new DBAccess();
  String Month_for_List = null;
%>
<%=DBA.getListBox("List_Month2","submit",Month_for_List,"",1,"style=\"width:196px\"",true)%>

</html>


