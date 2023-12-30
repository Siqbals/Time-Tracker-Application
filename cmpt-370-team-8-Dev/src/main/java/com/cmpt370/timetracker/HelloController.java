package com.cmpt370.timetracker;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.Border;
import javafx.scene.layout.VBox;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.control.Alert;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimerTask;

import static com.cmpt370.timetracker.DataSave.deserialize;

public class HelloController {

    @FXML
    private Button CancelButton;

    @FXML
    private DatePicker DatePicker;

    @FXML
    private Button Editbutton;

    @FXML
    private VBox NewTaskVBox;

    @FXML
    private Button CreateNewTaskButton;

    @FXML
    private Button ExitButton;

    @FXML
    private AnchorPane LeftArrow;

    @FXML
    private MenuButton MenuButton;

    @FXML
    private Button MinimizeViewButton;

    @FXML
    private Text OngoingTask;

    @FXML
    private ProgressBar ProcessBar;

    @FXML
    private Text SelectADay;

    @FXML
    private Button ShowOngoingTask;

    @FXML
    private Button StartButton;

    @FXML
    private ImageView StartButtonImage;


    @FXML
    private Label time1;
    @FXML
    private Text TaskForTheDay;

    @FXML
    private Label timerLabel;

    private Stage settings;

    private Timeline timeline;
    protected Notification notification;

    private Model model;

    private ScheduledTimeBlock previousTimeBlock;

    SettingsController settingsController;

    @FXML
    public void initialize() throws IOException {
        this.model = new ModelImpl(new IdleDetector(MenuButton), new HotkeyHandler(MenuButton));
        this.timerLabel.setText("00:00:00");
        this.timeline = createTimeline();
        this.notification = new Notification();
        timeline.play();
        //StartButton.setDisable(true);
        CancelButton.setDisable(true);
        time1.setStyle("-fx-background-color: transparent;");
        time1.setFont(javafx.scene.text.Font.font("System", 24));

        // Setup settings window
        settingsController = new SettingsController(this.model);
        FXMLLoader settingsFXMLLoader = new FXMLLoader(HelloApplication.class.getResource("settings.fxml"));
        settingsFXMLLoader.setController(settingsController);
        Scene settingsScene = new Scene(settingsFXMLLoader.load(), 600, 800);
        settings = new Stage();
        settings.setTitle("Settings");
        settings.setScene(settingsScene);

        /**
         * Called every time the user goes idle, or returns from being idle.
         * @param event Data regarding the idle event, including how long the user has
         *              been idle for.
         */
        MenuButton.addEventHandler(IdleEvent.IDLE_EVENT, (IdleEvent event) -> {
            // inside the Notifications class & the Model (Stopping & Starting the timer)
            Day day = this.model.getSchedule().getCurrDay();
            if (day.getCurrentTimeBlock() == null) {
                return;
            }
            if (event.getMessage() == IdleMessage.BECAME_IDLE) {
                if (day.getCurrentTimeBlock().isRunning()) {
                    day.getCurrentTimeBlock().setPaused(true);
                    day.getCurrentTimeBlock().stop();
                }
            } else if (event.getMessage() == IdleMessage.RETURNED_FROM_IDLE) {
                if (day.getCurrentTimeBlock().isPaused()) {
                    day.getCurrentTimeBlock().setPaused(false);
                    day.getCurrentTimeBlock().start();
                }
            }
        });

        /**
         * Called every time a hotkey is pressed
         * @param event Data regarding the hotkey event, including what action
         *              the hotkey is bound to
         */
        MenuButton.addEventHandler(HotkeyEvent.HOTKEY_EVENT, (HotkeyEvent event) -> {
            LocalDate date = DatePicker.getValue();
            Day day = model.getSchedule().getDay(date.getDayOfMonth(), date.getMonthValue(), date.getYear());
            if (event.getAction() == HotkeyAction.START_TIMER) {
                day.getCurrentTimeBlock().start();
            } else if (event.getAction() == HotkeyAction.STOP_TIMER) {
                day.getCurrentTimeBlock().stop();
            }
        });

        DatePicker.setValue(LocalDate.now());

        TryLoadData();
    }

