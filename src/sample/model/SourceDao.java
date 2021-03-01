package sample.model;

public class SourceDao {
    String name;
    String createTime;
    String completeCount;
    String denied;
    String meanSystemTime;
    String meanWaitTime;
    String meanCompletingTime;
    String denyProbability;

    public String getDispersion() {
        return dispersion;
    }

    String dispersion;

    public SourceDao(String name, String createTime, String completeCount, String denied, String meanSystemTime, String meanWaitTime, String meanCompletingTime, String denyProbability, String dispersion) {
        this.name = name;
        this.createTime = createTime;
        this.completeCount = completeCount;
        this.denied = denied;
        this.meanSystemTime = meanSystemTime;
        this.meanWaitTime = meanWaitTime;
        this.meanCompletingTime = meanCompletingTime;
        this.denyProbability = denyProbability;
        this.dispersion = dispersion;
    }

    public String getName() {
        return name;
    }

    public String getCreateTime() {
        return createTime;
    }

    public String getCompleteCount() {
        return completeCount;
    }

    public String getDenied() {
        return denied;
    }

    public String getMeanSystemTime() {
        return meanSystemTime;
    }

    public String getMeanWaitTime() {
        return meanWaitTime;
    }

    public String getMeanCompletingTime() {
        return meanCompletingTime;
    }

    public String getDenyProbability() {
        return denyProbability;
    }
}
