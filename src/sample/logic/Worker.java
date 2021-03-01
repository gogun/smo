package sample.logic;

import java.util.Random;

public class Worker {
    private final int num;
    private double timeOfRelease;

    public Request getCurrentRequest() {
        return currentRequest;
    }

    private Request currentRequest;

    public Worker(final int num) {
        this.num = num;
        this.currentRequest = null;
        this.timeOfRelease = 0;
    }

    public boolean isAvailable() {
        return currentRequest == null;
    }

    public void addRequest(final Request request) {
        currentRequest = request;
        timeOfRelease = calculateWorkTime();
        currentRequest.inWorker(num, timeOfRelease);
        Time.updateTime(timeOfRelease);
    }

    private double calculateWorkTime() {
        return Time.TIME + (((int)(Config.bet - Config.alf) * new Random().nextDouble()) + Config.alf);
    }

    public void tryToRelease() {
        if (Time.TIME >= timeOfRelease && !isAvailable()) {
            currentRequest.leaveWorker(num);
            currentRequest = null;
        } else if (!isAvailable()) {
            Time.updateTime(timeOfRelease);
        }
    }
}
