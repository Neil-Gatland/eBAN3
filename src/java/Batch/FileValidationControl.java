package Batch;

import java.io.*;
import java.util.Enumeration;
import DBUtilities.DBAccess;
import JavaUtil.EBANProperties;

public class FileValidationControl
{
  private DBAccess DBA;
  private boolean validType;
  private String suffix;
  private String newFilePath;
  private String newFileName;

  private FileValidationControl()
  {
    DBA = new DBAccess();
    DBA.startDB();

  }

  private void checkPathExists(String processDir, String[] sections)
  {
    StringBuffer path = new StringBuffer(processDir);
    for (int i = 0; i < 2; i++)
    {
      path.append(File.separator + sections[i]);
      File test = new File(path.toString());
      if (!test.isDirectory())
      {
      }
    }
  }

  private boolean validate(String fileName, String processDir)
  {
    boolean isValid = false;
    String[] sections = {"","","","",""};
    int count = 0;
    int start = 0;
    int pos = fileName.indexOf("_");
    try
    {    
        while (pos != -1)
        {
        sections[count] = fileName.substring(start, pos).toUpperCase();
        count++;
        start = ++pos;
        pos = fileName.indexOf("_", start);
        }
        int dot = fileName.indexOf(".");
        if (dot > 0)
        {
            if (fileName.indexOf(".", dot+1) == -1)
            {
                sections[count] = fileName.substring(start, dot).toUpperCase();
                if (count == 4)
                {
                    if (validSource(sections[0], sections[4]))
                    {
                        if (validType)
                        {
                            if (DBA.validCustomer(sections[1]))
                            {
                                if (DBA.validAccount(sections[1], sections[2]))
                                {
                                    if (DBA.validateInvoice(sections[2], sections[3].replace('-', '/')))
                                    {
                                        File dir = new File(processDir + File.separator + sections[0] +
                                        File.separator + sections[1] + File.separator + sections[2]);
                                        dir.mkdirs();
                                        newFilePath = processDir + File.separator + sections[0] +
                                        File.separator + sections[1] + File.separator + sections[2] +
                                        File.separator + sections[3] + "_" + sections[4] +
                                        fileName.substring(dot);
                                        newFileName = fileName;
                                        isValid = true;
                                    }
                                    else
                                    {
                                        suffix = "inv";
                                    }
                                }
                                else
                                {
                                    suffix = "acct";
                                }
                            }
                            else
                            {
                                suffix = "cust";
                            }
                        }
                        else
                        {
                            suffix = "type";
                        }
                    }
                    else
                    {
                        suffix = "source";
                    }
                }
                else if (count == 2)
                {
                    if (validSource("MS", sections[2]))
                    {
                        if (validType)
                        {
                            String gcId = DBA.validAccount(sections[0]);
                            if (gcId != null)
                            {
                                if (DBA.validateInvoice(sections[0], sections[1].replace('-', '/')))
                                {
                                    File dir = new File(processDir + File.separator + "MS" +
                                        File.separator + gcId + File.separator + sections[0]);
                                    dir.mkdirs();
                                    newFilePath = processDir + File.separator + "MS" +
                                        File.separator + gcId + File.separator + sections[0] +
                                        File.separator + sections[1] + "_" + sections[2] +
                                        fileName.substring(dot);
                                    newFileName = "MS_" + gcId + "_" + sections[0] + "_" + sections[1] +
                                        "_" + sections[2] + fileName.substring(dot);
                                    isValid = true;
                                }
                                else
                                {
                                    suffix = "inv";
                                }
                            }
                            else
                            {
                                suffix = "acct";
                            }
                        }
                        else
                        {
                            suffix = "type";
                        }
                    }
                    else
                    {
                        suffix = "source";
                    }
                }
                else if (count == 1)
                {
                    if (validSource("MS", sections[1]))
                    {
                        if (validType)
                        {
                            String gcId = DBA.validAccount(sections[0]);
                            if (gcId != null)
                            {
                                String invoiceNo = DBA.findInvoice(gcId, sections[0]);
                                if (invoiceNo != null)
                                {
                                    File dir = new File(processDir + File.separator + "MS" +
                                        File.separator + gcId + File.separator + sections[0]);
                                    dir.mkdirs();
                                    newFilePath = processDir + File.separator + "MS" +
                                        File.separator + gcId + File.separator + sections[0] +
                                        File.separator + invoiceNo + "_" + sections[1] +
                                        fileName.substring(dot);
                                    newFileName = "MS_" + gcId + "_" + sections[0] + "_" + invoiceNo +
                                        "_" + sections[1] + fileName.substring(dot);
                                    isValid = true;
                                }
                                else
                                {
                                    suffix = "inv";
                                }
                            }
                            else
                            {
                                suffix = "acct";
                            }
                        }
                        else
                        {
                            suffix = "type";
                        }
                    }
                    else
                    {
                        suffix = "source";
                    }
                }
                else
                {
                    suffix = "format";
                }
            }
            else
            {
                suffix = "suffix";
            }
        }
        else
        {
            suffix = "suffix";
        }
    }
    catch (Exception ex)
    {
        suffix = "format";
    }    
    finally
    {
        return isValid;
    }    
  }

  private boolean validSource(String source, String type)
  {
    Enumeration attachmentTypes = DBA.validateSource(source);
    validType = false;
    boolean isValid = attachmentTypes.hasMoreElements();
    while (attachmentTypes.hasMoreElements())
    {
      String thisType = (String)attachmentTypes.nextElement();
      if (thisType.equalsIgnoreCase(type))
      {
        validType = true;
        break;
      }
    }
    return isValid;
  }

  private void copy(File src, File dst)
  {
    try
    {
      InputStream in = new FileInputStream(src);
      OutputStream out = new FileOutputStream(dst);

      // Transfer bytes from in to out
      byte[] buf = new byte[1024];
      int len;
      while ((len = in.read(buf)) > 0) {
          out.write(buf, 0, len);
      }
      in.close();
      out.close();
    }
    catch (IOException iox)
    {
      System.out.println(iox.getMessage());
    }
  }

  private void validateFiles()
  {
    String userPath = EBANProperties.getEBANProperty(EBANProperties.USERPATH);
    String validPath = EBANProperties.getEBANProperty(EBANProperties.VALIDPATH);
    String dropDir = EBANProperties.getEBANProperty(EBANProperties.DROPDIR);
    String rejectDir = EBANProperties.getEBANProperty(EBANProperties.REJECTDIR);
    String processDir = userPath + File.separator +
      EBANProperties.getEBANProperty(EBANProperties.PROCESSDIR);
    File downloadDir = new File(userPath + File.separator + dropDir);
    File[] fileArray = downloadDir.listFiles();
    if (fileArray != null)
    {
      for (int i = 0; i < fileArray.length; i++)
      {
        String fileName = fileArray[i].getName();
        if (validate(fileName, processDir))
        {
          File newFile = new File(newFilePath);
          copy(fileArray[i], newFile);
          File newValidFile = new File(validPath + File.separator + newFileName);
          if (newValidFile.exists())
            newValidFile.delete();
          fileArray[i].renameTo(newValidFile);
        }
        else
        {
          fileArray[i].renameTo(new File(userPath + File.separator + rejectDir +
            File.separator + fileName + "." + suffix));
        }
      }
    }
  }

  public static void main(String[] args)
  {
    FileValidationControl fvc = new FileValidationControl();
    fvc.validateFiles();
  }

}



