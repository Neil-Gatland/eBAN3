<HTML>
<HEAD>
<TITLE></TITLE>
</HEAD>
<BODY onload="alert('done');">
<%@ page import="DBUtilities.DBAccess,DBUtilities.BANBean,java.util.Enumeration,JavaUtil.*"%>
<%@ page import="java.util.GregorianCalendar"%>
<%
GregorianCalendar now = new GregorianCalendar();

%>
<link rel="stylesheet" type="text/css" href="../nr/cw/newcss/world_ie.css">
<%=now.get(now.HOUR)+"."+now.get(now.MINUTE)+"."+now.get(now.SECOND)+"."+now.get(now.MILLISECOND)%>
<br>
<table border=0>
<%for (int i = 1; i < 1001; i++)
  {%>
<tr><td class=grid1 width=397 nowrap><%=i%> AAA aaasdasd                                                                                            </td>
<td class=grid1 width=65 nowrap>February</td><td class=grid1 width=65 nowrap>Monthly  </td>
<td class=grid1 width=55 nowrap>WD03</td><td class=grid1 width=55 nowrap>0</td>
<td class=grid1 width=60 nowrap><font color=red>Not Run</font></td>
<td class=grid1 width=60 nowrap>N</td><td class=grid1 >
<img src=../nr/cw/newimages/buttonA.gif align=bottom border=0 width=24 height=22 onClick="sendSelected('AAA','Account')">
<img src=../nr/cw/newimages/buttonD.gif align=bottom border=0 width=24 height=22 onClick="sendSelected('AAA','DataBilling')">
<img src=../nr/cw/newimages/buttonE.gif align=bottom border=0 width=24 height=22 onClick="sendSelected('AAA','Exceptions')">
<img src=../nr/cw/newimages/buttonS.gif align=bottom border=0 width=24 height=22 onClick="sendSelected('AAA','Submit')">
<img src=../nr/cw/newimages/buttonV.gif align=bottom border=0 width=24 height=22 onClick="sendSelected('AAA','ViewBillLog')">
<img src=../nr/cw/newimages/buttonG.gif align=bottom border=0 width=24 height=22 onClick="sendSelected('AAA','Extract')"></td>
</tr>
<%}
now = new GregorianCalendar();
%>
</table>
<br>
<%=now.get(now.HOUR)+"."+now.get(now.MINUTE)+"."+now.get(now.SECOND)+"."+now.get(now.MILLISECOND)%>
</BODY>
</HTML>
