package sample.logic;

import java.util.*;

public class StatCollector {
    public double[] sourceCreated;
    public double[] sourceDenied;
    public double[] sourceCompleted;
    public double[] workersWorked;
    public double[] sourceWaitTime;
    public Map<Integer, List<Double>> timeInBuffer;
    public Stack<Integer> lastDenied;
    public double[] sourceAllTime;
    public double[] sourceWorkTime;
    public double[] workersLastRequestTime;

    public StatCollector() {
        sourceCreated = new double[Config.numOfSources];
        sourceDenied = new double[Config.numOfSources];
        sourceCompleted = new double[Config.numOfSources];
        workersWorked = new double[Config.numOfWorkers];
        sourceWaitTime = new double[Config.numOfSources];
        sourceAllTime = new double[Config.numOfSources];
        sourceWorkTime = new double[Config.numOfSources];
        workersLastRequestTime = new double[Config.numOfWorkers];
        timeInBuffer = new HashMap<>();
        lastDenied = new Stack<>();
    }

    public void created(final int i) {
        sourceCreated[i] += 1;
    }

    public void denied(final int i) {
        sourceDenied[i] += 1;
        lastDenied.add(i);
    }

    public void completed(final int i) {
        sourceCompleted[i] += 1;
    }

    public void addWorkerTime(final int num,
                              final double time) {
        workersLastRequestTime[num] = time;
    }

    public void addWorkerTime2(final int num,
                               final double time) {
        workersWorked[num] += time - workersLastRequestTime[num];
    }

    public void addWaitTime(final int num,
                            final double time) {
        sourceWaitTime[num] += time;
        timeInBuffer.computeIfAbsent(num, k -> new ArrayList<>());

        if (time != 0.0) {
            var tmp = timeInBuffer.get(num);
            tmp.add(time);
            timeInBuffer.put(num, tmp);
        }
    }

    public void addAllTime(final int num,
                           final double time) {
        sourceAllTime[num] += time;
    }

    public void addWorkTime(final int num,
                            final double time) {
        sourceWorkTime[num] += time;
    }
}
