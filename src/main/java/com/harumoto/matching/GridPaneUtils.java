package com.harumoto.matching;

import javafx.animation.ParallelTransition;
import javafx.animation.PauseTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

public class GridPaneUtils {

    @SuppressWarnings("exports")
    public static void swapCells(GridPane gridPane, int row1, int col1, int row2, int col2) {
        Node node1 = getNodeByRowColumnIndex(gridPane, row1, col1);
        Node node2 = getNodeByRowColumnIndex(gridPane, row2, col2);

        if (node1 == null || node2 == null || !(node1 instanceof StackPane) || !(node2 instanceof StackPane)) {
            return;
        }

        Node button1 = ((StackPane) node1).getChildren().get(0);
        Node button2 = ((StackPane) node2).getChildren().get(0);

        double deltaX = (col2 - col1) * 45;
        double deltaY = (row2 - row1) * 45;

        TranslateTransition transition1 = new TranslateTransition(Duration.millis(300), button1);
        transition1.setByX(deltaX);
        transition1.setByY(deltaY);

        TranslateTransition transition2 = new TranslateTransition(Duration.millis(300), button2);
        transition2.setByX(-deltaX);
        transition2.setByY(-deltaY);

        ParallelTransition parallelTransition = new ParallelTransition(transition1, transition2);
        parallelTransition.setOnFinished(event -> {
            PauseTransition pause = new PauseTransition(Duration.millis(3));
            pause.setOnFinished(e -> {
                // Update indices and reset translation properties simultaneously
                GridPane.setRowIndex(node1, row2);
                GridPane.setColumnIndex(node1, col2);
                GridPane.setRowIndex(node2, row1);
                GridPane.setColumnIndex(node2, col1);
                button1.setTranslateX(0);
                button1.setTranslateY(0);
                button2.setTranslateX(0);
                button2.setTranslateY(0);
                System.out.println("Swap animation finished.");
            });
            pause.play();
        });

        parallelTransition.play();
    }

    @SuppressWarnings("exports")
    public static void swapCellsWithCallBack(GridPane gridPane, int row1, int col1,
            int row2, int col2,
            Runnable callBack) {
        Node node1 = getNodeByRowColumnIndex(gridPane, row1, col1);
        Node node2 = getNodeByRowColumnIndex(gridPane, row2, col2);

        if (node1 == null || node2 == null || !(node1 instanceof StackPane) || !(node2 instanceof StackPane)) {
            return;
        }

        Node button1 = ((StackPane) node1).getChildren().get(0);
        Node button2 = ((StackPane) node2).getChildren().get(0);

        double deltaX = (col2 - col1) * 45;
        double deltaY = (row2 - row1) * 45;

        TranslateTransition transition1 = new TranslateTransition(Duration.millis(300), button1);
        transition1.setByX(deltaX);
        transition1.setByY(deltaY);

        TranslateTransition transition2 = new TranslateTransition(Duration.millis(300), button2);
        transition2.setByX(-deltaX);
        transition2.setByY(-deltaY);

        ParallelTransition parallelTransition = new ParallelTransition(transition1, transition2);

        // final Runnable runnable = callBack;

        parallelTransition.setOnFinished(event -> {
            PauseTransition pause = new PauseTransition(Duration.millis(3));
            pause.setOnFinished(e -> {
                // Update indices and reset translation properties simultaneously
                GridPane.setRowIndex(node1, row2);
                GridPane.setColumnIndex(node1, col2);
                GridPane.setRowIndex(node2, row1);
                GridPane.setColumnIndex(node2, col1);
                button1.setTranslateX(0);
                button1.setTranslateY(0);
                button2.setTranslateX(0);
                button2.setTranslateY(0);
                System.out.println("Swap animation finished.");
                callBack.run();
            });
            pause.play();
        });

        parallelTransition.play();
    }

    @SuppressWarnings("exports")
    public static Node getNodeByRowColumnIndex(GridPane gridPane, int row, int col) {
        for (Node node : gridPane.getChildren()) {
            Integer nodeRow = GridPane.getRowIndex(node);
            Integer nodeCol = GridPane.getColumnIndex(node);

            if (nodeRow != null && nodeCol != null && nodeRow == row && nodeCol == col) {
                return node;
            }
        }
        return null;
    }
}
