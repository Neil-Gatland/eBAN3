package Batch;

import java.io.*;
import java.util.*;
import JavaUtil.*;
import DBUtilities.*;

public class cdrDeletion
{
  // class variables
  private String logDir, billingSource, mode, attachmentPath,archiveAttachmentPath, destinationPath;
  private String dailyPeriod, monthlyPeriod;
  private int dailyMonths, monthlyMonths;
  private DBAccess dba;
  private StringUtil su;
  private String runTS, logMessage;
  private BufferedWriter logWriter;
  private File logFile, attachmentFolder, destinationFolder;

  // class constants
  private final String DTFMT = "yyyyMMddHHmmss";
  private final String SSBS = "SSBS";
  private final String EBILLZ = "ebillz";
  private final String ARBOR = "Arbor";
  private final String REPORT = "Report";
  private final String DELETE = "Delete";
  private final String DAILY = "Daily";
  private final String MONTHLY = "Monthly";
  private final String FSLASH = "/";
  private final String OK = "OK";
  private final String ACCOUNT = "account";
  private final String INVOICE = "invoice";
  private final String USCORE = "_";
  private final String CDRDEL = "CDR Deletion";
  private final String NOTAVAILABLE = "Not available";
  private final String ACTIVE = "Active";
  private final String INACTIVE = "Inactive";
  private final String ARCHIVE = "archive";
  private final String NORMAL = "normal";

  private cdrDeletion()
  {
    dba = new DBAccess();
    su = new StringUtil();
    // get parameters from properties files
    logDir = EBANProperties.getEBANProperty(EBANProperties.CDRDELLOGDIR);
    billingSource = EBANProperties.getEBANProperty(EBANProperties.CDRDELBILLSOURCE);
    mode = EBANProperties.getEBANProperty(EBANProperties.CDRDELMODE);
    attachmentPath = EBANProperties.getEBANProperty(EBANProperties.CDRDELATTACHPATH);
    archiveAttachmentPath = EBANProperties.getEBANProperty(EBANProperties.CDRDELARCHIVEATTACHPATH);
    destinationPath = EBANProperties.getEBANProperty(EBANProperties.CDRDELDESTPATH);
    dailyPeriod=EBANProperties.getEBANProperty(EBANProperties.CDRDELDAILYPERIOD);
    monthlyPeriod=EBANProperties.getEBANProperty(EBANProperties.CDRDELMONTHLYPERIOD);
  }

  public static void main(String[] args)
  {
    cdrDeletion cd = new cdrDeletion();
    cd.control();
  }

