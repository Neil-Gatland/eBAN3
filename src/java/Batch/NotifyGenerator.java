package Batch;

import java.text.*;
import java.util.*;
import java.security.*;
import java.security.cert.X509Certificate;
import java.security.cert.CertificateFactory;
import javax.mail.*;
import javax.mail.internet.*;
import com.isnetworks.smime.*;
import java.util.*;
import java.io.*;
import java.util.zip.*;
import javax.activation.*;
import DBUtilities.*;
import JavaUtil.*;

public class NotifyGenerator
{
  private String fromAddress =
    EBANProperties.getEBANProperty("fromAddress"); //"C&W e-billing <admin@e-billing.cw.com>";;
  private String replyAddress =
    EBANProperties.getEBANProperty("replyToAddress"); //"C&W e-billing Support <e-bill@cw.com>";;
  private final String mailHost =
    EBANProperties.getEBANProperty("mailHost");
  private final String securityPath =
    EBANProperties.getEBANProperty("securityPath");
  private final String logDir =
    EBANProperties.getEBANProperty("notifyLogDir");
  private final String attachmentPath =
    EBANProperties.getEBANProperty("dfAttDir");
  private final String cwImage =
    EBANProperties.getEBANProperty("cwImage");
  private final String styleSheet =
    EBANProperties.getEBANProperty("styleSheet");
  private final String ebImage =
    EBANProperties.getEBANProperty("ebImage");
  private final String href =
    EBANProperties.getEBANProperty("href");
  private final String bulkInvoicePath =
    EBANProperties.getEBANProperty("bulkInvDir");
  //private final String pdfMessage =
    //EBANProperties.getEBANProperty("pdfMessage", "");
  private final Properties pdfMessages = EBANProperties.getEBANProperties("pdfMessage");
  private final Properties notifyMessages = EBANProperties.getEBANProperties("notifyMessage");

  private final String DATETIMEFORMAT = "dd/MM/yyyy HH:mm:ss";
  private final String TIMESTAMPFORMAT = "yyyyMMddHHmmssSSS";

  private File logFile;
  private BufferedWriter logWriter;
  private File backupFile;
  private BufferedWriter backupWriter;
  private String runTS;
  private String storedEmailAddress;
  private DBAccess dba;
  private StringUtil su;
  private StringBuffer sb;
  private Object[] pdfMessageKeys;
  private Object[] notifyMessageKeys;

  public NotifyGenerator()
  {
    dba = new DBAccess();
    su = new StringUtil();
    runTS = su.reformatDate("now", null, TIMESTAMPFORMAT);
    logFile = new File(logDir + File.separator + "Notify_" +
      runTS + "_log.txt");
    pdfMessageKeys = pdfMessages.keySet().toArray();
    java.util.Arrays.sort(pdfMessageKeys);
    notifyMessageKeys = notifyMessages.keySet().toArray();
    java.util.Arrays.sort(notifyMessageKeys);
  }

  private void createLogFile()
  {
    try
    {
      logWriter = new BufferedWriter(new FileWriter(logFile));
      String now = su.reformatDate("now", null, DATETIMEFORMAT);
      writeToLogFile("Notification Email Generation processing started - " +
        now);
    }
    catch (Exception ex)
    {
      System.out.println("Error creating log file: " + ex.getMessage());
      System.exit(1);
    }
  }

  private void writeToLogFile(String message)
  {
    try
    {
      logWriter.write(message);
      logWriter.newLine();
    }
    catch (Exception ex)
    {
      System.out.println("Error writing message '" + message +
        "' to log file: " + ex.getMessage());
      System.exit(1);
    }
  }

   private void createBackupFile(String source)
  {
    try
    {
      backupFile = new File(EBANProperties.getEBANProperty("monbackDir") +
        File.separator + source + ".txt");
      if (!backupFile.exists())
      {
        backupFile.createNewFile();
      }
      backupWriter = new BufferedWriter(new FileWriter(backupFile));
    }
    catch (Exception ex)
    {
      System.out.println("Error creating backup file: " + ex.getMessage());
      System.exit(1);
    }
  }

  private void writeToBackupFile(String message)
  {
    try
    {
      backupWriter.write(message);
      backupWriter.newLine();
    }
    catch (Exception ex)
    {
      System.out.println("Error writing message '" + message +
        "' to backup file: " + ex.getMessage());
      System.exit(1);
    }
  }

 private void closeLogFile()
  {
    try
    {
      String now = su.reformatDate("now", null, DATETIMEFORMAT);
      writeToLogFile("Notification Email Generation processing ended - " + now);
      logWriter.close();
    }
    catch (Exception ex)
    {
      System.out.println("Error closing log file: " + ex.getMessage());
      System.exit(1);
    }
  }


 private void closeBackupFile()
  {
    try
    {
      backupWriter.close();
    }
    catch (Exception ex)
    {
      System.out.println("Error closing backup file: " + ex.getMessage());
      System.exit(1);
    }
  }


  public boolean generateAndSendAll(String source)
  {
    boolean ok = false;
    try
    {
      if (dba.processAlreadyRunning("Generic", "Send Notification Emails"))
      {
        String message = "Notification processing is already active.";
        System.out.println(message);
        createLogFile();
        writeToLogFile(message);
        closeLogFile();
        sendSignedEmail(replyAddress, "Error in notification processing", message,
          null);
        return ok;
      }
      else
      {
        dba.updateRunControl("Send Notification Emails", "Active", "Generic");
      }
      createLogFile();
      //get all PDF_Email_Requests

      Collection pdfs = dba.getAllPDFEmailRequests();
//writeToLogFile("dba.getAllPDFEmailRequests()");
      EmailRequestDescriptor erdPrev = new EmailRequestDescriptor();
      for (Iterator it = pdfs.iterator(); it.hasNext(); )
      {
//writeToLogFile("top of loop");
        EmailRequestDescriptor erd = (EmailRequestDescriptor)it.next();
        //process
//  sendPDFEmail(erd);
//  dba.removePDFEmailRequest(erd);
        if ((erd.getInvoiceId() == erdPrev.getInvoiceId()) &&
          (erd.getLoginId() == erdPrev.getLoginId()))
        {
          erd.addAttachmentLocationArray(erdPrev.getAttachmentLocationArray());
        }
        else
        {
          if (erdPrev.getInvoiceId() > 0)
          {
            sendPDFEmail(erdPrev);
          }
          //erdPrev = erd;
        }
        erdPrev = erd;

        //delete
        dba.removePDFEmailRequest(erd);
      }
      //flush the last one
      if (erdPrev.getInvoiceId() > 0)
      {
        sendPDFEmail(erdPrev);
      }
      //get all Notification_Email_Requests
      Collection notify = dba.getAllNotificationEmailRequests();
      if (!notify.isEmpty())
      {
        long previousLoginId = 0;
        sb = new StringBuffer("<link rel=\"stylesheet\" type=\"text/css\" href=\"" +
          styleSheet +"\"><table><tr><td><img src=\"" +
          cwImage + "\"></td><td><img src=\"" + ebImage + "\"></td></tr>" +
          "<tr><td colspan=\"2\">&nbsp;</td></tr>");
        for (int i=0; i<notifyMessageKeys.length; i++)
        {
          String msgLine = notifyMessages.getProperty((String)notifyMessageKeys[i]);
//System.out.println("message n: " + msgLine);
            sb.append("<tr><td colspan=\"2\">" + msgLine + "</td></tr>");
        }
        sb.append("<tr><td colspan=\"2\">&nbsp;</td></tr>");
        EmailRequestDescriptor lastErd = null;
        boolean lastAlone = false;
        for (Iterator it = notify.iterator(); it.hasNext(); )
        {
          EmailRequestDescriptor erd = (EmailRequestDescriptor)it.next();
          //process
          sendNotificationEmail(erd, previousLoginId, false, false);
          lastAlone = previousLoginId!=0&&previousLoginId!=erd.getLoginId();
          previousLoginId = erd.getLoginId();
          lastErd = erd;
          //delete
          dba.removeNotificationEmailRequest(erd);
        }
        //flush the last login
        sendNotificationEmail(lastErd, -1, true, lastAlone);
      }
      //send processing complete email
      if (!source.equals("none"))
      {
        Properties addrs = source.endsWith("Error")?EBANProperties.getEBANProperties(source):
          EBANProperties.getEBANProperties("notifyAddr");
        InternetAddress[] recipients = new InternetAddress[addrs.size()];
        Enumeration en = addrs.elements();
        int pos = 0;
        while (en.hasMoreElements())
        {
          String addr = (String)en.nextElement();
          recipients[pos] = new InternetAddress(addr);
          pos++;
        }
        String subject = source.endsWith("Error")?("Error in " +
          source.substring(0, source.indexOf("Error")) + " daily CDRs"):
          (source + " daily CDRs complete");
        String contents = source.endsWith("Error")?("Error in " +
          source.substring(0, source.indexOf("Error")) + " daily CDR processing"):
          (source + " daily CDR processing has completed");
        sendSignedEmail(recipients, subject, contents, null);
      }
      ok = true;
    }
    catch (Exception ex)
    {
      String message = "Error in generateAndSendAll(): " + ex.getMessage();
      System.out.println(message);
      writeToLogFile(message);
      sendSignedEmail(replyAddress, "Error in notification processing", message,
        null);
    }
    finally
    {
      try
      {
        dba.updateRunControl("Send Notification Emails", "Inactive", "Generic");
      }
      catch (Exception ex)
      {
        String message = "Error resetting run status: " + ex.getMessage();
        System.out.println(message);
        writeToLogFile(message);
        sendSignedEmail(replyAddress, "Error in notification processing", message,
          null);
      }
      closeLogFile();
      return ok;
    }
  }

