<html>
<%@ include file="../includes/Page_Header6.jsp"%>
<%@ page import="JavaUtil.*"%>
<jsp:useBean id="BAN" class="DBUtilities.BANBean" scope="session"/>
<jsp:useBean id="RSB" class="DBUtilities.RevenueShareBean" scope="session"/>
<jsp:useBean id="HB" class="HTMLUtil.HTMLBean" scope="session"/>
<jsp:useBean id="DBA" class="DBUtilities.DBAccess" scope="session"/>

<%  String PageSent;
    String sharedPath=
          EBANProperties.getEBANProperty(EBANProperties.SHAREDPATH);
    String colspan;
    String system;
    StringUtil SU = new JavaUtil.StringUtil();
    String Message;
    String providerId = RSB.getProviderId();
    String masterAccountId = RSB.getMasterAccountId();
    String accountId = RSB.getAccountId();
    String productGroupId = RSB.getProductGroupId();
    String productId = RSB.getProductId();
    String actionProvider;
    String actionProduct;
    String actionProductGroup;
    String actionMaster;
    String actionAccount;
    String actionAccountProduct;
    String actionMasterProductGroup;
//Set Initial values
  //Are we coming from the handler page, or a fresh start?

  system = (String)session.getAttribute("System");

  Message=SU.isNull(RSB.getMessage(),SU.isNull((String)session.getAttribute("Message"),""));
  RSB.setMessage("");
  PageSent=SU.isNull((String)session.getAttribute("PageSent"),"");
  String selectStyle = " style=\"width:400px\"";
  String viewProMa;
  String viewAccMoveHist;

  if (providerId.equals(""))
    {actionProvider="alert('You must select a Provider for this option')";}
  else
    {actionProvider="submitForm(this)";}
  if (masterAccountId.equals(""))
  {
    actionMaster="alert('You must select a Master Account for this option')";
    viewProMa = "";
  }
  else
  {
    actionMaster="submitForm(this)";
    viewProMa = "View Provider/Master Account Summary";
  }
  if (accountId.equals(""))
  {
    actionAccount="alert('You must select an Account for this option')";
    viewAccMoveHist = "";
  }
  else
  {
    actionAccount="submitForm(this)";
    viewAccMoveHist = "View Account Move History";
  }
  if (productGroupId.equals(""))
    {actionProductGroup="alert('You must select a Product Group for this option')";}
  else
    {actionProductGroup="submitForm(this)";}
  if (productId.equals(""))
    {actionProduct="alert('You must select a Product for this option')";}
  else
    {actionProduct="submitForm(this)";}
  if ((accountId.equals("")) || (productId.equals("")))
    {actionAccountProduct="alert('You must select both an Account and a Product for this option')";}
  else
    {actionAccountProduct="submitForm(this)";}
  if ((masterAccountId.equals("")) || (productGroupId.equals("")) || (productGroupId.equals("PRS")))
    {actionMasterProductGroup="alert('You must select both a Master Account and a Product Group (not PRS) for this option')";}
  else
    {actionMasterProductGroup="submitForm(this)";}

  colspan = "5";
  String Menu_Headings[]={"Go To", "Options"};
  String Options[][] = {{"Log Off","Revenue Share Desktop","Missing Data Identification"},
                            {"Reset","Refresh","View All Processes",viewProMa,viewAccMoveHist}};
  HB.setMenu_Headings(Menu_Headings);
  HB.setOption_Array(Options);

