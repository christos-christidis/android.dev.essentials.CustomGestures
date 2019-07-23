package com.devessentials.customgestures;

import android.annotation.SuppressLint;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.Prediction;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity
        implements GestureOverlayView.OnGesturePerformedListener, View.OnTouchListener {

    private GestureLibrary gestureLibrary;
    private boolean mGestureRecognized;

    // SOS: used to suppress bs warning about performClick
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ConstraintLayout layout = findViewById(R.id.layout);
        layout.setOnTouchListener(this);

        gestureSetup();
    }

    private void gestureSetup() {
        // SOS: I can obtain a file w the gestures I want by using any gesture builder app on App Store.
        gestureLibrary = GestureLibraries.fromRawResource(this, R.raw.gesture);
        if (!gestureLibrary.load()) {
            finish();
        }

        GestureOverlayView gestureOverlay = findViewById(R.id.gestureOverlay);
        gestureOverlay.addOnGesturePerformedListener(this);
    }

    @Override
    public void onGesturePerformed(GestureOverlayView overlay, Gesture gesture) {
        // SOS: predictions are ordered from best-matching to worst
        List<Prediction> predictions = gestureLibrary.recognize(gesture);
        if (predictions.size() > 0 && predictions.get(0).score > 1.0) {
            String action = predictions.get(0).name;
            Toast.makeText(this, action, Toast.LENGTH_SHORT).show();
            mGestureRecognized = true;
        }
    }

    // TODO: I want to get this to fire when the gestureOverlay can't identify a gesture!
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (!mGestureRecognized) {
            Toast.makeText(this, "Try drawing a triangle or circle",
                    Toast.LENGTH_SHORT).show();
        }
        mGestureRecognized = false;
        return true;
    }
}
