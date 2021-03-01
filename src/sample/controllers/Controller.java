package sample.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import sample.Main;
import sample.logic.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Controller {

    @FXML
    private Button autoModeBtn;
    private StatCollector stat;


    @FXML
    public void autoModeBtnClick(ActionEvent actionEvent) throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("../res/auto_mode.fxml"));
        Stage primaryStage = new Stage();
        primaryStage.setTitle("Auto mode");
        primaryStage.setScene(new Scene(root, 1100, 500));
        primaryStage.show();
        Main.primaryStage.close();
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

    @FXML
    public void stepModeBtnClick(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("../res/step_mode.fxml"));
        Stage primaryStage = new Stage();
        primaryStage.setTitle("Step mode");
        primaryStage.setScene(new Scene(root, 950, 500));
        primaryStage.show();
        Main.primaryStage.close();
    }

    @FXML
    public void graph1(ActionEvent actionEvent) throws IOException {
        Stage primaryStage = new Stage();
        primaryStage.setTitle("Графики");

        NumberAxis x = new NumberAxis();
        NumberAxis y = new NumberAxis();

        LineChart<Number, Number> numberLineChart = new LineChart<Number, Number>(x,y);
        numberLineChart.setTitle("Графики");
        XYChart.Series series1 = new XYChart.Series();
        XYChart.Series series2 = new XYChart.Series();
        XYChart.Series series3 = new XYChart.Series();
        XYChart.Series series4 = new XYChart.Series();
        series1.setName("Вероятность отказа от размера буфера");
        series2.setName("Вероятность отказа от интенсивности источника");
        series3.setName("Вероятность отказа от интенсивности прибора");
        series4.setName("загруженность прибора от числа источников");
        ObservableList<XYChart.Data> datas = FXCollections.observableArrayList();
        ObservableList<XYChart.Data> datas1 = FXCollections.observableArrayList();
        ObservableList<XYChart.Data> datas2 = FXCollections.observableArrayList();
        ObservableList<XYChart.Data> datas3 = FXCollections.observableArrayList();
        var tmp = Config.bufferLength;
        for(int i=1; i<10; i++){
            Config.bufferLength = i;
            stat = new StatCollector();
            mainWork();
            datas.add(new XYChart.Data(i,stat.sourceDenied[0] / stat.sourceCreated[0] * 100));
        }
        Config.bufferLength = tmp;

        var tmp1 = Config.lambda;
        for(float i=1; i<10; i+=0.5){
            Config.lambda = i;
            stat = new StatCollector();
            mainWork();
            datas1.add(new XYChart.Data(i,stat.sourceDenied[0] / stat.sourceCreated[0] * 100));
        }
        Config.lambda = tmp1;

        var tmp2 = Config.bet;
        var tmpAlf = Config.alf;
        Config.bet = 0.1;
        for(float i=10f; i>0; i-=0.5) {
            Config.alf = i;
            stat = new StatCollector();
            mainWork();
            datas2.add(new XYChart.Data(10-i,stat.sourceDenied[0] / stat.sourceCreated[0] * 100));
        }
        Config.alf = tmpAlf;
        Config.bet = tmp2;

        var tmp3 = Config.numOfSources;
        for(int i=1; i<10; i++){
            Config.numOfSources = i;
            stat = new StatCollector();
            mainWork();
            datas3.add(new XYChart.Data(i,(stat.workersWorked[0] / Time.TIME) * 100));
        }
        Config.numOfSources = tmp3;


        series1.setData(datas);
        series2.setData(datas1);
        series3.setData(datas2);
        series4.setData(datas3);

        Scene scene = new Scene(numberLineChart, 600,600);
        numberLineChart.getData().add(series1);
        numberLineChart.getData().add(series2);
        numberLineChart.getData().add(series3);
        numberLineChart.getData().add(series4);
        primaryStage.setScene(scene);

        primaryStage.show();
    }


}
