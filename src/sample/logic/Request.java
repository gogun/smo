package sample.logic;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class Request {

    private final int numOfSources;
    private final double createTime;
    final StatCollector stat;
    NumberFormat formatter = new DecimalFormat("#0.00");

    public Request(final int numOfSources,
                   final double createTime,
                   final StatCollector stat) {
        this.numOfSources = numOfSources;
        this.createTime = createTime;
        this.stat = stat;
    }

    public double getCreateTime() {
        return createTime;
    }

    public String getId() {
        return numOfSources + ", " + formatter.format(createTime);
    }

    public void created() {
        stat.created(numOfSources);
    }

    public void leaveBuffer() {
        stat.addWaitTime(numOfSources, Time.TIME - createTime);
    }

    public void inWorker(final int num, final double time) {
        stat.addWorkTime(numOfSources, time - Time.TIME);
        stat.addWorkerTime(num, Time.TIME);
    }

    public void leaveWorker(final int num) {
        stat.addWorkerTime2(num, Time.TIME);
        stat.completed(numOfSources);
        stat.addAllTime(numOfSources, Time.TIME - createTime);
    }

    public void denyBuffer() {
        stat.denied(numOfSources);
//        stat.addWaitTime(numOfSources, Time.TIME);
    }
}
