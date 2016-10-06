<%@ page import="JavaUtil.DesktopCustomerDescriptor"%>
<%@ page import="java.util.*"%>
<jsp:useBean id="BAN" class="DBUtilities.BANBean" scope="session"/>
<%
String userId = request.getParameter("userId");
Collection customerList = BAN.getConglomCustomerList2(userId,
  request.getParameter("sortOrder"));
%>
<SCRIPT language="JavaScript">
function sS(selectedId, selectedOption)
{
  parent.document.forms.<%=(String)session.getAttribute("formname")%>.gcId.value=selectedId;
  parent.document.forms.<%=(String)session.getAttribute("formname")%>.ButtonPressed.value="list_"+selectedOption;
  parent.document.forms.<%=(String)session.getAttribute("formname")%>.submit();
}
</SCRIPT>
<link rel="stylesheet" type="text/css" href="../nr/cw/newcss/world_ie.css">
<table border=0>
<%
  boolean first = true;
  boolean one = true;
  for (Iterator it = customerList.iterator(); it.hasNext(); )
  {
    DesktopCustomerDescriptor dcd = (DesktopCustomerDescriptor)it.next();
    String cl = one?"grid1":"grid2";
    one = !one;
    String gcId = dcd.getConglomCustId();
%>
  <tr>
  <td class=<%=cl%> width=397><a name="<%=gcId%>"></a><%=dcd.getCustomerName()%></td>
  <td class=<%=cl%><%=first?" width=65":""%> nowrap><%=dcd.getFrequency() %></td>
  <td class=<%=cl%><%=first?" width=55":""%> nowrap><%=dcd.getPeriod()%></td><td class=<%=cl%><%=first?" width=70":""%> nowrap><%=dcd.getStatus()%></td>
  <td class=<%=cl%><%=first?" width=40":""%> nowrap><%=dcd.getDOB()%></td><td class=<%=cl%><%=first?" width=55":""%> nowrap><%=dcd.getProducts()%></td>
  <td class=<%=cl%><%=first?" width=70":""%> nowrap><%=dcd.getExceptions()%></td>
  <td class=<%=cl%>><input class=listbutton type=button value="P" onClick="sS('<%=gcId%>','P')"><input
    class=listbutton type=button value="B" onClick="sS('<%=gcId%>','B')"><input
    class=listbutton type=button value="E" onClick="sS('<%=gcId%>','E')"><input
    class=listbutton type=button value="S" onClick="sS('<%=gcId%>','S')"><input
    class=listbutton type=button value="V" onClick="sS('<%=gcId%>','V')"><input
    class=listbutton type=button value="Pr" onClick="sS('<%=gcId%>','Pr')">
  </td>
  </tr>
<%  first = false;
  }

%>
</table>