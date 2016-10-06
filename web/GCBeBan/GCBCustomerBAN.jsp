<html>
<jsp:include page="../includes/Page_Header6.jsp" flush="true" />
<%@ page import="JavaUtil.*"%>
<jsp:useBean id="gcBAN" class="DBUtilities.GCBCustomerBANBean" scope="session"/>
<jsp:useBean id="HB" class="HTMLUtil.HTMLBean" scope="session"/>
<jsp:useBean id="DBA" class="DBUtilities.DBAccess" scope="session"/>

  <%!//Variables
    String BAN_Identifier,Global_Customer_Id,Global_Customer_Name,Global_Account_Manager;
    String Required_BAN_Effective_Date,BAN_Summary,BAN_Reason;
    String Required_BP_Start_Date, gsrPrefix, Product_Type, DOB_Date, Status,
      multipleGCDs;
    String SelectMode="",Calendar="",InputMode="",Mode="",Action="",ProcessMode="";
    String Sales_Business_Unit="",Automated_Back_Billing="",DOB_Date_Date="",
      Global_Billing_Analyst="",Auto_Close="",MANS_Customer="",MANS_Customer_Name="";
    int BEDay, BEMonth, BEYear, BPSDay, BPSMonth, BPSYear, Billing_Frequency;

    String sharedPath=
          EBANProperties.getEBANProperty(EBANProperties.SHAREDPATH);

    //HTMLUtil.HTMLBean HB = new HTMLUtil.HTMLBean();
    JavaUtil.StringUtil SU = new JavaUtil.StringUtil();
    //DBUtilities.DBAccess DBA = new DBUtilities.DBAccess();
   %>
  <%//Initialization
    //Get values from last submission
    Global_Customer_Name=gcBAN.getGlobalCustomerName();
    BAN_Summary = gcBAN.getBAN_Summary();
    BAN_Reason = gcBAN.getBAN_Reason();
    BEDay = gcBAN.getBANEffectiveDateDays();
    BEMonth = gcBAN.getBANEffectiveDateMonths();
    BEYear = gcBAN.getBANEffectiveDateYears();
    BPSDay = gcBAN.getBPStartDateDays();
    BPSMonth = gcBAN.getBPStartDateMonths();
    BPSYear = gcBAN.getBPStartDateYears();
    Required_BAN_Effective_Date=SU.isNull(gcBAN.getRequired_BAN_Effective_Date(),"Today");
    Required_BP_Start_Date=SU.isNull(gcBAN.getRequired_BP_Start_Date(),"FirstOfMonth");
    Global_Customer_Id=gcBAN.getGlobalCustomerId();
    gsrPrefix = gcBAN.getGSRPrefix();
    Product_Type = gcBAN.getProductType();
    DOB_Date = gcBAN.getDOBDate();
    Status = gcBAN.getBanStatus();
    Billing_Frequency=gcBAN.getBillingFrequency();
    Auto_Close=gcBAN.getAutoClose();

    Mode=gcBAN.getMode();

    SelectMode=gcBAN.getSelectMode();
    boolean disableDate=gcBAN.getInputMode("DOB_Date").equals("READONLY")?true:false;
    InputMode=gcBAN.getInputMode();
    ProcessMode=gcBAN.getProcessMode();
    Calendar=gcBAN.getCalendar();

    BAN_Identifier=gcBAN.getBanIdentifier();
    Action=gcBAN.getAction();
    gcBAN.setBanCreatedBy((String)session.getAttribute("User_Id"));
    boolean isMANS = gcBAN.getIsMANS();
    MANS_Customer = gcBAN.getMANSCustomer();
    MANS_Customer_Name = gcBAN.getMANSCustomerName();
    if (MANS_Customer_Name.equals("Completely New Customer"))
    {
      MANS_Customer = "Completely New Customer";
    }
    boolean cnVis = true;
    if (isMANS && Mode.equals("Add") && Action.equals("Create")  &&
      !MANS_Customer_Name.equals("Completely New Customer"))
    {
      cnVis = false;
    }
    String Current_BA = gcBAN.getCurrentBusinessAnalyst();
    String New_BA = gcBAN.getNewBusinessAnalyst();
//if (New_BA == null)
//New_BA = "";
    String fullMansCustomer = gcBAN.getFullMANSCustomer().equals("")
      ?Global_Customer_Id:gcBAN.getFullMANSCustomer();
    multipleGCDs = gcBAN.getMultipleGCDs();
    String gcdVis = Product_Type.equals("GM Billing")&&
      multipleGCDs.equals("Y")?"visible":"hidden";
    String gcds = gcBAN.getGCDs();
   %>

