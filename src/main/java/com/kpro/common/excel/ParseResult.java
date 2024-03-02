package com.kpro.common.excel;

import java.util.Objects;

public class ParseResult {
    private String status;
    private String importStatus;
    private String importErrDesc;
    private String importErrCode;
    private int totalLine;
    private int importedLine;
    private String verifyStatus;
    private int verifyTotalCnt;
    private int verifyFailedCnt;
    private int verifySuccessCnt;

    public ParseResult() {
    }

    public ParseResult(String status, String importStatus, String importErrDesc, String importErrCode, int totalLine, int importedLine, String verifyStatus, int verifyTotalCnt, int verifyFailedCnt, int verifySuccessCnt) {
        this.status = status;
        this.importStatus = importStatus;
        this.importErrDesc = importErrDesc;
        this.importErrCode = importErrCode;
        this.totalLine = totalLine;
        this.importedLine = importedLine;
        this.verifyStatus = verifyStatus;
        this.verifyTotalCnt = verifyTotalCnt;
        this.verifyFailedCnt = verifyFailedCnt;
        this.verifySuccessCnt = verifySuccessCnt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getImportStatus() {
        return importStatus;
    }

    public void setImportStatus(String importStatus) {
        this.importStatus = importStatus;
    }

    public String getImportErrDesc() {
        return importErrDesc;
    }

    public void setImportErrDesc(String importErrDesc) {
        this.importErrDesc = importErrDesc;
    }

    public String getImportErrCode() {
        return importErrCode;
    }

    public void setImportErrCode(String importErrCode) {
        this.importErrCode = importErrCode;
    }

    public int getTotalLine() {
        return totalLine;
    }

    public void setTotalLine(int totalLine) {
        this.totalLine = totalLine;
    }

    public int getImportedLine() {
        return importedLine;
    }

    public void setImportedLine(int importedLine) {
        this.importedLine = importedLine;
    }

    public String getVerifyStatus() {
        return verifyStatus;
    }

    public void setVerifyStatus(String verifyStatus) {
        this.verifyStatus = verifyStatus;
    }

    public int getVerifyTotalCnt() {
        return verifyTotalCnt;
    }

    public void setVerifyTotalCnt(int verifyTotalCnt) {
        this.verifyTotalCnt = verifyTotalCnt;
    }

    public int getVerifyFailedCnt() {
        return verifyFailedCnt;
    }

    public void setVerifyFailedCnt(int verifyFailedCnt) {
        this.verifyFailedCnt = verifyFailedCnt;
    }

    public int getVerifySuccessCnt() {
        return verifySuccessCnt;
    }

    public void setVerifySuccessCnt(int verifySuccessCnt) {
        this.verifySuccessCnt = verifySuccessCnt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ParseResult)) return false;
        ParseResult that = (ParseResult) o;
        return getTotalLine() == that.getTotalLine() && getImportedLine() == that.getImportedLine() && getVerifyTotalCnt() == that.getVerifyTotalCnt() && getVerifyFailedCnt() == that.getVerifyFailedCnt() && getVerifySuccessCnt() == that.getVerifySuccessCnt() && Objects.equals(getStatus(), that.getStatus()) && Objects.equals(getImportStatus(), that.getImportStatus()) && Objects.equals(getImportErrDesc(), that.getImportErrDesc()) && Objects.equals(getImportErrCode(), that.getImportErrCode()) && Objects.equals(getVerifyStatus(), that.getVerifyStatus());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getStatus(), getImportStatus(), getImportErrDesc(), getImportErrCode(), getTotalLine(), getImportedLine(), getVerifyStatus(), getVerifyTotalCnt(), getVerifyFailedCnt(), getVerifySuccessCnt());
    }

    @Override
    public String toString() {
        return "ParseResult{" +
                "status='" + status + '\'' +
                ", importStatus='" + importStatus + '\'' +
                ", importErrDesc='" + importErrDesc + '\'' +
                ", importErrCode='" + importErrCode + '\'' +
                ", totalLine=" + totalLine +
                ", importedLine=" + importedLine +
                ", verifyStatus='" + verifyStatus + '\'' +
                ", verifyTotalCnt=" + verifyTotalCnt +
                ", verifyFailedCnt=" + verifyFailedCnt +
                ", verifySuccessCnt=" + verifySuccessCnt +
                '}';
    }
}
