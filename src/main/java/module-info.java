module com.harumoto.matching {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.base;
    requires javafx.graphics;
    requires java.desktop;

    opens com.harumoto.matching to javafx.fxml;

    exports com.harumoto.matching;
    exports com.harumoto.matching.MatchingGame.Levels;
    exports com.harumoto.matching.MatchingGame;
    exports com.harumoto.matching.MatchingGame.Pieces;
    exports com.harumoto.matching.MatchingGame.Judge;
}
