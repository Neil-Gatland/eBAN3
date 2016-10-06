package Batch;

import java.io.*;
import java.sql.*;
import java.util.*;
import DBUtilities.DBAccess;
import JavaUtil.*;

public class MOBILoad
{
  private DBAccess dba;
  private Connection conn;
  private String dropDir;
  private String outDir;
  private String rejectDir ;
  private String controlDir;
  private String controlName;
  private String newControlName;
  private File controlFile;
  private String noShowDir;
  private String archiveDir;
  private String logDir;
  private File logFile;
  private File rejectControlFile;
  private String runTS;
  private final String DATETIMEFORMAT = "yyyyMMddHHmmss";
  private StringUtil su;
  private BufferedWriter logWriter;
  private BufferedWriter rejectControlWriter;
  private String primaryRecipient;
  private String secondaryRecipient;
  private long invoiceId;
  private String invoiceNo;
  private String newPathSection;

  private MOBILoad()
  {
    dba = new DBAccess();
    dba.startDB();
    su = new StringUtil();
    runTS = su.reformatDate("now", null, DATETIMEFORMAT);
    dropDir = EBANProperties.getEBANProperty("mobi.dropDir");
    outDir = EBANProperties.getEBANProperty("mobi.outDir");
    rejectDir = EBANProperties.getEBANProperty("mobi.rejectDir");
    controlDir = EBANProperties.getEBANProperty("mobi.controlDir");
    controlName = EBANProperties.getEBANProperty("mobi.controlName");
    controlFile = new File(controlDir + File.separator + controlName);
    noShowDir = EBANProperties.getEBANProperty("mobi.noShowDir");
    archiveDir = EBANProperties.getEBANProperty("mobi.archiveDir");
    primaryRecipient = EBANProperties.getEBANProperty("mobi.primaryRecipient");
    secondaryRecipient = EBANProperties.getEBANProperty("mobi.secondaryRecipient");
    logDir = EBANProperties.getEBANProperty("mobi.logDir");
    logFile = new File(logDir + File.separator + "MOBILoad_" +
      runTS + ".log");
    newControlName = controlName.substring(0, controlName.indexOf(".")) +
      "_" + runTS + ".txt.";
    rejectControlFile = new File(rejectDir + File.separator +
      controlName.substring(0, controlName.indexOf(".")) + "_Reject_" + runTS +
      ".txt.");
    createLogFile();
  }

  private void createLogFile()
  {
    try
    {
      /*if (!logFile.exists())
      {
        logFile.createNewFile();
      }*/
      logWriter = new BufferedWriter(new FileWriter(logFile));
      GregorianCalendar gc = new GregorianCalendar();
      String now = su.DateToString(gc.getTime(), DATETIMEFORMAT);
      writeToLogFile("MOBI Load processing started at " +
        now.substring(8, 10) + ":" + now.substring(10, 12) + "." +
        now.substring(12, 14) + " on " + now.substring(6, 8) + "/" +
        now.substring(4, 6) + "/" + now.substring(0, 4));
    }
    catch (Exception ex)
    {
      System.out.println("Error creating log file: " + ex.getMessage());
      System.exit(1);
    }
  }

  private void createRejectControlFile()
  {
    try
    {
      /*if (!rejectControlFile.exists())
      {
        rejectControlFile.createNewFile();
      }*/
      rejectControlWriter = new BufferedWriter(new FileWriter(rejectControlFile));
    }
    catch (Exception ex)
    {
      System.out.println("Error creating reject control file: " + ex.getMessage());
      System.exit(1);
    }
  }

