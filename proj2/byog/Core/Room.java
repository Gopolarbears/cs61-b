package byog.Core;

/**
 * ClassName: Room
 * Description:
 *
 * @Author Seth Neiman
 * @Create 2024/3/26 21:16
 * @Version 1.0
 */
public class Room {
    Position topLeft;
    Position topRight;
    Position bottomLeft;
    Position bottomRight;
    int width;
    int height;

    public Room(Position p, int width, int height) {
        topLeft = p;
        this.width = width;
        this.height = height;
        topRight = new Position(topLeft.x + width, topLeft.y);
        bottomLeft = new Position(topLeft.x, topLeft.y - height);
        bottomRight = new Position(topLeft.x + width, topLeft.y - height);
    }

}
