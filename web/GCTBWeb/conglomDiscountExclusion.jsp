<html>
<head>
  <title>Conglomerate Discount - Account Exclusion List</title>
  <link rel="stylesheet" type="text/css" href="../nr/cw/newcss/world_ie.css">
</head>
<%@ page import="JavaUtil.StringUtil"%>
<jsp:useBean id="disBAN" class="DBUtilities.ConglomDiscountBANBean" scope="session"/>
<jsp:useBean id="BAN" class="DBUtilities.BANBean" scope="session"/>
<%
  JavaUtil.StringUtil SU = new JavaUtil.StringUtil();
  String fromSelf = request.getParameter("fromSelf") == null?"":
    request.getParameter("fromSelf");
  String message = "";
  String conglomCustName = BAN.getGlobalCustomerId() + " " +
    BAN.getConglomCustomerName() + " ("+BAN.getConglomCustomerId() + ")";
  String billedProduct = disBAN.getBilledProductDesc();
  String excludeAccount = "";
  String discountType = disBAN.getDiscountTypeDesc();

  if (!fromSelf.equals(""))
  {
    if (fromSelf.equals("add"))
    {
      excludeAccount = request.getParameter("excludeAccount");
      if (disBAN.validateAccountToExclude(excludeAccount))
      {
        disBAN.excludeAccount(excludeAccount);
      }
    }
    else //(fromSelf.equals("remove"))
    {
      long exclId = Long.parseLong(request.getParameter("exclId"));
      disBAN.removeExcludedAccount(exclId);
    }
    message = disBAN.getMessage();
  }
/*
03000360
00145958
09030474
00472195
*/
%>
<script language="JavaScript" src="../includes/eBanfunctions.js">
</script>
<script language="JavaScript">
function valAndSub()
{
  if (conglomDiscountExclusion.excludeAccount.value == "")
  {
    alert('Please enter a source account to exclude');
  }
  else
  {
    conglomDiscountExclusion.fromSelf.value = "add";
    conglomDiscountExclusion.submit();
  }
}
function Toggle_Sort(buttonPressed)
{
  if (buttonPressed == 'Action')
  {
    window.open("conglomDiscountExclusionKey.htm", "ndk", "toolbar=no,menubar=no,location=no," +
      "directories=no,status=no,scrollbars=no,resizable=no,height=200,width=200,top=50,left=50");
  }
}
function window_onload()
{
  window.focus();
}
</script>
<body language=javascript onload="return window_onload()">
<form name="conglomDiscountExclusion" method="post" action="conglomDiscountExclusion.jsp">
<input name="ButtonPressed" type=hidden value="">
<input name="fromSelf" type=hidden value="">
<input name="exclId" type=hidden value="">
<a name="top"></a>
<table width="100%" border="0" id=3>
  <tr>
    <td colspan=2 align=center>
      <b>Conglomerate Discount - Account Exclusion List</b>
    </td>
  </tr>
  <tr>
    <td colspan=2>
      <!--header-->
      <table border=0 width=100%>
        <tr>
          <td><b>Customer:</b></td>
          <td class=bluebold><%=conglomCustName%></td>
        </tr>
        <tr>
          <td><b>Billed Product:</b></td>
          <td class=bluebold><%=billedProduct%></td>
          </td>
        </tr>
        <tr>
          <td><b>Discount Type:</b></td>
          <td class=bluebold><%=discountType%></td>
          </td>
        </tr>
        <tr>
          <td><b>Account to be Excluded:</b></td>
          <td><input style="height:18px;font-size:xx-small;" type=text name="excludeAccount" value="<%=excludeAccount%>"></td>
          </td>
        </tr>
        <tr>
          <td colspan="2">
            <table border=0 width=100%>
              <tr class=gridHeader>
                <td class=gridHeader NOWRAP valign=top width=365>
                  <button class=grid_menu>Accounts Currently Excluded</button>
                </td>
                <td class=gridHeader NOWRAP valign=top>
                  <button class=grid_menu onclick="Toggle_Sort('Action')">Action</button>
                </td>
              </tr>
              <tr>
                <td colspan=2>
                <iframe frameborder=0 width="100%" height=150 id=GridData name=GridData src="conglomDiscountExclusionGrid.jsp"></iframe>
                </td>
              </tr>
            </table>
          </td>
        </tr>
      </table>
    </td>
  </tr>
  <tr>
    <td colspan=2 align=center>
      <b><%=message%></b>
    </td>
  </tr>
  <tr>
    <td align=right>
      <input class=button title="Add" type=button value="Add" style="width:90px" onClick="valAndSub()">
    </td>
    <td align=left>
      <input class=button title="Done" type=button value="Done" style="width:90px" onClick="window.close()">
    </td>
  </tr>
</table><!--table 3-->
</form>
</body>
</html>


