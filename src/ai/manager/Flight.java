
package ai.manager;

public class Flight{
    
    //Variables
    private final String dep_station;
    private final double dep_time;
    private final String arr_station;
    private final double arr_time;
    
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
    //1425cav519
    public double getDeptimeRatio(){
        double deptime;
        double deptime_100;
        double hours;
        double minutes;
        double minutes_100;
        
        deptime = getDeptime();
        if (deptime < 100){
            deptime = deptime+2400;
        }
        minutes = deptime % 100;
        hours = deptime - minutes;
        minutes_100 = (minutes / 60) * 100;
        deptime_100 = hours + minutes_100;
        //System.out.println("Dep100: "+deptime_100);
        return deptime_100;
    }
    
    public String getArrstation(){
        return arr_station;
    }
    
    public double getArrtime(){
        return arr_time;
    }
    
    public double getArrtimeRatio(){
        double arrtime;
        double deptime;
        double arrtime_100;
        double hours;
        double minutes;
        double minutes_100;
        deptime = getDeptime();
        arrtime = getArrtime();
        if (deptime < 100){
            arrtime = arrtime+2400;
        }
        minutes = arrtime % 100;
        hours = arrtime - minutes;
        minutes_100 = (minutes / 60) * 100;
        arrtime_100 = hours + minutes_100;
        //System.out.println("Dep100: "+deptime_100);
        return arrtime_100;
    }
    
    public double getDuration(){
        if(getArrtime()<getDeptime()){
            return (getArrtime()+2400) - getDeptime();
        }
        return getArrtime() - getDeptime();
    }
    
    public double getDurationRatio(){
        double duration;
        double duration_100;
        double hours;
        double minutes;
        double minutes_100;
        duration = getArrtime()-getDeptime();
        if(getArrtime()-getDeptime()<=100 && getArrtime()-getDeptime()>60){
            duration = getArrtime()-getDeptime()-40;
        }
        if(getArrtime()<getDeptime()){
            minutes = duration % 100;
            hours = duration - minutes;
            minutes_100 = (minutes / 60) * 100;
            duration_100 = hours + minutes_100;
            return duration_100;
        }
        minutes = duration % 100;
        hours = duration - minutes;
        minutes_100 = (minutes / 60) * 100;
        duration_100 = hours + minutes_100;
        System.out.println("duration: "+duration);
        System.out.println("minutes: "+minutes);
        System.out.println("hours: "+hours);
        System.out.println("minutes_100: "+minutes_100);
        System.out.println("duration_100: "+duration_100);
        return duration_100;
    }
    
    
    
    @Override
    public String toString(){
        String dep_time_S = String.valueOf(dep_time);
        String arr_time_S = String.valueOf(arr_time);
        String Flight_S = dep_station + "\n" +
                          dep_time_S  + "\n" +
                          arr_station + "\n" +
                          arr_time_S;
        return Flight_S;
    }
    
}