  // get and validate supplied parameters, check that the process is not already running and then invoke deletion process
  public void control()
  {
    // Validate supplied parameters - only proceeed if correct
    boolean parametersOK = true, logging = false;
    // check that the log directory exists
    File logFolder = new File(logDir);
    if (logFolder.exists())
    {
      // perform other validation which can be written to the log file
      logging = true;
      openLogFile();
      // Validate billing source
      billingSource = billingSource.trim();
      if ((!billingSource.equals(SSBS))&&(!billingSource.equals(EBILLZ))&&(!billingSource.equals(ARBOR)))
      {
        logMessage = "   CANNOT PROCEED - Invalid billing source "+billingSource;
        writeToLogFile(logMessage);
        parametersOK = false;
      }
      // Validate mode
      mode = mode.trim();
      if ((!mode.equals(REPORT))&&(!mode.equals(DELETE)))
      {
        logMessage = "   CANNOT PROCEED - Invalid mode "+mode;
        writeToLogFile(logMessage);
        parametersOK = false;
      }
      // Validate attachment path
      attachmentFolder = new File(attachmentPath);
      if (!attachmentFolder.exists())
      {
        logMessage = "   CANNOT PROCEED - Unable to find source attachment path "+attachmentPath;
        writeToLogFile(logMessage);
        parametersOK = false;
      }
      // Validate attachment path
      destinationFolder = new File(destinationPath);
      if (!destinationFolder.exists())
      {
        logMessage = "   CANNOT PROCEED - Unable to find target destination path "+destinationPath;
        writeToLogFile(logMessage);
        parametersOK = false;
      }
      // Validate daily period
      dailyMonths = Integer.parseInt(dailyPeriod);
      if (dailyMonths<1)
      {
        logMessage = "   CANNOT PROCEED - Invalid daily period of  "+dailyPeriod;
        writeToLogFile(logMessage);
        parametersOK = false;
      }
      // Validate monthly period
      monthlyMonths = Integer.parseInt(monthlyPeriod);
      if (monthlyMonths<1)
      {
        logMessage = "   CANNOT PROCEED - Invalid monthly period of  "+monthlyPeriod;
        writeToLogFile(logMessage);
        parametersOK = false;
      }
    }
    else
    {
      System.out.println("Cannot proceed as log directory not found");
      parametersOK = false;

    }
    if ((logging)&&(!parametersOK))
    {
      logMessage = "   ";
      writeToLogFile(logMessage);
    }
    // if parameters are OK then proceed to identify CDR files to delete
    if (parametersOK)
    {
      logMessage = "   Running CDR Deletion process in "+mode.toLowerCase()+" mode for billing source "+billingSource;
      writeToLogFile(logMessage);
      if (mode.equals(DELETE))
      {
          logMessage = "   Deleted CDRs will be copied to "+destinationPath;
          writeToLogFile(logMessage);
      }
      if (dailyMonths==1)
        logMessage = "   Daily cdrs over 1 month old will be processed";
      else
        logMessage = "   Daily cdrs over "+dailyPeriod.trim()+" months old will be processed";
      writeToLogFile(logMessage);
      if (monthlyMonths==1)
        logMessage = "   Monthly cdrs over 1 month old will be processed";
      else
        logMessage = "   Monthly cdrs over "+monthlyPeriod.trim()+" months old will be processed";
      writeToLogFile(logMessage);
      logMessage = "   ";
      writeToLogFile(logMessage);
      String status = dba.getRunControlStatus(billingSource,CDRDEL);
      if (status.startsWith(NOTAVAILABLE))
      {
        logMessage = "   CANNOT PROCEED - MISSING RUN CONTROL FOR CDR DELETION PROCESS";
        writeToLogFile(logMessage);
        logMessage = "   ";
        writeToLogFile(logMessage);
      }
      else
      {
        if (status.startsWith(ACTIVE))
        {
          logMessage = "   CANNOT PROCEED - CDR DELETION PROCESS ALREADY RUNNING";
          writeToLogFile(logMessage);
          logMessage = "   ";
          writeToLogFile(logMessage);
        }
        else
        {
          if (dba.updateRunControlStatus(billingSource,CDRDEL,ACTIVE))
          {
            deleteCDRs();
            if (!dba.updateRunControlStatus(billingSource,CDRDEL,INACTIVE))
            {
              logMessage = "   FAILED TO REST RUN CONTROL AT COMPLETION";
              writeToLogFile(logMessage);
              logMessage = "   ";
              writeToLogFile(logMessage);
            }
          }
          else
            {
              logMessage = "   CANNOT PROCEED - FAILED TO SET RUN CONTROL FOR CDR DELETION PROCESS";
              writeToLogFile(logMessage);
              logMessage = "   ";
              writeToLogFile(logMessage);
          }
        }
      }
    }
    if (logging)
      closeLogFile();
  }

