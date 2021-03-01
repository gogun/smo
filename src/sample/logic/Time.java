package sample.logic;

public final class Time {
    private Time() {
        //
    }

    public static double TIME = 0;
    public static int STEP = 0;
    static double NEXT_TIME = 0;

    public static void resetTime() {
        TIME = 0;
        STEP = 0;
    }

    public static void updateTime(final double time) {
        if (NEXT_TIME == TIME) {
            NEXT_TIME = Integer.MAX_VALUE;
        }
        if (NEXT_TIME > time) {
            NEXT_TIME = time;
        }
    }

    public static void step() {
        TIME = NEXT_TIME;
        STEP += 1;
    }
}