  public boolean generateStatsEmail(String source)
  {
    boolean ok = false;
    try
    {
      createLogFile();
      if (!source.equals("none"))
      {
        File statsFile = new File(EBANProperties.getEBANProperty("statsDir") +
          File.separator + source+"out.txt");
        String title = EBANProperties.getEBANProperty(source+"Title");
        Properties addrs = EBANProperties.getEBANProperties(source+"Addr");
        InternetAddress[] recipients = new InternetAddress[addrs.size()];
        Enumeration en = addrs.elements();
        int pos = 0;
        while (en.hasMoreElements())
        {
          String addr = (String)en.nextElement();
          recipients[pos] = new InternetAddress(addr);
          pos++;
        }
        StringBuffer contents = new StringBuffer();
        //loop
        BufferedReader buff = new BufferedReader(new FileReader(statsFile.getAbsolutePath()));
        String line = buff.readLine();
        if ((line != null) && (line.length() > 0))
        {
          boolean eof = false;

          while (!eof)
          {
            if (line.equals("PL/SQL procedure successfully completed."))
            {
              eof = true;
            }
            else
            {
              contents.append(line);
            }
            line = buff.readLine();
          }
        }
        sendSignedEmail(recipients, title,
          contents.toString(), null);
      }
      ok = true;
    }
    catch (Exception ex)
    {
      String message = "Error in generateStatsEmail(): " + ex.getMessage();
      System.out.println(message);
      writeToLogFile(message);
      sendSignedEmail(replyAddress, "Error in notification processing", message,
        null);
    }
    finally
    {
      closeLogFile();
      return ok;
    }
  }

  public boolean generateArborEmail(String source)
  {
    boolean ok = false;
    try
    {
      createLogFile();
      if (!source.equals("none"))
      {
        File statsFile = new File(EBANProperties.getEBANProperty("arborDir") +
          File.separator + source+ ".txt");
        String title = EBANProperties.getEBANProperty(source+"Title");
        Properties addrs = EBANProperties.getEBANProperties(source+"Addr");
        InternetAddress[] recipients = new InternetAddress[addrs.size()];
        Enumeration en = addrs.elements();
        int pos = 0;
        while (en.hasMoreElements())
        {
          String addr = (String)en.nextElement();
          recipients[pos] = new InternetAddress(addr);
          pos++;
        }
        Calendar now = Calendar.getInstance();
        int hh = now.get(now.HOUR_OF_DAY);
        int mm = now.get(now.MINUTE);
        int dd = now.get(now.DATE);
        int mo = now.get(now.MONTH) + 1;
        StringBuffer contents = new StringBuffer("Arbor processing ended at " +
          (hh>9?Integer.toString(hh):"0"+Integer.toString(hh)) + ":" +
          (mm>9?Integer.toString(mm):"0"+Integer.toString(mm)) + " on " +
          (dd>9?Integer.toString(dd):"0"+Integer.toString(dd)) + "/" +
          (mo>9?Integer.toString(mo):"0"+Integer.toString(mo)) + "/" +
          now.get(now.YEAR) + ".<br>");
        if (source.equals("arborError"))
        {
          contents.append("Errors found during PostScript file processing. Please investigate.<br>");
        }
        else if (source.equals("arborErrorFTP"))
        {
          contents.append("Error during Arbor FTP step. Please investigate.<br>");
        }
        else if (source.equals("arborNoFiles"))
        {
          contents.append("No Arbor files received today. Please investigate.<br>");
        }
        if (!source.equals("arborNoFiles"))
        {
          //loop
          BufferedReader buff = new BufferedReader(new FileReader(statsFile.getAbsolutePath()));
          String line = buff.readLine();
          contents.append(line+"<br>"); //allow for empty first line
          line = buff.readLine();
          int lineCount = 1;
          while ((line != null)/* && (line.length() > 0)*/)
          {
            contents.append(line+"<br>");
            line = buff.readLine();
            lineCount++;
          }
        }
        /*if ((source.equals("arborAccounts")) && (lineCount <= 2))
        {
          //no accounts
        }
        else
        {*/
          sendSignedEmail(recipients, title,
            contents.toString(), null);
        //}
      }
      ok = true;
    }
    catch (Exception ex)
    {
      String message = "Error in generateArborEmail(): " + ex.getMessage();
      System.out.println(message);
      writeToLogFile(message);
      sendSignedEmail(replyAddress, "Error in notification processing", message,
        null);
    }
    finally
    {
      closeLogFile();
      return ok;
    }
  }

  public boolean generateCompletionEmail(String source)
  {
    boolean ok = false;
    try
    {
      createLogFile();
      //send processing complete email
      if (!source.equals("none"))
      {
        Properties addrs = EBANProperties.getEBANProperties("notifyAddr");
        InternetAddress[] recipients = new InternetAddress[addrs.size()];
        Enumeration en = addrs.elements();
        int pos = 0;
        while (en.hasMoreElements())
        {
          String addr = (String)en.nextElement();
          recipients[pos] = new InternetAddress(addr);
          pos++;
        }
        sendSignedEmail(recipients, (source.equals("ebillz")?"Crystal":source) +
          " daily CDRs complete", (source.equals("ebillz")?"Crystal":source) +
          " daily CDR processing has completed", null);
      }
      ok = true;
    }
    catch (Exception ex)
    {
      String message = "Error in generateCompletionEmail(): " + ex.getMessage();
      System.out.println(message);
      writeToLogFile(message);
      sendSignedEmail(replyAddress, "Error in notification processing", message,
        null);
    }
    finally
    {
      closeLogFile();
      return ok;
    }
  }

  private String readLine(BufferedReader buff)
    throws java.io.IOException
  {
    String line = buff.readLine();
    if (line != null)
    {
      return line.trim();
    }
    else
    {
      return null;
    }
  }