  // handle CDR file deletion/reporting processing
  private void deleteCDRs()
  {
    long dailyCDRCount = 0, monthlyCDRCount = 0, monthlyArchiveCDRCount = 0;
    long dailyDeletedCount = 0, monthlyDeletedCount = 0, monthlyArchiveDeletedCount = 0;
    // Process daily cdrs
    if (mode.equals(DELETE))
      dba.setRBS09();
    java.sql.ResultSet dRS = dba.getAttachments(DAILY,dailyPeriod,billingSource,NORMAL);
    if (mode.equals(DELETE))
      dba.setRBS09();
    try
    {
      while (dRS.next())
      {
        dailyCDRCount++;
        // only delete cdrs if in delete mode
        if(mode.equals(DELETE))
        {
          boolean ok = true;
          String location = dRS.getString("Location");
          long accountId = dRS.getLong("Id");
          String filename = cdrFilename(location);
          String originalFilename = attachmentPath+File.separator+location;
          File oldLocation = new File(originalFilename);
          if (oldLocation.exists())
            if (!oldLocation.delete())
            {
              logMessage = "   ERROR ON DELETION - failure to delete daily CDR in  "+originalFilename;
              writeToLogFile(logMessage);
              ok = false;
            }
          // delete the attachment record
          if (ok)
          {
            try
            {
              long fileId = dRS.getLong("File_Id");
              String result = dba.removeAttachment(location,accountId,ACCOUNT,fileId);
              if (result.equals(OK))
              {
                dailyDeletedCount++;
                emptyDirectoryCheck(originalFilename,filename);
              }
              else
              {
                logMessage = "   ERROR ON DELETION - Error deleting daily CDR attachment : "+result;
                writeToLogFile(logMessage);
              }
            }
            catch(java.sql.SQLException ex)
            {
              logMessage = "   ERROR ON DELETION - Error deleting daily CDR attachment : "+ex.getMessage();
              writeToLogFile(logMessage);
            }
          }
        }
      }
    }
    catch(java.sql.SQLException ex)
    {
      logMessage = "   ERROR ON DELETION - Error accessing daily CDR attachments : "+ex.getMessage();
      writeToLogFile(logMessage);
    }
    dRS=null;
    // report on daily deletions
    logMessage = "   Number of daily CDRs identified for deletion           : "+dailyCDRCount;
    writeToLogFile(logMessage);
    if (mode.equals(DELETE))
    {
      logMessage = "   Number of daily CDRs deleted                           : "+dailyDeletedCount;
      writeToLogFile(logMessage);
    }
    logMessage = "   ";
    writeToLogFile(logMessage);
    // Process monthly cdrs (not archive)
    if (mode.equals(DELETE))
      dba.setRBS09();
    java.sql.ResultSet mRS = dba.getAttachments(MONTHLY,monthlyPeriod,billingSource,NORMAL);
    if (mode.equals(DELETE))
      dba.setRBS09();
    try
    {
      while (mRS.next())
      {
        monthlyCDRCount++;
        // only delete cdrs if in delete mode
        if(mode.equals(DELETE))
        {
          boolean ok = true;
          String originalFilename = "";
          // check if account folder exists in destination, if not create it
          long invoiceId = mRS.getLong("Id");
          String accountNumber = dba.getInvoiceAccountNumber(invoiceId);
          String location = mRS.getString("Location");
          String filename = cdrFilename(location);
          if (accountNumber.equals(""))
            accountNumber = filenameAccountNumber(filename,billingSource);
          File accountFolder = new File(destinationPath+File.separator+accountNumber);
          if (!accountFolder.exists())
            accountFolder.mkdir();
          // move CDR file, renaming it into the destination folder
          String newCDRLocation = destinationPath+File.separator+accountNumber+File.separator+filename;
          File newLocation = new File(newCDRLocation);
          if (newLocation.exists())
          {
            logMessage = "   ERROR ON DELETION - new CDR location "+newCDRLocation+" already used!";
            writeToLogFile(logMessage);
            ok = false;
          }
          else
          {
            originalFilename = attachmentPath+File.separator+location;
            File oldLocation = new File(originalFilename);
            if(!oldLocation.renameTo(newLocation))
            {
              logMessage = "   ERROR ON DELETION - failed to move CDR file from  "+originalFilename+" to "+newCDRLocation;
              writeToLogFile(logMessage);
              ok = false;
            }
          }
          // delete the attachment record
          if (ok)
          {
            try
            {
              long fileId = mRS.getLong("File_Id");
              String result = dba.removeAttachment(location,invoiceId,INVOICE,fileId);
              if (result.equals(OK))
              {
                monthlyDeletedCount++;
                emptyDirectoryCheck(originalFilename,filename);
              }
              else
              {
                //System.out.println(location+" : "+invoiceId+" : "+INVOICE+" : "+fileId);
                logMessage = "   ERROR ON DELETION - Error deleting monthly CDR attachment : "+result;
                writeToLogFile(logMessage);
              }
            }
            catch(java.sql.SQLException ex)
            {
              logMessage = "   ERROR ON DELETION - Error deleting monthly CDR attachment : "+ex.getMessage();
              writeToLogFile(logMessage);
            }
          }
        }
      }
    }
    catch(java.sql.SQLException ex)
    {
      logMessage = "   ERROR ON DELETION - Error accessing monthly CDR attachments : "+ex.getMessage();
      writeToLogFile(logMessage);
    }
    mRS=null;
    // report on monthly deletions
    logMessage = "   Number of monthly CDRs identified for deletion         : "+monthlyCDRCount;
    writeToLogFile(logMessage);
    if (mode.equals(DELETE))
    {
      logMessage = "   Number of monthly CDRs deleted                         : "+monthlyDeletedCount;
      writeToLogFile(logMessage);
    }
    logMessage = "   ";
    writeToLogFile(logMessage);
    // Process monthly cdrs (archive)
    if (mode.equals(DELETE))
      dba.setRBS09();
    java.sql.ResultSet maRS = dba.getAttachments(MONTHLY,monthlyPeriod,billingSource,ARCHIVE);
    if (mode.equals(DELETE))
      dba.setRBS09();
    try
    {
      while (maRS.next())
      {
        monthlyArchiveCDRCount++;
        // only delete cdrs if in delete mode
        if(mode.equals(DELETE))
        {
          boolean ok = true;
          String originalFilename = "";
          // check if account folder exists in destination, if not create it
          long invoiceId = maRS.getLong("Id");
          String accountNumber = dba.getInvoiceAccountNumber(invoiceId);
          String originalLocation = maRS.getString("Location");
          String location = originalLocation.substring(8,originalLocation.length());
          String filename = cdrFilename(location);
          if (accountNumber.equals(""))
            accountNumber = filenameAccountNumber(filename,billingSource);
          File accountFolder = new File(destinationPath+File.separator+accountNumber);
          if (!accountFolder.exists())
            accountFolder.mkdir();
          // move CDR file, renaming it into the destination folder
          String newCDRLocation = destinationPath+File.separator+accountNumber+File.separator+filename;
          File newLocation = new File(newCDRLocation);
          if (newLocation.exists())
          {
            logMessage = "   ERROR ON DELETION - new CDR location "+newCDRLocation+" already used!";
            writeToLogFile(logMessage);
            ok = false;
          }
          else
          {
            originalFilename = archiveAttachmentPath+File.separator+location;
            File oldLocation = new File(originalFilename);
            if(!oldLocation.renameTo(newLocation))
            {
              logMessage = "   ERROR ON DELETION - failed to move CDR file from  "+originalFilename+" to "+newCDRLocation;
              writeToLogFile(logMessage);
              ok = false;
            }
          }
          // delete the attachment record
          if (ok)
          {
            try
            {
              long fileId = maRS.getLong("File_Id");
              String result = dba.removeAttachment(originalLocation,invoiceId,INVOICE,fileId);
              if (result.equals(OK))
              {
                monthlyArchiveDeletedCount++;
                emptyDirectoryCheck(originalFilename,filename);
              }
              else
              {
                //System.out.println(location+" : "+invoiceId+" : "+INVOICE+" : "+fileId);
                logMessage = "   ERROR ON DELETION - Error deleting monthly archive CDR attachment : "+result;
                writeToLogFile(logMessage);
              }
            }
            catch(java.sql.SQLException ex)
            {
              logMessage = "   ERROR ON DELETION - Error deleting monthly archive CDR attachment : "+ex.getMessage();
              writeToLogFile(logMessage);
            }
          }
        }
      }
    }
    catch(java.sql.SQLException ex)
    {
      logMessage = "   ERROR ON DELETION - Error accessing monthly archive CDR attachments : "+ex.getMessage();
      writeToLogFile(logMessage);
    }
    maRS=null;
    // report on monthly deletions
    logMessage = "   Number of monthly archive CDRs identified for deletion : "+monthlyArchiveCDRCount;
    writeToLogFile(logMessage);
    if (mode.equals(DELETE))
    {
      logMessage = "   Number of monthly archive CDRs deleted                 : "+monthlyArchiveDeletedCount;
      writeToLogFile(logMessage);
    }
    logMessage = "   ";
    writeToLogFile(logMessage);
  }

