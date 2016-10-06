<%@ page import="JavaUtil.DesktopCustomerDescriptor"%>
<%@ page import="java.util.*"%>
<jsp:useBean id="BAN" class="DBUtilities.BANBean" scope="session"/>
<%
String userId = request.getParameter("userId");
boolean all = userId.equals("All");
String sortOrder = request.getParameter("sortOrder");
Collection customerList = BAN.getCustomerList2(userId, sortOrder);
%>
<SCRIPT language="JavaScript">
function sS(selectedId, selectedOption, closedDate)
{
  if ((selectedOption == 'R') &&
    (!confirm("Please confirm you want to re-open customer " + selectedId +
      ", closed on " + closedDate + ".")))
  {
    return
  }
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
    String gcId = dcd.getGCID();
    boolean reOpen = dcd.getStatus().equals("<font color=blue>Closed</font>");
    String subRe = reOpen?"R":"S";
%>
  <tr>
<%if (all)
  {%>
  <td class=<%=cl%> width=367><div title="<%=dcd.getAnalyst()%>"><%=dcd.getCustomerName()%></div></td>
<%}
  else
  {%>
  <td class=<%=cl%> width=367><a name="<%=gcId%>"></a><%=dcd.getCustomerName()%></td>
<%}%>
  <td class=<%=cl%><%=first?" width=65":""%> nowrap><%=dcd.getPeriod() %></td>
  <td class=<%=cl%><%=first?" width=115":""%> nowrap><%=dcd.getFrequency()%></td><td class=<%=cl%><%=first?" width=37":""%> nowrap><%=dcd.getDOB()%></td>
  <td class=<%=cl%><%=first?" width=55":""%> nowrap><%=dcd.getAccounts()%></td><td class=<%=cl%><%=first?" width=70":""%> nowrap><%=dcd.getStatus()%></td>
  <td class=<%=cl%><%=first?" width=60":""%> nowrap><%=dcd.getAutoClose()%></td>
<%if (all)
  {%>
  <td<%=first?" width=144":""%> class=<%=cl%>>&nbsp;
<%}
  else
  {%>
  <td class=<%=cl%>><input class=listbutton type=button value="A" onClick="sS('<%=gcId%>','A')"><input
    class=listbutton type=button value="D" onClick="sS('<%=gcId%>','D')"><input
    class=listbutton type=button value="E" onClick="sS('<%=gcId%>','E')"><input
    class=<%=reOpen?"listbutton2":"listbutton"%> type=button value="<%=subRe%>" onClick="sS('<%=gcId%>','<%=subRe%>','<%=dcd.getBillClosedDate()%>')"><input
    class=listbutton type=button value="V" onClick="sS('<%=gcId%>','V')"><input
    class=listbutton type=button value="G" onClick="sS('<%=gcId%>','G')">
<%}%>
  </td>
  </tr>
<%  first = false;
  }

%>
</table>
