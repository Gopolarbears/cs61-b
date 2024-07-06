package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;


public class Game {
    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 30;
    private long SEED;
    private Random RANDOM = new Random(SEED);
    private List<Room> roomList = new LinkedList<>();

    /**
     * Method used for playing a fresh game. The game should start from the main menu.
     */
    public void playWithKeyboard() {
//        startInterface();
    }

    /**
     * Method used for autograding and testing the game code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The game should
     * behave exactly as if the user typed these characters into the game after playing
     * playWithKeyboard. If the string ends in ":q", the same world should be returned as if the
     * string did not end with q. For example "n123sss" and "n123sss:q" should return the same
     * world. However, the behavior is slightly different. After playing with "n123sss:q", the game
     * should save, and thus if we then called playWithInputString with the string "l", we'd expect
     * to get the exact same world back again, since this corresponds to loading the saved game.
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] playWithInputString(String input) {
        // Fill out this method to run the game using the input passed in,
        // and return a 2D tile representation of the world that would have been
        // drawn if the same inputs had been given to playWithKeyboard().
        input = toLower(input);

        TETile[][] finalWorldFrame = new TETile[WIDTH][HEIGHT];

        initialize(finalWorldFrame);

        StringBuilder seed = new StringBuilder();
        for (char ch : input.toCharArray()) {
            if ('0' <= ch && ch <= '9') {
                seed.append(ch);
            }
        }
        SEED = Long.parseLong(seed.toString());


        //Position p1 = new Position(50, 25);
        //Position p2 = new Position(50, 20);
        //v2Hallway(finalWorldFrame, p1, p2);


        generateRooms(finalWorldFrame);

        connectRooms(finalWorldFrame);

//        ter.renderFrame(finalWorldFrame);
        return finalWorldFrame;
    }

    private void initialize(TETile[][] finalWorldFrame) {
//        ter.initialize(WIDTH, HEIGHT);
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                finalWorldFrame[i][j] = Tileset.NOTHING;
            }
        }
    }

    private  void generateRooms(TETile[][] finalWorldFrame) {
        int num = RandomUtils.uniform(RANDOM, 20, 30);
        while (num > 0) {
            if (generateARoom(finalWorldFrame)) {
                num--;
            }
        }

        roomList.sort((room1, room2) -> {
            return room1.topLeft.x - room2.topLeft.x;
        });
    }

    private boolean generateARoom(TETile[][] finalWorldFrame) {
        int x = RandomUtils.uniform(RANDOM, WIDTH);
        int y = RandomUtils.uniform(RANDOM, HEIGHT);
        Position p = new Position(x, y);
        int width = RandomUtils.uniform(RANDOM, 3, 10);
        int height = RandomUtils.uniform(RANDOM, 3, 10);
        Room room = new Room(p, width, height);
        if (!isRoomValid(room) || !notOverlap(room)) {
            return false;
        }

        roomList.add(room);

        for (int i = room.topLeft.x; i <= room.topRight.x; i++) {
            finalWorldFrame[i][room.topLeft.y] = Tileset.WALL;
        }
        for (int i = room.bottomLeft.x; i <= room.bottomRight.x; i++) {
            finalWorldFrame[i][room.bottomLeft.y] = Tileset.WALL;
        }
        for (int j = room.topLeft.y; j >= room.bottomLeft.y; j--) {
            finalWorldFrame[room.bottomLeft.x][j] = Tileset.WALL;
        }
        for (int j = room.topRight.y; j >= room.bottomRight.y; j--) {
            finalWorldFrame[room.bottomRight.x][j] = Tileset.WALL;
        }


        for (int i = room.topLeft.x + 1; i <= room.topRight.x - 1; i++) {
            for (int j = room.topLeft.y - 1; j >= room.bottomLeft.y + 1; j--) {
                finalWorldFrame[i][j] = Tileset.FLOOR;
            }
        }
        return true;
    }

    private boolean isRoomValid(Room room) {
        if (positionOutRange(room.topLeft)
            || positionOutRange(room.topRight)
            || positionOutRange(room.bottomRight)
            || positionOutRange(room.bottomLeft)) {
            return false;
        }
        return true;
    }

    private boolean positionOutRange(Position p) {
        return  p.x < 0 || p.x >= WIDTH
                || p.y < 0 || p.y >= HEIGHT;
    }

    private boolean notOverlap(Room room) {
        if (roomList.isEmpty()) {
            return true;
        }
        for (Room preRoom : roomList) {
            if (!(room.topRight.x < preRoom.bottomLeft.x - 1    // left
                    || room.topRight.y < preRoom.bottomLeft.y - 1    // bottom
                    || room.bottomLeft.x > preRoom.topRight.x + 1    // right
                    || room.bottomLeft.y > preRoom.topRight.y + 1)) {
                return false;
            }
        }
        return true;
    }

    private void connectRooms(TETile[][] finalWorldFrame) {
        for (int i = 0; i < roomList.size() - 1; i++) {
            Room room1 = roomList.get(i);
            Room room2 = roomList.get(i + 1);
            connectTwoRoom(finalWorldFrame, room1, room2);
        }
    }

    private void connectTwoRoom(TETile[][] finalWorldFrame, Room room1, Room room2) {
        if (room1.topRight.x < room2.topLeft.x) {
            zConnect(finalWorldFrame, room1, room2);
        } else if (room1.topLeft.x > room2.topRight.x) {
            zConnect(finalWorldFrame, room2, room1);
        } else if (room1.bottomLeft.y > room2.topLeft.y) {
            lConnect(finalWorldFrame, room1, room2);
        } else if (room1.topLeft.y < room2.bottomLeft.y) {
            lConnect(finalWorldFrame, room2, room1);
        }
    }

    private void zConnect(TETile[][] finalWorldFrame, Room lRoom, Room rRoom) {
        int x = RandomUtils.uniform(RANDOM, lRoom.topRight.x, rRoom.topLeft.x);
        int y1 = RandomUtils.uniform(RANDOM, lRoom.bottomRight.y + 1, lRoom.topRight.y);
        int y2 = RandomUtils.uniform(RANDOM, rRoom.bottomLeft.y + 1, rRoom.topLeft.y);
        Position start = new Position(lRoom.bottomRight.x, y1);
        Position p1 = new Position(x, y1);
        Position p2 = new Position(x, y2);
        Position end = new Position(rRoom.bottomLeft.x, y2);
        hHallway1(finalWorldFrame, start, p1);
        if (p1.y <= p2.y) {
            v1Hallway(finalWorldFrame, p2, p1);
        } else {
            v1Hallway(finalWorldFrame, p1, p2);
        }
        hHallway2(finalWorldFrame, p2, end);

    }

    private void lConnect(TETile[][] finalWorldFrame, Room tRoom, Room bRoom) {

        int x = RandomUtils.uniform(RANDOM, Math.max(bRoom.topLeft.x, tRoom.topLeft.x),
                Math.min(tRoom.topRight.x, bRoom.topRight.x) + 1);
        Position p1 = new Position(x, tRoom.bottomRight.y + 1);
        Position p2 = new Position(x, bRoom.topLeft.y - 1);
        v2Hallway(finalWorldFrame, p1, p2);

    }

    private void v1Hallway(TETile[][] finalWorldFrame, Position p1, Position p2) {
        for (int j = p1.y + 1; j >= p2.y - 1; j--) {
            if (!(finalWorldFrame[p1.x - 1][j] == Tileset.FLOOR)) {
                finalWorldFrame[p1.x - 1][j] = Tileset.WALL;
            }

            if (j < p1.y + 1 && j > p2.y - 1) {
                finalWorldFrame[p1.x][j] = Tileset.FLOOR;
            }

            if (!(finalWorldFrame[p1.x + 1][j] == Tileset.FLOOR)) {
                finalWorldFrame[p1.x + 1][j] = Tileset.WALL;
            }
        }
    }

    private void v2Hallway(TETile[][] finalWorldFrame, Position p1, Position p2) {
        for (int j = p1.y + 1; j >= p2.y - 1; j--) {
            if (!(finalWorldFrame[p1.x - 1][j] == Tileset.FLOOR)) {
                finalWorldFrame[p1.x - 1][j] = Tileset.WALL;
            }

            finalWorldFrame[p1.x][j] = Tileset.FLOOR;

            if (!(finalWorldFrame[p1.x + 1][j] == Tileset.FLOOR)) {
                finalWorldFrame[p1.x + 1][j] = Tileset.WALL;
            }
        }
    }

    private void hHallway1(TETile[][] finalWorldFrame, Position p1, Position p2) {
        for (int i = p1.x; i <= p2.x; i++) {
            if (!(finalWorldFrame[i][p1.y + 1] == Tileset.FLOOR)) {
                finalWorldFrame[i][p1.y + 1] = Tileset.WALL;
            }

            if (i < p2.x) {
                finalWorldFrame[i][p1.y] = Tileset.FLOOR;
            }

            if (!(finalWorldFrame[i][p1.y - 1] == Tileset.FLOOR)) {
                finalWorldFrame[i][p1.y - 1] = Tileset.WALL;
            }
        }
    }

    private void hHallway2(TETile[][] finalWorldFrame, Position p1, Position p2) {
        for (int i = p1.x; i <= p2.x; i++) {
            if (!(finalWorldFrame[i][p1.y + 1] == Tileset.FLOOR)) {
                finalWorldFrame[i][p1.y + 1] = Tileset.WALL;
            }

            if (i > p1.x) {
                finalWorldFrame[i][p1.y] = Tileset.FLOOR;
            }

            if (!(finalWorldFrame[i][p1.y - 1] == Tileset.FLOOR)) {
                finalWorldFrame[i][p1.y - 1] = Tileset.WALL;
            }
        }
    }

//    private void startInterface() {
//        int width = 40;
//        int height = 40;
//        StdDraw.setCanvasSize(width * 16, height * 16);
//        Font font = new Font("Monaco", Font.BOLD, 40);
//        StdDraw.setFont(font);
//        StdDraw.setXscale(0, width);
//        StdDraw.setYscale(0, height);
//        StdDraw.clear(Color.BLACK);
//        StdDraw.setPenColor(Color.WHITE);
//        StdDraw.enableDoubleBuffering();
//
//        StdDraw.text((double) width / 2, (double) height * 3 / 4, "CS61B: THE GAME");
//        Font smallFont = new Font("Monaco", Font.BOLD, 20);
//        StdDraw.setFont(smallFont);
//        StdDraw.text((double) width / 2, (double) height / 2 + 2, "New Game (N)");
//        StdDraw.text((double) width / 2, (double) height / 2, "Load Game (L)");
//        StdDraw.text((double) width / 2, (double) height / 2 - 2, "Quit (Q)");
//
//
//        StdDraw.show();
//    }

    private String toLower(String input) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < input.length(); i++) {
            char ch = input.charAt(i);
            if (Character.isUpperCase(ch)) {
                sb.append(Character.toLowerCase(ch));
            } else {
                sb.append(ch);
            }
        }
        return sb.toString();
    }

}
