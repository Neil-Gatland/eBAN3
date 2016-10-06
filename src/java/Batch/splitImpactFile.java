package Batch;

import java.io.*;
import java.util.*;
import JavaUtil.*;
import DBUtilities.*;

public class splitImpactFile
{

  // class variables
  private DBAccess dba;
  private String dropDir, logDir, splitDir, procDir, runTS, splitFileName;;
  private StringUtil su;
  private BufferedWriter logWriter, splitWriter;
  private File logFile;
  private boolean splitFileStart;
  private String currentKey;
  private String previousData;
  private String buffer;
  private int lineCount, splitCount;
  private boolean bufferInUse;
  private boolean firstLine, invoice, billingAddress, WAD, secondInvoicePage, countingContent;
  private String totalCharges, tpd, periodStart, periodEnd, yearChar,dateStartChar, totalStart;
  private String[] billingAddressBuffer;
  private String[] enquiriesBuffer;
  private String wadReplace;
  private int bufferPos, enquiriesBufferPos, contentLineCount;
  private String storedAccountNumber;
  // class constants
  private final String SSBS = "SSBS";
  private final String SIF = "Split Impact Files";
  private final String LSI = "Load SSBS Invoices";
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
  private final String NEWPAGE = "10";
  private final String MERGE1 = "+þ";
  private final String MERGE2 = "+1";
  private final String MERGE3 = "+5";
  private final String MERGE4 = "+ý";
  private final String JOIN = "+ú";
  private final String PREVBLANK = "0";
  private final String IGNOREBLANK = "0ú";
  private final String IGNOREBLANK2 = "00";

  private splitImpactFile()
  {
    su = new StringUtil();
    dba = new DBAccess();
    dropDir = EBANProperties.getEBANProperty(EBANProperties.SIDROPDIR);
    logDir = EBANProperties.getEBANProperty(EBANProperties.SILOGDIR);
    splitDir = EBANProperties.getEBANProperty(EBANProperties.SISPLITDIR);
    procDir = EBANProperties.getEBANProperty(EBANProperties.SIPROCDIR);
    wadReplace = EBANProperties.getEBANProperty(EBANProperties.SIWR);
  }

