<SCRIPT language="JavaScript">
function sendSelected(invoiceNo, lineNo, selectedOption)
{
  parent.document.forms.<%=(String)session.getAttribute("formname")%>.invoiceNo.value=invoiceNo;
  parent.document.forms.<%=(String)session.getAttribute("formname")%>.lineNo.value=lineNo;
  parent.document.forms.<%=(String)session.getAttribute("formname")%>.ButtonPressed.value="list_AdjLine"+selectedOption;
  parent.document.forms.<%=(String)session.getAttribute("formname")%>.submit();
}
</SCRIPT>
<link rel="stylesheet" type="text/css" href="../nr/cw/newcss/world_ie.css">
<jsp:useBean id="ADJ" class="DBUtilities.AdjustmentBean" scope="session"/>
<%=ADJ.getAdjustmentLineList()%>
