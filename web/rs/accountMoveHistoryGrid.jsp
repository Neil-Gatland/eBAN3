<SCRIPT language="JavaScript">
function sendSelected(processSeqNo, processName, selectedOption)
{
  parent.document.forms.viewProcess.processSeqNo.value=processSeqNo;
  parent.document.forms.viewProcess.processName.value=processName;
  parent.document.forms.viewProcess.fromSelf.value="drill";
  parent.document.forms.viewProcess.submit();
}
</SCRIPT>
<link rel="stylesheet" type="text/css" href="../nr/cw/newcss/world_ie.css">
<jsp:useBean id="RSB" class="DBUtilities.RevenueShareBean" scope="session"/>
<%=RSB.getAccountMoveHistoryList()%>
