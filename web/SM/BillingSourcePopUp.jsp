<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/1999/REC-html401-19991224/loose.dtd">
<html>
<head>
  <title>Select Billing Source</title>
  <link rel="stylesheet" type="text/css" href="../nr/cw/newcss/world_ie.css">
</head>
<%@ page import="DBUtilities.DBAccess,HTMLUtil.HTMLBean,JavaUtil.*"%>
<jsp:useBean id="acb" class="DBUtilities.AssureChargeBean" scope="session"/>
<jsp:useBean id="BAN" class="DBUtilities.BANBean" scope="session"/>
<%
  String Billing_Source = "";
  String Account = (String)request.getParameter("Account");
  String whereFrom = (String)request.getParameter("whereFrom");
  String onLoad = "";
  String Message = "<font color=\"#0000FF\">The Account Number selected is used by more than one Billing Source.  Please select the appropriate Billing Source</font>";
  HTMLUtil.HTMLBean HB = new HTMLUtil.HTMLBean();
  DBAccess DBA = new DBAccess();
  if (request.getParameter("fromSelf") != null)
  {
    onLoad = "window.open('" +
      (whereFrom.equals("menu")?"AdHocMenu.jsp":"AssureCharge.jsp") +
      "','ahmain');window.close();";
    session.setAttribute("PageSent",request.getRequestURI());
    BAN.setBillingSource((String)request.getParameter("Billing_Source"));
    BAN.setAccount_Id(Account);
    //BAN.setAccountName(Account);
    if (!whereFrom.equals("menu"))
    {
      acb.setBillingSource((String)request.getParameter("Billing_Source"));
      acb.setAccountDetails(Account);
      acb.setCustomerFromAccount(Account);
      acb.setCustomerNameWithAccount();
      acb.setBanStatus("Draft");
    }
  }
  else
  {
    BAN.setAccount_Id("");
    BAN.setAccountName("");
    BAN.setBillingSource("");
  }
 %>
  <script language="JavaScript">
  <!--
  function MM_preloadImages() { //v3.0
    var d=document; if(d.images){ if(!d.MM_p) d.MM_p=new Array();
      var i,j=d.MM_p.length,a=MM_preloadImages.arguments; for(i=0; i<a.length; i++)
      if (a[i].indexOf("#")!=0){ d.MM_p[j]=new Image; d.MM_p[j++].src=a[i];}}
  }

  function MM_swapImgRestore() { //v3.0
    var i,x,a=document.MM_sr; for(i=0;a&&i<a.length&&(x=a[i])&&x.oSrc;i++) x.src=x.oSrc;
  }

  function MM_findObj(n, d) { //v4.0
    var p,i,x;  if(!d) d=document; if((p=n.indexOf("?"))>0&&parent.frames.length) {
      d=parent.frames[n.substring(p+1)].document; n=n.substring(0,p);}
    if(!(x=d[n])&&d.all) x=d.all[n]; for (i=0;!x&&i<d.forms.length;i++) x=d.forms[i][n];
    for(i=0;!x&&d.layers&&i<d.layers.length;i++) x=MM_findObj(n,d.layers[i].document);
    if(!x && document.getElementById) x=document.getElementById(n); return x;
  }

  function MM_swapImage() { //v3.0
  //alert("in there!");
    var i,j=0,x,a=MM_swapImage.arguments; document.MM_sr=new Array; for(i=0;i<(a.length-2);i+=3)
     if ((x=MM_findObj(a[i]))!=null){document.MM_sr[j++]=x; if(!x.oSrc) x.oSrc=x.src; x.src=a[i+2];}
  }

  function window_onload()
  {
    <%=onLoad%>
  }
  //-->
  </script>
<body language=javascript onload="return window_onload()">
<form name="BillingSourcePopUp" method="post" action="BillingSourcePopUp.jsp">
<input type="hidden" name="fromSelf" value="true">
<input type="hidden" name="Account" value="<%=Account%>">
<input type="hidden" name="whereFrom" value="<%=whereFrom%>">
<table>
  <tr>
    <td colspan=2 align="center">
      <h3><b>Select Billing Source<b></h3>
    </td>
  </tr>
  <tr>
    <td colspan=2 align="center">
      <font color="red"><%=Message%></font>
    </td>
  </tr>
  <tr>
    <td>
      <font color="#0000FF"><nobr>Billing Source:</nobr></font>
    </td>
    <td>
      <%=DBA.getListBox("Billing_Source","submit",Billing_Source,Account)%>
    </td>
  </tr>
</table>
</form>
</body>
</html>

