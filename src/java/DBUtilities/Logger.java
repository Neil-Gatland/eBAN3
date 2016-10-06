package DBUtilities;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author
 * @version 1.0
 */
import java.io.*;
import java.util.Date;

public class Logger
{
  private PrintWriter writer;
  public Logger( String LogName) throws IOException
  {
    File dir;
    FileWriter filewriter;

    dir = new File(".","logs");
    if (!dir.exists())
    {
      dir.mkdir();
    }
    if (dir.isDirectory())
    {
      filewriter = new FileWriter(new File("logs",LogName));
      writer = new PrintWriter(filewriter,true);
    }
    else
    {
      throw new IOException("The log directory is a file");
    }
  }
}