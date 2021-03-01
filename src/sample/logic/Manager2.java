package sample.logic;

import java.util.List;

public class Manager2 {

    final Buffer buffer;
    final List<Worker> workers;

    public Manager2(final Buffer buffer,
                    final List<Worker> workers) {
        this.buffer = buffer;
        this.workers = workers;
    }

    private void fillWorkers() {
        for (final Worker worker : workers) {
            if (worker.isAvailable()) {
                final Request request = buffer.getRequest();
                if (request != null) {
                    worker.addRequest(request);
                } else {
                    break;
                }
            }
        }
    }

    public void work() {
        for (final Worker worker : workers) {
            worker.tryToRelease();
        }
        fillWorkers();
    }

}