  public boolean generateQueryEmail()
  {
    boolean ok = false;
    try
    {
      createLogFile();
      File inFiles = new File(EBANProperties.getEBANProperty("abqsDir"));
      File[] fileArray = inFiles.listFiles();
      String[] queryItems = new String[6];
      StringBuffer dets = null;
      StringBuffer mobDets = null;
      StringBuffer sup = null;
      if (fileArray != null)
      {
        for (int i = 0; i < fileArray.length; i++)
        {
          File queryFile = fileArray[i];
          //queryFile.
          BufferedReader buff = new BufferedReader(new FileReader(fileArray[i]));
          String line = readLine(buff);
//System.out.println("1:"+line+":");
          //if ((line != null) && (line.length() > 0))
          while (line != null)
          {
            if (line.equals("***start***"))
            {
              for (int j=0; j<6; j++)
              {
                line = readLine(buff);
//System.out.println("2:"+line);
                queryItems[j] = line;
              }
              line = readLine(buff);
//System.out.println("3:"+line);
              dets = new StringBuffer("<b>Query Details</b><br><hr>");
              mobDets = new StringBuffer("");
              while (!line.equals("endquerytext"))
              {
                dets.append(line + "<br>");
                mobDets.append(line.replace('\n', ' ').replace('\r', ' ').trim() + " ");
                line = readLine(buff);
//System.out.println("4:"+line);
              }
              line = line = readLine(buff);
//System.out.println("5:"+line);
              sup = new StringBuffer("<br><hr><b>Supplementary Information</b><br><hr>");
              while (!line.equals("endsupplementary"))
              {
                sup.append(line + "<br>");
                line = line = readLine(buff);
//System.out.println("6:"+line);
              }
              line = readLine(buff);
//System.out.println("7:"+line);
            }
            else if (line.equals("***end***"))
            {
              Properties addrs = EBANProperties.getEBANProperties(queryItems[2]+"QueryAddr");
              Properties mobAddrs = EBANProperties.getEBANProperties(queryItems[2]+"MobQueryAddr");
              InternetAddress[] recipients = new InternetAddress[addrs.size()];
              InternetAddress[] mobRecipients = new InternetAddress[mobAddrs.size()];
              Enumeration en = addrs.elements();
              int pos = 0;
              while (en.hasMoreElements())
              {
                String addr = (String)en.nextElement();
//System.out.println("addr:"+addr);
                recipients[pos] = new InternetAddress(addr);
                pos++;
              }
              String contents = "<hr><b>Automated Query No. " + queryItems[0] +
                "</b><hr>User Id: " + queryItems[1] + "<br>" +
                "Reply To: <a href=\"mailto:"  + queryItems[3] +
                "?subject=RE: Automated%20Query%20No.%20" + queryItems[0] + "\">" +
                queryItems[3] + "</a><br>" +
                "Global Customer Id: " + queryItems[4] + "<br>" +
                "Screen Id: " + queryItems[5] + "<br><br><hr>" + dets.toString() +
                sup.toString();
/*fromAddress = queryItems[3];
replyAddress = queryItems[3];
sendEmail(recipients, "Automated Query No. " + queryItems[0],
contents, null);*/
              sendSignedEmail(recipients, "Automated Query No. " + queryItems[0],
                contents, null, queryItems[3]);
              if (mobRecipients.length > 0)
              {
                en = mobAddrs.elements();
                pos = 0;
                while (en.hasMoreElements())
                {
                  String addr = (String)en.nextElement();
                  mobRecipients[pos] = new InternetAddress(addr);
                  pos++;
                }
                sendSignedEmail(mobRecipients, "Q" + queryItems[0] + ": " +
                  mobDets.toString(), contents, null);
              }
//System.out.println("contents:"+contents);
              line = readLine(buff);
//System.out.println("8:"+line);
            }
            else
            {
              line = readLine(buff);
//System.out.println("9:"+line+":");
            }
          }
        }
      }
      ok = true;
    }
    catch (Exception ex)
    {
      String message = "Error in generateQueryEmail(): " + ex.getMessage();
      System.out.println(message);
      writeToLogFile(message);
      sendSignedEmail(EBANProperties.getEBANProperty("DataQueryAddr1"),
        "Error in query processing", message, null);
    }
    finally
    {
      closeLogFile();
      return ok;
    }
  }

  public boolean generateMonitorEmail(String source)
  {
    boolean ok = false;
    try
    {
      createLogFile();
      //send processing complete email
      File inFiles = new File(EBANProperties.getEBANProperty(source+"Dir"));
      File[] fileArray = inFiles.listFiles();
      if (fileArray != null)
      {
        createBackupFile(source);
        String title = null;
        StringBuffer message = new StringBuffer();
        Properties addrs = EBANProperties.getEBANProperties(source+"Addr");
        InternetAddress[] recipients = new InternetAddress[addrs.size()];
        Enumeration en = addrs.elements();
        int pos = 0;
        while (en.hasMoreElements())
        {
          String addr = (String)en.nextElement();
          recipients[pos] = new InternetAddress(addr);
          pos++;
        }
        //for each file
        int problemCount = 0;
        for (int i = 0; i < fileArray.length; i++)
        {
          String fileName = fileArray[i].getName().substring(0,
            fileArray[i].getName().lastIndexOf("."));
          int dash1 = fileName.indexOf("-");
          int dash2 = fileName.lastIndexOf("-");
          String type = fileName.substring(dash1+1, dash2);
          String server = fileName.substring(dash2+1);
          if (type.equals("httpd"))
          {
            String msg = "There are no httpd/Apache processes currently running on " +
              server;
            message.append(msg + ".<br>");
            writeToBackupFile(msg);
            writeToLogFile(msg);
            problemCount++;
            title = msg;
          }
          else if (type.equals("ping"))
          {
            String msg = server + " is not responding to ping requests";
            message.append(msg + ".<br>");
            writeToBackupFile(msg);
            writeToLogFile(msg);
            problemCount++;
            title = msg;
          }
          else if (type.equals("iis"))
          {
            String msg = "IIS web server " + server + " is stopped";
            message.append(msg + ".<br>");
            writeToBackupFile(msg);
            writeToLogFile(msg);
            problemCount++;
            title = msg;
          }
          else if (type.equals("ftp"))
          {
            String msg = "FTP server " + server + " is stopped";
            message.append(msg + ".<br>");
            writeToBackupFile(msg);
            writeToLogFile(msg);
            problemCount++;
            title = msg;
          }
          else if (type.equals("data"))
          {
            String msg = "The SAN on " + server + " is not accessible";
            message.append(msg + ".<br>");
            writeToBackupFile(msg);
            writeToLogFile(msg);
            problemCount++;
            title = msg;
          }
          else if (type.equals("web"))
          {
            String msg = "A WebSphere node on " + server + " is not running";
            message.append(msg + ".<br>");
            writeToBackupFile(msg);
            writeToLogFile(msg);
            problemCount++;
            title = msg;
          }
          else if (type.equals("msg"))
          {
            String msg = "Messaging Direct service on " + server + " is not running";
            message.append(msg + ".<br>");
            writeToBackupFile(msg);
            writeToLogFile(msg);
            problemCount++;
            title = msg;
          }
          else if (type.equals("db"))
          {
            String msg = "The database on " + server + " is unavailable";
            message.append(msg + ".<br>");
            writeToBackupFile(msg);
            writeToLogFile(msg);
            problemCount++;
            title = msg;
          }
          else if (type.equals("edify"))
          {
            String msg = "Edify on " + server + " is down.";
            message.append(msg + ".<br>");
            writeToBackupFile(msg);
            writeToLogFile(msg);
            problemCount++;
            title = msg;
          }
          else if (type.equals("feed"))
          {
            String msg = "Edify " + server + " feed job has been running for more than 1 hour.";
            message.append(msg + ".<br>");
            writeToBackupFile(msg);
            writeToLogFile(msg);
            problemCount++;
            title = msg;
          }
          else if (type.equals("crystal"))
          {
            String msg = "Crystal on " + server + " is unavailable";
            message.append(msg + ".<br>");
            writeToBackupFile(msg);
            writeToLogFile(msg);
            problemCount++;
            title = msg;
          }
          else if (type.equals("print"))
          {
            String msg = "Error on printer " + server;
            message.append(msg + ".<br>");
            writeToBackupFile(msg);
            writeToLogFile(msg);
            problemCount++;
            title = msg;
          }
          else if (type.equals("print2"))
          {
            String msg = "Possible error on printer " + server;
            message.append(msg + ".<br>");
            writeToBackupFile(msg);
            writeToLogFile(msg);
            problemCount++;
            title = msg;
          }
          else if (type.equals("capacity"))
          {
            String msg = "Capacity critical on " + server;
            message.append(msg + ".<br>");
            writeToBackupFile(msg);
            writeToLogFile(msg);
            problemCount++;
            title = msg;
          }
          else if (type.equals("dataflow"))
          {
            String msg = server + " daily files delayed";
            message.append(msg + ".<br>");
            writeToBackupFile(msg);
            writeToLogFile(msg);
            problemCount++;
            title = msg;
          }
          else if (type.equals("ssbsattmult"))
          {
            String msg = "SSBS attachment processing - account " +
							server + " has multiple invoices";
            message.append(msg + ".<br>");
            writeToBackupFile(msg);
            writeToLogFile(msg);
            problemCount++;
            title = msg;
          }
          else if (type.equals("ssbsattinfail"))
          {
            String msg = "SSBS attachment processing - account " +
							server + " insert failed";
            message.append(msg + ".<br>");
            writeToBackupFile(msg);
            writeToLogFile(msg);
            problemCount++;
            title = msg;
          }
          else if (type.equals("readfromfile"))
          {
            BufferedReader buff = new BufferedReader(new FileReader(fileArray[i]));
            String line = buff.readLine();
            Vector fileAddr = new Vector();
            String attachment = null;
            StringBuffer thisMsg = new StringBuffer();
            while ((line != null) && (line.length() > 0))
            {
              if (line.startsWith("addr:"))
              {
                fileAddr.add(line.substring(line.indexOf(":")+1));
              }
              else if (line.startsWith("title:"))
              {
                title = line.substring(line.indexOf(":")+1);
              }
              else if (line.startsWith("attachment:"))
              {
                attachment = line.substring(line.indexOf(":")+1);
              }
              else if (line.startsWith("message:"))
              {
                thisMsg.append(line.substring(line.indexOf(":")+1)+"<br>");
              }
              line = buff.readLine();
            }
            buff.close();
            recipients = new InternetAddress[fileAddr.size()];
            en = fileAddr.elements();
            pos = 0;
            while (en.hasMoreElements())
            {
              String addr = (String)en.nextElement();
              recipients[pos] = new InternetAddress(addr);
              pos++;
            }
            writeToBackupFile(message.toString());
            writeToLogFile(message.toString());
            //problemCount++;
            sendSignedEmail(recipients, title, thisMsg.toString(), attachment);
          }
          else
          {
            String msg = "Unknown problem on " + server + " - " +
              fileArray[i].getName();
            message.append(msg + ".<br>");
            writeToBackupFile(msg);
            writeToLogFile(msg);
            problemCount++;
            title = msg;
          }
          fileArray[i].delete();
        }
        closeBackupFile();
        if (problemCount > 1)
        {
          title = "multiple " + (source.equals("monitor")?"e-billing problem alert":
            "gcb problem alert");
        }
        if (problemCount > 0)
        {
          sendSignedEmail(recipients, title, message.toString(), null);
        }
      }
      ok = true;
    }
    catch (Exception ex)
    {
      String msg = "Error in generateMonitorEmail(): " + ex.getMessage();
      System.out.println(msg);
      writeToLogFile(msg);
      sendSignedEmail(replyAddress, "Error in notification processing", msg,
        null);
    }
    finally
    {
      closeLogFile();
      return ok;
    }
  }

