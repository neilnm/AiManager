/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ai.manager;

import static ai.manager.AIManager.ac_array;
import static ai.manager.AIManager.search_array;
import static ai.manager.AIManager.results;
import static ai.manager.AIManager.sp;
import java.io.File;
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
    
    @Override
    public void handle(ActionEvent btn_load){
        
        //Variables
        Scanner reader;
        ArrayList<String> lineList = new ArrayList<>();
        ArrayList<String[]> commalist = new ArrayList<>();
        
        
        //RESET BUTTON
        if(btn_load.getSource().equals(AIManager.btn_reset)){
            //Clearing Arrays and Result Pane
            AIManager.results.getChildren().clear();
            AIManager.ac_array.clear();
            AIManager.rec_array.clear();
            
            //Reloading ac count text
            AIManager.ac_count_txt.setText("Aircraft Loaded: "+AIManager.ac_array.size());
            
        }
        
        
        //SEARCH BUTTON
        if(btn_load.getSource().equals(AIManager.btn_search)){
            //Clearing Arrays and Result Pane
            AIManager.results.getChildren().clear();
            
            //Reloading ac count text
            AIManager.ac_count_txt.setText("Aircraft Loaded: "+AIManager.ac_array.size());
            
        }
        
        
        //LOAD BUTTON
        if(btn_load.getSource().equals(AIManager.btn_load)){
            
            //Window Explorer screen
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Choose Flight Plan");
            File selectedfile = fileChooser.showOpenDialog(null);
            AIManager.load_text.setText(selectedfile.getAbsolutePath());
            AIManager.filePathInput = AIManager.load_text.getText();

            try{
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
                                    
                                    /*System.out.println(commalist.get(i)[10+position]);
                                    System.out.println(Double.parseDouble(commalist.get(i)[11+position].substring(0,5).replaceAll(":","")));
                                    System.out.println(commalist.get(i)[16+position]);
                                    System.out.println(Double.parseDouble(commalist.get(i)[12+position].substring(0,5).replaceAll(":","")));*/
                                    position = position+6;
                                }
                            }
                        }
                        catch(java.lang.NumberFormatException | java.lang.StringIndexOutOfBoundsException e){
                            System.out.println("test: "+i+" "+commalist.get(commalist.size()-1)[0].substring(3)+" "+commalist.get(commalist.size()-1)[1]);
                        }
                        AIManager.ac_array.add(ac);
                        
                    }
                }
            }
            catch(java.io.FileNotFoundException e){
                System.out.println(e);
            }
            finally{
                //Variables
                int top_margin = 18;
                int left_margin = 130;
                int ac_label_spacing = 20;
                int ac_line_spacing = 30;
                int hour_spacing = 30;
                
                //Setting AC COUNT TEXT
                AIManager.ac_count_txt.setText("Aircraft Loaded: "+AIManager.ac_array.size());
                
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
                    AIManager.results.getChildren().add(h_lines);
                    AIManager.results.getChildren().add(ac_label);
                }

                
                //Drawing Vertical Lines and Hour labels
                for(int i=0; i<=48; i++){
                    //Vertical Lines
                    Line v_lines = new Line();
                    v_lines.setStartX(30*i+130);
                    v_lines.setStartY(0);
                    v_lines.setEndX(30*i+130);
                    v_lines.setEndY(AIManager.ac_array.size()*hour_spacing+top_margin);
                    v_lines.setStroke(Color.LIGHTSLATEGREY);
                    
                    //ADDING TO PANE
                    results.getChildren().add(v_lines);
                }
                
                //Drawing Flights + CONNECTION LINE + STATION LABELS
                for(int j = 0; j < ac_array.size(); j++){
                    
                    /*Old Connection Line
                    //CONNECTION LINE
                    Line conn_line = new Line();
                    conn_line.setStartX(left_margin);
                    conn_line.setStartY(j*ac_line_spacing+34);
                    conn_line.setEndX(2000);
                    conn_line.setEndY(j*ac_line_spacing+34);
                    conn_line.setStroke(Color.LAWNGREEN);
                    */
                    
                    for(int i = 0; i < ac_array.get(j).flight_array.size(); i++){
                        System.out.println(ac_array.get(j).getFlightnum()+"Num of flights :"+ac_array.get(j).getFlight(i));
                        
                        //VARIABLES
                        double dep_time_in_gui = timeTopixel(ac_array.get(j).getFlight(i).getDeptimeRatio());
                        double arr_time_in_gui = timeTopixel(ac_array.get(j).getFlight(i).getArrtimeRatio());
                        double duration_in_gui = arr_time_in_gui - dep_time_in_gui;
                        Line conn_line = new Line();

                        //Drawing Aircrafts
                        //REC(X START, Y START, WIDTH, HEIGHT)
                        Rectangle leg_rec = new Rectangle(dep_time_in_gui+30,j*30+26,duration_in_gui,15);
                        Tooltip stations = new Tooltip();
                        stations.setText(ac_array.get(j).getFlight(i).getDepstation()+" - "+ac_array.get(j).getFlight(i).getArrstation()+"\n"+
                                         ac_array.get(j).getFlight(i).getDeptime()+" - "+ac_array.get(j).getFlight(i).getArrtime());
                        if (ac_array.get(j).getFlight(i).getDeptime() >= 2400){
                            stations.setText(ac_array.get(j).getFlight(i).getDepstation()+"-"+ac_array.get(j).getFlight(i).getArrstation()+"\n"+
                            (ac_array.get(j).getFlight(i).getDeptime()-2400)+" - "+ac_array.get(j).getFlight(i).getArrtime());
                        }
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
                        
                        //Connection Lines
                        
                        if(i > 0){
                            double ground_time = ac_array.get(j).getFlight(i).getDeptimeRatio() - ac_array.get(j).getFlight(i-1).getArrtimeRatio();
                            System.out.println(ground_time);
                            if(ground_time >= Integer.parseInt(AIManager.down_text.getText())*100){
                                conn_line.setStartX(timeTopixel(ac_array.get(j).getFlight(i-1).getArrtimeRatio())+31.5);
                                conn_line.setStartY(j*ac_line_spacing+34);
                                conn_line.setEndX(timeTopixel(ac_array.get(j).getFlight(i).getDeptimeRatio())+30);
                                conn_line.setEndY(j*ac_line_spacing+34);
                                conn_line.setStroke(Color.RED);
                                conn_line.setStrokeWidth(2.5);
                                
                            }

                        }
                        
                        
                        

                        //Adding to Rectangle Array and to results pane
                        //AIManager.rec_array.add(leg_rec);
                        AIManager.results.getChildren().addAll(conn_line,leg_rec);
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
                        double yTranslate = ((newValue.doubleValue() * (AIManager.ac_array.size()*hour_spacing+top_margin)) -
                                             (newValue.doubleValue() * (sp.getHeight()-19)));
                        hours_label.translateYProperty().setValue(yTranslate);
                        hours_rec.translateYProperty().setValue(yTranslate);
                    });
                    
                    //ADDING TO PANES
                    results.getChildren().add(hours_label);
                
                }
            }   
        }
    }
        public double timeTopixel(double time){
            //Divided by 100 because 134 time is 1.34 hours * 30 (30 is spacing per hour) + 130 (130 is 100 for margin spacing and 30 for 1 hour spacing because starting at 00:00)
            //Returning a minimum of 130 
            return time/100*30+100;
            
            //This used to be to return minimum time at margin but does not seem neccesary anymore because of adjustment of starting at 00:00 and dealing with negative flight times.
            /*
            if(time/100*30+130<130){
                return 130.0;
            }
            else{
                return time/100*30+100;
            }*/
        }
}
