<SCRIPT language="JavaScript">
function sendSelected(selectedId, selectedOption)
{
  parent.document.forms.<%=(String)session.getAttribute("formname")%>.rowId.value=selectedId;
  parent.document.forms.<%=(String)session.getAttribute("formname")%>.ButtonPressed.value="list_"+selectedOption;
  parent.document.forms.<%=(String)session.getAttribute("formname")%>.submit();
}
</SCRIPT>
<link rel="stylesheet" type="text/css" href="../nr/cw/newcss/world_ie.css">
<jsp:useBean id="BAN" class="DBUtilities.BANBean" scope="session"/>
<%
  String type = request.getParameter("type");
  String contents = type.equals("request")
    ?BAN.getGSORequestList(request.getParameter("sortOrder"))
    :type.equals("reject")
    ?BAN.getGSORejectList(request.getParameter("sortOrder"))
    :type.equals("exception")
    ?BAN.getGSOExceptionList(request.getParameter("sortOrder"))
    :BAN.getGSOBillingLogList(request.getParameter("sortOrder"));
%>
<%=contents%>
