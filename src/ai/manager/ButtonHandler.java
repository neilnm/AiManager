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
import static ai.manager.AIManager.flightTxt;
import static ai.manager.AIManager.acTxt;
import static ai.manager.AIManager.airportTxt;
import static ai.manager.AIManager.acArray;
import static ai.manager.AIManager.statusTxt;
import static ai.manager.AIManager.fromTxt;
import static ai.manager.AIManager.toTxt;

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
import javafx.scene.control.TextField;

public class ButtonHandler implements EventHandler<ActionEvent>{
    
    //Margin and Spacing Variables
    static int TOP_MARGIN = 36;
    static int LEFT_MARGIN = 160;
    static int AC_SPACING = 30;
    static int HOUR_SPACING = 30;
    static int MIDDLE_ROW = 52;
    TextField loadTxt = new TextField();
    
    public ButtonHandler(){
    }
    
    public ButtonHandler(TextField loadTxt){
        this.loadTxt = loadTxt;
    }
    
    @Override
    public void handle(ActionEvent action_event){
        //LOAD BUTTON
        if(action_event.getSource().equals(AIManager.btnLoad)){
            //clearing
            acArray.clear();
            results.getChildren().clear();
            sp.setVvalue(0);
            loadTxt.setText("");
            
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
            fromTxt.setText("From");
            toTxt.setText("To");
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
        String from_s = fromTxt.getText();
        Double from_d = 0.0;
        String to_s = toTxt.getText();
        Double to_d = 0.0;

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
        
        if (from_s.equals("From") || from_s.equals("")){
            from_s = "*";
        }
        else{
            from_d = Math.floor(timeToRatio(Double.parseDouble(from_s)));
        }
        
        if (to_s.equals("To") || to_s.equals("")){
            to_s = "*";
        }
        else{
            to_d = Math.floor(timeToRatio(Double.parseDouble(to_s)));
        }
        
        for(int i=0; i<acArray.size(); i++){
            acArray.get(i).setHasLongGround(false);
            for(int j=0; j < acArray.get(i).flight_array.size(); j++){
                acArray.get(i).getFlight(j).setHasInBetween(false);
            }
        }
        
        drawVlines();
        
        
        //Printing ACs matching search criteria to screen
        for(int j = 0; j < acArray.size(); j++){
            
            double time_shift = acArray.get(j).getFlight(0).getDepTimeRatio();
            
            //Marking ACs which has a long ground connection
            for(int i = 0; i < acArray.get(j).flight_array.size(); i++){
                double ground_time;
                
                double avail_start;
                double avail_end;
                boolean to_b = false;
                boolean from_b = false;
                boolean to_b2 = false;
                boolean from_b2 = false;
                boolean is_in_between;
                boolean last_ac = i+1 == acArray.get(j).flight_array.size();
                
                double local_diff = 0.0;
                
                String down_station = acArray.get(j).getFlight(i).getArrstation();

                
                boolean found_from = false;
                boolean found_to = false;
                
                for(int k=0; k < localTimeCommaList.size()-1; k++){
                    if(down_station.equalsIgnoreCase(localTimeCommaList.get(k)[0])){
                        local_diff = Double.parseDouble(localTimeCommaList.get(k)[1])*100;
                        break;
                    }
                }
                
                
                /*between time*/
                
                double from_eval = from_d;
                double to_eval = to_d;
                /*Adding a day if TO is larger than FROM*/
                /*System.out.println("Start of from_d:"+from_d);                
                System.out.println("Start of to_d:"+to_d);
                System.out.println("Start of from_eval:"+from_eval); 
                System.out.println("Start of to_eval:"+to_eval);
                System.out.println("");*/
                if(to_eval < from_eval){
                    to_eval += 2400.0;
                }
                
                /*time shift adjustment*/
                from_eval -= time_shift;
                to_eval -= time_shift;
                
                if(acArray.get(j).getFlight(i).getArrTimeRatio() > 2400.0 || last_ac){
                    /*if rot within 2400 and (last avail is -2400 >= than to_d)*/
                    boolean cond1 = acArray.get(j).getFlight(acArray.get(j).flight_array.size()-1).getArrTimeRatio() < 2400.0;
                    boolean cond2 = (acArray.get(j).getFlight(0).getDepTimeRatio() + 2400.0) > to_d;
                    boolean cond3 = (from_d < to_d);
                    boolean cond = (cond1 && cond2 && cond3);
                            
                    /*if rotation touches 2 days*/

                    if(acArray.get(j).getFlight(acArray.get(j).flight_array.size()-1).getArrTimeRatio() > 2400.0 || cond){
                        to_eval += 2400.0;
                        from_eval += 2400.0;
                    }
                    avail_start = acArray.get(j).getFlight(i).getArrTimeRatio() - time_shift;
                    if(last_ac){
                        avail_end = (acArray.get(j).getFlight(0).getDepTimeRatio() + 2400.0) - time_shift;
                    }
                    else{
                        avail_end = acArray.get(j).getFlight(i+1).getDepTimeRatio() - time_shift;
                    }
                }
                else{
                    avail_start = acArray.get(j).getFlight(i).getArrTimeRatio() - time_shift;
                    avail_end = acArray.get(j).getFlight(i+1).getDepTimeRatio() - time_shift;
                }
                
                /*FROM CHECK*/
                /*System.out.println("AC#: "+i);
                System.out.println("from: "+from_eval);
                System.out.println("avail_start: "+avail_start);
                System.out.println("from_station: "+down_station);
                System.out.println("local_diff: "+local_diff);*/
                
                avail_start += local_diff;
                if(from_eval > avail_start){
                    from_b = true;
                }
                
                if(from_eval - 2400.00 > avail_start){
                    from_b2 = true;
                }
                
                /*TO CHECK*/
                /*System.out.println("to: "+to_eval);
                System.out.println("start_end: "+avail_end);
                System.out.println("");
                System.out.println("to_station: "+down_station);
                System.out.println("local_diff: "+local_diff);*/
                
                avail_end += local_diff;
                if(to_eval < avail_end){
                    to_b = true;
                }
                
                if(to_eval - 2400.0 < avail_end){
                    to_b2 = true;
                }
                
                is_in_between = (from_b && to_b) || (from_b2 && to_b2);
                
                if(is_in_between){
                    acArray.get(j).getFlight(i).setHasInBetween(is_in_between);
                }
                /*between time*/
                
                
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
                                if(acArray.get(j).getFlight(acArray.get(j).flight_array.size()-1).getHasInBetween()){
                                    conn_line.setStroke(Color.GREEN);
                                }
                                else{
                                    conn_line.setStroke(Color.RED);
                                }
                                conn_line.setStrokeWidth(2.5);

                                results.getChildren().add(conn_line);
                            }
                            else{
                                //Drawing Connection Line
                                conn_line.setStartX(acArray.get(j).getFlight(i-1).getArrTimeInGui()+1.5);
                                conn_line.setStartY((num_results-1)*AC_SPACING+MIDDLE_ROW);
                                conn_line.setEndX(acArray.get(j).getFlight(i).getDepTimeInGui());
                                conn_line.setEndY((num_results-1)*AC_SPACING+MIDDLE_ROW);
                                if(acArray.get(j).getFlight(i-1).getHasInBetween()){
                                    conn_line.setStroke(Color.GREEN);
                                }
                                else{
                                    conn_line.setStroke(Color.RED);
                                }
                                
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
                    
                    double ground_time2;
                    if(i==acArray.get(j).flight_array.size()-1){
                         ground_time2 = acArray.get(j).getFlight(0).getDepTimeRatio()- acArray.get(j).getFlight(i).getArrTimeRatio();
                         if(ground_time2<0){
                             ground_time2 = ground_time2 + 2400;
                         }
                    }
                    else{
                        ground_time2 = acArray.get(j).getFlight(i+1).getDepTimeRatio() - acArray.get(j).getFlight(i).getArrTimeRatio();
                    }   
                    stations.setText(acArray.get(j).getFlight(i).toString()+"Down Time: "+String.valueOf(ground_time2/100).substring(0,3));
                    
                    Tooltip.install(leg_rec, stations);
                    
                    //Alternating Leg Colors
                    leg_rec.setFill(Color.BLUE);
                    if((num_results%2>0)){
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
        loadTxt.setText(selectedfile.getAbsolutePath());
        AIManager.filePathInput = loadTxt.getText();
        
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
            Label ac_label = new Label(position+1+" AC#"+String.valueOf(acArray.get(ac).getAcnum())+" "+acArray.get(ac).getFlightnum()+" "+acArray.get(ac).getFlight(0).getDepstation());
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
    
    public static double timeToRatio(Double time){
        double deptime_100;
        double hours;
        double minutes;
        double minutes_100;
        
        minutes = time % 100;
        hours = time - minutes;
        minutes_100 = (minutes / 60) * 100;
        deptime_100 = hours + minutes_100;
        return deptime_100;
    }
    
}
