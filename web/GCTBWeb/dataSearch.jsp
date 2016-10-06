<html>
<%@ page import="HTMLUtil.HTMLBean"%>
<jsp:useBean id="BAN" class="DBUtilities.BANBean" scope="session"/>
<jsp:useBean id="HB" class="HTMLUtil.HTMLBean" scope="session"/>
<jsp:useBean id="DBA" class="DBUtilities.DBAccess" scope="session"/>
<%
  //DBAccess DBA = new DBAccess();
  boolean noneAssigned = false;
  boolean ok = true;
  String colspan = "6";
  String check1 = "";
  String check2 = "";
  String check3 = "";
  String check4 = "";
  String check5 = "";
  String fromSelf = request.getParameter("fromSelf");
  String searchData = request.getParameter("searchData");
  String searchType = request.getParameter("searchType");
  if (searchType != null)
  {
    BAN.dataSearch(searchData, searchType);
  }
  else
  {
    searchData = "";
    BAN.setSearchResults("");
  }
%>
<head>
<link rel="stylesheet" type="text/css" href="../nr/cw/newcss/world_ie.css">
<title>Customer Data Search</title>
</head>
<script language="JavaScript">
  function valsub(type)
  {
    if ((type == '2') && (dataSearch.searchData.value.length != 10))
    {
      alert('Account Id must be 10 characters long');
    }
    else
    {
      dataSearch.submit();
    }
  }

  function window_onLoad()
  {
    window.focus();
  }
</script>
<body topmargin="0" leftmargin="0" marginleft="0" margintop="0" onLoad="return window_onLoad()">
<form name="dataSearch" method="post" action="dataSearch.jsp">
<input name="ButtonPressed" type=hidden value="">
<input name="fromSelf" type=hidden value="true">
<a name="top"></a>
<table width="100%" border="0" id=3>
  <tr>
    <td colspan=2 align=center>
      <b>Customer Data Search</b>
    </td>
  </tr>
  <tr>
    <td colspan=2 align=center>Please enter a value in the search data field and select the type of search to perform
    </td>
  </tr>
  <tr>
    <td colspan=2>
      <!--header-->
      <table border=0 width=100%>
        <tr class=gridHeader>
          <td class=gridHeader valign=center width=310>Search Data<br>
  	    <input class=inp type=text name="searchData" size=70 maxlength=255 value="<%=searchData%>">
          </td>
          <td class=gridHeader valign=top width=310>
  	    <input type=radio name="searchType" value="gcid" <%=check1%> onClick="valsub('1')">Global Customer Id<br>
  	    <input type=radio name="searchType" value="acc" <%=check2%> onClick="valsub('2')">Account Id<br>
  	    <input type=radio name="searchType" value="c2ref" <%=check3%> onClick="valsub('3')">C2 Ref<br>
  	    <input type=radio name="searchType" value="c3ref" <%=check4%> onClick="valsub('4')">C3 Ref<br>
  	    <input type=radio name="searchType" value="gsr" <%=check5%> onClick="valsub('5')">GSR
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
<%if ((searchType == null) || (searchType.equals("gcid")) || (searchType.equals("acc")))
  {%>
          <td class=gridHeader NOWRAP valign=top width=250>
            <button class=grid_menu>Customer Name</button>
          </td>
          <td class=gridHeader NOWRAP valign=top width=75>
            <button class=grid_menu>Customer Id</button>
          </td>
          <td class=gridHeader NOWRAP valign=top width=190>
            <button class=grid_menu>Invoice Region</button>
          </td>
          <td class=gridHeader NOWRAP valign=top width=75>
            <button class=grid_menu>Account Id</button>
          </td>
          <td class=gridHeader NOWRAP valign=top width=150>
            <button class=grid_menu>Analyst</button>
          </td>
<%}
  else if ((searchType.equals("c2ref")) || (searchType.equals("c3ref")))
  {
    colspan = "6";
%>
          <td class=gridHeader NOWRAP valign=top width=140>
            <button class=grid_menu>Customer Id</button>
          </td>
          <td class=gridHeader NOWRAP valign=top width=150>
            <button class=grid_menu>Account Id</button>
          </td>
          <td class=gridHeader NOWRAP valign=top width=150>
            <button class=grid_menu>GSR</button>
          </td>
          <td class=gridHeader NOWRAP valign=top width=100>
            <button class=grid_menu>Charge Type</button>
          </td>
          <td class=gridHeader NOWRAP valign=top width=200>
            <button class=grid_menu>Analyst</button>
          </td>
<%}
  else //if (searchType.equals("gsr"))
  {
    colspan = "4";
%>
          <td class=gridHeader NOWRAP valign=top width=248>
            <button class=grid_menu>Customer Id</button>
          </td>
          <td class=gridHeader NOWRAP valign=top width=246>
            <button class=grid_menu>Account Id</button>
          </td>
          <td class=gridHeader NOWRAP valign=top width=246>
            <button class=grid_menu>Last Billed Date</button>
          </td>
<%}%>
          <td class=gridHeader NOWRAP valign=top>
            <button class=grid_menu>&nbsp;</button>
          </td>
        </tr>
        <tr>
          <td colspan=<%=colspan%>>
          <iframe frameborder=0 width="100%" height=250 id=GridData name=GridData src="dataSearchGrid.jsp"></iframe>
          </td>
        </tr>
      </table>
    </td>
  </tr>
  <tr>
    <td colspan=2 align=center>
      <input class=button style="width:100" type=button value=Close onClick="window.close();">
    </td>
  </tr>
</table><!--table 3-->
</form>
</body>
</html>


