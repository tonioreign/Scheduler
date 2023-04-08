package sample.models;

public class MonthlyReport {
    public String apmtMonth;
    public int apmtTotal;

    /**
     * @param apmtMonth
     * @param apmtTotal
     */
    public MonthlyReport(String apmtMonth, int apmtTotal) {
        this.apmtMonth = apmtMonth;
        this.apmtTotal = apmtTotal;
    }

    /**GETTERS AND SETTERS*/
    public String getApmtMonth() {
        return apmtMonth;
    }

    public void setApmtMonth(String apmtMonth) {
        this.apmtMonth = apmtMonth;
    }

    public int getApmtTotal() {
        return apmtTotal;
    }

    public void setApmtTotal(int apmtTotal) {
        this.apmtTotal = apmtTotal;
    }
}
