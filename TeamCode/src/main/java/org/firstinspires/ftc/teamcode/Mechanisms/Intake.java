package org.firstinspires.ftc.teamcode.Mechanisms;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Intake {
    private DcMotor intakeMotor;
    private Telemetry telemetry;

    private Beam beam;

    public enum State {
        RUNNING,
        STOPPED
    }

    public State state = State.STOPPED;
    private boolean prevButton = false;

    public Intake(HardwareMap hm, Telemetry telemetry, Beam beam) {
        this.telemetry = telemetry;
        this.beam = beam;
        intakeMotor = hm.get(DcMotor.class, "intake");
    }

    public void update(boolean button) {
        if (button && !prevButton) {
            setState(state == State.STOPPED ? State.RUNNING : State.STOPPED);
        }
        prevButton = button;


        if (beam.getDetectFlag() && state == State.RUNNING) {
            setState(State.STOPPED);
        }

        telemetry.addData("[Intake] artifact3 Detected", beam.getDetectFlag());
        telemetry.addData("[Intake] State", state);
    }

    public void setState(State state) {
        this.state = state;

        switch (state) {
            case RUNNING:
                intakeMotor.setPower(1.0);
                break;
            case STOPPED:
                intakeMotor.setPower(0.0);
                break;
        }
    }
}