<html>
<jsp:useBean id="BAN" class="DBUtilities.BANBean" scope="session"/>
<jsp:useBean id="HB" class="HTMLUtil.HTMLBean" scope="session"/>
<%
  if (request.getParameter("fromSelf") != null)
  {
    BAN.getConglomJobQueue();
  }
%>
<head>
<link rel="stylesheet" type="text/css" href="../nr/cw/newcss/world_ie.css">
</head>
<body topmargin="0" leftmargin="0" marginleft="0" margintop="0" onLoad="window.focus();">
<form name="conglomJobQueue" method="post" action="conglomJobQueue.jsp">
<input name="ButtonPressed" type=hidden value="">
<input name="fromSelf" type=hidden value="true">
<a name="top"></a>
<table width="100%" border="0" id=3>
  <tr>
    <td colspan=2 align=center>
      <b>Conglomerate Job Queue</b>
    </td>
  </tr>
  <tr>
    <td colspan=2>
      <!--header-->
      <table border=0 width=100%>
        <tr class=gridHeader>
          <td class=gridHeader NOWRAP valign=top width=330>
            <button class=grid_menu>Customer</button>
          </td>
          <td class=gridHeader NOWRAP valign=top width=200>
            <button class=grid_menu>Billed Product</button>
          </td>
          <td class=gridHeader NOWRAP valign=top width=60>
            <button class=grid_menu>Bill Period</button>
          </td>
          <td class=gridHeader NOWRAP valign=top width=50>
            <button class=grid_menu>Status</button>
          </td>
          <td class=gridHeader NOWRAP valign=top>
            <button class=grid_menu>User Id</button>
          </td>
        </tr>
        <tr>
          <td colspan=5>
          <iframe frameborder=0 width="100%" height=250 id=GridData name=GridData src="conglomJobQueueGrid.jsp"></iframe>
          </td>
        </tr>
      </table>
    </td>
  </tr>
  <tr>
    <td align=right>
      <input class=button type=submit value=Refresh>
    </td>
    <td align=left>
      <input class=button type=button value=Close onClick="window.close();">
    </td>
  </tr>
</table><!--table 3-->
</form>
</body>
</html>


