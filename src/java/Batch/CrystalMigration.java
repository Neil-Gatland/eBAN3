package Batch;

import java.io.*;
import java.text.*;
import java.util.*;

public class CrystalMigration
{

  private CrystalMigration()
  {
  }

  private void migrateReports()
  {
    try
    {
      File controlFile = new File("D:\\temp\\cm\\control.txt");
      FileReader fr = new FileReader(controlFile);
      BufferedReader br = new BufferedReader(fr);
      File noCustFile = new File("D:\\temp\\cm\\nocust.txt");
      noCustFile.createNewFile();
      FileWriter fw1 = new FileWriter(noCustFile);
      BufferedWriter bw1 = new BufferedWriter(fw1);
      File noInvFile = new File("D:\\temp\\cm\\noinv.txt");
      noInvFile.createNewFile();
      FileWriter fw2 = new FileWriter(noInvFile);
      BufferedWriter bw2 = new BufferedWriter(fw2);
      File notFoundFile = new File("D:\\temp\\cm\\notfound.txt");
      notFoundFile.createNewFile();
      FileWriter fw3 = new FileWriter(notFoundFile);
      BufferedWriter bw3 = new BufferedWriter(fw3);
      /*
      File newerFile = new File("D:\\temp\\cm\\newer.txt");
      newerFile.createNewFile();
      FileWriter fw4 = new FileWriter(newerFile);
      BufferedWriter bw4 = new BufferedWriter(fw4);
      */
      SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

      boolean eof = false;
      while (!eof)
      {
        String line = br.readLine();
        if (line == null)
        {
          eof = true;
        }
        else
        {
          int start = 0;
          int pos = line.indexOf(",");
          String gcId = line.substring(start, pos).trim();
          start = ++pos;
          pos = line.indexOf(",", start);
          String invoiceNo = line.substring(start, pos).trim();
          start = ++pos;
          pos = line.indexOf(",", start);
          String reportName = line.substring(start, pos).trim();
          start = ++pos;
          pos = line.indexOf(",", start);
          String fileLocation = line.substring(start, pos).trim();
          File reportFile = new File(fileLocation);

          start = ++pos;
          String reportDate = line.substring(start, line.length()).trim();
          if (gcId.equals(""))
          {
            bw1.write(line+"\n");
          }
          else if (invoiceNo.equals(""))
          {
            bw2.write(line+"\n");
          }
          else if (!reportFile.exists())
          {
            bw3.write(line+"\n");
          }
          else
          {
            //process
            File gcDir = new File("D:\\temp\\cm\\drop\\" + gcId);
            if (!gcDir.isDirectory())
            {
              gcDir.mkdir();
              File invDir = new File("D:\\temp\\cm\\drop\\" + gcId + "\\" + invoiceNo);
              invDir.mkdir();
              Date lmDate = sdf.parse(reportDate);
              reportFile.setLastModified(lmDate.getTime());
              reportFile.renameTo(new File("D:\\temp\\cm\\drop\\" + gcId + "\\" +
                invoiceNo + "\\" + reportName + ".rpt"));
            }
            else
            {
              File invDir = new File("D:\\temp\\cm\\drop\\" + gcId + "\\" + invoiceNo);
              if (!invDir.isDirectory())
              {
                invDir.mkdir();
                Date lmDate = sdf.parse(reportDate);
                reportFile.setLastModified(lmDate.getTime());
                reportFile.renameTo(new File("D:\\temp\\cm\\drop\\" + gcId + "\\" +
                  invoiceNo + "\\" + reportName + ".rpt"));
              }
              else
              {
                File[] fileArray = invDir.listFiles();
                boolean found = false;
                for (int i = 0; i < fileArray.length; i++)
                {
                  String fileName = fileArray[i].getName();
                  if (fileName.equals(reportName + ".rpt"))
                  {
                    found = true;
                    long oldDate = fileArray[i].lastModified();
                    Date lmDate = sdf.parse(reportDate);
                    long newDate = lmDate.getTime();
                    if (newDate > oldDate) //check date format
                    {
                      fileArray[i].delete();
                      reportFile.setLastModified(newDate);
                      reportFile.renameTo(new File("D:\\temp\\cm\\drop\\" + gcId + "\\" +
                        invoiceNo + "\\" + reportName + ".rpt"));
//            bw4.write(line+"\n");

                    }
                    break;
                  }
                }

                if (!found)
                {
                  Date lmDate = sdf.parse(reportDate);
                  reportFile.setLastModified(lmDate.getTime());
                  reportFile.renameTo(new File("D:\\temp\\cm\\drop\\" + gcId + "\\" +
                    invoiceNo + "\\" + reportName + ".rpt"));
                }
              }
            }
          }
        }
      }
      br.close();
      fr.close();
      bw1.close();
      bw2.close();
      bw3.close();
      ////
 //     bw4.close();
    }
    catch (Exception ex)
    {
      System.out.println(ex.getMessage());
    }
  }

  public static void main(String[] args)
  {
    CrystalMigration cm = new CrystalMigration();
    cm.migrateReports();
  }

}



