package Batch;

import java.io.*;
import java.util.*;
import JavaUtil.*;
import DBUtilities.*;

public class nonGCBInvoiceDeletion
{
  // class variables
  private String logDir, attachDir, attach2Dir,attachArchiveDir, destinationFolder, months, reportArchiveDir;
  private File attachFolder, attach2Folder, attachArchiveFolder, reportArchiveFolder;
  private int numberMonths;
  private DBAccess dba;
  private StringUtil su;
  private String runTS, logMessage;
  private BufferedWriter logWriter;
  private File logFile;
  private String topLevelFolder, shortFilename;
  private int candidateCount, attachmentDeletionCount, invoiceRemovalCount;

  // class constants
  private final String DTFMT = "yyyyMMddHHmmss";
  private final String SSBS = "SSBS";
  private final String EBILLZ = "ebillz";
  private final String ARBOR = "Arbor";
  private final String REPORT = "Report";
  private final String DELETE = "Delete";
  private final String ARCHIVE = "archive";
  private final String FSLASH = "/";
  private final String BSLASH = "\\";
  private final String BE = "BE";
  private final String BV = "BV";
  private final String CONGLOM = "CONGLOM";
  private final String CPS = "CPS";
  private final String DATA = "Data";
  private final String MOBI = "MOBI";
  private final String MS = "MS";
  private final String RAM = "RAM";
  private final String USCORE = "_";
  private final String ENERGISINVOICE = "Energis_Invoice";
  private final String ARBOR_D = "Arbor_Invoice_D.pdf";
  private final String ARBOR_S = "Arbor_Invoice_S.pdf";
  private final String PDFEXT = ".pdf";

  private nonGCBInvoiceDeletion()
  {
    dba = new DBAccess();
    su = new StringUtil();
    // get parameters from properties files
    logDir = EBANProperties.getEBANProperty(EBANProperties.NGIDLOGDIR);
    attachDir = EBANProperties.getEBANProperty(EBANProperties.NGIDATTACHDIR);
    attach2Dir = EBANProperties.getEBANProperty(EBANProperties.NGIDATTACH2DIR);
    attachArchiveDir = EBANProperties.getEBANProperty(EBANProperties.NGIDATTACHARCHIVEDIR);
    destinationFolder = EBANProperties.getEBANProperty(EBANProperties.NGIDDESTPATH);
    months=EBANProperties.getEBANProperty(EBANProperties.NGIDNOMONTHS);
    reportArchiveDir = EBANProperties.getEBANProperty(EBANProperties.NGIDREPORTARCHIVE);
  }

