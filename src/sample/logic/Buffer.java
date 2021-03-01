package sample.logic;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class Buffer {

    private final int bufferLength;

    public List<Request> getBuffer() {
        return buffer;
    }

    private final List<Request> buffer;

    public int getPointer1() {
        return pointer1;
    }

    public int getPointer2() {
        return pointer2;
    }

    private int pointer1;
    private int pointer2;

    public Buffer(final int bufferLength) {
        this.bufferLength = bufferLength;
        this.buffer = new ArrayList<>();
        for (int i = 0; i < bufferLength; i++) {
            buffer.add(null);
        }
        pointer1 = 0;
        pointer2 = 0;
    }

    public void addRequest(final Request request) {
        boolean added = false;
        for (int i = pointer1; i < bufferLength; i++) {
            if (buffer.get(i) == null) {
                pointer1 = i + 1;
                buffer.set(i, request);
                added = true;
                break;
            }
        }

        if (added && pointer1 == bufferLength) {
            pointer1 = 0;
        }

        if (!added) {
            for (int i = 0; i < pointer1; i++) {
                if (buffer.get(i) == null) {
                    pointer1 = i + 1;
                    buffer.add(i, request);
                    added = true;
                    break;
                }
            }
        }

        if (!added) {
            denyRequest(request);
        }
    }

    private void denyRequest(Request request) {
        buffer.get(pointer1).denyBuffer();
        buffer.add(pointer1, request);
    }

    public Request getRequest() {
        for (int i = pointer2; i < bufferLength; i++) {
            if (buffer.get(i) != null) {
                final Request r = buffer.get(i);
                buffer.set(i, null);
                if (i + 1 == bufferLength) {
                    pointer2 = 0;
                } else {
                    pointer2 = i + 1;
                }

                r.leaveBuffer();
                return r;
            }
        }

        for (int i = 0; i < pointer2; i++) {
            if (buffer.get(i) != null) {
                final Request r = buffer.get(i);
                buffer.set(i, null);
                pointer2 = i + 1;

                r.leaveBuffer();
                return r;
            }
        }

        return null;
    }
}