  // gets filename from the attachment location
  private String cdrFilename (String location)
  {
    String name = "";
    int startPos = location.length()-1;
    boolean notFound = true;
    for( int i=startPos; i>=0; i--)
    {
      if (notFound)
      {
        String testChar = location.substring(i,i+1);
        if (testChar.equals(FSLASH))
        {
          notFound = false;
          name = location.substring(i+1,location.length());
        }
      }
    }
    return name;
  }

  // gets account number from a CDR filename
  private String filenameAccountNumber(String filename, String billingSource)
  {
    String accountNumber = "";
    if (billingSource.equals(EBILLZ))
    {
      accountNumber = filename.substring(0,9);
    }
    else
    {
      boolean done = false;
      for(int i=0; i<filename.length(); i++)
      {
        String test = filename.substring(i,i+1);
        if (test.equals(USCORE))
          done = true;
        if (!done)
          accountNumber = accountNumber+test;
      }
      int zeroCount = 8 - accountNumber.length();
      for (int i=0; i<zeroCount; i++)
      {
        accountNumber = 0 + accountNumber;
      }
    }
    return accountNumber;
  }

  // checks whether a directory is empty and can be deleted
  private void emptyDirectoryCheck(String fullname, String filename)
  {
    int fullLength = fullname.length();
    int nameLength = filename.length();
    String directoryName = fullname.substring(0,fullLength-nameLength-1);
    File directory = new File(directoryName);
    if (directory.isDirectory())
      if (directory.list().length==0)
        if (!directory.delete())
          System.out.println("Failed to delete empty directory "+directoryName);
  }