%>
<script language="JavaScript">
function Toggle_Sort(buttonPressed)
{
  if ((buttonPressed == 'Customer_Order') ||
    (buttonPressed == 'DOB') ||
    (buttonPressed == 'Status'))
  {
    rsDesktop.sortOrder.value = buttonPressed;
    rsDesktop.submit();
  }
  else if (buttonPressed == 'Action')
  {
    window.open("rsDesktopKey.htm", "ndk", "toolbar=no,menubar=no,location=no," +
      "directories=no,status=no,scrollbars=no,resizable=no,height=200,width=200,top=50,left=50");
  }
}
</script>
<form name="referenceDataMenu" method="post" action="referenceDataMenuHandler.jsp">
  <input name="ButtonPressed" type=hidden value="">
  <a name="top"></a>
  <table id=1 width="100%" border=0>
    <tr>
      <td colspan=3>
        <%@ include file="../includes/Page_Header4.htm"%>
      </td>
    </tr>
    <tr>
      <td colspan=3>
      <%=HB.getMenu_Bar()%>
      </td>
    </tr>
    <tr>
      <td colspan=3>
	<table width="100%" border="0" cellspacing="0" cellpadding="0" id=2>
          <tr>
            <td colspan=4>
	      <h3 align="center"><b>Reference Data</b></h3>
	      <%=Message%><br>
	      <%session.setAttribute("Message","");%></font>
            </td>
	  </tr>
          <tr>
            <td colspan=4>
	      <table width="100%" border="0">
                <tr>
                  <td><b>Call Month:</b></td>
                  <td class=bluebold><%=RSB.getLatestCallMonth()%></td>
                  <td><b>Current Status:</b></td>
                  <td class=bluebold><%=RSB.getCurrentStatus()%></td>
                  <td><b>RQR09 Load Time:</b></td>
                  <td class=bluebold><%=RSB.getRQR09LoadTime()%></td>
                </tr>
                <tr>
                  <td><b>Rating Time:</b></td>
                  <td class=bluebold><%=RSB.getRatingTime()%></td>
                  <td><b>Invoicing Time:</b></td>
                  <td class=bluebold><%=RSB.getInvoicingTime()%></td>
                  <td><b>Ebilling Upload Time:</b></td>
                  <td class=bluebold><%=RSB.getEbillingUploadTime()%></td>
                </tr>
                <tr>
                  <td colspan="6">
                    <hr>
                  </td>
                </tr>
              </table>
            </td>
	  </tr>
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
	      <table width="100%" border="0">
	        <tr>
                  <td colspan="2">&nbsp;</td>
		</tr>
	        <tr>
		  <td align=left colspan=2><font color="#0000FF">Provider:</font>
		  </td>
		  <td align=left>
		    <%=DBA.getListBox("rsProvider","submit",providerId,(String)session.getAttribute("User_Id"),1,selectStyle,true)%>
		  </td>
                  <td>&nbsp;</td>
		</tr>
		<tr>
		  <td align=left colspan=2><font color="#0000FF">Master Account:</font>
		  </td>
		  <td  align=left>
		  <%=DBA.getListBox("rsMasterAccount","submit",masterAccountId,providerId,1,selectStyle,true)%>
		  </td>
                  <td>&nbsp;</td>
		</tr>
		<tr>
		  <td align=left colspan=2><font color="#0000FF">Account:</font>
		  </td>
		  <td  align=left>
		  <%=DBA.getListBox("rsAccount","submit",accountId,masterAccountId,1,selectStyle,true)%>
		  </td>
                  <td>&nbsp;</td>
		</tr>
		<tr>
                  <td colspan=4>&nbsp;</td>
		</tr>
		<tr>
		  <td align=left colspan=2><font color="#0000FF">Product Group:</font>
		  </td>
		  <td  align=left>
		  <%=DBA.getListBox("rsProductGroup","submit",productGroupId,(String)session.getAttribute("User_Id"),1,selectStyle,true)%>
		  </td>
                  <td>&nbsp;</td>
		</tr>
		<tr>
		  <td align=left colspan=2><font color="#0000FF">Product:</font>
		  </td>
		  <td  align=left>
		  <%=DBA.getListBox("rsProduct","submit",productId,productGroupId,1,selectStyle,true)%>
		  </td>
                  <td>&nbsp;</td>
		</tr>
	      </table>
	      <table border="0">
		  <tr>
		    <td colspan=<%=colspan%>>&nbsp
		    </td>
		  </tr>
		  <tr>
		    <td width=800 colspan=<%=colspan%> valign="top"> <b>Please select from the list of maintenance options below</b>
		  </tr>
		  <tr>
		    <td colspan=<%=colspan%>>&nbsp
		    </td>
		  </tr>
		  <tr>
		    <td height=20 width=200>
		      <img src='<%=sharedPath%>/nr/cw/newimages/menu_brown.gif'>&nbsp
		      <%=HB.getButton("Create Provider","submitForm(this)")%>
		    </td>
		    <td height=20 valign=top width=200>
		      <img src='<%=sharedPath%>/nr/cw/newimages/menu_brown.gif'>&nbsp
		      <%=HB.getButton("Create Master Account",actionProvider)%>
		    </td>
		    <td height=20 valign=top width=200>
		      <img src='<%=sharedPath%>/nr/cw/newimages/menu_brown.gif'>&nbsp
		      <%=HB.getButton("Create Account",actionMaster)%>
		    </td>
		    <td height=20 valign=top width=200>
		      <img src='<%=sharedPath%>/nr/cw/newimages/menu_brown.gif'>&nbsp
		      <%=HB.getButton("Create Product Group","submitForm(this)")%>
		    </td>
		    <td height=20 valign=top width=200>
		      <img src='<%=sharedPath%>/nr/cw/newimages/menu_brown.gif'>&nbsp
		      <%=HB.getButton("Create Product",actionProductGroup)%>
		    </td>
		  </tr>
		  <tr>
		    <td height=20 width=200>
		      <img src='<%=sharedPath%>/nr/cw/newimages/menu_brown.gif'>&nbsp
		      <%=HB.getButton("Update Provider",actionProvider)%>
		    </td>
		    <td height=20 valign=top width=200>
		      <img src='<%=sharedPath%>/nr/cw/newimages/menu_brown.gif'>&nbsp
		      <%=HB.getButton("Update Master Account",actionMaster)%>
		    </td>
		    <td height=20 valign=top width=200>
		      <img src='<%=sharedPath%>/nr/cw/newimages/menu_brown.gif'>&nbsp
		      <%=HB.getButton("Update Account",actionAccount)%>
		    </td>
		    <td height=20 valign=top width=200>
		      <img src='<%=sharedPath%>/nr/cw/newimages/menu_brown.gif'>&nbsp
		      <%=HB.getButton("Update Product Group",actionProductGroup)%>
		    </td>
		  </tr>
		  <tr>
		    <td height=20 width=200>
                      &nbsp;
		    </td>
		    <td height=20 valign=top width=200>
		      <img src='<%=sharedPath%>/nr/cw/newimages/menu_brown.gif'>&nbsp
		      <%=HB.getButton("Delete Master Account",actionMaster)%>
		    </td>
		    <td height=20 valign=top width=200>
		      <img src='<%=sharedPath%>/nr/cw/newimages/menu_brown.gif'>&nbsp
		      <%=HB.getButton("Delete Account",actionAccount)%>
		    </td>
		    <td height=20 valign=top width=200>
		      <img src='<%=sharedPath%>/nr/cw/newimages/menu_brown.gif'>&nbsp
		      <%=HB.getButton("Delete Product Group",actionProductGroup)%>
		    </td>
		    <td height=20 valign=top width=200>
		      <img src='<%=sharedPath%>/nr/cw/newimages/menu_brown.gif'>&nbsp
		      <%=HB.getButton("Delete Product",actionProduct)%>
		    </td>
		  </tr>
		  <tr>
		    <td height=20 width=200>
                      &nbsp;
		    </td>
		    <td height=20 width=200>
                      &nbsp;
		    </td>
		    <td height=20 valign=top width=200>
		      <img src='<%=sharedPath%>/nr/cw/newimages/menu_brown.gif'>&nbsp
		      <%=HB.getButton("Add Product to Account",actionAccountProduct)%>
		    </td>
		  </tr>
		  <tr>
		    <td height=20 width=200>
                      &nbsp;
		    </td>
		    <td height=20 width=200>
                      &nbsp;
		    </td>
		    <td height=20 valign=top width=200>
		      <img src='<%=sharedPath%>/nr/cw/newimages/menu_brown.gif'>&nbsp
		      <%=HB.getButton("Delete Product from Account",actionAccountProduct)%>
		    </td>
		    <td height=20 valign=top colspan=2>
		      <img src='<%=sharedPath%>/nr/cw/newimages/menu_brown.gif'>&nbsp
		      <%=HB.getButton("Update Product NTS Prefix List","submitForm(this)")%>
		    </td>
		  </tr>
		  <tr>
		    <td height=20 width=200>
                      &nbsp;
		    </td>
		  </tr>
		  <tr>
        <td colspan="5">
          <table width="100%">
            <tr>
		    <td height=20 width=250>
                      &nbsp;
		    </td>
		    <td height=20 valign=top width=250>
		      <img src='<%=sharedPath%>/nr/cw/newimages/menu_brown.gif'>&nbsp
		      <%=HB.getButton("Create Invoice/Master Threshold",actionMaster)%>
		    </td>
