package Batch;

import java.io.*;
import java.util.*;
import JavaUtil.*;
import DBUtilities.*;
import java.util.zip.*;

public class createEbillingUploadFiles
{

  // class variables
  private DBAccess dba;
  private StringUtil su;
  private createSAPUploadFile csuf;
  private String logDir, invDir, uploadDir, ignoreDir, runTS, logFilename, message, completionMessage;
  private BufferedWriter logWriter, workWriter, iViewWriter;
  private BufferedReader workReader;
  private String callMonth;
  private boolean ok;
  private long processSeqNo;
  // class constants
  private final String DTFMT = "yyyyMMddHHmmss";
  private final String PROCESSNAME = "Ebilling Upload";
  private final String YES = "Y";
  private final String NO = "N";
  private final String NOTSUPPLIED = "N/S";
  private final String UPDATEDBY = "createEbillingUploadFiles";
  private final String PROCESSPREFIX = "Upload";
  private final String COMMA = ",";
  private final String USCORE = "_";

  private createEbillingUploadFiles()
  {
    dba = new DBAccess();
    su = new StringUtil();
    csuf = new createSAPUploadFile();
    logDir = EBANProperties.getEBANProperty(EBANProperties.REVSHARELOGDIR);
    invDir = EBANProperties.getEBANProperty(EBANProperties.REVSHAREINVOICEDIR);
    uploadDir = EBANProperties.getEBANProperty(EBANProperties.REVSHAREUPLOADDIR);
    ignoreDir = EBANProperties.getEBANProperty(EBANProperties.REVSHAREIGNOREDIR);
  }