  private void sendPDFEmail(EmailRequestDescriptor erd)
  {
//writeToLogFile("sendPDFEmail()");
    String subject = null;
    String content = null;
    String link = href + "?fromEbill=y&type=p&invoiceId=" +
      Long.toString(erd.getInvoiceId()) + "&accountId=" +
      Long.toString(erd.getAccountId()) +
      (erd.getPaymentGroupId()!=0?("&payGroupId=" +
      Long.toString(erd.getPaymentGroupId())):("&infoGroupId=" +
      Long.toString(erd.getInformationGroupId()))) + "&invoiceNo=" +
      erd.getInvoiceNo();
    //String fileAttachment = attachmentPath + File.separator +
      //erd.getAttachmentLocationAsFilePath();
    subject = "Invoice " + erd.getInvoiceNo() + " (account " +
      erd.getAccountDetails() + ") ";
    StringBuffer sb1 = new StringBuffer("<link rel=\"stylesheet\" type=\"text/css\" href=\"" + styleSheet +"\">" +
      "<table><tr><td><img src=\""+cwImage+"\"></td><td>"+
      "<img src=\""+ebImage+"\"></td></tr><tr><td colspan=\"2\">&nbsp;</td></tr>");

    for (int i=0; i<pdfMessageKeys.length; i++)
    {
      String msgLine = pdfMessages.getProperty((String)pdfMessageKeys[i]);
//System.out.println("message: " + msgLine);
        sb1.append("<tr><td colspan=\"2\">" + msgLine + "</td></tr>");
    }
    sb1.append("<tr><td colspan=\"2\">&nbsp;</td></tr><tr><td colspan=\"2\">" +
//"A PDF document giving summary details for invoice " + erd.getInvoiceNo() +
//" is attached.  If you wish to view further information about this " +
    "Details for invoice " + erd.getInvoiceNo() +
    " are attached.  If you wish to view further information about this " +
    "invoice online, please click <a href=\""+link+"\">here</a> to log on to the "+
    "Cable&amp;Wireless Worldwide e-billing service.</td></tr></table>");


//writeToLogFile("subject:"+subject);
//writeToLogFile("link:"+link);
//writeToLogFile("fileAttachment:"+fileAttachment);
    sendSignedEmail(erd, subject, sb1.toString());
  }

