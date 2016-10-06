<SCRIPT language="JavaScript">
function savetoparent()
{
  var formname='<%=(String)session.getAttribute("formname")%>';
  parent.document.forms[formname].Site_Name.value=Site_Name.value;
  parent.document.forms[formname].Site_Reference.value=Site_Reference.value;
  parent.document.forms[formname].City.value=City.value;
  parent.document.forms[formname].Site_Address.value=Site_Address.value;
  parent.document.forms[formname].Country.value=Country.value;
  parent.document.all['SiteFrame'].className='siteclosed';
}
</SCRIPT>
<link rel="stylesheet" type="text/css" href="../nr/cw/newcss/world_ie.css">
			  <table>
			    <tr>
			      <td><b><small>Site Reference<br><input name ="Site_Reference"></td>
			      <td><b><small>Site Name<br><input name ="Site_Name"></td>
			      <td><b><small>City<br><input type=text name ="City"></td>
			      <td><b><small>Country<br><input name ="Country"></td>
			      <td width=100><button class='menu_bar' onClick="savetoparent()">OK</button></td>
			    </tr>
			    <tr>
			      <td colspan=5><b><small>Site Address<br><textarea name="Site_Address" cols=50></textarea></td>
			    </tr>
			  </table>
