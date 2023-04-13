package sample.models;

public class MonthlyReport {
    /**
     * The month for which the report is generated.
     */
    public String apmtMonth;

    /**
     * The total number of appointments for the month.
     */
    public int apmtTotal;

    /**
     * Constructs a new MonthlyReport object with the given parameters.
     *
     * @param apmtMonth  The month for which the report is generated.
     * @param apmtTotal  The total number of appointments for the month.
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