  private void control()
  {
    boolean hasRun = false;
    // open log file
    runTS = su.reformatDate("now", null, DTFMT);
    logFilename = logDir + File.separator + runTS + "_createEbillingUploadFiles_log.txt";
    String masterAccountNumber = "", netAmount = "", vatAmount = "", grossAmount = "";
    if (openLogFile())
    {
      // get current call month
      callMonth = dba.getCurrentCallMonth();
      // check for process row with status of requested
      if (dba.checkRequestProcessExists(callMonth,PROCESSNAME))
      {
        hasRun = true;
        int result = 0;
        // get process seq no
        processSeqNo = dba.getRequestProcessSeqNo(callMonth,PROCESSNAME);
        fullMessage("   Ebilling upload has started");
        // update process status to running
        ok = dba.setRequestProcessRunning(callMonth,processSeqNo,UPDATEDBY);
        // Check if there are invoices to upload
        long uploadCount = dba.getUploadCount(), failedCount = 0, ignoreCount = 0, successCount = 0;
        if (uploadCount==0)
        {
          ok = false;
          completionMessage="   No invoices due to be uploaded to ebilling";
        }
        else
          fullMessage("   "+uploadCount+" invoices identified to be uploaded");
        // Cleardown invoice report for the current call month
        if (ok)
        {
          fullMessage("   Clearing down Invoice Report table for "+callMonth);
          result = dba.cleardownInvoiceReport(callMonth);
          if (result!=0)
          {
            ok = false;
            completionMessage="   Failed to clear down invoice report : return value = "+result;
          }
        }
        // Identify files to upload and put into work file
        java.sql.ResultSet invoicesRS = dba.getInvoicesForUploading();
        String workFilename = uploadDir + File.separator + "work.txt";
        GregorianCalendar gc = new GregorianCalendar();  
        String now = su.DateToString(gc.getTime(), DTFMT);
        String dts = now.substring(0, 8) + USCORE + now.substring(8, 14);
        String iViewFilename = uploadDir + File.separator + "IView_Control"+USCORE+dts+".csv";
        if ((!openWorkFile(workFilename))||(!openIViewFile(iViewFilename)))
        {
          ok = false;
          completionMessage="   Failed to open work and/or IView file "+workFilename + " : " + iViewFilename;
        }
        else
            writeToIViewFile(
              "Master Account Number" + COMMA +
              "Invoice Number" + COMMA +
              "Net Amount" + COMMA +
              "Vat Amount" + COMMA +
              "Gross Amount" + COMMA +
              "CDR Filename");
        try
        {
          while ((invoicesRS.next())&&(ok))
          {
            String invoiceNumber = invoicesRS.getString("Invoice_Number");
            String pdfFilename = invoicesRS.getString("PDF_Filename");
            String csvFilename = invoicesRS.getString("CSV_Filename");
            String presentationInd = invoicesRS.getString("Presentation_Ind");
            masterAccountNumber = invoicesRS.getString("Master_Account_Number");
            netAmount = invoicesRS.getString("Net_Amount");
            vatAmount = invoicesRS.getString("Vat_Amount");
            grossAmount = invoicesRS.getString("Gross_Amount");
            String workLine = invoiceNumber+"|"+pdfFilename+"|"+csvFilename+"|"+presentationInd;
            if (!writeToWorkFile(workLine))
            {
              ok = false;
              completionMessage="   Failed to write to work file : "+workLine;
            }
            // Write to IView control if invoice is presented on ebilling
            if (presentationInd.startsWith("Y"))
            {
                String iViewLineStart = 
                      masterAccountNumber + COMMA +
                      invoiceNumber + COMMA +
                      netAmount + COMMA +
                      vatAmount + COMMA +
                      grossAmount + COMMA;
                if (csvFilename.startsWith(NOTSUPPLIED)) 
                    writeToIViewFile(iViewLineStart);
                else                   
                    writeToIViewFile(iViewLineStart+csvFilename);
            }
          }
        }
        catch(java.sql.SQLException ex)
        {
          ok = false;
          completionMessage="   Failed processing invoices to upload : "+ex.getMessage();
        }
        finally
        {
          try
          {
            invoicesRS.close();
            invoicesRS = null;
          }
          catch(java.sql.SQLException ex)
          {
            ok = false;
            completionMessage="   Failed to closed invoicesRS : "+ex.getMessage();
          }
        }
        if (!closeWorkFile())
        {
          ok = false;
          completionMessage="   Failed to close work file";
        }
        // Open work file and process each invoice record
        if (openWorkForRead(workFilename))
        {
          String workFileLine = readWorkFile();
          while (!(workFileLine==null))
          {
            String[] decode = new String[4];
            decode[0] = "";
            decode[1] = "";
            decode[2] = "";
            decode[3] = "";
            String testChar="";
            int pos = 0;
            for (int i = 0; i < workFileLine.length(); i++)
            {
              testChar = workFileLine.substring(i,i+1);
              if (testChar.equals("|"))
                pos++;
              else
                decode[pos]= decode[pos] + testChar;
            }
            String invoiceNumber = decode[0];
            String pdfFilename = decode[1];
            String csvFilename = decode[2];
            String presentationInd = decode[3];
            // Check that at least the PDF filename has been provided
            if (pdfFilename.equals(NOTSUPPLIED))
            {
              fullMessage("   PDF filename is missing for "+invoiceNumber);
              failedCount++;
            }
            else
            {
              // Check that the PDF file exists
              String pdfPath = invDir + File.separator + pdfFilename;
              File pdfFile = new File(pdfPath);
              if (pdfFile.exists())
              {
                // if there is also a CSV filename supplied check that it exists
                String csvPath = "";
                File csvFile = null;
                boolean doMoves = true;
                if (!(csvFilename.equals(NOTSUPPLIED)))
                {
                  csvPath = invDir + File.separator + csvFilename;
                  csvFile = new File(csvPath);
                  if (!csvFile.exists())
                  {
                    fullMessage("   CSV ZIP "+csvFilename+" for invoice " + invoiceNumber+" is missing");
                    failedCount++;
                    doMoves = false;
                  }
                }
                if (doMoves)
                {
                  // Determine new location for PDF file
                  String pdfPathNew = "";                          
                  File csvFileNew = null;
                  boolean updateInvoice = false;
                  if (presentationInd.equals("Y"))
                    pdfPathNew = uploadDir + File.separator + pdfFilename;
                  else
                    pdfPathNew = ignoreDir + File.separator + pdfFilename;
                  File pdfFileNew = new File(pdfPathNew);
                  if (pdfFile.renameTo(pdfFileNew))
                  {
                    if (presentationInd.equals("Y"))
                      successCount++;
                    else
                      ignoreCount++;
                    updateInvoice = true;
                    // Move backup CSV file if required
                    if (!(csvFilename.equals(NOTSUPPLIED)))
                    {
                      String csvPathNew = "";
                      if (presentationInd.equals("Y"))
                        csvPathNew = uploadDir + File.separator + csvFilename;
                      else
                        csvPathNew = ignoreDir + File.separator + csvFilename;
                      csvFileNew = new File(csvPathNew);
                      if (!csvFile.renameTo(csvFileNew))
                      {
                        fullMessage("   Failed to move CSV ZIP file to "+csvPathNew);
                        failedCount++;
                        updateInvoice = false;
                        // attempt to move PDF file back
                        if (!pdfFileNew.renameTo(pdfFile))
                          fullMessage("   Failed to move PDF file back to "+pdfPath+" after CSV move failure");
                      }
                    }
                  }
                  else
                  {
                    failedCount++;
                    fullMessage("   Failed to move PDF file to "+pdfPathNew);
                  }
                  if (updateInvoice)
                  {
                    if(!dba.updateInvoiceToUploaded(invoiceNumber,UPDATEDBY))
                    {
                      fullMessage("   Failed to update invoice "+invoiceNumber+" to uploaded");
                      // try to move files back if invoice update fails
                      failedCount++;
                      if (presentationInd.equals("Y"))
                        successCount--;
                      else
                        ignoreCount--;
                      if (!pdfFileNew.renameTo(pdfFile))
                        fullMessage("   Failed to move PDF file back to "+pdfPath+" after invoice update failure");
                      if (!(csvFilename.equals(NOTSUPPLIED)))
                          if (!csvFileNew.renameTo(csvFile))
                            fullMessage("   Failed to move CSV ZIP file back to "+csvPath+" after invoice update failure");
                    }
                  }
                }
              }
              else
              {
                fullMessage("   PDF "+pdfFilename+" for invoice " + invoiceNumber+" is missing");
                failedCount++;
              }
            }
            workFileLine = readWorkFile();
          }
          if ((closeWorkForRead())&&(closeIViewFile()))
          {
            // Delete work file
            File delFile = new File(workFilename);
            if (!delFile.delete())
            {
              fullMessage("   Failed to delete work file "+workFilename);
              fullMessage("   and/or failed to close IView file "+iViewFilename);
            }
          }
          else
          {
            ok = false;
            completionMessage="   Failed to close work file";
          }
        }
        else
        {
          ok = false;
          completionMessage="   Failed to read work file";
        }
        // Issue relevant invoice count messages
        if (failedCount>0)
          fullMessage("   "+failedCount+" invoices failed to be uploaded");
        if (ignoreCount>0)
          fullMessage("   "+ignoreCount+" invoices ignored for upload");
        if (successCount>0)
          fullMessage("   "+successCount+" invoices were moved to be uploaded");
        // Populate the invoice report for the current call month
        if (ok)
        {
          fullMessage("   Populating the Invoice Report table");
          result = dba.populateInvoiceReport(callMonth,UPDATEDBY);
          if (result!=0)
          {
            ok = false;
            completionMessage="   Failed to populate invoice report : return value = "+result;
          }
        }
        // Create SAP Journal file
        if (ok)
        {
          fullMessage("   Determine if there are any invoices to go to SAP");
          result = csuf.makeFile(callMonth,UPDATEDBY);
          if (result<0)
          {
            ok = false;
            completionMessage="   Failed to create SAP Upload File : return value = "+result;
          }
          else if (result==0)
            fullMessage("   No invoices due for SAP so no SAP upload file created");
          else
            fullMessage("   SAP upload file created for "+result+" invoice(s)");
        }
        // update process row and process control for completion
        if (ok)
        {
          completionMessage = "   Ebilling upload completed";
          ok = dba.setRequestProcessCompleted(callMonth,PROCESSPREFIX,processSeqNo,YES,UPDATEDBY);
          if (!ok)
            completionMessage = "   Failed to set process for successful completion : "+completionMessage;
        }
        else
        {
          ok = dba.setRequestProcessCompleted(callMonth,PROCESSPREFIX,processSeqNo,NO,UPDATEDBY);
          if (!ok)
            completionMessage = "  Failed to set process to failed completion : "+completionMessage;
        }
        // output completion message
        fullMessage(completionMessage);
      }
      // close logfile (and delete if no processing has occurred)
      if (closeLogFile())
      {
        if (!hasRun)
        {
          // delete logfile if no request to run outstanding
          File lFile = new File(logFilename);
          if (!lFile.delete())
            System.out.println("Failed to delete empty logfile");
        }
      }
    }
  }