  private void sendNotificationEmail(EmailRequestDescriptor erd,
    long previousLoginId, boolean last, boolean lastAlone)
  {
    String content = null;
    if (!last)
    {
      if ((erd.getLoginId() != previousLoginId) && (previousLoginId != 0))
      {
        sb.append("</table>");
        sendSignedEmail(storedEmailAddress,
          "Notification email from Cable&Wireless Worldwide e-billing", sb.toString(), null);
        sb = new StringBuffer("<link rel=\"stylesheet\" type=\"text/css\" href=\"" +
          styleSheet +"\"><table><tr><td><img src=\"" +
          cwImage + "\"></td><td><img src=\"" + ebImage + "\"></td></tr>" +
          "<tr><td colspan=\"2\">&nbsp;</td></tr>");
        for (int i=0; i<notifyMessageKeys.length; i++)
        {
          String msgLine = notifyMessages.getProperty((String)notifyMessageKeys[i]);
//System.out.println("message: " + msgLine);
            sb.append("<tr><td colspan=\"2\">" + msgLine + "</td></tr>");
        }
        sb.append("<tr><td colspan=\"2\">&nbsp;</td></tr>");
      }
//      if ((erd.getLoginId() == previousLoginId) || (previousLoginId == 0))
//      {
        storedEmailAddress = erd.getEmailAddress();
        if (erd.getNotificationType().equals(erd.NOTIFICATION_TYPE_P))
        {
          String link = href + "?fromEbill=y&type=pn&invoiceId=" +
            Long.toString(erd.getInvoiceId()) + "&accountId=" +
            Long.toString(erd.getAccountId()) + (erd.getPaymentGroupId()!=0?("&payGroupId=" +
            Long.toString(erd.getPaymentGroupId())):("&infoGroupId=" +
            Long.toString(erd.getInformationGroupId()))) + "&invoiceNo=" +
            erd.getInvoiceNo();
          sb.append("<tr><td colspan=\"2\">"+
            "Invoice " + erd.getInvoiceNo() +
            " is now available.  If you wish to view this " +
            "invoice online, please click <a href=\""+link+"\">here</a>.</td></tr>");
        }
        else if (erd.getNotificationType().equals(erd.NOTIFICATION_TYPE_M))
        {
          String link = href + "?fromEbill=y&type=m&invoiceId=" +
            Long.toString(erd.getInvoiceId()) + "&accountId=" +
            Long.toString(erd.getAccountId()) + (erd.getPaymentGroupId()!=0?("&payGroupId=" +
            Long.toString(erd.getPaymentGroupId())):("&infoGroupId=" +
            Long.toString(erd.getInformationGroupId()))) + "&invoiceNo=" +
            erd.getInvoiceNo();
          sb.append("<tr><td colspan=\"2\">"+
            "Monthly CDRs for invoice " + erd.getInvoiceNo() +
            " are now available.  If you wish to view this " +
            "invoice online, please click <a href=\""+link+"\">here</a>.</td></tr>");
        }
        else if (erd.getNotificationType().equals(erd.NOTIFICATION_TYPE_D))
        {
          String link = href + "?fromEbill=y&type=d&invoiceId=0&accountId=" +
            Long.toString(erd.getAccountId()) + (erd.getPaymentGroupId()!=0?("&payGroupId=" +
            Long.toString(erd.getPaymentGroupId())):("&infoGroupId=" +
            Long.toString(erd.getInformationGroupId())));// + "&invoiceNo=" +
            //erd.getInvoiceNo();
          sb.append("<tr><td colspan=\"2\">"+
            "Daily CDRs for account " + erd.getAccountDetails() +
            " are now available.  If you wish to view this " +
            "account online, please click <a href=\""+link+"\">here</a>.</td></tr>");
        }
/*      }
      else
      {
        sb.append("</table>");
        sendSignedEmail(storedEmailAddress,
          "Notification email from Cable&Wireless Worldwide e-billing", sb.toString(), null);
        sb = new StringBuffer("<link rel=\"stylesheet\" type=\"text/css\" href=\"" +
          styleSheet +"\"><table><tr><td><img src=\"" +
          cwImage + "\"></td><td><img src=\"" + ebImage + "\"></td></tr>" +
          "<tr><td colspan=\"2\">&nbsp;</td></tr>");
        for (int i=0; i<notifyMessageKeys.length; i++)
        {
          String msgLine = notifyMessages.getProperty((String)notifyMessageKeys[i]);
//System.out.println("message: " + msgLine);
            sb.append("<tr><td colspan=\"2\">" + msgLine + "</td></tr>");
        }
        sb.append("<tr><td colspan=\"2\">&nbsp;</td></tr>");
      }*/
    }
    else
    {
      if (lastAlone)
      {
        if (erd.getNotificationType().equals(erd.NOTIFICATION_TYPE_P))
        {
          String link = href + "?fromEbill=y&type=pn&invoiceId=" +
            Long.toString(erd.getInvoiceId()) + "&accountId=" +
            Long.toString(erd.getAccountId()) + (erd.getPaymentGroupId()!=0?("&payGroupId=" +
            Long.toString(erd.getPaymentGroupId())):("&infoGroupId=" +
            Long.toString(erd.getInformationGroupId()))) + "&invoiceNo=" +
            erd.getInvoiceNo();
          sb.append("<tr><td colspan=\"2\">"+
            "Invoice " + erd.getInvoiceNo() +
            " is now available.  If you wish to view this " +
            "invoice online, please click <a href=\""+link+"\">here</a>.</td></tr>");
        }
        else if (erd.getNotificationType().equals(erd.NOTIFICATION_TYPE_M))
        {
          String link = href + "?fromEbill=y&type=m&invoiceId=" +
            Long.toString(erd.getInvoiceId()) + "&accountId=" +
            Long.toString(erd.getAccountId()) + (erd.getPaymentGroupId()!=0?("&payGroupId=" +
            Long.toString(erd.getPaymentGroupId())):("&infoGroupId=" +
            Long.toString(erd.getInformationGroupId()))) + "&invoiceNo=" +
            erd.getInvoiceNo();
          sb.append("<tr><td colspan=\"2\">"+
            "Monthly CDRs for invoice " + erd.getInvoiceNo() +
            " are now available.  If you wish to view this " +
            "invoice online, please click <a href=\""+link+"\">here</a>.</td></tr>");
        }
        else if (erd.getNotificationType().equals(erd.NOTIFICATION_TYPE_D))
        {
          String link = href + "?fromEbill=y&type=d&invoiceId=0&accountId=" +
            Long.toString(erd.getAccountId()) + (erd.getPaymentGroupId()!=0?("&payGroupId=" +
            Long.toString(erd.getPaymentGroupId())):("&infoGroupId=" +
            Long.toString(erd.getInformationGroupId())));// + "&invoiceNo=" +
            //erd.getInvoiceNo();
          sb.append("<tr><td colspan=\"2\">"+
            "Daily CDRs for account " + erd.getAccountDetails() +
            " are now available.  If you wish to view this " +
            "account online, please click <a href=\""+link+"\">here</a>.</td></tr>");
        }
      }
      sb.append("</table>");
      sendSignedEmail(erd.getEmailAddress(),
        "Notification email from Cable&Wireless Worldwide e-billing", sb.toString(), null);
//System.out.println("send: " + sb.toString());
    }
  }

  public void sendSignedEmail(String toAddress, String subject,
    String content, String fileAttachment)
  {
//writeToLogFile("sendSignedEmail 1");
    try
    {
      InternetAddress recipients[] = new InternetAddress[ 1 ];
      recipients[ 0 ] = new InternetAddress( toAddress );
      sendSignedEmail(recipients, subject, content, fileAttachment);
    }
    catch(Exception ex)
    {
            ex.printStackTrace();
    }
  }

  private void sendSignedEmail(InternetAddress[] recipients, String subject,
    String content, String fileAttachment, String replyAddressOverride)
  {
    replyAddress = replyAddressOverride;
    sendSignedEmail(recipients, subject, content, fileAttachment);
    replyAddress = EBANProperties.getEBANProperty("replyToAddress");
  }

  private void sendSignedEmail(InternetAddress[] recipients, String subject,
    String content, String fileAttachment)
  {
//writeToLogFile("sendSignedEmail 2");
    try
    {

      // Set up Java for S/MIME - adds Mailcap entries and registers
      // the ISNetworks JCE provider
      SMIMEUtils.configureSMIME();

//writeToLogFile( "Loading the KeyStore and grabbing the X509Certificate and PrivateKey... " );
      KeyStore keyStore = KeyStore.getInstance( "JKS" );
      FileInputStream fIn = new FileInputStream(securityPath + File.separator +
        "verisign-cw.ks" );
      keyStore.load( fIn, "danet".toCharArray() );
      PrivateKey privateKey = (PrivateKey)keyStore.getKey( "default", "danet".toCharArray() );
      X509Certificate certificate = (X509Certificate)keyStore.getCertificate( "default" );

//writeToLogFile( "Initializing the JavaMail session for SMTP... " );
      Properties sessionProperties = new Properties();
      sessionProperties.setProperty( "mail.smtp.host", mailHost );
      Session session = Session.getInstance( sessionProperties, null );

//writeToLogFile( "Creating the message and setting some basic information... " );

      MimeMessage message = new MimeMessage( session );
      InternetAddress from = new InternetAddress(fromAddress);
      message.setFrom( from );
      InternetAddress replyTo[] = new InternetAddress[ 1 ];
      replyTo[ 0 ] = new InternetAddress(replyAddress);
      //message.setReplyTo(replyTo);
      //InternetAddress recipients[] = new InternetAddress[ 1 ];
      //recipients[ 0 ] = new InternetAddress( toAddress );
      message.addRecipients( Message.RecipientType.TO, recipients );
      message.setSubject( subject );
      message.setSentDate(new java.util.Date());
      message.setReplyTo(replyTo);
//message.addHeader("Return-Path", replyAddress);
//System.out.println(fromAddress);
//message.removeHeader("Return-Path");
      if (fileAttachment == null)
      {
//writeToLogFile( "Creating the multipart/signed and set it to be the message's content... " );
        MimeBodyPart bodyPart = new MimeBodyPart();
        bodyPart.setContent(content, "text/html");
        SignedMultipart signedMultipart = new SignedMultipart( bodyPart );
        signedMultipart.addSigner( privateKey, certificate, "SHA1withRSA" );
        message.setContent( signedMultipart );
//writeToLogFile( "done." );
      }
      else
      {
//writeToLogFile( "Creating the multipart/signed and set it to be the message's content... " );
        BodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setContent(content, "text/html");
        MimeMultipart multipart = new MimeMultipart("related");
        multipart.addBodyPart(messageBodyPart);

        messageBodyPart = new MimeBodyPart();
        DataSource source = new FileDataSource(fileAttachment);
        messageBodyPart.setDataHandler(new DataHandler(source));
        messageBodyPart.setFileName(fileAttachment.substring(
          fileAttachment.lastIndexOf(File.separator)+1));
        multipart.addBodyPart(messageBodyPart);
        SignedMultipart signedMultipart = new SignedMultipart( multipart );
        signedMultipart.addSigner( privateKey, certificate, "SHA1withRSA" );
        message.setContent( signedMultipart );
      }

//writeToLogFile( "Sending the message... " );
/*Enumeration en = message.getAllHeaders();
while (en.hasMoreElements())
{
  Header h = (Header)en.nextElement();
  System.out.println(h.getName() + ": " +h.getValue());
}*/
      Transport.send( message );
    }
    catch(Exception ex)
    {
			writeToLogFile( "Exception sending the message: " + ex.getMessage() );
            ex.printStackTrace();
    }
  }

