/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ai.manager;

//Static Imports from Main
import static ai.manager.AIManager.results;
import static ai.manager.AIManager.sp;
import static ai.manager.AIManager.localTimeCommaList;
import static ai.manager.AIManager.missingAirportTxt;

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
import static ai.manager.AIManager.flightTxt;
import static ai.manager.AIManager.acTxt;
import static ai.manager.AIManager.airportTxt;
import static ai.manager.AIManager.acArray;
import static ai.manager.AIManager.statusTxt;

public class ButtonHandler implements EventHandler<ActionEvent>{
    
    //Margin and Spacing Variables
    static int TOP_MARGIN = 36;
    static int LEFT_MARGIN = 130;
    static int AC_SPACING = 30;
    static int HOUR_SPACING = 30;
    static int MIDDLE_ROW = 52;
    
    @Override
    public void handle(ActionEvent action_event){
        //LOAD BUTTON
        if(action_event.getSource().equals(AIManager.btnLoad)){
            //clearing
            acArray.clear();
            results.getChildren().clear();
            sp.setVvalue(0);
            AIManager.loadTxt.setText("");
            
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
        if(action_event.getSource().equals(AIManager.btnReset)){
            //Clearing Arrays and Result Pane
            results.getChildren().clear();
            flightTxt.setText("Flight Number");
            acTxt.setText("Aircraft Number");
            airportTxt.setText("Airport Code");
            sp.setVvalue(0);
            statusTxt.setStyle("-fx-text-fill:#7FFF00");
            missingAirportTxt.setStyle("-fx-text-fill:#666699");
            
            printAllAcs();
            AIManager.searchCountTxt.setText("Aircraft Found: "+acArray.size());
        }
        
        //SEARCH BUTTON
        if(action_event.getSource().equals(AIManager.btnSearch)){
            Search();
        }
    }
    
    //********METHODS**********\\

    //SEARCH
    public static void Search(){
        //Clearing Arrays and Result Pane
        results.getChildren().clear();
        sp.setVvalue(0);
        statusTxt.setStyle("-fx-text-fill:#7FFF00");
        missingAirportTxt.setStyle("-fx-text-fill:#666699");

        //Variables
        int num_results = 0;
        String flight_num;
        String ac_num;
        String airport_code;

        //Setting search variables
        if (acTxt.getText().equals("Aircraft Number")){
            ac_num = "*";
        }
        else{
            ac_num = acTxt.getText().toUpperCase();
        }
        if (flightTxt.getText().equals("Flight Number")){
            flight_num = "*";
        }
        else{
            flight_num = flightTxt.getText().toUpperCase();
        }
        if (airportTxt.getText().equals("Airport Code")){
            airport_code = "*";
        }
        else{
            airport_code = airportTxt.getText().toUpperCase();
        }

        for(int i=0; i<acArray.size(); i++){
            acArray.get(i).setHasLongGround(false);
        }
        
        drawVlines();
        
        //Printing ACs matching search criteria to screen
        for(int j = 0; j < acArray.size(); j++){

            //Marking ACs which has a long ground connection
            for(int i = 0; i < acArray.get(j).flight_array.size(); i++){
                double ground_time;
                if(i == 0){
                    ground_time = acArray.get(j).getFlight(i).getDepTimeRatio() - acArray.get(j).getFlight(acArray.get(j).flight_array.size()-1).getArrTimeRatio();
                    if(ground_time<0){
                        ground_time = ground_time+2400;
                    }
                }
                else{
                    ground_time = acArray.get(j).getFlight(i).getDepTimeRatio() - acArray.get(j).getFlight(i-1).getArrTimeRatio();
                }
                if((ground_time >= Integer.parseInt(AIManager.downTxt.getText())*100 &&
                    airport_code.equals(acArray.get(j).getFlight(i).getDepstation())) ||
                   (ground_time >= Integer.parseInt(AIManager.downTxt.getText())*100 &&
                   (airport_code.equals("*") || airport_code.equalsIgnoreCase("")))){
                    acArray.get(j).setHasLongGround(true);
                }
            }

/***********************CREATE boolean********************************/                   
            //Checking if AC matches search criteria
            if ((acArray.get(j).getHasLongGround() && acArray.get(j).getFlightnum().contains(flight_num) && String.valueOf(acArray.get(j).getAcnum()).contains(ac_num)) ||
                (acArray.get(j).getHasLongGround() && flight_num.equals("*") && String.valueOf(acArray.get(j).getAcnum()).contains(ac_num)) ||
                (acArray.get(j).getHasLongGround() && acArray.get(j).getFlightnum().contains(flight_num) && ac_num.equals("*")) ||
                (acArray.get(j).getHasLongGround() && flight_num.equals("*") && ac_num.equals("*"))){

                num_results++;            
                
                drawHlines(num_results-1);
                drawAcLabel(num_results-1,j);

                //Drawing Flights
                for(int i = 0; i < acArray.get(j).flight_array.size(); i++){
                    //VARIABLES
                    double dep_time_in_gui = acArray.get(j).getFlight(i).getDepTimeInGui();
                    double arr_time_in_gui = acArray.get(j).getFlight(i).getArrTimeInGui();
                    double duration_in_gui = arr_time_in_gui - dep_time_in_gui;
                    double ground_time;
                    Font font = new Font("Verdana",20);
                    
                    if(i==0){
                         ground_time = acArray.get(j).getFlight(i).getDepTimeRatio() - acArray.get(j).getFlight(acArray.get(j).flight_array.size()-1).getArrTimeRatio();
                         if(ground_time<0){
                             ground_time = ground_time + 2400;
                         }
                    }
                    else{
                        ground_time = acArray.get(j).getFlight(i).getDepTimeRatio() - acArray.get(j).getFlight(i-1).getArrTimeRatio();
                    }   
                        /***********************CREATE BOOLEAN********************************/
                        if((ground_time >= Integer.parseInt(AIManager.downTxt.getText())*100 &&
                            airport_code.equals(acArray.get(j).getFlight(i).getDepstation())) ||
                           (ground_time >= Integer.parseInt(AIManager.downTxt.getText())*100 &&
                           (airport_code.equals("*") || airport_code.equalsIgnoreCase("")))){

                            Line conn_line = new Line();
                            Tooltip groundTimeTooltip = new Tooltip();
                            groundTimeTooltip.setText(String.valueOf(ground_time/100).substring(0,3));
                            groundTimeTooltip.setFont(font);
                            Tooltip.install(conn_line, groundTimeTooltip);

                            if(i==0){
                                //720 is 24 hours in GUI (24*30)
                                double endX = acArray.get(j).getFlight(i).getDepTimeInGui() + 720;

                                //1.5 is extra spacing since stroke width is 2.5
                                conn_line.setStartX(acArray.get(j).getFlight(acArray.get(j).flight_array.size()-1).getArrTimeInGui()+1.5);
                                conn_line.setStartY((num_results-1)*AC_SPACING+MIDDLE_ROW);
                                conn_line.setEndX(endX);
                                conn_line.setEndY((num_results-1)*AC_SPACING+MIDDLE_ROW);
                                conn_line.setStroke(Color.RED);
                                conn_line.setStrokeWidth(2.5);

                                results.getChildren().add(conn_line);
                            }
                            else{
                                //Drawing Connection Line
                                conn_line.setStartX(acArray.get(j).getFlight(i-1).getArrTimeInGui()+1.5);
                                conn_line.setStartY((num_results-1)*AC_SPACING+MIDDLE_ROW);
                                conn_line.setEndX(acArray.get(j).getFlight(i).getDepTimeInGui());
                                conn_line.setEndY((num_results-1)*AC_SPACING+MIDDLE_ROW);
                                conn_line.setStroke(Color.RED);
                                conn_line.setStrokeWidth(2.5);

                                results.getChildren().add(conn_line);
                            }

                        }

                    //Drawing Aircrafts
                    //REC(X START, Y START, WIDTH, HEIGHT)
                    Rectangle leg_rec = new Rectangle(dep_time_in_gui,(num_results-1)*AC_SPACING+44,duration_in_gui,15);
                    Tooltip stations = new Tooltip();
                    stations.setText(acArray.get(j).getFlight(i).toString());
                    stations.setFont(font);
                    Tooltip.install(leg_rec, stations);
                    
                    //Alternating Leg Colors
                    leg_rec.setFill(Color.BLUE);
                    if((j%2>0)){
                        leg_rec.setFill(Color.BLUEVIOLET);
                    }

                    //Adding to Rectangle Array and to results pane
                    results.getChildren().addAll(leg_rec);
                }
            }
        }
        printHoursHeader();
        AIManager.searchCountTxt.setText("Aircraft Found: "+num_results);
    }
    
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
        AIManager.loadTxt.setText(selectedfile.getAbsolutePath());
        AIManager.filePathInput = AIManager.loadTxt.getText();
        
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
                acArray.add(ac);
            }
        }
    }
    
    //Print/Load all ac_array to screen Method:
    public void printAllAcs(){
        //Setting AC COUNT TEXT
        AIManager.acCountTxt.setText("Aircraft Loaded: "+acArray.size());
        AIManager.searchCountTxt.setText("Aircraft Found: "+acArray.size());
        
        //Drawing H lines and Printing Aircraft Labels
        for (int i=0; i < acArray.size(); i++) {
            drawHlines(i);
            drawAcLabel(i,i);
        }
        
        drawVlines();
                
        //Drawing Flights + CONNECTION LINE
        for(int j = 0; j < acArray.size(); j++){
            for(int i = 0; i < acArray.get(j).flight_array.size(); i++){
                //VARIABLES
                double dep_time_in_gui = acArray.get(j).getFlight(i).getDepTimeInGui();
                double arr_time_in_gui = acArray.get(j).getFlight(i).getArrTimeInGui();
                double duration_in_gui = arr_time_in_gui - dep_time_in_gui;

                //Drawing Aircrafts
                //REC(X START, Y START, WIDTH, HEIGHT)
                Rectangle leg_rec = new Rectangle(dep_time_in_gui,j*AC_SPACING+44,duration_in_gui,15);
                Tooltip stations = new Tooltip();
                stations.setText(acArray.get(j).getFlight(i).toString());
                Font font = new Font("Verdana",20);
                stations.setFont(font);
                Tooltip.install(leg_rec, stations);
                
                //Alternating Leg Colors
                leg_rec.setFill(Color.BLUE);
                if((j%2>0)){
                    leg_rec.setFill(Color.BLUEVIOLET);
                }
                results.getChildren().addAll(leg_rec);
            }
        }
        printHoursHeader();
    }
    
    public static void printHoursHeader(){
        //Variables
        String airport = airportTxt.getText();
        String hours;
        int hoursInt;
        int timeDifference;
        boolean missingAirport = false;
        
        //Rectangle for Hours header
        Rectangle hoursRec = new Rectangle(0,0,2000,17);
        hoursRec.setFill(Color.web("#003366"));
        hoursRec.setStroke(Color.web("#003366"));
        
        Label utcTime = new Label("UTC TIME");
        utcTime.setTextFill(Color.WHITE);
        utcTime.setLayoutX(5);
        utcTime.setLayoutY(0);
        
        Rectangle localHoursRec = new Rectangle(0,18,2000,17);
        localHoursRec.setFill(Color.web("#000000"));
        localHoursRec.setStroke(Color.web("#000000"));
        
        Label localTime = new Label("LOCAL TIME");
        localTime.setTextFill(Color.WHITE);
        localTime.setLayoutX(5);
        localTime.setLayoutY(18);
        
        results.getChildren().addAll(hoursRec,localHoursRec,utcTime,localTime);
        
        //Loops for hours header
        for(int i=0; i<=48; i++){
            hoursInt = i;
            //Hour Labels
            Label hoursLabel = new Label(String.valueOf(i));
            if(i > 24){
                hoursLabel.setText(String.valueOf(i-24));
            }
            hoursLabel.setLayoutX((HOUR_SPACING*i)+LEFT_MARGIN-3);
            hoursLabel.setLayoutY(1);
            hoursLabel.setTextFill(Color.WHITE);
            
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
            
            Label localHoursLabel = new Label();
            localHoursLabel.setLayoutX((HOUR_SPACING*i)+LEFT_MARGIN-3);
            localHoursLabel.setLayoutY(19);
            localHoursLabel.setTextFill(Color.LIGHTSKYBLUE);
            
            if(missingAirport){
                localHoursLabel.setTextFill(Color.RED);
            }
            hours = String.valueOf(hoursInt);
            if(hoursInt<0){
                hours = String.valueOf((hoursInt)+24);
            }
            if(hoursInt > 24){
                hours = String.valueOf((hoursInt)-24);
            }

            localHoursLabel.setText(hours);

            sp.vvalueProperty().addListener((observable, oldValue, newValue) -> {
                double yTranslate = ((newValue.doubleValue() * (acArray.size()*HOUR_SPACING+TOP_MARGIN)) -
                                     (newValue.doubleValue() * (sp.getHeight()-18)));
                hoursLabel.translateYProperty().setValue(yTranslate);
                hoursRec.translateYProperty().setValue(yTranslate);
                utcTime.translateYProperty().setValue(yTranslate);
                localTime.translateYProperty().setValue(yTranslate);
                localHoursRec.translateYProperty().setValue(yTranslate);
                localHoursLabel.translateYProperty().setValue(yTranslate);
                utcTime.translateYProperty().setValue(yTranslate);
                localTime.translateYProperty().setValue(yTranslate);
            });
            results.getChildren().addAll(hoursLabel,localHoursLabel);
        }
        if(missingAirport){
            statusTxt.setStyle("-fx-text-fill:#ff0000");
            missingAirportTxt.setStyle("-fx-text-fill:#ff0000");
        }
    }
    
    public static void drawVlines(){
        //Drawing Vertical Lines and Hour labels
        for(int i=0; i<=48; i++){
            //Vertical Lines
            Line v_lines = new Line();
            v_lines.setStartX(HOUR_SPACING*i+LEFT_MARGIN);
            v_lines.setStartY(0);
            v_lines.setEndX(HOUR_SPACING*i+LEFT_MARGIN);
            v_lines.setEndY(acArray.size()*HOUR_SPACING+TOP_MARGIN);
            v_lines.setStroke(Color.LIGHTSLATEGREY);

            //ADDING TO PANE
            results.getChildren().add(v_lines);
        }
    }
    
    //Not taking the total number of results as input but at which A/C it is at to know where to print the line. 
    //Maybe need to readjust to keep same behavior as Vlines
    public static void drawHlines(int position){
        //HLINES (TO DO: CREATE METHOD)
        Line h_lines = new Line();
        h_lines.setStartX(0);
        h_lines.setStartY(((position)*AC_SPACING)+TOP_MARGIN);
        h_lines.setEndX(2000);
        h_lines.setEndY((position)*AC_SPACING+TOP_MARGIN);
        h_lines.setStroke(Color.LIGHTSLATEGREY);
        results.getChildren().add(h_lines);
    }
    
     public static void drawAcLabel(int position, int ac){
            Label ac_label = new Label(position+1+" AC#"+String.valueOf(acArray.get(ac).getAcnum())+" "+acArray.get(ac).getFlightnum());
            ac_label.setLayoutX(0);
            ac_label.setLayoutY(position*AC_SPACING+TOP_MARGIN);
            ac_label.setTextFill(Color.BLACK);

            results.getChildren().add(ac_label);
    }
    
    public static int getMargin(String whichMargin){
        switch (whichMargin){
            case "LEFT_MARGIN": return LEFT_MARGIN;
            case "TOP_MARGIN": return TOP_MARGIN;
            default: return 0;
        }
    }
}
