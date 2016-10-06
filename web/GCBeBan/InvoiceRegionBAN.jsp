<html>
<jsp:include page="../includes/Page_Header5.jsp" flush="true" />
<%@ page import="JavaUtil.StringUtil"%>
<jsp:useBean id="irBAN" class="DBUtilities.InvoiceRegionBANBean" scope="session"/>
<jsp:useBean id="gcBAN" class="DBUtilities.GCBCustomerBANBean" scope="session"/>
<jsp:useBean id="HB" class="HTMLUtil.HTMLBean" scope="session"/>
<jsp:useBean id="DBA" class="DBUtilities.DBAccess" scope="session"/>

  <%!//Variables
    String BAN_Identifier,Global_Customer_Id,Global_Customer_Name,Global_Account_Manager,Invoice_Region;
    String Customer_Billing_Name,Customer_Contact,Customer_Billing_Address,
      Status,strategicReports;
    String Required_BAN_Effective_Date,BAN_Summary,BAN_Reason,Account_Id,Currency_Desc;
    String SelectMode="",ProcessMode="",Calendar="",InputMode="",Mode="",Action="";
    int BEDay, BEMonth, BEYear;
    String companyAddressId, banTaxRequirement, multipleBillingRegions,
      customerBillingCountry, sapCustomerNo, customerVatRegNo, brVis,
      billingRegions, Country_Literal, customerContactPoint;

    //HTMLUtil.HTMLBean HB = new HTMLUtil.HTMLBean();
    JavaUtil.StringUtil SU = new JavaUtil.StringUtil();
    //DBUtilities.DBAccess DBA = new DBUtilities.DBAccess();
   %>
  <%//Initialization
    //Get values from last submission
    Global_Customer_Name=irBAN.getGlobalCustomerName();
    BAN_Summary = irBAN.getBAN_Summary();
    BAN_Reason = irBAN.getBAN_Reason();
    Global_Account_Manager=irBAN.getGlobal_Account_Manager();
    Required_BAN_Effective_Date=SU.isNull(irBAN.getRequired_BAN_Effective_Date(),"Today");
    Global_Customer_Id=irBAN.getGlobalCustomerId();
    Account_Id=irBAN.getAccount_Id();

    Customer_Contact=irBAN.getCustomer_Contact();
    Customer_Billing_Address=irBAN.getCustomer_Billing_Address();
    Customer_Billing_Name=irBAN.getCustomer_Billing_Name();
    Invoice_Region=irBAN.getInvoice_Region();
    Currency_Desc = SU.isBlank(irBAN.getCurrency_Desc(),"GBP");
    BEDay = irBAN.getBANEffectiveDateDays();
    BEMonth = irBAN.getBANEffectiveDateMonths();
    BEYear = irBAN.getBANEffectiveDateYears();
    companyAddressId = irBAN.getCompany_Address_Id();
    banTaxRequirement = irBAN.getBAN_Tax_Requirement();
    multipleBillingRegions = irBAN.getMultiple_Billing_Regions();
    brVis = multipleBillingRegions.equals("Y")?"visible":"hidden";
    billingRegions = irBAN.getBilling_Regions();
    customerBillingCountry = irBAN.getCustomer_Billing_Country();
    sapCustomerNo = irBAN.getSAP_Customer_No();
    customerVatRegNo = irBAN.getCustomer_VAT_Reg_No();
    Country_Literal = irBAN.getCountryLiteral();
    Status = irBAN.getBanStatus();
    customerContactPoint = irBAN.getCustomerContactPoint();
 
    Mode=irBAN.getMode();
    String accountVis = Mode.equals("Amend")?"visible":"hidden";

    ProcessMode=irBAN.getProcessMode();
    SelectMode=irBAN.getSelectMode();
    boolean disableDate=SelectMode.equals("DISABLED")?true:false;
    InputMode=irBAN.getInputMode();
    Calendar=irBAN.getCalendar();

    BAN_Identifier=irBAN.getBanIdentifier();
    Action=irBAN.getAction();
    irBAN.setBanCreatedBy((String)session.getAttribute("User_Id"));
    String system = (String)session.getAttribute("System");
    String selectStyle = " style=\"width:418px\"";
    strategicReports = irBAN.getStrategicReports();
   %>