  // output message to batch job, log file and database
  private void fullMessage(String messageText)
  {
    writeToLogFile(messageText);
    System.out.println(messageText);
    String shorterMessage = messageText.substring(3,messageText.length());
    dba.insertProcessMessage(callMonth,processSeqNo,shorterMessage,UPDATEDBY);
  }

  private boolean openLogFile()
  {
    boolean result = false;
    // open a new log file
    try
    {
      GregorianCalendar gc = new GregorianCalendar();
      logWriter = new BufferedWriter(new FileWriter(new File(logFilename)));
      String now = su.DateToString(gc.getTime(), DTFMT);
      if (writeToLogFile("createEbillingUploadFiles processing started at " +
            now.substring(8, 10) + ":" + now.substring(10, 12) + "." +
            now.substring(12, 14) + " on " + now.substring(6, 8) + "/" +
            now.substring(4, 6) + "/" + now.substring(0, 4)))
        result = true;
    }
    catch (Exception ex)
    {
      System.out.println("Error opening log file "+logFilename+" : "+ex.getMessage());
    }
    return result;
  }

  private boolean openWorkFile(String workFilename)
  {
    boolean result = false;
    // open a work file
    try
    {
      workWriter = new BufferedWriter(new FileWriter(new File(workFilename)));
      result = true;
    }
    catch (Exception ex)
    {
      System.out.println("Error opening work file "+workFilename+" : "+ex.getMessage());
    }
    return result;
  }

