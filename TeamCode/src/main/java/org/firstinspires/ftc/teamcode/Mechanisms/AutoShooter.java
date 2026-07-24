package org.firstinspires.ftc.teamcode.Mechanisms;

import static java.lang.Math.abs;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class AutoShooter {

    public enum State {IDLE, INTAKE, SHOOTER_PREP, SHOOTING, RESET}

    public State state = State.IDLE;

    private DcMotorEx shooterMotor1;
    private DcMotorEx shooterMotor2;
    private Servo fingerServo;
    private Servo rampServo;

    private Intake intake;
    private Beam beam;
    private Telemetry telemetry;

    private double SHOOTER_POWER = 0.4;
    private double SHOOTER_TARGET_POWER = 800;
    private long SHOOT_MAX_TIME = 4000;

    private double FINGER_OPEN = 1.0;
    private double FINGER_CLOSED = 0.6;
    private double RAMP_UP = 1.0;
    private double RAMP_DOWN = 0.5;

    private long shootStartTime = 0;
    private boolean prevButton = false;

    public AutoShooter(HardwareMap hm, Telemetry telemetry, Intake intake, Beam beam) {
        this.telemetry = telemetry;
        this.intake = intake;
        this.beam = beam;

        shooterMotor1 = hm.get(DcMotorEx.class, "shooter_1");
        shooterMotor2 = hm.get(DcMotorEx.class, "shooter_2");
        fingerServo = hm.get(Servo.class, "shooter_finger");
        rampServo = hm.get(Servo.class, "ramp");

        shooterMotor2.setDirection(DcMotorEx.Direction.REVERSE);
    }

    public void update(boolean button) {

        if (button && !prevButton && state == State.IDLE) {
            setState(State.INTAKE);
        }
        prevButton = button;

        switch (state) {

            case IDLE:
                break;

            case INTAKE:
                if (beam.getDetectFlag()) {
                    setState(State.SHOOTER_PREP);
                }
                break;

            case SHOOTER_PREP:
                if (abs(shooterMotor1.getVelocity()) >= SHOOTER_TARGET_POWER) {
                    setState(State.SHOOTING);
                }
                break;

            case SHOOTING:
                if (System.currentTimeMillis() - shootStartTime >= SHOOT_MAX_TIME) {
                    setState(State.RESET);
                }
                break;

            case RESET:
                setState(State.IDLE);
                break;
        }

        telemetry.addData("[Shooter] state", state);
        telemetry.addData("[Shooter] velocity", String.format("%.2f", shooterMotor1.getVelocity()));
    }

    private void setState(State state) {
        this.state = state;

        switch (state) {

            case IDLE:
                break;

            case INTAKE:
                // intake starts with both bumpers
                // left -> all the automation process runs, starts with intake
                // right -> only the intake process happens
                intake.setState(Intake.State.RUNNING);
                break;

            case SHOOTER_PREP:
                intake.setState(Intake.State.STOPPED);
                shooterMotor1.setPower(SHOOTER_POWER);
                shooterMotor2.setPower(SHOOTER_POWER);
                break;

            case SHOOTING:
                intake.setState(Intake.State.RUNNING);
                fingerServo.setPosition(FINGER_OPEN);
                rampServo.setPosition(RAMP_UP);
                shootStartTime = System.currentTimeMillis();
                break;

            case RESET:
                shooterMotor1.setPower(0);
                shooterMotor2.setPower(0);
                intake.setState(Intake.State.STOPPED);
                fingerServo.setPosition(FINGER_CLOSED);
                rampServo.setPosition(RAMP_DOWN);
                beam.reset();
                setState(State.IDLE);
                break;
        }
    }
}