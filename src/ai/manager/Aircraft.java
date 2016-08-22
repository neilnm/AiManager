package ai.manager;

import java.util.ArrayList;
import java.util.List;

public class Aircraft{
    //Constructor variables
    private final int ac_num;
    private final String flight_num;
    
    //Variables
    List<Flight> flight_array = new ArrayList<>();
    
    //Constructor
    public Aircraft(int ac_num,String flight_num){
        this.ac_num = ac_num;
        this.flight_num = flight_num;
    }
    
    //Get Methods
    public int getAcnum(){
        return ac_num;
    }
    
    public String getFlightnum(){
        return flight_num;
    }
    
    public Flight getFlight(int pos){
        return flight_array.get(pos);
    }
    
    public double getGroundTime(int pos){
        return (flight_array.get(pos + 1).getDeptime() - flight_array.get(pos).getArrtime()) - 40;
    }
    
    /**
     *Returns Departure Time of flight at position pos
     * @param pos
     * @return
     */
    public double getDepTime(int pos){
        return flight_array.get(pos).getDeptime();
    }
    
    /**
     *Returns Arrival Time of flight at position pos
     * @param pos
     * @return
     */
    public double getArrTime(int pos){
        return flight_array.get(pos).getArrtime();
    }
 
     /**
     *Returns Duration of flight at position pos
     * @param pos
     * @return
     */
    public double getDuration(int pos){
        return flight_array.get(pos).getDuration();
    }
    
    
    //methods
    public void AddFlight(String dep_station,double dep_time,
                          String arr_station,double arr_time){
        
        flight_array.add(new Flight(dep_station,dep_time,
                                    arr_station,arr_time));
    }
    
    public void AddWeeklyFlight(int day,
                                String dep_station,int dep_time,
                                String arr_station,int arr_time){
        
        flight_array.add(new WeeklyFlight(day,
                                          dep_station,dep_time,
                                          arr_station,arr_time));
    }
    
    
    
}
