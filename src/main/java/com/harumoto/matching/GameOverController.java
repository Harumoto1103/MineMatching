package com.harumoto.matching;

import com.harumoto.matching.MatchingGame.Levels.Level;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;

public class GameOverController {
    @FXML
    Button starA;

    @FXML
    Button starB;

    @FXML
    Button starC;

    @FXML
    Text starText;

    @FXML
    TextArea detailText;

    @SuppressWarnings("exports")
    @FXML
    public void onRestartButtonPressed(Event e) throws Exception {
        for (Integer i = App.levels.size() - 1; i >= 0; i--) {
            App.levelIndex = i;
            Level reloadedLevel = new Level(App.currentLevel().getPath());
            App.levels.set(App.levelIndex, reloadedLevel);
        }
        App.setRoot("primary", () -> {

        });
    }

    @SuppressWarnings("exports")
    @FXML
    public void onQuitButtonPressed(Event e) {
        System.exit(0);
    }

    @FXML
    public void initialize() {

        Integer wins = 0;

        // Loop through the 'passed' list in the App class to count the number of wins
        for (Boolean win : App.passed) {
            wins += win ? 1 : 0;
        }

        // Set the opacity of starA, starB, and starC based on the number of wins
        // If wins are less than the threshold, set opacity to 0 (hide the star), otherwise, show
        // the star
        starA.setStyle(wins < 1 ? "-fx-opacity: 0;" : "");
        starB.setStyle(wins < 3 ? "-fx-opacity: 0;" : "");
        starC.setStyle(wins < 5 ? "-fx-opacity: 0;" : "");

        // Initialize the number of stars to 0
        Integer stars = 0;
        // Determine the number of stars based on the number of wins
        if (wins >= 1 && wins < 3)
            stars = 1;
        if (wins >= 3 && wins < 5)
            stars = 2;
        if (wins >= 5)
            stars = 3;


        starText.setText(String.format("Stars: %d/%d", stars, 3));

        String detailTextString = "Details:\n\n";

        // Loop through each level in the App.levels list to build the details string
        for (Integer i = 0; i < App.levels.size(); i++) {
            detailTextString += String.format("Level %d:\n", i + 1);
            // Check if the level was passed and add the corresponding judge details or a lose
            // message
            if (App.passed[i])
                detailTextString += App.levels.get(i).getJudge().toString();
            else
                detailTextString += "You lose! (Gave up)\n";
            detailTextString += "\n";
        }

        detailText.setText(detailTextString);
    }

}
