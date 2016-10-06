<html>
<jsp:include page="../includes/Page_Header6.jsp" flush="true" />
<%@ page import="JavaUtil.*,HTMLUtil.*"%>
<jsp:useBean id="BAN" class="DBUtilities.BANBean" scope="session"/>
<jsp:useBean id="adjBAN" class="DBUtilities.AdjustmentBANBean" scope="session"/>
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
  adjBAN.setMessage("");
  String selectStyle = " style=\"width:400px\"";
  session.setAttribute("formname","adHocInvoice");

  if (Invoice_Region.equals(""))
  {
    ActionAccount="alert('You must select a Customer and Invoice Region for this option')";
  }
  else
  {
    ActionAccount="submitForm(this)";
  }
  if (INP.equals(""))
  {
    ActionInvoice="alert('You must select an Invoice for this option')";
    ActionInvoiceClose="alert('You must select an Invoice for this option')";
  }
  else
  {
    invoiceClosed = DBA.checkInvoiceClosed(Global_Customer_Id, Invoice_Region, INP);
    String rslt = adjBAN.populateAdjustmentList(invoiceClosed);
    if ((Message.equals("")) && (rslt.startsWith("<font")))
    {
      Message = rslt;
    }
    if (invoiceClosed)
    {
      ActionInvoice="alert('Invoice number " + INP + " has been closed and cannot be amended')";
      ActionInvoiceClose="alert('Invoice number " + INP + " has been closed already')";
    }
    else
    {
      ActionInvoice="submitForm(this)";
      if (DBA.invoiceHasAdjustments(Global_Customer_Id, Invoice_Region, INP))
      {
        ActionInvoiceClose="submitConfirm()";
      }
      else
      {
        ActionInvoiceClose="alert('Invoice number " + INP + " cannot be closed until it has at least one adjustment added to it')";
      }
    }
  }
  /*StringBuffer invoiceAdjustmentTotal = new StringBuffer(SU.roundToString(adjBAN.getInvoiceAdjustmentTotal(), 2));
  if (invoiceAdjustmentTotal.charAt(invoiceAdjustmentTotal.length()-2) == '.')
  {
    invoiceAdjustmentTotal.append("0");
  }*/
%>
<script language="JavaScript">
  function accChange()
  {
    adHocInvoice.accountName.value = adHocInvoice.GCB_Account2[adHocInvoice.GCB_Account2.selectedIndex].text;
    adHocInvoice.submit();
  }

  function submitConfirm()
  {
    var msg = "WARNING! If you close this invoice you will no longer be able " +
      "to amend it or any adjustments associated with it.\n\r" +
      "Press 'OK' to confirm you want to close thie invoice.";
    if (confirm(msg))
    {
      adHocInvoice.ButtonPressed.value = "Close an Ad Hoc Invoice";
      adHocInvoice.submit();
    }
  }
  function Toggle_Sort(buttonPressed)
  {
    if (buttonPressed == 'Action')
    {
      window.open("adHocInvoiceKey.htm", "ahk", "toolbar=no,menubar=no,location=no," +
        "directories=no,status=no,scrollbars=no,resizable=no,height=200,width=200,top=50,left=50");
    }
  }
</script>
<form name="adHocInvoice" method="post" action="adHocInvoiceHandler.jsp">
<input name="ButtonPressed" type=hidden value="">
<input name="accountName" type=hidden value="">
<input name="adjustmentId" type=hidden value="">
<input name="accountId" type=hidden value="">
<input name="iRId" type=hidden value="">
<input name="total" type=hidden value="">
<input name="archived" type=hidden value="">
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
	      <h3 align="center"><b>Ad Hoc Invoices and Adjustments</b></h3>
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
		  <td align=left colspan=2><font color="#0000FF">Invoice Region:</font>
		  </td>
		  <td  align=left>
		  <%=DBA.getListBox("GCB_Account2","onChange=\"accChange()\"",Invoice_Region,Account_Qualifier,1,selectStyle,true)%>
		  </td>
                  <td>&nbsp;</td>
		</tr>
		<tr>
		  <td align=left colspan=2><font color="#0000FF">Invoice Number:</font>
		  </td>
		  <td  align=left>
		  <%=DBA.getListBox("Ad_Hoc_Invoice_Number","submit",INP,Account_Qualifier,INP_Qualifier,1,selectStyle,true)%>
		  </td>
                  <td>&nbsp;</td>
		</tr>
		  <tr>
                  <td height=20 colspan=2>&nbsp;</td>
		</tr>
		  <tr>
                    <td colspan=2>&nbsp;</td>
		    <td height=20>
		      <img src='<%=sharedPath%>/nr/cw/newimages/menu_brown.gif'>&nbsp
		      <%=HB.getButton("Create an Ad Hoc Invoice",ActionAccount)%>
		    </td>
		  </tr>
		  <tr>
                    <td colspan=2>&nbsp;</td>
		    <td height=20>
		      <img src='<%=sharedPath%>/nr/cw/newimages/menu_brown.gif'>&nbsp
		      <%=HB.getButton("Add an Adjustment",ActionInvoice)%>
		    </td>
		  </tr>
		  <tr>
                    <td colspan=2>&nbsp;</td>
		    <td height=20>
		      <img src='<%=sharedPath%>/nr/cw/newimages/menu_brown.gif'>&nbsp
		      <%=HB.getButton("Close an Ad Hoc Invoice",ActionInvoiceClose)%>
		    </td>
		  </tr>
		</table>
		</form>
	      </td>
	    </tr>
	  </table><!--2-->
	</td>
      </tr>
     <tr>
      <td colspan=3 align=center height=25>&nbsp;</td>
     </tr>
<%if (!adjBAN.getAdjustmentList().equals(""))
  {
%>
     <tr>
      <td colspan=3 align=center>
	<table width="72%" border="0" id=4>
          <tr>
            <td colspan=5 class=optional>
              Adjustments for Invoice <%=INP%> - Total Value: <%=adjBAN.getInvoiceAdjustmentTotal().toString()%>
<%if (invoiceClosed)
  {%>
              <font color=red><b>(Invoice Closed)</b></font>
<%}%>
            </td>
          </tr>
          <tr class=gridHeader>
            <td class=gridHeader NOWRAP valign=top width=100>
              <button class=grid_menu>Adjustment Date</button>
            </td>
            <td class=gridHeader NOWRAP valign=top width=300>
              <button class=grid_menu>Description</button>
            </td>
            <td class=gridHeader NOWRAP valign=top width=100>
              <button class=grid_menu>Amount</button>
            </td>
            <td class=gridHeader NOWRAP valign=top width=70>
              <button class=grid_menu>Status</button>
            </td>
            <td class=gridHeader NOWRAP valign=top>
              <button class=grid_menu onclick="Toggle_Sort('Action')">Action</button>
            </td>
          </tr>
          <tr>
            <td colspan=5>
            <iframe frameborder=0 width="100%" height=200 id=GridData name=GridData src="adjustmentGrid.jsp"></iframe>
            </td>
          </tr>
        </table><!--4-->
      </td>
    </tr>
<%}
%>
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


