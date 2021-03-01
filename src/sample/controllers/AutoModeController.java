package sample.controllers;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import sample.logic.*;
import sample.model.DeviceDao;
import sample.model.SourceDao;

import java.net.URL;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class AutoModeController implements Initializable {

    @FXML
    TableView<SourceDao> table;
    @FXML
    TableView<DeviceDao> table2;
    private StatCollector stat;
    private final ObservableList<SourceDao> data = FXCollections.observableArrayList();
    private final ObservableList<DeviceDao> data2 = FXCollections.observableArrayList();
    @FXML
    TableColumn<SourceDao, String> name;
    @FXML
    TableColumn<SourceDao, String> createTime;
    @FXML
    TableColumn<SourceDao, String> completeCount;
    @FXML
    TableColumn<SourceDao, String> denied;
    @FXML
    TableColumn<SourceDao, String> meanSystemTime;
    @FXML
    TableColumn<SourceDao, String> meanWaitTime;
    @FXML
    TableColumn<SourceDao, String> meanCompletingTime;
    @FXML
    TableColumn<SourceDao, String> denyProbability;
    @FXML
    TableColumn<SourceDao, String> dispersion;

    @FXML
    TableColumn<DeviceDao, String> nameDevice;

    @FXML
    TableColumn<DeviceDao, String> coef;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        stat = new StatCollector();
        mainWork();
        init();
        table.setItems(data);
        table.setFixedCellSize(25);
        table.prefHeightProperty().bind(table.fixedCellSizeProperty().multiply(Bindings.size(table.getItems()).add(1.5)));
        table.minHeightProperty().bind(table.prefHeightProperty());
        table.maxHeightProperty().bind(table.prefHeightProperty());

        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        createTime.setCellValueFactory(new PropertyValueFactory<>("createTime"));
        completeCount.setCellValueFactory(new PropertyValueFactory<>("completeCount"));
        denied.setCellValueFactory(new PropertyValueFactory<>("denied"));
        meanSystemTime.setCellValueFactory(new PropertyValueFactory<>("meanSystemTime"));
        meanWaitTime.setCellValueFactory(new PropertyValueFactory<>("meanWaitTime"));
        meanCompletingTime.setCellValueFactory(new PropertyValueFactory<>("meanCompletingTime"));
        denyProbability.setCellValueFactory(new PropertyValueFactory<>("denyProbability"));
        dispersion.setCellValueFactory(new PropertyValueFactory<>("dispersion"));

        table2.setItems(data2);
        table2.setFixedCellSize(25);
        table2.prefHeightProperty().bind(table.fixedCellSizeProperty().multiply(Bindings.size(table.getItems()).add(1.5)));
        table2.minHeightProperty().bind(table.prefHeightProperty());
        table2.maxHeightProperty().bind(table.prefHeightProperty());

        nameDevice.setCellValueFactory(new PropertyValueFactory<>("name"));
        coef.setCellValueFactory(new PropertyValueFactory<>("coef"));
    }

    private void mainWork() {
        Time.resetTime();
        final Buffer buffer = new Buffer(Config.bufferLength);
        final List<Source> sources = new ArrayList<>(Config.numOfSources);
        for (int i = 0; i < Config.numOfSources; i++) {
            sources.add(new Source(i, this.stat));
        }
        final Manager1 manager1 = new Manager1(buffer, sources);
        final List<Worker> workers = new ArrayList<>(Config.numOfWorkers);
        for (int i = 0; i < Config.numOfWorkers; i++) {
            workers.add(new Worker(i));
        }
        final Manager2 manager2 = new Manager2(buffer, workers);

        for (int i = 0; i < Config.autoModeSteps; i++) {
            manager1.work();
            manager2.work();
            Time.step();
        }
    }

    private void init() {
        NumberFormat formatter = new DecimalFormat("#0.00");
        for (int i = 2; i < 2 + Config.numOfSources; i++) {
            String name = "Source " + (i - 2);
            String createTime = String.valueOf((int) stat.sourceCreated[i - 2]);
            String completeCount = String.valueOf((int) stat.sourceCompleted[i - 2]);
            String denied = String.valueOf((int) (stat.sourceDenied[i - 2]));
            String meanSystemTime = String.valueOf(formatter.format(stat.sourceAllTime[i - 2] / (stat.sourceCompleted[i - 2] + stat.sourceDenied[i - 2])));
            String meanWaitTime = String.valueOf(formatter.format(stat.sourceWaitTime[i - 2] / (stat.sourceCompleted[i - 2] + stat.sourceDenied[i - 2])));
            String meanCompletingTime = String.valueOf(formatter.format(stat.sourceWorkTime[i - 2] / stat.sourceCompleted[i - 2]));
            String denyProbability = formatter.format(stat.sourceDenied[i - 2] / stat.sourceCreated[i - 2] * 100) + " %";
            String dispersion = formatter.format(getDisper(i - 2));
            data.add(new SourceDao(name, createTime, completeCount, denied, meanSystemTime, meanWaitTime, meanCompletingTime, denyProbability, dispersion));
        }

        for (int i = Config.numOfSources + 4; i < Config.numOfSources + 4 + Config.numOfWorkers; i++) {
            String name = "Device " + (i - Config.numOfSources - 4);
            String coef = formatter.format((stat.workersWorked[i - Config.numOfSources - 4] / Time.TIME) * 100) + " %";
            data2.add(new DeviceDao(name, coef));
        }
    }

    private double getDisper(int num) {
        double result = 0;
        var arr = stat.timeInBuffer.get(num);

        for (var it : arr) {
            result += Math.pow(it - (stat.sourceWaitTime[num] / (stat.sourceCompleted[num] + stat.sourceDenied[num])), 2);
        }

        return result / (arr.size());
    }
}
