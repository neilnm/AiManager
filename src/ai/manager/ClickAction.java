package ai.manager;

import static ai.manager.AIManager.ac_text;
import static ai.manager.AIManager.flight_text;
import static ai.manager.AIManager.airport_text;
import javafx.event.Event;
import javafx.event.EventHandler;

public class ClickAction implements EventHandler<Event>{

    @Override
    public void handle(Event event) {
        if(event.getSource().equals(ac_text)){
            ac_text.clear();
        }
        
        if(event.getSource().equals(flight_text)){
            flight_text.clear();
        }
        
        if(event.getSource().equals(airport_text)){
            airport_text.clear();
        }
        
    }
    
}