  private boolean checkRunControl()
  {
    boolean result = false;
    String status = dba.getRunControlStatus(SSBS,SIF), message="";
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
        status = dba.getRunControlStatus(SSBS,LSI);
        if (!status.startsWith(INACTIVE))
        {
          message = "!!!cannot run while load SSBS invoice process is running!!!";
          writeToLogFile(message);
          System.out.println(message);
        }
        else
        {
          if (dba.updateRunControlStatus(SSBS,SIF,ACTIVE))
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
    if (!dba.updateRunControlStatus(SSBS,SIF,INACTIVE))
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
    billingAddressBuffer = new String[20];
    enquiriesBuffer = new String[5];
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
          if (len<172)
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
              splitFileName =
                splitDir + BACKSLASH +
                currentKey.substring(0,8)+USCORE+
                currentKey.substring(8,16)+USCORE+
                currentKey.substring(16,24)+USCORE+
                INVOICE+TXT;
              storedAccountNumber = currentKey.substring(0,8);
              openSplitFile();
              splitCount++;
              firstLine = true;
              splitFileStart = true;
              bufferInUse = false;
              totalCharges = "000";
              tpd = currentKey.substring(8,16);
              yearChar = currentKey.substring(11,12);
              dateStartChar = currentKey.substring(14,15);
              periodStart = tpd;
              periodEnd= tpd;
              invoice = true;
              billingAddress = true;
              secondInvoicePage = false;
              WAD = false;
              bufferPos = 0;
              enquiriesBufferPos = 0;
            }
            parseLine(line.substring(74,172),line.substring(61,63));
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

  private String mergeLinesType(String firstLine, String secondLine)
  {
    // merge two data lines
    String result = "";
    String shortLine, longLine;
    int shortLen, longLen;
    if (firstLine.length()>secondLine.length())
    {
      shortLen = secondLine.length();
      shortLine = secondLine;
      longLen = firstLine.length();
      longLine = firstLine;
    }
    else
    {
      shortLen = firstLine.length();
      shortLine = firstLine;
      longLen = secondLine.length();
      longLine = secondLine;
    }
    char[] shortScan = new char[shortLen];
    char[] longScan = new char[longLen];
    for(int i=0; i<shortLen; i++)
    {
      shortScan[i] = shortLine.charAt(i);
    }
    for(int i=0; i<longLen; i++)
    {
      longScan[i] = longLine.charAt(i);
    }
    for(int i=0;i<shortLen; i++)
    {
      char shortChar = shortScan[i];
      char longChar  = longScan[i];
      if (!Character.isSpaceChar(longChar))
        result = result + longChar;
      else
        result = result + shortChar;
    }
    for(int i=shortLen+1; i<longLen; i++)
    {
      result = result + longScan[i];
    }
    return result;
  }

  private String joinLines(String firstLine, String secondLine)
  {
    // join two data lines
    String result = "";
    result = " " + removeCCs(firstLine).trim() + " " + secondLine.trim();
    return result;
  }
  
  private String removeCCs(String input)
  {
    // remove control characters from a string
    String result = "";
    char[] scan = new char[172];
    for(int i=0; i<input.length(); i++)
    {
      scan[i] = input.charAt(i);
    }
    for(int i = 0; i < input.length(); i++)
    {
      if (!Character.isISOControl(scan[i]))
        result = result+scan[i];
    }
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
    if (totalLine.trim().endsWith("CR"))
        result = result + "CR";
    return result;
  }
  
  private String fixedFormatInvoiceTotal(String total)
  {
      // format total as a fixed -ZZZ,ZZZ,ZZ9.99 string right justified
      String result = "", CR = "";
      // always take last three characters with inserted decimal place
      int origLength = total.length();
      result = total.substring(origLength-3,origLength-2)+"."+total.substring(origLength-2,origLength);
      // 
      if (total.endsWith("CR"))
      {
          total = total.substring(0,total.length() - 2 );
          origLength = total.length();
          result = total.substring(origLength-3,origLength-2)+"."+total.substring(origLength-2,origLength);
          CR = "CR";
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
      int reqdSpaces = 15 - result.length();
      if (reqdSpaces>0)
          result = spaceFill(reqdSpaces) + result;
      result = result + CR;
      return result;
  }

  private void updatePeriodDates(String periodLine)
  {
    // look at dates and update period dates if required
    String test = periodLine.trim();
    String firstDate = "", lastDate = "";
    int pos = 0;
    boolean scanDone = false;
    // get the last date (may be the only one)
    for(int i=test.length()-2; !scanDone ; i--)
    {
      String scanTest = String.valueOf(test.charAt(i));
      if ((scanTest.startsWith("-"))||(scanTest.startsWith("(")))
        scanDone = true;
      else
        lastDate = test.charAt(i) + lastDate;
        pos = i;
    }
    lastDate = lastDate.trim();
    if (pos==0)
      firstDate=lastDate;
    else
    {
      scanDone = false;
      for(int i=pos-2; !scanDone ; i--)
      {
        String scanTest = String.valueOf(test.charAt(i));
        if (scanTest.startsWith("("))
          scanDone = true;
        else
          firstDate = test.charAt(i) + firstDate;
      }
    }
    if (Integer.parseInt(formatDate(firstDate))<Integer.parseInt(periodStart))
      periodStart = formatDate(firstDate);
    if (Integer.parseInt(formatDate(lastDate))>Integer.parseInt(periodEnd))
      periodEnd = formatDate(lastDate);
  }

  private String formatDate(String inDate)
  {
    // format a DD MON date into YYYYMMDD format
    String result = "";
    int tpdYear = Integer.parseInt(tpd.substring(0,4));
    int tpdMonth = Integer.parseInt(tpd.substring(4,6));
    int indMonth = 0, indYear = 0;
    if ( inDate.endsWith("Jan") )
      indMonth = 1;
    else if ( inDate.endsWith("Feb") )
      indMonth = 2;
    else if ( inDate.endsWith("Mar") )
      indMonth = 3;
    else if ( inDate.endsWith("Apr") )
      indMonth = 4;
    else if ( inDate.endsWith("May") )
      indMonth = 5;
    else if ( inDate.endsWith("Jun") )
      indMonth = 6;
    else if ( inDate.endsWith("Jul") )
      indMonth = 7;
    else if ( inDate.endsWith("Aug") )
      indMonth = 8;
    else if ( inDate.endsWith("Sep") )
      indMonth = 9;
    else if ( inDate.endsWith("Oct") )
      indMonth = 10;
    else if ( inDate.endsWith("Nov") )
      indMonth = 11;
    else
      indMonth = 12;
    if (indMonth > tpdMonth)
      indYear = tpdYear -1;
    else
      indYear = tpdYear;
    String retYear = String.valueOf(indYear);
    String retMonth;
    if (indMonth<10)
      retMonth = "0" + String.valueOf(indMonth);
    else
      retMonth = String.valueOf(indMonth);
    boolean dayFound = false;
    String retDay = "";
    for(int i=0; !dayFound; i++)
    {
      if (Character.isSpaceChar(inDate.charAt(i)))
        dayFound = true;
      else
        retDay = retDay + inDate.charAt(i);
    }
    if (retDay.length()==1)
      retDay = "0" + retDay;
    result = retYear + retMonth + retDay;
    return result;
  }

  private void parseLine(String inputLine, String reportType)
  {
    // control line buffering and combination by control character values
    String currentCC = inputLine.substring(0,2);
    String currentData = inputLine.substring(2,inputLine.length());
    //String totalLiteral = currentKey.substring(0,8)+currentKey.substring(16,24);
    if ( (currentData.trim().endsWith(")")) &&
        ( (currentData.trim().endsWith("Jan)")) ||
          (currentData.trim().endsWith("Feb)")) ||
          (currentData.trim().endsWith("Mar)")) ||
          (currentData.trim().endsWith("Apr)")) ||
          (currentData.trim().endsWith("May)")) ||
          (currentData.trim().endsWith("Jun)")) ||
          (currentData.trim().endsWith("Jul)")) ||
          (currentData.trim().endsWith("Aug)")) ||
          (currentData.trim().endsWith("Sep)")) ||
          (currentData.trim().endsWith("Oct)")) ||
          (currentData.trim().endsWith("Nov)"))||
          (currentData.trim().endsWith("Dec)")) ))
      updatePeriodDates(currentData);
    if ((invoice)&&(!reportType.startsWith("01")))
    {
      if (bufferInUse)
      {
        writeToSplitFile(buffer);
      }
      closeSplitFile();
      WAD = false;
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
    }
    if ((currentCC.startsWith(MERGE1))||
        (currentCC.startsWith(MERGE3))||
        (currentCC.startsWith(MERGE4)))
    {
      // merge this line with previous and place in buffer
      writeToSplitFile(mergeLinesType(previousData,currentData));
      bufferInUse = false;
    }
    if (((currentCC.startsWith(MERGE2))||
        (currentData.trim().startsWith("Faults")))&&(invoice))
    {
      // write out previous data and store current data in enquiries buffer
      if (previousData.trim().length()>0)  
        writeToSplitFile(previousData);      
      // fix to add missing s to truncated text
      if (currentData.endsWith("Bill & General Enquirie"))
        currentData = currentData+"s";
      if (splitFileStart)
      {
          enquiriesBuffer[enquiriesBufferPos] = spaceFill(15) + currentData;
          enquiriesBufferPos++;
      }
      bufferInUse = false;
    }
    else if (currentCC.startsWith(JOIN))
    {
      // join this line with previous and output
      writeToSplitFile(buffer = joinLines(previousData,currentData));
      bufferInUse = false;
    }
    else if (currentData.trim().startsWith("Total charges to date"))
    {
      // join this line with previous and output (in reverse)
      if (previousData.trim().length()==0)
        writeToSplitFile(buffer = " "+
                  mergeLinesType("Total charges to date",spaceFill(35)+currentData.trim().substring(21,currentData.trim().length())));
      else
        writeToSplitFile(buffer = " "+mergeLinesType(previousData,currentData.trim()));
      bufferInUse = false;
    }
    else if (currentData.trim().startsWith("Payment now due"))
    {
      // join this line with previous and output (in reverse)
      writeToSplitFile(buffer = " "+mergeLinesType(previousData,currentData.trim()));
      bufferInUse = false;
    }
    else if ((currentCC.startsWith(PREVBLANK)&&
             ( (!currentCC.startsWith(IGNOREBLANK))||
               (!currentCC.startsWith(IGNOREBLANK2)) ) ) )
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
    else if (currentCC.startsWith(NEWPAGE)&&(!splitFileStart))
    {
      // check for buffered print and then print out form feed etc
      if (bufferInUse)
      {
        writeToSplitFile(buffer);
      }
      if (invoice)
      {
        secondInvoicePage = true;
        bufferPos = 0;
        enquiriesBufferPos = 0;
      }
      writeToSplitFile(FORMFEED+currentData);
      buffer = currentData;
      bufferInUse = true;
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
    previousData = currentData;
  }

  private void writeToSplitFile(String splitLine)
  {
    // write line to current split file
    // fix to add missing s to truncated text
    if (splitLine.endsWith("Bill & General Enquirie"))
      splitLine = splitLine+"s";
    // fix to add full year on tax point date on other report file
    if ((!invoice)&&(!dateStartChar.startsWith("0"))&&(splitLine.trim().startsWith("Account number:")))
      splitLine = splitLine+yearChar;
    try
    {
      // put first 11 lines of invoice report to buffer to check if WAD Address
      // is to be used
      if (invoice)
      {
        if ((secondInvoicePage)&&(bufferPos==0))
        {            
          splitWriter.newLine();
          splitWriter.newLine();
        }
        if ((billingAddress)||(secondInvoicePage))
        {
          billingAddressBuffer[bufferPos] = splitLine;
          bufferPos++;
        }
        if (splitLine.trim().startsWith("Account"))
        {  
          // check if account is a WAD account, if it is then replace current
          // biling address with WAD billing address
          billingAddress = false;
          secondInvoicePage = false;
          String[] wadAddress = new String [5];
          wadAddress = dba.wadDetails(currentKey.substring(0,8));
          if (!wadAddress[0].startsWith("NOT WAD"))
          {
            WAD = true;
            // only replace billing address with WAD address if switch is set on
            if (wadReplace.startsWith("Y"))
            {
              billingAddressBuffer[4] = wadLine(billingAddressBuffer[4],wadAddress[0]);
              billingAddressBuffer[5] = wadLine(billingAddressBuffer[5],wadAddress[1]);
              billingAddressBuffer[6] = wadLine(billingAddressBuffer[6],wadAddress[2]);
              billingAddressBuffer[7] = wadLine(billingAddressBuffer[7],wadAddress[3]);
              billingAddressBuffer[8] = wadLine(billingAddressBuffer[8],wadAddress[4]);
              billingAddressBuffer[9] = wadLine(billingAddressBuffer[9]," ");
            }
          }
          // six blank lines before billing address
          splitWriter.newLine();
          splitWriter.newLine();
          splitWriter.newLine();
          splitWriter.newLine();
          splitWriter.newLine();
          splitWriter.newLine();
          int lCount=0;
          for( int i=0; i<bufferPos-1; i++)
          {
            // Write out first 10 lines from the buffer
            if (billingAddressBuffer[i].trim().length()>0)
            {
                splitWriter.write(billingAddressBuffer[i]);
                splitWriter.newLine();
                splitFileStart = false;
                lCount++;
            }
          }
          // two blank lines after billing address
          splitWriter.newLine();
          splitWriter.newLine();
          // extra line for short addresses
          if (lCount==4)
              splitWriter.newLine();
          for( int i=0; i<enquiriesBufferPos; i++)
          {
            // Write out lines from the enquiry buffer
            splitWriter.write(enquiriesBuffer[i]);
            splitWriter.newLine();
            splitFileStart = false;
          }
          // required number of blank lines after enquiries details
          if (enquiriesBufferPos==2)
          {
              splitWriter.newLine();
              splitWriter.newLine(); 
              splitWriter.newLine();
              splitWriter.newLine();
          }
          if (enquiriesBufferPos==3)
          {
              splitWriter.newLine();
              splitWriter.newLine(); 
              splitWriter.newLine();
              splitWriter.newLine();
          }
          if (enquiriesBufferPos==4)
          {
              splitWriter.newLine();
              splitWriter.newLine();
          }           
        }
      }
      totalStart = currentKey.substring(0,8)+currentKey.substring(16,24);
      if (splitLine.trim().startsWith("Description"))
      {    
          // 5 lines before the description
          splitWriter.newLine();
          splitWriter.newLine(); 
          splitWriter.newLine(); 
          splitWriter.newLine();  
          //FFsplitWriter.newLine(); 
          contentLineCount = 0;
          countingContent = true;
      }
      if ((splitLine.trim().startsWith(totalStart))&&(!splitLine.trim().endsWith("X")))
      {
          totalCharges = invoiceTotal(splitLine);
          for (int i=0;i<46-contentLineCount;i++)
          {
              splitWriter.newLine();
          }
          splitWriter.write(spaceFill(71)+fixedFormatInvoiceTotal(totalCharges));
          splitWriter.newLine();
          countingContent = false;
          // 14 lines after the amount in the total box
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
      if (splitLine.trim().endsWith("X")&&(!billingAddress))
      {          
          // 14 lines before the final giro line
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
      }                      
          
      if ((!billingAddress)&&(!secondInvoicePage))
      {
        splitFileStart = false; 
        //if (!(splitLine.trim().startsWith("0.00")))
        if (!ignoreAmountOnly(splitLine))
        {            
            splitWriter.write(splitLine);
            splitWriter.newLine();        
            if (countingContent)
                contentLineCount++;       
        }
         // More space after header
        if (splitLine.trim().startsWith("£"))
        {
            splitWriter.newLine();
            if (countingContent)
                contentLineCount++;
        }
      }
    }
    catch (Exception ex)
    {
      System.out.println("Error writing to split file: "+splitFileName+" : "+ex.getMessage());
      System.exit(1);
    }
  }
  
  private boolean ignoreAmountOnly(String line)
  {
      boolean result = false;
      // ignore first line of new page and gito box line or if not in invoice
      if ((line.startsWith(FORMFEED))||(line.trim().startsWith(totalStart))||(!invoice))
          return result;
      String test = line.trim();
      // must start with a numeric character
      if ((test.startsWith("0"))||(test.startsWith("1"))||(test.startsWith("2"))||
          (test.startsWith("3"))||(test.startsWith("4"))||(test.startsWith("5"))||
          (test.startsWith("6"))||(test.startsWith("7"))||(test.startsWith("8"))||
          (test.startsWith("9")))
          // must end with a numeric character
          if ((test.endsWith("0"))||(test.endsWith("1"))||(test.endsWith("2"))||
              (test.endsWith("3"))||(test.endsWith("4"))||(test.endsWith("5"))||
              (test.endsWith("6"))||(test.endsWith("7"))||(test.endsWith("8"))||
              (test.endsWith("9")))
          {
              // check that there is a decimal place
              for (int i=0;i<test.length();i++)
              {
                  if (test.substring(i, i+1).startsWith("."))
                      result = true;
              }              
          }
      return result;
  }

  private String wadLine(String splitLine, String wadAddressLine)
  {
    // function to overlay splitLine with WAD address line data
    String result = "";
    if (splitLine.length()<14)
    {
      for( int j=splitLine.length(); j<14; j++)
      {
        splitLine = splitLine + " ";
      }
    }
    result = splitLine.substring(0,14);
    int slLength = splitLine.length(), walLength = wadAddressLine.length();
    for( int i=0; i<slLength-14; i++)
    {
      if (i<walLength)
        result = result + wadAddressLine.substring(i,i+1);
      else
        if (i>49)
          result = result + splitLine.substring(i+14,i+15);
        else
          result = result + " ";
    }
    return result;
  }

  private void closeSplitFile()
  {
    // close current split file
    try
    {
      splitWriter.close();
      File sf = new File(splitFileName);
          String newName =
            splitFileName.substring(0,splitFileName.length()-4)+USCORE+
            totalCharges+USCORE+tpd+USCORE+tpd;
      if (totalCharges.endsWith("CR"))
      {
          newName =
            splitFileName.substring(0,splitFileName.length()-4)+USCORE+
            "-"+totalCharges.substring(0, totalCharges.length() - 2) +USCORE+tpd+USCORE+tpd;
          
      }
      // determine if account is WAD, Ex-Energis (GREEN) or C&W (BLUE)
      // for later use in PDF stamping
      if (WAD)
        newName = newName + USCORE + "WAD";
      else
      {
        if (invoice)
        {
          if (dba.thusAccount(storedAccountNumber))
            newName = newName + USCORE + "THUS";
          else if ((newName.startsWith(splitDir+File.separator+"081"))||
                   (newName.startsWith(splitDir+File.separator+"080")))
            newName = newName + USCORE + "GREEN";
          else
            newName = newName + USCORE + "BLUE";
        }
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
  
  public static void main(String[] args)
  {
    // control processing
    splitImpactFile sif = new splitImpactFile();
    sif.openLogFile();
    if (sif.checkRunControl())
    {
      sif.processFiles();
      sif.resetRunControl();
    }
    sif.closeLogFile();
  }

}



