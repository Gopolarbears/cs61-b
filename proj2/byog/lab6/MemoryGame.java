package byog.lab6;

import byog.Core.RandomUtils;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.Color;
import java.awt.Font;
import java.util.Random;

public class MemoryGame {
    private int width;
    private int height;
    private int round;
    private Random rand;
    private boolean gameOver;
    private boolean playerTurn;
    private static final char[] CHARACTERS = "abcdefghijklmnopqrstuvwxyz".toCharArray();
    private static final String[] ENCOURAGEMENT = {"You can do this!", "I believe in you!",
                                                   "You got this!", "You're a star!", "Go Bears!",
                                                   "Too easy for you!", "Wow, so impressive!"};

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Please enter a seed");
            return;
        }

        int seed = Integer.parseInt(args[0]);
        MemoryGame game = new MemoryGame(40, 40, seed);
        game.startGame();
    }

    public MemoryGame(int width, int height, int seed) {
        /* Sets up StdDraw so that it has a width by height grid of 16 by 16 squares as its canvas
         * Also sets up the scale so the top left is (0,0) and the bottom right is (width, height)
         */
        this.width = width;
        this.height = height;
        StdDraw.setCanvasSize(this.width * 16, this.height * 16);
        Font font = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.setXscale(0, this.width);
        StdDraw.setYscale(0, this.height);
        StdDraw.clear(Color.BLACK);
        StdDraw.enableDoubleBuffering();

        // Initialize random number generator
        rand = new Random(seed);
    }

    public String generateRandomString(int n) {
        // Generate random string of letters of length n
        StringBuilder buffer = new StringBuilder();
        for (int i = 0; i < n; i++) {
            int num = RandomUtils.uniform(rand, 0, 26);
            buffer.append(CHARACTERS[num]);
        }
        return buffer.toString();
    }

    public void drawFrame(String s) {
        // Take the string and display it in the center of the screen
        // If game is not over, display relevant game information at the top of the screen
        StdDraw.clear(Color.white);

        if (!gameOver) {
            Font smallFont = new Font("Serif", Font.BOLD, 20);
            StdDraw.setFont(smallFont);
            StdDraw.textLeft(1, height - 1, "Round:" + round);
            StdDraw.text((double)width / 2, height - 1, playerTurn ? "TYPE!" : "WATCH!");
            int num = RandomUtils.uniform(rand, 0, 7);
            StdDraw.textRight(width - 1, height - 1, ENCOURAGEMENT[num]);
            StdDraw.line(0, height - 2, width, height - 2);
        }

        Font font = new Font("Serif", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.text((double) width / 2, (double) height / 2, s);
        StdDraw.setPenColor(Color.BLACK);
        StdDraw.show();

    }

    public void flashSequence(String letters) {
        // Display each character in letters, making sure to blank the screen between letters
        char[] chars = letters.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            drawFrame(String.valueOf(chars[i]));
            StdDraw.pause(1000);
            drawFrame("");
            StdDraw.pause(500);
        }
    }

    public String solicitNCharsInput(int n) {
        // Read n letters of player input
        StringBuilder builder = new StringBuilder();
        while(builder.length() < n) {
            if (StdDraw.hasNextKeyTyped()) {
                builder.append(StdDraw.nextKeyTyped());
                drawFrame(builder.toString());
            }
        }
        StdDraw.pause(1000);
        return builder.toString();
    }

    public void startGame() {
        // Set any relevant variables before the game starts
        round = 0;
        gameOver = false;

        // Establish Game loop
        while (!gameOver) {
            round++;
            playerTurn = false;
            drawFrame("Round:" + round);
            String cur = generateRandomString(round);
            flashSequence(cur);
            playerTurn = true;
            String ans = solicitNCharsInput(round);
            if (!cur.equals(ans)) {
                drawFrame("Game Over! You made it to round:" + round);
                gameOver = true;
            }
        }
    }

}
