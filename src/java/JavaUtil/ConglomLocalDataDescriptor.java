package JavaUtil;

import java.util.Collection;

public class ConglomLocalDataDescriptor {

  private String refType;
  private int colSeqNo;
  private int sortSeqNo;
  private Collection localReportData;

  public ConglomLocalDataDescriptor(String refType, int colSeqNo,
    int sortSeqNo)
  {
    this.refType = refType;
    this.colSeqNo = colSeqNo;
    this.sortSeqNo = sortSeqNo;
  }

  public String getRefType()
  {
    return refType;
  }

  public int getColSeqNo()
  {
    return colSeqNo;
  }

  public int getSortSeqNo()
  {
    return sortSeqNo;
  }

  public void setLocalReportData(Collection lrd)
  {
    localReportData = lrd;
  }

  public Collection getLocalReportData()
  {
    return localReportData;
  }

}