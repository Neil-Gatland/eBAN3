package Batch;

import java.io.*;
import java.sql.*;
import java.util.*;
import JavaUtil.*;
import DBUtilities.*;

public class processRevShareFiles
{

  // class variables
  private String runTS, logMessage;
  private StringUtil su;
  private DBAccess dba;
  private String logDir, dropDir, procDir, rejDir, attachDir;
  private BufferedWriter logWriter;
  private File logFile;
  private boolean noControlFiles;
  // class constants
  private final String DTFMT = "yyyyMMddHHmmss";
  private final String cfEXT = "ftp_control.txt";
  private final String USCORE = "_";
  private final String PERIOD = ".";
  private final String PDF = "pdf";
  private final String ZIP = "zip";
  private final String REVSHARE = "RevShare";
  private final String FWDSLASH = "/";

  private void controlProcessing()
  {
    // assume no control files to process
    noControlFiles = true;
    // open log file
    if (openLogFile())
    {
      // process control files in the drop directory
      processControlFiles();
      // delete log file if no control files processed
      if (closeLogFile())
        if (noControlFiles)
          if (!logFile.delete())
            System.out.println("Failed to delete log file on empty run");
    }
  }

  private void processControlFiles()
  {
    boolean ok = true;
    // load name of any control files into a string array
    // and process the control files
    File dropDirectory = new File(dropDir);
    FilenameFilter controlFilter = new FilenameFilter()
    {
      public boolean accept(File dir, String name)
      {
        return name.endsWith(cfEXT);
      }
    };
    File[] controlFileArray = dropDirectory.listFiles(controlFilter);
    for (int i = 0; i < controlFileArray.length; i++)
    {
      noControlFiles = false;
      String controlFileName = controlFileArray[i].getName();
      if (!logAndOutput("Processing control file "+controlFileName))
        ok = false;
      // open control file and process each file listed in it
      if (ok)
      {
        File controlFile = new File(dropDir + File.separator + controlFileName);
        long noFiles = 0, missingFiles = 0, invalidTypes = 0, missingAccounts = 0, failedInvoices = 0;
        long failedPDFs = 0, loadedPDFs = 0, failedZIPs = 0, loadedZIPs = 0;
        try
        {
          BufferedReader controlBR = new BufferedReader(new FileReader(controlFile));
          String controlFileLine = controlBR.readLine();
          while ((controlFileLine!=null)&&(ok))
          {
            // check that the file exists in the drop directory
            File invoiceFile = new File(dropDir + File.separator + controlFileLine.trim());
            if (invoiceFile.exists())
            {
              // unpack file details from the file name
              String [] filenameParts = getFileDetails(controlFileLine);
              String invoiceNumber = filenameParts[0];
              String taxPointDate = filenameParts[1];
              String invoiceTotal = filenameParts[2];
              String fileType = filenameParts[3];
              // check that file type is valid
              if ((fileType.equals(PDF))||(fileType.equals(ZIP)))
              {
                // check that the account exists
                String accountNumber = invoiceNumber.substring(0,7);
                String type = invoiceNumber.substring(7,9);
                if (accountNumber.startsWith("T"))   // account number will be 8 characters if a test account starting with T
                {
                  accountNumber = invoiceNumber.substring(0,8);
                  type = invoiceNumber.substring(8,10);
                }
                long accountId = dba.getRevShareAccountId(accountNumber);
                if (accountId>0)
                {
                  // create invoice (or update if it already exists)
                  long invoiceId = dba.insertRevShareInvoice(invoiceNumber,taxPointDate,invoiceTotal,accountId,type);
                  if (invoiceId>0)
                  {
                    // move file to attachment location
                    String attachmentDirectory =
                      attachDir + File.separator +
                      REVSHARE + File.separator +
                      accountNumber + File.separator +
                      taxPointDate;
                    String attachmentPath =
                      attachmentDirectory + File.separator+
                      controlFileLine.trim();
                    // if the attachment already exists then delete it
                    boolean success = true;
                    File attachment = new File(attachmentPath);
                    if (attachment.exists())
                      if (!attachment.delete())
                      {
                        logAndOutput("Unable to add delete existing attachment at "+attachmentPath);
                        success = false;
                      }
                    // check if the attachment directory exists
                    if (success)
                    {
                      File attachmentDir = new File(attachmentDirectory);
                      if (!attachmentDir.exists())
                        if (!attachmentDir.mkdirs())
                        {
                          logAndOutput("Unable to create directory "+attachmentDirectory);
                          success = false;
                        }
                    }
                    // move the file to the attachment directory
                    if (success)
                      if (!invoiceFile.renameTo(attachment))
                      {
                        logAndOutput("Unable to create file "+controlFileLine.trim()+" in "+attachmentDirectory);
                        success = false;
                      }
                    // create the attachment record (or update if it already exists)
                    if (success)
                    {
                      String location =
                        REVSHARE + FWDSLASH +
                        accountNumber + FWDSLASH +
                        taxPointDate + FWDSLASH +
                        controlFileLine.trim();
                      long result = dba.insertRevShareAttachment(location,fileType,invoiceId,taxPointDate);
                      if (result!=0)
                      {
                        logAndOutput("Unable to insert attachment for file "+controlFileLine.trim()+" : return value "+result);
                        success = false;
                      }
                    }
                    // update relevant count total
                    if (success)
                    {
                      if (fileType.equals(PDF))
                        loadedPDFs++;
                      else
                        loadedZIPs++;
                    }
                    else
                    {
                      if (fileType.equals(PDF))
                        failedPDFs++;
                      else
                        failedZIPs++;
                    }

                  }
                  else
                  {
                    logAndOutput("Unable to add invoice "+invoiceNumber+" for file "+controlFileLine.trim()+" : return value "+invoiceId);
                    failedInvoices++;
                  }
                }
                else
                {
                  logAndOutput("Account "+accountNumber+" is missing for file "+controlFileLine.trim());
                  missingAccounts++;
                }
              }
              else
              {
                logAndOutput("File "+controlFileLine.trim() +" is not valid. It must be either a PDF or ZIP file.");
                invalidTypes++;
              }

            }
            else
            {
              logAndOutput("File "+controlFileLine.trim() +" is missing");
              missingFiles++;
            }
            noFiles++;
            controlFileLine = controlBR.readLine();
          }
          controlBR.close();
        }
        catch(java.io.IOException ex)
        {
          logAndOutput("Failed to process control file : "+ex.getMessage());
          ok = false;
        }
        // report on processed files
        if (ok)
        {
          if (!logAndOutput("   No. files processed            : "+noFiles))
            ok = false;
          else
          {
            if (missingFiles>0)
              if (!logAndOutput("   No. missing file               : "+missingFiles))
                ok = false;
            if ((ok)&&(invalidTypes>0))
              if (!logAndOutput("   No. invalid files              : "+invalidTypes))
                ok = false;
            if ((ok)&&(missingAccounts>0))
              if (!logAndOutput("   No. with missing accounts      : "+missingAccounts))
                ok = false;
            if ((ok)&&(failedInvoices>0))
              if (!logAndOutput("   No. invoice creation failures  : "+failedInvoices))
                ok = false;
            if (ok)
              if (!logAndOutput("   No. loaded PDFs                : "+loadedPDFs))
                ok = false;
            if ((ok)&&(failedPDFs>0))
              if (!logAndOutput("   No. failed PDFs                : "+failedPDFs))
                ok = false;
            if (ok)
              if (!logAndOutput("   No. loaded ZIPs                : "+loadedZIPs))
                ok = false;
            if ((ok)&&(failedZIPs>0))
              if (!logAndOutput("   No. failed PDFs                : "+failedZIPs))
                ok = false;
          }
        }
        // move control file to the processed directory
        File processedControlFile = new File(procDir + File.separator + controlFileName);
        if (!controlFile.renameTo(processedControlFile))
        {
          logAndOutput("Failed to move control file : "+controlFileName + " to processed directory "+procDir);
          ok = false;
        }
      }
    }
  }

