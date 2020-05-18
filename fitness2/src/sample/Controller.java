//"Приложение позволяет указать вид тренировки: отжимание, скакалка, приседания.
// Доступны команды: начать тренировку (запускается таймер тренировки),
// закончить тренировку (таймер останавливается).
// За отработанное время высчитывается количество потраченных
// калорий по формуле K*t=cal, где K - количество калорий в час, затрачиваемое
// на определенный вид тренировки, t - время, засеченное трекером.
// От запуска к запуску программы данные должны сохраняться и общее количество калорий - суммироваться.
// Персистенция данных приложения с помощью ObjectOutputStream +5 баллов или JAXB + 10 баллов
// Поддержка нескольких профилей пользователей приложением + 5 баллов"
package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.File;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.io.File;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.FileReader;
import javax.print.attribute.standard.JobOriginatingUserName;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.Timer;
import java.util.jar.JarException;
import java.io.*;
import java.io.Serializable;
import java.util.stream.Collectors;

public class Controller implements Initializable {
    @FXML
    public ComboBox<String> cbTask;
    @FXML
    private Button btTask;
    @FXML
    private TextField tfTimer;
    @FXML
    private Label lbTimer, lbKkal;
    @FXML
    private TableView<Journal> tableTask;
    @FXML
    private TableColumn<Journal, String> dtName;
    @FXML
    private TableColumn<Journal, Integer> dtTime;
    @FXML
    private TableColumn<Journal, Integer> dtKkal;
    public String btText, tmTask, cbValue;
    public String filename = "src/sample/file.dat";
    public int finallyTimer, finallyKkal, allKkal;
    ObservableList<Journal> data = FXCollections.observableArrayList();
    public ObservableList<Journal> listfromfile;

    @Override
    public void initialize(URL url, ResourceBundle resources) {
        // Initialization code can go here.
        // The parameters url and resources can be omitted if they are not needed
        cbTask.getItems().setAll("Отжимание", "Приседание", "Прыжки");
        cbTask.setValue("Отжимание");
        TaskDeserialized();
        lbKkal.setText(String.valueOf(allKkal));
        dtName.setCellValueFactory(new PropertyValueFactory<Journal, String>("nameTask"));
        dtTime.setCellValueFactory(new PropertyValueFactory<Journal, Integer>("timeTask"));
        dtKkal.setCellValueFactory(new PropertyValueFactory<Journal, Integer>("kkalTask"));
        ObservableList<Journal> listfromfile = read();
        System.out.println("Lists equal? "+ listfromfile.equals(data));
        //ReadData();
        tableTask.setItems(listfromfile);
    }
    public void buttonClicked(javafx.event.ActionEvent actionEvent) throws IOException  {
        btText = btTask.getText();
        if (btText == "Завершить тренировку") {
            btTask.setText("Начать тренировку");
            stop();
            tmTask = tfTimer.getText();
            finallyTimer = Integer.parseInt(tmTask);
            reset();
            lbTimer.setText(tmTask);
            finallyKkal = finallyTimer * getCbTextKkal();
            allKkal=allKkal+finallyKkal;
            lbKkal.setText(String.valueOf(allKkal));
            TaskSerialized();
            data.add(new Journal(cbTask.getValue(), finallyTimer, finallyKkal));
            //WriteData();
            tableTask.setItems(data);
            write(data);
        } else {
            btTask.setText("Завершить тренировку");
            start();
        }
    }
    public static void main(String[] args){}
    public void start() {
        Thread thread = new Thread(runnable);
        thread.start();
        isStopped = false;
    }
    public void reset() {
        seconds = 0;
        tfTimer.setText(""+seconds);
    }
    public void stop() {
        isStopped = true;
    }
    int seconds = 0;
    boolean isStopped = false;
    Runnable runnable = new Runnable() {
        @Override
        public synchronized void run() {
            while (true) {
                try {
                    Thread.sleep(1000);
                    if (isStopped == false) {
                        seconds++;
                        tfTimer.setText(""+seconds);
                    }
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        }
    };
    public void TaskSerialized()
    {
        TaskModel object = new TaskModel(allKkal);
        // Сериализация
        try
        {
            // Сохранение объекта в файл
            FileOutputStream file = new FileOutputStream(filename);
            ObjectOutputStream out = new ObjectOutputStream(file);
            // Метод сериализации объекта
            out.writeObject(object);
            out.close();
            file.close();
        }
        catch(IOException ex)
        {
            System.out.println("IOException is caught");
        }
    }
    public void TaskDeserialized()
    {
        TaskModel object1 = null;
        try
        {
            // Чтение объекта из файла
            FileInputStream file = new FileInputStream(filename);
            ObjectInputStream in = new ObjectInputStream(file);
            // Способ десериализации объекта
            object1 = (TaskModel)in.readObject();
            in.close();
            file.close();
            allKkal = object1.kkal;
         }
        catch(IOException ex)
        {
            System.out.println("IOException is caught");
        }
        catch(ClassNotFoundException ex)
        {
            System.out.println("ClassNotFoundException is caught");
        }
    }
    public int getCbTextKkal() {
        int kkalValue = 0;
        cbValue = cbTask.getValue();
        if (cbValue=="Отжимание")
        {
            kkalValue = 100;
        }
        else if(cbValue=="Приседание")
        {
            kkalValue = 150;
        }
        else
            kkalValue = 50;
        return kkalValue;
    }

private static void write(List<Journal> datas ) {
    try {
        Path file = Paths.get("src/sample/journal.ser");

        OutputStream fos = Files.newOutputStream(file);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        List<JournalDto> dtos = datas.stream().map(d -> new JournalDto(d.getNameTask(), d.getTimeTask(), d.getKkalTask())).collect(Collectors.toList());
        oos.writeObject(dtos);
        oos.close();
    } catch (FileNotFoundException e) {
        e.printStackTrace();
    } catch (IOException e) {
        e.printStackTrace();
    }
}
    private static ObservableList<Journal> read() {
        try {
            Path file = Paths.get("src/sample/journal.ser");

            InputStream in = Files.newInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(in);
            List<Journal> data = ((List<JournalDto>) ois.readObject()).stream()
                    .map(o -> new Journal(o.getNameTask(), o.getTimeTask(), o.getKkalTask()))
                    .collect(Collectors.toList());

            return FXCollections.observableList(data);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return FXCollections.emptyObservableList();
    }
}
