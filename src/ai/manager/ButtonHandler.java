/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ai.manager;

import static ai.manager.AIManager.ac_array;
import static ai.manager.AIManager.results;
import static ai.manager.AIManager.hbox3;
import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;

public class ButtonHandler implements EventHandler<ActionEvent>{
    
    //variables
    Scanner reader;

    
    @Override
    public void handle(ActionEvent btn_load){
        ArrayList<String> lineList = new ArrayList<>();
        ArrayList<String[]> commalist = new ArrayList<>();
        
        if(btn_load.getSource().equals(AIManager.btn_reset)){
            AIManager.results.getChildren().clear();
            AIManager.ac_array.clear();
            AIManager.rec_array.clear();
            AIManager.ac_count_txt.setText("Aircraft Loaded: "+AIManager.ac_array.size());
            System.out.println("neil array size :"+AIManager.ac_array.size());
            System.out.println("neil linelist size :"+lineList.size());
            System.out.println("neil comma size :"+commalist.size());
            
        }
        
        if(btn_load.getSource().equals(AIManager.btn_load)){
            System.out.println("neil array size :"+AIManager.ac_array.size());
            System.out.println("neil linelist size :"+lineList.size());
            System.out.println("neil comma size :"+commalist.size());
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

                        //checking if 24 hour aircraft and creating Aircrafts and Flights
                        if(commalist.get(commalist.size()-1)[3].equals("24")){
                            Aircraft ac = new Aircraft(Integer.parseInt(commalist.get(commalist.size()-1)[0].substring(3)),
                                                       commalist.get(commalist.size()-1)[1]);
                            try{
                                ac.AddFlight(commalist.get(commalist.size()-1)[commalist.get(commalist.size()-1).length-1].substring(0,4), 
                                Double.parseDouble(commalist.get(commalist.size()-1)[5].substring(0,5).replaceAll(":","")), 
                                commalist.get(commalist.size()-1)[10], 
                                Double.parseDouble(commalist.get(commalist.size()-1)[6].substring(0,5).replaceAll(":","")));

                               /*
                                System.out.println(commalist.get(commalist.size()-1)[commalist.get(commalist.size()-1).length-1].substring(0,4));
                                System.out.println(Integer.parseInt(commalist.get(commalist.size()-1)[5].substring(0,5).replaceAll(":","")));
                                System.out.println(commalist.get(commalist.size()-1)[10]);
                                System.out.println(Integer.parseInt(commalist.get(commalist.size()-1)[6].substring(0,5).replaceAll(":","")));
                                System.out.println();*/
                            }
                            catch(java.lang.NumberFormatException | java.lang.StringIndexOutOfBoundsException e){
                                System.out.println("test: "+i+" "+commalist.get(commalist.size()-1)[0].substring(3)+" "+commalist.get(commalist.size()-1)[1]);
                            }
                            AIManager.ac_array.add(ac);
                        }
                        /*
                        System.out.println(commalist.get(commalist.size()-1).length);
                        System.out.println(commalist.get(commalist.size()-1)[commalist.get(commalist.size()-1).length-1]);
                        System.out.println(commalist.get(commalist.size()-1)[3]);*/
                    }
                }
            }
            catch(java.io.FileNotFoundException e){
                System.out.println(e);
            }
            finally{
                AIManager.ac_count_txt.setText("Aircraft Loaded: "+AIManager.ac_array.size());
                
                //Drawing H lines and Printing Aircraft Labels
                for (int i=0; i < ac_array.size(); i++) {
                    Line r = new Line();
                    r.setStartX(0);
                    r.setStartY(i*30+18);
                    r.setEndX(2000);
                    r.setEndY(i*30+18);
                    //r.getStrokeDashArray().addAll(2d);
                    r.setStroke(Color.LIGHTSLATEGREY);

                    Label l = new Label(i+1+" AC#"+String.valueOf(ac_array.get(i).getAcnum())+" "+ac_array.get(i).getFlightnum());
                    l.setLayoutX(0);
                    l.setLayoutY(i*30+20);
                    l.setTextFill(Color.BLACK);
                    AIManager.results.getChildren().add(l);
                    AIManager.results.getChildren().add(r);
                }

                for(int i=0; i<=48; i++){
                    //Rectangle r = new Rectangle(30*i,1,1,500);
                    Line r = new Line();
                    r.setStartX(30*i+130);
                    r.setStartY(0);
                    r.setEndX(30*i+130);
                    r.setEndY(AIManager.ac_array.size()*30+18);
                    //r.getStrokeDashArray().addAll(2d);
                    r.setStroke(Color.LIGHTSLATEGREY);
                    Label l = new Label(String.valueOf(i));
                    if(i > 24){
                        l.setText(String.valueOf(i-24));
                    }
                    l.setLayoutX((30*i)+134);
                    l.setLayoutY(1);
                    l.setTextFill(Color.WHITE);
                    results.getChildren().add(r);
                    hbox3.getChildren().add(l);
                }


                //Drawing Flights
                    for(int j = 0; j < ac_array.size(); j++){
                    //for(int j = 0; j < 3; j++){
                        Label neil = new Label();
                        Line r2 = new Line();
                        Label depl = new Label();
                        Label arrl = new Label();
                        r2.setStartX(130);
                        r2.setStartY(j*30+34);
                        r2.setEndX(2000);
                        r2.setEndY(j*30+36);
                        //r.getStrokeDashArray().addAll(2d);
                        r2.setStroke(Color.LAWNGREEN);
                        AIManager.results.getChildren().add(r2);
                        double dep_time_in_gui = rec_Dep(ac_array.get(j).getFlight(0).getDeptimeRatio(),j);
                        double arr_time_in_gui = rec_Dep(ac_array.get(j).getFlight(0).getArrtimeRatio(),j);
                        double duration_in_gui = arr_time_in_gui - dep_time_in_gui;
                        
                        depl.setText(ac_array.get(j).getFlight(0).getDepstation());
                        arrl.setText(ac_array.get(j).getFlight(0).getArrstation());
                        depl.setLayoutX(dep_time_in_gui);
                        arrl.setLayoutX(arr_time_in_gui+35);
                        depl.setLayoutY(j*30+26);
                        arrl.setLayoutY(j*30+26);
                        
                        //Rectangle r = new Rectangle((rec_Dep(ac_array.get(12).getFlight(0).getDeptimeRatio(),j)),j*30+26,(ac_array.get(12).getFlight(0).getDurationRatio())/3,15);
                        Rectangle r = new Rectangle(dep_time_in_gui+30,j*30+26,duration_in_gui,15);
                        //System.out.println("AC:"+ac_array.get(j).getAcnum()+ac_array.get(j).getFlightnum()+"rec position: "+r.getX());
                        //System.out.println("AC:"+ac_array.get(12).getFlightnum());
                        //System.out.println("Dep:"+dep_time_in_gui);
                        //System.out.println("Arr:"+arr_time_in_gui);
                        //System.out.println("Duration:"+arr_time_in_gui);
                        AIManager.rec_array.add(r);
                        //r.setStyle("-fx-border-radius: 10 10 10 10");
                        r.setStyle("-fx-background-radius: 10 10 10 10");
                        r.setFill(Color.BLUE);
                        if((j%2>0)){
                            r.setFill(Color.BLUEVIOLET);
                        }
                        
                        /*//Position test
                        neil.setText("neil");
                        neil.setLayoutX(130);
                        neil.setLayoutY(56); */
                       
                        AIManager.results.getChildren().addAll(r,depl,arrl);

                    }

                /*for(int i = 0; i < AIManager.ac_array.size(); i++){
                    System.out.println("AC# :"+AIManager.ac_array.get(i).getAcnum());
                    System.out.println("Flight# :"+AIManager.ac_array.get(i).getFlightnum());
                }*/
            }   

        }
        

    }
        public double rec_Dep(double time,int pos){
            //if((ac_array.get(pos).getDepTime(0)/100)*30+100<130){
            //Divided by 100 because 134 time is 1.34 hours * 30 (30 is spacing per hour) + 130 (130 is 100 for margin spacing and 30 for 1 hour spacing because starting at 00:00)
            if(time/100*30+130<130){
                System.out.println("neil:"+time);
                return 130.0;
            }
            else{
                return time/100*30+100;
            }
        }
}
