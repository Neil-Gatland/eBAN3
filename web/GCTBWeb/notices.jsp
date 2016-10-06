<%@ page import="java.io.*"%>

<table>
<%
  StringBuffer table = new StringBuffer();
  File notices = new File("c:\\test\\messagefile.txt");
  if (notices.exists())
  {
    BufferedReader br = new BufferedReader(new FileReader(notices));
    String line = br.readLine();
    while (line != null)
    {
      table.append("<tr><td><font size=-1 face=arial color=red><b>" + line + "</b></font></td></tr>");
      line = br.readLine();
    }
    br.close();
  }
  //notices = null;
%>
<%=table.toString()%>
</table>