    /**
     * Attempts to load saved disk data, if it exists
     */
    public void TryLoadData() {
        try {
            Schedule loadedSchedule;
            loadedSchedule = deserialize("data.ser");

            if (loadedSchedule != null) {
                model.setLoadedSchedule(loadedSchedule);
            }
        } catch (Exception e) {
            // File didn't exist but its fine....just means its the first run
        }

        FillTaskMenu();
    }

    public void SaveData() {
        DataSave.serialize(model.getSchedule(), "data.ser");
        settingsController.SaveData();
    }

    public Label getTimerLabel()
    {
        return this.timerLabel;
    }

    private Timeline createTimeline()
    {
        double duration = 1;
        KeyFrame keyFrame = new KeyFrame(Duration.seconds(duration), event -> {
            this.notification.DisplayNotification(this.model);
            this.model.getIdleDetector().Update();
            this.model.getHotkeyHandler().Update();

            // When dealing with the "ongoing task", always grab from the current date,
            // instead of the date that the user has selected
            Day day = model.getSchedule().getCurrDay();
            ScheduledTimeBlock currentTimeBlock = day.getCurrentTimeBlock();

            // Check if the TimeBlock has changed
            if (currentTimeBlock != previousTimeBlock) {
                // Reset the timer if the TimeBlock has changed
                if(previousTimeBlock != null)
                {
                    previousTimeBlock.stop();
                }
                timerLabel.setText("00:00:00");
                time1.setText("00:00:00");
                StartButtonImage.setImage(new Image(getClass().getResource("5261101.png").toString()));// toString

                previousTimeBlock = currentTimeBlock;
            }


            if (day.getCurrentTimeBlock() != null) {

                timerLabel.setText(day.getCurrentTimeBlock().timetostr(day.getCurrentTimeBlock().total_time_work()));
                time1.setText(day.getCurrentTimeBlock().timetostr(day.getCurrentTimeBlock().total_time_work()));
                // TODO: Notification
                ShowOngoingTask.setText(day.getCurrentTimeBlock().gettagName());
                StartButton.setDisable(false);
                String hour = "";
                String minute = "";
                String second = "";

                System.out.println(day.getCurrentTimeBlock().isRunning());
                    if(!day.getCurrentTimeBlock().isRunning()) {
                       // StartButtonImage.setImage(new Image("file:src/main/resources/com/cmpt370/timetracker/5261101.png"));
                        StartButtonImage.setImage(new Image(getClass().getResource("5261101.png").toString()));

                    }else {

                        //StartButtonImage.setImage(new Image("file:src/main/resources/com/cmpt370/timetracker/end.png"));
                        StartButtonImage.setImage(new Image(getClass().getResource("end.png").toString()));


                    }
                if (day.getCurrentTimeBlock().isRunning()) {
                    day.getCurrentTimeBlock().getAppTracker().Update(duration);
                    System.out.println(day.getCurrentTimeBlock().getAppTracker());
                    if(day.getCurrentTimeBlock().hour <= 9)
                    {
                       hour = "0";
                    }
                    if(day.getCurrentTimeBlock().minute <= 9)
                    {
                        minute = "0";
                    }
                    if(day.getCurrentTimeBlock().second <= 9) {
                        second = "0";
                    }

                    timerLabel.setText(hour + day.getCurrentTimeBlock().hour + ":" + minute + day.getCurrentTimeBlock().minute + ":" + second + day.getCurrentTimeBlock().second);
                    time1.setText(timerLabel.getText());
                    timerLabel.setStyle("-fx-background-color: transparent;");
                    timerLabel.setFont(javafx.scene.text.Font.font("System", 24));

                }
            } else {
                timerLabel.setText("00:00:00");
                time1.setText("00:00:00");
                timerLabel.setBorder(null);
                timerLabel.setStyle("-fx-background-color: transparent;");
                timerLabel.setFont(javafx.scene.text.Font.font("System", 24));
                ShowOngoingTask.setText("Ongoing Task");


            }
        });
        Timeline timeline = new Timeline(keyFrame);
        timeline.setCycleCount(Animation.INDEFINITE);
        return timeline;
    }

