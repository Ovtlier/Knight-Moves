import java.util.LinkedList;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.shape.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
 
public class Knight_Moves extends Application {

    private static final int GRID_SIZE = 9;
    private static final int CELL_SIZE = 50;
    private Cell[][] gameBoard = new Cell[9][9];
    
    public static void start(String[] args) {
        launch(args);
    }
    
    @Override
    public void start(Stage primaryStage) {
        initialiseMatrix();
        primaryStage.setTitle("Knight Moves using JavaFX");
        primaryStage.setScene(new Scene(updateMatrix()));
        primaryStage.show();
    }

    private Parent updateMatrix() {
        Pane base = new Pane();
        base.setPrefSize(GRID_SIZE * CELL_SIZE, GRID_SIZE * CELL_SIZE);

        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                Cell cell = gameBoard[i][j];
                
                cell.setOnMouseClicked(e -> {
                    if (e.getButton() == MouseButton.PRIMARY)  {
                        cell.setSource();
                    } else if (e.getButton() == MouseButton.SECONDARY) {
                        cell.setTarget();
                    } else if (e.getButton() == MouseButton.MIDDLE) {
                        clearPaths();
                        Cell sourceC = cell.getSource();
                        Cell targetC = cell.getTarget();
                        if (sourceC != null && targetC != null) {
                            Position sourceP = new Position(sourceC.xValue - 1, sourceC.yValue - 1);
                            Position targetP = new Position(targetC.xValue - 1, targetC.yValue - 1);
                            Knight_BFS newBFS = new Knight_BFS(sourceP, targetP);                    
                            LinkedList<Position> pathList = newBFS.getPathList();
                            for (int pathCount = 0; !pathList.isEmpty(); pathCount++) {
                                Position p = pathList.removeFirst();
                                if (!p.equals(sourceP) && !p.equals(targetP)) {
                                    Cell c = gameBoard[p.getY() + 1][p.getX() + 1];
                                    c.id.setText(Integer.toString(pathCount));
                                    c.drawCell.setFill(Color.MEDIUMBLUE);
                                }
                            }
                        }
                    }
                });
                base.getChildren().add(cell);
            }
        }

        return base;
    }

    private void clearPaths() {
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                Cell cell = gameBoard[i][j];
                if (i == 0 || j == 0) {
                    continue;
                }
                if (!cell.isSource() && !cell.isTarget()) {
                    if (!cell.id.equals(new Text(" "))) {
                        cell.id.setText(" ");
                        cell.drawCell.setFill(Color.BLACK);
                    }
                }
            }
        }
    }

    private void initialiseMatrix() {
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                Cell cell;
                if (i == 0 && j > 0) {
                    cell = new Cell(j, i, Character.toString('a' + j - 1));
                } else if (i >= 0 && j == 0) {
                    if (i == 0) {
                        cell = new Cell(j, i, "BFS");
                    } else {
                        cell = new Cell(j, i, i);
                    }
                } else {
                    cell = new Cell(j, i);
                }
                gameBoard[i][j] = cell;
            }
        }
    }

    private static class Cell extends StackPane {
        enum State {BASE, TARGET, SOURCE};
        private int xValue;
        private int yValue;
        private Text id;
        private State cellState = State.BASE;
        private Rectangle drawCell;
        private static Cell sourceExists = null;
        private static Cell targetExists = null;

        Cell(int x, int y) {
            xValue = x;
            yValue = y;

            setTranslateX(xValue * CELL_SIZE);
            setTranslateY(yValue * CELL_SIZE);

            drawCell = new Rectangle(CELL_SIZE, CELL_SIZE, Color.BLACK);
            drawCell.setStroke(Color.WHITE);
            id = new Text(" ");
            id.setFont(Font.font(30));
            id.setFill(Color.WHITE);
            getChildren().addAll(drawCell, id);
        }

        Cell(int x, int y, int i) {
            this(x, y);
            drawCell.setFill(Color.WHITE);
            id.setText(Integer.toString(i));
            id.setFill(Color.BLACK);
        }

        Cell(int x, int y, String s) {
            this(x, y);
            drawCell.setFill(Color.WHITE);
            id.setText(s);
            id.setFill(Color.BLACK);
        }
        
        private void setSource() {
            if (isGrid()) {
                if (sourceExists == null && isBase()) {
                    cellState = State.SOURCE;
                    drawCell.setFill(Color.GREEN);
                    id.setText("S");
                    sourceExists = this;
                } else if (sourceExists != null && isSource()) {
                    setBase();
                    sourceExists = null;
                }
            }
        }

        private void setTarget() {
            if (isGrid()) {
                if (targetExists == null && isBase()) {
                    cellState = State.TARGET;
                    drawCell.setFill(Color.RED);
                    id.setText("T");
                    targetExists = this;
                } else if (targetExists != null && isTarget()) {
                    setBase();
                    targetExists = null;
                }
            }
        }

        private void setBase() {
            cellState = State.BASE;
            drawCell.setFill(Color.BLACK);
            id.setText(" ");
        }

        private boolean isSource() {
            return cellState == State.SOURCE;
        }

        private boolean isTarget() {
            return cellState == State.TARGET;
        }

        private boolean isBase() {
            return cellState == State.BASE;
        }

        public Cell getSource() {
            return sourceExists;
        }

        public Cell getTarget() {
            return targetExists;
        }

        private boolean isGrid() {
            return xValue != 0 && yValue != 0;
        }
    }
}