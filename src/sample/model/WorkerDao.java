package sample.model;

public class WorkerDao {
    String workerName;
    String workerElem;

    public WorkerDao(String workerName, String workerElem) {
        this.workerName = workerName;
        this.workerElem = workerElem;
    }

    public String getWorkerName() {
        return workerName;
    }

    public void setWorkerName(String workerName) {
        this.workerName = workerName;
    }

    public String getWorkerElem() {
        return workerElem;
    }

    public void setWorkerElem(String workerElem) {
        this.workerElem = workerElem;
    }
}
