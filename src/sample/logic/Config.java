package sample.logic;

public final class Config {
    private Config() {
        //
    }

    public static int bufferLength = 8;
    public static int numOfSources = 8;
    public static int numOfWorkers = 5;
    public static double alf = 1.0;
    public static double bet = 1.1;
    public static double lambda = 1;
    public static int autoModeSteps = 10000;
}
