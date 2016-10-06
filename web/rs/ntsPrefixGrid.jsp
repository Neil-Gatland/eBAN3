<SCRIPT language="JavaScript">
function sendSelected(prodGroup, prodCode, ntsPrefix, selectedOption)
{
  parent.document.forms.<%=(String)session.getAttribute("formname")%>.listProductGroup.value=prodGroup;
  parent.document.forms.<%=(String)session.getAttribute("formname")%>.listProductCode.value=prodCode;
  parent.document.forms.<%=(String)session.getAttribute("formname")%>.listNTSPrefix.value=ntsPrefix;
  parent.document.forms.<%=(String)session.getAttribute("formname")%>.ButtonPressed.value="list_"+selectedOption;
  parent.document.forms.<%=(String)session.getAttribute("formname")%>.submit();
}
</SCRIPT>
<link rel="stylesheet" type="text/css" href="../nr/cw/newcss/world_ie.css">
<jsp:useBean id="NTS" class="DBUtilities.NTSBean" scope="session"/>
<%=NTS.getNTSPrefixList()%>
