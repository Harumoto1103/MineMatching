package com.harumoto.matching;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import com.harumoto.matching.MatchingGame.Backup;
import com.harumoto.matching.MatchingGame.Coordinate;
import com.harumoto.matching.MatchingGame.Judge.Condition;
import com.harumoto.matching.MatchingGame.Judge.Judge;
import com.harumoto.matching.MatchingGame.Levels.Level;
import com.harumoto.matching.MatchingGame.Pieces.Piece;
import com.harumoto.matching.MatchingGame.Pieces.PieceTypes;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.WritableImage;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class GameSceneController {

    private final Integer gameGridSize = 5;

    private Boolean animating = false;

    // Declare buttons in the scene.
    @FXML
    private Button menuButton;
    @FXML
    private Button hammerButton;
    @FXML
    private Button restartButton;
    @FXML
    private Button undoButton;
    @FXML
    private Button giveupButton;

    // Declare texts in the scene.
    @FXML
    private Text levelText;
    @FXML
    private Text hammerText;
    @FXML
    private Text potionText;

    @FXML
    private GridPane conditionGrid;
    @FXML
    private GridPane gameGrid;

    @FXML
    public void initialize() {
        initGameGrid();
        updateGameScene();
    }

    /**
     * Initializes the game grid.
     */
    private void initGameGrid() {
        for (Integer i = 0; i < this.gameGridSize; i++) {
            for (Integer j = 0; j < this.gameGridSize; j++) {
                // Creating StackPanes.
                StackPane inLoopPane = new StackPane();
                inLoopPane.getStyleClass().add("frame");

                // Creating Buttons to put inside each StackPane.
                Button inLoopButton = new Button();
                inLoopPane.getChildren().add(inLoopButton);

                // Setting Styles and Texts
                inLoopButton.getStyleClass().add("piece");
                inLoopButton.setText(App.levels.get(App.levelIndex).levelCanvas
                        .getPiece(new Coordinate(i, j)).toString());
                this.gameGrid.add(inLoopPane, j, i);
            }
        }
    }

    // Event handlers lies here.
    @FXML
    private void onGameGridButtonPressed(Integer i, Integer j) throws Exception {

        Coordinate coordinate = new Coordinate(i, j);

        if (this.animating) {
            System.out.println("Animating!");
            return;
        }

        if (App.onHammer) {
            this.animating = true;

            // Add the piece at the pressed coordinate to the judge's satisfied conditions.
            App.currentLevel().getJudge()
                    .addSatisfied(App.currentCanvas().getPiece(coordinate).toString());

            App.currentCanvas().nullize(coordinate);

            updateGameScene();
            App.hammer--;

            this.animatedGravitate(() -> {
                App.currentCanvas().suffuse();
                App.currentLevel().getJudge().log();
                this.updateGameScene();
                this.animating = false;

                // Increment the hammer tool if the judge's log reaches 5.
                if (App.currentLevel().judge.getDLog() >= 5)
                    App.hammer++;

                // Check if all judge conditions are satisfied to move to the next level.
                if (App.currentLevel().getJudge().isAllSatisfied())
                    try {
                        this.next(true);
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                this.updateGameScene();
            }, App.currentCanvas().gravitate());

            // Reset the hammer tool state.
            App.onHammer = false;
        } else {
            // If the hammer tool is not active, clear the piece at the pressed coordinate.
            Coordinate coordinateToClear = new Coordinate(i, j);
            this.clear(coordinateToClear);
        }
    }

    @FXML
    private void onMenuButtonPressed(Event e) throws Exception {
        App.setRoot("menu", () -> {
        });
    }

    @FXML
    private void onGameGridButtonDragDetected(Event e, Button button, Integer i, Integer j) {
        if (this.animating)
            return;
        Dragboard db = button.startDragAndDrop(TransferMode.ANY);
        ClipboardContent cb = new ClipboardContent();
        cb.putString(String.format("%d %d", i, j));

        // Weird thing to do tho... without this, there would be a unknown file icon
        // sticking to the
        // cursor while dragging on macOS.
        if (OperatingSystem.isMac()) {
            WritableImage transparentImage = new WritableImage(1, 1);
            db.setDragView(transparentImage);
        }
        db.setContent(cb);
        e.consume();
    }

    @FXML
    private void onGameGridButtonDragged(DragEvent e) {
        if (this.animating)
            return;
        if (e.getDragboard().hasString())
            e.acceptTransferModes(TransferMode.ANY);
    }

    @FXML
    private void onGameGridButtonDragDropped(DragEvent e, Button button, Integer i, Integer j)
            throws Exception {

        if (this.animating)
            return;

        if (App.onHammer)
            return;

        this.animating = true;

        // Extract the source coordinates from the drag event.
        String str = e.getDragboard().getString();
        int sourceI = Integer.parseInt(str.split(" ")[0]);
        int sourceJ = Integer.parseInt(str.split(" ")[1]);

        // Check if the drop is in the same row or column as the source.
        if ((i - sourceI) * (j - sourceJ) == 0) {
            // Determine the direction of the swap.
            int dx = i - sourceI > 0 ? 1 : (i - sourceI < 0 ? -1 : 0);
            int dy = j - sourceJ > 0 ? 1 : (j - sourceJ < 0 ? -1 : 0);

            // Attempt to swap the pieces and proceed if successful.
            if (attemptSwap(sourceI, sourceJ, dx, dy)) {
                int x = sourceI;
                int y = sourceJ;

                // Swap the pieces on the canvas.
                App.currentCanvas().swap(new Coordinate(x, y), new Coordinate(x + dx, y + dy));

                // Collect all coordinates that can be cleared.
                Set<Coordinate> clearables = new HashSet<>();
                clearables.addAll(App.currentCanvas().getClearables(new Coordinate(x, y)));
                clearables
                        .addAll(App.currentCanvas().getClearables(new Coordinate(x + dx, y + dy)));

                // Satisfy judge conditions and nullify cleared pieces.
                for (Coordinate clearable : clearables) {
                    App.currentLevel().getJudge()
                            .addSatisfied(App.currentCanvas().getPiece(clearable).toString());
                    App.currentCanvas().nullize(clearable);
                }

                // Log the current state of the judge.
                App.currentLevel().getJudge().log();

                // Animate the cell swap with callback for subsequent animations.
                GridPaneUtils.swapCellsWithCallBack(gameGrid, x, y, x + dx, y + dy, () -> {

                    updateGameScene();
                    App.currentCanvas().gravitate();

                    this.animatedGravitate(() -> {
                        updateGameScene();
                        App.currentCanvas().suffuse();

                        updateGameScene();
                        this.animating = false;

                        this.backup();

                        // Increment the hammer tool if the judge's log reaches 5.
                        if (App.currentLevel().judge.getDLog() >= 5)
                            App.hammer++;

                        // Check if all judge conditions are satisfied to move to the next level.
                        if (App.currentLevel().getJudge().isAllSatisfied())
                            try {
                                this.next(true);
                            } catch (Exception e1) {
                                e1.printStackTrace();
                            }
                        this.updateGameScene();
                    }, App.currentCanvas().gravitate());
                });
            }
        }
        // If no animation occurs, reset the animating flag.
        this.animating = false;
    }

    @FXML
    private void onUndoButtonPressed(Event e) {
        if (App.rollBacks <= 0)
            return;
        this.restore();
        App.rollBacks--;
        updateGameScene();
    }

    @FXML
    private void onHammerButtonPressed(Event e) {
        if (App.hammer <= 0)
            return;
        App.onHammer = !App.onHammer;
    }

    @FXML
    private void onGiveupButtonPressed(Event e) throws Exception {
        this.next(false);
    }

    @FXML
    private void onRestartButtonPressed(Event e) throws Exception {
        Level reloadedLevel = new Level(App.currentLevel().getPath());
        App.levels.set(App.levelIndex, reloadedLevel);
        updateGameScene();
    }

    @FXML
    private void onMenuButtonPressed() throws Exception {
        App.setRoot("menu", () -> {
        });
    }

    // Updates
    private void updateConditionGrid() {
        Judge judge = App.currentLevel().getJudge();
        Condition condition = judge.getCondition();

        for (int i = 0; i < Piece.ORDINARIES.length; i++) {

            StackPane stackPane = (StackPane) conditionGrid.getChildren().get(i);
            Integer inLoopCondition = condition.getCondition(Piece.ORDINARIES[i]);
            Integer inLoopSatisfied = condition.getSatisfied(Piece.ORDINARIES[i]);

            // Setting up condition texts for pieces
            Text inLoopConditionText = (Text) stackPane.getChildren().get(0);
            inLoopConditionText.setText(String.format("%d",
                    inLoopSatisfied >= inLoopCondition ? 0 : inLoopCondition - inLoopSatisfied));

            // Setting up translations
            inLoopConditionText.setTranslateX(-5);
            inLoopConditionText.setTranslateY(-5);
        }

        StackPane overAllPane = (StackPane) conditionGrid.getChildren().get(Piece.ORDINARIES.length);
        StackPane timesPane = (StackPane) conditionGrid.getChildren().get(Piece.ORDINARIES.length + 1);
        Text overAllText = (Text) overAllPane.getChildren().get(0);
        Text timesText = (Text) timesPane.getChildren().get(0);

        Integer overAllCondition = condition.getCondition("overall");
        Integer overAllSatisfied = condition.getSatisfied("overall");
        Integer timesCondition = condition.getCondition("times");
        Integer timesSatisfied = condition.getSatisfied("times");

        // Setting up condition texts for overall and times.
        overAllText.setText(String.format("%d",
                overAllSatisfied >= overAllCondition ? 0 : overAllCondition - overAllSatisfied));
        System.out.println(String.format("[GUI] Condition: %d, Satisfied: %d", timesCondition, timesSatisfied));
        timesText.setText(String.format("%d",
                timesSatisfied >= timesCondition ? 0 : timesCondition - timesSatisfied));

        // Adjusting position.
        overAllText.setTranslateX(-5);
        overAllText.setTranslateY(-5);
        timesText.setTranslateX(-5);
        timesText.setTranslateY(-5);
    }

    private void updateUndoText() {
        potionText.setText(String.format("%d", App.rollBacks));
    }

    private void updateHammerText() {
        hammerText.setText(String.format("%d", App.hammer));
    }

    private void updateLevelText() {
        levelText.setText(String.format("Level %d/%d", App.levelIndex + 1, App.levels.size()));
    }

    private void updateGameScene() {
        updateConditionGrid();
        updateUndoText();
        updateHammerText();
        updateLevelText();
        updateGameGrid();
        App.currentLevel().getJudge().printLogger();
        this.animating = false;
    }

    private void setGameGridButtonHandlers() {
        for (Integer i = 0; i < this.gameGridSize; i++) {
            for (Integer j = 0; j < this.gameGridSize; j++) {
                final Integer innerI = i;
                final Integer innerJ = j;
                Button inLoopButton = this.getButton(i, j);
                Coordinate inLoopCoordinate = new Coordinate(i, j);

                if (App.currentCanvas().getPieceType(inLoopCoordinate).equals(PieceTypes.WALL))
                    continue;

                inLoopButton.setOnAction(e -> {
                    try {
                        this.onGameGridButtonPressed(innerI, innerJ);
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                });

                inLoopButton.setOnDragDetected(
                        e -> onGameGridButtonDragDetected(e, inLoopButton, innerI, innerJ));
                inLoopButton.setOnDragOver(e -> onGameGridButtonDragged(e));
                inLoopButton.setOnDragDropped(e -> {
                    try {
                        onGameGridButtonDragDropped(e, inLoopButton, innerI, innerJ);
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                });
            }
        }
    }

    private void updateGameGrid() {
        this.animating = true;
        for (Integer i = 0; i < this.gameGridSize; i++) {
            for (Integer j = 0; j < this.gameGridSize; j++) {
                Coordinate inLoopCoordinate = new Coordinate(i, j);
                Button inLoopButton = this.getButton(i, j);
                String inLoopStrRepr = App.currentCanvas().getPiece(inLoopCoordinate).toString();

                inLoopButton.setText(inLoopStrRepr);
                inLoopButton.getStyleClass().removeIf(style -> style.startsWith("piece-"));

                // Appending ClassNames based on String Representations.
                if (!inLoopStrRepr.equals("+") && !inLoopStrRepr.equals("|")
                        && !inLoopStrRepr.equals("#")) {
                    inLoopButton.getStyleClass().add(String.format("piece-%s", inLoopStrRepr));
                } else if (inLoopStrRepr.equals("+")) {
                    inLoopButton.getStyleClass().add("piece-TNT");
                } else if (inLoopStrRepr.equals("|")) {
                    inLoopButton.getStyleClass().add("piece-sward");
                } else if (inLoopStrRepr.equals("#")) {
                    inLoopButton.getStyleClass().add("piece-wall");
                }

                // Create the shaking animation for the button.
                TranslateTransition shakeTransition = new TranslateTransition(Duration.millis(50), inLoopButton);
                shakeTransition.setByX(10);
                shakeTransition.setAutoReverse(true);
                shakeTransition.setCycleCount(6);
                shakeTransition.setOnFinished(e -> {
                    inLoopButton.setTranslateX(0);
                    this.animating = false;
                });

                shakeTransition.play();
            }
        }
        this.setGameGridButtonHandlers();
    }

    // Additional methods.
    private void clear(Coordinate coordinate) throws Exception {
        this.animating = true;
        if (App.currentLevel().clear(coordinate)) {
            this.updateGameScene();
            Integer fallCount = App.currentCanvas().gravitate();
            this.animatedGravitate(() -> {
                App.currentCanvas().suffuse();
                this.updateGameScene();
                App.currentLevel().getJudge().log();
                this.animating = false;
                if (App.currentLevel().getJudge().getDLog() >= 5)
                    App.hammer++;
                this.backup();
                if (App.currentLevel().getJudge().isAllSatisfied())
                    try {
                        this.next(true);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                this.updateGameScene();
            }, fallCount == 0 ? 3 : fallCount + 1);
        }
        this.animating = false;
    }

    private void backup() {
        Backup backup = new Backup();
        backup.backupSatisfiedArray(
                App.currentLevel().getJudge().getCondition().getSatisfiedArray());
        backup.backupCanvas(App.currentCanvas().getCanvasArray(), App.currentCanvas().size());
        backup.backupLogger(App.currentLevel().judge.getCondition().getLogger(),
                App.currentLevel().judge.getCondition().getLoggedTimes());
        backup.backupHammer(App.hammer);
        App.backupStack.push(backup);
    }

    private void restore() {
        if (App.backupStack.size() == 0)
            return;
        Backup backup = App.backupStack.pop();
        Boolean flag = false;

        for (String key : backup.getSatisfiedArray().keySet()) {
            if (!backup.getSatisfiedArray().get(key)
                    .equals(App.currentLevel().getJudge().getCondition().getSatisfied(key))) {
                flag = true;
                break;
            }
        }

        if (!flag && App.backupStack.size() > 0) {
            backup = App.backupStack.pop();
        }

        backup.restoreLogger(App.currentLevel().getJudge().getCondition().getLogger());
        App.hammer = backup.getHammer();
        App.currentLevel().judge.getCondition().setSatisfiedArray(backup.getSatisfiedArray());
        System.out.println(backup.getSatisfiedArray().get("A"));
        App.currentLevel().judge.getCondition().setLoggedTimes(backup.getLoggedTimes());
        backup.restoreCanvas(App.currentCanvas().getCanvasArray());
        if (App.backupStack.size() == 0)
            this.backup();
        System.out.println("Restore performed: " + App.backupStack.size()); // Debugging statement
    }

    private Boolean attemptSwap(Integer x, Integer y, Integer dx, Integer dy) {
        App.currentCanvas().swap(new Coordinate(x, y), new Coordinate(x + dx, y + dy));
        if (App.currentCanvas().getPieceType(new Coordinate(x, y)).equals(PieceTypes.WALL)
                || App.currentCanvas().getPieceType(new Coordinate(x + dx, y + dy))
                        .equals(PieceTypes.WALL))
            return false;
        Set<Coordinate> clearables = new HashSet<>();
        clearables.addAll(App.currentCanvas().getClearables(new Coordinate(x, y)));
        clearables.addAll(App.currentCanvas().getClearables(new Coordinate(x + dx, y + dy)));
        if (clearables.size() == 0) {
            App.currentCanvas().swap(new Coordinate(x, y), new Coordinate(x + dx, y + dy));
            return false;
        }
        App.currentCanvas().swap(new Coordinate(x, y), new Coordinate(x + dx, y + dy));
        return true;
    }

    private void next(Boolean won) throws Exception {
        System.out.println("Called next();");
        App.backupStack.clear();
        App.passed[App.levelIndex] = won;
        if (won)
            App.rollBacks++;

        if (App.levelIndex + 1 >= App.levels.size()) {
            App.setRoot("gameover", () -> {
            });
            return;
        }

        App.levelIndex++;

        updateGameScene();
        App.setRoot("gamescene", () -> {
            Timeline timeline = new Timeline();
            timeline.setCycleCount(1);
            timeline.getKeyFrames().add(new KeyFrame(Duration.millis(50), ev -> {
                updateGameScene();
            }));
            timeline.play();
        });
    }

    private void animatedGravitate(Runnable onFinished, Integer fallCount) {

        // Create an array to hold the timelines
        ArrayList<Timeline> timelineArray = new ArrayList<>();

        // Determine the number of times the gravity animation should run
        int iterations = (fallCount == 0 ? 2 : fallCount);

        // Loop to create timelines for each iteration
        for (int p = 0; p < iterations; p++) {
            Timeline innerTimeline = new Timeline();
            innerTimeline.setCycleCount(1);

            // Create keyframes for each column in the grid
            for (int i = 0; i < App.currentCanvas().size(); i++) {
                int columnIndex = i;
                innerTimeline.getKeyFrames().add(new KeyFrame(Duration.seconds(0.4), event -> {
                    int k = 4;
                    // Find the first empty cell from bottom to top
                    while (k >= 1 && !(getButton(k, columnIndex).getText().equals(" "))) {
                        k--;
                    }
                    int l = k;
                    // Find the first non-empty cell above the empty cell
                    while (l >= 1 && (getButton(l, columnIndex).getText().equals(" "))) {
                        l--;
                    }

                    // Swap cells if conditions are met
                    if (k != l && l < k && (getButton(k, columnIndex).getText().equals(" "))
                            && !(getButton(l, columnIndex).getText().equals(" "))
                            && !getButton(l, columnIndex).getText().equals("#")) {
                        GridPaneUtils.swapCells(gameGrid, k, columnIndex, l, columnIndex);
                    }
                }));
            }
            timelineArray.add(innerTimeline);
        }

        // Chain timelines so that each one starts when the previous one finishes
        for (int i = 0; i < timelineArray.size() - 1; i++) {
            final int innerI = i;
            timelineArray.get(i).setOnFinished(event -> {
                timelineArray.get(innerI + 1).play();
            });
        }

        // Start the first timeline
        timelineArray.get(0).play();

        // After the last timeline finishes, call the onFinished runnable
        timelineArray.get(timelineArray.size() - 1).setOnFinished(event -> {
            Timeline subTimeline = new Timeline();
            subTimeline.setCycleCount(1);
            subTimeline.getKeyFrames().add(new KeyFrame(Duration.millis(400), subEvent -> {
                onFinished.run();
            }));
            subTimeline.play();
        });
    }

    private Button getButton(int x, int y) {
        Button button = new Button();
        for (int i = 0; i < App.currentCanvas().size() * App.currentCanvas().size(); i++) {
            StackPane inLoopPane = (StackPane) gameGrid.getChildren().get(i);
            if (GridPane.getColumnIndex(inLoopPane) == y && GridPane.getRowIndex(inLoopPane) == x) {
                button = (Button) inLoopPane.getChildren().get(0);
                break;
            }
        }
        return button;
    }
}
