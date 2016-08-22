
package ai.manager;

public class WeeklyFlight extends Flight{
    
    //Variables
    private final int day;
    
    public WeeklyFlight(int day,
                        String dep_station,int dep_time,
                        String arr_station,int arr_time){
        
        super(dep_station, dep_time, arr_station, arr_time);
        this.day = day;
        
    }
    
    public int getDay(){
        return day;
    }
    
    public String getDayName(int day){
        switch (day){
            case 0: return "Sunday";
            case 1: return "Monday";
            case 2: return "Tueday";
            case 3: return "Wednesday";
            case 4: return "Thursday";
            case 5: return "Friday";
            case 6: return "Saturday";
            default: return "Error";
        }
    }
    
    @Override
    public String toString(){
        String day_S = getDayName(day);
        String Flight_S = day_S + "\n" +
                          super.toString();
        return Flight_S;
    }
    
}
