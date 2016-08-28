/*
   @author Neil Mancini
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ai.manager;

import java.util.ArrayList;
import java.util.List;
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
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Border;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

public class AIManager extends Application {
    
    //Static Variables
    static TextField load_text = new TextField();
    static TextField down_text = new TextField();
    static String filePathInput = new String();
    static List <Aircraft> ac_array = new ArrayList<>();
    static List <Aircraft> search_array = new ArrayList<>();
    static List <Rectangle> rec_array = new ArrayList<>();
    static Label ac_count_txt = new Label();
    static Pane results = new Pane();
    static ToggleButton btn_load = new ToggleButton("");
    static Button btn_reset = new Button("reset");
    static Button btn_search = new Button("SEARCH");
    static ScrollPane sp = new ScrollPane();
    static VBox vbox1 = new VBox();
    static TextField flight_text = new TextField();
    static TextField ac_text = new TextField();
    
    @Override
    public void start(Stage primaryStage) {
        
        //***********GUI Setup**************//
        //images
        Image img_load = new Image(getClass().getClassLoader().getResourceAsStream("Images/icon_load.png"));
        
        //VBOX
        
        vbox1.setMinSize(100, 105);
        vbox1.setStyle("-fx-background-color:#003366;");
        
        //HBOX
        HBox hbox1 = new HBox(15);
        hbox1.setMinSize(vbox1.getWidth(), 75);
        hbox1.setStyle("-fx-background-color:#003366");
        hbox1.setAlignment(Pos.CENTER_LEFT);
        hbox1.setPadding(new Insets(0,15,0,15));
        
        HBox hbox2 = new HBox();
        hbox2.setMinSize(vbox1.getWidth(), 30);
        hbox2.setStyle("-fx-background-color:#2b2b3b;");
        hbox2.setAlignment(Pos.CENTER_LEFT);
        
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

        
        //TXT Fields
        load_text.setMinHeight(25);
        load_text.setMinWidth(400);
        
        TextField airport_text = new TextField();
        airport_text.setText("Airport Code");
        
        flight_text.setText("Flight Number");

        ac_text.setText("Aircraft Number");
        
        down_text.setText("4");
        down_text.setMaxWidth(30);
        
        //Labels
        Label status_txt = new Label();
        status_txt.setText("STATUS:");
        status_txt.setStyle("-fx-text-fill:#7FFF00");
        status_txt.setPadding(new Insets(0,0,0,32));
        
        ac_count_txt.setText("Aircraft Loaded: 0");
        ac_count_txt.setStyle("-fx-text-fill:#F0FFFF");
        ac_count_txt.setPadding(new Insets(0,0,0,50));
        
        
        //Others
        Separator sep1 = new Separator();
        sep1.setOrientation(Orientation.VERTICAL);
        
        //add to children
        hbox1.getChildren().addAll(btn_load,load_text,sep1,ac_text,flight_text,airport_text,down_text,btn_search,btn_reset);
        hbox2.getChildren().addAll(status_txt,ac_count_txt);
        vbox1.getChildren().addAll(hbox1,hbox2);
        
        //Setting Scene
        
        sp.setContent(results);
        sp.setStyle("-fx-border-color: #003366;");
        
        //sp.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        /*Tooltip hello = new Tooltip();
        hello.setText("I love Julesy");
        sp.setTooltip(hello);*/
        
        
        main_pane.setTop(vbox1);
        main_pane.setCenter(sp);
        
        Scene scene = new Scene(main_pane,1625,800);
        
        primaryStage.setTitle("AI Manager by Neil Mancini");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    //**************************GUI Setup END********************************//

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