<!--
		    <td height=20 valign=top width=250>
		      <img src='<%=sharedPath%>/nr/cw/newimages/menu_brown.gif'>&nbsp
		      <%=HB.getButton("Create Product/System Threshold",actionProduct)%>
		    </td>
-->
		    <td height=20 valign=top width=250>
		      <img src='<%=sharedPath%>/nr/cw/newimages/menu_brown.gif'>&nbsp
		      <%=HB.getButton("Create Product Group/Master Threshold",actionMasterProductGroup)%>
		    </td>
		  </tr>
		  <tr>
		    <td height=20 valign=top width=250>
		      <img src='<%=sharedPath%>/nr/cw/newimages/menu_brown.gif'>&nbsp
		      <%=HB.getButton("Update Invoice/System Threshold","submitForm(this)")%>
		    </td>
		    <td height=20 valign=top width=250>
		      <img src='<%=sharedPath%>/nr/cw/newimages/menu_brown.gif'>&nbsp
		      <%=HB.getButton("Update Invoice/Master Threshold",actionMaster)%>
		    </td>
<!--
		    <td height=20 valign=top width=250>
		      <img src='<%=sharedPath%>/nr/cw/newimages/menu_brown.gif'>&nbsp
		      <%=HB.getButton("Update Product/System Threshold",actionProduct)%>
		    </td>
