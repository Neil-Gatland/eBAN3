<html>
<head>
<script language="JavaScript">
<!--
function acceptClick()
{
  parent.ListCharges.<%=request.getParameter("name")%>.value = filter.value;
  parent.ListCharges.submit();
}
//-->
</script>
</head>
<body style="background-color:#eaeffa;">
  <span id="test" style="position:absolute;top:-3px;">
    <input id="filter" type="text" value="<%=request.getParameter("value")%>" style="width:415px;font-weight:bold;font-size:6pt;background-color:#eaeffa;">
    <input type="image" src="../nr/cw/newimages/accept.gif" onClick="acceptClick();">
  </span>
</body>
</html>