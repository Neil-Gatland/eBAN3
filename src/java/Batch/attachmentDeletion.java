package Batch;

import java.io.*;
import java.util.*;
import JavaUtil.*;
import DBUtilities.*;

public class attachmentDeletion
{
  // class variables
  private String logDir, mode, attachmentPath, archiveAttachmentPath, destinationPath, reportArchiveDir, period, locationType;
  private int numberMonths;
  private DBAccess dba;
  private StringUtil su;
  private String runTS, logMessage;
  private BufferedWriter logWriter;
  private File logFile, attachmentFolder, archiveAttachmentFolder, destinationFolder;
  private String topLevelFolder, shortFilename;

  // class constants
  private final String DTFMT = "yyyyMMddHHmmss";
  private final String SSBS = "SSBS";
  private final String EBILLZ = "ebillz";
  private final String ARBOR = "Arbor";
  private final String REPORT = "Report";
  private final String DELETE = "Delete";
  private final String FSLASH = "/";
  private final String BSLASH = "\\";
  private final String INVOICE = "invoice";
  private final String USCORE = "_";
  private final String CDRDEL = "Attachment Deletion";
  private final String NOTAVAILABLE = "Not available";
  private final String ACTIVE = "Active";
  private final String INACTIVE = "Inactive";
  private final String ARCHIVE = "archive";
  private final String NORMAL = "normal";
  private final String BE = "BE";
  private final String BV = "BV";
  private final String CONGLOM = "CONGLOM";
  private final String CPS = "CPS";
  private final String DATA = "Data";
  private final String MS = "MS";
  private final String MOBI = "MOBI";
  private final String RAM = "RAM";
  private final String ENERGISINVOICE = "Energis_Invoice";
  private final String ARBOR_D = "Arbor_Invoice_D.pdf";
  private final String ARBOR_S = "Arbor_Invoice_S.pdf";
  private final String PDFEXT = ".pdf";

  private attachmentDeletion()
  {
    dba = new DBAccess();
    su = new StringUtil();
    // get parameters from properties files
    logDir = EBANProperties.getEBANProperty(EBANProperties.ATTACHDELLOGDIR);
    attachmentPath = EBANProperties.getEBANProperty(EBANProperties.ATTACHDELATTACHPATH);
    archiveAttachmentPath = EBANProperties.getEBANProperty(EBANProperties.ATTACHDELARCHIVEATTACHPATH);
    destinationPath = EBANProperties.getEBANProperty(EBANProperties.ATTACHDELDESTPATH);
    reportArchiveDir = EBANProperties.getEBANProperty(EBANProperties.ATTACHDELREPORTARCHIVE);
  }

  public static void main(String[] args)
  {
    attachmentDeletion ad = new attachmentDeletion();
    ad.control();
  }

