<SCRIPT language="JavaScript">
function sendSelected(selectedId, reverseNo)
{
  if (reverseNo != '0000000000')
  {
    alert('This invoice has been reversed already by invoice ' + reverseNo);
  }
  else if (confirm("Are you sure you want to reverse this invoice?"))
  {
    parent.document.forms.<%=(String)session.getAttribute("formname")%>.invoiceNo.value=selectedId;
    parent.document.forms.<%=(String)session.getAttribute("formname")%>.ButtonPressed.value="list_Reverse";
    parent.document.forms.<%=(String)session.getAttribute("formname")%>.submit();
  }
}
</SCRIPT>
<link rel="stylesheet" type="text/css" href="../nr/cw/newcss/world_ie.css">
<jsp:useBean id="BAN" class="DBUtilities.BANBean" scope="session"/>
<%=BAN.getClosedInvoiceGrid()%>
