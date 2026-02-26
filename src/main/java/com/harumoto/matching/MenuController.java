package com.harumoto.matching;

import java.util.ArrayList;

import com.harumoto.matching.MatchingGame.Levels.Level;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

public class MenuController {

    @FXML
    Button closeMenuButton;

    @FXML
    GridPane menuGrid;

    @FXML
    private void onCloseMenuButtonPressed(Event e) throws Exception {
        App.setRoot("gamescene", () -> {
        });
    }

    @FXML
    private void initialize() {
        initMenuButtons();
    }

    private void initMenuButtons() {

        Integer levelsSize = App.levels.size();
        ArrayList<StackPane> levelPanes = new ArrayList<>();

        Integer counter = 0;
        Integer index = 0;

        // Loop to find all StackPane elements in the menu grid
        while (counter != levelsSize) {
            if (this.menuGrid.getChildren().get(index) != null) {
                counter++;
                levelPanes.add((StackPane) this.menuGrid.getChildren().get(index));
            }
            index++;
        }

        // Loop through each level to initialize corresponding buttons
        for (Integer i = 0; i < levelsSize; i++) {

            final Integer innerI = i;
            Button levelButton = (Button) levelPanes.get(i).getChildren().get(0);

            if (App.passed[i])
                levelButton.getStyleClass().add("passed");

            if (i != App.levelIndex)
                levelButton.setOnAction(e -> {
                    try {

                        switchLevel(innerI);
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                });
        }
    }

    private void switchLevel(Integer levelIndex) throws Exception {
        for (Integer j = App.levelIndex; j >= levelIndex; j--) {
            Level reloadedLevel = new Level(App.currentLevel().getPath());
            App.levels.set(App.levelIndex, reloadedLevel);
            App.levelIndex = levelIndex;
            App.setRoot("gamescene", () -> {
            });
        }
    }

}
