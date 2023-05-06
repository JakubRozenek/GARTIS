package database;

public class parts {
    private Integer jobPartsID;
    private Integer jobID;
    private String partName;
    private String partNumber;

    public Integer getJobPartsID() {
        return jobPartsID;
    }

    public void setJobPartsID(Integer jobPartsID) {
        this.jobPartsID = jobPartsID;
    }

    public Integer getJobID() {
        return jobID;
    }

    public void setJobID(Integer jobID) {
        this.jobID = jobID;
    }

    public String getPartName() {
        return partName;
    }

    public void setPartName(String partName) {
        this.partName = partName;
    }

    public String getPartNumber() {
        return partNumber;
    }

    public void setPartNumber(String partNumber) {
        this.partNumber = partNumber;
    }

    public parts(Integer jobPartsID, Integer jobID, String partName, String partNumber) {
        this.jobPartsID = jobPartsID;
        this.jobID = jobID;
        this.partName = partName;
        this.partNumber = partNumber;
    }
}
