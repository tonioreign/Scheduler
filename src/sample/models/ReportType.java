package sample.models;

public class ReportType {
    public String apmtType;
    public int apmtTotal;

    /**
     *
     * @param apmtTotal
     * @param apmtType
     */
    public ReportType(String apmtType, int apmtTotal) {
        this.apmtType = apmtType;
        this.apmtTotal = apmtTotal;
    }

    /**GETTER AND SETTERS*/
    public String getApmtType() {
        return apmtType;
    }

    public void setApmtType(String apmtType) {
        this.apmtType = apmtType;
    }

    public int getApmtTotal() {
        return apmtTotal;
    }

    public void setApmtTotal(int apmtTotal) {
        this.apmtTotal = apmtTotal;
    }
}