<script language="JavaScript">
<!--
/*function BAN_Tax_Requirement_List_Process()
{
  if (IRBAN.Customer_Billing_Country[IRBAN.Customer_Billing_Country.selectedIndex].value != '')
  {
    taxCompatibility();
  }
}*/

function Customer_Billing_Country_List_Process()
{
  IRBAN.Country_Literal.value =
    IRBAN.Customer_Billing_Country[IRBAN.Customer_Billing_Country.selectedIndex].text;
  if (IRBAN.BAN_Tax_Requirement[IRBAN.BAN_Tax_Requirement.selectedIndex].value != '')
  {
    taxCompatibility();
  }
}

function taxCompatibility()
{
  var taxInfo = new String(IRBAN.BAN_Tax_Requirement[IRBAN.BAN_Tax_Requirement.selectedIndex].value);
  var taxCountry = taxInfo.substr(taxInfo.length-2);
  var custCountry = IRBAN.Customer_Billing_Country[IRBAN.Customer_Billing_Country.selectedIndex].value;
  if ((taxCountry != '00') && (taxCountry != custCountry))
  {
    alert('Warning! Tax Requirement selected is not consistent with Country.');
  }
}

function addarrowClick()
{
<%if (disableDate)
  {%>
  alert("Add not allowed");
<%}
  else
  {%>
  if (IRBAN.New_Billing_Region.value == "")
  {
    alert("Please enter a new Billing Region to add");
  }
  else if (checkBillingRegionName(IRBAN.New_Billing_Region.value))
  {
    alert("Billing Region name exists already");
  }
  else
  {
    IRBAN.BAN_Billing_Region[IRBAN.BAN_Billing_Region.length] =
      new Option(IRBAN.New_Billing_Region.value,
        IRBAN.New_Billing_Region.value, false, false);
    IRBAN.New_Billing_Region.value = "";
    storeBillingRegions();
  }
<%}%>
}

function removearrowClick()
{
<%if (disableDate)
  {%>
  alert("Remove not allowed");
<%}
  else
  {%>
//alert(IRBAN.BAN_Billing_Region.length);
//alert(IRBAN.BAN_Billing_Region[0].value);
//alert(IRBAN.BAN_Billing_Region[1].value);
  if (IRBAN.BAN_Billing_Region.selectedIndex == -1)
  {
    alert("No Billing Region selected for removal");
  }
  else if (IRBAN.BAN_Billing_Region.length == 1)
  {
    alert("The last remaining Billing Region cannot be removed");
  }
  else
  {
    IRBAN.BAN_Billing_Region.remove(IRBAN.BAN_Billing_Region.selectedIndex);
    storeBillingRegions();
  }
<%}%>
}

function storeBillingRegions()
{
  var nr = "";
  for (i=0; i < IRBAN.BAN_Billing_Region.length; i++)
  {
//alert(nr);
    nr += IRBAN.BAN_Billing_Region[i].value + ",";
  }
  IRBAN.BillingRegions.value = nr;
}

function checkBillingRegionName(newBR)
{
  var found = false;
  var br = new String(IRBAN.BillingRegions.value);
  if ((br.indexOf(newBR+",") == 0) || (br.indexOf(","+newBR+",") != -1))
  {
    found = true;
  }
  return found;
}