  public static void main(String[] args)
  {
    nonGCBInvoiceDeletion ngid = new nonGCBInvoiceDeletion();
    ngid.control();
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
      attachFolder = new File(attachDir);

      if (!attachFolder.exists())
      {
        parametersOK = false;
        logMessage = "   ERROR - cannot proceed - attachment directory "+attachDir+" not found";
        writeToLogFile(logMessage);
      }
      attach2Folder = new File(attach2Dir);
      if (!attach2Folder.exists())
      {
        parametersOK = false;
        logMessage = "   ERROR - cannot proceed - attachment2 directory "+attach2Dir+" not found";
        writeToLogFile(logMessage);
      }
      attachArchiveFolder = new File(attachArchiveDir);
      if (!attachArchiveFolder.exists())
      {
        parametersOK = false;
        logMessage = "   ERROR - cannot proceed - attachment archive directory "+attachArchiveDir+" not found";
        writeToLogFile(logMessage);
      }
      reportArchiveFolder = new File(reportArchiveDir);
      if (!reportArchiveFolder.exists())
      {
        parametersOK = false;
        logMessage = "   ERROR - cannot proceed - report archive directory "+reportArchiveDir+" not found";
        writeToLogFile(logMessage);
      }
      numberMonths=Integer.parseInt(months);
      if (numberMonths<1)
      {
        logMessage = "   CANNOT PROCEED - Invalid number of months value of  "+months;
        writeToLogFile(logMessage);
        parametersOK = false;
      }
    }
    else
    {
      parametersOK = false;
      System.out.println("   ERROR - Cannot proceed - log directory "+logDir+" not found");
    }
    // continue if parameter validation is completed
    if (parametersOK)
    {
      candidateCount = 0;
      attachmentDeletionCount = 0;
      invoiceRemovalCount = 0;
      java.sql.ResultSet candidateInvoicesRS = dba.getNonGCBCandidateInvoices(numberMonths);
      try
      {
        while (candidateInvoicesRS.next())
        {
          candidateCount++;
          long invoiceId = candidateInvoicesRS.getLong("Invoice_Id");
          String accountNumber = candidateInvoicesRS.getString("Account_Number");
          String billingSource = candidateInvoicesRS.getString("Billing_Source");
          boolean noAttachments = true, failedToDeleteAllAttachments = false;
          // identify invoice attachments
          java.sql.ResultSet invoiceAttachmentsRS = dba.getInvoiceAttachments(invoiceId);
          try
          {
            while (invoiceAttachmentsRS.next())
            {
              noAttachments = false;
              String location = invoiceAttachmentsRS.getString("Location");
              String originalLocation = invoiceAttachmentsRS.getString("Original_Location");
              String itemType = invoiceAttachmentsRS.getString("Item_Type");
              String name = invoiceAttachmentsRS.getString("Name");
              // check that attachment exists (if not ignore and still delete DB record)
              // if it does exist then move attachment
              String currentPath = "";
              if (location.startsWith(ARCHIVE))
                currentPath = attachArchiveDir + File.separator + location;
              else if (billingSource.startsWith(SSBS))
                currentPath = attach2Dir + File.separator + location;
              else
                currentPath = attachDir + File.separator + location;
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
                    failedToDeleteAllAttachments = true;
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
                        String locationTest = originalLocation;
                        if (originalLocation.startsWith(ARCHIVE))
                          locationTest = originalLocation.substring(8,originalLocation.length());
                        String newname = newFilename(billingSource,locationTest);
                        emptyDirectoryCheck(currentPath);
                        attachmentDeletionCount++;
                      }
                      else
                      {
                        logMessage =
                          "    Could not move attachment from location = "+originalLocation+
                          " to report archive";
                        writeToLogFile(logMessage);
                        failedToDeleteAllAttachments = true;
                      }
                    }
                    else
                    {
                      logMessage = "   Could not create report archive file "+newPath;
                      writeToLogFile(logMessage);
                      failedToDeleteAllAttachments = true;
                    }
                  }
                }
                else
                {
                  // make account path if it does not exists
                  String locationTest = originalLocation;
                  if (location.startsWith(ARCHIVE))
                    locationTest = originalLocation.substring(8,originalLocation.length());
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
                    failedToDeleteAllAttachments = true;
                  }
                  else
                  {
                    if (currentLoc.renameTo(newLoc))
                    {
                      if (dba.deleteAttachment(originalLocation))
                      {
                        emptyDirectoryCheck(currentPath);
                        attachmentDeletionCount++;
                      }
                      else
                      {
                        logMessage =
                          "    Could not delete attachment where location = "+originalLocation+
                          " although new file "+newPath+" has been created";
                        writeToLogFile(logMessage);
                        failedToDeleteAllAttachments = true;
                      }
                    }
                    else
                    {
                      logMessage = "   Could not create archived attachment file "+newPath;
                      writeToLogFile(logMessage);
                      failedToDeleteAllAttachments = true;
                    }
                  }
                }
              }
            }
            invoiceAttachmentsRS.close();
          }
          catch(java.sql.SQLException e)
          {
            logMessage = "   ERROR - Identifying attachments for invoice (id = "+invoiceId+" : "+e.getMessage();
            writeToLogFile(logMessage);
            failedToDeleteAllAttachments=true;
            noAttachments = false;
          }
          // set invoice to be deleted if it had no attachments or all it's attachments have been deleted
          if ((noAttachments)||((!noAttachments)&&(!failedToDeleteAllAttachments)))
          {
            if (dba.removeInvoice(invoiceId))
              invoiceRemovalCount++;
            else
            {
              logMessage = "  Failed to set invoice (id = "+invoiceId+" for removal";
              writeToLogFile(logMessage);
            }
          }
        }
        candidateInvoicesRS.close();
      }
      catch(java.sql.SQLException e)
      {
        logMessage = "   ERROR - Identifying candidate invoices : "+e.getMessage();
        writeToLogFile(logMessage);
      }
    }
    writeToLogFile("   ");
    writeToLogFile("   Number of invoice identified for deletion : "+candidateCount);
    writeToLogFile("   Number of invoice attachments deleted     : "+attachmentDeletionCount);
    writeToLogFile("   Number of invoice set for removal         : "+invoiceRemovalCount);
    writeToLogFile("   ");
    if (logging)
      closeLogFile();
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

  // opens process log file for writing
  private boolean openLogFile()
  {
    boolean result = false;
    // open a new log file
    try
    {
      GregorianCalendar gc = new GregorianCalendar();
      runTS = su.reformatDate("now", null, DTFMT);
      logFile = new File(logDir + File.separator + runTS + "_nonGCBInvoiceDeletion_log.txt");
      logWriter = new BufferedWriter(new FileWriter(logFile));
      String now = su.DateToString(gc.getTime(), DTFMT);
      writeToLogFile("nonGCBInvoiceDeletion processing started at " +
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
      writeToLogFile("nonGCBInvoiceDeletion processing ended at " +
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



