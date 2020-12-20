package com.example.kpureposition.myUtil;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * Point <p>
 * Point position in RSSI field
 * </p>
 */
public class Point {

    public float x, y;

    /**
     * 内部是一次扫描的结果
     * 外部是多次的
     * wifiList[1][2]第二次扫描的第三个AP的信息
     */
    public ArrayList<ArrayList<WifiSource>> wifiLists = new ArrayList<>();

    public Point() {
        x = 0.0f;
        y = 0.0f;
    }

    public Point(float X, float Y) {
        x = X;
        y = Y;
    }

    public void ClearLists(){
        wifiLists.clear();
    }

    /**
     * Sort by rssi level
     */
    public void SortByLevel() {
        for (ArrayList<WifiSource> item :
                wifiLists) {
            if(item.size()>0) {
                item.sort(new Comparator<WifiSource>() {
                    @Override
                    public int compare(WifiSource o1, WifiSource o2) {
                        return Float.compare(o1.level, o2.level);
                    }
                });
            }
        }
    }

    static public float Distance(Point a, Point b) {
        return (float) Math.sqrt((a.x - b.x) * (a.x - b.x) + (a.y - b.y) * (a.y - b.y));
    }

    static public float DistanceSq(Point a, Point b) {
        return (a.x - b.x) * (a.x - b.x) + (a.y - b.y) * (a.y - b.y);
    }

}
