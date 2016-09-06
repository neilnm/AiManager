/*
   @author Neil Mancini
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ai.manager;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import javafx.application.Application;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class AIManager extends Application {
    
    //Static Variables
    static TextField loadTxt = new TextField();
    static TextField downTxt = new TextField();
    static TextField flightTxt = new TextField();
    static TextField acTxt = new TextField();
    static TextField airportTxt = new TextField();
    
    static String filePathInput = new String();

    
    static List <Aircraft> acArray = new ArrayList<>();
    static List <Aircraft> searchArray = new ArrayList<>();
    static ArrayList<String[]> localTimeCommaList = new ArrayList<>();
    
    static Label acCountTxt = new Label();
    static Label searchCountTxt = new Label();
    static Label statusTxt = new Label();
    static Label missingAirportTxt = new Label();
    
    static ToggleButton btnLoad = new ToggleButton("");
    static Button btnReset = new Button("reset");
    static Button btnSearch = new Button("SEARCH");
    
    static Pane results = new Pane();
    static ScrollPane sp = new ScrollPane();
    static VBox vbox1 = new VBox();
    static HBox hbox2 = new HBox();
    
    @Override
    public void start(Stage primaryStage) throws FileNotFoundException {
        //***********GUI Setup**************//
        //images
        Image imgLoad = new Image(getClass().getClassLoader().getResourceAsStream("Images/iconLoad.png"));
        Image air = new Image(getClass().getClassLoader().getResourceAsStream("Images/air.png"));
        
        //VBOX
        
        vbox1.setMinSize(100, 105);
        vbox1.setStyle("-fx-background-color:#003366;");
        
        //HBOX
        HBox hbox1 = new HBox(15);
        hbox1.setMinSize(vbox1.getWidth(), 75);
        hbox1.setStyle("-fx-background-color:#003366");
        hbox1.setAlignment(Pos.CENTER_LEFT);
        hbox1.setPadding(new Insets(0,15,0,15));
        
        
        hbox2.setMinSize(vbox1.getWidth(), 30);
        hbox2.setStyle("-fx-background-color:#2b2b3b;");
        hbox2.setAlignment(Pos.CENTER_LEFT);
        
        VBox vboxAc = new VBox();
        vboxAc.setAlignment(Pos.CENTER);
        vboxAc.setPadding(new Insets(0,0,16.5,0));
        VBox vboxFlight = new VBox();
        vboxFlight.setPadding(new Insets(0,0,16.5,0));
        vboxFlight.setAlignment(Pos.CENTER);
        VBox vboxStation = new VBox();
        vboxStation.setPadding(new Insets(0,0,16.5,0));
        vboxStation.setAlignment(Pos.CENTER);
        VBox vboxHours = new VBox();
        vboxHours.setPadding(new Insets(0,0,16.5,0));
        vboxHours.setAlignment(Pos.CENTER);
        
        //Panes
        BorderPane mainPane = new BorderPane();
        
        results.setMinSize(1800,1000);
        results.setStyle("-fx-background-color:#b0cce8;");
        
        //Buttons
        btnLoad.setGraphic(new ImageView(imgLoad));
        btnLoad.setStyle("-fx-background-color: transparent");
        btnLoad.setMinSize(52,52);
        btnLoad.setOnAction(new ButtonHandler());
        
        btnReset.setOnAction(new ButtonHandler());
        
        btnSearch.setOnAction(new ButtonHandler());
        
        Button btnTikki = new Button();
        btnTikki.setStyle("-fx-background-color: transparent");
        btnTikki.setGraphic(new ImageView(air));
        
        //TXT Fields
        loadTxt.setMinHeight(25);
        loadTxt.setMinWidth(400);
        
        airportTxt.setText("Airport Code");
        airportTxt.setAlignment(Pos.CENTER);
        airportTxt.setOnMouseReleased(new ClickAction());
        airportTxt.setOnKeyPressed(new KeyAction());
        
        flightTxt.setText("Flight Number");
        flightTxt.setAlignment(Pos.CENTER);
        flightTxt.setOnMouseReleased(new ClickAction());
        flightTxt.setOnKeyPressed(new KeyAction());
        
        acTxt.setText("Aircraft Number");
        acTxt.setAlignment(Pos.CENTER);
        acTxt.setOnMouseReleased(new ClickAction());
        acTxt.setOnKeyPressed(new KeyAction());
        
        downTxt.setText("4");
        downTxt.setAlignment(Pos.CENTER);
        downTxt.setMaxWidth(30);
        downTxt.setOnMouseReleased(new ClickAction());
        downTxt.setOnKeyPressed(new KeyAction());
        
        acCountTxt.setText("Aircraft Loaded: 0");
        acCountTxt.setStyle("-fx-text-fill:#F0FFFF");
        acCountTxt.setPadding(new Insets(0,0,0,50));
        
        searchCountTxt.setText("Aircraft Found: 0");
        searchCountTxt.setStyle("-fx-text-fill:#F0FFFF");
        searchCountTxt.setPadding(new Insets(0,0,0,50));
        
        missingAirportTxt.setText("Missing Airport");
        missingAirportTxt.setStyle("-fx-text-fill:#666699");
        missingAirportTxt.setPadding(new Insets(0,0,0,50));
        
        //Labels
        
        statusTxt.setText("STATUS:");
        statusTxt.setStyle("-fx-text-fill:#7FFF00");
        statusTxt.setPadding(new Insets(0,0,0,32));
        
        Label acLabel = new Label("Aircraft Number");
        Label flightLabel = new Label("Flight Number");
        Label stationLabel = new Label("Airport Code");
        Label hoursLabel = new Label("Down Hours");
        acLabel.setTextFill(Color.WHITE);
        flightLabel.setTextFill(Color.WHITE);
        stationLabel.setTextFill(Color.WHITE);
        hoursLabel.setTextFill(Color.WHITE);
        
        //Others
        Separator sep1 = new Separator();
        sep1.setOrientation(Orientation.VERTICAL);
        Separator sep2 = new Separator();
        sep2.setOrientation(Orientation.VERTICAL);
        
        //add to children
        vboxAc.getChildren().addAll(acLabel,acTxt);
        vboxFlight.getChildren().addAll(flightLabel,flightTxt);
        vboxStation.getChildren().addAll(stationLabel,airportTxt);
        vboxHours.getChildren().addAll(hoursLabel,downTxt);
        hbox1.getChildren().addAll(btnLoad,loadTxt,sep1,vboxAc,vboxFlight,vboxStation,vboxHours,sep2,btnSearch,btnReset,btnTikki);
        hbox2.getChildren().addAll(statusTxt,acCountTxt,searchCountTxt,missingAirportTxt);
        vbox1.getChildren().addAll(hbox1,hbox2);
        
        //Setting Scene
        
        sp.setContent(results);
        sp.setStyle("-fx-border-color: #003366;");
        
        mainPane.setTop(vbox1);
        mainPane.setCenter(sp);
        
        Scene scene = new Scene(mainPane,1450,800);
        
        primaryStage.setTitle("AI Manager by Neil Mancini");
        primaryStage.setScene(scene);
        primaryStage.show();
        
        //Getting Airport Information
        String username = System.getProperty("user.name");
        Scanner localTimeReader = new Scanner(new File("C:\\Users\\"+username+"\\Documents\\AirportData\\airportdata.csv"));
        ArrayList<String> localTimeList = new ArrayList<>();
        while(localTimeReader.hasNext()){
            localTimeList.add(localTimeReader.nextLine());
        }
        //Converting Aiport Information to a Comma List
        for(int i = 0; i < localTimeList.size(); i++){
            localTimeCommaList.add(localTimeList.get(i).split(","));
        }
        
    }
    //**************************GUI Setup END********************************//

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}

        /*Tooltip hello = new Tooltip();
        hello.setText("I love Julesy");
        sp.setTooltip(hello);*/