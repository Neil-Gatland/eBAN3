<html>
<head>
  <title>Re-pull Billing Data from Goldfish</title>
  <link rel="stylesheet" type="text/css" href="../nr/cw/newcss/world_ie.css">
</head>
<%@ page import="java.util.Calendar"%>
<%@ page import="JavaUtil.StringUtil"%>
<jsp:useBean id="BAN" class="DBUtilities.BANBean" scope="session"/>
<jsp:useBean id="HB" class="HTMLUtil.HTMLBean" scope="session"/>
<jsp:useBean id="ccBAN" class="DBUtilities.ConglomCustomerBANBean" scope="session"/>
<jsp:useBean id="DBA" class="DBUtilities.DBAccess" scope="session"/>
<%
  JavaUtil.StringUtil SU = new JavaUtil.StringUtil();
  //DBUtilities.DBAccess DBA = new DBUtilities.DBAccess();
  String message = "<font color=blue>Select Product, date and Extract Type and press 'OK'</font>";
  long conglomCustId = ccBAN.getConglomCustomerId();
  String conglomCustName = ccBAN.getCustomerName();
  String billedProduct = "";
  String button1Title = "This process will invalidate any trial bills which " +
    "may have been generated previously. Therefore the Trial Bill process " +
    "should be re-initiated on successful completion of the download.";
  String button1Text = "OK";
  String button2Text = "Exit";
  String button1OnClick = "valAndSub();";
  String button2OnClick = "window.close();";
  String fromSelf = request.getParameter("fromSelf") == null?"":
    request.getParameter("fromSelf");
  String selectMode = "SELECT";
  String submitMode = "submit";
  String extractDate = "";
  boolean fullChecked = true;
  boolean disableDate = false;
  if (!fromSelf.equals(""))
  {
    button1Title = "";
    if (fromSelf.equals("refresh"))
    {
      extractDate = request.getParameter("conglomExtractDate");
      billedProduct = request.getParameter("conglomBilledProduct");
      fullChecked = request.getParameter("extractType").equals("full");
    }
    else if (fromSelf.equals("confirm"))
    {
      extractDate = request.getParameter("conglomExtractDate");
      billedProduct = request.getParameter("conglomBilledProduct");
      fullChecked = request.getParameter("extractType").equals("full");
      selectMode = "DISABLED";
      submitMode = "DISABLED";
      disableDate = true;
      button1Text = "Confirm";
      button1OnClick = "confirmSub();";
      button2Text = "Cancel";
      message = "<font color=blue>Press 'Confirm' to initiate the extract</font>";
    }
    else if (fromSelf.equals("repull"))
    {
      extractDate = request.getParameter("conglomExtractDateh");
      billedProduct = request.getParameter("conglomBilledProducth");
      fullChecked = request.getParameter("extractTypeh").equals("full");
      selectMode = "SELECT";
      submitMode = "submit";
      disableDate = false;
      if (ccBAN.rePullDataFromGoldfish(extractDate, fullChecked, billedProduct) == 0)
      {
        button1Text = "View Log";
        button1OnClick = "viewLog();";
      }
      message = ccBAN.getMessage();
    }
  }
%>
<script language="JavaScript" src="../includes/eBanfunctions.js">
</script>
<script language="JavaScript">
function viewLog()
{
  window.open("../GCTBWeb/billingLog.jsp?conglom=true&fromSelf=true", "mblog", "toolbar=no,menubar=no,location=no," +
    "directories=no,status=no,scrollbars=no,resizable=no,height=400,width=800,top=50,left=50");
  this.close();
}
function confirmSub()
{
  rePullGoldfish.fromSelf.value = "repull";
  rePullGoldfish.submit();
}
function refreshSub()
{
  rePullGoldfish.fromSelf.value = "refresh";
  rePullGoldfish.submit();
}
function valAndSub()
{
  if (rePullGoldfish.conglomBilledProduct.selectedIndex < 1)
  {
    alert("Please enter both Goldfish product name and extract date");
    return;
  }
  if (rePullGoldfish.conglomExtractDate.selectedIndex < 1)
  {
    alert("Please enter both Goldfish product name and extract date");
    return;
  }
  rePullGoldfish.fromSelf.value = "confirm";
  rePullGoldfish.submit();
}


function window_onload()
{
  window.focus();
<%if (fromSelf.equals("adjust"))
  {%>
  //window.opener.location.href = "adHocInvoiceHandler.jsp?ButtonPressed=fromInvoice";
  //self.close();
<%}
  else if (fromSelf.equals("refresh"))
  {%>
  //window.opener.location.href = "adHocInvoice.jsp";
  //self.close();
<%}%>
}
</script>
<body language=javascript onload="return window_onload()">
<form name="rePullGoldfish" method="post" action="rePullGoldfish.jsp">
<input name="ButtonPressed" type=hidden value="">
<input name="fromSelf" type=hidden value="refresh">
<input name="conglomExtractDateh" type=hidden value="<%=extractDate%>">
<input name="conglomBilledProducth" type=hidden value="<%=billedProduct%>">
<input name="extractTypeh" type=hidden value="<%=fullChecked?"full":"partial"%>">
<a name="top"></a>
<table width="100%" border="0" id=3>
  <tr>
    <td colspan=2 align=center>
      <b>Re-pull Billing Data from Goldfish</b>
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
          <td><b>Goldfish Product Name:</b></td>
          <td>
              <%=DBA.getListBox("conglomBilledProduct",submitMode,
              billedProduct,Long.toString(conglomCustId),"goldfish",1,"",true)%>
          </td>
        </tr>
        <tr>
          <td><b>Extract Date:</b></td>
          <td>
            <%=ccBAN.getExtractDatesListBox(extractDate, fullChecked,
              billedProduct, disableDate)%>
          </td>
        </tr>
        <tr>
          <td><b>Full Extract:</b></td>
          <td>
            <input <%=disableDate?"disabled":""%> type=radio name=extractType value=full <%=fullChecked?"checked":""%>
              title="Any data previously extracted for this period will be cleared down." onClick="refreshSub();">
          </td>
        </tr>
<!--
        <tr>
          <td><b>Partial Extract:</b></td>
          <td>
            <input <%=disableDate?"disabled":""%> type=radio name=extractType value=partial <%=fullChecked?"":"checked"%>
              title="Any bills generated in Goldfish since the last extract will be added." onClick="refreshSub();">
          </td>
        </tr>
-->
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
      <input class=button title="<%=button1Title%>" type=button value="<%=button1Text%>" style="width:90px" onClick="<%=button1OnClick%>">
    </td>
    <td align=left>
      <input class=button type=button value="<%=button2Text%>" style="width:90px" onClick="<%=button2OnClick%>">
    </td>
  </tr>
</table><!--table 3-->
</form>
</body>
</html>


