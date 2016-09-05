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
import static ai.manager.AIManager.localTimeCommaList;
import static ai.manager.AIManager.missingAirportTxt;
import static ai.manager.AIManager.status_txt;

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
    final int topMargin = 36;
    final int leftMargin = 130;
    final int acLabelSpacing = 38;
    final int acSpacing = 30;
    final int hourSpacing = 30;
    final int middleRow = 52;
    
    @Override
    public void handle(ActionEvent action_event){
        //LOAD BUTTON
        if(action_event.getSource().equals(AIManager.btn_load)){
            //clearing
            ac_array.clear();
            results.getChildren().clear();
            sp.setVvalue(0);
            
            //Loading
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
            status_txt.setStyle("-fx-text-fill:#7FFF00");
            missingAirportTxt.setStyle("-fx-text-fill:#666699");
            
            printAllAcs();
            AIManager.search_count_txt.setText("Aircraft Found: "+ac_array.size());

        }
        
        //SEARCH BUTTON
        if(action_event.getSource().equals(AIManager.btn_search)){
            //Clearing Arrays and Result Pane
            results.getChildren().clear();
            sp.setVvalue(0);
            status_txt.setStyle("-fx-text-fill:#7FFF00");
            missingAirportTxt.setStyle("-fx-text-fill:#666699");
            
            //Variables
            int num_results = 0;
            String flight_num;
            String ac_num;
            String airport_code;
            
            //Setting search variables
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
                airport_code = airport_text.getText().toUpperCase();
            }
            
            for(int i=0; i<ac_array.size(); i++){
                ac_array.get(i).setHasLongGround(false);
            }
            
            
            //Drawing Vertical Lines and Hour labels
            for(int i=0; i<=48; i++){
                //Vertical Lines
                Line v_lines = new Line();
                v_lines.setStartX(hourSpacing*i+leftMargin);
                v_lines.setStartY(0);
                v_lines.setEndX(hourSpacing*i+leftMargin);
                v_lines.setEndY(ac_array.size()*hourSpacing+topMargin);
                v_lines.setStroke(Color.LIGHTSLATEGREY);

                //ADDING TO PANE
                results.getChildren().add(v_lines);
            }
            
            //Printing ACs matching search criteria to screen
            for(int j = 0; j < ac_array.size(); j++){
                
                //Marking ACs which has a long ground connection
                for(int i = 0; i < ac_array.get(j).flight_array.size(); i++){
                    double ground_time;
                    if(i == 0){
                        ground_time = ac_array.get(j).getFlight(i).getDepTimeRatio() - ac_array.get(j).getFlight(ac_array.get(j).flight_array.size()-1).getArrTimeRatio();
                        if(ground_time<0){
                            ground_time = ground_time+2400;
                        }
                    }
                    else{
                        ground_time = ac_array.get(j).getFlight(i).getDepTimeRatio() - ac_array.get(j).getFlight(i-1).getArrTimeRatio();
                    }
                    if((ground_time >= Integer.parseInt(AIManager.down_text.getText())*100 &&
                        airport_code.equals(ac_array.get(j).getFlight(i).getDepstation())) ||
                       (ground_time >= Integer.parseInt(AIManager.down_text.getText())*100 &&
                       (airport_code.equals("*") || airport_code.equalsIgnoreCase("")))){
                        ac_array.get(j).setHasLongGround(true);
                    }
                }
                
                //Checking if AC matches search criteria
                if ((ac_array.get(j).getHasLongGround() && ac_array.get(j).getFlightnum().contains(flight_num) && String.valueOf(ac_array.get(j).getAcnum()).contains(ac_num)) ||
                    (ac_array.get(j).getHasLongGround() && flight_num.equals("*") && String.valueOf(ac_array.get(j).getAcnum()).contains(ac_num)) ||
                    (ac_array.get(j).getHasLongGround() && ac_array.get(j).getFlightnum().contains(flight_num) && ac_num.equals("*")) ||
                    (ac_array.get(j).getHasLongGround() && flight_num.equals("*") && ac_num.equals("*"))){
                    
                    num_results++;
                    
                    //HLINES (TO DO: CREATE METHOD)
                    Line h_lines = new Line();
                    h_lines.setStartX(0);
                    h_lines.setStartY(((num_results-1)*hourSpacing)+topMargin);
                    h_lines.setEndX(2000);
                    h_lines.setEndY((num_results-1)*hourSpacing+topMargin);
                    h_lines.setStroke(Color.LIGHTSLATEGREY);

                    //AIRCRAFT LABELS
                    Label ac_label = new Label((num_results-1)+1+" AC#"+String.valueOf(ac_array.get(j).getAcnum())+" "+ac_array.get(j).getFlightnum());
                    ac_label.setLayoutX(0);
                    ac_label.setLayoutY((num_results-1)*hourSpacing+acLabelSpacing);
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
                        double ground_time;
                        
                        if(i==0){
                             ground_time = ac_array.get(j).getFlight(i).getDepTimeRatio() - ac_array.get(j).getFlight(ac_array.get(j).flight_array.size()-1).getArrTimeRatio();
                             if(ground_time<0){
                                 ground_time = ground_time + 2400;
                             }
                        }
                        else{
                            ground_time = ac_array.get(j).getFlight(i).getDepTimeRatio() - ac_array.get(j).getFlight(i-1).getArrTimeRatio();
                        }    
                            if((ground_time >= Integer.parseInt(AIManager.down_text.getText())*100 &&
                                airport_code.equals(ac_array.get(j).getFlight(i).getDepstation())) ||
                               (ground_time >= Integer.parseInt(AIManager.down_text.getText())*100 &&
                               (airport_code.equals("*") || airport_code.equalsIgnoreCase("")))){
                                
                                Line conn_line = new Line();
                                
                                if(i==0){
                                    if(j==0){
                                        System.out.println(ac_array.get(j).getFlightnum()+" "+ac_array.get(j).getFlight(i).getDepTimeInGui());
                                    }
                                    double endX = ac_array.get(j).getFlight(i).getDepTimeInGui() + 750;
                                    
                                    conn_line.setStartX(ac_array.get(j).getFlight(ac_array.get(j).flight_array.size()-1).getArrTimeInGui()+31.5);
                                    conn_line.setStartY((num_results-1)*acSpacing+middleRow);
                                    conn_line.setEndX(endX);
                                    conn_line.setEndY((num_results-1)*acSpacing+middleRow);
                                    conn_line.setStroke(Color.RED);
                                    conn_line.setStrokeWidth(2.5);
                                    
                                    results.getChildren().add(conn_line);
                                }
                                else{
                                    //Drawing Connection Line
                                    conn_line.setStartX(ac_array.get(j).getFlight(i-1).getArrTimeInGui()+31.5);
                                    conn_line.setStartY((num_results-1)*acSpacing+52);
                                    conn_line.setEndX(ac_array.get(j).getFlight(i).getDepTimeInGui()+30);
                                    conn_line.setEndY((num_results-1)*acSpacing+52);
                                    conn_line.setStroke(Color.RED);
                                    conn_line.setStrokeWidth(2.5);

                                    results.getChildren().add(conn_line);
                                }
                                
                            }
                        
                        //Drawing Aircrafts
                        //REC(X START, Y START, WIDTH, HEIGHT)
                        Rectangle leg_rec = new Rectangle(dep_time_in_gui+30,(num_results-1)*30+44,duration_in_gui,15);
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
            printHoursHeader();
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
            h_lines.setStartY((i*acSpacing)+topMargin);
            h_lines.setEndX(2000);
            h_lines.setEndY(i*acSpacing+topMargin);
            h_lines.setStroke(Color.LIGHTSLATEGREY);
            //Caused performance issues:
            //r.getStrokeDashArray().addAll(2d);

            //AIRCRAFT LABELS
            Label ac_label = new Label(i+1+" AC#"+String.valueOf(ac_array.get(i).getAcnum())+" "+ac_array.get(i).getFlightnum());
            ac_label.setLayoutX(0);
            ac_label.setLayoutY(i*acSpacing+acLabelSpacing);
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
            v_lines.setEndY(ac_array.size()*hourSpacing+topMargin);
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
                Rectangle leg_rec = new Rectangle(dep_time_in_gui+30,j*30+44,duration_in_gui,15);
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
                //Adding to Rectangle Array and to results pane
                //AIManager.rec_array.add(leg_rec);
                results.getChildren().addAll(leg_rec);
            }
        }
        printHoursHeader();
    }
    
    
    public void printHoursHeader(){
        //Variables
        String airport = airport_text.getText();
        String hours;
        int hoursInt;
        int timeDifference;
        boolean missingAirport = false;
        
        //Rectangle for Hours header
        Rectangle hours_rec = new Rectangle(0,0,2000,17);
        hours_rec.setFill(Color.web("#003366"));
        hours_rec.setStroke(Color.web("#003366"));
        
        Rectangle local_hours_rec = new Rectangle(0,18,2000,17);
        local_hours_rec.setFill(Color.web("#000000"));
        local_hours_rec.setStroke(Color.web("#000000"));
        results.getChildren().addAll(hours_rec,local_hours_rec);
        
        //Loops for hours header
        for(int i=0; i<=48; i++){
            hoursInt = i;
            //Hour Labels
            Label hours_label = new Label(String.valueOf(i));
            if(i > 24){
                hours_label.setText(String.valueOf(i-24));
            }
            hours_label.setLayoutX((30*i)+127);
            hours_label.setLayoutY(1);
            hours_label.setTextFill(Color.WHITE);
            
            
            //Local Time Hour Labels
            if(!airport.equalsIgnoreCase("") && !airport.equalsIgnoreCase("*") && !airport.equalsIgnoreCase("Airport Code")){
                missingAirport = true;
                for(int k=0; k < localTimeCommaList.size()-1; k++){
                    if(airport.equalsIgnoreCase(localTimeCommaList.get(k)[0])){
                        timeDifference = Integer.parseInt(localTimeCommaList.get(k)[1]);
                        hoursInt = i+timeDifference;
                        missingAirport = false;
                    }
                }
            }
            
            Label local_hours_label = new Label();
            local_hours_label.setLayoutX((30*i)+127);
            local_hours_label.setLayoutY(19);
            local_hours_label.setTextFill(Color.LIGHTSKYBLUE);
            if(missingAirport){
                local_hours_label.setTextFill(Color.RED);
            }
            hours = String.valueOf(hoursInt);
            if(hoursInt<0){
                hours = String.valueOf((hoursInt)+24);
            }
            if(hoursInt > 24){
                hours = String.valueOf((hoursInt)-24);
            }

            local_hours_label.setText(hours);
            

            

            sp.vvalueProperty().addListener((observable, oldValue, newValue) -> {
                double yTranslate = ((newValue.doubleValue() * (ac_array.size()*hourSpacing+topMargin)) -
                                     (newValue.doubleValue() * (sp.getHeight()-18)));
                hours_label.translateYProperty().setValue(yTranslate);
                hours_rec.translateYProperty().setValue(yTranslate);
                local_hours_rec.translateYProperty().setValue(yTranslate);
                local_hours_label.translateYProperty().setValue(yTranslate);
            });

            //ADDING TO PANES
            results.getChildren().addAll(hours_label,local_hours_label);
        }
        if(missingAirport){
            status_txt.setStyle("-fx-text-fill:#ff0000");
            missingAirportTxt.setStyle("-fx-text-fill:#ff0000");
        }
    }
}
