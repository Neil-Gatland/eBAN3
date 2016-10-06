<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/1999/REC-html401-19991224/loose.dtd">
<html>
<%@ include file="../includes/Page_Header1.htm"%>
<%@ page import="DBUtilities.DBAccess,HTMLUtil.HTMLBean,JavaUtil.*"%>
<jsp:useBean id="BAN" class="DBUtilities.BANBean" scope="session"/>
<jsp:useBean id="acb" class="DBUtilities.AssureChargeBean" scope="session"/>

<%! String SQL;
    String ActionAccount="";
    String Message;
    HTMLUtil.HTMLBean HB = new HTMLUtil.HTMLBean();
    JavaUtil.StringUtil SU = new JavaUtil.StringUtil();
    DBAccess DBA = new DBAccess();
    String PageSent,SelectMode,InputMode,BAN_Identifier;
    String Account_Id,Account_Name;

%>
<%//Set Initial values
    HB.setGroupName((String)session.getAttribute("Group_Name"));
    String Customer_Name = acb.getCustomerName();
    String Account_Filter = acb.getAccountFilter();
    String Requested_By = acb.getBanCreatedBy();
    String Mode=acb.getMode();
    String Action=acb.getAction();
    SelectMode=acb.getSelectMode();
    boolean disableDate=SelectMode.equals("DISABLED")?true:false;
    InputMode=acb.getInputMode();
    String FaultCodeMode = "onClick=\"alert('test');\"";
    int VDay = acb.getVisitDateDays();
    int VMonth = acb.getVisitDateMonths();
    int VYear = acb.getVisitDateYears();
    String Site_Address_1 = acb.getSite_Address_1();
    String Site_Address_2 = acb.getSite_Address_2();
    String Site_Address_3 = acb.getSite_Address_3();
    String Site_Postcode = acb.getSite_Postcode();
    String Site_Country = acb.getSite_Country();
    String Customer_Info = acb.getCustomer_Info();
    String Signatory = acb.getSignatory();
    String SR_Ref = acb.getSR_Ref();
    String C002_Ref = acb.getC002_Ref();
    String CW_Info = acb.getCW_Info();
    String Invoice_Amount = acb.getInvoice_Amount();
    String Fault_Code = acb.getFault_Code();
    String Invoice_Currency = acb.getInvoice_Currency();
    String Ad_Hoc_Product = acb.getProduct();
    String Visit_Date=SU.isNull(acb.getVisit_Date(),"Today");
    String Contact_Name = acb.getContact_Name();
    String Contact_Telephone = acb.getContact_Telephone();
    String Contact_Mobile = acb.getContact_Mobile();
    String Contact_Fax = acb.getContact_Fax();
    String Contact_Email = acb.getContact_Email();
    String Fault_Details = acb.getFault_Details();
    String FDVis = acb.getFDVis();
    String Circuit_Ref = acb.getCircuit_Ref();
    String Billing_Source = acb.getBillingSource();
    String Status = acb.getBanStatus();
    String Job_Title = acb.getJob_Title();
    String Engineer = acb.getEngineer();

  //Are we coming from the handler page, or a fresh start?

  //Message=SU.isNull(BAN.getMessage(),SU.isNull((String)session.getAttribute("Message"),""));
    Message=SU.isNull((String)session.getAttribute("Message"),"");
    PageSent=SU.isNull((String)session.getAttribute("PageSent"),"");
    Account_Id=acb.getAccount_Id();
    Account_Name=acb.getAccountName();
    BAN_Identifier = acb.getBanIdentifier();
