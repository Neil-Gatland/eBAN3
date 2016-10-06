package Batch;

import java.io.*;
import java.sql.*;
import java.util.*;
import JavaUtil.*;
import java.text.DateFormatSymbols;
import java.math.BigDecimal;

public class TeleBusinessConvert
{
  private static final int BUFFERSIZE = 20480;
  private static final String ASCII = "ascii";
  private static final String EXTRACT = "extract";
  private static final String ALL = "all";
  private String ebcdicEncoding;
  private String dropDir;
  private String asciiDir;
  private String logDir;
  private File logFile;
  private String runTS;
  private final String DATETIMEFORMAT = "yyyyMMddHHmmss";
  private StringUtil su;
  private BufferedWriter logWriter;
  private boolean weekly;
  private final String WEEKLY = "WEEKLY";
  //private String asciiName;
  private int ret;
  private final String accountText =
    EBANProperties.getEBANProperty("teleBusiness.accountText"); //"Your Account Number";
  private final String callStartText =
    EBANProperties.getEBANProperty("teleBusiness.callStartText"); //"element of the cost.";
  private final String callStartText2 =
    EBANProperties.getEBANProperty("teleBusiness.callStartText2"); //"Call summary by month";
  private final String reportText =
    EBANProperties.getEBANProperty("teleBusiness.reportText"); //"Report 7,";
   private final String reportText2 =
    EBANProperties.getEBANProperty("teleBusiness.reportText2"); //"Cable&Wireless Worldwide,";
  private final String uprText =
    EBANProperties.getEBANProperty("teleBusiness.uprText"); //"LøÌ";
  private final String dateText =
    EBANProperties.getEBANProperty("teleBusiness.dateText"); //"Date of report:";
  private final String billingSource =
    EBANProperties.getEBANProperty("teleBusiness.billingSource"); //"TeleBusiness";
  private final String companyName =
    EBANProperties.getEBANProperty("teleBusiness.companyName"); //"Cable&Wireless Worldwide";
  private final String creditImmediate =
    EBANProperties.getEBANProperty("teleBusiness.creditImmediate"); //"N";
  private final String currencyCode =
    EBANProperties.getEBANProperty("teleBusiness.currencyCode"); //"GBP";
  private final String cwContact =
    EBANProperties.getEBANProperty("teleBusiness.cwContact"); //"Craig.Allen@cwcom.cwplc.com";
  private String uprChar;
  private char textStart;
  private char textEnd;
  private File input;
  private String outputDir;
  private FileWriter xmlFileWriter;
  private FileWriter addressFileWriter;
  private FileWriter dbFileWriter;
  private FileWriter misfitsFileWriter;
  private FileWriter deFileWriter;
  private String xmlFile;
  private String addressFile;
  private String dbFile;
  private String misfitsFile;
  private String deFile;
  private FileReader fr;
  private BufferedReader br;
  private boolean endCustomer;
  private boolean eof;
  private String forNextTime;
  private final char FIELD_SEPARATOR = '|';
  private final char NEWLINE = '\n';
  private final String HEADER = "Header";
  private final String TRAILER = "Trailer";
  private final String ACCOUNT = "Account";
  private final String ADDRESS = "Address";
  private final String BILLING = "Billing";
  private final String INVOICE = "Invoice";
  private final String CALL = "Call";
  private final String NOT_APPLICABLE = "Not Applicable";
  private int recordNumber;
  private int callRecordNumber;
  private int fileNumber;
  private String week;
  private String referenceNumber;
  private char uprCharOne;
  private char uprCharTwo;
  private char uprCharThree;
  private DateFormatSymbols dfs;
  private String xmlReportDate;

  private TeleBusinessConvert()
  {
    dfs = new DateFormatSymbols();
    su = new StringUtil();
    ebcdicEncoding = EBANProperties.getEBANProperty("teleBusiness.ebcdicEncoding");
    dropDir = EBANProperties.getEBANProperty("teleBusiness.dropDir");
    asciiDir = EBANProperties.getEBANProperty("teleBusiness.asciiDir");
    runTS = su.reformatDate("now", null, DATETIMEFORMAT);
    logDir = EBANProperties.getEBANProperty("teleBusiness.logDir");
    logFile = new File(logDir + File.separator + "TeleBusinessConvert_" +
      runTS + ".log");
    createLogFile();

  }

