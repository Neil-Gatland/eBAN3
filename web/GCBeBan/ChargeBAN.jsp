<html>
<%@ include file="../includes/Page_Header1.htm"%>
<%@ page import="HTMLUtil.HTMLBean,JavaUtil.StringUtil,JavaUtil.EBANProperties"%>
<jsp:useBean id="svBAN" class="DBUtilities.ServiceBANBean" scope="session"/>
<jsp:useBean id="chBAN" class="DBUtilities.GCBChargeBANBean" scope="session"/>
<jsp:useBean id="DBA" class="DBUtilities.DBAccess" scope="session"/>
  <%!//Variables
    StringBuffer SQL2 = new StringBuffer("");
    String Product,Global_Customer_Id,Global_Customer_Name,Invoice_Region,Service_Reference;
    String Service_Description="",Site="",Contract_Number="";
    String BAN_Summary,BAN_Reason,Product_Type="",Account_Id="",Tax_Type="";
    String Required_BAN_Effective_Date;
    String From_End,To_End,BAN_Identifier="",Revenue_Reason_Code="",Revenue_Type_Code="";
    String SelectMode="",Calendar="",InputMode="",Mode="";
    String Charge_Type="",Charge_Category="",Charge_Category_Display,CRDB,Charge_Frequency="Quarterly";
    String Charge_Description="",Split_Sites="0";
    String Gross_Amount,Currency_Desc,Currency_Display,From_Charge_Valid_Date,
      To_Charge_Valid_Date,Action,Unit_Quantity;
    String desc1="Line1";

    JavaUtil.StringUtil SU = new JavaUtil.StringUtil();
    HTMLUtil.HTMLBean HB = new HTMLUtil.HTMLBean();
    //DBUtilities.DBAccess DBA = new DBUtilities.DBAccess();
    String enabledLists = "";
    int BEDay, BEMonth, BEYear, FCVDay, FCVMonth, FCVYear, TCVDay, TCVMonth,
      TCVYear;
    String chargeTypeExtra="",splitSitesExtra="",Package_Type="";
    int Package;
   %>
  <%//Initialization
    if (chBAN.getProduct().equals(""))
    {
      Product=(String)session.getAttribute("Product");
      chBAN.setProduct(Product);
    }
    else
    {
      Product=chBAN.getProduct();
    }
    Charge_Category=chBAN.getCharge_Category();
    Charge_Type=chBAN.getCharge_Type();
    if (Product.equals("01") || Product.equals("02") ||
      Product.equals("03") || Product.equals("04"))
    {
      enabledLists = "disabled";
      chBAN.setCharge_Frequency(EBANProperties.getEBANProperty("GCB.chargeFrequency"));
      chBAN.setRevenue_Reason_Code(EBANProperties.getEBANProperty("GCB.revenueReasonCode"));
      chargeTypeExtra="<input type=\"hidden\" name=\"GCB_Charge_Type\" value=\""+
      Charge_Type+"\">";
      splitSitesExtra="<input type=\"hidden\" name=\"Split_Sites\" value=\""+
      EBANProperties.getEBANProperty("GCB.splitSites")+"\">";
    }
    Global_Customer_Id=chBAN.getGlobalCustomerId();
    Account_Id=chBAN.getAccount_Id();
    Global_Customer_Name=chBAN.getGlobalCustomerName();

    Mode=chBAN.getMode();
    SelectMode=chBAN.getSelectMode();
    boolean disableDate=SelectMode.equals("DISABLED")?true:false;
    InputMode=chBAN.getInputMode();
    Calendar=chBAN.getCalendar();

    BAN_Identifier=chBAN.getBanIdentifier();
    Action=chBAN.getAction();

    BAN_Summary = chBAN.getBAN_Summary();
    BAN_Reason = chBAN.getBAN_Reason();
    Service_Description=SU.isNull(svBAN.getService_Description(),"");
    Required_BAN_Effective_Date=SU.isNull(chBAN.getRequired_BAN_Effective_Date(),"Today");
    Service_Reference=svBAN.getService_Reference();
    Product_Type=svBAN.getProduct_Type();
    From_End=svBAN.getFrom_End();
    To_End=SU.isNull(svBAN.getTo_End(),"");
    Site=chBAN.getSite();
    Charge_Category_Display=chBAN.getCharge_CategoryDisplay();
    Revenue_Reason_Code=chBAN.getRevenue_Reason_Code();
    Revenue_Type_Code=chBAN.getRevenue_Type_Code();
    Contract_Number=SU.isNull(chBAN.getContract_Number(),"");
    Charge_Frequency=chBAN.getCharge_Frequency();

    CRDB=chBAN.getCRDB();
    Account_Id=chBAN.getAccount_Id();
    Invoice_Region=chBAN.getInvoice_Region();

    Charge_Description  = chBAN.getCharge_Description();
    Gross_Amount = chBAN.getGross_Amount();
    Unit_Quantity = chBAN.getUnit_Quantity();
    Currency_Desc = chBAN.getCurrency_Desc();
    Currency_Display = chBAN.getCurrency_Display();
    From_Charge_Valid_Date=SU.isNull(chBAN.getCharge_Valid_From_Date(),"Today");
    To_Charge_Valid_Date=chBAN.getCharge_Valid_To_Date();
    Split_Sites=chBAN.getSplit_Sites();
    Charge_Type=chBAN.getCharge_Type();
    Tax_Type=chBAN.getTax_Type();
    BEDay = chBAN.getBANEffectiveDateDays();
    BEMonth = chBAN.getBANEffectiveDateMonths();
    BEYear = chBAN.getBANEffectiveDateYears();
    FCVDay = chBAN.getFromChargeValidDateDays();
    FCVMonth = chBAN.getFromChargeValidDateMonths();
    FCVYear = chBAN.getFromChargeValidDateYears();
    TCVDay = chBAN.getToChargeValidDateDays();
    TCVMonth = chBAN.getToChargeValidDateMonths();
    TCVYear = chBAN.getToChargeValidDateYears();
    Package_Type=chBAN.getPackageType();
    Package=chBAN.getPackageId();
   %>
