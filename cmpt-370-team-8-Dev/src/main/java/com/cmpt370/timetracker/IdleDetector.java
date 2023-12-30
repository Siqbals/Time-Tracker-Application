package com.cmpt370.timetracker;

import javafx.scene.Parent;

public class IdleDetector {
    // Is the user currently in an idle state?
    private boolean isIdle = false;

    // How long in seconds has elapsed since the user has gone idle?
    private double idleTime = 0.0;

    // How long in seconds after the last input event before the user is marked as idle.
    private double idleDelay = 5.0;

    // Need a reference to the root node to fire events
    private Parent rootNode;

    /**
     * Constructs a new Idle Detector
     * @param rootNode We use the JavaFX event system to handling our custom events.
     *                 JavaFX requires a reference to a member of the active scene in order
     *                 to emit events, so we store a reference to the root node here.
     */
    IdleDetector(Parent rootNode) {
        this.rootNode = rootNode;
    }

    public boolean isIdle() {
        return isIdle;
    }

    /**
     * Call this periodically to check whether the user has become/stayed idle.
     *
     * This update function sends events to any interested listeners. To handle events from this
     * Update function, you must register a JavaFX event handler that handles events of type IdleEvent.
     */
    public void Update() {
        JNI.get().ifPresent(singleton -> {
            // How long since the user inputted their most recent input event?
            // Uses the native lib
            double time = singleton.CurrentIdleTime();

            // Bail early if the user disabled this feature
            if (this.idleDelay == 0.0) {
                return;
            }

            if (time < this.idleTime && isIdle) {
                // User just returned from idle
                this.isIdle = false;
                this.FireIdleEvent(IdleMessage.RETURNED_FROM_IDLE);
            } else if (time > this.idleDelay && !isIdle) {
                // User just became idle
                this.isIdle = true;
                this.FireIdleEvent(IdleMessage.BECAME_IDLE);
            }

            // Update the stored idle time
            this.idleTime = time;
        });
    }

    // Fires a single IdleEvent
    private void FireIdleEvent(IdleMessage message) {
        IdleEvent event = new IdleEvent(message, this.idleTime);
        this.rootNode.fireEvent(event);
    }

    public double getIdleDelay() {
        return idleDelay;
    }

    public void setIdleDelay(double idleDelay) {
        this.idleDelay = idleDelay;
    }
}
