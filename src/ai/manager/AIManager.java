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
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

public class AIManager extends Application {
    
    //Static Variables
    static TextField load_text = new TextField();
    static String filePathInput = new String();
    static List <Aircraft> ac_array = new ArrayList<>();
    static List <Rectangle> rec_array = new ArrayList<>();
    static Label ac_count_txt = new Label();
    static Pane results = new Pane();
    static ToggleButton btn_load = new ToggleButton("");
    static Button btn_reset = new Button("reset");
    static Pane hbox3 = new Pane();
    @Override
    public void start(Stage primaryStage) {
        
        //***********GUI Setup**************//
        //images
        Image img_load = new Image(getClass().getClassLoader().getResourceAsStream("Images/icon_load.png"));
        
        
        //VBOX
        VBox vbox1 = new VBox();
        vbox1.setMinSize(100, 105);
        vbox1.setStyle("-fx-background-color:#003366");
        
        //HBOX
        HBox hbox1 = new HBox(15);
        hbox1.setMinSize(vbox1.getWidth(), 75);
        hbox1.setStyle("-fx-background-color:#003366");
        hbox1.setAlignment(Pos.CENTER_LEFT);
        hbox1.setPadding(new Insets(0,15,0,15));
        
        HBox hbox2 = new HBox();
        hbox2.setMinSize(vbox1.getWidth(), 30);
        hbox2.setStyle("-fx-background-color:#2b2b3b");
        hbox2.setAlignment(Pos.CENTER_LEFT);
        
        
        hbox3.setMinSize(vbox1.getWidth(), 20);
        hbox3.setStyle("-fx-background-color:#003366");
        
        
        //Panes
        BorderPane main_pane = new BorderPane();
        
        
        results.setMinSize(1800,1000);
        results.setStyle("-fx-background-color:#b0cce8");
        
        //Buttons
        
        btn_load.setGraphic(new ImageView(img_load));
        btn_load.setStyle("-fx-background-color: transparent");
        btn_load.setMinSize(52,52);
        btn_load.setOnAction(new ButtonHandler());
        
        btn_reset.setOnAction(new ButtonHandler());
        
        Button btn_search = new Button("SEARCH");

        
        
        
        //TXT Fields
        
        //load_text.setPadding(new Insets(0,0,0,150));
        load_text.setMinHeight(25);
        load_text.setMinWidth(400);
        TextField airport_text = new TextField();
        airport_text.setText("Airport Code");
        TextField airline_text = new TextField();
        airline_text.setText("Airline Code");
        TextField ac_text = new TextField();
        ac_text.setText("Aircraft Number");
        TextField down_text = new TextField();
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
        hbox1.getChildren().addAll(btn_load,load_text,sep1,airport_text,airline_text,ac_text,down_text,btn_search,btn_reset);
        hbox2.getChildren().addAll(status_txt,ac_count_txt);
        vbox1.getChildren().addAll(hbox1,hbox2,hbox3);
        
        //**************************GUI Setup END********************************//
        
        
        
        
        //**************************LOGIC********************************//


        
        for(int j = 0; j < 1300; j++){
            /*
            Rectangle r = new Rectangle(j*10+130,j*30+26,45,15);
            r.setFill(Color.BLUE);
            if((j%2>0)){
                r.setFill(Color.BLUEVIOLET);
            }
            results.getChildren().add(r);*/
        }
        
        
        //**************************LOGIC END********************************//
        
        
        //Setting Scene
        ScrollPane sp = new ScrollPane();
        sp.setContent(results);
        sp.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        Tooltip hello = new Tooltip();
        hello.setText("I love Julesy");
        sp.setTooltip(hello);
        
        main_pane.setTop(vbox1);
        main_pane.setCenter(sp);
        
        Scene scene = new Scene(main_pane,1625,800);
        
        primaryStage.setTitle("AI Manager by Neil Mancini");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
