package byog.Core;

//import byog.SaveDemo.World;
import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;
import edu.princeton.cs.introcs.StdDraw;


//import javax.imageio.plugins.tiff.ExifGPSTagSet;
import java.awt.*;
import java.io.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;


public class Game implements Serializable {
    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    private static int WIDTH = 80;
    private static int HEIGHT = 30;
    private static int extraHeight = 2;
    private static int interfaceWidth = 40;
    private static int interfaceHeight = 40;
    private static int wTextPosition = 1;
    private static int hTextPosition = HEIGHT + extraHeight - 1;
    private static TETile[][] finalWorldFrame;
    private static int xPlayerPosition;
    private static int yPlayerPosition;
    private static int xLockedDoorPosition;
    private static int yLockedDoorPosition;
    private long SEED;
    private Random RANDOM;
    private List<Room> roomList = new LinkedList<>();

    public static class State implements Serializable {
        public int WIDTH;
        public int HEIGHT;
        public int extraHeight;
        public int interfaceWidth;
        public int interfaceHeight;
        public int wTextPosition;
        public int hTextPosition;
        public TETile[][] finalWorldFrame;
        public int xPlayerPosition;
        public int yPlayerPosition;
        public int xLockedDoorPosition;
        public int yLockedDoorPosition;
        public long SEED;
        public Random RANDOM;
        public List<Room> roomList;
    }

