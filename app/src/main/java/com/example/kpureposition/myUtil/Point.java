package com.example.kpureposition.myUtil;

/**
 * Point <p>
 * Point position in RSSI field
 * </p>
 */
public class Point {

    public float x, y;

    public Point(){
        x=0.0f;
        y=0.0f;
    }


    public Point(float X, float Y) {
        x = X;
        y = Y;
    }

    static public float Distance(Point a, Point b) {
        return (float) Math.sqrt((a.x - b.x) * (a.x - b.x) + (a.y - b.y) * (a.y - b.y));
    }

    static public float DistaneSq(Point a,Point b)
    {
        return (a.x - b.x) * (a.x - b.x) + (a.y - b.y) * (a.y - b.y);
    }

}
