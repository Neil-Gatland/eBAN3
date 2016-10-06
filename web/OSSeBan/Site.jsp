<SCRIPT language="JavaScript">
function savetoparent()
{
  var formname='<%=(String)session.getAttribute("formname")%>';
  var framename=this.name;
  parent.document.all[framename+"_Site_Reference"].value=Site_Reference.value;
  parent.document.all[framename+"_City"].value=City.value;
  parent.document.all[framename+"_Site_Address"].value=Site_Address.value;
  parent.document.all[framename+"_Country"].value=Country.value;
  parent.document.all[framename].className='siteclosed';
}
</SCRIPT>
<link rel="stylesheet" type="text/css" href="../nr/cw/newcss/world_ie.css">
			  <table>
			    <tr>
			      <td><b><small>Site Reference<br><input name ="Site_Reference"></td>
			      <td><b><small>City<br><input size=7 style="WIDTH: 90px" type=text name ="City"></td>
			      <td><b><small>Country<br><input size=7 style="WIDTH: 90px" type=text name ="Country"></td>
			    </tr>
			    <tr>
			      <td colspan=2><b><small>Site Address<br><textarea name="Site_Address" cols=30></textarea></td>
			      <td><button class='menu_bar' onClick="savetoparent()">OK</button></td>
			    </tr>
			  </table>