<script language="JavaScript">
<!--
function showUnusedCustomers()
{
  unusedCust.style.visibility = "visible";
}

function customerNotFound()
{
  alert('Please contact x@cw.com for further assistance');
}

function storeCustomerName()
{
  if (CustomerBAN.MANS_Customer[CustomerBAN.MANS_Customer.selectedIndex].text == "Completely New Customer")
  {
    document.getElementById('GCNtd').style.visibility = 'visible';
  }
  CustomerBAN.MANS_Customer_Name.value = CustomerBAN.MANS_Customer[CustomerBAN.MANS_Customer.selectedIndex].text;
}

function checkSimilarNames()
{
  var newCustomer = new String(CustomerBAN.Global_Customer_Name.value);
  var matches = "The following customers are similar to the one you have entered:\n\r";
  for (i = 1; i < CustomerBAN.MANS_Customer.length - 1; i++)
  {
    var loopCustomer = new String(CustomerBAN.MANS_Customer[i].text);
    var compareLength = 5;
    if (newCustomer.length < compareLength)
    {
      compareLength = newCustomer.length
    }
    if (loopCustomer.length < compareLength)
    {
      compareLength = loopCustomer.length
    }
    if (compareLength > 1)
    {
      if (newCustomer.substring(0, compareLength).toUpperCase() == loopCustomer.substring(0, compareLength).toUpperCase())
      {
        matches += loopCustomer + " (" + CustomerBAN.MANS_Customer[i].value + ")\n\r";
      }
    }
  }
  if (matches.length > 66)
  {
    matches += "Are you sure this is a completely new customer?";
    alert(matches);
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
  var gcd = new String(CustomerBAN.GCD_Name.value);
  var gsr = new String(CustomerBAN.GSR_Prefix.value);
  if (CustomerBAN.GCD_Name.value == "")
  {
    alert("Please enter a new Global Customer Division name to add");
  }
  else if (CustomerBAN.GSR_Prefix.value == "")
  {
    alert("Please enter a new GSR Reference Prefix to add");
  }
  else if (checkGCDName(CustomerBAN.GCD_Name.value))
  {
    alert("Global Customer Division name exists already");
  }
  else if ((gcd.indexOf("(") != -1) || (gcd.indexOf(")") != -1))
  {
    alert("Global Customer Division name cannot contain brackets");
  }
  else if ((gsr.indexOf("(") != -1) || (gsr.indexOf(")") != -1))
  {
    alert("GSR Reference cannot contain brackets");
  }
  else
  {
    CustomerBAN.BAN_GCD[CustomerBAN.BAN_GCD.length] =
      new Option(CustomerBAN.GCD_Name.value+" ("+CustomerBAN.GSR_Prefix.value+")",
        CustomerBAN.GCD_Name.value+"|"+CustomerBAN.GSR_Prefix.value, false, false);
    CustomerBAN.GCD_Name.value = "";
    CustomerBAN.GSR_Prefix.value = "";
    storeGCDs();
  }
<%}%>
}

function removearrowClick()
{
  if (CustomerBAN.BAN_GCD.selectedIndex == -1)
  {
    alert("No Global Customer Division selected for removal");
  }
  else if ((CustomerBAN.BAN_GCD.selectedIndex == 0) &&
    (CustomerBAN.BAN_GCD.length == 1))
  {
    alert("This is the only GCD and cannot be removed");
  }
  else
  {
    CustomerBAN.BAN_GCD.remove(CustomerBAN.BAN_GCD.selectedIndex);
    storeGCDs();
  }
}

function storeGCDs()
{
/*alert("a"+CustomerBAN.BAN_GCD.length);
alert("b"+CustomerBAN.BAN_GCD.text);
alert("c"+CustomerBAN.BAN_GCD[0].value);
alert("d"+CustomerBAN.BAN_GCD[1].value);
  if (CustomerBAN.BAN_GCD.length == 1)
  {
    CustomerBAN.GCDs.value = CustomerBAN.BAN_GCD.value + ",";
  }
  else
  {*/
    var nr = "";
    for (i=0; i < CustomerBAN.BAN_GCD.length; i++)
    {
      nr += CustomerBAN.BAN_GCD[i].value + ",";
    }
    CustomerBAN.GCDs.value = nr;
  //}
}