  // opens process log file for writing
  private boolean openLogFile()
  {
    boolean result = false;
    // open a new log file
    try
    {
      GregorianCalendar gc = new GregorianCalendar();
      runTS = su.reformatDate("now", null, DTFMT);
      logFile = new File(logDir + File.separator + runTS + "_cdrDeletion_log.txt");
      logWriter = new BufferedWriter(new FileWriter(logFile));
      String now = su.DateToString(gc.getTime(), DTFMT);
      writeToLogFile("cdrDeletion processing started at " +
        now.substring(8, 10) + ":" + now.substring(10, 12) + "." +
        now.substring(12, 14) + " on " + now.substring(6, 8) + "/" +
        now.substring(4, 6) + "/" + now.substring(0, 4));
      writeToLogFile(" ");
      result = true;
    }
    catch (Exception ex)
    {
      System.out.println("Error opening log file: " + ex.getMessage());
    }
    return result;
  }

  // closes process log file
  private void closeLogFile()
  {
    // at end of program close log file
    try
    {
      GregorianCalendar gc = new GregorianCalendar();
      String now = su.DateToString(gc.getTime(), DTFMT);
      writeToLogFile("cdrDeletion processing ended at " +
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

  // writes a line of data to the process log file
  private void writeToLogFile(String message)
  {
    // write supplied text to program log file
    try
    {
      logWriter.write(message);
      logWriter.newLine();
    }
    catch (Exception ex)
    {
      System.out.println("Error writing message '" + message +
        "' to log file : " + ex.getMessage());
      System.exit(1);
    }
  }

}



