
package ai.manager;

import static ai.manager.AIManager.localTimeCommaList;

public class Flight{
    
    //Variables
    private final String dep_station;
    private final double dep_time;
    private final String arr_station;
    private final double arr_time;
    private final int leftMargin = ButtonHandler.getMargin("LEFT_MARGIN");
    private boolean has_in_between = false;
    
    public Flight(String dep_station,double dep_time,
                  String arr_station,double arr_time){
    
        this.dep_station = dep_station;
        this.dep_time = dep_time;
        this.arr_station = arr_station;
        this.arr_time = arr_time;
    }
    
    public String getDepstation(){
        return dep_station;
    }
    
    public double getDeptime(){
        return dep_time;
    }
    
    public String getArrstation(){
        return arr_station;
    }
    
    public double getArrtime(){
        return arr_time;
    }
    
    public double getDuration(){
        //For negative durations example: 23:00dep 01:00 arrival
        if(getArrtime()<getDeptime()){
            return (getArrtime()+2400) - getDeptime();
        }
        return getArrtime() - getDeptime();
    }
    
    public void setHasInBetween(boolean has_in_between){
        this.has_in_between = has_in_between;
     }
    
    public boolean getHasInBetween(){
        return has_in_between;
     }
    
    //Method to return minutes in % ratio. Example: 30 minutes = 50; 45 minutes = 75.
    public double getDepTimeRatio(){
        double deptime;
        double deptime_100;
        double hours;
        double minutes;
        double minutes_100;
        
        deptime = getDeptime();
        minutes = deptime % 100;
        hours = deptime - minutes;
        minutes_100 = (minutes / 60) * 100;
        deptime_100 = hours + minutes_100;
        return deptime_100;
    }
    
    //Method to return minutes in % ratio. Example: 30 minutes = 50; 45 minutes = 75.
    public double getArrTimeRatio(){
        double arrtime;
        double deptime;
        double arrtime_100;
        double hours;
        double minutes;
        double minutes_100;
        deptime = getDeptime();
        arrtime = getArrtime();
        
        // for flights with minus duration departing after 00:00 and arriving //
        if(getArrtime()-getDeptime()<0){
            arrtime = arrtime + 2400;
        }
        
        minutes = arrtime % 100;
        hours = arrtime - minutes;
        minutes_100 = (minutes / 60) * 100;
        arrtime_100 = hours + minutes_100;
        return arrtime_100;
    }
    
    //Method to return duration in % ratio. Example: 30 minutes = 50; 45 minutes = 75.
    public double getDurationRatio(){
        return getArrTimeRatio() - getDepTimeRatio();
    }
    
    public double getDepTimeInGui(){
        //Divided by 100 because 134 time is 1.34 hours * 30 (30 is spacing per hour) + 130 (130 is 100 for margin spacing and 30 for 1 hour spacing because starting at 00:00)
        //Returning a minimum of 130 
        return getDepTimeRatio()/100*30+leftMargin;
    }
    
    public double getArrTimeInGui(){
        //Divided by 100 because 134 time is 1.34 hours * 30 (30 is spacing per hour) + 130 (130 is 100 for margin spacing and 30 for 1 hour spacing because starting at 00:00)
        //Returning a minimum of 130 
        return getArrTimeRatio()/100*30+leftMargin;
    }
    
    //TO STRING
    @Override
    public String toString(){
        double dep_time_check = dep_time;
        double depDifference = 0;
        double arrDifference = 0;
        
        if(dep_time_check > 2359){
            dep_time_check = dep_time_check - 2400;
        }
        
        for(int k=0; k < localTimeCommaList.size()-1; k++){
            if(dep_station.equalsIgnoreCase(localTimeCommaList.get(k)[0])){
                depDifference = Double.parseDouble(localTimeCommaList.get(k)[1])*100;
            }
        }
        
        for(int k=0; k < localTimeCommaList.size()-1; k++){
            if(arr_station.equalsIgnoreCase(localTimeCommaList.get(k)[0])){
                arrDifference = Double.parseDouble(localTimeCommaList.get(k)[1])*100;
            }
        }
        
        double localDepTime = dep_time_check + depDifference;
        double localArrTime = arr_time + arrDifference;
        
        if(localDepTime<0){
            localDepTime=localDepTime+2400;
        }
        
        if(localArrTime<0){
            localArrTime=localArrTime+2400;
        }
        
        String dep_time_S = String.valueOf(dep_time_check).replace(".0","");
        String arr_time_S = String.valueOf(arr_time).replace(".0","");
        
        String localDepTimeS = String.valueOf(localDepTime).replace(".0","");
        String localArrtimeS = String.valueOf(localArrTime).replace(".0","");
        
        if(dep_time_S.length() < 2){
            dep_time_S = "000"+dep_time_S;
        }
        
        if(arr_time_S.length() < 2){
            arr_time_S = "000"+arr_time_S;
        }
        
        if(dep_time_S.length() < 3){
            dep_time_S = "00"+dep_time_S;
        }
        
        if(arr_time_S.length() < 3){
            arr_time_S = "00"+arr_time_S;
        }
        
        if(dep_time_S.length() < 4){
            dep_time_S = "0"+dep_time_S;
        }
        
        if(arr_time_S.length() < 4){
            arr_time_S = "0"+arr_time_S;
        }
        
        //Local
        
        if(localDepTimeS.length() < 2){
            localDepTimeS = "000"+localDepTimeS;
        }
        
        if(localArrtimeS.length() < 2){
            localArrtimeS = "000"+localArrtimeS;
        }
        
        if(localDepTimeS.length() < 3){
            localDepTimeS = "00"+localDepTimeS;
        }
        
        if(localArrtimeS.length() < 3){
            localArrtimeS = "00"+localArrtimeS;
        }
        
        if(localDepTimeS.length() < 4){
            localDepTimeS = "0"+localDepTimeS;
        }
        
        if(localArrtimeS.length() < 4){
            localArrtimeS = "0"+localArrtimeS;
        }

        
        dep_time_S = dep_time_S.substring(0, 2) + ":" +dep_time_S.subSequence(2, 4);
        arr_time_S = arr_time_S.substring(0, 2) + ":" +arr_time_S.subSequence(2, 4);
        
        localDepTimeS = localDepTimeS.substring(0, 2) + ":" +localDepTimeS.subSequence(2, 4);
        localArrtimeS = localArrtimeS.substring(0, 2) + ":" +localArrtimeS.subSequence(2, 4);
        
        String Flight_S = dep_station + " - " + arr_station + "\n" +
                          "UTC \n" +
                          dep_time_S + " - " + arr_time_S + "\n" +
                          "Local \n" +
                          localDepTimeS + " - " + localArrtimeS + "\n";
        return Flight_S;
    }
    
}