function checkGCDName(newGCD)
{
  var found = false;
  var gcd = new String(CustomerBAN.GCDs.value);
//alert(CustomerBAN.GCDs.value);
  if ((gcd.indexOf(newGCD+"|") == 0) || (gcd.indexOf(","+newGCD+"|") != -1))
  {
    found = true;
  }
  return found;
}

function checkGSRVis()
{
//alert (CustomerBAN.Product_Type.value);
//alert (CustomerBAN.Multiple_GCDs.value);
  if (CustomerBAN.Product_Type.value == 'GM Billing')
  {
    if (CustomerBAN.Multiple_GCDs.value == 'Y')
      CustomerBAN.GSRP.visibility = 'hidden';
    else
      CustomerBAN.GSRP.visibility = 'visible';
  }
}

//-->
</script>

   <table border=0 id=1 width="100%">
  <tr><!-- This row holds the standard C&W intranet bar-->
    <td>
      <%@ include file="../includes/Page_Header4.htm"%>
    </td>
  </tr>
  <tr><!-- This row holds the local menu bar-->
      <td>
	<form name="CustomerBAN" method="post" action="GCBCustomerBANHandler.jsp">
        <%=HB.getBANMenu_Bar("GCBCustomerBAN",Mode,Action,(String)session.getAttribute("System"),gcBAN.getIsDirect())%>
        <input name="ButtonPressed" type=hidden value="">
        <input name="MANS_Customer_Name" type=hidden value="<%=MANS_Customer_Name%>">
        <input name="GCDs" type=hidden value="<%=gcds%>">
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
		    <h3 align="center"><%=Mode%> a Customer</h2>
		    <%=SU.hasValue(BAN_Identifier,"</td></tr><tr><td><font color=darkblue><b>BAN Id:- "+BAN_Identifier+"<td>")+SU.isBlank(gcBAN.getMessage(),"<font color=blue><b>Mandatory fields are shown in blue</b>")%>
		  </td>
		</tr>
		<tr>
		  <td valign="top" colspan=2>
		    <table width="100%" border="0" cellpadding="0" cellspacing="0" id=4>
		      <tr>
			<td colspan="4">
                          <table border="0" width= "100%">
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
			<td class=<%=gcBAN.getClass("BAN_Summary")%>>
			  <b><small>BAN summary
		          <br><textarea class=tworows name="BAN_Summary" cols="40" rows="2" <%=gcBAN.getMode("BAN_Summary")%>><%=BAN_Summary%></textarea>
		        </td>
			<td class=<%=gcBAN.getClass("Product_Type")%>><b><small>Product Type
			  <br><%=DBA.getListBox("Product_Type",SelectMode,Product_Type,"Product Type")%>
			</td>
			<td>
			</td>
		        <td style="visibility:hidden" class=<%=gcBAN.getClass("BANEffectiveDateh")%>>BAN Effective Date<br>
                          <%=HB.getDays("CustomerBAN", "BANEffectiveDate", BEDay, false, disableDate)%>
                          <%=HB.getMonths("CustomerBAN", "BANEffectiveDate", BEMonth, false, disableDate)%>
                          <%=HB.getYears("CustomerBAN", "BANEffectiveDate", BEYear, false, disableDate)%>
                          <input type="hidden" name="BANEffectiveDateh" value="<%=Required_BAN_Effective_Date%>">
		        </td>
		        <td>
		        </td>
		      </tr>
