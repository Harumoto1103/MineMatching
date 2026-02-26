# MineMatching

MineMatching is a Minecraft-themed "match-3" style elimination game built with JavaFX. The game offers rich and engaging gameplay through various items and elements across four main interfaces: Start, Game, Menu, and Game Over. 

## Features & Gameplay
* **Levels**: The game consists of 6 distinct levels, each with specific clear conditions based on either the number of elements eliminated or a limited number of elimination moves.
* **Interaction**: Players interact with the game board by clicking to eliminate elements or dragging to swap them. A valid elimination requires at least 3 identical elements connected in a row or column.
* **Element Types**: 
    * **Ordinaries**: Standard elements that only affect themselves when eliminated.
    * **Eliminators**: Pulsing elements that, when triggered, eliminate all Ordinaries of the same appearance.
    * **Specials**: Includes **Bomb** elements that clear a 3x3 grid, and **Diamond Sword** elements that clear an entire column.
    * **Walls**: Immovable obstacles appearing in Level 6.
    * **Nulls**: Blank elements that briefly appear after an elimination.
* **Items**: 
    * **Hammer**: Clears a specific element without triggering other eliminations (earned by eliminating 5+ elements at once).
    * **Restore Potion**: Undoes the last action (earned upon completing a level).
    * **Poison Potion (Give Up)**: Instantly ends the current level, allowing the player to fail and move on.

## Architecture & Design
The project is structured into modular components, cleanly separating the GUI controllers from the core game logic using Object-Oriented Programming (OOP) principles.
* **Core Modules**: The `App.java` class serves as the primary game entry point, while controllers (`PrimaryController`, `MenuController`, `GameSceneController`, `GameOverController`) handle UI and level transitions.
* **Game Engine**: The `MatchingGame` module contains `GameCanvas.java` for managing the 2D array board, alongside `Judge` and `Levels` sub-modules for evaluating win-conditions.
* **OOP Principles**: The design utilizes encapsulation for coordinate data in `Coordinate.java`. It employs inheritance and polymorphism for game pieces, using `Piece.java` as the abstract base class and specific elements like `Eliminator.java` and `Special.java` as subclasses.

## Build & Deployment
* **Prerequisites**: The application supports Windows, macOS, and Linux. It has been tested successfully on Ubuntu 24.04 (JDK 22) and macOS 15 (JDK 16).
* **Running the Game**: 
  Navigate to the `matching-gui` folder and execute the following Maven commands:
```bash
  mvn clean install
  mvn javafx:run
```
- **Packaging**: The `pom.xml` designates `Launcher.java` as the `mainClass`. The packaged `.jar` file bundles all dependencies and can be executed via the `java -jar` command.
- **macoS Note**: A specific workaround is implemented in the `OperatingSystem.java` class to replace an unexpected drag-and-drop file icon on macOS with a transparent `WritableImage`. If unexpected graphical behavior occurs on macOS related to this, you may need to remove that specific code block.