  private void sendSignedEmail(EmailRequestDescriptor erd, String subject,
    String content)
  {
//writeToLogFile("sendSignedEmail 2");
    String zipName = null;
    try
    {
      InternetAddress recipients[] = new InternetAddress[ 1 ];
      recipients[ 0 ] = new InternetAddress(erd.getEmailAddress());
      // Set up Java for S/MIME - adds Mailcap entries and registers
      // the ISNetworks JCE provider
      SMIMEUtils.configureSMIME();

//writeToLogFile( "Loading the KeyStore and grabbing the X509Certificate and PrivateKey... " );
      KeyStore keyStore = KeyStore.getInstance( "JKS" );
      FileInputStream fIn = new FileInputStream(securityPath + File.separator +
        "verisign-cw.ks" );
      keyStore.load( fIn, "danet".toCharArray() );
      PrivateKey privateKey = (PrivateKey)keyStore.getKey( "default", "danet".toCharArray() );
      X509Certificate certificate = (X509Certificate)keyStore.getCertificate( "default" );

//writeToLogFile( "Initializing the JavaMail session for SMTP... " );
      Properties sessionProperties = new Properties();
      sessionProperties.setProperty( "mail.smtp.host", mailHost );
      Session session = Session.getInstance( sessionProperties, null );

//writeToLogFile( "Creating the message and setting some basic information... " );

      MimeMessage message = new MimeMessage( session );
      InternetAddress from = new InternetAddress(fromAddress);
      message.setFrom( from );
      InternetAddress replyTo[] = new InternetAddress[ 1 ];
      replyTo[ 0 ] = new InternetAddress(replyAddress);
      //message.setReplyTo(replyTo);
      //InternetAddress recipients[] = new InternetAddress[ 1 ];
      //recipients[ 0 ] = new InternetAddress( toAddress );
      message.addRecipients( Message.RecipientType.TO, recipients );
      message.setSubject( subject );
      message.setSentDate(new java.util.Date());
      message.setReplyTo(replyTo);
//message.addHeader("Return-Path", replyAddress);
//System.out.println(replyAddress);
message.removeHeader("Return-Path");
      if (erd.getAttachmentLocationArray().isEmpty())
      {
//writeToLogFile( "Creating the multipart/signed and set it to be the message's content... " );
        MimeBodyPart bodyPart = new MimeBodyPart();
        bodyPart.setContent(content, "text/html");
        SignedMultipart signedMultipart = new SignedMultipart( bodyPart );
        signedMultipart.addSigner( privateKey, certificate, "SHA1withRSA" );
        message.setContent( signedMultipart );
//writeToLogFile( "done." );
      }
      else
      {
//writeToLogFile( "Creating the multipart/signed and set it to be the message's content... " );
        BodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setContent(content, "text/html");
        MimeMultipart multipart = new MimeMultipart("related");
        multipart.addBodyPart(messageBodyPart);

        if (erd.getAttachmentLocationArray().size() > 1)
        {
          zipName = erd.getInvoiceNo().replace('/', '-') +".zip";
          zipFile(erd.getAttachmentLocationArray(), zipName);
          //String fileAttachment = attachmentPath + File.separator + zipName;
          messageBodyPart = new MimeBodyPart();
          DataSource source = new FileDataSource(zipName);
          messageBodyPart.setDataHandler(new DataHandler(source));
          messageBodyPart.setFileName(zipName);
          multipart.addBodyPart(messageBodyPart);
          //File z = new File(zipName);
          //z.delete();
        }
        else
        {
          String fileAttachment = attachmentPath + File.separator +
            (String)erd.getAttachmentLocationArray().get(0);
          messageBodyPart = new MimeBodyPart();
          DataSource source = new FileDataSource(fileAttachment);
          messageBodyPart.setDataHandler(new DataHandler(source));
          messageBodyPart.setFileName(fileAttachment.substring(
            fileAttachment.lastIndexOf(File.separator)+1));
          multipart.addBodyPart(messageBodyPart);
        }
        SignedMultipart signedMultipart = new SignedMultipart( multipart );
        signedMultipart.addSigner( privateKey, certificate, "SHA1withRSA" );
        message.setContent( signedMultipart );
      }

//writeToLogFile( "Sending the message... " );
/*Enumeration en = message.getAllHeaders();
while (en.hasMoreElements())
{
  Header h = (Header)en.nextElement();
  System.out.println(h.getName() + ": " +h.getValue());
}*/
      Transport.send( message );
    }
    catch(Exception ex)
    {
			writeToLogFile( "Exception sending the message: " + ex.getMessage() );
            ex.printStackTrace();
    }
    finally
    {
      try
      {
        if (zipName != null)
        {
          File z = new File(zipName);
          z.delete();
        }
      }
      catch (Exception ex)
      {
        writeToLogFile( "Problem deleting file " + zipName + ": " + ex.getMessage() );
      }

    }
  }

  public void sendSignedOpaqueEnvelopedEmail2(String toAddress, String subject, String content,
    String fileAttachment, String cert)
  {
    try
    {
      // Set up Java for S/MIME - adds Mailcap entries and registers
      // the ISNetworks JCE provider
      SMIMEUtils.configureSMIME();

      System.out.print( "Loading the KeyStore and grabbing the X509Certificates and PrivateKey... " );
      //KeyStore keyStore = KeyStore.getInstance( "JKS" );
      KeyStore keyStore2 = KeyStore.getInstance( "JKS" );
      //FileInputStream fIn = new FileInputStream(securityPath + File.separator +
        //"example.ks");
      FileInputStream fIn2 = new FileInputStream(securityPath + File.separator +
        "verisign-cw.ks" );
        //"my.keystore");
      //keyStore.load( fIn, "password".toCharArray() );
      //keyStore2.load( fIn2, "danet01".toCharArray() );
      keyStore2.load( fIn2, "danet".toCharArray() );
      //X509Certificate senderCertificate = (X509Certificate)keyStore.getCertificate( "sender" );
      X509Certificate senderCertificate = (X509Certificate)keyStore2.getCertificate( "default" );
      //X509Certificate senderCertificate = (X509Certificate)keyStore2.getCertificate( "myalias" );
      //X509Certificate caCertificate = (X509Certificate)keyStore.getCertificate( "rootca" );
      //PrivateKey senderPrivateKey = (PrivateKey)keyStore.getKey( "sender", "password".toCharArray() );
      //PrivateKey senderPrivateKey = (PrivateKey)keyStore2.getKey( "myalias", "danet01".toCharArray() );
      PrivateKey senderPrivateKey = (PrivateKey)keyStore2.getKey(  "default", "danet".toCharArray() );
      System.out.println( "done." );


      System.out.print( "Loading the recipient X509Certificate from the file system... " );
      //System.out.println( "securityPath: " + securityPath + File.separator + cert + ".crt" );
      CertificateFactory cf = CertificateFactory.getInstance( "X.509" );
      X509Certificate recipientCertificate =
        (X509Certificate)cf.generateCertificate( new FileInputStream(securityPath +
        //File.separator + "myalias.cer" ) );
        //File.separator + "recipient.cer" ) );
        File.separator + cert + ".crt" ) );
      System.out.println( "done." );


      System.out.print( "Initializing the JavaMail session for SMTP... " );
      Properties sessionProperties = new Properties();
      sessionProperties.setProperty( "mail.smtp.host", mailHost );

      String dsn = "SUCCESS,FAILURE,DELAY ORCPT=rfc822;" + "tim.aitchison@danet-uk.com";
      sessionProperties.setProperty("mail.smtp.dsn.notify", dsn);
      sessionProperties.setProperty("mail.smtp.dsn.ret", "HDRS");



      Session session = Session.getInstance( sessionProperties, null );
      System.out.println( "done." );

      System.out.print( "Creating the message and setting some basic information... " );
      MimeMessage message = new MimeMessage( session );


      message.setHeader("Disposition-Notification-To","tim.aitchison@danet-uk.com");
      message.setHeader("Return-Receipt-To","tim.aitchison@danet-uk.com");


      InternetAddress from = new InternetAddress( "admin@e-billing.cw.com" );
      message.setFrom( from );
      InternetAddress recipients[] = new InternetAddress[ 1 ];
      recipients[ 0 ] = new InternetAddress( toAddress );
      message.addRecipients( Message.RecipientType.TO, recipients );
      message.setSubject( cert + " Sample S/MIME signed (opaque) and enveloped message" );
      message.setSentDate(new java.util.Date());
      System.out.println( "done." );


      System.out.print( "Creating the SignedBodyPart and setting the message's content... " );
/*      MimeBodyPart bodyPart = new MimeBodyPart();
      bodyPart.setContent( "Test signed (opaque) and enveloped message content", "text/plain" );
      SignedBodyPart signedBodyPart = new SignedBodyPart( bodyPart );
      signedBodyPart.addSigner( senderPrivateKey, senderCertificate, "SHA1withRSA" );
      signedBodyPart.addCertificate( caCertificate );*/

      BodyPart messageBodyPart = new MimeBodyPart();
      messageBodyPart.setContent("test with attachment", "text/html");
      MimeMultipart multipart = new MimeMultipart("related");
      multipart.addBodyPart(messageBodyPart);

      // Part two is attachment
      if (fileAttachment != null)
      {
        messageBodyPart = new MimeBodyPart();
        DataSource source = new FileDataSource(fileAttachment);
        messageBodyPart.setDataHandler(new DataHandler(source));
        messageBodyPart.setFileName(fileAttachment.substring(
          fileAttachment.lastIndexOf("\\")+1));
        multipart.addBodyPart(messageBodyPart);
      }
      SignedMultipart signedMultiPart = new SignedMultipart( multipart );
      signedMultiPart.addSigner( senderPrivateKey, senderCertificate, "SHA1withRSA" );
      signedMultiPart.addCertificate( senderCertificate );


      System.out.println( "done." );


      System.out.print( "Creating the EnvelopedBodyPart around the SignedBodyPart... " );
      EnvelopedBodyPart envelopedPart = new EnvelopedBodyPart( signedMultiPart );
      // Set to use RC2, 40 bit encryption so that weak crypto clients can read them
      envelopedPart.addRecipient( recipientCertificate, new Capabilities( false, false, false, false, true ) );
      envelopedPart.addRecipient( senderCertificate, new Capabilities( false, false, false, false, true ) );
      message.setDataHandler( envelopedPart.getDataHandler() );
      message.setDisposition( "attachment; filename=smime.p7m" );
      message.setDescription( "S/MIME Encrypted Message" );
      System.out.println( "done." );


      System.out.print( "Sending the message... " );
      Transport.send( message );
      System.out.println( "done." );


      // Decrypt the enveloped message and verify the signature.  Normally
      // the message would be retrieved from a mailbox but for simplicity
      // we just decrypt and verify the message we already have
/*      EnvelopedBodyPart testEnvelopedBodyPart = (EnvelopedBodyPart)message.getContent();
      SignedBodyPart verificationSignedBodyPart = (SignedBodyPart)testEnvelopedBodyPart.getUnencryptedContent( senderCertificate, senderPrivateKey );;
      System.out.println( "Message content: " + verificationSignedBodyPart.getContent() );
      if ( verificationSignedBodyPart.verify( senderCertificate ) ) {
        System.out.println( "Signature verified" );
      }
      else {
        System.out.println( "Invalid signature" );
      }*/
    }
    catch(Exception ex)
    {
            ex.printStackTrace();
    }
  }

