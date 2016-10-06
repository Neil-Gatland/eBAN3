<html>
<jsp:include page="../includes/Page_Header6.jsp" flush="true" />
<jsp:useBean id="BAN" class="DBUtilities.BANBean" scope="session"/>
<jsp:useBean id="HB" class="HTMLUtil.HTMLBean" scope="session"/>
<jsp:useBean id="DBA" class="DBUtilities.DBAccess" scope="session"/>
<jsp:useBean id="NTS" class="DBUtilities.NTSBean" scope="session"/>
<jsp:useBean id="RSB" class="DBUtilities.RevenueShareBean" scope="session"/>
<%
  JavaUtil.StringUtil SU = new JavaUtil.StringUtil();
  String fromForm = SU.isNull((String)session.getAttribute("formname"), "");
  session.setAttribute("formname","ntsPrefixMaint");
  String SQL;
  String Mode=NTS.getMode();
  String action;
  String Menu_Headings[]={"Go To", "Options"};
  String Options[][] = {{"Log Off","Revenue Share Desktop","Reference Data Menu"},
    {"Submit","","","","Refresh","View All Processes"}};

  if (Mode.equals("Confirm"))
  {
    Options[1][0] = "Confirm";
    Options[1][1] = "Cancel";
    Options[1][2] = "";
    Options[1][3] = "";
  }
  else if (Mode.equals("View"))
  {
    Options[1][0] = "";
    Options[1][1] = "";
    Options[1][2] = "";
    Options[1][3] = "";
  }
  else if (Mode.equals("Delete"))
  {
    Options[1][0] = "Delete";
    Options[1][1] = "";
    Options[1][2] = "";
    Options[1][3] = "";
    Options[1][4] = "";
  }
  else if (Mode.equals("Amend"))
  {
    Options[1][0] = "Update";
    Options[1][1] = "Add Product";
    Options[1][2] = "Move Account";
    Options[1][3] = "View Account Move History";
  }
  HB.setMenu_Headings(Menu_Headings);
  HB.setOption_Array(Options);
  String userId = (String)session.getAttribute("User_Id");
%>
<script language="JavaScript">
function Toggle_Sort(buttonPressed)
{
  if (buttonPressed == 'Action')
  {
    window.open("ntsPrefixKey.htm", "ndk", "toolbar=no,menubar=no,location=no," +
      "directories=no,status=no,scrollbars=no,resizable=no,height=100,width=400,top=50,left=50");
  }
}
</script>
<form name="ntsPrefixMaint" method="post" action="ntsPrefixMaintHandler.jsp">
<input name="ButtonPressed" type=hidden value="">
<input name="listProductCode" type=hidden value="">
<input name="listProductGroup" type=hidden value="">
<input name="listNTSPrefix" type=hidden value="">
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
      <tr id=2>
	<!-- this is a spacer column-->
	<td width="1" id=2>&nbsp;</td>
	<td width="1" valign="top" align="left" id=2>
	</td>
	<td width="12" id=2><!-- a spacer column-->
	  &nbsp;
	</td>
	<td valign="top" id=2>
	  <h3 align="center">Product NTS Prefix Set-Up and Maintenance</h3>
	  <table width="100%" border="0" id=3>
	    <tr>
	      <td colspan="3" width=100% class=bluebold>
    	        <%=SU.hasNoValue(NTS.getMessage(),"<font color=blue><b>Mandatory fields are shown in blue</b></font>")%>
<%
session.setAttribute("Message","");
NTS.setMessage("");
RSB.setMessage("");
%>
              </td>
            </tr>
            <tr>
              <td colspan=3>
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
              <td class=<%=NTS.getClass("rsProductGroup")%>><b>Product Group</b>
                <br><%=DBA.getListBox("rsProductGroup",NTS.getSelectMode(),NTS.getProductGroup(),(String)session.getAttribute("User_Id"),1,"style=\"width:90%\"",true)%>
              </td>
              <td class=<%=NTS.getClass("rsProduct")%>><b>Product Code</b>
                <br><%=DBA.getListBox("rsProduct",NTS.getSelectMode(),NTS.getProductCode(),NTS.getProductGroup(),1,"style=\"width:90%\"",true)%>
              </td>
              <td width="50%" class=<%=NTS.getClass("ntsPrefix")%>><b>NTS Prefix</b>
                <br><input style="height:18px;font-size:xx-small;" maxlength="7" type=text name="ntsPrefix" value="<%=NTS.getNTSPrefix()%>" <%=NTS.getMode("ntsPrefix")%>>
              </td>
            </tr>
            <tr>
              <td colspan=3>&nbsp;
              </td>
            </tr>
            <tr>
              <td colspan=3>
                <table border=0 width="75%">
                  <tr class=gridHeader>
                    <td class=gridHeader NOWRAP valign=top width="250">
                      <button class=grid_menu>Product Code</button>
                    </td>
                    <td class=gridHeader NOWRAP valign=top width="150">
                      <button class=grid_menu>NTS Prefix</button>
                    </td>
                    <td class=gridHeader NOWRAP valign=top width="125">
                      <button class=grid_menu>Last Maintained Date</button>
                    </td>
                    <td class=gridHeader NOWRAP valign=top width="125">
                      <button class=grid_menu>Last Maintained By</button>
                    </td>
                    <td class=gridHeader NOWRAP valign=top>
                      <button class=grid_menu onclick="Toggle_Sort('Action')">Action <font color=red>(click here for key)</font></button>
                    </td>
                  </tr>
                  <tr>
                    <td colspan=5>
                    <iframe frameborder=0 width="100%" height=350 id=GridData name=GridData src="ntsPrefixGrid.jsp#<%=NTS.getProductCode()%><%=NTS.getNTSPrefix()%>"></iframe>
                    </td>
                  </tr>
                </table>
              </td>
            </tr>
	  </table><!--table 3-->
      </td>
      <td width="1">&nbsp;</td><!--spacer-->
      <td width="1" valign="top"><!--Right Hand column-->
      </td>
    </tr>
  </table><!--2-->
    </td>
  </tr>
</table><!--1-->
</form>
</body>
</html>


