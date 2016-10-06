package Batch;

import java.io.*;

public class PdfTest
{

  private PdfTest()
  {
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

  private void copyPdf()
  {
    File inputPdf = new File("c:\\temp\\999999.pdf");
    File outputPdf = new File("c:\\temp\\999991.pdf");
    StringBuffer sb = new StringBuffer();
    try
    {
      if (!outputPdf.exists())
        outputPdf.createNewFile();
      FileReader fr = new FileReader(inputPdf);
      BufferedReader br = new BufferedReader(fr);
      boolean eof = false;
      while (!eof)
      {
        String line = br.readLine();
        if (line == null)
          eof = true;
        else
          sb.append(line + "\n");
      }
      br.close();
      /*InputStream in1 = new FileInputStream(inputPdf);
      DataInputStream in = new DataInputStream(in1);
      byte[] buf = new byte[1024];
      int len;
      while ((len = in.read(buf)) > 0)
      {
        sb.append(buf);
      }
      in.close();
      try
      {
        while (true)
        {
          sb.append(in.r.read());
        }
      }
      catch (EOFException eof)
      {
        in.close();
      }*/
      String tempPdf = sb.toString();
      FileWriter fw = new FileWriter(outputPdf);
      System.out.println("length: " + Integer.toString(tempPdf.length()));
      fw.write(tempPdf);
      fw.close();
    }
    catch (IOException iox)
    {
      System.out.println(iox.getMessage());
    }
  }

  public static void main(String[] args)
  {
    PdfTest pt = new PdfTest();
    pt.copyPdf();
  }

}