function BAN_Tax_Requirement_List_Process()
{
  var taxInfo = new String(IRBAN.BAN_Tax_Requirement[IRBAN.BAN_Tax_Requirement.selectedIndex].value);
  var lastDash = taxInfo.lastIndexOf("-");
  var lastDashButOne = taxInfo.lastIndexOf("-", lastDash-1);
  var sapTaxCode = taxInfo.substring(lastDashButOne+1, lastDash);
  var userId = new String("<%=session.getAttribute("User_Id")%>");
  if ((sapTaxCode == "A8") && (userId.substr(0,7).toUpperCase() != "KG08953")) 
  {
    alert("This tax rate is restricted. If you need to use it, please select another rate, submit the BAN, do NOT authorise it and raise a query, including the BAN Identifier.");
    if (IRBAN.BAN_Tax_Requirement.selectedIndex > 0)
    {
      IRBAN.BAN_Tax_Requirement.selectedIndex = IRBAN.BAN_Tax_Requirement.selectedIndex - 1;
    }	
    else
    {
      IRBAN.BAN_Tax_Requirement.selectedIndex = IRBAN.BAN_Tax_Requirement.selectedIndex + 1;
    }	
  }
  else
  {
    taxCompatibility();
  }

}//-->
</script>

  <table border=0 id=1 width="100%">
  <tr><!-- This row holds the standard C&W intranet bar-->
    <td>
      <%/*<c:import url="http://localhost:8080/shared/includes/Page_Header2.htm"/>*/%>
      <%@ include file="../includes/Page_Header4.htm"%>
    </td>
  </tr>
  <tr><!-- This row holds the local menu bar-->
      <td>
	<form name="IRBAN" method="post" action="InvoiceRegionBANHandler.jsp">
        <%=HB.getBANMenu_Bar("GCBCustomerBAN",Mode,Action,system,irBAN.getIsDirect())%>
        <input name="ButtonPressed" type=hidden value="">
        <input name="BillingRegions" type=hidden value="<%=billingRegions%>">
      </td>
    </tr>
     <tr>
      <td>
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
		    <h3 align="center"><%=Mode%> an Account</h2>
		    <%=SU.hasValue(BAN_Identifier,"</td></tr><tr><td><font color=darkblue><b>BAN Id:- "+BAN_Identifier+"<td>")+SU.isBlank(irBAN.getMessage(),"<font color=blue><b>Mandatory fields are shown in blue</b>")%>
		  </td>
		</tr>
		<tr>
		  <td valign="top" colspan=2>
		    <table width="100%" border="0" cellpadding="0" cellspacing="0" id=4>
		      <tr>
			<td colspan="4">
                          <table border="0" width= "100%">
                            <tr>
                              <td width="20%"><b>Customer:</b></td>
                              <td width="30%" class=bluebold><%=Global_Customer_Id%> <%=Global_Customer_Name%></td>
                              <td style="visibility:<%=accountVis%>" width="20%"><b>Account:</b></td>
                              <td style="visibility:<%=accountVis%>" width="30%" class=bluebold><%=Account_Id%></td>
                            </tr>
                            <tr>
                              <td width="20%"><b>Billing Advice Status:</b></td>
                              <td width="30%" class=bluebold><%=Status%></td>
                              <td width="20%"><b>Requested By:</b></td>
                              <td width="30%" class=bluebold><%=session.getAttribute("User_Name")%></td>
                            </tr>
                          </table>
                        </td>
		      </tr>
		      <tr>
			<td colspan=4>
			  <hr>
			</td>
		      </tr>
		      <tr>
			<td width="50%" colspan="2" class=<%=irBAN.getClass("BAN_Summary")%>>
			  <b><small>BAN summary
		          <br><textarea class=tworows name="BAN_Summary" cols="40" rows="2" <%=irBAN.getMode("BAN_Summary")%>><%=BAN_Summary%></textarea>
		        </td>
		        <td style="visibility:hidden" colspan="2" valign="top" class=<%=irBAN.getClass("BANEffectiveDateh")%>>BAN Effective Date<br>
                          <%=HB.getDays("CustomerBAN", "BANEffectiveDate", BEDay, false, disableDate)%>
                          <%=HB.getMonths("CustomerBAN", "BANEffectiveDate", BEMonth, false, disableDate)%>
                          <%=HB.getYears("CustomerBAN", "BANEffectiveDate", BEYear, false, disableDate)%>
                          <input type="hidden" name="BANEffectiveDateh" value="<%=Required_BAN_Effective_Date%>">
		        </td>
		      </tr>
		      <tr>
			<td width="50%" colspan="2" class=<%=irBAN.getClass("Invoice_Region")%>><b><small>Invoice Region Name
			  <br><input class=inp style="width:400px" type=text name="Invoice_Region" maxlength=50 value="<%=Invoice_Region%>" <%=irBAN.getMode("Invoice_Region")%>>
			</td>
			<td width="50%" colspan="2" class=<%=irBAN.getClass("Global_Account_Manager")%>><b><small>Account Manager
			  <br><input class=inp style="width:400px" type=text name="Global_Account_Manager" maxlength=50 value="<%=Global_Account_Manager%>" <%=irBAN.getMode("Global_Account_Manager")%>>
			</td>
		      </tr>
		      <tr>
			<td colspan="2" class=<%=irBAN.getClass("Customer_Billing_Name")%>><b><small>Customer Billing Name
			  <br><input class=inp style="width:400px" type=text name="Customer_Billing_Name" maxlength=100 value="<%=Customer_Billing_Name%>" <%=irBAN.getMode("Customer_Billing_Name")%>>
			</td>
		      </tr>
		      <tr>
			<td colspan="2" rowspan="2" class=<%=irBAN.getClass("Customer_Billing_Address")%>>
			<b><small>Customer Billing Address<br>
			<textarea class=tworows style="height:70px" name="Customer_Billing_Address" cols=79 rows=4 <%=irBAN.getMode("Customer_Billing_Address")%>><%=Customer_Billing_Address%></textarea></td>
			</td>
		      </tr>
		      <tr>
			<td colspan="2" valign="top" style="visibility:<%=(irBAN.getClass("Customer_Contact").equals("mandatory")||irBAN.getClass("Customer_Contact").equals("errored"))?"visible":"hidden"%>" colspan="2" class=<%=irBAN.getClass("Customer_Contact")%>>
			  <b><small>Customer Contact
			  <br><input class=inp style="width:400px" type=text name="Customer_Contact" maxlength=50 value="<%=Customer_Contact%>" <%=irBAN.getMode("Customer_Contact")%>>
			</td>
		      </tr>
		      <tr>
			<td colspan="2" valign="top" class=<%=irBAN.getClass("Customer_Billing_Country")%>><b><small>Country<br>
			  <%=DBA.getListBox("Customer_Billing_Country",SelectMode,customerBillingCountry,system,1,selectStyle,true)%>
	                  <input type="hidden" name="Country_Literal" value="<%=Country_Literal%>">
			</td>
		        <td colspan="2" valign="top" class=<%=irBAN.getClass("Currency_Desc")%>><b><small>Billing Currency<br>
			  <%=DBA.getListBox("Currency_Desc",irBAN.getMode("Currency_Desc"),Currency_Desc,"")%>
			</td>
		      </tr>
		      <tr>
			<td colspan="2" class=<%=irBAN.getClass("Company_Address_Id")%>><b><small>Company Address Id<br>
			  <%=DBA.getListBox("Company_Address_Id",irBAN.getMode("Company_Address_Id"),companyAddressId,"Company Address Id",1,selectStyle,true)%>
			</td>
			<td colspan="2" class=<%=irBAN.getClass("BAN_Tax_Requirement")%>><b><small>Tax Requirement<br>
			  <%=DBA.getListBox("BAN_Tax_Requirement",irBAN.getMode("BAN_Tax_Requirement"),banTaxRequirement,companyAddressId, false)%>
			</td>
		      </tr>
		      <tr>
			<td colspan="4" class=<%=irBAN.getClass("Customer_Contact_Point")%>><b><small>Customer Contact Point<br>
			  <%=DBA.getListBox("Customer_Contact_Point",irBAN.getMode("Customer_Contact_Point"),
                            customerContactPoint,companyAddressId,gcBAN.getProductType(),1," style=\"width:692px\"",false)%>
			</td>
		      </tr>
		      <tr>
                      <!--
			<td  class=<%=irBAN.getClass("SAP_Customer_No")%>><b><small>SAP Customer No.
			  <br><input class=inp type=text name="SAP_Customer_No" maxlength=50 value="<%=sapCustomerNo%>" <%=irBAN.getMode("SAP_Customer_No")%>>
			</td>
			<td>
			</td>
                      -->
			<td class=<%=irBAN.getClass("Customer_VAT_Reg_No")%>><b><small>Customer VAT Reg. No.
			  <br><input class=inp type=text name="Customer_VAT_Reg_No" maxlength=50 value="<%=customerVatRegNo%>" <%=InputMode%>>
			</td>
			<td  class=<%=irBAN.getClass("Arbor_Bar")%>><b><small>Arbor Bar
			  <br><input class=inp type=text name="Arbor_Bar" maxlength=50 value="<%=irBAN.getArborBar()%>" <%=InputMode%>>
			</td>
			<td  class=<%=irBAN.getClass("PCB_Bar")%>><b><small>PCB Bar
			  <br><input class=inp type=text name="PCB_Bar" maxlength=50 value="<%=irBAN.getPCBBar()%>" <%=InputMode%>>
			</td>
			<td  class=<%=irBAN.getClass("CMPLS")%>><b><small>CMPLS
                                    <br><input class=inp type=text name="CMPLS" maxlength=50 value="<%=irBAN.getCMPLS()%>" <%=InputMode%>>
			</td>
		      </tr>
		      <tr>
			<td colspan="2" valign="top" class=<%=irBAN.getClass("Multiple_Billing_Regions")%>><b><small>Multiple Billing Regions Required<br>
			  <%=DBA.getListBox("Multiple_Billing_Regions",SelectMode,multipleBillingRegions,"")%>
			</td>
