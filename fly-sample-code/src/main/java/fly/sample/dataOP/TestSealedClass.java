package fly.sample.dataOP;


import java.awt.Point;

public class TestSealedClass {
    public static void main(String[] args) {

    }

//    public static float area(Shape shape) {
//        float area = switch (shape) {
//            case Circle c -> Math.PI * c.radius() * c.radius();
//            case Rectangle r -> Math.abs((r.uppperRight.y() - r.lowerLeft().y())
//                    * (r.upperRight().x() - r.lowerLeft().x()));
//            case Shape.Circle c -> c.radius();
//            case Shape.Rectangle r -> r.uppperRight().x;
            // no default needed!
//        };
//        return 0f;
//    }
}

sealed interface Shape {
    record Circle(Point center, int radius) implements Shape {
    }

    record Rectangle(Point lowerLeft, Point uppperRight) implements Shape {

    }

}
