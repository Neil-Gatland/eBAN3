package JavaUtil;

import java.io.FilenameFilter;

public class VBSDailyZipFileFilter implements FilenameFilter
{
  public boolean accept(java.io.File f, String name)
  {
    boolean acceptable = false;
    return ((name.toLowerCase().endsWith(".zip")) &&
      (name.toLowerCase().startsWith("vbs")) &&
      (name.length() == 15) &&
      (StringUtil.validDate(name.substring(3, 11), "yyyyMMdd")));
  }
}
