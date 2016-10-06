<SCRIPT language="JavaScript">
function sendSelected(selectedId, selectedOption)
{
  parent.document.forms.conglomDiscountExclusion.exclId.value=selectedId;
  parent.document.forms.conglomDiscountExclusion.fromSelf.value="remove";
  parent.document.forms.conglomDiscountExclusion.submit();
}
</SCRIPT>
<link rel="stylesheet" type="text/css" href="../nr/cw/newcss/world_ie.css">
<jsp:useBean id="disBAN" class="DBUtilities.ConglomDiscountBANBean" scope="session"/>
<%=disBAN.getConglomDiscountExclusionList()%>