  // get and validate supplied parameters, check that the process is not already running and then invoke deletion process
  public void control()
  {
    // Validate supplied parameters - only proceeed if correct
    boolean parametersOK = true, logging = false;
    long attachmentCount = 0, deletedCount = 0;
    // check that the log directory exists
    File logFolder = new File(logDir);
    if (logFolder.exists())
    {
      // perform other validation which can be written to the log file
      logging = true;
      openLogFile();
      // Validate attachment path
      attachmentFolder = new File(attachmentPath);
      if (!attachmentFolder.exists())
      {
        logMessage = "   CANNOT PROCEED - Unable to find source attachment path "+attachmentPath;
        writeToLogFile(logMessage);
        parametersOK = false;
      }
      // Validate archive attachment path
      archiveAttachmentFolder = new File(archiveAttachmentPath);
      if (!archiveAttachmentFolder.exists())
      {
        logMessage = "   CANNOT PROCEED - Unable to find source archive attachment path "+archiveAttachmentPath;
        writeToLogFile(logMessage);
        parametersOK = false;
      }
      // Validate destination path
      destinationFolder = new File(destinationPath);
      if (!destinationFolder.exists())
      {
        logMessage = "   CANNOT PROCEED - Unable to find target destination path "+destinationPath;
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
    // if parameters are OK then proceed to identify attachment files to delete
    if (parametersOK)
    {
      java.sql.ResultSet iListRS = dba.getInvoiceAttachments();
      try
      {
        while (iListRS.next())
        {
          attachmentCount++;
          long invoiceId = iListRS.getLong("Invoice_Id");
          String accountNumber = iListRS.getString("Account_Number");
          String billingSource = iListRS.getString("Billing_Source");
          String location = iListRS.getString("Location");
          String originalLocation = iListRS.getString("Original_Location");
          String itemType = iListRS.getString("Item_Type");
          String name = iListRS.getString("Name");
          String currentPath = "";
          if (location.startsWith(ARCHIVE))
            currentPath = archiveAttachmentPath + File.separator + location;
          else
            currentPath = attachmentPath + File.separator + location;
          File currentLoc = new File(currentPath);
          if (currentLoc.exists())
          {
            // move to report archive if the item type is 'report'
            // otherwise move to final archive
            if (itemType.equals("report"))
            {
              // make GCID folder if it does not exist
              String gcidPath = reportArchiveDir+File.separator+billingSource+USCORE+accountNumber;
              File gcidFolder = new File(gcidPath);
              if (!gcidFolder.exists())
                gcidFolder.mkdir();
              // make invoice folder if it does not exist
              String invoicePath = gcidPath+File.separator+dba.getInvoiceNo(invoiceId);
              File invoiceFolder = new File(invoicePath);
              if (!invoiceFolder.exists())
                invoiceFolder.mkdir();
              String newPath = invoicePath+File.separator+name+PDFEXT;
              File newLoc = new File(newPath);
              if (newLoc.exists())
              {
                logMessage = "   Report archive file "+newPath+" already exists";
                writeToLogFile(logMessage);
              }
              else
              {
                if (currentLoc.renameTo(newLoc))
                {
                  String invoiceNo = dba.getInvoiceNoRaw(invoiceId);
                  // dba function to delete attachment and create gcb report archive
                  if (dba.attachmentToReportArchive
                      (originalLocation,billingSource+USCORE+accountNumber,name+PDFEXT,invoiceNo,accountNumber))
                  {
                    deletedCount++;
                    String locationTest = location;
                    if (location.startsWith(ARCHIVE))
                      locationTest = location.substring(8,location.length()-1);
                    String newname = newFilename(billingSource,locationTest);
                    emptyDirectoryCheck(currentPath);
                  }
                  else
                  {
                    logMessage =
                      "    Could not move attachment frome location = "+originalLocation+
                      " to report archive";
                    writeToLogFile(logMessage);
                  }
                }
              }
            }
            else
            {
              String locationTest = location;
              if (location.startsWith(ARCHIVE))
                locationTest = location.substring(8,location.length());
              String newname = newFilename(billingSource,locationTest);
              // make account folder if it does not exist
              // (now not using billing source to be consistent with other archiving programs
              //String accountPath = destinationFolder+File.separator+topLevelFolder+File.separator+accountNumber;
              String accountPath = destinationFolder+File.separator+accountNumber;
              File accountFolder = new File(accountPath);
              if (!accountFolder.exists())
                accountFolder.mkdir();
              String newPath = accountPath+File.separator+newname;
              File newLoc = new File(newPath);
              if (newLoc.exists())
              {
                logMessage = "   Archived attachment file "+newPath+" already exists";
                writeToLogFile(logMessage);
              }
              else
              {
                if (currentLoc.renameTo(newLoc))
                {
                  if (dba.deleteAttachment(originalLocation))
                  {
                    deletedCount++;
                    emptyDirectoryCheck(currentPath);
                  }
                  else
                  {
                    logMessage =
                      "    Could not delete attachment where location = "+originalLocation+
                      " although new file "+newPath+" has been created";
                    writeToLogFile(logMessage);
                  }
                }
                else
                {
                  logMessage = "   Could not create archived attachment file "+newPath;
                  writeToLogFile(logMessage);
                }
              }
            }
          }
          else
          {
            logMessage = "   Cannot find attachment in location : "+currentPath;
            writeToLogFile(logMessage);
          }
        }
      }
      catch(java.sql.SQLException ex)
      {
        //if (!ex.getMessage().startsWith("Closed Resultset ");
        //{
        logMessage = "   ERROR - Error identifying invoice attachments : "+ex.getMessage();
        writeToLogFile(logMessage);
        //}
      }
    }
    if (logging)
    {
      logMessage = "   ";
      writeToLogFile(logMessage);
      if (attachmentCount>0)
      {
        logMessage = "   No of attachments identified for deletion : "+attachmentCount;
        writeToLogFile(logMessage);
        logMessage = "   No of attachments deleted                 : "+deletedCount;
        writeToLogFile(logMessage);
      }
      else
      {
        logMessage = "   There are no attachments outstanding for invoices flagged for deletion";
        writeToLogFile(logMessage);
      }
      logMessage = "   ";
      writeToLogFile(logMessage);
      closeLogFile();
    }
  }

  // work out destination filename
  private String newFilename( String billingSource, String location )
  {
    String name = "", work = "";
    String [] parts = new String[20];
    int noParts = 0, partPos = 1;
    // break up location into each folder name and the final file name
    for (int i=0; i<location.length(); i++)
    {
      String test = location.substring(i,i+1);
      if ((test.equals(FSLASH))||(test.equals(BSLASH)))
      {
        parts[partPos] = work;
        work = "";
        partPos++;
        noParts++;
      }
      else
      {
        work = work + test;
      }
    }
    parts[partPos] = work;
    noParts++;
    // work out filename depending on billing source and format of filename
    if ( (parts[noParts].startsWith(ENERGISINVOICE)) || (parts[1].equals(MOBI)) ||
         (parts[noParts].endsWith(ARBOR_D)) || (parts[noParts].endsWith(ARBOR_S)) )
      name = dba.getInvoiceNo((Long.parseLong(parts[noParts-1]))) + USCORE + parts[noParts];
    else if ((parts[1].equals(BE))||(parts[1].equals(BV))||(parts[1].equals(CONGLOM))||
        (parts[1].equals(CPS))||(parts[1].equals(DATA))||(parts[1].equals(MS))||
        (parts[1].equals(RAM)))
    {
      for (int i=1; i<=noParts; i++)
      {
        name = name + parts[i];
        if (i<noParts)
          name = name + USCORE;
      }
      topLevelFolder = parts[1];
    }
    else
    {
      name = parts[noParts];
      topLevelFolder = billingSource;
    }
    shortFilename = parts[noParts];
    return name;
  }

  // checks whether a directory is empty and can be deleted
  private void emptyDirectoryCheck(String fullpath)
  {
    int revisedPos = fullpath.length() - shortFilename.length()-1;
    String directoryName = fullpath.substring(0,revisedPos);
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
      logFile = new File(logDir + File.separator + runTS + "_attachmentDeletion_log.txt");
      logWriter = new BufferedWriter(new FileWriter(logFile));
      String now = su.DateToString(gc.getTime(), DTFMT);
      writeToLogFile("attachmentDeletion processing started at " +
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

  // closes processes log file
  private void closeLogFile()
  {
    // at end of program close log file
    try
    {
      GregorianCalendar gc = new GregorianCalendar();
      String now = su.DateToString(gc.getTime(), DTFMT);
      writeToLogFile("attachmentDeletion processing ended at " +
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



