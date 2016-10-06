<html>
<jsp:include page="../includes/Page_Header5.jsp" flush="true" />
<%@ page import="JavaUtil.*"%>
<jsp:useBean id="BAN" class="DBUtilities.BANBean" scope="session"/>
<jsp:useBean id="HB" class="HTMLUtil.HTMLBean" scope="session"/>
<jsp:useBean id="DBA" class="DBUtilities.DBAccess" scope="session"/>

<%! String SQL;
    String Global_Customer_Id="",Carrier_Name="";
    String Global_Customer_Name="";
    String Account_Id="",Division="",C2_Ref_No="";
    String Circuit_Reference="";
    String Site_Reference="";
    String Message,ActionDivision,ActionCircuit,ActionCarrier,ActionCustomer;
    String Account_Qualifier="",Circuit_Qualifier="All",Circuit_Qualifier2="All";
    //HTMLUtil.HTMLBean HB = new HTMLUtil.HTMLBean();
    JavaUtil.StringUtil SU = new JavaUtil.StringUtil();
    //DBAccess DBA = new DBAccess();
    String PageSent;
    String siteVis;
%>
<%//Set Initial values
  //Are we coming from the handler page, or a fresh start?

  Message=SU.isBlank(BAN.getMessage(),SU.isNull((String)session.getAttribute("Message"),""));
  PageSent=SU.isNull((String)session.getAttribute("PageSent"),"");
  if (PageSent.endsWith("BANMenuHandler.jsp")
  || (PageSent.endsWith("FromEdify2.jsp"))
  || (PageSent.endsWith("ossebanLink.jsp"))
  || (Message.endsWith("charges for this circuit<br>"))
  || (PageSent.endsWith("BANMenuHandler.jsp") && BAN.getAction().compareTo("Authorise")==0))
  {
    Global_Customer_Id=BAN.getGlobalCustomerId();
    Account_Qualifier=SU.isBlank(Global_Customer_Id,"All");
    Account_Id=BAN.getAccount_Id();
    Division=BAN.getDivision_Id();
    C2_Ref_No=BAN.getC2RefNo();
    if (Division.equals(""))
    {
      if (C2_Ref_No.equals(""))
        Circuit_Qualifier="";
      else
        Circuit_Qualifier=" C2_Ref_No = '" + C2_Ref_No + "' and ";
    }
    else
      Circuit_Qualifier=" Global_Customer_Division_Id = '" + Division + "' and ";
    Circuit_Reference=BAN.getCircuit_Reference();
    Site_Reference=BAN.getSite();
    Carrier_Name=BAN.getCarrier_Name();
  }
  else
  {
    //Store old values
    session.setAttribute("Global_Customer",BAN.getGlobalCustomerId());
    session.setAttribute("Account",BAN.getAccount_Id());
    session.setAttribute("GCD_Id",BAN.getDivision_Id());
    session.setAttribute("Circuit",BAN.getCircuit_Reference());
    session.setAttribute("C2_Ref_No",BAN.getC2RefNo());
    session.setAttribute("Site",BAN.getSite());
    session.setAttribute("Carrier",BAN.getCarrier_Name());

    BAN.setAction("");
    BAN.setCircuit_Reference("");
    BAN.setSite("");
    BAN.setGlobalCustomerId("");
    BAN.setGlobalCustomerName("");
    BAN.setAccount_Id("");
    BAN.setDivision_Id("");
    BAN.setCarrier_Name("");
    BAN.setBanIdentifier("");
    Global_Customer_Id="";
    Account_Id="";
    Circuit_Reference="";
    Division="";
    Carrier_Name="";
  }
  if (Global_Customer_Id.compareTo("")==0)
    {ActionCustomer="alert('You must select a Customer for this option')";}
  else
    {ActionCustomer="submitForm(this)";}
  if (Division.compareTo("")==0)
    {ActionDivision="alert('You must select a Division for this option')";}
  else
    {ActionDivision="submitForm(this)";}

  if ((SU.isNull(Circuit_Reference,"").compareTo("")==0) ||
      (SU.isNull(Circuit_Reference,"").startsWith("N/A")) ||
      (Message.endsWith("cancelled")) ||
      (Message.endsWith("Invalid Circuit Reference")))
    {ActionCircuit="alert('You must select a Circuit Reference for this option')";}
  else if ((Global_Customer_Id.compareTo("")==0) &&
	    (Division.compareTo("")==0))
    {ActionCircuit="alert('This Circuit either has no owning Division or Customer ')";}
  else
    {ActionCircuit="submitForm(this)";}

  if (Carrier_Name.compareTo("")==0)
    {ActionCarrier="alert('You must select a Carrier for this option')";}
  else
    {ActionCarrier="submitForm(this)";}

  //new bit for managed services - TA 04/05/2005
  boolean isMANS = BAN.getGroupName().equals("MANS");
  String imgSrc = "<img src='../nr/cw/newimages/menu_brown.gif'>";
  String createCustAcct;
  String createCircuit;
  String createRecCharge;
  String amendCircuit;
  String amendRecCharge;
  String ceaseCircuit;
  String ceaseRecCharge;
  String ceaseReprovideCharge;
  String deleteRecCharge;
  String createCredit;
  String createCarrier;
  String createOneOffCharge;
  String createSite;
  String amendCarrier;
  String amendCredit;
  String deleteCredit;
  String amendOneOffCharge;
  String deleteOneOffCharge;
  String amendSite;
  String createAdjustment;
  String customerQualifier = "";
  String accQual2 = "";
  String selectStyle = "";
  String selectMode = BAN.getEdifySetCustomer()?"DISABLED":"submit";
  siteVis = BAN.getSiteVisibility();
  String circuitSelect = "";
  if (isMANS)
  {
    imgSrc = "";
    createCustAcct = "";
    createCircuit = "";
    createRecCharge = "";
    amendCircuit = "";
    amendRecCharge = "";
    ceaseCircuit = "";
    ceaseRecCharge = "";
    ceaseReprovideCharge = "";
    deleteRecCharge = "";
    createCredit = "";
    amendCredit = "";
    deleteCredit = "";
    createCarrier = "";
    createOneOffCharge = "";
    amendOneOffCharge = "";
    deleteOneOffCharge = "";
    amendCarrier = "";
    createSite = "";
    amendSite = "";
    createAdjustment = "";
    customerQualifier = "MANS";
    accQual2 = "MANS";
    selectStyle = " style=\"width:300px\"";
  }
  else
  {
    createCustAcct = HB.getButton("Create a Customer Account",ActionCustomer);
    createCircuit = HB.getButton("Create a Circuit",ActionCustomer);
    createRecCharge = HB.getButton("Create a Recurring Charge",ActionCircuit);
    amendCircuit = HB.getButton("Amend Circuit Details",ActionCircuit);
    amendRecCharge = HB.getButton("Amend a Recurring Charge",ActionCustomer);
    ceaseCircuit = HB.getButton("Cease a Circuit",ActionCircuit);
    ceaseRecCharge = HB.getButton("Cease a Recurring Charge",ActionCircuit);
    ceaseReprovideCharge = HB.getButton("Cease/Reprovide a Recurring Charge",ActionCircuit);
    deleteRecCharge = HB.getButton("Delete a Recurring Charge",ActionCircuit);
    createCredit = HB.getButton("Create a Credit",ActionCircuit);
    amendCredit = HB.getButton("Amend a Credit",ActionCircuit);
    deleteCredit = HB.getButton("Delete a Credit",ActionCircuit);
    createCarrier = HB.getButton("Create a Carrier","submitForm(this)");
    createOneOffCharge = HB.getButton("Create a One Off Charge",ActionCircuit);
    amendOneOffCharge = HB.getButton("Amend a One Off Charge",ActionCircuit);
    deleteOneOffCharge = HB.getButton("Delete a One Off Charge",ActionCircuit);
    amendCarrier = HB.getButton("Amend a Carrier",ActionCarrier);
    createAdjustment = HB.getButton("Create an Adjustment",ActionDivision);
    createSite = HB.getButton("Create a Site",ActionCustomer);
    amendSite = HB.getButton("Amend a Site",ActionCustomer);
    selectStyle = " style=\"width:300px\"";
    circuitSelect = DBA.getListBox("Circuit","submit",Circuit_Reference,
      Global_Customer_Id,Circuit_Qualifier,1,selectStyle,true);
  }
  String custSelect = DBA.getListBox("Global_Customer",selectMode,
    Global_Customer_Id,customerQualifier,1,selectStyle,true);
  String divSelect = DBA.getListBox("GCD_Id","submit",
    Division,Account_Qualifier,1,selectStyle,true);
  String c2Select = DBA.getListBox("C2_Ref_No","submit",C2_Ref_No,
   Account_Qualifier,1,selectStyle,true);
  String siteSelect = DBA.getListBox("Site_Reference","process",Site_Reference,
    Global_Customer_Id,1," style=\"width:450px\"",true);
