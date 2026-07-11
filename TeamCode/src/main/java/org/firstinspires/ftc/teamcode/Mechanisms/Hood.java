package org.firstinspires.ftc.teamcode.Mechanisms;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.ServoImplEx;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Hood {
    private ServoImplEx hoodServo;
    private Telemetry telemetry;

    public enum State {
        UP,
        DOWN
    }

    public State state = State.DOWN;
    private boolean prevButton = false;
    private double MIN_HOOD = 0.2, MAX_HOOD = 0.8;

    public Hood(HardwareMap hm, Telemetry telemetry) {
        this.telemetry = telemetry;
        hoodServo = hm.get(ServoImplEx.class, "hoodServo");
    }

    public void update(boolean button) {

        if (button && !prevButton) {
            setState(state == State.UP ? State.DOWN : State.UP);
        }
        prevButton = button;

        telemetry.addData("[Hood] State ", state);
    }

    public void setState(State state) {
        this.state = state;

        switch (state) {
            case UP:
                setPosition(0);
                break;
            case DOWN:
                setPosition(1);
                break;
        }
    }

    public void setPosition(double position) {
        hoodServo.setPosition(Range.scale(position, 0, 1, MIN_HOOD, MAX_HOOD));
    }
}
