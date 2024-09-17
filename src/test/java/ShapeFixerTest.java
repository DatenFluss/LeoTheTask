import com.task.controller.ShapeFixer;
import com.task.model.Point2D;
import com.task.model.Shape2D;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Arrays;

public class ShapeFixerTest {

    private final ShapeFixer shapeFixer = new ShapeFixer();

    private Shape2D createShape(Point2D... points) {
        return new Shape2D(Arrays.asList(points));
    }

    @Test
    public void testValidSquareShape() {
        // Create a valid square
        Shape2D square = createShape(
                new Point2D(0, 0),
                new Point2D(1, 0),
                new Point2D(1, 1),
                new Point2D(0, 1),
                new Point2D(0, 0)
        );

        assertTrue(shapeFixer.isValid(square), "Square should be valid");
    }

    @Test
    public void testSegmentThroughVertexIntersection() {
        // Create invalid shape
        Shape2D square = createShape(
                new Point2D(0, 0), new Point2D(2, 0),
                new Point2D(4, 0), new Point2D(4, 2),
                new Point2D(2, 2), new Point2D(2, 1),
                new Point2D(2, 4), new Point2D(0, 4),
                new Point2D(0, 0)
        );

        assertFalse(shapeFixer.isValid(square), "Shape presented as an invalid convex polygon");
    }

    @Test
    public void testInvalidOpenShape() {
        // Create an invalid shape that is not closed
        Shape2D openShape = createShape(
                new Point2D(0, 0),
                new Point2D(1, 0),
                new Point2D(1, 1),
                new Point2D(0, 1)
        );

        assertFalse(shapeFixer.isValid(openShape), "Open shape should be invalid");
    }

    @Test
    public void testShapeWithSelfIntersection() {
        // Create a shape that intersects itself (a bow-tie shape)
        Shape2D selfIntersectingShape = createShape(
                new Point2D(0, 0),
                new Point2D(1, 1),
                new Point2D(0, 1),
                new Point2D(1, 0),
                new Point2D(0, 0)
        );

        assertFalse(shapeFixer.isValid(selfIntersectingShape), "Self-intersecting shape should be invalid");
    }

    @Test
    public void testRepairInvalidShape() {
        // Create an invalid shape (not closed)
        Shape2D invalidShape = createShape(
                new Point2D(0, 0),
                new Point2D(1, 0),
                new Point2D(1, 1),
                new Point2D(0, 1)
        );
        Shape2D repairedShape = shapeFixer.repair(invalidShape);

        assertTrue(shapeFixer.isValid(repairedShape), "Repaired shape should be valid");
        assertEquals(repairedShape.points.get(0), repairedShape.points.get(repairedShape.points.size() - 1), "Shape should be closed");
    }

    @Test
    public void testRepairShapeWithDuplicatePoints() {
        Shape2D invalidShape = createShape(
                new Point2D(0, 0),
                new Point2D(1, 0),
                new Point2D(1, 1),
                new Point2D(1, 1), // Duplicate point
                new Point2D(0, 1),
                new Point2D(0, 0)
        );
        Shape2D repairedShape = shapeFixer.repair(invalidShape);

        assertTrue(shapeFixer.isValid(repairedShape), "Repaired shape should be valid");
        assertEquals(5, repairedShape.points.size(), "Repaired shape should have no duplicate points");
    }
}
