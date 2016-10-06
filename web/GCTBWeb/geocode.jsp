<html>
<%@ page import="HTMLUtil.HTMLBean"%>
<jsp:useBean id="BAN" class="DBUtilities.BANBean" scope="session"/>
<jsp:useBean id="HB" class="HTMLUtil.HTMLBean" scope="session"/>
<jsp:useBean id="DBA" class="DBUtilities.DBAccess" scope="session"/>
<%
  //DBAccess DBA = new DBAccess();
  String city = "";
  String state = "";
  String zip = "";
  if (request.getParameter("fromSelf") == null)
  {
    BAN.setGeocodes("");
  }
  else
  {
    city = request.getParameter("city");
    state = request.getParameter("usState") == null?"":request.getParameter("usState");
    zip = request.getParameter("zip");
    BAN.findGeocodes(city, state, zip);
  }
%>
<head>
<link rel="stylesheet" type="text/css" href="../nr/cw/newcss/world_ie.css">
<title>Geocode Search</title>
</head>
<script language="JavaScript">
function valAndSub()
{
  if ((geocode.city.value == "") && (geocode.zip.value == "") &&
    (geocode.usState.selectedIndex <= 0))
  {
    alert("Please enter at least one search parameter");
  }
  else
  {
    geocode.submit();
  }

}
</script>
<body topmargin="0" leftmargin="0" marginleft="0" margintop="0" onLoad="window.focus();">
<form name="geocode" method="post" action="geocode.jsp">
<input name="ButtonPressed" type=hidden value="">
<input name="fromSelf" type=hidden value="true">
<a name="top"></a>
<table width="100%" border="0" id=3>
  <tr>
    <td colspan=2 align=center>
      <b>Geocode Search</b>
    </td>
  </tr>
  <tr>
    <td colspan=2 align=center>Please enter at least one of the following and press 'Refresh' to see the results
    </td>
  </tr>
  <tr>
    <td colspan=2>
      <!--header-->
      <table border=0 width=100%>
        <tr class=gridHeader>
          <td class=gridHeader NOWRAP valign=top width=200>City<br>
            <input type=text name=city value="<%=city%>" style="height:18px">
          </td>
          <td class=gridHeader valign=top width=310>State<br>
            <%=DBA.getListBox("usState","input",state,"US States",1," style=\"width:300px\"",true)%>
          </td>
          <td class=gridHeader NOWRAP valign=top width=100>Zipcode<br>
            <input type=text name=zip value="<%=zip%>" style="height:18px">
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
          <td class=gridHeader NOWRAP valign=top width=160>
            <button class=grid_menu>City</button>
          </td>
          <td class=gridHeader NOWRAP valign=top width=100>
            <button class=grid_menu>County</button>
          </td>
          <td class=gridHeader NOWRAP valign=top width=320>
            <button class=grid_menu>State</button>
          </td>
          <td class=gridHeader NOWRAP valign=top width=80>
            <button class=grid_menu>Zipcode</button>
          </td>
          <td class=gridHeader NOWRAP valign=top width=80>
            <button class=grid_menu>Geocode</button>
          </td>
          <td class=gridHeader NOWRAP valign=top>
            <button class=grid_menu>&nbsp;</button>
          </td>
        </tr>
        <tr>
          <td colspan=6>
          <iframe frameborder=0 width="100%" height=250 id=GridData name=GridData src="geocodeGrid.jsp"></iframe>
          </td>
        </tr>
      </table>
    </td>
  </tr>
  <tr>
    <td align=right>
      <input class=button type=button value=Refresh onClick="valAndSub();">
    </td>
    <td align=left>
      <input class=button type=button value=Close onClick="window.close();">
    </td>
  </tr>
</table><!--table 3-->
</form>
</body>
</html>