<table border=0 id=1 width="100%">
  <tr><!-- This row holds the standard C&W intranet bar-->
    <td colspan=2>
      <%@ include file="../includes/Page_Header4.htm"%>
    </td>
  </tr>
  <tr><!-- This row holds the menu bar-->
      <td colspan=2>
	<form name="ChargeBAN" method="post" action="ChargeBANHandler.jsp">
        <%=HB.getBANMenu_Bar("BAN",Mode,Action,(String)session.getAttribute("System"))%>

        <input name="ButtonPressed" type=hidden value="">
        <input name="RejectReason" type=hidden value="">
	<table width="100%" border="0" cellspacing="0" cellpadding="0" id=2>
	  <tr>
	    <!-- this is a spacer column-->
	    <td width="13">&nbsp;</td>
	    <td valign="top" align="left" NOWRAP width=10><!--Formerly Menu Column--></td>
	    <!-- this is a spacer column-->
	    <td width="13">&nbsp;</td>
	    <td  valign ="Top"><!-- Main table starts here-->
	      <table width="100%" border="0" cellpadding="0" cellspacing="0" id=3>
		<tr>
		  <td valign="top" colspan=2>
		    <h3 align="center"><%=Mode%> <%=Charge_Category_Display%></h3>
		    <%=SU.hasValue(BAN_Identifier,"</td></tr><tr><td><font color=darkblue><b>BAN Id:- "+BAN_Identifier+"<td>")+SU.isBlank(chBAN.getMessage(),"<font color=blue><b>Mandatory fields are shown in blue</b>")%>
		  </td>
		</tr>
		<tr>
		  <td valign="top" height="341" colspan=2>
		    <table width="100%" border="0"  cellpadding="0" cellspacing="0" id=4>
		      <tr>
			<td colspan=4>
			  <table width=100%>
			    <tr>
			      <td><b>Product:</b></td>
			      <td class=bluebold><%=chBAN.getProductName()%></td>
			      <td cosplan=2>&nbsp;</td>
			    </tr>
			    <tr>
			      <td><b>Customer:</b></td>
			      <td class=bluebold><%=Global_Customer_Name%></td>
			      <td><b>Division:</b></td>
			      <td class=bluebold><%=svBAN.getDivision_Id()%></td>
			    </tr>
			    <tr>
			      <td><b>Billing Advice Status:</td>
			      <td class=bluebold><%=chBAN.getBanStatus()%></td>
			      <td><b>Requested By:</td>
			      <td class=bluebold>
				<%=SU.isNull(chBAN.getBanCreatedBy(),(String)session.getAttribute("User_Name"))%><br>
			      </td>
			    </tr>
			  </table>
			</td>
		      </tr>
		      <%
			if (Charge_Category.compareTo("05")==0)
		        {
			  %><%
		        }
		      %>
		      <tr>
		        <td colspan=4>
			  <hr>
		        </td>
		      </tr>
		      <!-- Service Details go here -->
		      <tr>
			<td><b><small>Service Reference: </b><%=Service_Reference%>
			</td>
			<td><b>
			</td>
			<td colspan=2><b><small>From End: </b><%=From_End%>
			</td>
  		      </tr>
		      <tr>
			<td colspan=2><b><small>Service Description: </b><%=Service_Description%>
			</td>
			<td colspan=2><b><small>To End: </b><%=To_End%>
			</td>
		      </tr>
      		      <!-- End of Service Details-->
		      <%
			if (Charge_Category.compareTo("05")==0)
		        {
			  %><%
		        }
		      %>
		      <tr>
			<td colspan=4>
			  <hr>
			</td>
		      </tr>
		      <tr>
			<td class=<%=chBAN.getClass("BAN_Summary")%>>
			  <b><small>BAN summary
		          <br><textarea class=tworows name="BAN_Summary" cols="40" rows="2" <%=chBAN.getMode("BAN_Summary")%>><%=BAN_Summary%></textarea>
		        </td>
		        <td class=<%=chBAN.getClass("BAN_Reason")%>>
			  <b><small>BAN Reason
		          <br><textarea class=tworows name="BAN_Reason" cols="40" rows="2" <%=chBAN.getMode("BAN_Reason")%>><%=BAN_Reason%></textarea>
		        </td>
		        <td class=<%=chBAN.getClass("BANEffectiveDateh")%>>BAN Effective Date<br>
                          <%=HB.getDays("ChargeBAN", "BANEffectiveDate", BEDay, false, disableDate)%>
                          <%=HB.getMonths("ChargeBAN", "BANEffectiveDate", BEMonth, false, disableDate)%>
                          <%=HB.getYears("ChargeBAN", "BANEffectiveDate", BEYear, false, disableDate)%>
                          <input type="hidden" name="BANEffectiveDateh" value="<%=Required_BAN_Effective_Date%>">
		        </td>
			<td>
			  <span style="VISIBILITY: <%=chBAN.getRejectVisibility()%>">
			    <b><small><%=chBAN.getBanStatus()%> Reason
		            <br><textarea class=red cols="40" rows="2" READONLY><%=chBAN.getRejectReason()%></textarea>
			  </span>
		        </td>
		      </tr>
		      <tr>
		        <td colspan=4 class=bluebold><hr></td>
		      </tr>
		      <tr>
		        <td class=<%=chBAN.getClass("Charge_Type")%> valign=top>
			  <b><small>Charge Type<br>
			  <%=DBA.getListBox("GCB_Charge_Type",SelectMode,Charge_Type,Charge_Category,1,enabledLists,false)%>
                          <%=chargeTypeExtra%>
		        </td>
		        <td class=<%=chBAN.getClass("Site")%>>
			  <span style="VISIBILITY: <%=chBAN.getChargeFieldVisibility("Site")%>"><b><small>Site<br>
			    <%=DBA.getListBox("Site",SelectMode,Site,Global_Customer_Id)%>
			  </span>
		        </td>
		        <td class=<%=chBAN.getClass("Split_Sites")%>>
			  <span style="VISIBILITY: <%=chBAN.getChargeFieldVisibility("Split_Sites")%>"><b><small>No of Split Sites<br>
    			    <%=DBA.getListBox("Split_Sites",SelectMode,Split_Sites,"15",1,enabledLists,false)%>
                            <%=splitSitesExtra%>
			  </span>
		        </td>
		        <td class=<%=chBAN.getClass("Contract_Number")%> valign=top>
			  <span><b><small>Contract Number<br>
			    <input class=inp type="text" name="Contract_Number" maxlength="25" size="25" value="<%=Contract_Number%>" <%=chBAN.getMode("Contract_Number")%>>
			  </span>
		        </td>
		      </tr>
		      <tr>
		        <td class=<%=chBAN.getClass("Gross_Amount")%>><b><small>Unit Charge Amount<br>
			  <input class=inp type="text" name="Gross_Amount" maxlength="25" size="25" value="<%=Gross_Amount%>" <%=chBAN.getMode("Gross_Amount")%>>
			</td>
		        <td class=<%=chBAN.getClass("Unit_Quantity")%> style="visibility:<%=chBAN.getChargeFieldVisibility("Unit_Quantity")%>"><b><small>Unit Quantity<br>
			  <input class=inp type="text" name="Unit_Quantity" maxlength="25" size="25" value="<%=Unit_Quantity%>" <%=chBAN.getMode("Unit_Quantity")%>>
			</td>
		        <td class=<%=chBAN.getClass("Currency_Desc")%> valign=top>
			  <span><b><small>Charge Currency<br>
			    <%=DBA.getListBox("Currency_Desc",SelectMode,Currency_Desc,"")%>
			  </span>
		        </td>
		        <td class=<%=chBAN.getClass("Charge_Frequency")%>>
			  <span style="VISIBILITY: <%=chBAN.getChargeFieldVisibility("Charge_Frequency")%>"><b><small>Charge Frequency<br>
			    <%=DBA.getListBox("Charge_Frequency",InputMode,Charge_Frequency,"",1,enabledLists,false)%>
			  </span>
		        </td>
		      </tr>
		      <tr>
                        <!--
			<td class=<%=chBAN.getClass("From_Charge_Valid_Dateh")%>><%=chBAN.getchargeDateHeading()%><br>
			  <input class=inp type="text" name="From_Charge_Valid_Date" READONLY value=<%=From_Charge_Valid_Date%>>
		          <a <%=Calendar%>href="javascript:show_calendar('ChargeBAN.From_Charge_Valid_Date');" onmouseover="window.status='Date Picker';return true;" onmouseout="window.status='';return true;">
			  <img src="/shared/nr/cw/newimages/show_calendar.gif" border=0></a>
			  <input type=hidden name="From_Charge_Valid_Dateh" value=<%=From_Charge_Valid_Date%>>
		        </td>
                        -->
		        <td class=<%=chBAN.getClass("FromChargeValidDateh")%>><%=chBAN.getchargeDateHeading()%><br>
                          <%=HB.getDays("ChargeBAN", "FromChargeValidDate", FCVDay, false, disableDate)%>
                          <%=HB.getMonths("ChargeBAN", "FromChargeValidDate", FCVMonth, false, disableDate)%>
                          <%=HB.getYears("ChargeBAN", "FromChargeValidDate", FCVYear, false, disableDate)%>
                          <input type="hidden" name="FromChargeValidDateh" value="<%=From_Charge_Valid_Date%>">
		        </td>
		        <td class=<%=chBAN.getClass("ToChargeValidDateh")%> style="visibility: <%=chBAN.getChargeFieldVisibility("ToDate")%>">
                          <b><small>Valid To Date</small></b><br>
                          <%=HB.getDays("ChargeBAN", "ToChargeValidDate", TCVDay, true, /*disableDate*/true)%>
                          <%=HB.getMonths("ChargeBAN", "ToChargeValidDate", TCVMonth, true, /*disableDate*/true)%>
                          <%=HB.getYears("ChargeBAN", "ToChargeValidDate", TCVYear, true, /*disableDate*/true)%>
                          <input type="hidden" name="ToChargeValidDateh" value="<%=To_Charge_Valid_Date%>">
		        </td>
                        <!--
		        <td class=<%=chBAN.getClass("To_Charge_Valid_Date")%>>
			  <span style="VISIBILITY: <%=chBAN.getChargeFieldVisibility("ToDate")%>"><b><small>Valid To Date<br>
			    <input class=inp type="text" name="To_Charge_Valid_Date" READONLY value=<%=To_Charge_Valid_Date%>>
			    <a <%=chBAN.getMode("To_Charge_Valid_Date")%>href="javascript:show_calendar('ChargeBAN.To_Charge_Valid_Date');" onmouseover="window.status='Date Picker';return true;" onmouseout="window.status='';return true;">
			    <img src="/shared/nr/cw/newimages/show_calendar.gif" border=0></a>
			    <input type=hidden name="To_Charge_Valid_Dateh" value=<%=To_Charge_Valid_Date%>>
		          </span>
		        </td>
                        -->
		        <td class=<%=chBAN.getClass("Tax_Type")%> valign=top style="visibility:<%=chBAN.getChargeFieldVisibility("Tax_Type")%>">
			  <span><b><small>Tax_Type<br>
			    <%=DBA.getListBox("Tax_Type",InputMode,Tax_Type,Global_Customer_Id + " " + Site)%>
			  </span>
		        </td>
		        <td height="18" class=<%=chBAN.getClass("CRDB")%>>
			  <span style="VISIBILITY: <%=chBAN.getChargeFieldVisibility("CRDB")%>"><b><small>Credit or Debit<br>
			  <%
			    SQL2
			     = new StringBuffer("");
			    SQL2.append("select 'Credit' Name, 'Credit' Value where 'Credit' <> '");
			    SQL2.append(CRDB).append("' and '").append(Charge_Category).append("' not in ('01','02')");
			    SQL2.append(" union select 'Debit' Name, 'Debit' Value where 'Debit' <> '");
			    SQL2.append(CRDB).append("' and '").append(Charge_Category).append("' not in ('03','04')");
			  %>
			  <%=DBA.getOptionList("CRDB",SQL2.toString(),false,CRDB,null)%>
			  </span>
			</td>
		      </tr>
		      <tr>
		        <td class=<%=chBAN.getClass("Package")%>><b><small>Package<br>
			    <%=DBA.getListBox("Package",SelectMode,Package,Package_Type)%>
			</td>
		        <td class=<%=chBAN.getClass("Charge_Description")%>><b><small>Description to appear on invoice<br>
			    <%=DBA.getListBox("Charge_Description",InputMode,Charge_Description,Package,
                              Charge_Type)%>
			</td>
		        <td class=<%=chBAN.getClass("Revenue_Type_Code")%> valign=top>
			  <span><b><small>Revenue Type<br>
			    <%=DBA.getListBox("GCB_Revenue_Type",InputMode,Revenue_Type_Code,Charge_Type)%>
			  </span>
		        </td>
		        <td class=<%=chBAN.getClass("Revenue_Reason_Code")%> valign=top>
			  <span><b><small>Revenue Reason<br>
			    <%=DBA.getListBox("GCB_Revenue_Reason",InputMode,Revenue_Reason_Code,Charge_Type,1,enabledLists,false)%>
			  </span>
		        </td>
		      </tr>
		      <tr>
			<td colspan=4>
			  <%=DBA.get_Site_Splits(Global_Customer_Id,Site,SU.toInt(Split_Sites),InputMode,chBAN.getSplitSitesTable(),chBAN.getClass("Splits_Table"))%>
			</td>
		      </tr>
		    </table><!--4-->
		  </td>
		</tr>
	      </table><!--3-->
	    </td>
	  </tr>
        </table><!--2-->
        </form>
      </td>
    </tr>
    <!--Footer-->
    <tr>
      <td>
      </td>
    </tr>
  </table><!--1-->
</body>
</html>


