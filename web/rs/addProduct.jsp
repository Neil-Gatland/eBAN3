<html>
<head>
  <title>Add Product to Account</title>
  <link rel="stylesheet" type="text/css" href="../nr/cw/newcss/world_ie.css">
</head>
<%@ page import="java.util.Calendar"%>
<jsp:useBean id="BAN" class="DBUtilities.BANBean" scope="session"/>
<jsp:useBean id="HB" class="HTMLUtil.HTMLBean" scope="session"/>
<jsp:useBean id="ACC" class="DBUtilities.AccountBean" scope="session"/>
<jsp:useBean id="DBA" class="DBUtilities.DBAccess" scope="session"/>
<jsp:useBean id="RSB" class="DBUtilities.RevenueShareBean" scope="session"/>
<%
  JavaUtil.StringUtil SU = new JavaUtil.StringUtil();
  String selectStyle = " style=\"width:400px\"";
  String message ="<font color=blue><b>Please select Product Group, followed by Product and then press 'Add'</b></font>";
  String productGroupId = "";
  String productId = "";
  String button1Text = "Add";
  String button2Text = "Exit";
  String button1OnClick = "valAndSub();";
  String button2OnClick = "window.close();";
  String fromSelf = request.getParameter("fromSelf") == null?"":
    request.getParameter("fromSelf");
  if (!fromSelf.equals(""))
  {
    productGroupId=(String)request.getParameter("rsProductGroup");
    productId=(String)request.getParameter("rsProduct");
    if (!productGroupId.equals(RSB.getProductGroupId()))
    {
      RSB.setProductGroupId(SU.isNull((String)request.getParameter("rsProductGroup"),""));
      RSB.setProductId("");
    }
    else
    {
      RSB.setProductId(productId);
    }
    if (fromSelf.equals("add"))
    {
      ACC.setProductCode(productId);
      if (!ACC.accountProductExists())
      {
        ACC.getProductFromDB();
        ACC.createAccountProduct();
      }
      else
      {
        fromSelf = "isThere";
        message ="<font color=blue><b>The Account already has this Product</b></font>";
      }
    }
  }
  productGroupId = RSB.getProductGroupId();
  productId = RSB.getProductId();
%>
<script language="JavaScript" src="../includes/eBanfunctions.js">
</script>
<script language="JavaScript">
function valAndSub()
{
  addProduct.button1.disabled = true;
<%if (productId.equals(""))
  {%>
    alert("Please select a Product to add to this Account");
<%}
  else
  {%>
    addProduct.button1.disabled = false;
    addProduct.fromSelf.value = "add";
    addProduct.submit();
<%}%>
}


function window_onload()
{
  window.focus();
<%if (fromSelf.equals("add"))
  {%>
  window.opener.location.href = "accountMaintHandler.jsp?ButtonPressed=Refresh";
  self.close();
<%}%>
}
</script>
<body language=javascript onload="return window_onload()">
<form name="addProduct" method="post" action="addProduct.jsp">
<input name="ButtonPressed" type=hidden value="">
<input name="fromSelf" type=hidden value="true">
<a name="top"></a>
<table width="100%" border="0" id=3>
  <tr>
    <td colspan=2 align=center>
      <b>Add Product to Account <%=ACC.getAccountName()%>/<%=ACC.getAccountNumber()%> (<%=ACC.getAccountId()%>)</b>
    </td>
  </tr>
  <tr>
    <td colspan=2>
      <!--header-->
      <table border=0 width=100%>
        <tr>
          <td align=left colspan=2><font color="#0000FF">Product Group:</font>
          </td>
          <td  align=left>
          <%=DBA.getListBox("rsProductGroup","submit",productGroupId,(String)session.getAttribute("User_Id"),1,selectStyle,true)%>
          </td>
          <td>&nbsp;</td>
        </tr>
        <tr>
          <td align=left colspan=2><font color="#0000FF">Product:</font>
          </td>
          <td  align=left>
          <%=DBA.getListBox("rsProduct","submit",productId,productGroupId,1,selectStyle,true)%>
          </td>
          <td>&nbsp;</td>
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
      <input name="button1" class=button type=button value="<%=button1Text%>" style="width:90px" onClick="<%=button1OnClick%>">
    </td>
    <td align=left>
      <input class=button type=button value="<%=button2Text%>" style="width:90px" onClick="<%=button2OnClick%>">
    </td>
  </tr>
</table><!--table 3-->
</form>
</body>
</html>


