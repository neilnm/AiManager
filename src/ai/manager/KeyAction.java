package ai.manager;

import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class KeyAction implements EventHandler<KeyEvent>{

    @Override
    public void handle(KeyEvent event) {
        if(event.getCode() == KeyCode.ENTER){
            ButtonHandler.Search();
        }
    }
}