  public void getMail()
  {
    try
    {
      // Get system properties
/*      Properties props = System.getProperties();
      props.put("mail.imap.host", mailHost);

      // Setup authentication, get session
      Authenticator auth = new MailAuthenticator();

      Session session =
        Session.getDefaultInstance(props, auth);*/
      StringUtil su = new StringUtil();
      Session session = Session.getInstance(new Properties(), null);
//session.setDebug(true);
      // Get the store
      Store store = session.getStore("imap");
System.out.println("Get the store" );
      //store.connect();
      //store.connect(mailHost, "GCBAdmin", "L1kewhatever");
      store.connect(mailHost, "tim.aitchison", "Nineteen76");

      // Get folder
System.out.println("Get folder" );
      Folder folder = store.getFolder("INBOX");
      Folder destFolder = store.getFolder("test");
      //folder.open(Folder.READ_ONLY);
      folder.open(Folder.READ_WRITE);
      destFolder.open(Folder.READ_WRITE);
      ArrayList msgMove = new ArrayList();


      // Get directory
      Message message[] = folder.getMessages();
      Message doneMessage[] = destFolder.getMessages();
      //ArrayList msgDone = new ArrayList(
      for (int i=0, n=message.length; i<n; i++) {
//Ian Watson [i.watson@telis.co.uk]
         String sender = (message[i].getFrom()[0]).toString();
         String subject = message[i].getSubject();
//System.out.println("sender: " + sender );
         if ((sender.indexOf("admin@e-billing.cw.com") >= 0) && subject.startsWith("SSBS"))
         {
           boolean done = false;
           for (int j=0, o=doneMessage.length; j<o; j++)
           {
              if (doneMessage[j].getSubject().equals(subject))
              {
                done = true;
                break;
              }
           }
           if (!done)
           {
             //String subject = message[i].getSubject();
             //System.out.println(subject.substring(subject.indexOf("No. ") + 4));
             System.out.println(subject);
             //System.out.println(message[i].getReplyTo());
             /*long msgTime = message[i].getReceivedDate().getTime();
             DateTime dt = new DateTime(msgTime);
             dt.
             System.out.println(su.DateToString(message[i].getReceivedDate(),"yyyyMMddHHmmss"));
             System.out.println(message[i].getReceivedDate().getTime());
             System.out.println((new Date()).getTime());
             System.out.println((new Date()).getTime() - message[i].getReceivedDate().getTime());
             long oneHour = 3600000;
             if (((new Date()).getTime() - message[i].getReceivedDate().getTime()) > oneHour)
             {
                System.out.println(" > 1 hour");
             }*/

             MimeMultipart mmp = (MimeMultipart)message[i].getContent();
             BodyPart bp = mmp.getBodyPart(0);
             String content = bp.getContent().toString();
             //String headers[] = message[i].getHeader("Reply-To");
             //System.out.println(headers[0].substring(headers[0].indexOf("<")+1,headers[0].indexOf(">")));
             String replyTo  = message[i].getHeader("Reply-To")[0];
             System.out.println(replyTo);


             //System.out.println(content.substring(content.indexOf("mailto") + 7,content.indexOf("?")));
             /*String content =
               message[i].getContent().toString();
             if (content.length() > 200) {
               content = content.substring(0, 200);
             }
             System.out.println("start:"+content+":end");*/

             //msgMove.add(message[i]);
             //message[i].setFlag(Flags.Flag.DELETED, true);
           }
         }
      }
      //Message[] msgs = (Message[])msgMove.toArray();
      Message[] msgs = new Message[msgMove.size()];
      for (int i = 0; i < msgMove.size(); i++)
      {
        msgs[i] = (Message)msgMove.get(i);
      }
      folder.copyMessages(msgs, destFolder);
      // Close connection
      //folder.close(false);
      folder.close(true);
      destFolder.close(true);
      store.close();
      System.exit(0);
    }
    catch (Exception ex)
    {
      System.out.println("Error: " + ex.getMessage());
      ex.printStackTrace();
    }
  }

  public void deleteAutoresponse()
  {
    try
    {
      StringUtil su = new StringUtil();
      Session session = Session.getInstance(new Properties(), null);
      Store store = session.getStore("imap");
      //store.connect();
      //store.connect(mailHost, "GCBAdmin", "L1kewhatever");
      store.connect(mailHost, "tim.aitchison", "Nineteen76");

      // Get folder
      Folder folder = store.getFolder("test");
      Folder destFolder = store.getFolder("INBOX");
      //folder.open(Folder.READ_ONLY);
      folder.open(Folder.READ_WRITE);
      destFolder.open(Folder.READ_WRITE);
      ArrayList msgMove = new ArrayList();


      // Get directory
      Message message[] = folder.getMessages();
      Message doneMessage[] = destFolder.getMessages();
      for (int i=0, n=message.length; i<n; i++) {
        String sender = (message[i].getFrom()[0]).toString();
        String subject = message[i].getSubject();
        if ((sender.indexOf("admin@e-billing.cw.com") >= 0) && subject.startsWith("SSBS"))
        {
          boolean found = false;
          for (int j=0, o=doneMessage.length; j<o; j++)
          {
            if (doneMessage[j].getSubject().equals(subject))
            {
              found = true;
              break;
            }
          }
          if (!found)
          {
            message[i].setFlag(Flags.Flag.DELETED, true);
          }
        }
      }
      folder.close(true);
      destFolder.close(true);
      store.close();
      System.exit(0);
    }
    catch (Exception ex)
    {
      System.out.println("Error: " + ex.getMessage());
      ex.printStackTrace();
    }
  }

