<html>
<head>
<%@ page import="JavaUtil.EBANProperties"%>
<jsp:useBean id="BAN" class="DBUtilities.BANBean" scope="request"/>

<link rel="stylesheet" type="text/css" href="../nr/cw/newcss/world_ie.css">
<title></title></head><body>
<form METHOD="POST" ACTION="http://<%=EBANProperties.getEBANProperty(EBANProperties.EDIFYSERVERNAME)%>/scripts/cgiclnt2.exe/JavaDesktop/ND000_">
<input type=hidden name="EWF_SYS_0" value="9c457cc7-f18f-4923-b765-13f897f9093d">
<input type=hidden name="EWF_FORM_NAME" value="EdifyLogon">
<p><input type=hidden name="EWF_HIDDEN_User_Id" value="<%=session.getAttribute("User_Id")%>">
<p><input type=hidden name="EWF_HIDDEN_Billing_Team" value="Global">
<p><input type=hidden name="EWF_HIDDEN_Type" value="<%=request.getParameter("type")%>">
</p><p>&nbsp;</p><p>&nbsp;</p><p>&nbsp;</p><p>
        <p class="cwstrap">Please wait.....Connecting to the Desktop</p>
</p></form>
<script language="JavaScript"><!--
setTimeout('document.forms(0).submit()',10);
//--></script>
<%session.invalidate();%>
</body>
</html>