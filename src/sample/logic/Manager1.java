package sample.logic;

import java.util.ArrayList;
import java.util.List;

public class Manager1 {
    private final List<Request> requests;
    private final Buffer buffer;
    private final List<Source> sources;

    public Manager1(final Buffer buffer,
                    final List<Source> sources) {
        requests = new ArrayList<>();
        this.buffer = buffer;
        this.sources = sources;
    }

    private void collectRequests() {
        for (final Source source : this.sources) {
            final Request r = source.getRequest();
            if (r != null) {
                requests.add(r);
            }
        }
    }

    private void fillBuffer() {
        for (final Request request : requests) {
            buffer.addRequest(request);
        }

        requests.clear();
    }

    public void work() {
        collectRequests();
        fillBuffer();
    }
}