  public void sendAutoresponse()
  {
    try
    {
      long oneHour = 3600000;
      StringUtil su = new StringUtil();
      Session session = Session.getInstance(new Properties(), null);
      // Get the store
      Store store = session.getStore("imap");
      store.connect(mailHost, "GCBAdmin", "L1kewhatever");
      //store.connect(mailHost, "tim.aitchison", "Nineteen77");

      // Get folder
      Folder folder = store.getFolder("INBOX");
      //Folder destFolder = store.getFolder("test");
      //Folder folder = store.getFolder("test");
      folder.open(Folder.READ_WRITE);


      // Get directory
      Message message[] = folder.getMessages();
      for (int i=0, n=message.length; i<n; i++)
      {
        String sender = (message[i].getFrom()[0]).toString();
        String subject = message[i].getSubject();
/*System.out.println(subject);
if (message[i].isSet(Flags.Flag.ANSWERED))
{
  System.out.println("answered");
}
else
{
  System.out.println("unanswered");
}
System.out.println((new Date(message[i].getReceivedDate().getTime()).toString()));*/
        if ((sender.indexOf("admin@e-billing.cw.com") >= 0) &&
          (subject.startsWith("Automated Query No.")) &&
          (!message[i].isSet(Flags.Flag.ANSWERED)) &&
          (((new Date()).getTime() - message[i].getReceivedDate().getTime()) > oneHour))
        {
//System.out.println("in there!");
          String headers[] = message[i].getHeader("Reply-To");
          //String replyTo = headers[0].substring(headers[0].indexOf("<")+1,headers[0].indexOf(">"));
          String replyTo = headers[0];
          sendEmailNoAtt(replyTo, "Re: " + subject, "Your query has been received " +
            "and will be dealt with as soon as possible.");
          message[i].setFlag(Flags.Flag.ANSWERED, true);
        }
      }
      folder.close(true);
      store.close();
      System.exit(0);
    }
    catch (Exception ex)
    {
      System.out.println("Error: " + ex.getMessage());
      ex.printStackTrace();
    }
  }

  public void sendTestEmail(String toAddress, String subject, String content)
  {
    sendEmail(toAddress, subject, content, null);
  }

  public void generateBulkInvoiceEmail(String toAddress, String accountNo)
  {
    String subject = "Invoices for account " + accountNo;
    String content = "The attached zip file contains the invoices you " +
      "requested for account " + accountNo;
    String fileAttachment = bulkInvoicePath + File.separator + toAddress +
      File.separator + accountNo + ".zip";
    sendEmail(toAddress, subject, content, fileAttachment);
  }

  private void sendEmail(String toAddress, String subject, String content,
    String fileAttachment)
  {
    try
    {
      // Get system properties
      Properties props = System.getProperties();
      // Setup mail server
      props.put("mail.smtp.host", mailHost);
      // Get session
      Session session = Session.getInstance(props, null);
      Message message = new MimeMessage(session);
      //message.setFrom(new InternetAddress("GCB Admin <GCBAdmin@danet-uk.com>"));
      message.setFrom(new InternetAddress(fromAddress));
      InternetAddress[] recips = {new InternetAddress(toAddress)};
      message.setRecipients(Message.RecipientType.TO,
        recips);
      InternetAddress replyTo[] = new InternetAddress[ 1 ];
      //replyTo[ 0 ] = new InternetAddress( "GCB Admin <GCBAdmin@danet-uk.com>" );
      replyTo[ 0 ] = new InternetAddress(replyAddress);
      message.setReplyTo(replyTo);
      message.setSubject(subject);
      message.setSentDate(new java.util.Date());
      BodyPart messageBodyPart = new MimeBodyPart();
      messageBodyPart.setContent(content, "text/html");
      Multipart multipart = new MimeMultipart("related");
      multipart.addBodyPart(messageBodyPart);

      // Part two is attachment
      if (fileAttachment != null)
      {
        messageBodyPart = new MimeBodyPart();
        DataSource source = new FileDataSource(fileAttachment);
        messageBodyPart.setDataHandler(new DataHandler(source));
        messageBodyPart.setFileName(fileAttachment.substring(
          fileAttachment.lastIndexOf("\\")+1));
        multipart.addBodyPart(messageBodyPart);
      }


      message.setContent(multipart);
      Transport.send(message);
    }
    catch(Exception ex)
    {
            ex.printStackTrace();
    }
  }

  private void sendEmail(InternetAddress[] recips, String subject, String content,
    String fileAttachment)
  {
    try
    {
      // Get system properties
      Properties props = System.getProperties();
      // Setup mail server
      props.put("mail.smtp.host", mailHost);
      // Get session
      Session session = Session.getInstance(props, null);
      Message message = new MimeMessage(session);
      //message.setFrom(new InternetAddress("GCB Admin <GCBAdmin@danet-uk.com>"));
      message.setFrom(new InternetAddress(fromAddress));
      //InternetAddress[] recips = {new InternetAddress(toAddress)};
      message.setRecipients(Message.RecipientType.TO,
        recips);
      InternetAddress replyTo[] = new InternetAddress[ 1 ];
      //replyTo[ 0 ] = new InternetAddress( "GCB Admin <GCBAdmin@danet-uk.com>" );
      replyTo[ 0 ] = new InternetAddress(replyAddress);
      message.setReplyTo(replyTo);
      message.setSubject(subject);
      message.setSentDate(new java.util.Date());
      BodyPart messageBodyPart = new MimeBodyPart();
      messageBodyPart.setContent(content, "text/html");
      Multipart multipart = new MimeMultipart("related");
      multipart.addBodyPart(messageBodyPart);

      // Part two is attachment
      if (fileAttachment != null)
      {
        messageBodyPart = new MimeBodyPart();
        DataSource source = new FileDataSource(fileAttachment);
        messageBodyPart.setDataHandler(new DataHandler(source));
        messageBodyPart.setFileName(fileAttachment.substring(
          fileAttachment.lastIndexOf("\\")+1));
        multipart.addBodyPart(messageBodyPart);
      }


      message.setContent(multipart);
      Transport.send(message);
    }
    catch(Exception ex)
    {
            ex.printStackTrace();
    }
  }

  private void sendEmailNoAtt(String toAddress, String subject, String content)
  {
    try
    {
      // Get system properties
      Properties props = System.getProperties();
      // Setup mail server
      props.put("mail.smtp.host", mailHost);
      // Get session
      Session session = Session.getInstance(props, null);
      Message message = new MimeMessage(session);
      message.setFrom(new InternetAddress("GCBAdmin@danet-uk.com"));
      message.setRecipient(Message.RecipientType.TO,
        new InternetAddress(toAddress));
      message.setSubject(subject);
      message.setSentDate(new java.util.Date());
      BodyPart messageBodyPart = new MimeBodyPart();
      messageBodyPart.setContent(content, "text/html");
      Multipart multipart = new MimeMultipart("related");
      multipart.addBodyPart(messageBodyPart);
      message.setContent(multipart);
      Transport.send(message);
    }
    catch(Exception ex)
    {
            ex.printStackTrace();
    }
  }

  private void zipFile(ArrayList filesToZip,
    String zippedFile)
  {
    if (!filesToZip.isEmpty())
    {
      // Create a buffer for reading the files
      byte[] buf = new byte[1024];

      try
      {
        // Create the ZIP file
        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zippedFile));

        for (Iterator it = filesToZip.iterator(); it.hasNext(); )
        {
          String fileAttachment = attachmentPath + File.separator + (String)it.next();
          // Compress the file
          FileInputStream in = new FileInputStream(fileAttachment);


          // Add ZIP entry to output stream.
          out.putNextEntry(new ZipEntry(fileAttachment.substring(fileAttachment.lastIndexOf(File.separator)+1)));

          // Transfer bytes from the file to the ZIP file
          int len;
          while ((len = in.read(buf)) > 0)
          {
            out.write(buf, 0, len);
          }

          // Complete the entry
          out.closeEntry();
          in.close();
        }
        // Complete the ZIP file
        out.close();
      }
      catch (IOException ex)
      {
        writeToLogFile("Error in zipFile(): " + ex.getMessage());
      }
    }
  }
}



