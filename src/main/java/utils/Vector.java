package utils;

import java.io.Serializable;

public class Vector implements Serializable {

    public final float x;
    public final float y;

    public Vector(float x, float y) {
        this.x = x;
        this.y = y;
    }

    static public Vector fromAngle(float angle) {
        return new Vector((float) Math.cos(angle), (float) Math.sin(angle));
    }

    public Vector rotate(float theta) {
        float newX = x * (float) Math.cos(theta) - y * (float) Math.sin(theta);
        float newY = x * (float) Math.sin(theta) + y * (float) Math.cos(theta);
        return new Vector(newX, newY);
    }

    public Vector copy() {
        return new Vector(x, y);
    }

    public float[] get(float[] target) {
        if (target == null) {
            return new float[]{x, y};
        }
        if (target.length >= 2) {
            target[0] = x;
            target[1] = y;
        }
        return target;
    }

    public float mag() {
        return (float) Math.sqrt(x * x + y * y);
    }

    public float magSq() {
        return (x * x + y * y);
    }

    public Vector add(Vector v) {
        return new Vector(x + v.x, y + v.y);
    }

    public Vector add(float x, float y) {
        return new Vector(this.x + x, this.y + y);
    }

    static public Vector add(Vector v1, Vector v2) {
        return new Vector(v1.x + v2.x, v1.y + v2.y);
    }

    public float dist(Vector v) {
        float dx = x - v.x;
        float dy = y - v.y;
        return (float) Math.sqrt(dx * dx + dy * dy);
    }

    static public float dist(Vector v1, Vector v2) {
        float dx = v1.x - v2.x;
        float dy = v1.y - v2.y;
        return (float) Math.sqrt(dx * dx + dy * dy);
    }

    public float dot(Vector v) {
        return x * v.x + y * v.y;
    }

    public float dot(float x, float y, float z) {
        return this.x * x + this.y * y;
    }

    static public float dot(Vector v1, Vector v2) {
        return v1.x * v2.x + v1.y * v2.y;
    }

    public Vector normalize(Vector target) {
        float m = mag();
        if (m > 0) {
            return new Vector(x / m, y / m);
        } else {
            return new Vector(x, y);
        }
    }

    public Vector mult(float n) {
        return new Vector(x * n, y * n);
    }

    static public Vector mult(Vector v, float n) {
        return new Vector(v.x * n, v.y * n);
    }

    public Vector limit(float max) {
        if (magSq() > max * max) {
            return normalize().mult(max);
        }
        return new Vector(x, y);
    }

    public Vector normalize() {
        float m = mag();
        if (m != 0 && m != 1) {
            return div(m);
        }
        return new Vector(x, y);
    }

    public Vector div(float n) {
        return new Vector(x / n, y / n);
    }

    static public Vector div(Vector v, float n) {
        return new Vector(v.x / n, v.y / n);
    }

    public Vector setMag(float len) {
        return normalize().mult(len);
    }

    public float heading() {
        return (float) Math.atan2(y, x);
    }

    @Override
    public String toString() {
        return "[ " + x + ", " + y + " ]";
    }


    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Vector)) {
            return false;
        }
        final Vector p = (Vector) obj;
        return x == p.x && y == p.y;
    }


    @Override
    public int hashCode() {
        int result = 1;
        result = 31 * result + Float.floatToIntBits(x);
        result = 31 * result + Float.floatToIntBits(y);
        return result;
    }
}