    @FXML
    void CancelButtonOnAction(ActionEvent event) {
        Day day = model.getSchedule().getDay(DatePicker.getValue().getDayOfMonth(), DatePicker.getValue().getMonthValue(), DatePicker.getValue().getYear());
        ScheduledTimeBlock block = day.getCurrentTimeBlock();
        if(block != null)
        {
            if(block.isRunning()) {
                block.stop();
                StartButton.setDisable(false);
                CancelButton.setDisable(true);
            }
        }
    }

    @FXML
    void CreateNewTaskOnAction(ActionEvent event) {
        UpcomingTask newTask = new UpcomingTask(this.model, DatePicker.getValue());
        NewTaskVBox.getChildren().add(newTask);
    }

    @FXML
    void DatePickerOnAuction(ActionEvent event) {
        FillTaskMenu();
    }


    public void FillTaskMenu() {
        ArrayList<UpcomingTask> tasks = new ArrayList<>();
        NewTaskVBox.getChildren().clear();
        LocalDate date = DatePicker.getValue();
        Day day = model.getSchedule().getDay(date.getDayOfMonth(), date.getMonthValue(), date.getYear());
        for (int i = 0; i < day.getAllTimeBlocks().size(); i++) {
            tasks.add(new UpcomingTask(this.model, DatePicker.getValue(), day.getAllTimeBlocks().get(i)));
            NewTaskVBox.getChildren().add(tasks.get(i));
        }
    }

    @FXML
    void EditbuttonOnAction(ActionEvent event)
    {

    }

    @FXML
    void MenuButtonOnAction(ActionEvent event) {

    }

    @FXML
    void ShowOngoingTaskOnAction(ActionEvent event) {

    }

    boolean istart=true;
    @FXML
    void StartButtonOnAction(ActionEvent event)
    {
        // Start button should only activate the current day ongoing task
        if(istart) {
            StartButtonImage.setImage(new Image(getClass().getResource("end.png").toString()));
            istart = false;
           // start();
        }else {
            StartButtonImage.setImage(new Image(getClass().getResource("5261101.png").toString()));

            istart=true;
            stop();
        }
        Day day = model.getSchedule().getCurrDay();
        System.out.println(model.getSchedule().getAllDays().size());
        ScheduledTimeBlock block = day.getCurrentTimeBlock();
        if(block != null)
        {
            if (block.isRunning()) {
                block.stop();
              //  StartButtonImage.setImage(new Image("file:src/main/resources/com/cmpt370/timetracker/5261101.png"));
                StartButtonImage.setImage(new Image(getClass().getResource("5261101.png").toString()));


            } else {
                block.start();
                //StartButtonImage.setImage(new Image("file:src/main/resources/com/cmpt370/timetracker/end.png"));
                StartButtonImage.setImage(new Image(getClass().getResource("end.png").toString()));


            }
            StartButton.setDisable(true);
            CancelButton.setDisable(false);
        }

    }

    Timeline timeline1 = new Timeline(new KeyFrame(Duration.millis(17), t -> this.draw()));

    long l;
//    void start(){
//        l=System.currentTimeMillis();
//        timeline1.setCycleCount(Timeline.INDEFINITE);
//        timeline1.play();
//    }

    void draw(){
        time1.setText(sumSecondToTime(((int) (System.currentTimeMillis()-l)/1000)));
        //timerLabel.setText(sumSecondToTime(((int) (System.currentTimeMillis()-l)/1000)));
    }
    void stop(){
        timeline1.stop();
    }


    private static String sumSecondToTime(int sumSecond) {
        if(sumSecond <= 0){
            return "00:00:00";
        }
        int h = sumSecond/3600;
        int m = (sumSecond-h*3600)/60;
        int s = sumSecond - h*3600-m*60;
        String time = "%02d:%02d:%02d";
        time = String.format(time,h,m,s);
        return time;
    }

    @FXML
    void SettingsMenuOnAction(ActionEvent event) {
        settings.showAndWait();
    }


    @FXML
    void ShowTaskDistributionOnAction(ActionEvent event) throws IOException {
        Stage appChartInfo = new Stage();
        TaskDistributionController TBC = new TaskDistributionController();
        LocalDate date = DatePicker.getValue();
        Day day = model.getSchedule().getDay(date.getDayOfMonth(), date.getMonthValue(), date.getYear());
        TBC.starts(appChartInfo, day);
    }
}