%>
  <a name="top"></a>
  <script language="JavaScript">
  <!--
  function Site_Reference_List_Process()
  {
    OSSBANMenu.ButtonPressed.value = 'Amend Site 2';
    OSSBANMenu.submit();
  }
  //-->
  </script>
  <table id=1 width ="100%">
    <tr>
      <td colspan=3>
        <%@ include file="../includes/Page_Header4.htm"%>
      </td>
    </tr>
    <tr>
      <td colspan=3>
      <form name="OSSBANMenu" method="post" action="OSSBANMenuHandler.jsp">
      <%=HB.getBANMenu_Bar("Menu","","",(String)session.getAttribute("System"))%>
      <input name="ButtonPressed" type=hidden value="">
      </td>
    </tr>
    <tr>
      <td colspan=3>
      &nbsp;
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
	      <h3 align="center"><b>Create a BAN for an existing Customer</b></h3>
	       <%=Message%><br>
	       <%session.setAttribute("Message","");%></font>
	      <b>Please select the entries from the lists relevant to the type of BAN you wish to create</b>
	      <table width="100%" border="0">
		<tr>
                  <td colspan="2">&nbsp;</td>
		</tr>
	        <tr><!-- Customer-->
		  <td align=left colspan=2><font color="#0000FF">Customer Name:</font>
		  </td>
		  <td align=left>
		    <%=custSelect%>
		  </td>
		</tr>
		<tr>
		  <!-- Division-->
		  <td align=left colspan=2><font color="#0000FF">Division:</font>
		  </td>
		  <td  align=left>
		  <%=divSelect%>
		  </td>
		</tr>
		<tr>
		  <!-- C2 Ref-->
		  <td align=left colspan=2><font color="#0000FF">C2 Ref No:</font>
		  </td>
		  <td  align=left>
		  <%=c2Select%>
		  </td>
		</tr>
