package com.harumoto.matching;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class PrimaryController {

    @FXML
    private Button startGame;

    @FXML
    private void onStartGameButtonPressed(Event e) throws Exception {
        App.setRoot("gamescene", () -> {
        });
    }

    @FXML
    private void onQuitButtonPressed(Event e) {
        System.exit(0); /* Normal exit. */
    }

}