  // breaks up file name into it's component parts
  private String[] getFileDetails(String filename)
  {
    String[] fileDetails = new String[4];
    String testChar = "", workString = "";
    int partPos = 0;
    for (int i=0; i < filename.length(); i++)
    {
      testChar = filename.substring(i,i+1);
      if ( (testChar.equals(USCORE)) || (testChar.equals(PERIOD)) )
      {
        fileDetails[partPos] = workString;
        workString = "";
        partPos++;
      }
      else
        workString = workString + testChar;
    }
    fileDetails[partPos] = workString;
    return fileDetails;
  }


  private processRevShareFiles()
  {
    su = new StringUtil();
    dba = new DBAccess();
    // get parameters from properties files
    logDir=EBANProperties.getEBANProperty(EBANProperties.PROCESSRSFILESLOGDIR);
    dropDir=EBANProperties.getEBANProperty(EBANProperties.PROCESSRSFILESDROPDIR);
    procDir=EBANProperties.getEBANProperty(EBANProperties.PROCESSRSFILESPROCDIR);
    attachDir=EBANProperties.getEBANProperty(EBANProperties.PROCESSRSFILESATTACHDIR);
  }

  // open log file
  private boolean openLogFile()
  {
    boolean result = false;
    try
    {
      GregorianCalendar gc = new GregorianCalendar();
      runTS = su.reformatDate("now", null, DTFMT);
      logFile = new File(logDir + File.separator + runTS + "_processRevShareFiles_log.txt");
      logWriter = new BufferedWriter(new FileWriter(logFile));
      String now = su.DateToString(gc.getTime(), DTFMT);
      writeToLogFile("processRevShareFiles started at " +
        now.substring(8, 10) + ":" + now.substring(10, 12) + "." +
        now.substring(12, 14) + " on " + now.substring(6, 8) + "/" +
        now.substring(4, 6) + "/" + now.substring(0, 4));
      writeToLogFile(" ");
      result = true;
    }
    catch (Exception ex)
    {
      System.out.println("Error opening log file: " + ex.getMessage());
      System.exit(1);
    }
    return result;
  }

