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

    public State state = State.DOWN, prev_state = State.DOWN;
    private double MIN_HOOD = 0.2, MAX_HOOD = 0.8;

    public Hood(HardwareMap hm, Telemetry telemetry) {
        this.telemetry = telemetry;
        hoodServo = hm.get(ServoImplEx.class, "hoodServo");
    }

    public void update(boolean button) {
        telemetry.addData("[Hood] State: ", state);

        if (button) {
            setState(state == State.UP ? State.DOWN : State.UP);
        }
    }

    public void setState(State state) {
        if (state == prev_state) return;
        prev_state = this.state;
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