%>
<script language="JavaScript" src="../includes/eBanfunctions.js">
</script>
<script language="JavaScript">
<!--
function Ad_Hoc_Account_List_Process()
{
  AssureCharge.Account.value =
    AssureCharge.Ad_Hoc_Account[AssureCharge.Ad_Hoc_Account.selectedIndex].text;
  if (AssureCharge.Ad_Hoc_Account[AssureCharge.Ad_Hoc_Account.selectedIndex].value == 1)
  {
    AssureCharge.submit();
  }
  else
  {
    window.open('BillingSourcePopUp.jsp?whereFrom=ach&Account='+AssureCharge.Account.value,
    'bspop','toolbar=no,menubar=no,location=no,directories=no,status=no,scrollbars=auto,resizable=yes,height=135,width=330,top=250,left=400');
  }
}
function searchClick()
{
  if (AssureCharge.Account_Filter.value == "")
  {
    alert('Please enter an account filter value');
  }
  else
  {
    AssureCharge.submit();
  }
}
function detsOver(thistag)
{
  obj = document.getElementById(thistag);
  obj.style.visibility = "visible";
}
function detsOut(thistag)
{
  obj = document.getElementById(thistag);
  obj.style.visibility = "hidden";
}
function printInvoice()
{
  window.open('Print.jsp','printpop',
  'toolbar=no,menubar=no,location=no,directories=no,status=no,scrollbars=auto,resizable=yes,height=600,width=800,top=0,left=0');
}
//-->
</script>
<table border=0 id=1 width="100%">
  <tr><!-- This row holds the standard C&W intranet bar-->
    <td colspan=2>
      <%@ include file="../includes/Page_Header4.htm"%>
    </td>
  </tr>
  <tr><!-- This row holds the menu bar-->
      <td colspan=2>
	<form name="AssureCharge" method="post" action="AssureChargeHandler.jsp">
        <%=HB.getBANMenu_Bar("BAN",Mode,Action,(String)session.getAttribute("System"),Status)%>

        <input name="ButtonPressed" type=hidden value="">
        <input name="RejectReason" type=hidden value="">
        <input name="AuthManager" type=hidden value="">
        <input name="AuthDetails" type=hidden value="">
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
		  <td valign="top" colspan=2><br>
		    <h3 align="center"><%=Mode%> Assure Charge
                    <%=SU.hasValue(BAN_Identifier," "+BAN_Identifier+" ")%></h3>
		    <%=SU.isBlank(acb.getMessage(),"</td></tr><tr><td><font color=blue><b>Mandatory fields are shown in blue</b>")%>
		  </td>
		</tr>
		<tr>
		  <td valign="top" height="341" colspan=2>
		    <table width="100%" border="0"  cellpadding="0" cellspacing="0" id=4>
		      <tr>
                        <td><b>Customer:</b></td>
                        <td class=bluebold><%=Customer_Name%></td>
                        <td><b>Requested By:</b></td>
                        <td class=bluebold><%=Requested_By+acb.getDisplayCreatedDate()%></td>
		      </tr>
                      <%if (Status.equals("Implemented"))
                        {%>
                      <tr>
                        <td colspan="2">&nbsp;</td>
                        <td><b>Implemented By:</b></td>
                        <td class=bluebold><%=acb.getAuthorisor()+acb.getDisplayAuthorisedDate()%></td>
		      </tr>
                      <%}%>
		      <tr>
			      <td><b>Billing Source:</b></td>
			      <td class=bluebold><%=Billing_Source%></td>
			      <td><b>Process Status:</b></td>
			      <td class=bluebold><%=Status%></td>
		      </tr>
		      <tr>
                      <% if ((!Account_Id.equals("")) && (!Account_Id.equals(" ")))
                        {%>
			      <td><b>Account Id:</b></td>
			      <td class=bluebold><%=Account_Id%>
                              <input type="hidden" name="Account_Id" value="<%=Account_Id%>">
                              </td>
  			      <td><b>Account Name:</b></td>
			      <td class=bluebold><%=Account_Name%>
                              <input type="hidden" name="Account_Name" value="<%=Account_Name%>">
                              </td>
                      <%}%>
		      </tr>
		      <tr>
			<td colspan=4>
			  <hr>
			</td>
		      </tr>
                      <%if (acb.getRejectVisibility().equalsIgnoreCase("visible"))
                        {
                        if (Status.equals("Implemented"))
                        {%>
                      <tr>
                        <td valign="top">
                          <b><small>Authorising Manager
                          <br><input class=inp type=text name="Site_Address_1" size="35"
                          READONLY value="<%=acb.getAuthorising_Manager()%>">
                        </td>
                        <td colspan="2">
                          <b><small>Authorisation Details
                          <br><textarea cols="40" rows="2" READONLY><%=acb.getRejectReason()%></textarea>
                        </td>
                      </tr>
                      <%}
                        else
                        {%>
                      <tr>
                        <td>
                          <b><small><%=Status%> Reason
                          <br><textarea class=red cols="40" rows="2" READONLY><%=acb.getRejectReason()%></textarea>
                        </td>
                        <td>&nbsp;</td>
                      </tr>
                          <%}%>
                      <%}
                        else if ((Account_Id.equals("")) || (Account_Id.equals(" ")))
                        {%>
                      <tr><td colspan="3">
                      <table border="1"><tr>
                        <td align=left class="mandatory" valign="top" colspan=2>
                          Account Filter (Use * as the wildcard)<br>
                          <table border="0">
                            <tr>
                              <td valign="top">
                                <input tabindex="1" class=inp type=text name="Account_Filter" size="21"
                                  value="<%=Account_Filter%>">
                              </td>
                              <td>
                                <%=HB.getImageAsAnchor("search", 2)%>
                              </td>
                            </tr>
                          </table>
                        </td>
                        <td align=left class="mandatory" valign="top">
                          Account Id<br>
                          <table border="0">
                            <tr>
                              <td valign="top">
                          <%=DBA.getListBox("Ad_Hoc_Account","process",Account_Id,Account_Filter,3)%>
                          <input type="hidden" name="Account" value="<%=Account_Name%>">
                              </td>
                            </tr>
                          </table>
                        </td>
                      </tr>
                      </table></td></tr>
                      <%}%>
                      <tr>
                        <td align=left class=<%=acb.getClass("Site_Address_1")%>
                        valign="top">Site Address:</td>
                        <td align=left class=<%=acb.getClass("Contact_Name")%>
                        valign="top">Contact Details:</td>
                      </tr>
                      <tr>
                        <td class=<%=acb.getClass("Site_Address_1")%> valign="top">
                          <b><small>Line 1<br>
                          <input tabindex="4" class=inp type=text name="Site_Address_1" size="35"
                          <%=InputMode%> value="<%=Site_Address_1%>">
                        </td>
                        <td class=<%=acb.getClass("Contact_Name")%> valign="top">
                          <b><small>Name<br>
                          <input tabindex="16" class=inp type=text name="Contact_Name" size="35"
                          <%=InputMode%> value="<%=Contact_Name%>">
                        </td>
                        <td valign="top" rowspan=2 colspan=2>
                          <b><small>Party Management<br>
                	  <%=DBA.getListBox("Ad_Hoc_Party_Management","readonly",
                          "",Account_Id + "' and Billing_Source = '" + Billing_Source ,
                          3," style=\"width:85%;height:60px;background-color:white\"", false)%>
                        </td>
                      </tr>
                      <tr>
                        <td class=<%=acb.getClass("Site_Address_2")%> valign="top">
                          <b><small>Line 2<br>
                          <input tabindex="5" class=inp type=text name="Site_Address_2" size="35"
                          <%=InputMode%> value="<%=Site_Address_2%>">
                        </td>
                        <td class=<%=acb.getClass("Job_Title")%> valign="top">
                          <b><small>Job Title<br>
                          <input tabindex="17" class=inp type=text name="Job_Title" size="35"
                          <%=InputMode%> value="<%=Job_Title%>">
                        </td>
                      </tr>
                      <tr>
                        <td class=<%=acb.getClass("Site_Address_3")%> valign="top">
                          <b><small>Line 3<br>
                          <input tabindex="6" class=inp type=text name="Site_Address_3" size="35"
                          <%=InputMode%> value="<%=Site_Address_3%>">
                        </td>
                        <td class=<%=acb.getClass("Contact_Telephone")%> valign="top">
                          <b><small>Telephone<br>
                          <input tabindex="18" class=inp type=text name="Contact_Telephone" size="35"
                          <%=InputMode%> value="<%=Contact_Telephone%>">
                        </td>
                        <td rowspan=2 class=<%=acb.getClass("Customer_Info")%> valign="top">
                          <span id="cdets" onMouseOver="detsOver('cdetsmsg')" onMouseOut="detsOut('cdetsmsg')">
                          <b><small>Customer Details<br>
                          <textarea tabindex="26" class=fiverows rows="3" name="Customer_Info" cols="35"
                          <%=InputMode%> onFocus="detsOver('cdetsmsg')" onBlur="detsOut('cdetsmsg')"><%=Customer_Info%></textarea>
                          </span>
                        </td>
                        <td rowspan=2>
                          <span id="cdetsmsg" style="visibility: hidden">
                            <small><font color="red"><b>Text entered here<br>will be
                            visible on<br>the invoice sent<br>to the customer</font>
                          </span>
                        </td>
                      </tr>
                      <tr>
                        <td class=<%=acb.getClass("Site_Postcode")%> valign="top">
                          <b><small>Postcode<br>
                          <input tabindex="7" class=inp type=text name="Site_Postcode" size="35"
                          <%=InputMode%> value="<%=Site_Postcode%>">
                        </td>
                        <td class=<%=acb.getClass("Contact_Mobile")%> valign="top">
                          <b><small>Mobile<br>
                          <input tabindex="19" class=inp type=text name="Contact_Mobile" size="35"
                          <%=InputMode%> value="<%=Contact_Mobile%>">
                        </td>
                      </tr>
                      <tr>
                        <td class=<%=acb.getClass("Site_Country")%> valign="top">
                          <b><small>Country<br>
                	  <%=DBA.getListBox("Site_Country",InputMode,Site_Country,"AdHoc",8)%>
                        </td>
                        <td class=<%=acb.getClass("Contact_Fax")%> valign="top">
                          <b><small>Fax<br>
                          <input tabindex="20" class=inp type=text name="Contact_Fax" size="35"
                          <%=InputMode%> value="<%=Contact_Fax%>">
                        </td>
                        <td rowspan=2 class=<%=acb.getClass("CW_Info")%> valign="top">
                          <span id="wdets" onMouseOver="detsOver('wdetsmsg')" onMouseOut="detsOut('wdetsmsg')">
                          <b><small>C&W Details<br>
                          <textarea tabindex="27" class=fiverows rows="3" name="CW_Info" cols="35"
                          <%=InputMode%> onFocus="detsOver('wdetsmsg')" onBlur="detsOut('wdetsmsg')"><%=CW_Info%></textarea>
                          </span>
                        </td>
                        <td rowspan=2>
                          <span id="wdetsmsg" style="visibility: hidden">
                            <small><font color="black"><b>Please enter SR or<br>BQ reference</font>
                          </span>
                        </td>
                      </tr>
                      <tr>
                        <td class=<%=acb.getClass("Invoice_Amount")%> valign="top">
                          <b><small>Charge Amount<br>
                          <input tabindex="9" class=inp type=text name="Invoice_Amount" size="35"
                          readonly value="<%=Invoice_Amount%>">
                        </td>
                        <td class=<%=acb.getClass("Contact_Email")%> valign="top">
                          <b><small>email<br>
                          <input tabindex="21" class=inp type=text name="Contact_Email" size="35"
                          <%=InputMode%> value="<%=Contact_Email%>">
                        </td>
                      </tr>
                      <tr>
                        <td class=<%=acb.getClass("Invoice_Currency")%> valign="top">
                          <b><small>Currency<br>
                	  <%=DBA.getListBox("Invoice_Currency",InputMode,Invoice_Currency,"GBP",1,
                          "disabled",false)%>
                          <input type="hidden" name="Invoice_Currency" value="GBP">
                        </td>
                        <td class=<%=acb.getClass("Signatory")%> valign="top">
                          <b><small>Name of Signatory<br>
                          <input tabindex="22" class=inp type=text name="Signatory" size="35"
                          <%=InputMode%> value="<%=Signatory%>">
                        </td>
                        <td colspan="2"class=<%=acb.getClass("Fault_Code")%> valign="top">
                          <b><small>Fault Code<br>
                	  <%=DBA.getListBox("Fault_Code",SelectMode,Fault_Code,"Fault Code",
                          1," style=\"width:85%;\"",true,28)%>
                        </td>
                      </tr>
                      <tr>
		        <td class=<%=acb.getClass("VisitDateh")%>>Date of Chargeable Visit<br>
                          <%=HB.getDays("AssureCharge", "VisitDate", VDay, false, disableDate, 11)%>
                          <%=HB.getMonths("AssureCharge", "VisitDate", VMonth, false, disableDate, 12)%>
                          <%=HB.getYears("AssureCharge", "VisitDate", VYear, false, disableDate, 13)%>
                          <input type="hidden" name="VisitDateh" value="<%=Visit_Date%>">
		        </td>
                        <td class=<%=acb.getClass("SR_Ref")%> valign="top">
                          <b><small>FLOG Reference<br>
                          <input tabindex="23" class=inp type=text name="SR_Ref" size="35"
                          <%=InputMode%> value="<%=SR_Ref%>">
                        </td>
                        <td class=<%=acb.getClass("Fault_Details")%> rowspan=2 style="visibility:<%=FDVis%>" valign="top">
                          <b><small>Fault Details<br>
                          <textarea tabindex="29" class=fiverows rows="3" name="Fault_Details" cols="35"
                          <%=InputMode%>><%=Fault_Details%></textarea>
                        </td>
                      </tr>
                      <tr>
                        <td class=<%=acb.getClass("Engineer")%> valign="top">
                          <b><small>Engineer<br>
                          <input tabindex="14" class=inp type=text name="Engineer" size="35"
                          <%=InputMode%> value="<%=Engineer%>">
                        </td>
                        <td class=<%=acb.getClass("C002_Ref")%> valign="top">
                          <b><small>Works Order Reference<br>
                          <input tabindex="24" class=inp type=text name="C002_Ref" size="35"
                          <%=InputMode%> value="<%=C002_Ref%>">
                        </td>
                      </tr>
                      <tr>
                        <td class=<%=acb.getClass("Ad_Hoc_Product")%> valign="top">
                          <b><small>Product<br>
                	  <%=DBA.getListBox("Ad_Hoc_Product",SelectMode,Ad_Hoc_Product,"AdHoc",15)%>
                        </td>
                        <td class=<%=acb.getClass("Circuit_Ref")%> valign="top">
                          <b><small>Circuit Reference/CLI<br>
                          <input tabindex="25" class=inp type=text name="Circuit_Ref" size="35"
                          <%=InputMode%> value="<%=Circuit_Ref%>">
                        </td>
                      </tr>
                      <tr>
                        <td colspan=4>&nbsp;</td>
                      </tr>
                    </table>
                  </table>
		</form>
	      </td>
	    </tr>
	  </table><!--2-->
	</td>
      </tr>
     <tr>
      <td height="100">&nbsp
      </td>
  </table><!--1-->
</body>
</html>


