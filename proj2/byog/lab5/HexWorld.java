package byog.lab5;
import org.junit.Test;
import static org.junit.Assert.*;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.util.Random;

/**
 * Draws a world consisting of hexagonal regions.
 */
public class HexWorld {
    private static final int WIDTH = 60;
    private static final int HEIGHT = 60;

    private static final long SEED = 2873123;
    private static final Random RANDOM = new Random(SEED);

    public static void initialize(TETile[][] world) {
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                world[x][y] = Tileset.NOTHING;
            }
        }
    }

    public static void addHexagon(TETile[][] world, Position p, int size, TETile t) {
        Position lowLeft = p;
        Position topLeft = new Position(p.x, p.y + 2 * size - 1);

        for (int i = 0; i < size; i++) {
            Position start = new Position(topLeft.x - i, topLeft.y - i);
            for (int j = 0; j < size + 2 * i; j++) {
                world[start.x + j][start.y] = t;
            }
        }

        for (int i = 0; i < size; i++) {
            Position start = new Position(lowLeft.x - i, lowLeft.y + i);
            for (int j = 0; j < size + 2 * i; j++) {
                world[start.x + j][start.y] = t;
            }
        }
    }

    public static void drawRandomVerticalHexes(TETile[][] world, Position p, int size) {
        for (int i = 0; i < 3; i++) {
            Position start = new Position(p.x + (2 * size - 1) * i, p.y - size * i);
            for (int j = 0; j < 3 + i; j++) {
                TETile t = randomTile();
                Position current = new Position(start.x, start.y + 2 * size * j);
                addHexagon(world, current, size, t);
            }
        }

        p = new Position(p.x + 4 * (2 * size - 1), p.y);
        for (int i = 0; i < 2; i++) {
            Position start = new Position(p.x - (2 * size - 1) * i, p.y - size * i);
            for (int j = 0; j < 3 + i; j++) {
                TETile t = randomTile();
                Position current = new Position(start.x, start.y + 2 * size * j);
                addHexagon(world, current, size, t);
            }
        }
    }

    private static TETile randomTile() {
        int tileNum = RANDOM.nextInt(7);
        switch (tileNum) {
            case 0: return Tileset.SAND;
            case 1: return Tileset.WALL;
            case 2: return Tileset.FLOWER;
            case 3: return Tileset.MOUNTAIN;
            case 4: return Tileset.GRASS;
            case 5: return Tileset.TREE;
            case 6: return Tileset.WATER;
            case 7: return Tileset.FLOOR;
            default: return Tileset.SAND;
        }
    }

    public static void main(String[] args) {
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);

        TETile[][] world = new TETile[WIDTH][HEIGHT];
        initialize(world);
        Position p = new Position(20, 20);
        drawRandomVerticalHexes(world, p, 3);

        ter.renderFrame(world);
    }
}
