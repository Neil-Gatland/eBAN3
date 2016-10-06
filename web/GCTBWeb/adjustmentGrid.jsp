<SCRIPT language="JavaScript">
function sendSelected(selectedId, selectedOption, archived, invoiceNo, accId, accName, total)
{
  parent.document.forms.<%=(String)session.getAttribute("formname")%>.adjustmentId.value=selectedId;
  parent.document.forms.<%=(String)session.getAttribute("formname")%>.ButtonPressed.value="list_"+selectedOption;
  parent.document.forms.<%=(String)session.getAttribute("formname")%>.archived.value=archived;
  parent.document.forms.<%=(String)session.getAttribute("formname")%>.iRId.value=invoiceNo;
  parent.document.forms.<%=(String)session.getAttribute("formname")%>.accountId.value=accId;
  parent.document.forms.<%=(String)session.getAttribute("formname")%>.accountName.value=accName;
  parent.document.forms.<%=(String)session.getAttribute("formname")%>.total.value=total;
  parent.document.forms.<%=(String)session.getAttribute("formname")%>.submit();
}
</SCRIPT>
<link rel="stylesheet" type="text/css" href="../nr/cw/newcss/world_ie.css">
<jsp:useBean id="adjBAN" class="DBUtilities.AdjustmentBANBean" scope="session"/>
<%=adjBAN.getAdjustmentList()%>
