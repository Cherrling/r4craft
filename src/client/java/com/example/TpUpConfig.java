package com.example;

public class TpUpConfig {
    private static double height = 0.3; // 默认高度
    
    public static double getHeight() {
        return height;
    }
    
    public static void setHeight(double newHeight) {
        height = Math.max(0.1, Math.min(10.0, newHeight)); // 限制在0.1到10.0之间
    }
    
    public static String getHeightString() {
        return String.format("%.2f", height);
    }
}
