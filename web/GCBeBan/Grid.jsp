<SCRIPT language="JavaScript">
function sendSelected(selectedId)
{
  parent.document.forms.<%=(String)session.getAttribute("formname")%>.BAN_Id.value=selectedId;
  parent.document.forms.<%=(String)session.getAttribute("formname")%>.ButtonPressed.value="Select";
  parent.document.forms.<%=(String)session.getAttribute("formname")%>.submit();
}
</SCRIPT>
<link rel="stylesheet" type="text/css" href="../nr/cw/newcss/world_ie.css">
<jsp:useBean id="BAN" class="DBUtilities.BANBean" scope="session"/>
<%=BAN.getBanList("GCB")%>