  private void writeToRejectControlFile(String line)
  {
    try
    {
      rejectControlWriter.write(line);
      rejectControlWriter.newLine();
    }
    catch (Exception ex)
    {
      System.out.println("Error writing message '" + line +
        "' to reject control file: " + ex.getMessage());
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

  private void closeLogFile()
  {
    try
    {
      GregorianCalendar gc = new GregorianCalendar();
      String now = su.DateToString(gc.getTime(), DATETIMEFORMAT);
      writeToLogFile("MOBI Load processing ended at " +
        now.substring(8, 10) + ":" + now.substring(10, 12) + "." +
        now.substring(12, 14) + " on " + now.substring(6, 8) + "/" +
        now.substring(4, 6) + "/" + now.substring(0, 4));
      logWriter.close();
    }
    catch (Exception ex)
    {
      System.out.println("Error closing log file: " + ex.getMessage());
      System.exit(1);
    }
  }

  private boolean validate(String fileName)
  {
    boolean isValid = false;
    // String split() method not available in this version of java
    StringTokenizer st = new StringTokenizer(fileName, "_");
    String[] sections = new String[st.countTokens()];
    int tCount = 0;
    while (st.hasMoreTokens())
    {
      sections[tCount++] = st.nextToken();
    }
    if (sections.length != 3)
    {
      writeToLogFile(fileName + " - invalid file format");
    }
    else if (!sections[2].substring(0, sections[2].indexOf(".")).equals("1"))
    {
      writeToLogFile(fileName + " - too many pages");
    }
    else
    {
      invoiceNo = sections[1];
      isValid = true;
    }
    return isValid;
  }

  private boolean customerMappingExists(String customerName)
    throws Exception
  {
    boolean found = false;
    try
    {
      conn = dba.connectExt();
      String sql = "{? = call MOBI_LOAD.Customer_Mapping_Exists(?)}";
      CallableStatement cstmt = conn.prepareCall(sql);
      cstmt.registerOutParameter(1, java.sql.Types.INTEGER);
      cstmt.setString(2, customerName);
      cstmt.execute();
      long ret = cstmt.getLong(1);
      found = ret > 0;
      dba.freeConnectExt(conn);
      return found;
    }
    catch (Exception ex)
    {
      dba.freeConnectExt(conn);
      throw new Exception("Error in customerMappingExists: " + ex.getMessage());
    }
  }

  private boolean getAttachmentLocation()
    throws Exception
  {
    boolean found = false;
    try
    {
      conn = dba.connectExt();
      String sql = "{? = call MOBI_LOAD.Get_Attachment_Location(?,?)}";
      CallableStatement cstmt = conn.prepareCall(sql);
      cstmt.registerOutParameter(1, java.sql.Types.VARCHAR);
      cstmt.setLong(2, invoiceId);
      cstmt.setString(3, "invoice");
      cstmt.execute();
      String ret = cstmt.getString(1);
      if ((!ret.equals("-1")) && (!ret.equals("-2")))
      {
        found = true;
        newPathSection = ret;
      }
      dba.freeConnectExt(conn);
      return found;
    }
    catch (Exception ex)
    {
      dba.freeConnectExt(conn);
      throw new Exception("Error in getAttachmentLocation: " + ex.getMessage());
    }
  }

  private boolean processReport()
    throws Exception
  {
    boolean success = false;
    try
    {
      conn = dba.connectExt();
      String sql = "{? = call MOBI_LOAD.Process_Invoice_Report(?,?,?)}";
      CallableStatement cstmt = conn.prepareCall(sql);
      cstmt.registerOutParameter(1, java.sql.Types.INTEGER);
      cstmt.setString(2, invoiceNo);
      cstmt.setString(3, primaryRecipient);
      cstmt.setString(4, secondaryRecipient);
      cstmt.execute();
      long ret = cstmt.getLong(1);
System.out.println("ret:"+ret);
      if (ret > 0)
      {
        success = true;
        invoiceId = ret;
      }
      else
      {
        String errorMsg = "";
        if(ret == -1) errorMsg = "error finding payment group";
        else if (ret == -3) errorMsg = "no control record";
        else if (ret == -4) errorMsg = "more than 1 control record";
        else if (ret == -5) errorMsg = "error finding control record";
        else if (ret == -7) errorMsg = "error creating payment group";
        else if (ret == -8) errorMsg = "error finding account";
        else if (ret == -9) errorMsg = "error creating account";
        else if (ret == -10) errorMsg = "error creating invoice";
        else if (ret == -11) errorMsg = "error finding attachment report name";
        else if (ret == -12) errorMsg = "error creating attachment";
        else if (ret == -13) errorMsg = "error adding primary recipient link";
        else if (ret == -14) errorMsg = "error adding secondary recipient link";
        else if (ret == -15)
        {
          errorMsg = "report for invoice " + invoiceNo + " not for display";
          invoiceId = ret;
        }
        else errorMsg = "unknown return value " + ret;
        writeToLogFile(invoiceNo + " - processReport: " + errorMsg);
      }
      dba.freeConnectExt(conn);
      return success;
    }
    catch (Exception ex)
    {
      dba.freeConnectExt(conn);
      throw new Exception("Error in processReport: " + ex.getMessage());
    }
  }

  private boolean insertControlRecord(String[] controlInfo)
    throws Exception
  {
    boolean success = false;
    try
    {
      conn = dba.connectExt();
      String sql = "{? = call MOBI_LOAD.Insert_Control_Record(?,?,?,?,?,?,?,?,?,?,?,?)}";
      CallableStatement cstmt = conn.prepareCall(sql);
      cstmt.registerOutParameter(1, java.sql.Types.INTEGER);
      cstmt.setString(2, controlInfo[0]);
      cstmt.setString(3, controlInfo[2]);
      cstmt.setString(4, controlInfo[3]);
      cstmt.setString(5, controlInfo[4]);
      cstmt.setString(6, controlInfo[5]);
      cstmt.setString(7, controlInfo[6]);
      cstmt.setString(8, controlInfo[7]);
      cstmt.setString(9, controlInfo[8]);
      cstmt.setString(10, controlInfo[9]);
      cstmt.setString(11, newControlName);
      cstmt.setString(12, "N");
      cstmt.setString(13, controlInfo[10]);
      cstmt.execute();
      success = cstmt.getLong(1) == 0;
      dba.freeConnectExt(conn);
      return success;
    }
    catch (Exception ex)
    {
      dba.freeConnectExt(conn);
      throw new Exception("Error in insertControlRecord: " + ex.getMessage());
    }
  }

  private boolean updateControlRecord()
    throws Exception
 {
    boolean success = false;
    try
    {
      conn = dba.connectExt();
      String sql = "{? = call MOBI_LOAD.Update_MOBI_Control(?)}";
      CallableStatement cstmt = conn.prepareCall(sql);
      cstmt.registerOutParameter(1, java.sql.Types.INTEGER);
      cstmt.setString(2, invoiceNo);
      cstmt.execute();
      success = cstmt.getLong(1) == 0;
      dba.freeConnectExt(conn);
      return success;
    }
    catch (Exception ex)
    {
      dba.freeConnectExt(conn);
      throw new Exception("Error in updateControlRecord: " + ex.getMessage());
    }
  }

  private boolean processFile(File oldFile)
    throws Exception
  {
    boolean success = false;
    if (getAttachmentLocation())
    {
      File thisDir = new File(outDir + File.separator +
        newPathSection.substring(0, newPathSection.lastIndexOf(File.separator)));
      if (!thisDir.isDirectory())
      {
        thisDir.mkdirs();
      }
      File thisFile = new File(outDir + File.separator + newPathSection);
      if (thisFile.exists())
      {
        thisFile.delete();
      }
      if (oldFile.renameTo(thisFile))
      {
        success = updateControlRecord();
      }
    }
    return success;
  }

  private void processControlFile()
    throws Exception
  {
    createRejectControlFile();
    long validCount = 0;
    long rejectCount = 0;
    String line = null;
    BufferedReader buff = new BufferedReader(new FileReader(controlFile));
    line = buff.readLine();
    while ((line != null) && (!line.equals("")))
    {
      // String split() method not available in this version of java
      StringTokenizer st = new StringTokenizer(line, "|");
      String[] controlInfo = new String[st.countTokens()];
      int tokenCount = 0;
      while (st.hasMoreTokens())
      {
        controlInfo[tokenCount++] = st.nextToken();
      }
      boolean valid = tokenCount == 11;
      if (valid)
      {
        if (customerMappingExists(controlInfo[0]))
        {
          if (insertControlRecord(controlInfo))
          {
            validCount++;
          }
          else
          {
            rejectCount++;
            writeToLogFile("Unable to insert control record: " + line);
            writeToRejectControlFile(line);
          }
        }
        else
        {
          rejectCount++;
          writeToLogFile("processControlFile: customer mapping not found for " +
            controlInfo[0]);
          writeToLogFile("processControlFile: " + line);
          writeToRejectControlFile(line);
        }
      }
      else
      {
        rejectCount++;
        writeToLogFile("processControlFile: invalid number of fields in " +
          "control record - found " + tokenCount + ", expected 11");
        writeToLogFile("processControlFile: " + line);
        writeToRejectControlFile(line);
      }
      line = buff.readLine();
    }
    buff.close();
    rejectControlWriter.close();
    writeToLogFile("Control Records Successful: " + validCount);
    writeToLogFile("Control Records Rejected: " + rejectCount);
    if (rejectCount == 0)
    {
      rejectControlFile.delete();
    }
    controlFile.renameTo(new File(archiveDir + File.separator + newControlName));
  }

  private void validateFiles()
  {
    long validCount = 0;
    long rejectCount = 0;
    long nonDisplayCount = 0;
    try
    {
      if (controlFile.exists())
      {
        processControlFile();
      }
      else
      {
        writeToLogFile("Control Records Successful: 0");
        writeToLogFile("Control Records Rejected: 0");
      }

      File[] fileArray = (new File(dropDir)).listFiles();
      if (fileArray != null)
      {
        for (int i = 0; i < fileArray.length; i++)
        {
          String fileName = fileArray[i].getName();
          if (validate(fileName))
          {
            if (processReport())
            {
              if (processFile(fileArray[i]))
              {
                validCount++;
              }
              else
              {
                rejectCount++;
              }
            }
            else
            {
              File newFile = null;
              if (invoiceId == -15)
              {
                if (updateControlRecord())
                {
                  newFile = new File(noShowDir + File.separator + fileName);
                  nonDisplayCount++;
                }
                else
                {
                  newFile = new File(rejectDir + File.separator + fileName);
                  rejectCount++;
                }
              }
              else
              {
                newFile = new File(rejectDir + File.separator + fileName);
                rejectCount++;
              }
              fileArray[i].renameTo(newFile);
            }
          }
          else
          {
            rejectCount++;
          }
        }
      }
    }
    catch (Exception ex)
    {
      writeToLogFile("Error in MOBILoad: " + ex.getMessage());
    }
    finally
    {
      writeToLogFile("Successful: " + validCount);
      writeToLogFile("Rejected: " + rejectCount);
      writeToLogFile("Not displayed: " + nonDisplayCount);
      closeLogFile();
    }
  }

  public static void main(String[] args)
  {
    MOBILoad ml = new MOBILoad();
    ml.validateFiles();
  }

}



