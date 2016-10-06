package Batch;

import java.io.*;
import java.util.*;
import JavaUtil.*;
import DBUtilities.*;

public class splitPCBImpactFile
{

  // class variables
  private DBAccess dba;
  private String dropDir, logDir, splitDir, splitDirSSBS, procDir, runTS, splitFileName;
  private StringUtil su;
  private BufferedWriter logWriter, splitWriter;
  private File logFile;
  private boolean splitFileStart;
  private String currentKey, currentCC, currentData;
  private String previousCC, previousData;
  private String buffer;
  private int lineCount, splitCount,contentLineCount, pageNo;
  private boolean bufferInUse, countingContent;
  private boolean firstLine, invoice;
  private String totalCharges, tpd, totalStart, savedLine, billingType;
  // class constants
  private final String PCB = "PCB";
  private final String SPIF = "Split PCB Impact Files";
  private final String LPI = "Load PCB Invoices";
  private final String NOTAVAILABLE = "Not available";
  private final String ACTIVE = "Active";
  private final String INACTIVE = "Inactive";
  private final String DTFMT = "yyyyMMddHHmmss";
  private final String USCORE = "_";
  private final String INVOICE = "INVOICE";
  private final String OTHERS = "OTHERS";
  private final String TXT = ".txt";
  private final String BACKSLASH = "\\";
  private final String FORMFEED = "\f";
  private final String NEWPAGE = "1";
  private final String TOTALREFUND = "TOTAL REFUND";
  private final String MINUSSIGN = "-";
  private final String PREVBLANK1 = "-";
  private final String PREVBLANK2 = "0";
  
  private splitPCBImpactFile()
  {
    su = new StringUtil();
    dba = new DBAccess();
    dropDir = EBANProperties.getEBANProperty(EBANProperties.SPIDROPDIR);
    logDir = EBANProperties.getEBANProperty(EBANProperties.SPILOGDIR);
    splitDir = EBANProperties.getEBANProperty(EBANProperties.SPISPLITDIR);
    splitDirSSBS = EBANProperties.getEBANProperty(EBANProperties.SPSISPLITDIR);
    procDir = EBANProperties.getEBANProperty(EBANProperties.SPIPROCDIR);
  }

  private boolean checkRunControl()
  {
    boolean result = false;
    String status = dba.getRunControlStatus(PCB,SPIF), message="";
    if (status.startsWith(NOTAVAILABLE))
    {
      message = "!!!Expected job control row for this process is missing!!!";
      writeToLogFile(message);
      System.out.println(message);
    }
    else
    {
      if (status.startsWith(ACTIVE))
      {
        message = "!!!this process is already running!!!";
        writeToLogFile(message);
        System.out.println(message);
      }
      else
      {
        status = dba.getRunControlStatus(PCB,LPI);
        if (!status.startsWith(INACTIVE))
        {
          message = "!!!cannot run while load PCB invoice process is running!!!";
          writeToLogFile(message);
          System.out.println(message);
        }
        else
        {
          if (dba.updateRunControlStatus(PCB,SPIF,ACTIVE))
          {
            result = true;
          }
          else
          {
            message = "!!!cannot set run control for this process!!!";
            writeToLogFile(message);
            System.out.println(message);
          }
        }
      }
    }
    return result;
  }

  private void resetRunControl()
  {
    if (!dba.updateRunControlStatus(PCB,SPIF,INACTIVE))
    {
      String message = "!!!failed to rest run control for this process to inactive!!!";
      writeToLogFile(message);
      System.out.println(message);
    }
  }

  private void processFiles()
  {
    writeToLogFile(" ");
    int fileCount = 0;
    try
    {
      // process impact files in the drop directory
      File directory = new File(dropDir);
      File[] fileArray = directory.listFiles();
      for (int i = 0; i < fileArray.length; i++)
      {
        File impactFile = fileArray[i];
        String filename = impactFile.getAbsolutePath();
        splitFile(filename);
        File ifMove = new File(procDir+BACKSLASH+impactFile.getName());
        if (!impactFile.renameTo(ifMove))
          writeToLogFile("failed to move impact file: "+filename);
        fileCount++;
      }
      // summarise impact files processed in log file
      switch (fileCount)
      {
        case 0 : writeToLogFile("No impact files in drop directory");
        break;
        case 1 : writeToLogFile("1 impact file processed");
        break;
        default : writeToLogFile(fileCount+" impact files processed");
      }
      writeToLogFile(" ");
    }
    catch(Exception ex)
    {
      writeToLogFile("Error in processFiles : " + ex.getMessage());
    }
  }

