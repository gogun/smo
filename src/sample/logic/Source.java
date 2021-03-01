package sample.logic;

import java.util.Random;

public class Source {

    private final int num;
    private final StatCollector stat;
    double timeForCreate;

    public Source(final int num,
                  final StatCollector stat) {
        this.num = num;
        this.timeForCreate = 0;
        this.stat = stat;
    }

    public Request getRequest() {
        if (timeForCreate <= Time.TIME) {
            timeForCreate = calcTime();
            final Request r = new Request(num, Time.TIME, stat);
            r.created();
            Time.updateTime(timeForCreate);
            return r;
        }

        Time.updateTime(timeForCreate);
        return null;
    }

    private double calcTime() {
        return Time.TIME + (-1/Config.lambda)*Math.log(new Random().nextDouble());
    }
}