  private void createLogFile()
  {
    try
    {
      logWriter = new BufferedWriter(new FileWriter(logFile));
      GregorianCalendar gc = new GregorianCalendar();
      String now = su.DateToString(gc.getTime(), DATETIMEFORMAT);
      writeToLogFile("TeleBusiness conversion processing started at " +
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
      writeToLogFile("TeleBusiness conversion processing ended at " +
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

  private void convertEBCDICToASCII(String fileName)
  {
    try
    {
      FileInputStream fis = new FileInputStream(dropDir + File.separator + fileName);
      FileOutputStream fos = new FileOutputStream(asciiDir + File.separator + fileName);
      OutputStreamWriter osr = new OutputStreamWriter(fos);
      InputStreamReader isr = new InputStreamReader(fis, ebcdicEncoding);
      BufferedReader br = new BufferedReader(isr, BUFFERSIZE);
      BufferedWriter bw = new BufferedWriter(osr, BUFFERSIZE);
      char[] cbuf = new char[BUFFERSIZE];
      int ret = br.read(cbuf, 0, BUFFERSIZE);
      while (ret != -1)
      {
        bw.write(cbuf, 0, ret);
        ret = br.read(cbuf, 0, BUFFERSIZE);
      }
      br.close();
      isr.close();
      fis.close();
      bw.close();
      osr.close();
      fos.close();
    }
    catch (IOException ex)
    {
      writeToLogFile("Exception in convertEBCDICToASCII for file " + fileName +
        ": " + ex.getMessage());
    }
  }

  private FileWriter createFileWriter(String file)
    throws IOException
  {
    File f = new File(file);
    if (!f.exists())
    {
      f.createNewFile();
    }
    return new FileWriter(f);
  }

  private void extract(String fileName, String period, String fileDate,
    boolean withLegacy)
  {
    ret = 0;
    recordNumber = 0;
    callRecordNumber = 0;
    String dir = "";

    //dir = EBANProperties.getEBANProperty("teleBusiness.inputFilePath");

    if (weekly)
    {
      outputDir = EBANProperties.getEBANProperty("teleBusiness.outputFilePathWeek");
      uprChar = EBANProperties.getEBANProperty("teleBusiness.uprCharW"); //"]";
    }
    else
    {
      outputDir = EBANProperties.getEBANProperty("teleBusiness.outputFilePathMonth");
      uprChar = EBANProperties.getEBANProperty("teleBusiness.uprCharM"); //"!";
    }
    input = new File(asciiDir+File.separator+fileName);
    int start =
      Integer.parseInt(EBANProperties.getEBANProperty("teleBusiness.textStart")); //238;
    textStart = (char)start;
    int end =
      Integer.parseInt(EBANProperties.getEBANProperty("teleBusiness.textEnd")); //8249;
    textEnd = (char)end;
/*    int intOne = 76;
    uprCharOne = (char)intOne;
    int intTwo = 248;
    uprCharTwo = (char)intTwo;
    int intThree = 204;
    uprCharThree = (char)intThree;*/
    String suffix = EBANProperties.getEBANProperty("teleBusiness.fileSuffix");
    addressFile = outputDir+File.separator+
    EBANProperties.getEBANProperty("teleBusiness.accountPrefix")+"."+period+
    "."+fileDate+"."+suffix;
    dbFile = outputDir+File.separator+
    EBANProperties.getEBANProperty("teleBusiness.dbCallsPrefix")+"."+period+
    "."+fileDate+"."+suffix;
    misfitsFile = outputDir+File.separator+
    EBANProperties.getEBANProperty("teleBusiness.misfitsPrefix")+"."+period+
    "."+fileDate+"."+suffix;
    deFile = outputDir+File.separator+
    EBANProperties.getEBANProperty("teleBusiness.deCallsPrefix")+"."+period+
    "."+fileDate+"."+suffix;
    xmlFile = outputDir+File.separator+"extract_"+period+"_"+fileDate+".xml";

    generateCallLinkRecords(withLegacy);

  }
  private void generateCallLinkRecords(boolean withLegacy)
  {
    try
    {
      fr = new FileReader(input);
      br = new BufferedReader(fr);
      if (withLegacy)
      {
        addressFileWriter = createFileWriter(addressFile);
        dbFileWriter = createFileWriter(dbFile);
        misfitsFileWriter = createFileWriter(misfitsFile);
        deFileWriter = createFileWriter(deFile);
      }
      //xmlFileWriter = createFileWriter(xmlFile);
      eof = false;
      forNextTime = "";
//System.out.println("before createHeaders()");
      createHeaders(withLegacy);
      //xmlFileWriter.write("<tb_invoices value=\"" + (weekly?"Weekly":"Monthly") +
        //"\">" + NEWLINE);
//System.out.println("after createHeaders()");
      while (!eof)
      {
        endCustomer = false;
//System.out.println("before removeUnprintable()");
        String cleanText = removeUnprintable();
//System.out.println("after removeUnprintable():"+cleanText);

//System.out.println("before createRecords()");
        createRecords(cleanText, withLegacy);
//System.out.println("after createRecords()");
      }
//System.out.println("before createTrailers()");
      if (withLegacy)
      {
        createTrailers();
      }
      //xmlFileWriter.write("</tb_invoices>" + NEWLINE);
//System.out.println("after createTrailers()");
    }
    catch (Exception ex)
    {
      writeToLogFile("Unable to generate Call Link records. Reason: "+
      ex.getMessage());
    }
    finally
    {
      try
      {
        br.close();
        fr.close();
        if (withLegacy)
        {
          addressFileWriter.close();
          dbFileWriter.close();
          misfitsFileWriter.close();
          deFileWriter.close();
        }
        //xmlFileWriter.close();
      }
      catch (Exception ex)
      {
        writeToLogFile("Unable to close file readers/writers. Reason: "+
          ex.getMessage());
      }
    }
  }

  private void createHeaders(boolean withLegacy)
    throws IOException
  {
    String startDate;
    String endDate;
    String reportDate;
    FileReader tempFr = new FileReader(input);
    BufferedReader tempBr = new BufferedReader(tempFr);
    String line = "";
    int pos = -1;
    while (pos == -1)
    {
      line = tempBr.readLine();
      pos = line.indexOf(dateText);
    }
    tempBr.close();
    tempFr.close();
    int dateStart = pos+17;
    String dd = line.substring(dateStart,dateStart+2);
    String mm = line.substring(dateStart+3,dateStart+5);
    String yy = line.substring(dateStart+6,dateStart+8);
    reportDate = 2000+Integer.parseInt(yy)+"/"+mm+"/"+dd;
    xmlReportDate = dd + "-" + dfs.getMonths()[Integer.parseInt(mm)-1] +
      "-" + (2000+Integer.parseInt(yy));
    Calendar calendar = Calendar.getInstance();
    calendar.setMinimalDaysInFirstWeek(7);
    calendar.set(Calendar.DATE, Integer.parseInt(dd));
    calendar.set(Calendar.MONTH, Integer.parseInt(mm)-1);
    calendar.set(Calendar.YEAR, 2000+Integer.parseInt(yy));
    int lastDay = 0;
    if (weekly)
    {
      calendar.add(calendar.WEEK_OF_YEAR, -1);
      while (calendar.get(calendar.DAY_OF_WEEK) != calendar.MONDAY)
      {
        calendar.add(calendar.DATE, -1);
      }
      week = Integer.toString(calendar.get(calendar.WEEK_OF_YEAR));
      lastDay = calendar.get(Calendar.DATE)+6;
    }
    else
    {
      week = "EOM";
      calendar.add(Calendar.MONTH, -1);
      calendar.set(Calendar.DATE, 1);
      lastDay = calendar.getMaximum(Calendar.DAY_OF_MONTH);
    }
    int mon = calendar.get(calendar.MONTH)+1;
    mm = mon<10?"0"+mon:Integer.toString(mon);
    referenceNumber = "/"+calendar.get(calendar.YEAR)+
      mm+"/"+week;
    startDate = su.DateToString(calendar.getTime(), "yyyy/MM/dd");//DateHelper.formatAccountDate(calendar.getTime());
    calendar.set(Calendar.DATE, lastDay);
    endDate = su.DateToString(calendar.getTime(), "yyyy/MM/dd");//DateHelper.formatAccountDate(calendar.getTime());
    fileNumber = 1;
    recordNumber++;
    String headerRecord = billingSource + FIELD_SEPARATOR + fileNumber +
      FIELD_SEPARATOR + recordNumber + FIELD_SEPARATOR + HEADER +
      FIELD_SEPARATOR + startDate + FIELD_SEPARATOR +
      reportText2.substring(0, reportText2.indexOf(",")) +
      //reportText.substring(0, reportText.indexOf(",")) +
      NEWLINE;
    if (withLegacy)
    {
      addressFileWriter.write(headerRecord);
    }
    callRecordNumber++;
    headerRecord = billingSource + FIELD_SEPARATOR + fileNumber +
      FIELD_SEPARATOR + callRecordNumber + FIELD_SEPARATOR + HEADER +
      FIELD_SEPARATOR + startDate + FIELD_SEPARATOR + endDate +
      FIELD_SEPARATOR + reportDate + FIELD_SEPARATOR +
      reportText2.substring(0, reportText2.indexOf(",")) +
      //reportText.substring(0, reportText.indexOf(",")) +
      NEWLINE;
    if (withLegacy)
    {
      dbFileWriter.write(headerRecord);
    }
  }

  private void createTrailers()
    throws IOException
  {
    int actualCount = recordNumber - 1;
    recordNumber++;
    String trailerRecord = billingSource + FIELD_SEPARATOR + fileNumber +
      FIELD_SEPARATOR + recordNumber + FIELD_SEPARATOR + TRAILER +
      FIELD_SEPARATOR + actualCount +
      FIELD_SEPARATOR + NEWLINE;
    addressFileWriter.write(trailerRecord);
    actualCount = callRecordNumber - 1;
    callRecordNumber++;
    trailerRecord = billingSource + FIELD_SEPARATOR + fileNumber +
      FIELD_SEPARATOR + callRecordNumber + FIELD_SEPARATOR + TRAILER +
      FIELD_SEPARATOR + actualCount +
      FIELD_SEPARATOR + NEWLINE;
    dbFileWriter.write(trailerRecord);
  }

  private String removeUnprintable()
    throws IOException
  {
    StringBuffer contents = new StringBuffer();
    StringBuffer printOnly = new StringBuffer();
    contents.append(forNextTime);
    while (!endCustomer)
    {
      String line = br.readLine();
      if (line == null)
      {
        endCustomer = true;
        eof = true;
      }
      else
      {
        int accPos = line.indexOf(accountText);
        if (accPos != -1)
        {
          String tempStr = contents.toString();
          if (tempStr.indexOf(accountText) != -1)
          {
            endCustomer = true;
            String line2 = line.substring(0, accPos);
            int uprPos = line2.lastIndexOf(uprText);
            if (uprPos != -1)
            {
              contents.append(line.substring(0, uprPos));
              forNextTime = line.substring(uprPos);
            }
            else
            {
              forNextTime = tempStr.substring(tempStr.lastIndexOf(uprText)) + line;
            }
          }
          else
          {
            contents.append(line);
          }
        }
        else
        {
          contents.append(line);
        }
      }
    }
    String records = contents.toString();
    int locator = records.indexOf(textStart);

    while ((locator != -1))
    {
      locator++;
      int newLocator = records.indexOf(textStart, locator)==-1?records.length()
                            :records.indexOf(textStart, locator);
      int endLocator = records.indexOf(textEnd, locator)==-1?records.length()
                            :records.indexOf(textEnd, locator);
      int uprLocator = records.indexOf(uprChar, locator)==-1?records.length()
                            :records.indexOf(uprChar, locator);
      if (uprLocator < endLocator)
      {
        endLocator = uprLocator;
      }
      if (newLocator < endLocator)
      {
        locator = newLocator+1;
      }
      if (endLocator == -1)
      {
        endLocator = uprLocator;
      }
      String text = records.substring(locator, endLocator);
      int len = text.length();
      int pos = 0;
      boolean aN = true;
      while((pos < len) && (aN))
      {
        char thisChar = text.charAt(pos);
        int thisInt = thisChar;
        if (((thisInt < 32) || (thisInt > 122)) ||
              ((thisInt > 90) && (thisInt < 97)))
          aN = false;
        pos++;
      }
      if ((aN) && (text.length() > 0))
      {
        String newText = text;
        if (text.indexOf(',') != -1)
        {
          newText = text.replace(',',' ');
          if (newText.charAt(newText.length()-1) == ' ')
          {
            newText = newText.substring(0, newText.length()-1);
          }
        }
        String trimmed = newText.trim();
        if (trimmed.length() > 1)
          printOnly.append(trimmed+",");
      }
      locator = records.indexOf(textStart, endLocator);
    }
    return printOnly.toString();
  }

  private void createRecords(String cleanText, boolean withLegacy)
    throws IOException
  {
    Hashtable dateSummary = new Hashtable();
    Hashtable productSummary = new Hashtable();
    Hashtable productDateSummary = new Hashtable();
    Hashtable productNumberSummary = new Hashtable();
    //String dateInfo = "";
//System.out.println("top");
//System.out.println("cleanText:"+cleanText);
    int addressLocator = 0;
    int locator = cleanText.indexOf(accountText);
    int n = 0;
    while ((locator != -1))
    {
      BigDecimal invoiceTotalAmount = new BigDecimal(0);
      long invoiceTotalDuration = 0;
      long invoiceTotalCalls = 0;
//System.out.println("locator loop");
      double amountTotal = 0;
      String address = cleanText.substring(addressLocator, locator-1);
//System.out.println("address:"+address);
//System.out.println("addressLocator:"+addressLocator);
      String name = address.substring(0, address.indexOf(","));
      locator += 21;
      int endLocator = cleanText.indexOf(",", locator);
      String accountNo = cleanText.substring(locator, endLocator);
//System.out.println("accountNo:"+accountNo);
//System.out.println("endLocator:"+endLocator);
      recordNumber++;
      String line = billingSource + FIELD_SEPARATOR + fileNumber +
      FIELD_SEPARATOR + recordNumber + FIELD_SEPARATOR + ACCOUNT +
      FIELD_SEPARATOR + accountNo + FIELD_SEPARATOR + name + NEWLINE;
      if (withLegacy)
      {
        addressFileWriter.write(line);
      }
      xmlFileWriter = createFileWriter(outputDir + File.separator + accountNo +
        referenceNumber.replace('/','-') + ".xml");
      xmlFileWriter.write("<tb_invoices value=\"" + (weekly?"Weekly":"Monthly") +
        "\">" + NEWLINE);
      xmlFileWriter.write("<invoice>" + NEWLINE + "<customer_name>" +
        su.replaceString(name, "&", "&amp;") +
        "</customer_name>" + NEWLINE + "<account_number>" + accountNo +
        "</account_number>" + NEWLINE);
      recordNumber++;
      StringBuffer sb = new StringBuffer();
      StringBuffer xsb = new StringBuffer();
      xsb.append("<address>");
      String[] addressLine = new String[11];
      sb.append(billingSource + FIELD_SEPARATOR + fileNumber +
      FIELD_SEPARATOR + recordNumber + FIELD_SEPARATOR + ADDRESS +
      FIELD_SEPARATOR + accountNo +
      FIELD_SEPARATOR);
      int commaPos = 0;
      int a;
      for (a=0; a<10; a++)
      {
        int startPos = ++commaPos;
        commaPos = address.indexOf(",", commaPos);
        if (commaPos == -1)
        {
          addressLine[a] = address.substring(startPos, address.length());
          break;
        }
        else
        {
          addressLine[a] = address.substring(startPos, commaPos);
        }
      }
      for (int i=1; i<=a; i++)
      {
        sb.append(addressLine[i]);
        sb.append(FIELD_SEPARATOR);
        xsb.append("<address_line_" + i + ">" +
          su.replaceString(addressLine[i], "&", "&amp;") + "</address_line_" +
          i + ">");
      }
      for (int i=a; i<9; i++)
      {
        sb.append(FIELD_SEPARATOR);
        xsb.append("<address_line_" + (i+1) + "/>");
      }
      sb.append(NEWLINE);
      line = sb.toString();
      if (withLegacy)
      {
        addressFileWriter.write(line);
      }
      xsb.append("</address>" + NEWLINE);
      xmlFileWriter.write(xsb.toString());
      int startCalls = locator;
//System.out.println("locator:"+locator);
//System.out.println("endLocator:"+endLocator);
//System.out.println("accountText:"+accountText);
      locator = cleanText.indexOf(accountText, endLocator);
      int temp = 0;
//System.out.println("locator:"+locator);
      if (locator == -1)
      {
        addressLocator = cleanText.length()-2;
      }
      else
      {
        //temp = cleanText.lastIndexOf(reportText, locator)+26;
        temp = cleanText.lastIndexOf(reportText2, locator)+15;
//System.out.println("temp:"+temp);
        addressLocator = cleanText.indexOf(",", temp)+1;
        if (cleanText.charAt(addressLocator) == ',')
          addressLocator++;
      }
      //call processing
      int callLocator = startCalls;

//System.out.println("callLocator:"+callLocator);
//System.out.println("addressLocator:"+addressLocator);
      while (callLocator < addressLocator)
      {
//System.out.println("call processing loop");
        int endCalls = cleanText.indexOf(reportText, callLocator+1);
//System.out.println("callLocator:"+callLocator);
//System.out.println("creportText:"+reportText);
//System.out.println("endCalls:"+endCalls);

        //temp = endCalls+26;
        temp = endCalls+9;
        int endTemp = cleanText.indexOf(",", temp);
        String chargeType = cleanText.substring(temp, endTemp);
//System.out.println("chargeType:"+chargeType);
        int anotherLocator = cleanText.indexOf(callStartText, callLocator)+21;
        if ((anotherLocator > endTemp) || (anotherLocator == 20))
        {
          anotherLocator = cleanText.indexOf(callStartText2, callLocator)+22;
        }
        callLocator = anotherLocator;
        while (callLocator < endCalls)
        {
//System.out.println("call processing loop 2");
          int endCallLocator = callLocator-1;
          String calledNumber = "";
          String dateInfo = "";
          String dateKey = "";
          String dbDateInfo = "";
          String xmlDateInfo = "";
          String duration = "";
          String callCount = "";
          String chargeAmount = "";
          for (int i = 1; i < 6; i++)
          {
            int lastValid = endCallLocator;
            int tempStart = ++endCallLocator;
            endCallLocator = cleanText.indexOf(",", endCallLocator);
            String tempStr = cleanText.substring(tempStart, endCallLocator);
            boolean validCall = true;
            switch (i)
            {
              case 1:
                if ((tempStr.indexOf(".") != -1) || (tempStr.indexOf(":") != -1))
                  validCall = false;
                else
                {
                  try
                  {
                    double validNum = Double.parseDouble(tempStr);
                    calledNumber = tempStr;
                  }
                  catch (NumberFormatException nex)
                  {
                    validCall = false;
                  }
                }
                break;
              case 2:
                if ((tempStr.indexOf(".") != -1) || (tempStr.indexOf(":") != -1)
                    || (tempStr.length() != 6))
                  validCall = false;
                else
                {
                  String dd = tempStr.substring(0,2);
                  String mm = tempStr.substring(2,4);
                  String yy = tempStr.substring(4,6);
                  dateInfo = "20"+yy+","+mm+","+dd+","+week;
                  dateKey = "20"+yy+mm+dd;
                  dbDateInfo = tempStr;
                  xmlDateInfo = dd + "-" +
                    dfs.getShortMonths()[Integer.parseInt(mm)-1] + "-" +
                    (2000+Integer.parseInt(yy));
                }
                break;
              case 3:
                if ((tempStr.indexOf(".") == -1) || (tempStr.indexOf(":") != -1))
                  validCall = false;
                else
                  callCount = tempStr;
                break;
              case 4:
              int colon1 = tempStr.indexOf(":");
              if ((tempStr.indexOf(".") != -1) || (colon1 == -1))
                validCall = false;
              else
              {
                int colon2 = tempStr.indexOf(":", colon1+1);
                int hours = Integer.parseInt(tempStr.substring(0, colon1));
                int minutes = Integer.parseInt(tempStr.substring(colon1+1, colon2));
                int seconds = Integer.parseInt(tempStr.substring(colon2+1));
                duration = String.valueOf((hours*60*60)+(minutes*60)+seconds);
              }
                break;
              case 5:
                if ((tempStr.indexOf(".") == -1) || (tempStr.indexOf(":") != -1))
                  validCall = false;
                else
                  chargeAmount = tempStr;
                break;
            }
            if (!validCall)
            {
              String invalidCall = "";
              if (i == 1)
              {
                invalidCall = tempStr;
                callLocator = endCallLocator;
                if ((callLocator+1) >= endCalls)
                  callLocator++;
              }
              else
              {
                invalidCall = cleanText.substring(callLocator, lastValid);
                callLocator = ++lastValid;
              }
              line = accountNo+","+chargeType+","+invalidCall+NEWLINE;
              if (withLegacy)
              {
                misfitsFileWriter.write(line);
              }
              endCallLocator = callLocator;
              if (callLocator < endCalls)
                i = 0;
              else
                i = 6;
            }
          }
          String call = cleanText.substring(callLocator, endCallLocator).replace(',', FIELD_SEPARATOR);
          if (callLocator < endCalls)
          {
            callRecordNumber++;
            line = billingSource + FIELD_SEPARATOR + fileNumber +
            FIELD_SEPARATOR + callRecordNumber + FIELD_SEPARATOR + CALL +
            FIELD_SEPARATOR + accountNo + FIELD_SEPARATOR + accountNo +
            referenceNumber + FIELD_SEPARATOR + chargeType + FIELD_SEPARATOR +
            calledNumber + FIELD_SEPARATOR + dbDateInfo + FIELD_SEPARATOR +
            callCount + FIELD_SEPARATOR + duration + FIELD_SEPARATOR +
            chargeAmount + NEWLINE;
            if (withLegacy)
            {
              dbFileWriter.write(line);
            }
            long numCalls = Long.parseLong(callCount.substring(0, callCount.indexOf(".")));
            long callDuration = Long.parseLong(duration);
            BigDecimal chargeAmtBD = new BigDecimal(chargeAmount);
            invoiceTotalAmount = invoiceTotalAmount.add(chargeAmtBD);
            invoiceTotalDuration += callDuration;
            invoiceTotalCalls += numCalls;
            String pndKey = chargeType + calledNumber + dateKey;
            /*xmlFileWriter.write("<call>" + "<charge_type>" + chargeType +
              "</charge_type>" + "<number>" + calledNumber + "</number>" +
              "<date>" + xmlDateInfo + "</date>" + "<count>" + numCalls +
              "</count>" + "<duration>" + su.reformatLongDuration(callDuration) + "</duration>" +
              "<charge_amount>" + chargeAmount + "</charge_amount>" +
              "</call>" + NEWLINE);*/
            TeleBusinessSummaryDescriptor tbsd = /*dateSummary.containsKey(dateKey)
              ?(TeleBusinessSummaryDescriptor)dateSummary.get(dateKey)
              :*/new TeleBusinessSummaryDescriptor(chargeType, calledNumber, xmlDateInfo);
            tbsd.incrementNumberOfCalls(numCalls);
            tbsd.incrementDuration(Long.parseLong(duration));
            tbsd.incrementCost(chargeAmtBD);
            //if (!dateSummary.containsKey(productDateKey))
            //{
              productSummary.put(pndKey, tbsd);
            //}

            tbsd = dateSummary.containsKey(dateKey)
              ?(TeleBusinessSummaryDescriptor)dateSummary.get(dateKey)
              :new TeleBusinessSummaryDescriptor(xmlDateInfo);
            tbsd.incrementNumberOfCalls(numCalls);
            tbsd.incrementDuration(Long.parseLong(duration));
            tbsd.incrementCost(chargeAmtBD);
            if (!dateSummary.containsKey(dateKey))
            {
              dateSummary.put(dateKey, tbsd);
            }

            tbsd = productDateSummary.containsKey(chargeType + dateKey)
              ?(TeleBusinessSummaryDescriptor)productDateSummary.get(chargeType + dateKey)
              :new TeleBusinessSummaryDescriptor(chargeType, null, xmlDateInfo);
            tbsd.incrementNumberOfCalls(numCalls);
            tbsd.incrementDuration(Long.parseLong(duration));
            tbsd.incrementCost(chargeAmtBD);
            if (!productDateSummary.containsKey(chargeType + dateKey))
            {
              productDateSummary.put(chargeType + dateKey, tbsd);
            }

            tbsd = productNumberSummary.containsKey(chargeType + calledNumber)
              ?(TeleBusinessSummaryDescriptor)productNumberSummary.get(chargeType + calledNumber)
              :new TeleBusinessSummaryDescriptor(chargeType, calledNumber, null);
            tbsd.incrementNumberOfCalls(numCalls);
            tbsd.incrementDuration(Long.parseLong(duration));
            tbsd.incrementCost(chargeAmtBD);
            if (!productNumberSummary.containsKey(chargeType + dateInfo))
            {
              productNumberSummary.put(chargeType + calledNumber, tbsd);
            }

            line = name+","+accountNo+","+accountNo+referenceNumber+","+
            chargeType+","+calledNumber+","+dateInfo+","+callCount+","
            +chargeAmount+","+duration+NEWLINE;
            if (withLegacy)
            {
              deFileWriter.write(line);
            }
            callLocator = ++endCallLocator;
            amountTotal += Double.parseDouble(chargeAmount);
          }
          int nextC = cleanText.indexOf(",", callLocator);
          String nextS = cleanText.substring(callLocator, nextC);
          if ((nextS.indexOf(reportText.substring(0,7)) != -1))
          {
            callLocator = endCalls;
            int nextEnd = cleanText.indexOf(reportText, callLocator+1);
            if ((nextEnd > addressLocator) || (nextEnd == -1))
            {
              callLocator = addressLocator;
              if (addressLocator == cleanText.length()-2)
              {
                locator = -1;
              }
            }
          }
        }
      }
      callRecordNumber++;
      String at = Double.toString(amountTotal);
      String at1 = at.substring(0, at.indexOf("."));
      String at2 = at.substring(at.indexOf(".")+1);
      String atRound = "000";
      if (Long.parseLong(at2) > 0)
      {
        if (at2.length() > 3)
        {
          String at3 = at2.substring(0,3)+"."+at2.substring(3);
          double at4 = Double.parseDouble(at3);
          long at5 = Math.round(at4);
          if (at5 < 10)
            atRound = "00"+Long.toString(at5);
          else if (at5 < 100)
            atRound = "0"+Long.toString(at5);
          else
            atRound = Long.toString(at5);
        }
        else
        {
          atRound = at2;
        }
      }
      line = billingSource + FIELD_SEPARATOR + fileNumber +
      FIELD_SEPARATOR + callRecordNumber + FIELD_SEPARATOR + INVOICE +
      FIELD_SEPARATOR + accountNo + FIELD_SEPARATOR + accountNo +
      referenceNumber + FIELD_SEPARATOR + at1 + "." + atRound + NEWLINE;
      if (withLegacy)
      {
        dbFileWriter.write(line);
      }
      long totalNumberOfCalls = 0;
      long totalDuration = 0;
      BigDecimal totalCost = new BigDecimal(0);
      xmlFileWriter.write("<date_summary>" + NEWLINE);
      Enumeration enKeys = new AlphaSortedEnumeration(dateSummary.keys());

      String firstDate = "";
      String lastDate = "";
      boolean first = true;
      while (enKeys.hasMoreElements())
      {
        String key = (String)enKeys.nextElement();
        TeleBusinessSummaryDescriptor tbsd =
          (TeleBusinessSummaryDescriptor)dateSummary.get(key);
        xmlFileWriter.write("<item><date>" + tbsd.getCallDate() + "</date>" +
          "<number_of_calls>" + tbsd.getNumberOfCalls() + "</number_of_calls>" +
          "<duration>" + tbsd.getDurationPrint() + "</duration><cost>" +
          su.padWithZeros(tbsd.getCostPrint(), 3) + "</cost></item>" + NEWLINE);
        totalNumberOfCalls += tbsd.getNumberOfCalls();
        totalDuration += tbsd.getDuration();
        totalCost = totalCost.add(tbsd.getCost());
        if (first)
        {
          first = false;
          firstDate = key;
        }
        lastDate = key;
      }
      xmlFileWriter.write("<total_number_of_calls>" + totalNumberOfCalls +
        "</total_number_of_calls>" + NEWLINE + "<total_duration>" +
        su.reformatLongDuration(totalDuration) + "</total_duration>" + NEWLINE +
        "<total_cost>" + su.padWithZeros(totalCost.toString(), 3) +
        "</total_cost>" + NEWLINE + "</date_summary>" + NEWLINE);

      xmlFileWriter.write("<product_date_summary>" + NEWLINE);
      enKeys = new AlphaSortedEnumeration(productDateSummary.keys());

      String prevProduct = "";
      while (enKeys.hasMoreElements())
      {
        String key = (String)enKeys.nextElement();
        TeleBusinessSummaryDescriptor tbsd =
          (TeleBusinessSummaryDescriptor)productDateSummary.get(key);
        String thisProduct = tbsd.getProduct();
        if (!prevProduct.equals(thisProduct))
        {
          if (!prevProduct.equals(""))
          {
            xmlFileWriter.write("<total_number_of_calls>" + totalNumberOfCalls +
              "</total_number_of_calls>" + NEWLINE + "<total_duration>" +
              su.reformatLongDuration(totalDuration) + "</total_duration>" + NEWLINE +
              "<total_cost>" + su.padWithZeros(totalCost.toString(), 3) +
              "</total_cost>" + NEWLINE + "</product>" + NEWLINE);
          }
          xmlFileWriter.write("<product value=\"" + thisProduct + "\">" +
            NEWLINE);
          prevProduct = thisProduct;
          totalNumberOfCalls = 0;
          totalDuration = 0;
          totalCost = new BigDecimal(0);
        }
        xmlFileWriter.write("<item><date>" + tbsd.getCallDate() + "</date>" +
          "<number_of_calls>" + tbsd.getNumberOfCalls() + "</number_of_calls>" +
          "<duration>" + tbsd.getDurationPrint() + "</duration><cost>" +
          su.padWithZeros(tbsd.getCostPrint(), 3) + "</cost></item>" + NEWLINE);
        totalNumberOfCalls += tbsd.getNumberOfCalls();
        totalDuration += tbsd.getDuration();
        totalCost = totalCost.add(tbsd.getCost());
      }
      xmlFileWriter.write("<total_number_of_calls>" + totalNumberOfCalls +
        "</total_number_of_calls>" + NEWLINE + "<total_duration>" +
        su.reformatLongDuration(totalDuration) + "</total_duration>" + NEWLINE +
        "<total_cost>" + su.padWithZeros(totalCost.toString(), 3) +
        "</total_cost>" + NEWLINE + "</product>" + NEWLINE);
      xmlFileWriter.write("</product_date_summary>" + NEWLINE);

      xmlFileWriter.write("<product_number_summary>" + NEWLINE);
      enKeys = new AlphaSortedEnumeration(productNumberSummary.keys());

      prevProduct = "";
      while (enKeys.hasMoreElements())
      {
        String key = (String)enKeys.nextElement();
        TeleBusinessSummaryDescriptor tbsd =
          (TeleBusinessSummaryDescriptor)productNumberSummary.get(key);
        String thisProduct = tbsd.getProduct();
        if (!prevProduct.equals(thisProduct))
        {
          if (!prevProduct.equals(""))
          {
            xmlFileWriter.write("<total_number_of_calls>" + totalNumberOfCalls +
              "</total_number_of_calls>" + NEWLINE + "<total_duration>" +
              su.reformatLongDuration(totalDuration) + "</total_duration>" + NEWLINE +
              "<total_cost>" + su.padWithZeros(totalCost.toString(), 3) +
              "</total_cost>" + NEWLINE + "</product>" + NEWLINE);
          }
          xmlFileWriter.write("<product value=\"" + thisProduct + "\">" +
            NEWLINE);
          prevProduct = thisProduct;
          totalNumberOfCalls = 0;
          totalDuration = 0;
          totalCost = new BigDecimal(0);
        }
        xmlFileWriter.write("<item><called_number>" + tbsd.getCalledNumber() + "</called_number>" +
          "<number_of_calls>" + tbsd.getNumberOfCalls() + "</number_of_calls>" +
          "<duration>" + tbsd.getDurationPrint() + "</duration><cost>" +
          su.padWithZeros(tbsd.getCostPrint(), 3) + "</cost></item>" + NEWLINE);
        totalNumberOfCalls += tbsd.getNumberOfCalls();
        totalDuration += tbsd.getDuration();
        totalCost = totalCost.add(tbsd.getCost());
      }
      xmlFileWriter.write("<total_number_of_calls>" + totalNumberOfCalls +
        "</total_number_of_calls>" + NEWLINE + "<total_duration>" +
        su.reformatLongDuration(totalDuration) + "</total_duration>" + NEWLINE +
        "<total_cost>" + su.padWithZeros(totalCost.toString(), 3) +
        "</total_cost>" + NEWLINE + "</product>" + NEWLINE);
      xmlFileWriter.write("</product_number_summary>" + NEWLINE);

      xmlFileWriter.write("<product_summary>" + NEWLINE);
      enKeys = new AlphaSortedEnumeration(productSummary.keys());

      prevProduct = "";
      while (enKeys.hasMoreElements())
      {
        String key = (String)enKeys.nextElement();
        TeleBusinessSummaryDescriptor tbsd =
          (TeleBusinessSummaryDescriptor)productSummary.get(key);
        String thisProduct = tbsd.getProduct();
        if (!prevProduct.equals(thisProduct))
        {
          if (!prevProduct.equals(""))
          {
            xmlFileWriter.write("<total_number_of_calls>" + totalNumberOfCalls +
              "</total_number_of_calls>" + NEWLINE + "<total_duration>" +
              su.reformatLongDuration(totalDuration) + "</total_duration>" + NEWLINE +
              "<total_cost>" + su.padWithZeros(totalCost.toString(), 3) +
              "</total_cost>" + NEWLINE + "</product>" + NEWLINE);
          }
          xmlFileWriter.write("<product value=\"" + thisProduct + "\">" +
            NEWLINE);
          prevProduct = thisProduct;
          totalNumberOfCalls = 0;
          totalDuration = 0;
          totalCost = new BigDecimal(0);
        }
        xmlFileWriter.write("<item><called_number>" + tbsd.getCalledNumber() + "</called_number>" +
          "<date>" + tbsd.getCallDate() + "</date>" +
          "<number_of_calls>" + tbsd.getNumberOfCalls() + "</number_of_calls>" +
          "<duration>" + tbsd.getDurationPrint() + "</duration><cost>" +
          su.padWithZeros(tbsd.getCostPrint(), 3) + "</cost></item>" + NEWLINE);
        totalNumberOfCalls += tbsd.getNumberOfCalls();
        totalDuration += tbsd.getDuration();
        totalCost = totalCost.add(tbsd.getCost());
      }
      xmlFileWriter.write("<total_number_of_calls>" + totalNumberOfCalls +
        "</total_number_of_calls>" + NEWLINE + "<total_duration>" +
        su.reformatLongDuration(totalDuration) + "</total_duration>" + NEWLINE +
        "<total_cost>" + su.padWithZeros(totalCost.toString(), 3) +
        "</total_cost>" + NEWLINE + "</product>" + NEWLINE);
      xmlFileWriter.write("</product_summary>" + NEWLINE);

      xmlFileWriter.write("<reference_number>" + accountNo + referenceNumber +
        "</reference_number>" + NEWLINE + "<invoice_cost>" +
        su.padWithZeros(invoiceTotalAmount.toString(), 3) + "</invoice_cost>" +
        NEWLINE + "<invoice_duration>" + su.reformatLongDuration(invoiceTotalDuration) +
        "</invoice_duration>" + NEWLINE + "<invoice_number_of_calls>" +
        invoiceTotalCalls + "</invoice_number_of_calls>" + NEWLINE +
        "<report_date>" + xmlReportDate + "</report_date>" + NEWLINE +
        "<date_from>" + firstDate.substring(6,8) + "-" +
        dfs.getMonths()[Integer.parseInt(firstDate.substring(4,6))-1] + "-" +
        firstDate.substring(0,4) + "</date_from>" + NEWLINE +
        "<date_to>" + lastDate.substring(6,8) + "-" +
        dfs.getMonths()[Integer.parseInt(lastDate.substring(4,6))-1] + "-" +
        lastDate.substring(0,4) + "</date_to>" + NEWLINE +
        "</invoice>" + NEWLINE);
        xmlFileWriter.write("</tb_invoices>" + NEWLINE);
        xmlFileWriter.close();
    }
  }

  private void process(String stage)
  {
    try
    {
      File[] fileArray = (new File(dropDir)).listFiles();
      if (fileArray != null)
      {
        for (int i = 0; i < fileArray.length; i++)
        {
          String fileName = fileArray[i].getName();
          StringTokenizer st = new StringTokenizer(fileName, ".");
          if (st.countTokens() != 4)
          {
            writeToLogFile("Error: File name not in correct format: " + fileName);
          }
          else
          {
            String[] sections = new String[st.countTokens()];
            int tCount = 0;
            while (st.hasMoreTokens())
            {
              sections[tCount++] = st.nextToken();
            }
            weekly = sections[1].toUpperCase().equals(WEEKLY);
            if (stage.equalsIgnoreCase(ASCII))
            {
              convertEBCDICToASCII(fileName);
            }
            else if (stage.equalsIgnoreCase(EXTRACT))
            {
              extract(fileName, sections[1], sections[2], false);
            }
            else if (stage.equalsIgnoreCase(ALL))
            {
              convertEBCDICToASCII(fileName);
              extract(fileName, sections[1], sections[2], false);
            }
          }
        }
      }
    }
    catch (Exception ex)
    {
      writeToLogFile("Exception: " + ex.getMessage());
    }
    finally
    {
      closeLogFile();
    }
  }

  public static void main(String[] args)
  {
    TeleBusinessConvert tbc = new TeleBusinessConvert();
    tbc.process(args[0]);
  }

}