-->
		    <td height=20 valign=top width=250>
		      <img src='<%=sharedPath%>/nr/cw/newimages/menu_brown.gif'>&nbsp
		      <%=HB.getButton("Update Product Group/Master Threshold",actionMasterProductGroup)%>
		    </td>
		  </tr>
		  <tr>
		    <td height=20 width=250>
                      &nbsp;
		    </td>
		    <td height=20 valign=top width=250>
		      <img src='<%=sharedPath%>/nr/cw/newimages/menu_brown.gif'>&nbsp
		      <%=HB.getButton("Delete Invoice/Master Threshold",actionMaster)%>
		    </td>
<!--
		    <td height=20 valign=top width=250>
		      <img src='<%=sharedPath%>/nr/cw/newimages/menu_brown.gif'>&nbsp
		      <%=HB.getButton("Delete Product/System Threshold",actionProduct)%>
		    </td>
-->
		    <td height=20 valign=top width=250>
		      <img src='<%=sharedPath%>/nr/cw/newimages/menu_brown.gif'>&nbsp
		      <%=HB.getButton("Delete Product Group/Master Threshold",actionMasterProductGroup)%>
		    </td>
        </tr>
          </table>
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
	  &nbsp;
	</td
      </tr>
  </table><!--1-->
</body>
</html>
