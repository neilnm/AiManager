package ai.manager;

import javafx.event.Event;
import javafx.event.EventHandler;
import static ai.manager.AIManager.downTxt;
import static ai.manager.AIManager.flightTxt;
import static ai.manager.AIManager.acTxt;
import static ai.manager.AIManager.airportTxt;
import static ai.manager.AIManager.toTxt;
import static ai.manager.AIManager.fromTxt;

public class ClickAction implements EventHandler<Event>{

    @Override
    public void handle(Event event) {
        if(event.getSource().equals(acTxt)){
            acTxt.clear();
        }
        
        if(event.getSource().equals(flightTxt)){
            flightTxt.clear();
        }
        
        if(event.getSource().equals(airportTxt)){
            airportTxt.clear();
        }
        
        if(event.getSource().equals(downTxt)){
            downTxt.clear();
        }
        
        if(event.getSource().equals(fromTxt)){
            fromTxt.clear();
        }
        
        if(event.getSource().equals(toTxt)){
            toTxt.clear();
        }
        
    }
    
}