    /**
     * Method used for playing a fresh game. The game should start from the main menu.
     */
    public void playWithKeyboard() {
        startInterface();
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                char choise = StdDraw.nextKeyTyped();
                choise = Character.toUpperCase(choise);
                switch (choise) {
                    case 'N':
                        newGame();
                        break;
                    case 'L':
                        loadGame();
                        break;
                    case 'Q':
                        quit();
                        break;
                    default:
                }
            }
        }
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

        finalWorldFrame = new TETile[WIDTH][HEIGHT + extraHeight];

        initialize(finalWorldFrame);

        StringBuilder seed = new StringBuilder();
        int i = 1;
        if (input.charAt(0) == 'N' || input.charAt(0) == 'n') {
            while (input.charAt(i) != 'S' && input.charAt(i) != 's') {
                seed.append(input.charAt(i));
                i++;
            }
            SEED = Long.parseLong(seed.toString());
            RANDOM = new Random(SEED);
            generateRooms(finalWorldFrame);
            connectRooms(finalWorldFrame);
            playerInitialize();
            lockedDoorInitialize();
            while (i < input.length() && input.charAt(i) != ':') {
                move(input.charAt(i));
                i++;
            }
            if (i == input.length()) {
//                ter.renderFrame(finalWorldFrame);
                return finalWorldFrame;
            }
            if (input.charAt(i) == ':' && (input.charAt(i + 1) == 'Q' || input.charAt(i + 1) == 'q')) {
                saveGameInputString();
            }
//            ter.renderFrame(finalWorldFrame);
            return finalWorldFrame;
        } else if (input.charAt(0) == 'L' || input.charAt(0) == 'l') {
            loadGameInputString();
            while (i < input.length() && input.charAt(i) != ':') {
                move(input.charAt(i));
                i++;
            }
            if (i == input.length()) {
//                ter.renderFrame(finalWorldFrame);
                return finalWorldFrame;
            }
            if (input.charAt(i) == ':' && input.charAt(i + 1) == 'Q' || input.charAt(i + 1) == 'q') {
                saveGameInputString();
            }
//            ter.renderFrame(finalWorldFrame);
            return finalWorldFrame;
        }


        //Position p1 = new Position(50, 25);
        //Position p2 = new Position(50, 20);
        //v2Hallway(finalWorldFrame, p1, p2);

        return finalWorldFrame;
    }

    private void initialize(TETile[][] finalWorldFrame) {
//        ter.initialize(WIDTH, HEIGHT);
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT + extraHeight; j++) {
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
        int x =  -1;
        if (Math.max(bRoom.topLeft.x, tRoom.topLeft.x) == 0) {
            x = RandomUtils.uniform(RANDOM, Math.max(bRoom.topLeft.x, tRoom.topLeft.x) + 1,
                    Math.min(tRoom.topRight.x, bRoom.topRight.x) + 1);
        } else if (Math.min(tRoom.topRight.x, bRoom.topRight.x)  == WIDTH - 1) {
            x = RandomUtils.uniform(RANDOM, Math.max(bRoom.topLeft.x, tRoom.topLeft.x),
                    Math.min(tRoom.topRight.x, bRoom.topRight.x));
        } else {
            x = RandomUtils.uniform(RANDOM, Math.max(bRoom.topLeft.x, tRoom.topLeft.x),
                    Math.min(tRoom.topRight.x, bRoom.topRight.x) + 1);
        }
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

    private void startInterface() {
        StdDraw.setCanvasSize(interfaceWidth * 16, interfaceHeight * 16);
        Font font = new Font("Monaco", Font.BOLD, 40);
        StdDraw.setFont(font);
        StdDraw.setXscale(0, interfaceWidth);
        StdDraw.setYscale(0, interfaceHeight);
        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.enableDoubleBuffering();

        StdDraw.text((double) interfaceWidth / 2, (double) interfaceHeight * 3 / 4, "CS61B: THE GAME");
        Font smallFont = new Font("Monaco", Font.BOLD, 20);
        StdDraw.setFont(smallFont);
        StdDraw.text((double) interfaceWidth / 2, (double) interfaceHeight / 2 + 2, "New Game (N)");
        StdDraw.text((double) interfaceWidth / 2, (double) interfaceHeight / 2, "Load Game (L)");
        StdDraw.text((double) interfaceWidth / 2, (double) interfaceHeight / 2 - 2, "Quit (Q)");


        StdDraw.show();
    }

    private void playerInitialize() {
        while (true) {
            xPlayerPosition = RandomUtils.uniform(RANDOM, WIDTH);
            yPlayerPosition = RandomUtils.uniform(RANDOM, HEIGHT);
            if (finalWorldFrame[xPlayerPosition][yPlayerPosition] == Tileset.FLOOR) {
                finalWorldFrame[xPlayerPosition][yPlayerPosition] = Tileset.PLAYER;
                break;
            }
        }
    }

    private void lockedDoorInitialize() {
        while (true) {
            xLockedDoorPosition = RandomUtils.uniform(RANDOM, WIDTH);
            yLockedDoorPosition = RandomUtils.uniform(RANDOM, HEIGHT);
            if (finalWorldFrame[xLockedDoorPosition][yLockedDoorPosition] == Tileset.WALL
                && xLockedDoorPosition > 0 && xLockedDoorPosition < WIDTH - 1
                && yLockedDoorPosition > 0 && yLockedDoorPosition < HEIGHT - 1
                && (finalWorldFrame[xLockedDoorPosition - 1][yLockedDoorPosition] == Tileset.FLOOR
                    || finalWorldFrame[xLockedDoorPosition + 1][yLockedDoorPosition] == Tileset.FLOOR
                    || finalWorldFrame[xLockedDoorPosition][yLockedDoorPosition - 1] == Tileset.FLOOR
                    || finalWorldFrame[xLockedDoorPosition][yLockedDoorPosition + 1] == Tileset.FLOOR)) {
                finalWorldFrame[xLockedDoorPosition][yLockedDoorPosition] = Tileset.LOCKED_DOOR;
                break;
            }
        }
    }

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

    private void newGame() {
        StdDraw.clear(Color.BLACK);
        StdDraw.show();
        StringBuffer buffer = new StringBuffer();
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                char ch = StdDraw.nextKeyTyped();
                ch = Character.toUpperCase(ch);
                if (ch == 'S') {
                    break;
                } else {
                    buffer.append(ch);
                    drawNum(buffer);
                }
            }
        }

        finalWorldFrame = new TETile[WIDTH][HEIGHT + extraHeight];
        initialize(finalWorldFrame);

        SEED = Long.parseLong(buffer.toString());
        RANDOM = new Random(SEED);

        ter.initialize(WIDTH, HEIGHT + extraHeight);

        generateRooms(finalWorldFrame);
        connectRooms(finalWorldFrame);
        playerInitialize();
        lockedDoorInitialize();

        ter.renderFrame(finalWorldFrame);

        while (true) {
            if (gameGoing()) {
                break;
            }
        }
    }

    private void drawNum(StringBuffer buffer) {
        StdDraw.clear(Color.BLACK);
        Font smallFont = new Font("Serif", Font.BOLD, 30);
        StdDraw.setFont(smallFont);
        StdDraw.text((double) interfaceHeight / 2, (double) interfaceHeight / 2, buffer.toString());
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.show();
    }

    private boolean gameGoing() {
        if (StdDraw.hasNextKeyTyped()) {
            char ch = StdDraw.nextKeyTyped();
            if (ch == ':') {
                saveGame();
            } else if (ch == 'w' || ch == 'W' || ch == 's' || ch == 'S'
                || ch == 'a' || ch == 'A' || ch == 'd' || ch == 'D') {
                move(ch);
                if (xPlayerPosition == xLockedDoorPosition && yPlayerPosition == yLockedDoorPosition) {
                    win();
                    return true;
                }
            }

        }
        ter.renderFrame(finalWorldFrame);
        Font font = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.textLeft(wTextPosition, hTextPosition,
                finalWorldFrame[(int) StdDraw.mouseX()][(int) StdDraw.mouseY()].description());
        StdDraw.show();
        return false;
    }

    private void saveGame() {
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                char ch = StdDraw.nextKeyTyped();
                if (ch == 'Q' || ch == 'q') {
                    saveGameInputString();
                }
            }
        }
    }

    private void saveGameInputString() {
        File f = new File("./state.txt");
        try {
            if (!f.exists()) {
                f.createNewFile();
            }
            FileOutputStream fs = new FileOutputStream(f);
            ObjectOutputStream os = new ObjectOutputStream(fs);
            State state = new State();
            state = saveState(state);
            os.writeObject(state);
            os.close();
        } catch (FileNotFoundException e) {
            System.out.println("file not found");
            System.exit(0);
        } catch (IOException e) {
            System.out.println(e);
            System.exit(0);
        }
    }

    private State saveState(State state) {
        state.WIDTH = WIDTH;
        state.HEIGHT = HEIGHT;
        state.extraHeight = extraHeight;
        state.interfaceWidth = interfaceWidth;
        state.interfaceHeight = interfaceHeight;
        state.wTextPosition = wTextPosition;
        state.hTextPosition = hTextPosition;
        state.finalWorldFrame = finalWorldFrame;
        state.xPlayerPosition = xPlayerPosition;
        state.yPlayerPosition = yPlayerPosition;
        state.xLockedDoorPosition = xLockedDoorPosition;
        state.yLockedDoorPosition = yLockedDoorPosition;
        state.SEED = SEED;
        state.RANDOM = RANDOM;
        state.roomList = roomList;
        return state;
    }

    private void move(char ch) {
        ch = Character.toUpperCase(ch);
        if (ch == 'W') {
            if ((yPlayerPosition + 1) < HEIGHT
                    && (Tileset.FLOOR.equals(finalWorldFrame[xPlayerPosition][yPlayerPosition + 1])
                    || Tileset.LOCKED_DOOR.equals(finalWorldFrame[xPlayerPosition][yPlayerPosition + 1]))) {
                finalWorldFrame[xPlayerPosition][yPlayerPosition] = Tileset.FLOOR;
                finalWorldFrame[xPlayerPosition][yPlayerPosition + 1] = Tileset.PLAYER;
                yPlayerPosition++;
            }
        } else if (ch == 'S') {
            if ((yPlayerPosition - 1) >= 0
                    && (Tileset.FLOOR.equals(finalWorldFrame[xPlayerPosition][yPlayerPosition - 1])
                    || Tileset.LOCKED_DOOR.equals(finalWorldFrame[xPlayerPosition][yPlayerPosition - 1]))) {
                finalWorldFrame[xPlayerPosition][yPlayerPosition] = Tileset.FLOOR;
                finalWorldFrame[xPlayerPosition][yPlayerPosition - 1] = Tileset.PLAYER;
                yPlayerPosition--;
            }
        } else if (ch == 'A') {
            if ((xPlayerPosition - 1) < WIDTH
                    && (Tileset.FLOOR.equals(finalWorldFrame[xPlayerPosition - 1][yPlayerPosition])
                    || Tileset.LOCKED_DOOR.equals(finalWorldFrame[xPlayerPosition - 1][yPlayerPosition]))) {
                finalWorldFrame[xPlayerPosition][yPlayerPosition] = Tileset.FLOOR;
                finalWorldFrame[xPlayerPosition - 1][yPlayerPosition] = Tileset.PLAYER;
                xPlayerPosition--;
            }
        } else if (ch == 'D') {
            if ((xPlayerPosition + 1) >= 0
                    && (Tileset.FLOOR.equals(finalWorldFrame[xPlayerPosition + 1][yPlayerPosition])
                            || Tileset.LOCKED_DOOR.equals(finalWorldFrame[xPlayerPosition + 1][yPlayerPosition]))) {
                finalWorldFrame[xPlayerPosition][yPlayerPosition] = Tileset.FLOOR;
                finalWorldFrame[xPlayerPosition + 1][yPlayerPosition] = Tileset.PLAYER;
                xPlayerPosition++;
            }
        }
    }

    private void win() {
        StdDraw.clear(Color.BLACK);
        Font font = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.text(WIDTH / 2, HEIGHT / 2, "Congratulations! You Win!");
        StdDraw.show();
    }

    private void loadGame() {
        File f = new File("./state.txt");
        if (f.exists()) {
            try {
                FileInputStream fs = new FileInputStream(f);
                ObjectInputStream os = new ObjectInputStream(fs);
                State state = new State();
                state = (State) os.readObject();
                loadState(state);
                os.close();
            } catch (FileNotFoundException e) {
                System.out.println("file not found");
                System.exit(0);
            } catch (IOException e) {
                System.out.println(e);
                System.exit(0);
            } catch (ClassNotFoundException e) {
                System.out.println("class not found");
                System.exit(0);
            }
            ter.initialize(WIDTH, HEIGHT + extraHeight);

            ter.renderFrame(finalWorldFrame);

            while (true) {
                if (gameGoing()) {
                    break;
                }
            }
        }
    }

    private void loadGameInputString() {
        File f = new File("./state.txt");
        if (f.exists()) {
            try {
                FileInputStream fs = new FileInputStream(f);
                ObjectInputStream os = new ObjectInputStream(fs);
                State state = new State();
                state = (State) os.readObject();
                loadState(state);
                os.close();
            } catch (FileNotFoundException e) {
                System.out.println("file not found");
                System.exit(0);
            } catch (IOException e) {
                System.out.println(e);
                System.exit(0);
            } catch (ClassNotFoundException e) {
                System.out.println("class not found");
                System.exit(0);
            }
        }
    }

    private void loadState(State state) {
        WIDTH = state.WIDTH;
        HEIGHT = state.HEIGHT;
        extraHeight = state.extraHeight;
        interfaceWidth = state.interfaceWidth;
        interfaceHeight = state.interfaceHeight;
        wTextPosition = state.wTextPosition;
        hTextPosition = state.hTextPosition;
        finalWorldFrame = state.finalWorldFrame;
        xPlayerPosition = state.xPlayerPosition;
        yPlayerPosition = state.yPlayerPosition;
        xLockedDoorPosition = state.xLockedDoorPosition;
        yLockedDoorPosition = state.yLockedDoorPosition;
        SEED = state.SEED;
        RANDOM = state.RANDOM;
        roomList = state.roomList;
    }

    private void quit() {
        System.exit(0);
    }

}
