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
    static TextField load_text = new TextField();
    static TextField down_text = new TextField();
    static TextField flight_text = new TextField();
    static TextField ac_text = new TextField();
    static TextField airport_text = new TextField();
    
    static String filePathInput = new String();

    
    static List <Aircraft> ac_array = new ArrayList<>();
    static List <Aircraft> search_array = new ArrayList<>();
    static ArrayList<String[]> localTimeCommaList = new ArrayList<>();
    
    static Label ac_count_txt = new Label();
    static Label search_count_txt = new Label();
    static Label status_txt = new Label();
    static Label missingAirportTxt = new Label();
    
    static ToggleButton btn_load = new ToggleButton("");
    static Button btn_reset = new Button("reset");
    static Button btn_search = new Button("SEARCH");
    
    static Pane results = new Pane();
    static ScrollPane sp = new ScrollPane();
    static VBox vbox1 = new VBox();
    static HBox hbox2 = new HBox();
    
    @Override
    public void start(Stage primaryStage) throws FileNotFoundException {
        //***********GUI Setup**************//
        //images
        Image img_load = new Image(getClass().getClassLoader().getResourceAsStream("Images/icon_load.png"));
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
        
        VBox vbox_ac = new VBox();
        vbox_ac.setAlignment(Pos.CENTER);
        vbox_ac.setPadding(new Insets(0,0,16.5,0));
        VBox vbox_flight = new VBox();
        vbox_flight.setPadding(new Insets(0,0,16.5,0));
        vbox_flight.setAlignment(Pos.CENTER);
        VBox vbox_station = new VBox();
        vbox_station.setPadding(new Insets(0,0,16.5,0));
        vbox_station.setAlignment(Pos.CENTER);
        VBox vbox_hours = new VBox();
        vbox_hours.setPadding(new Insets(0,0,16.5,0));
        vbox_hours.setAlignment(Pos.CENTER);
        
        //Panes
        BorderPane main_pane = new BorderPane();
        
        results.setMinSize(1800,1000);
        results.setStyle("-fx-background-color:#b0cce8;");
        
        //Buttons
        btn_load.setGraphic(new ImageView(img_load));
        btn_load.setStyle("-fx-background-color: transparent");
        btn_load.setMinSize(52,52);
        btn_load.setOnAction(new ButtonHandler());
        
        btn_reset.setOnAction(new ButtonHandler());
        
        btn_search.setOnAction(new ButtonHandler());
        
        Button btn_air = new Button();
        btn_air.setStyle("-fx-background-color: transparent");
        btn_air.setGraphic(new ImageView(air));
        
        //TXT Fields
        load_text.setMinHeight(25);
        load_text.setMinWidth(400);
        
        airport_text.setText("Airport Code");
        airport_text.setAlignment(Pos.CENTER);
        airport_text.setOnMouseReleased(new ClickAction());
        airport_text.setOnKeyPressed(new KeyAction());
        
        flight_text.setText("Flight Number");
        flight_text.setAlignment(Pos.CENTER);
        flight_text.setOnMouseReleased(new ClickAction());
        flight_text.setOnKeyPressed(new KeyAction());
        
        ac_text.setText("Aircraft Number");
        ac_text.setAlignment(Pos.CENTER);
        ac_text.setOnMouseReleased(new ClickAction());
        ac_text.setOnKeyPressed(new KeyAction());
        
        down_text.setText("4");
        down_text.setAlignment(Pos.CENTER);
        down_text.setMaxWidth(30);
        
        ac_count_txt.setText("Aircraft Loaded: 0");
        ac_count_txt.setStyle("-fx-text-fill:#F0FFFF");
        ac_count_txt.setPadding(new Insets(0,0,0,50));
        
        search_count_txt.setText("Aircraft Found: 0");
        search_count_txt.setStyle("-fx-text-fill:#F0FFFF");
        search_count_txt.setPadding(new Insets(0,0,0,50));
        
        missingAirportTxt.setText("Missing Airport");
        missingAirportTxt.setStyle("-fx-text-fill:#666699");
        missingAirportTxt.setPadding(new Insets(0,0,0,50));
        
        //Labels
        
        status_txt.setText("STATUS:");
        status_txt.setStyle("-fx-text-fill:#7FFF00");
        status_txt.setPadding(new Insets(0,0,0,32));
        
        Label ac_label = new Label("Aircraft Number");
        Label flight_label = new Label("Flight Number");
        Label station_label = new Label("Airport Code");
        Label hours_label = new Label("Down Hours");
        ac_label.setTextFill(Color.WHITE);
        flight_label.setTextFill(Color.WHITE);
        station_label.setTextFill(Color.WHITE);
        hours_label.setTextFill(Color.WHITE);
        
        //Others
        Separator sep1 = new Separator();
        sep1.setOrientation(Orientation.VERTICAL);
        Separator sep2 = new Separator();
        sep2.setOrientation(Orientation.VERTICAL);
        
        //add to children
        vbox_ac.getChildren().addAll(ac_label,ac_text);
        vbox_flight.getChildren().addAll(flight_label,flight_text);
        vbox_station.getChildren().addAll(station_label,airport_text);
        vbox_hours.getChildren().addAll(hours_label,down_text);
        hbox1.getChildren().addAll(btn_load,load_text,sep1,vbox_ac,vbox_flight,vbox_station,vbox_hours,sep2,btn_search,btn_reset,btn_air);
        hbox2.getChildren().addAll(status_txt,ac_count_txt,search_count_txt,missingAirportTxt);
        vbox1.getChildren().addAll(hbox1,hbox2);
        
        //Setting Scene
        
        sp.setContent(results);
        sp.setStyle("-fx-border-color: #003366;");
        
        main_pane.setTop(vbox1);
        main_pane.setCenter(sp);
        
        Scene scene = new Scene(main_pane,1450,800);
        
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