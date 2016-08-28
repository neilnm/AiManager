/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ai.manager;

//Static Imports from Main
import static ai.manager.AIManager.ac_array;
import static ai.manager.AIManager.results;
import static ai.manager.AIManager.sp;
import static ai.manager.AIManager.flight_text;
import static ai.manager.AIManager.ac_text;
import static ai.manager.AIManager.airport_text;


//Java Imports
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.text.Font;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;

public class ButtonHandler implements EventHandler<ActionEvent>{
    
    //Margin and Spacing Variables
    final int top_margin = 18;
    final int left_margin = 130;
    final int ac_label_spacing = 20;
    final int ac_line_spacing = 30;
    final int hour_spacing = 30;
    
    @Override
    public void handle(ActionEvent action_event){
        //LOAD BUTTON
        if(action_event.getSource().equals(AIManager.btn_load)){
            ac_array.clear();
            results.getChildren().clear();
            sp.setVvalue(0);
            
            try{
                loadFile();
            }
            catch(java.io.FileNotFoundException e){
                System.out.println(e);
            }
            finally{
                printAllAcs();
            }   
        }
        
        //RESET BUTTON
        if(action_event.getSource().equals(AIManager.btn_reset)){
            //Clearing Arrays and Result Pane
            results.getChildren().clear();
            flight_text.setText("Flight Number");
            ac_text.setText("Aircraft Number");
            airport_text.setText("Airport Code");
            sp.setVvalue(0);
            
            printAllAcs();
            AIManager.search_count_txt.setText("Aircraft Found: "+ac_array.size());
        }
        
        //SEARCH BUTTON
        if(action_event.getSource().equals(AIManager.btn_search)){
            //Clearing Arrays and Result Pane
            results.getChildren().clear();
            sp.setVvalue(0);
            
            int num_results = 0;
            String flight_num;
            String ac_num;
            String airport_code;
            
            if (flight_text.getText().equals("Flight Number")){
                flight_num = "*";
            }
            else{
                flight_num = flight_text.getText();
            }
            
            if (ac_text.getText().equals("Aircraft Number")){
                ac_num = "*";
            }
            else{
                ac_num = ac_text.getText();
            }
            
            if (airport_text.getText().equals("Airport Code")){
                airport_code = "*";
            }
            else{
                airport_code = airport_text.getText();
            }
            
            
            for(int i=0; i<ac_array.size(); i++){
                ac_array.get(i).setHasLongGround(false);
            }
            
            //Drawing Vertical Lines and Hour labels
            for(int i=0; i<=48; i++){
                //Vertical Lines
                Line v_lines = new Line();
                v_lines.setStartX(30*i+130);
                v_lines.setStartY(0);
                v_lines.setEndX(30*i+130);
                v_lines.setEndY(ac_array.size()*30+18);
                v_lines.setStroke(Color.LIGHTSLATEGREY);

                //ADDING TO PANE
                results.getChildren().add(v_lines);
            }
            
            //Printing ACs matching search criteria to screen
            for(int j = 0; j < ac_array.size(); j++){
                
                //Marking ACs which has a long ground connection
                for(int i = 0; i < ac_array.get(j).flight_array.size(); i++){
                    if(i > 0){
                        double ground_time = ac_array.get(j).getFlight(i).getDepTimeRatio() - ac_array.get(j).getFlight(i-1).getArrTimeRatio();
                        
                        if((ground_time >= Integer.parseInt(AIManager.down_text.getText())*100 &&
                            airport_code.equals(ac_array.get(j).getFlight(i-1).getArrstation())) ||
                           (ground_time >= Integer.parseInt(AIManager.down_text.getText())*100 &&
                           (airport_code.equals("*") || airport_code.equalsIgnoreCase("")))){
                            ac_array.get(j).setHasLongGround(true);
                        }
                    }
                }
                
                //Checking if AC matches search criteria
                if ((ac_array.get(j).getHasLongGround() && ac_array.get(j).getFlightnum().contains(flight_num) && String.valueOf(ac_array.get(j).getAcnum()).contains(ac_num)) ||
                    (ac_array.get(j).getHasLongGround() && flight_num.equals("*") && String.valueOf(ac_array.get(j).getAcnum()).contains(ac_num)) ||
                    (ac_array.get(j).getHasLongGround() && ac_array.get(j).getFlightnum().contains(flight_num) && ac_num.equals("*")) ||
                    (ac_array.get(j).getHasLongGround() && flight_num.equals("*") && ac_num.equals("*"))){
                    
                    num_results++;
                    
                    //HLINES
                    Line h_lines = new Line();
                    h_lines.setStartX(0);
                    h_lines.setStartY(((num_results-1)*30)+18);
                    h_lines.setEndX(2000);
                    h_lines.setEndY((num_results-1)*30+18);
                    h_lines.setStroke(Color.LIGHTSLATEGREY);

                    //AIRCRAFT LABELS
                    Label ac_label = new Label((num_results-1)+1+" AC#"+String.valueOf(ac_array.get(j).getAcnum())+" "+ac_array.get(j).getFlightnum());
                    ac_label.setLayoutX(0);
                    ac_label.setLayoutY((num_results-1)*30+20);
                    ac_label.setTextFill(Color.BLACK);
                    
                    //ADDING TO RESULT PANE
                    results.getChildren().add(h_lines);
                    results.getChildren().add(ac_label);
                    
                    //Drawing Flights
                    for(int i = 0; i < ac_array.get(j).flight_array.size(); i++){
                        //VARIABLES
                        double dep_time_in_gui = ac_array.get(j).getFlight(i).getDepTimeInGui();
                        double arr_time_in_gui = ac_array.get(j).getFlight(i).getArrTimeInGui();
                        double duration_in_gui = arr_time_in_gui - dep_time_in_gui;
                        
                        if(i>0){
                            double ground_time = ac_array.get(j).getFlight(i).getDepTimeRatio() - ac_array.get(j).getFlight(i-1).getArrTimeRatio();
                            
                            if((ground_time >= Integer.parseInt(AIManager.down_text.getText())*100 &&
                                airport_code.equals(ac_array.get(j).getFlight(i-1).getArrstation())) ||
                               (ground_time >= Integer.parseInt(AIManager.down_text.getText())*100 &&
                               (airport_code.equals("*") || airport_code.equalsIgnoreCase("")))){
                                Line conn_line = new Line();
                                conn_line.setStartX(ac_array.get(j).getFlight(i-1).getArrTimeInGui()+31.5);
                                conn_line.setStartY((num_results-1)*30+34);
                                conn_line.setEndX(ac_array.get(j).getFlight(i).getDepTimeInGui()+30);
                                conn_line.setEndY((num_results-1)*30+34);
                                conn_line.setStroke(Color.RED);
                                conn_line.setStrokeWidth(2.5);

                                results.getChildren().add(conn_line);
                            }
                        }
                        
                        //Drawing Aircrafts
                        //REC(X START, Y START, WIDTH, HEIGHT)
                        Rectangle leg_rec = new Rectangle(dep_time_in_gui+30,(num_results-1)*30+26,duration_in_gui,15);
                        Tooltip stations = new Tooltip();
                        stations.setText(ac_array.get(j).getFlight(i).toString());
//                        if (ac_array.get(j).getFlight(i).getDeptime() >= 2400){
//                            stations.setText(ac_array.get(j).getFlight(i).getDepstation()+"-"+ac_array.get(j).getFlight(i).getArrstation()+"\n"+
//                            (ac_array.get(j).getFlight(i).getDeptime()-2400)+" - "+ac_array.get(j).getFlight(i).getArrtime());
//                        }
                        Font font = new Font("Verdana",20);
                        stations.setFont(font);
                        Tooltip.install(leg_rec, stations);
                        //Alternating Leg Colors
                        leg_rec.setFill(Color.BLUE);
                        if((j%2>0)){
                            leg_rec.setFill(Color.BLUEVIOLET);
                        }

                        //Adding to Rectangle Array and to results pane
                        //AIManager.rec_array.add(leg_rec);
                        results.getChildren().addAll(leg_rec);
                    }
                }
            }
           
            //Rectangle for Hours header
            Rectangle hours_rec = new Rectangle(0,0,2000,17);
            hours_rec.setFill(Color.web("#003366"));
            hours_rec.setStroke(Color.web("#003366"));
            results.getChildren().add(hours_rec);

            //Loops for hours header
            for(int i=0; i<=48; i++){
                //Hour Labels
                Label hours_label = new Label(String.valueOf(i));
                if(i > 24){
                    hours_label.setText(String.valueOf(i-24));
                }
                hours_label.setLayoutX((30*i)+127);
                hours_label.setLayoutY(1);
                hours_label.setTextFill(Color.WHITE);

                sp.vvalueProperty().addListener( (observable, oldValue, newValue) -> {
                    double yTranslate = ((newValue.doubleValue() * (ac_array.size()*30+18)) -
                                         (newValue.doubleValue() * (sp.getHeight()-18)));
                    hours_label.translateYProperty().setValue(yTranslate);
                    hours_rec.translateYProperty().setValue(yTranslate);
                });
                    //ADDING TO PANES
                    results.getChildren().add(hours_label);
            }
            
            //Reloading ac count text
            AIManager.search_count_txt.setText("Aircraft Found: "+num_results);
        }
    }
    
    
    //**************************************************************************************************\\
    //***********************************************METHODS********************************************\\
    //**************************************************************************************************\\
    
    
    //LoadFile and Create ac_array function
    public void loadFile() throws FileNotFoundException{
        //Variables
        Scanner reader;
        ArrayList<String> lineList = new ArrayList<>();
        ArrayList<String[]> commalist = new ArrayList<>();
        
        //Window Explorer screen
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose Flight Plan");
        File selectedfile = fileChooser.showOpenDialog(null);
        AIManager.load_text.setText(selectedfile.getAbsolutePath());
        AIManager.filePathInput = AIManager.load_text.getText();
        
        
        //Reading file
        reader = new Scanner(new File(AIManager.filePathInput));
        while(reader.hasNext()){
            lineList.add(reader.nextLine());
        }

        //Adding if line starts with AC#
        for(int i = 0; i < lineList.size(); i++){
            if(lineList.get(i).startsWith("AC#")){

                //Splitting by ","
                commalist.add(lineList.get(i).split(","));
            }
        }
        //checking if 24 hour aircraft and creating Aircrafts and Flights
        for(int i = 0; i < commalist.size(); i++){
            if(commalist.get(i)[3].equals("24")){

                //Creating new Aircraft
                Aircraft ac = new Aircraft(Integer.parseInt(commalist.get(i)[0].substring(3)),
                                           commalist.get(i)[1]);

                int num_of_flights = commalist.get(i).length / 6;
                //System.out.println(num_of_flights);

                //Adding Flights to Aircraft (Dep Station, Dep Time, Arr Station, Arr Time)
                try{
                    int position = 0;
                    double starttime = 0;
                    for(int j = 0; j < num_of_flights; j++){
                        if(j == 0){
                            ac.AddFlight(commalist.get(i)[commalist.get(i).length-1].substring(0,4), 
                            Double.parseDouble(commalist.get(i)[5].substring(0,5).replaceAll(":","")), 
                            commalist.get(i)[10], 
                            Double.parseDouble(commalist.get(i)[6].substring(0,5).replaceAll(":","")));
                            starttime = Double.parseDouble(commalist.get(i)[5].substring(0,5).replaceAll(":",""));
                        }
                        else{
                            double deptime = Double.parseDouble(commalist.get(i)[11+position].substring(0,5).replaceAll(":",""));
                            //for checking leg order
                            if(deptime < starttime){
                                deptime = deptime + 2400;
                            }
                            ac.AddFlight(commalist.get(i)[10+position], 
                            deptime, 
                            commalist.get(i)[16+position].substring(0,4), 
                            Double.parseDouble(commalist.get(i)[12+position].substring(0,5).replaceAll(":","")));
                            position = position+6;
                        }
                    }
                }
                catch(java.lang.NumberFormatException | java.lang.StringIndexOutOfBoundsException e){
                    System.out.println("test: "+i+" "+commalist.get(commalist.size()-1)[0].substring(3)+" "+commalist.get(commalist.size()-1)[1]);
                }
                ac_array.add(ac);
            }
        }
    }
    
    
    //Print/Load all ac_array to screen Method:
    public void printAllAcs(){
        //Setting AC COUNT TEXT
        AIManager.ac_count_txt.setText("Aircraft Loaded: "+ac_array.size());
        AIManager.search_count_txt.setText("Aircraft Found: "+ac_array.size());

        //Drawing H lines and Printing Aircraft Labels
        for (int i=0; i < ac_array.size(); i++) {
            //HLINES
            Line h_lines = new Line();
            h_lines.setStartX(0);
            h_lines.setStartY((i*ac_line_spacing)+top_margin);
            h_lines.setEndX(2000);
            h_lines.setEndY(i*ac_line_spacing+top_margin);
            h_lines.setStroke(Color.LIGHTSLATEGREY);
            //Caused performance issues:
            //r.getStrokeDashArray().addAll(2d);

            //AIRCRAFT LABELS
            Label ac_label = new Label(i+1+" AC#"+String.valueOf(ac_array.get(i).getAcnum())+" "+ac_array.get(i).getFlightnum());
            ac_label.setLayoutX(0);
            ac_label.setLayoutY(i*ac_line_spacing+ac_label_spacing);
            ac_label.setTextFill(Color.BLACK);

            //ADDING TO RESULT PANE
            results.getChildren().add(h_lines);
            results.getChildren().add(ac_label);
        }

        //Drawing Vertical Lines and Hour labels
        for(int i=0; i<=48; i++){
            //Vertical Lines
            Line v_lines = new Line();
            v_lines.setStartX(30*i+130);
            v_lines.setStartY(0);
            v_lines.setEndX(30*i+130);
            v_lines.setEndY(ac_array.size()*hour_spacing+top_margin);
            v_lines.setStroke(Color.LIGHTSLATEGREY);

            //ADDING TO PANE
            results.getChildren().add(v_lines);
        }

        //Drawing Flights + CONNECTION LINE + STATION LABELS
        for(int j = 0; j < ac_array.size(); j++){

            for(int i = 0; i < ac_array.get(j).flight_array.size(); i++){
                //VARIABLES
                double dep_time_in_gui = ac_array.get(j).getFlight(i).getDepTimeInGui();
                double arr_time_in_gui = ac_array.get(j).getFlight(i).getArrTimeInGui();
                double duration_in_gui = arr_time_in_gui - dep_time_in_gui;

                //Drawing Aircrafts
                //REC(X START, Y START, WIDTH, HEIGHT)
                Rectangle leg_rec = new Rectangle(dep_time_in_gui+30,j*30+26,duration_in_gui,15);
                Tooltip stations = new Tooltip();
                stations.setText(ac_array.get(j).getFlight(i).toString());
                Font font = new Font("Verdana",20);
                stations.setFont(font);
                Tooltip.install(leg_rec, stations);
                //Alternating Leg Colors
                leg_rec.setFill(Color.BLUE);
                if((j%2>0)){
                    leg_rec.setFill(Color.BLUEVIOLET);
                }
                //Border Radius does not seem to work
                //r.setStyle("-fx-border-radius: 10 10 10 10");

                //Station Labels
                Label depl = new Label();
                Label arrl = new Label();
                depl.setText(ac_array.get(j).getFlight(i).getDepstation());
                arrl.setText(ac_array.get(j).getFlight(i).getArrstation());
                depl.setLayoutX(dep_time_in_gui);
                arrl.setLayoutX(arr_time_in_gui+35);
                depl.setLayoutY(j*ac_line_spacing+26);
                arrl.setLayoutY(j*ac_line_spacing+26);


                //Adding to Rectangle Array and to results pane
                //AIManager.rec_array.add(leg_rec);
                results.getChildren().addAll(leg_rec);
            }
        }

        //Rectangle for Hours header
        Rectangle hours_rec = new Rectangle(0,0,2000,17);
        hours_rec.setFill(Color.web("#003366"));
        hours_rec.setStroke(Color.web("#003366"));
        results.getChildren().add(hours_rec);

        //Loops for hours header
        for(int i=0; i<=48; i++){

            //Hour Labels
            Label hours_label = new Label(String.valueOf(i));
            if(i > 24){
                hours_label.setText(String.valueOf(i-24));
            }
            hours_label.setLayoutX((30*i)+127);
            hours_label.setLayoutY(1);
            hours_label.setTextFill(Color.WHITE);

            sp.vvalueProperty().addListener( (observable, oldValue, newValue) -> {
                double yTranslate = ((newValue.doubleValue() * (ac_array.size()*hour_spacing+top_margin)) -
                                     (newValue.doubleValue() * (sp.getHeight()-18)));
                hours_label.translateYProperty().setValue(yTranslate);
                hours_rec.translateYProperty().setValue(yTranslate);
            });

            //ADDING TO PANES
            results.getChildren().add(hours_label);
        }
    }
    
    //Convert a time a Gui Pixel (not currently used, moved to Flight Class)
    public double timeTopixel(double time){
        //Divided by 100 because 134 time is 1.34 hours * 30 (30 is spacing per hour) + 130 (130 is 100 for margin spacing and 30 for 1 hour spacing because starting at 00:00)
        //Returning a minimum of 130 
        return time/100*30+100;
    }
}
