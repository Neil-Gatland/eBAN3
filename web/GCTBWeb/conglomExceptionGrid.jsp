<SCRIPT language="JavaScript">
function sendSelected(selectedId, selectedOption, selectedInvDo,
  selectedStatus)
{
  parent.document.forms.<%=(String)session.getAttribute("formname")%>.exId.value=selectedId;
  parent.document.forms.<%=(String)session.getAttribute("formname")%>.invoiceDocketNo.value=selectedInvDo;
  parent.document.forms.<%=(String)session.getAttribute("formname")%>.exStatus.value=selectedStatus;
  parent.document.forms.<%=(String)session.getAttribute("formname")%>.ButtonPressed.value="list_"+selectedOption;
  parent.document.forms.<%=(String)session.getAttribute("formname")%>.submit();
}
</SCRIPT>
<link rel="stylesheet" type="text/css" href="../nr/cw/newcss/world_ie.css">
<jsp:useBean id="BAN" class="DBUtilities.BANBean" scope="session"/>
<%=BAN.getConglomExceptionList()%>

