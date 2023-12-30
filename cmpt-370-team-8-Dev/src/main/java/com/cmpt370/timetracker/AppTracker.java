package com.cmpt370.timetracker;

import javafx.util.Pair;

import java.io.Serializable;
import java.util.HashMap;

public class AppTracker implements Serializable {
    // Stored the executable name of each app used as well as
    // the number of seconds that app has been used for
    HashMap<String, Double> map = new HashMap<>();
    String currentlyFocusedApp = new String();
    // The number of seconds since the user switched apps
    Double timeSinceLastChange = 0.0;

    void Update(double deltaTime) {
        JNI.get().ifPresent(singleton -> {
            String app = singleton.GetFocusedAppExeName();
            if (app == null) {
                timeSinceLastChange = timeSinceLastChange + deltaTime;
                return;
            }

            currentlyFocusedApp = app;

            double timeForThisApp = map.getOrDefault(currentlyFocusedApp, 0.0);
            timeForThisApp += deltaTime;
            map.put(currentlyFocusedApp, timeForThisApp);
        });
    }

    public Pair<String, Double> GetTopAppTime() {
        // Have to do this to satisfy Java anonymous functions, so some reason...
        String[] topAppName = {""};
        final Double[] secondCount = {Double.valueOf(0)};
        map.forEach((name, count) -> {
            if (count > secondCount[0]) {
                secondCount[0] = count;
                topAppName[0] = name;
            }
        });

        return new Pair<>(topAppName[0], secondCount[0]);
    }

    @Override
    public String toString() {
        return map.toString();
    }
}