<%if (isMANS)
  {%>
		      <tr>
			<td colspan="2" class=<%=gcBAN.getClass("MANS_Customer")%>><b><small>Select Base Customer
			  <br><%=DBA.getListBox("MANS_Customer",InputMode,MANS_Customer,"", 1, " style=\"width:365px\"", true)%>
			</td>
		      </tr>
<%}%>
		      <tr>
			<td name="GCNtd" id="GCNtd" style="visibility:<%=cnVis?"visible":"hidden"%>" colspan="2" class=<%=gcBAN.getClass("Global_Customer_Name")%>><b><small><%=isMANS?"New ":""%>Global Customer Name
			  <br><input onBlur="<%=cnVis?"":"checkSimilarNames()"%>" class=inp type=text name="Global_Customer_Name" size=70 maxlength=255 value="<%=Global_Customer_Name%>" <%=gcBAN.getInputMode("Global_Customer_Name")%>>

			</td>
			<td class=<%=gcBAN.getClass("gcId")%> style="visibility:<%=isMANS?"hidden":Global_Customer_Id.equals("")?"hidden":"visible"%>"><b><small>Global Customer Id
			  <br><input class=inp type=text name="Global_Customer_Id" size=10 maxlength=20 value="<%=Global_Customer_Id%>" READONLY>
			</td>
		        <td class=<%=gcBAN.getClass("BPStartDateh")%>>Billing Period Start Date<br>
                          <%=HB.getDays("CustomerBAN", "BPStartDate", /*isMANS?1:BPSDay*/1, false, /*isMANS?true:disableDate*/true, BPSMonth, BPSYear)%>
                          <%=HB.getMonths("CustomerBAN", "BPStartDate", BPSMonth, false, /*isMANS?disableDate:true*/true)%>
                          <%=HB.getYears("CustomerBAN", "BPStartDate", BPSYear, false, /*isMANS?disableDate:true*/true)%>
                          <input type="hidden" name="BPStartDateh" value="<%=Required_BP_Start_Date%>">
		        </td>
		      </tr>
		      <tr>
			<td class=<%=gcBAN.getClass("DOB_Date")%>><b><small>DOB Date
			  <br><%=DBA.getListBox("DOB_Date",gcBAN.getInputMode("DOB_Date"),DOB_Date,"DOB Date")%>
			</td>
			<td class=<%=gcBAN.getClass("Auto_Close")%>><b><small>Auto Close
			  <br><%=DBA.getListBox("Auto_Close",gcBAN.getInputMode("Auto_Close"),Auto_Close,"")%>
			</td>
			<td>&nbsp;
			</td>
			<td class=<%=gcBAN.getClass("Billing_Frequency")%>><b><small>Billing Frequency
			  <br><%=DBA.getListBox("Billing_Frequency",InputMode,Billing_Frequency,"")%>
			</td>
		      </tr>
		      <tr>
			<td name="GSRP" id="GSRP" style="visibility:<%=isMANS?"hidden":multipleGCDs.equals("Y")?"hidden":"visible"%>" class=<%=gcBAN.getClass("gsrPrefix")%>><b><small>Global Service Reference Prefix
			  <br><input class=inp type=text name="gsrPrefix" size=10 maxlength=20 value="<%=gsrPrefix%>" <%=InputMode%>>
			</td>
<%if (Mode.equals("Amend"))
  {%>
			<td class=<%=gcBAN.getClass("Current_BA")%>><b><small>Current Billing Analyst
			  <br><%=DBA.getListBox("Current_BA",gcBAN.getInputMode("Current_BA"),Current_BA,isMANS?fullMansCustomer:Global_Customer_Id, false)%>
			</td>
			<td class=<%=gcBAN.getClass("New_BA")%>><b><small>New Billing Analyst
			  <br><%=DBA.getListBox("New_BA",gcBAN.getInputMode("New_BA"),New_BA,Global_Customer_Id,true)%>
			</td>
<%}%>
		      </tr>
		      <tr>
			<td style="visibility:<%=Product_Type.equals("GM Billing")?"visible":"hidden"%>" valign="top" class=<%=gcBAN.getClass("Multiple_GCDs")%>><b><small>Multiple Global Customer Divisions Required<br>
			  <%=DBA.getListBox("Multiple_GCDs",gcBAN.getSelectMode("Multiple_GCDs"),multipleGCDs,"")%>
			</td>
		      </tr>
		      <tr id="gcdrow" name="gcdrow" style="visibility:<%=gcdVis%>">
			<td colspan="4" style="BORDER: thin groove">
                          <table width="100%" border="0">
                            <tr>
                              <td width="45%" class=<%=gcBAN.getClass("New_GCD")%> valign="top">
                                <b><small>New Global Customer Division Name<br>
                                <input class=inp style="width:350px" type=text name="GCD_Name" maxlength="90" <%=gcBAN.getInputMode("GCD_Name")%>>
                                <br><b><small>New GSR Reference Prefix<br>
                                <input class=inp type=text name="GSR_Prefix" maxlength="10" <%=gcBAN.getInputMode("GSR_Prefix")%>>
                              </td>
                              <td align="center">
                                <%=HB.getImageAsAnchor("addarrow")%><br>&nbsp;<br>
                                <%=HB.getImageAsAnchor("removearrow")%>
                              </td>
                              <td width="45%" class=<%=gcBAN.getClass("BAN_GCD")%>>
                                <b><small>Global Customer Divisions<br>
                                <%=gcBAN.getGCDListBox()%>
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
  </table><!--1-->
</body>
</html>
