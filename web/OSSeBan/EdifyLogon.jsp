<html>
<head>
<link rel="stylesheet" type="text/css" href="../nr/cw/newcss/world_ie.css">
<title></title></head><body>
<form METHOD="POST" ACTION="http://127.0.0.1/scripts/cgiclnt.exe/OSS%20Desktop/ND000_">
<input type=hidden name="EWF_SYS_0" value="F305AA46-2A0F-11D7-A538-0080AD09CEF7">
<input type=hidden name="EWF_FORM_NAME" value="EdifyLogon">
<p><input type=hidden name="EWF_HIDDEN_Logon_Id" value="<%=session.getAttribute("User_Id")%>">
</p><p>&nbsp;</p><p>&nbsp;</p><p>&nbsp;</p><p>
        <p class="cwstrap">Please wait.....Connecting to the Desktop</p>
</p></form>
<script language="JavaScript"><!--
setTimeout('document.forms(0).submit()',10);
//--></script>
<%session.invalidate();%>
</body>
</html>