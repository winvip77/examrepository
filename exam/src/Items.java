public class Items {
    private Integer n;
    private Double timeSeconds;
    private String resultNumber;

    public Items(Integer n, Double timeSeconds, String resultNumber) {
        this.n = n;
        this.timeSeconds = timeSeconds;
        this.resultNumber = resultNumber;
    }

    public Integer getN() {
        return n;
    }

    public void setN(Integer n) {
        this.n = n;
    }

    public Double getTimeSeconds() {
        return timeSeconds;
    }

    public void setTimeSeconds(Double timeSeconds) {
        this.timeSeconds = timeSeconds;
    }

    public String getResultNumber() {
        return resultNumber;
    }

    public void setResultNumber(String resultNumber) {
        this.resultNumber = resultNumber;
    }
}