<%if (isMANS)
  {%>
      <input name="Circuit" type=hidden value="">
<%}
  else
  {%>
		<tr>
		  <td  align=left>
		    <font color="#0000FF">Circuit Id:</font>
		  </td>
		  <td align=right>
        	  </td>
		  <td  align=left>
		    <!--Circuit_Reference-->
		    <%=circuitSelect%>
		  </td>
		</tr>
<%}%>
	      </table>
	      <table border="0">
		  <tr>
		    <td colspan=3>&nbsp
		    </td>
		  </tr>
		  <tr>
		    <td width=650 colspan=3 valign="top"> <b>Please select the type of BAN you wish to create</b>
		  </tr>
		  <tr>
		    <td colspan=3>&nbsp
		    </td>
		  <tr>
		    <td height=20 valign=top width=200>
                    <!--
		      <%=imgSrc%>&nbsp
		      <%=createCustAcct%>
                    -->
		    </td>
		    <td height=20 valign=top width=200>
		      <%=imgSrc%>&nbsp
		      <%=createCircuit%>
		    </td>
		    <td width=250>
		      <%=imgSrc%>&nbsp
		      <%=createRecCharge%>
		     </td>
		  </tr>
		  <tr>
		    <td height=20 valign=top width=200>
                    <!--
		    <img src='../nr/cw/newimages/menu_brown.gif'>&nbsp
		    <%=HB.getButton("Amend a Customer's Account",ActionDivision)%>
                    -->
		    </td>
		    <td height=20 valign=top>
		      <%=imgSrc%>&nbsp
		      <%=amendCircuit%>
		     </td>
		    <td>
		      <%=imgSrc%>&nbsp
		      <%=amendRecCharge%>
		     </td>
		  </tr>