<%if (gcBAN.getProductType().equals("GM Billing"))
  {%>
			<td colspan="2" valign="top" class=<%=irBAN.getClass("Strategic_Reports")%>><b><small>Strategic Reports Required (RS6000)<br>
			  <%=DBA.getListBox("Strategic_Reports",irBAN.getMode("Strategic_Reports"),strategicReports,"")%>
			</td>
<%}
  else
  {%>
                        <input type="hidden" name="Strategic_Reports" value="N">
<%}%>
		      </tr>
		      <tr>
			<td colspan="4">&nbsp;</td>
		      </tr>
		      <tr id="brrow" name="brrow" style="visibility:<%=brVis%>">
			<td colspan="4" style="BORDER: thin groove">
                          <table width="100%" border="0">
                            <tr>
                              <td width="45%" class=<%=irBAN.getClass("New_Billing_Region")%> valign="top">
                                <b><small>Billing Region to add<br>
                                <input class=inp style="width:400px" type=text name="New_Billing_Region" maxlength="50" <%=InputMode%>>
                              </td>
                              <td align="center">
                                <%=HB.getImageAsAnchor("addarrow")%><br>&nbsp;<br>
                                <%=HB.getImageAsAnchor("removearrow")%>
                              </td>
                              <td width="45%" class=<%=irBAN.getClass("BAN_Billing_Region")%>>
                                <b><small>Billing Regions<br>
                                <%=irBAN.getBillingRegionsListBox()%>
                              </td>
                            </tr>
                          </table>
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
     <tr>
      <td height="100">&nbsp
      </td>
    </tr>
    <!--Footer-->
    <tr>
      <td>
        <c:import url="http://localhost:8080/shared/includes/Footer.htm"/>
      </td>
    </tr>
  </table><!--1-->
</body>
</html>
