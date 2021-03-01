package sample.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import sample.logic.*;
import sample.model.BufferDao;
import sample.model.WorkerDao;


import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class StepModeController implements Initializable {
    @FXML
    public Label denied;

    private StatCollector stat;
    private Buffer buffer;
    private final ObservableList<BufferDao> data = FXCollections.observableArrayList();
    private final ObservableList<WorkerDao> data2 = FXCollections.observableArrayList();
    NumberFormat formatter = new DecimalFormat("#0.00");

    @FXML
    TableView<BufferDao> bufferTable;


    @FXML
    TableColumn<BufferDao, String> name;

    @FXML
    TableColumn<BufferDao, String> elem;

    @FXML
    TableColumn<BufferDao, String> pointer1;

    @FXML
    TableColumn<BufferDao, String> pointer2;

    @FXML
    TableView<WorkerDao> table;

    @FXML
    TableColumn<WorkerDao, String> workerName;

    @FXML
    TableColumn<WorkerDao, String> workerElem;

    @FXML
    Label timeLabel;

    @FXML
    Label stepLabel;
    private Manager1 manager1;
    private Manager2 manager2;
    private List<Worker> workers;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initSystem();
        initUi();
    }

    private void initUi() {

        for (int i = 0; i < Config.bufferLength; i++) {
            data.add(new BufferDao("Buffer " + i, "empty", "", ""));
        }

        bufferTable.setItems(data);

        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        elem.setCellValueFactory(new PropertyValueFactory<>("elem"));
        pointer1.setCellValueFactory(new PropertyValueFactory<>("pointer1"));
        pointer2.setCellValueFactory(new PropertyValueFactory<>("pointer2"));

        for (int i = 0; i < Config.numOfWorkers; i++) {
            data2.add(new WorkerDao("Device " + i, "free"));
        }

        table.setItems(data2);

        workerName.setCellValueFactory(new PropertyValueFactory<>("workerName"));
        workerElem.setCellValueFactory(new PropertyValueFactory<>("workerElem"));

    }

    private void initSystem() {
        Time.resetTime();
        this.stat = new StatCollector();
        this.buffer = new Buffer(Config.bufferLength);
        final List<Source> sources = new ArrayList<>(Config.numOfSources);
        for (int i = 0; i < Config.numOfSources; i++) {
            sources.add(new Source(i, this.stat));
        }
        this.manager1 = new Manager1(buffer, sources);
        this.workers = new ArrayList<>(Config.numOfWorkers);
        for (int i = 0; i < Config.numOfWorkers; i++) {
            workers.add(new Worker(i));
        }
        this.manager2 = new Manager2(buffer, workers);
    }

    @FXML
    public void oneStep(ActionEvent actionEvent) throws IOException {
        this.step();
        this.fillBuffer();
        this.fillWorkers();
        timeLabel.setText(formatter.format(Time.TIME));
        stepLabel.setText(String.valueOf(Time.STEP));
        if (stat.lastDenied.size() > 0) {
            denied.setText(String.valueOf(stat.lastDenied.peek()));
        }
    }

    @FXML
    public void tenSteps(ActionEvent actionEvent) throws IOException {
        for (int i =0; i < 10; i++) {
            this.step();
        }
        this.fillBuffer();
        this.fillWorkers();
        timeLabel.setText(formatter.format(Time.TIME));
        stepLabel.setText(String.valueOf(Time.STEP));
        if (stat.lastDenied.size() > 0) {
            denied.setText(String.valueOf(stat.lastDenied.peek()));
        }
    }

    private void fillWorkers() {
        for (int i = 0; i < Config.numOfWorkers; i++) {
            var v = workers.get(i).getCurrentRequest();
            var e = data2.get(i);
            if (v == null) {
                e.setWorkerElem("free");
            } else {
                e.setWorkerElem((v.getId()));
            }
            data2.set(i, e);
        }
    }

    private void fillBuffer() {
        for (int i = 0; i < Config.bufferLength; i++) {
            var v = buffer.getBuffer().get(i);
            var e = data.get(i);
            if (v == null) {
                e.setElem("empty");
            } else {
                e.setElem(v.getId());
            }
            if (i == buffer.getPointer1()) {
                e.setPointer1("-->");
            } else {
                e.setPointer1("");
            }
            if (i == buffer.getPointer2()) {
                e.setPointer2("<--");
            } else {
                e.setPointer2("");
            }
            data.set(i, e);
        }
    }

    private void step() {
        manager1.work();
        manager2.work();
        Time.step();
    }
}
