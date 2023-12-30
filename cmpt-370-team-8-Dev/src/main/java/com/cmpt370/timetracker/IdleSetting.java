package com.cmpt370.timetracker;

import javafx.beans.value.ObservableValue;
import org.controlsfx.control.PropertySheet;
import java.util.Optional;

public class IdleSetting implements PropertySheet.Item {
    private IdleDetector idleDetectorRef;

    public IdleSetting(IdleDetector ref) {
        this.idleDetectorRef = ref;
    }

    @Override
    public Class<?> getType() {
        return Double.class;
    }

    @Override
    public String getCategory() {
        return "Idle Detection";
    }

    @Override
    public String getName() {
        return "Inactivity Delay";
    }

    @Override
    public String getDescription() {
        return "Determine the length in seconds that the user should be away from their device " +
                "before the user is set to idle";
    }

    @Override
    public Object getValue() {
        return idleDetectorRef.getIdleDelay();
    }

    @Override
    public void setValue(Object o) {
        idleDetectorRef.setIdleDelay((Double) o);
    }

    @Override
    public Optional<ObservableValue<? extends Object>> getObservableValue() {
        return Optional.empty();
    }
}
