<SCRIPT language="JavaScript">
function sendSelected(processSeqNo, selectedOption)
{
  parent.document.forms.<%=(String)session.getAttribute("formname")%>.processSeqNo.value=processSeqNo;
  parent.document.forms.<%=(String)session.getAttribute("formname")%>.fromSelf.value="drill";
  parent.document.forms.<%=(String)session.getAttribute("formname")%>.submit();
}
</SCRIPT>
<link rel="stylesheet" type="text/css" href="../nr/cw/newcss/world_ie.css">
<jsp:useBean id="RSB" class="DBUtilities.RevenueShareBean" scope="session"/>
<%=RSB.getProcessMessageList()%>