  private void splitFile(String filename)
  {
    // split up an impact file into separate files for each invoice
    writeToLogFile("   Spliting file "+filename);
    String compareKey = "XXXXXXXXXXXXXXXXXXXXXXXX";
    currentKey = "";
    splitCount = 0;
    try
    {
      //FileReader fr = new FileReader(filename);
      BufferedReader br = new BufferedReader(new FileReader(filename));
      String line = br.readLine();
      String tempLine = "";
      String reportType = "";
      int len = 0, reduce = 0;
      firstLine = true;
      boolean eof = false, beforeCCC = false;
      if (line==null)
        eof = true;
      else
      {
        eof = false;
        len = line.length();
        if (len>0)
          currentKey = line.substring(0,24);
      }
      while (!eof)
      {
        lineCount++;
        // ignore zero length line
        if (len>0)
        {
          // spot either first or second split line
          if (len<171)
          {
            // first split line
            if (firstLine)
            {
              if (len<75)
              {
                beforeCCC = true;
                tempLine = "";
                reportType = line.substring(61,63);
                reduce = 73 - len;
              }
              else
              {
                beforeCCC = false;
                tempLine = line.substring(74,len);
              }
              firstLine = false;
            }
            // second split line
            else
            {
              if (beforeCCC)
              {
                tempLine = line.substring(reduce,len);
              }
              else
              {
                tempLine = tempLine + line;
              }
              parseLine(tempLine,reportType);
              firstLine = true;
            }
          }
          else
          // otherwise not a split line
          {
            // start of a new split file
            if (!compareKey.equals(currentKey))
            {
              if (compareKey!="XXXXXXXXXXXXXXXXXXXXXXXX")
              {
                parseLine("                 ",line.substring(61,63));
                closeSplitFile();
              }
              compareKey = currentKey;
              if (ssbsCredit(currentKey.substring(16,24)))
              {    
                  splitFileName = 
                    splitDirSSBS  + BACKSLASH +
                    currentKey.substring(0,8)+USCORE+
                    currentKey.substring(8,16)+USCORE+
                    currentKey.substring(16,24)+USCORE+
                    INVOICE+TXT;
                  billingType = "SSBS"; 
              }
              else
              {
                splitFileName =
                    splitDir + BACKSLASH +
                    currentKey.substring(0,8)+USCORE+
                    currentKey.substring(8,16)+USCORE+
                    currentKey.substring(16,24)+USCORE+
                    INVOICE+TXT;
                billingType = "PCB";
              }
              //storedAccountNumber = currentKey.substring(0,8);
              openSplitFile();
              splitCount++;
              firstLine = true;
              splitFileStart = true;
              bufferInUse = false;
              totalCharges = "000";
              tpd = currentKey.substring(8,16);
              invoice = true;
              pageNo = 1;
              savedLine ="";                    
              countingContent = false;
              contentLineCount = 0;
            }
            parseLine(line.substring(74,171),line.substring(61,63));
          }
        }
        line = br.readLine();
        //System.out.println(lineCount+" "+line.length());
        if (line==null)
        {
          eof = true;
        }
        else
        {
          len = line.length();
          if (len>0)
            currentKey = line.substring(0,24);
        }
      }
      // close last split file at the end of the impact file
      if (compareKey!="XXXXXXXXXXXXXXXXXXXXXXXX")
        closeSplitFile();
      br.close();
      writeToLogFile("   file "+filename+" successfully split into "+splitCount+" files");
      writeToLogFile(" ");
    }
    catch(Exception ex)
    {
      writeToLogFile("   Error splitting "+filename+" : "+ex.getMessage());
      System.out.println("Error splitting "+filename+" : "+ex.getMessage());
    }
  }

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
        "' to log file: " + ex.getMessage());
      System.exit(1);
    }
  }

  private void openSplitFile()
  {
    // open a new split file
    try
    {
      splitWriter = new BufferedWriter(new FileWriter(splitFileName));
    }
    catch (Exception ex)
    {
      System.out.println("Error opening split file: "+splitFileName+" : "+ex.getMessage());
      System.exit(1);
    }
  }
  
  private boolean ssbsCredit(String invoiceNo)
  {
      // determine if invoice no is for an SSBS credit note
      boolean result = false;
      if ((invoiceNo.startsWith("1")&&(invoiceNo.endsWith("C"))))
          result = true;
      return result;
  }

  private String invoiceTotal(String totalLine)
  {
    // determine invoice total as an integer string
    String result = "";
    String test = totalLine.trim();
    boolean spaceFound = false;
    for(int i=test.length()-1;!spaceFound;i--)
    {
      if (Character.isDigit(test.charAt(i)))
        result = test.charAt(i) + result;
      else if (Character.isSpaceChar(test.charAt(i)))
        spaceFound = true;
    }
    return result;
  }
    
  private String fixedFormatInvoiceTotal(String total)
  {
      // format total as a fixed -ZZZ,ZZZ,ZZ9.99 string right justified
      String result = "";
      boolean negative = false;
      // always take last three characters with inserted decimal place      
      int origLength = total.length();
      result = total.substring(origLength-3,origLength-2)+"."+total.substring(origLength-2,origLength);
      // cater for minus amounts
      if (total.startsWith(MINUSSIGN))
      {
          total = total.substring(1,origLength);          
          origLength = origLength -1;
          negative = true;
      }
          
      if (origLength>3)
      {
          int intPartCount = 1;
          for (int scanPos=origLength-4;scanPos>=0;scanPos--)
          {
              if ((intPartCount==3)||(intPartCount==6))
                  result = "," + result;
              result = total.substring(scanPos,scanPos+1) + result; 
              intPartCount++;
          }
      }  
      if (negative)
          result = MINUSSIGN + result;
      int reqdSpaces = 15 - result.length();
      if (reqdSpaces>0)
          result = spaceFill(reqdSpaces) + result;
      return result;
  } 
  
  // produce string of required number of spaces
  private String spaceFill(int spaceCount)
  {
      String result = "";
      for( int i=0; i<spaceCount; i++)
      {
          result = result + " ";
      }
      return result;
  }
  
  private void parseLine(String inputLine, String reportType)
  {
    // control line buffering and combination by control character values
    currentCC  = inputLine.substring(0,2);
    currentData = inputLine.substring(2,inputLine.length());
    if ((invoice)&&(!reportType.startsWith("21"))&&(!reportType.startsWith("26")))
    {
      if (bufferInUse)
      {
        writeToSplitFile(buffer);
      }
      closeSplitFile();
      splitFileName =
        splitDir + BACKSLASH +
        currentKey.substring(0,8)+USCORE+
        currentKey.substring(8,16)+USCORE+
        currentKey.substring(16,24)+USCORE+
        OTHERS+TXT;
      openSplitFile();
      splitCount++;
      firstLine = true;
      splitFileStart = true;
      bufferInUse = false;
      invoice = false;     
      countingContent = false;
      contentLineCount = 0;
    }
    else if (currentCC.startsWith(PREVBLANK1))
    {
      // check for ready buffered print, write out blank line
      // and then store current data in buffer
      if (bufferInUse)
      {
        writeToSplitFile(buffer);
      }
      writeToSplitFile(" ");
      buffer = currentData;
      bufferInUse = true;        
    }
    else if (currentCC.startsWith(PREVBLANK2))
    {
      // check for ready buffered print, write out blank line
      // and then store current data in buffer
      if (bufferInUse)
      {
        if ((currentData.trim().startsWith("TOTAL REFUND"))&&(invoice)&&(currentKey.endsWith("C"))) 
        {
            buffer = mergeRefundLines(previousData,currentData); 
            bufferInUse = true;
        }
        else   
        {
            writeToSplitFile(buffer);
            buffer = currentData;
            bufferInUse = true;
        }            
      }
      else
      {         
            buffer = currentData;
            bufferInUse = true; 
      }
      writeToSplitFile(" ");
    }
    else if (currentCC.startsWith(NEWPAGE)&&(!splitFileStart))
    {      
      if (bufferInUse)
      {
        writeToSplitFile(buffer);
      }
      writeToSplitFile(FORMFEED+currentData);
      pageNo++;
      bufferInUse = false;
    }
    else
    {
      // check for buffered print and then store current data in buffer
      if (bufferInUse)
      {
        writeToSplitFile(buffer);
      }
      buffer = currentData;
      bufferInUse = true;
    }
    previousCC = currentCC;
    previousData = currentData;
  }

  private String mergeRefundLines(String firstLine, String secondLine)
  {
      String result = "";
      int firstLen = firstLine.length();
      int secondLen = secondLine.length();
      int scanLen = secondLen;
      if (firstLen>secondLen)
          scanLen = firstLen;
      String firstChar = "", secondChar = "";
      for (int i=0; i<scanLen; i++)
      {
          if (i<firstLen)
              firstChar = firstLine.substring(i,i+1);
          else
              firstChar = " ";
          if (i<secondLen)
              secondChar = secondLine.substring(i,i+1);
          else
              secondChar = " ";
          if (firstChar.equals(" "))
              result = result + secondChar;
          else
              result = result + firstChar;
              
      }
      return result;
  }
  
  private void writeToSplitFile(String splitLine)
  {
    try
    {
      // Get total for a normal invoice
      totalStart = currentKey.substring(0,8)+currentKey.substring(16,24);
      if ((splitLine.trim().startsWith(totalStart))&&(!splitLine.trim().endsWith("X")))
        totalCharges = invoiceTotal(splitLine);

      // Get total for a credit note
      if (splitLine.trim().startsWith(TOTALREFUND))
      {
          totalCharges = MINUSSIGN+invoiceTotal(splitLine);
          savedLine = spaceFill(99)+fixedFormatInvoiceTotal(totalCharges); 
      }  
              
      // total box and giro boxes for invoices only
      if ((splitLine.trim().startsWith(totalStart))&&
          (!splitLine.trim().endsWith("X"))&&
          (!currentKey.endsWith("C")))
      {
          totalCharges = invoiceTotal(splitLine);
          for (int i=0;i<37-contentLineCount;i++)
          {
              splitWriter.newLine();
          }
          splitWriter.write(spaceFill(98)+fixedFormatInvoiceTotal(totalCharges));
          splitWriter.newLine();
          countingContent = false;
          // 20 lines after the amount in the total box
          splitWriter.newLine();
          splitWriter.newLine();
          splitWriter.newLine();
          splitWriter.newLine();
          splitWriter.newLine();
          splitWriter.newLine();
          splitWriter.newLine();
          splitWriter.newLine();
          splitWriter.newLine();
          splitWriter.newLine();
          splitWriter.newLine();
          splitWriter.newLine();
          splitWriter.newLine();
          splitWriter.newLine();
          splitWriter.newLine();
          splitWriter.newLine();
          splitWriter.newLine();
          splitWriter.newLine();
          splitWriter.newLine();
          splitWriter.newLine();
          // now amend line
          splitLine = splitLine.substring(0,28) + spaceFill(45) + fixedFormatInvoiceTotal(totalCharges);
      } 
      
      // 10 lines before the final giro line for invoices only
      if ((splitLine.trim().endsWith("X"))&&
          (splitLine.trim().startsWith(totalStart))&&
          (!currentKey.endsWith("C")))
      { 
          splitWriter.newLine();
          splitWriter.newLine();
          splitWriter.newLine();
          splitWriter.newLine();
          splitWriter.newLine();
          splitWriter.newLine();
          splitWriter.newLine();
          splitWriter.newLine();
          splitWriter.newLine();
          splitWriter.newLine();
      }
      
      //insert spacing lines before invoice header
      if (((splitLine.trim().startsWith("PRIVATE CIRCUIT BILLING"))||
           (splitLine.trim().startsWith("DATACOMMS BILLING")))&&
              (!currentKey.endsWith("C")))
          splitWriter.newLine();
      
      //insert spacing lines before amount header for a credit
      if ((splitLine.trim().startsWith("AMOUNT"))&&(currentKey.endsWith("C")))
      {
          splitWriter.newLine();
          splitWriter.newLine();
          splitWriter.newLine();
          splitWriter.newLine();
          contentLineCount=contentLineCount+4;
      }
                
      // insert a spacing line after invoice header and start content count
      if (((splitLine.trim().startsWith("SERVICE CHARGES"))||
           (splitLine.trim().startsWith("CONTRACT CHARGES"))||
           (splitLine.trim().startsWith("SUNDRY CHARGES")))&&
              (!currentKey.endsWith("C")))
      {
          splitWriter.newLine();
          contentLineCount=0;
          countingContent=true;
      }    
                
      // start content count after credit note header
      if ((splitLine.trim().startsWith("CREDIT NOTE REFUNDS"))&&(currentKey.endsWith("C")))
      {
          contentLineCount=0;
          countingContent=true;
      }      
      
      //insert spacing lines after the address
      if ((splitLine.trim().startsWith("CUSTOMER A/C NO")))
      {
          if (currentKey.endsWith("C"))
          {
            if (pageNo<3)
            // two lines for first two pages of a credit
            {
                splitWriter.newLine(); 
                splitWriter.newLine();                
                contentLineCount=contentLineCount+2;
            }
            else
            // four lines for third and subsequent pages of a credit
            {
                splitWriter.newLine(); 
                splitWriter.newLine(); 
                splitWriter.newLine(); 
                splitWriter.newLine();
                contentLineCount=contentLineCount+4;
            }              
          }
          else
          // ten lines for an invoice
          { 
            splitWriter.newLine(); 
            splitWriter.newLine(); 
            splitWriter.newLine(); 
            splitWriter.newLine(); 
            splitWriter.newLine(); 
            splitWriter.newLine(); 
            splitWriter.newLine(); 
            splitWriter.newLine(); 
            splitWriter.newLine(); 
            //FFsplitWriter.newLine(); 
          }                  
      }      
      
      // Prevent total being put too low for Credits
      if (((countingContent)&&
           (contentLineCount>=37)&&
           (billingType.startsWith("PCB")&&
           (currentKey.endsWith("C"))))||
          ((countingContent)&&
           (contentLineCount>=49)&&
           (billingType.startsWith("SSBS")&&
           (currentKey.endsWith("C")))))
      {
          // temporarily comment out as Credit note amount box code still not 100% working
          //splitLine = mergeRefundLines(savedLine,splitLine);
          savedLine = "";
          countingContent = false;
          contentLineCount = 0;
      }        
      
      // write out line
      splitWriter.write(splitLine);              
      splitWriter.newLine();
      splitFileStart = false;
      
      //maintain content count where relevant
      if (countingContent)
        contentLineCount++;
      
    }
    catch (Exception ex)
    {
      System.out.println("Error writing to split file: "+splitFileName+" : "+ex.getMessage());
      System.out.println(splitLine);
    }
  }

  private void closeSplitFile()
  {
    // close current split file
    if ((bufferInUse)&&(buffer.trim().length()>0)&&(!buffer.trim().endsWith("X")))
    {
      writeToSplitFile(buffer);
      //contentLineCount++;
    }
    
    if (savedLine.length()>0)
    {    
        
        int maxLines = 38;
        if (billingType.startsWith("SSBS"))
            maxLines = 50; 
        if (contentLineCount<maxLines)
        {
            for (int i=0;i<maxLines-contentLineCount;i++)
            {
                // temporarily comment out as Credit note amount box code still not 100% working
                //writeToSplitFile(" ");
            }
        }
        // temporarily comment out as Credit note amount box code still not 100% working
        //writeToSplitFile(savedLine);
    }
    countingContent = false;
    contentLineCount = 0;
    savedLine = "";       
    try
    {
      splitWriter.close();
      File sf = new File(splitFileName);
      String newName =
        splitFileName.substring(0,splitFileName.length()-4)+USCORE+
        totalCharges+USCORE+tpd+USCORE+tpd;
      if (invoice)
      {
        if ((newName.startsWith(splitDir+File.separator+"081"))||
                 (newName.startsWith(splitDir+File.separator+"080"))||
                 (newName.startsWith(splitDirSSBS+File.separator+"081"))||
                 (newName.startsWith(splitDirSSBS+File.separator+"080")))
          newName = newName + USCORE + "GREEN";
        else
          newName = newName + USCORE + "BLUE";
      }
      newName = newName+TXT;
      File nsf = new File(newName);
      if (nsf.exists())
        nsf.delete();
      if (!sf.renameTo(nsf))
        System.out.println("Error updating split file name to "+newName);
    }
    catch (Exception ex)
    {
      System.out.println("Error closing split file: "+splitFileName+": "+ex.getMessage());
      System.exit(1);
    }
  }

  private void openLogFile()
  {
    // open a new log file
    try
    {
      GregorianCalendar gc = new GregorianCalendar();
      runTS = su.reformatDate("now", null, DTFMT);
      logFile = new File(logDir + File.separator + runTS + "_splitImpactFile_log.txt");
      logWriter = new BufferedWriter(new FileWriter(logFile));
      String now = su.DateToString(gc.getTime(), DTFMT);
      writeToLogFile("splitImpactFile processing started at " +
        now.substring(8, 10) + ":" + now.substring(10, 12) + "." +
        now.substring(12, 14) + " on " + now.substring(6, 8) + "/" +
        now.substring(4, 6) + "/" + now.substring(0, 4));
    }
    catch (Exception ex)
    {
      System.out.println("Error opening log file: " + ex.getMessage());
      System.exit(1);
    }
  }

  private void closeLogFile()
  {
    // at end of program close log file
    try
    {
      GregorianCalendar gc = new GregorianCalendar();
      String now = su.DateToString(gc.getTime(), DTFMT);
      writeToLogFile("splitImpactFile processing ended at " +
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

  public static void main(String[] args)
  {
    // control processing
    splitPCBImpactFile spif = new splitPCBImpactFile();
    spif.openLogFile();
    if (spif.checkRunControl())
    {
      spif.processFiles();
      spif.resetRunControl();
    }
    spif.closeLogFile();
  }

}