  private boolean openIViewFile(String iViewFilename)
  {
    boolean result = false;
    // open a work file
    try
    {
      iViewWriter = new BufferedWriter(new FileWriter(new File(iViewFilename)));
      result = true;
    }
    catch (Exception ex)
    {
      System.out.println("Error opening iView file "+iViewFilename+" : "+ex.getMessage());
    }
    return result;
  }

  private boolean openWorkForRead(String workFilename)
  {
    boolean result = false;
    // open a work file
    try
    {
      workReader = new BufferedReader(new FileReader(new File(workFilename)));
      result = true;
    }
    catch (Exception ex)
    {
      System.out.println("Error opening work file for reading "+workFilename+" : "+ex.getMessage());
    }
    return result;
  }

  private boolean writeToLogFile(String message)
  {
    boolean result = false;
    // write supplied text to program log file
    try
    {
      logWriter.write(message);
      logWriter.newLine();
      result = true;
    }
    catch (Exception ex)
    {
      System.out.println("Error writing message '"+message+"' to log file: "+ex.getMessage());
    }
    return result;
  }

  private boolean writeToWorkFile(String workLine)
  {
    boolean result = false;
    // write supplied text to work file
    try
    {
      workWriter.write(workLine);
      workWriter.newLine();
      result = true;
    }
    catch (Exception ex)
    {
      System.out.println("Error writing line '"+workLine+"' to work file: "+ex.getMessage());
    }
    return result;
  }

  private boolean writeToIViewFile(String iViewLine)
  {
    boolean result = false;
    // write supplied text to work file
    try
    {
      iViewWriter.write(iViewLine);
      iViewWriter.newLine();
      result = true;
    }
    catch (Exception ex)
    {
      System.out.println("Error writing line '"+iViewLine+"' to IView file: "+ex.getMessage());
    }
    return result;
  }

  private String readWorkFile()
  {
    String workLine = "";
    // read line on work file
    try
    {
      workLine = workReader.readLine();
    }
    catch (Exception ex)
    {
      System.out.println("Error reading line on work file: "+ex.getMessage());
    }
    return workLine;
  }

  private boolean closeLogFile()
  {
    boolean result = false;
    // at end of program close log file
    try
    {
      GregorianCalendar gc = new GregorianCalendar();
      String now = su.DateToString(gc.getTime(), DTFMT);
      if (writeToLogFile("createEbillingUploadFiles processing ended at " +
            now.substring(8, 10) + ":" + now.substring(10, 12) + "." +
            now.substring(12, 14) + " on " + now.substring(6, 8) + "/" +
            now.substring(4, 6) + "/" + now.substring(0, 4)))
      {
        logWriter.close();
        result = true;
      }
    }
    catch (Exception ex)
    {
      System.out.println("Error closing log file: " + ex.getMessage());
    }
    return result;
  }

  private boolean closeWorkFile()
  {
    boolean result = false;
    // close work file
    try
    {
      workWriter.close();
      result = true;
    }
    catch (Exception ex)
    {
      System.out.println("Error closing work file : " + ex.getMessage());
    }
    return result;
  }

  private boolean closeIViewFile()
  {
    boolean result = false;
    // close iView file
    try
    {
      iViewWriter.close();
      result = true;
    }
    catch (Exception ex)
    {
      System.out.println("Error closing IView file : " + ex.getMessage());
    }
    return result;
  }

  private boolean closeWorkForRead()
  {
    boolean result = false;
    // close work file
    try
    {
      workReader.close();
      result = true;
    }
    catch (Exception ex)
    {
      System.out.println("Error closing work file for read : " + ex.getMessage());
    }
    return result;
  }

  public static void main(String[] args)
  {
    createEbillingUploadFiles cEUF = new createEbillingUploadFiles();
    cEUF.control();
  }

}



