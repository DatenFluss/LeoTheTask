package com.task.controller;

import com.task.model.Point2D;
import com.task.model.Shape2D;

import java.util.*;

public class ShapeFixer {

    public boolean isValid(Shape2D shape) {
        // A valid shape must have at least 3 points plus the closing point
        if (shape.points.size() < 4) {
            return false;
        }
        // First and last points must be the same
        if (!shape.points.get(0).equals(shape.points.get(shape.points.size() - 1))) {
            return false;
        }

        Set<Point2D> uniquePoints = new HashSet<>(shape.points);
        // All points except the last should be unique
        if (uniquePoints.size() != shape.points.size() - 1) {
            return false;
        }

        // Check for self-intersections
        for (int i = 0; i < shape.points.size() - 1; i++) {
            for (int j = i + 2; j < shape.points.size() - 1; j++) {
                if (intersect(shape.points.get(i), shape.points.get(i + 1), shape.points.get(j), shape.points.get(j + 1))) {
                    return false;
                }
            }
        }

        return true;
    }

    public Shape2D repair(Shape2D shape) {
        Set<Point2D> uniquePoints = new LinkedHashSet<>(shape.points);

        List<Point2D> repairedPoints = new ArrayList<>(uniquePoints);

        // Ensure the shape is closed
        if (!repairedPoints.get(0).equals(repairedPoints.get(repairedPoints.size() - 1))) {
            repairedPoints.add(repairedPoints.get(0));
        }

        // Remove self-intersections
        while (hasSelfIntersections(repairedPoints)) {
            removeSelfIntersection(repairedPoints);
        }

        return new Shape2D(repairedPoints);
    }

    private boolean intersect(Point2D p1, Point2D p2, Point2D p3, Point2D p4) {
        double d = (p2.x - p1.x) * (p4.y - p3.y) - (p2.y - p1.y) * (p4.x - p3.x);
        if (d == 0) return false;

        double t = ((p3.x - p1.x) * (p4.y - p3.y) - (p3.y - p1.y) * (p4.x - p3.x)) / d;
        double u = -((p2.x - p1.x) * (p3.y - p1.y) - (p2.y - p1.y) * (p3.x - p1.x)) / d;

        return t > 0 && t < 1 && u > 0 && u < 1;
    }

    private boolean hasSelfIntersections(List<Point2D> points) {
        for (int i = 0; i < points.size() - 1; i++) {
            for (int j = i + 2; j < points.size() - 1; j++) {
                if (intersect(points.get(i), points.get(i + 1), points.get(j), points.get(j + 1))) {
                    return true;
                }
            }
        }
        return false;
    }

    private void removeSelfIntersection(List<Point2D> points) {
        for (int i = 0; i < points.size() - 1; i++) {
            for (int j = i + 2; j < points.size() - 1; j++) {
                if (intersect(points.get(i), points.get(i + 1), points.get(j), points.get(j + 1))) {
                    // Remove the loop created by the intersection
                    points.subList(i + 1, j + 1).clear();
                    return;
                }
            }
        }
    }
}
