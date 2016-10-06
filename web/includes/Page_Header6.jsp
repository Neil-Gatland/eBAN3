<%@ page import="java.net.URLEncoder"%>
<%@ page import="java.util.*"%>
<head>
<script language="JavaScript" src="../includes/eBanfunctions.js">
</script>
<script language="JavaScript" src="../includes/RightClick.js">
</script>
<script language="JavaScript">
function showQuery()
{
  window.focus();
<%if (request.getParameter("query") != null)
  {
    Enumeration en = request.getParameterNames();
    StringBuffer sb = new StringBuffer("../query/addQuery.jsp?");
    while (en.hasMoreElements())
    {
      String pName = (String)en.nextElement();
      if (!pName.equals("query"))
      {
        sb.append(pName + "=" +
          URLEncoder.encode(request.getParameter(pName)) +
          "&");
      }
    }
    sb.deleteCharAt(sb.length()-1);
  %>
  window.open("<%=sb.toString()%>", "abqs", "toolbar=no,menubar=no,location=no," +
    "directories=no,status=no,scrollbars=no,resizable=no,height=600,width=800,top=50,left=50");
<%}
  else if (request.getParameter("queue") != null)
  {
    boolean isConglom = request.getParameter("conglom") != null;
    String screen = isConglom?"conglomJobQueue":"jobQueue";
  %>
  window.open("../GCTBWeb/<%=screen%>.jsp", "jobQueue", "toolbar=no,menubar=no,location=no," +
    "directories=no,status=no,scrollbars=no,resizable=no,height=400,width=800,top=50,left=50");
<%}
  else if (request.getParameter("mblog") != null)
  {
  %>
  window.open("../GCTBWeb/billingLog.jsp<%=request.getParameter("conglom")!=null?"?conglom=true":""%>", "mblog", "toolbar=no,menubar=no,location=no," +
    "directories=no,status=no,scrollbars=no,resizable=no,height=400,width=800,top=50,left=50");
<%}
  else if (request.getParameter("addProduct") != null)
  {
  %>
  window.open("../rs/addProduct.jsp", "rsProd", "toolbar=no,menubar=no,location=no," +
    "directories=no,status=no,scrollbars=no,resizable=no,height=400,width=800,top=50,left=50");
<%}
  else if (request.getParameter("moveAccount") != null)
  {
  %>
  window.open("../rs/moveAccount.jsp", "rsMoveAcc", "toolbar=no,menubar=no,location=no," +
    "directories=no,status=no,scrollbars=no,resizable=no,height=300,width=800,top=50,left=50");
<%}
  else if (request.getParameter("accountMoveHist") != null)
  {
  %>
  window.open("../rs/accountMoveHistory.jsp", "rsAccMoveHist", "toolbar=no,menubar=no,location=no," +
    "directories=no,status=no,scrollbars=no,resizable=yes,height=400,width=1100,top=50,left=50");
<%}
  else if (request.getParameter("suspendPopUp") != null)
  {
  %>
  window.open("../rs/suspendedData.jsp", "rsSuspend", "toolbar=no,menubar=no,location=no," +
    "directories=no,status=no,scrollbars=no,resizable=no,height=500,width=1200,top=50,left=24");
<%}
  else if (request.getParameter("adjustmentLineMaint") != null)
  {
  %>
  window.open("../rs/adjustmentLineMaint.jsp", "rsAdjLine", "toolbar=no,menubar=no,location=no," +
    "directories=no,status=no,scrollbars=no,resizable=no,height=200,width=500,top=50,left=50");
<%}
  else if (request.getParameter("advanceCallMonth") != null)
  {
  %>
  window.open("../rs/advanceCallMonth.jsp", "rsAdv", "toolbar=no,menubar=no,location=no," +
    "directories=no,status=no,scrollbars=no,resizable=no,height=400,width=800,top=50,left=50");
<%}
  else if (request.getParameter("viewProcess") != null)
  {
  %>
  window.open("../rs/viewProcess.jsp", "rsPro", "toolbar=no,menubar=no,location=no," +
    "directories=no,status=no,scrollbars=no,resizable=no,height=400,width=800,top=50,left=50");
<%}
  else if (request.getParameter("viewPMASummary") != null)
  {
  %>
  window.open("../rs/pmaSummary.jsp", "rsPMA", "toolbar=no,menubar=no,location=no," +
    "directories=no,status=no,scrollbars=yes,resizable=yes,width=1000,top=50,left=50");
<%}
  else if (request.getParameter("viewPDF") != null)
  {
  %>
  window.open("../GCTBWeb/pdfFrame.jsp?fileName=<%=request.getParameter("fileName")%>", "invPDF", "toolbar=no,menubar=no,location=no," +
    "directories=no,status=no,scrollbars=yes,resizable=yes,height=1000,width=800,top=50,left=50");
<%}
  else if (request.getParameter("viewThisProcess") != null)
  {
  %>
  window.open("../rs/viewProcessMessages.jsp?processName=<%=request.getParameter("viewThisProcess")%>", "rsAdv2", "toolbar=no,menubar=no,location=no," +
    "directories=no,status=no,scrollbars=no,resizable=no,height=400,width=800,top=75,left=75");
<%}
  else if (request.getParameter("viewReport") != null)
  {
  %>
  window.open("../rs/chooseReport.jsp?rsReport=<%=request.getParameter("viewReport")%>&reportType=<%=request.getParameter("reportType")%>", "viewReport", "toolbar=no,menubar=no,location=no," +
    "directories=no,status=no,scrollbars=auto,resizable=yes,height=300,width=500,top=50,left=50");
<%}
  else if (request.getParameter("changePass") != null)
  {
    StringBuffer parms = new StringBuffer("");
    if (request.getParameter("fromLogin") != null)
    {
      parms.append("?fromLogin=true");
      String passwordStatus = request.getParameter("passwordStatus");
      if (passwordStatus != null)
      {
        parms.append("&passwordStatus=" + passwordStatus);
      }
    }
  %>
  window.open("../GCTBWeb/changePassword.jsp<%=parms.toString()%>", "changePass", "toolbar=no,menubar=no,location=no," +
    "directories=no,status=no,scrollbars=no,resizable=no,height=200,width=350,top=50,left=50");
  //window.open("../GCTBWeb/changePassword.jsp<%=request.getParameter("fromLogin")!=null?"?fromLogin=true":""%>", "changePass");
<%}
  else if (request.getParameter("geocode") != null)
  {
  %>
  window.open("../GCTBWeb/geocode.jsp", "geocode", "toolbar=no,menubar=no,location=no," +
    "directories=no,status=no,scrollbars=no,resizable=no,height=400,width=800,top=50,left=50");
<%}
  else if (request.getParameter("invoiceSearch") != null)
  {
  %>
  window.open("../GCTBWeb/invoiceSearch.jsp", "invoiceSearch", "toolbar=no,menubar=no,location=no," +
    "directories=no,status=no,scrollbars=no,resizable=no,height=200,width=400,top=50,left=50");
<%}
  else if (request.getParameter("gcbDownloads") != null)
  {
  %>
  window.open("../GCTBWeb/chooseReport.jsp", "chooseReport", "toolbar=no,menubar=no,location=no," +
    "directories=no,status=no,scrollbars=auto,resizable=yes,height=300,width=500,top=50,left=50");
<%}
  else if (request.getParameter("downloadList") != null)
  {
  %>
  window.open("../rs/downloadList.jsp?fileName=<%=request.getParameter("downloadList")%>", "downloadList", "toolbar=no,menubar=no,location=no," +
    "directories=no,status=no,scrollbars=no,resizable=no,height=10,width=10,top=350,left=500");
<%}
  else if (request.getParameter("analyst") != null)
  {
    String parm = request.getParameter("conglom")!=null?"?conglom=true":"";
  %>
  window.open("../GCTBWeb/analyst.jsp<%=parm%>", "analyst", "toolbar=no,menubar=no,location=no," +
    "directories=no,status=no,scrollbars=no,resizable=no,height=400,width=450,top=50,left=50");
<%}
  else if (request.getParameter("dataSearch") != null)
  {
  %>
  window.open("../GCTBWeb/dataSearch.jsp", "dataSearch", "toolbar=no,menubar=no,location=no," +
    "directories=no,status=no,scrollbars=no,resizable=no,height=500,width=800,top=50,left=50");
<%}
  else if (request.getParameter("createInvoice") != null)
  {
  %>
  window.open("../GCTBWeb/createInvoice.jsp?gcId=<%=request.getParameter("gcId")%>&invoiceRegion=<%=request.getParameter("invoiceRegion")%>", "createInvoice", "toolbar=no,menubar=no,location=no," +
    "directories=no,status=no,scrollbars=no,resizable=no,height=300,width=450,top=50,left=50");
<%}
  else if (request.getParameter("createOneOffInvoice") != null)
  {
  %>
  window.open("../GCTBWeb/createOneOffInvoice.jsp", "createOneOffInvoice", "toolbar=no,menubar=no,location=no," +
    "directories=no,status=no,scrollbars=no,resizable=no,height=300,width=550,top=50,left=50");
<%}
  else if (request.getParameter("rePullGoldfish") != null)
  {
  %>
  window.open("../GCTBWeb/rePullGoldfish.jsp", "rePullGoldfish", "toolbar=no,menubar=no,location=no," +
    "directories=no,status=no,scrollbars=no,resizable=no,height=300,width=450,top=50,left=50");
<%}
  else if (request.getParameter("priorPeriod") != null)
  {
  %>
  window.open("../GCTBWeb/priorPeriodSetUp.jsp", "priorPeriod", "toolbar=no,menubar=no,location=no," +
    "directories=no,status=no,scrollbars=no,resizable=no,height=300,width=450,top=50,left=50");
<%}
  else if (request.getParameter("conglomDiscExclude") != null)
  {
  %>
  window.open("../GCTBWeb/conglomDiscountExclusion.jsp", "conglomDiscountExclusion", "toolbar=no,menubar=no,location=no," +
    "directories=no,status=no,scrollbars=no,resizable=no,height=450,width=450,top=50,left=50");
<%}
  else if (request.getParameter("submitGSOBilling") != null)
  {
  %>
  window.open("../GCTBWeb/submitGSOBilling.jsp", "submitGSOBilling", "toolbar=no,menubar=no,location=no," +
    "directories=no,status=no,scrollbars=no,resizable=no,height=200,width=350,top=50,left=50");
<%}
  else if (request.getParameter("createGSOInvoice") != null)
  {
  %>
  window.open("../GCTBWeb/createGSOInvoice.jsp?spId=<%=request.getParameter("spId")%>&spName=<%=URLEncoder.encode(request.getParameter("spName"))%>&invoiceCurrency=<%=request.getParameter("invoiceCurrency")%>", "createGSOInvoice", "toolbar=no,menubar=no,location=no," +
    "directories=no,status=no,scrollbars=no,resizable=no,height=300,width=450,top=50,left=50");
<%}
  else if (request.getParameter("conglomBill") != null)
  {
  %>
  window.open("../GCTBWeb/conglomBillGeneration.jsp?billType=<%=request.getParameter("billType")%>", "conglomBill", "toolbar=no,menubar=no,location=no," +
    "directories=no,status=no,scrollbars=no,resizable=no,height=300,width=450,top=50,left=50");
<%}
  else if (request.getParameter("gsoRejectDetail") != null)
  {
  %>
  window.open("../GCTBWeb/gsoRejectDetail.jsp?rowId=<%=request.getParameter("rowId")%>", "gsoRejectDetail", "toolbar=no,menubar=no,location=no," +
    "directories=no,status=no,scrollbars=no,resizable=no,height=500,width=500,top=50,left=50");
<%}
  else if (request.getParameter("crDocket") != null)
  {
  %>
  window.open("../GCTBWeb/updateCRDocket.jsp?interimNo=<%=request.getParameter("interimNo")%>&product=<%=request.getParameter("product")%>&sourceAccount=<%=request.getParameter("sourceAccount")%>", "crDocket", "toolbar=no,menubar=no,location=no," +
    "directories=no,status=no,scrollbars=no,resizable=no,height=300,width=500,top=50,left=50");
<%}
  else if (request.getParameter("conglomItemStatus") != null)
  {
  %>
  window.open("../GCTBWeb/updateConglomItemStatus.jsp?itemId=<%=request.getParameter("itemId")%>&product=<%=request.getParameter("product")%>&sourceAccount=<%=request.getParameter("sourceAccount")%>&status=<%=request.getParameter("status")%>", "conglomItemStatus", "toolbar=no,menubar=no,location=no," +
    "directories=no,status=no,scrollbars=no,resizable=no,height=500,width=500,top=50,left=50");
<%}
  else if (request.getParameter("billProfile") != null)
  {
    boolean isConglom = request.getParameter("conglom") != null;
    String conglom = isConglom?"?conglom=true":"";
    String height = isConglom?"650":"600";
  %>
  window.open("../GCTBWeb/billProfile.jsp<%=conglom%>", "billProfile", "toolbar=no,menubar=no,location=no," +
    "directories=no,status=no,scrollbars=no,resizable=no,height=<%=height%>,width=800,top=50,left=50");
<%}
  else if (request.getParameter("nYA") != null)
  {
  %>
  alert("Not yet available");
<%}
  else if (request.getParameter("msInvoice") != null)
  {
  %>
//alert("1");
  window.open("../GCTBWeb/msInvoiceImage.jsp", "invoiceView", "toolbar=no,menubar=no,location=no," +
    "directories=no,status=no,resizable=yes,height=740,width=675,top=50,left=50");
<%}
  else if (request.getParameter("standardInvoice") != null)
  {
  %>
//alert("2");
  window.open("../GCTBWeb/standardInvoiceImage.jsp", "invoiceView", "toolbar=no,menubar=no,location=no," +
    "directories=no,status=no,resizable=yes,height=740,width=675,top=50,left=50");
<%}
  else if (request.getParameter("strategicInvoice") != null)
  {
  %>
//alert("3");
  window.open("../GCTBWeb/strategicInvoiceImage.jsp", "invoiceView", "toolbar=no,menubar=no,location=no," +
    "directories=no,status=no,resizable=yes,height=740,width=675,top=50,left=50");
<%}
  else if (request.getParameter("extractConfirm") != null)
  {
  %>
  var msg = "Before running the GCD extract please check the GSR(s) you require:\n\r" +
    "\t* are on a Live status on GCD\n\r" +
    "\t* have a billing start date\n\r" +
    "\t* have a live service date\n\r" +
    "\t* have service items on GCD\n\r\n\r" +
    "Please note that updates made on GCD today will not be available to GCB until tomorrow\n\r\n\r" +
    "Press 'OK' to submit the extract or 'Cancel' to close this window without submitting.";
  if (confirm(msg))
  {
    newDesktop.ButtonPressed.value = "subExtract";
    newDesktop.submit();
  }
<%}
  else if (request.getParameter("submitConfirm") != null)
  {
  %>
  var msg = "<%=request.getParameter("confirmMsg")%>";
  if (confirm(msg))
  {
    billSubmission.ButtonPressed.value = "subBill";
  }
  else
  {
    billSubmission.ButtonPressed.value = "Desktop";
  }
  billSubmission.submit();
<%}%>
}
</script>
<title>Desktop</title>
<link rel="stylesheet" type="text/css" href="../nr/cw/newcss/world_ie.css">
<meta http-equiv="REFRESH" content="1801; url=../GCTBWeb/Login.jsp?type=t">
<meta http-equiv="PRAGMA" content="NO-CACHE">
<meta http-equiv="EXPIRES" content="-1">
</head>
<body topmargin="0" leftmargin="0" marginleft="0" margintop="0" onLoad="showQuery()" oncontextmenu="return false;">
