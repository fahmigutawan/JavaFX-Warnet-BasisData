package com.example.javafxwarnetbasisdata.util;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.paint.Paint;

public class CustomScene extends Scene {
    public CustomScene(Parent parent) {
        super(parent, 1280,720);
    }

    public CustomScene(Parent parent, double v, double v1) {
        super(parent, v, v1);
    }

    public CustomScene(Parent parent, Paint paint) {
        super(parent, paint);
    }

    public CustomScene(Parent parent, double v, double v1, Paint paint) {
        super(parent, v, v1, paint);
    }

    public CustomScene(Parent parent, double v, double v1, boolean b) {
        super(parent, v, v1, b);
    }

    public CustomScene(Parent parent, double v, double v1, boolean b, SceneAntialiasing sceneAntialiasing) {
        super(parent, v, v1, b, sceneAntialiasing);
    }
}
