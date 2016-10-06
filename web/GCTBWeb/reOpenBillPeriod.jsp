<html>
<jsp:include page="../includes/Page_Header6.jsp" flush="true" />
<%@ page import="JavaUtil.*,HTMLUtil.*"%>
<jsp:useBean id="BAN" class="DBUtilities.BANBean" scope="session"/>
<jsp:useBean id="DBA" class="DBUtilities.DBAccess" scope="session"/>

<%String SQL;
  JavaUtil.StringUtil SU = new JavaUtil.StringUtil();
  HTMLUtil.HTMLBean HB = new HTMLUtil.HTMLBean();
  String Global_Customer_Id=BAN.getGlobalCustomerId();
  String Invoice_Region=BAN.getInvoice_Region();
  String INP=BAN.getInvoiceNo();
  String Message;
  String ActionAccount;
  String ActionInvoice;
  String ActionInvoiceClose;
  String Account_Qualifier=SU.isBlank(Global_Customer_Id,"");
  String INP_Qualifier=SU.isBlank(Invoice_Region,"");
  String Customer_Qualifier="All";
  //DBAccess DBA = new DBAccess();
  String PageSent;
  String sharedPath=
        EBANProperties.getEBANProperty(EBANProperties.SHAREDPATH);
  String system = (String)session.getAttribute("System");
  String Menu_Headings[]={"Go To", "Options"};
  String Options[][] = {{"Log Off","Desktop"},
                          {"Refresh","Raise Query"}};
  HB.setMenu_Headings(Menu_Headings);
  HB.setOption_Array(Options);
  boolean invoiceClosed = false;
  Message=SU.isNull(BAN.getMessage(),SU.isNull((String)session.getAttribute("Message"),""));

  BAN.setMessage("");
  String selectStyle = " style=\"width:400px\"";
  session.setAttribute("formname","reOpenBillPeriod");

  String rslt = Invoice_Region.equals("")?"":BAN.getClosedInvoiceList();
  if ((Message.equals("")) && (rslt.startsWith("<font")))
  {
    Message = rslt;
  }
%>
<script language="JavaScript">
  function accChange()
  {
    reOpenBillPeriod.accountName.value = reOpenBillPeriod.GCB_Account2[reOpenBillPeriod.GCB_Account2.selectedIndex].text;
    reOpenBillPeriod.submit();
  }

  function Toggle_Sort(buttonPressed)
  {
    if (buttonPressed == 'Action')
    {
      window.open("reOpenBillPeriodKey.htm", "ahk", "toolbar=no,menubar=no,location=no," +
        "directories=no,status=no,scrollbars=no,resizable=no,height=200,width=200,top=50,left=50");
    }
  }
</script>
<form name="reOpenBillPeriod" method="post" action="reOpenBillPeriodHandler.jsp">
<input name="ButtonPressed" type=hidden value="">
<input name="accountName" type=hidden value="">
<input name="invoiceNo" type=hidden value="">
<a name="top"></a>
<table id=1 border=0 width="100%">
  <tr>
    <td colspan=3>
      <%@ include file="../includes/Page_Header4.htm"%>
    </td>
  </tr>
  <tr><!-- This row holds the local menu bar-->
    <td colspan=3>
      <%=HB.getMenu_Bar()%>
    </td>
  </tr>

    <tr>
      <td colspan=3>
	<table width="100%" border="0" cellspacing="0" cellpadding="0" id=2>
	  <tr>
	    <td width="13">&nbsp;
	    <!-- this is a spacer column-->
	    </td>
	    <td width="100" valign="top" align="left">
	    <!--former menu column-->
	    </td>
	    <td width="12"><!-- a spacer column-->
		&nbsp;
	    </td>
	    <td valign="top">
	      <h3 align="center"><b>Re-open Bill Period</b></h3>
	       <%=Message%><br>
	       <%session.setAttribute("Message","");%></font>
	      <table width="100%" border="0">
	        <tr>
                  <td colspan="2">&nbsp;</td>
		</tr>
	        <tr>
		  <td align=left colspan=2><font color="#0000FF">Customer:</font>
		  </td>
		  <td align=left>
		    <%=DBA.getListBox("GCB_Customer2","submit",Global_Customer_Id,BAN.getActAsLogon(),1,selectStyle,true)%>
		  </td>
                  <td>&nbsp;</td>
		</tr>
                <tr>
                  <td height=20 colspan=2>&nbsp;</td>
		</tr>
		</table>
		</form>
	      </td>
	    </tr>
	  </table><!--2-->
	</td>
      </tr>
     <tr>
      <td colspan=3 align=center>
	<table width="500" border="0" id=4>
          <tr class=gridHeader>
            <td class=gridHeader NOWRAP valign=top width=200>
              <button class=grid_menu>Invoice Number</button>
            </td>
            <td class=gridHeader NOWRAP valign=top width=200>
              <button class=grid_menu>Close Date</button>
            </td>
            <td class=gridHeader NOWRAP valign=top>
              <button class=grid_menu onclick="Toggle_Sort('Action')">Action</button>
            </td>
          </tr>
          <tr>
            <td colspan=5>
            <iframe frameborder=0 width="100%" height=200 id=GridData name=GridData src="reOpenBillPeriodGrid.jsp"></iframe>
            </td>
          </tr>
        </table><!--4-->
      </td>
    </tr>
      <!--Footer-->
      <tr>
        <td>
	  &nbsp;
	</td>
      </tr>


</table><!--1-->
</form>
</body>
</html>


