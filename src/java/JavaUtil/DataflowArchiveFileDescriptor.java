package JavaUtil;

public class DataflowArchiveFileDescriptor {

  private long inputFileId;
  private long controlFileId;
  private long outputFileId;
  private String filePath;
  private String accountNo;

  public DataflowArchiveFileDescriptor(long inputFileId, long controlFileId,
    long outputFileId, String filePath, String accountNo)
  {
    this.inputFileId = inputFileId;
    this.controlFileId = controlFileId;
    this.outputFileId = outputFileId;
    this.filePath = filePath;
    this.accountNo = accountNo;
  }

  public long getControlFileId()
  {
    return controlFileId;
  }

  public long getInputFileId()
  {
    return inputFileId;
  }

  public long getOutputFileId()
  {
    return outputFileId;
  }

  public String getFilePath()
  {
    return filePath;
  }

  public String getAccountNo()
  {
    return accountNo;
  }

}