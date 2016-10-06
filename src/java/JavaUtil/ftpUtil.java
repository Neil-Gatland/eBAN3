package JavaUtil;

import org.apache.commons.net.ftp.*;
import java.io.*;

public class ftpUtil {

  // Class variables
  private String tcpip, username, password;
  private org.apache.commons.net.ftp.FTPClient ftpObj;

public ftpUtil(String host, String user, String pword)
{
  tcpip = host;
  username = user;
  password = pword;
  ftpObj = new org.apache.commons.net.ftp.FTPClient();
}

public boolean connectToHost()
{
  boolean result = false;
  try
  {
    ftpObj.connect(tcpip);
    ftpObj.login(username,password);
    int reply = ftpObj.getReplyCode();
    if (FTPReply.isPositiveCompletion(reply))
      result = true;
  }
  catch(java.io.IOException ex)
  {
    System.out.println("Failed to connect to "+tcpip+" with "+username+"/"+password+" : "+ex.getMessage());
  }
  return result;
}

public boolean disconnectFromHost()
{
  boolean result = false;
  try
  {
    ftpObj.disconnect();
    int reply = ftpObj.getReplyCode();
    if (FTPReply.isPositiveCompletion(reply))
      result = true;
  }
  catch(java.io.IOException ex)
  {
    System.out.println("Failed to connect to "+tcpip+" with "+username+"/"+password+" : "+ex.getMessage());
  }
  return result;
}

public boolean transferFile(String downDir, String serverFile, String localFile)
{
  boolean result = false;
  try
  {
    BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(downDir+File.separator+localFile));
    if (ftpObj.retrieveFile("\'"+serverFile+"\'",out))
    {
      int reply = ftpObj.getReplyCode();
      if (FTPReply.isPositiveCompletion(reply))
        result = true;
    }
    out.close();
  }
  catch(org.apache.commons.net.io.CopyStreamException ex)
  {
    System.out.println("Failed to transfer file (stream exception at "+ex.getTotalBytesTransferred()+" bytes) "+serverFile+" as "+localFile+" : "+ex.getMessage());
  }
  catch(org.apache.commons.net.ftp.FTPConnectionClosedException ex)
  {
    System.out.println("Failed to transfer file (connection closed) "+serverFile+" as "+localFile+" : "+ex.getMessage());
  }
  catch(java.io.IOException ex)
  {
    System.out.println("Failed to transfer file "+serverFile+" as "+localFile+" : "+ex.getMessage());
  }
  return result;
}

// empty as not required
public static void main (String[] args)
{
}


}