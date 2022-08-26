import java.util.LinkedList;
import java.util.Queue;

public class Knight_BFS {
    private Position[][] adjM = new Position[8][8];
    private boolean[][] visitedM = new boolean[8][8];
    private boolean foundTarget = false;
    private Queue<Position> bfsQ = new LinkedList<Position>();
    private LinkedList<Position> pathList = new LinkedList<Position>();
    private Position target;

    Knight_BFS(Position source, Position target) {
        this.target = target;
        bfsQ.add(source);
        visitedM[source.getY()][source.getX()] = true;
        adjM[source.getY()][source.getX()] = null;
        doKnightBFS();
    }

    private LinkedList<Position> doKnightBFS() {
        while (!bfsQ.isEmpty()) {
            Position poppedPosition = bfsQ.remove();
            if (poppedPosition.equals(target)) {
                foundTarget = true;
                break;
            } else {
                addKnightMoves(poppedPosition);
            }
        }

        if (foundTarget) {
            Position curr = target;
            while (curr != null) {
                pathList.addFirst(curr);
                curr = adjM[curr.getY()][curr.getX()];
            }
        }

        return pathList;
    }

    private void addKnightMoves(Position p) {
        int[] yMod = {-2, -1, 1, 2, 2, 1, -1, -2};
        int[] xMod = {1, 2, 2, 1, -1, -2, -2, -1};
        for (int i = 0; i < 8; i++) {
            int potX = p.getX() + xMod[i];
            int potY = p.getY() + yMod[i];
            if (validMove(potX, potY) && !visitedM[potY][potX]) {
                Position newPosition = new Position(potX, potY);
                bfsQ.add(newPosition);
                visitedM[potY][potX] = true;
                adjM[potY][potX] = p;
            }
        }
    }

    private boolean validMove(int x, int y) {
        return (0 <= x && x < 8) && (0 <= y && y < 8);
    }

    public LinkedList<Position> getPathList() {
        return pathList;
    }

}