<!--
		  <tr>
		    <td width=200>
		    </td>
		    <td height=20 valign=top>
		      <%=imgSrc%>&nbsp
		      <%=ceaseCircuit%>
		    </td>
		    <td>
		      <%=imgSrc%>&nbsp
		      <%=ceaseRecCharge%>
		     </td>
		  </tr>
-->
		  <tr>
		    <td width=200>
		    </td>
		    <td height=20 valign=top>
		    </td>
		    <td>
		      <%=imgSrc%>&nbsp
		      <%=deleteRecCharge%>
		     </td>
		  </tr>
		  <tr>
		    <td height=20 valign=top width=200>
		    </td>
		    <td>
		    </td>
		    <td>
		      <%=imgSrc%>&nbsp
		      <%=ceaseReprovideCharge%>
		     </td>
		  </tr>
		  <tr>
		    <td height=20 valign=top width=200>
		    </td>
		    <td>
		      <%=imgSrc%>&nbsp
		      <%=createSite%>
		    </td>
		    <td>
		      <%=imgSrc%>&nbsp
		      <%=createCredit%>
		     </td>
		  </tr>
		  <tr>
		    <td>
		      <!--<%=imgSrc%>&nbsp
		      <%=createCarrier%>
                      -->
		    </td>
		    <td>
		      <%=imgSrc%>&nbsp
		      <%=amendSite%>
		    </td>
		    <td>
		      <%=imgSrc%>&nbsp
		      <%=amendCredit%>
		     </td>
		  </tr>
		  <tr>
		    <td width=200>
		    </td>
		    <td height=20 valign=top>
		    </td>
		    <td>
		      <%=imgSrc%>&nbsp
		      <%=deleteCredit%>
		     </td>
		  </tr>
		  <tr>
		    <td>
    		      <!--<%=imgSrc%>&nbsp
		      <%=amendCarrier%>
                      -->
		    </td>
		    <td>
		    </td>
<!--
		    <td>
		      <%=imgSrc%>&nbsp
		      <%=createAdjustment%>
		     </td>
-->
		    <td>
		      <%=imgSrc%>&nbsp
		      <%=createOneOffCharge%>
		     </td>
		  <tr>
		    <td>
		    </td>
		    <td>
		    </td>
		    <td>
		      <%=imgSrc%>&nbsp
		      <%=amendOneOffCharge%>
		    </td>
		  </tr>
		  <tr>
		    <td width=200>
		    </td>
		    <td height=20 valign=top>
		    </td>
		    <td>
		      <%=imgSrc%>&nbsp
		      <%=deleteOneOffCharge%>
		     </td>
		  </tr>
		</table>
                <table style="visibility: <%=siteVis%>" width="70%" border="0">
                  <tr>
                    <td colspan="2">&nbsp;</td>
                  </tr>
                  <tr>
                    <td width="25%" align=right colspan=2><font color="#0000FF">Site:</font>
                    </td>
                    <td align=left>
                      <%=siteSelect%>
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
      <td height="100">&nbsp
      </td>
    </tr>
      <!--Footer-->
      <tr>
        <td>
	</td
      </tr>
  </table><!--1-->
</body>
</html>