  // at end of program close log file
  private boolean closeLogFile()
  {
    boolean result = false;
    try
    {
      GregorianCalendar gc = new GregorianCalendar();
      String now = su.DateToString(gc.getTime(), DTFMT);
      writeToLogFile(" ");
      writeToLogFile("processRevShareFiles ended at " +
        now.substring(8, 10) + ":" + now.substring(10, 12) + "." +
        now.substring(12, 14) + " on " + now.substring(6, 8) + "/" +
        now.substring(4, 6) + "/" + now.substring(0, 4));
      logWriter.close();
      result = true;
    }
    catch (Exception ex)
    {
      System.out.println("Error closing log file: " + ex.getMessage());
    }
    return result;
  }

  // write supplied text to program log file
  private boolean writeToLogFile(String message)
  {
    boolean result = false;
    try
    {
      logWriter.write(message);
      logWriter.newLine();
      result = true;
    }
    catch (Exception ex)
    {
      System.out.println("Error writing message '" + message + "' to log file : " + ex.getMessage());
    }
    return result;
  }

  // send message to log and standard output
  private boolean logAndOutput(String message)
  {
    boolean result = false;
    System.out.println(message);
    if (writeToLogFile(message))
      result = true;
    return result;
  }

  // initial program control
  public static void main(String[] args)
  {
    // control processing
    processRevShareFiles prsf = new processRevShareFiles();
    prsf.controlProcessing();
  }

}



