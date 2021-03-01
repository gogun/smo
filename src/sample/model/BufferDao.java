package sample.model;

public class BufferDao {
    String name;
    String elem;
    String pointer1;
    String pointer2;

    public BufferDao(String name, String elem, String pointer1, String pointer2) {
        this.name = name;
        this.elem = elem;
        this.pointer1 = pointer1;
        this.pointer2 = pointer2;
    }

    public String getName() {
        return name;
    }

    public String getElem() {
        return elem;
    }

    public String getPointer1() {
        return pointer1;
    }

    public String getPointer2() {
        return pointer2;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setElem(String elem) {
        this.elem = elem;
    }

    public void setPointer1(String pointer1) {
        this.pointer1 = pointer1;
    }

    public void setPointer2(String pointer2) {
        this.pointer2 = pointer2;
    }
}
