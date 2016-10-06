<html>
<%@ page import="HTMLUtil.HTMLBean"%>
<jsp:useBean id="BAN" class="DBUtilities.BANBean" scope="session"/>
<jsp:useBean id="HB" class="HTMLUtil.HTMLBean" scope="session"/>
<jsp:useBean id="DBA" class="DBUtilities.DBAccess" scope="session"/>
<%
  //DBAccess DBA = new DBAccess();
  boolean noneAssigned = false;
  boolean ok = true;
  boolean conglom = false;
  String customer = "";
  String fromSelf = request.getParameter("fromSelf");
  if (fromSelf == null)
  {
    conglom = request.getParameter("conglom") != null;
    BAN.setAnalysts("");
  }
  else
  {
    conglom = request.getParameter("conglom").equals("true");
    customer = request.getParameter("GCB_Customer3");
    if (fromSelf.equals("react"))
    {
      ok = BAN.reactivateCustomer(customer);
    }
    String analysts = BAN.findAnalysts(customer, conglom);
    noneAssigned = analysts.indexOf("<td class=grid1 width=397 NOWRAP>None Assigned</td></tr>")!=-1;
  }
%>
<head>
<link rel="stylesheet" type="text/css" href="../nr/cw/newcss/world_ie.css">
<title>Analyst Search</title>
</head>
<script language="JavaScript">
  function confirmReact()
  {
    if (confirm("This will reactivate <%=customer%> and assign it to <%=BAN.getActAsLogon()%>. Press 'OK' to confirm."))
    {
      analyst.fromSelf.value = "react";
      analyst.submit();
    }
  }

  function window_onLoad()
  {
    window.focus();
<%if (!ok)
  {%>
    alert("Unable to reactivate this customer");
<%}%>
  }
</script>
<body topmargin="0" leftmargin="0" marginleft="0" margintop="0" onLoad="return window_onLoad()">
<form name="analyst" method="post" action="analyst.jsp">
<input name="ButtonPressed" type=hidden value="">
<input name="fromSelf" type=hidden value="true">
<input name="conglom" type=hidden value="<%=conglom%>">
<a name="top"></a>
<table width="100%" border="0" id=3>
  <tr>
    <td colspan=2 align=center>
      <b>Analyst Search</b>
    </td>
  </tr>
  <tr>
    <td colspan=2 align=center>Please select a customer from the list to see all assigned analysts
    </td>
  </tr>
  <tr>
    <td colspan=2>
      <!--header-->
      <table border=0 width=100%>
        <tr class=gridHeader>
          <td class=gridHeader valign=top width=310>Customer<br>
            <%=DBA.getListBox("GCB_Customer3","submit",customer,conglom?"true":"false",1," style=\"width:400px\"",true)%>
          </td>
        </tr>
      </table>
    </td>
  </tr>
  <tr>
    <td colspan=2>
      <!--header-->
      <table border=0 width=100%>
        <tr class=gridHeader>
          <td class=gridHeader NOWRAP valign=top width=400>
            <button class=grid_menu>Analyst</button>
          </td>
          <td class=gridHeader NOWRAP valign=top>
            <button class=grid_menu>&nbsp;</button>
          </td>
        </tr>
        <tr>
          <td colspan=2>
          <iframe frameborder=0 width="100%" height=250 id=GridData name=GridData src="analystGrid.jsp"></iframe>
          </td>
        </tr>
      </table>
    </td>
  </tr>
  <tr>
<%if ((noneAssigned) && (!conglom))
  {%>
    <td align=right>
      <input class=button style="width:100" type=button value=Close onClick="window.close();">
    </td>
    <td align=left>
      <input class=button style="width:100" type=button value=Reactivate onClick="confirmReact()">
    </td>
<%}
  else
  {%>
    <td colspan=2 align=center>
      <input class=button style="width:100" type=button value=Close onClick="window.close();">
    </td>
<%}%>
  </tr>
</table><!--table 3-->
</form>
</body>
</html>


