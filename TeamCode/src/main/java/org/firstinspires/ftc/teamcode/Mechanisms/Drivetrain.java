package org.firstinspires.ftc.teamcode.Mechanisms;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Drivetrain {
    private DcMotor frontLeftDrive, backLeftDrive, frontRightDrive, backRightDrive;
    private Telemetry telemetry;

    public enum State {
        ROBOT_CENTRIC,
        FIELD_CENTRIC
    }

    private State state = State.FIELD_CENTRIC;
    private boolean drivingMode = true; // true -> field centric, false-> robot centric
    private boolean prevDriveModeButton = false;

    public Drivetrain(HardwareMap hm, Telemetry telemetry) {
        this.telemetry = telemetry;

        frontLeftDrive = hm.get(DcMotor.class, "motorFL");
        backLeftDrive = hm.get(DcMotor.class, "motorBL");
        frontRightDrive = hm.get(DcMotor.class, "motorFR");
        backRightDrive = hm.get(DcMotor.class, "motorBR");

        frontLeftDrive.setDirection(DcMotor.Direction.REVERSE);
        backLeftDrive.setDirection(DcMotor.Direction.REVERSE);
        frontRightDrive.setDirection(DcMotor.Direction.FORWARD);
        backRightDrive.setDirection(DcMotor.Direction.FORWARD);
    }

    public void update(double forward, double strafe, double rotate, double heading, boolean driveModeButton, double booster) {

        if (driveModeButton && !prevDriveModeButton) {
            setState(state == State.FIELD_CENTRIC ? State.ROBOT_CENTRIC : State.FIELD_CENTRIC);
        }
        // prev buttons is needed so that one press of the button can equal one change in the state
        prevDriveModeButton = driveModeButton;

        // scaling the speed of the robot: default is 50%
        // the other 50% is scaled according to the pressure on R2
        double speed = 0.5 + (booster * 0.5);
        forward *= speed;
        strafe *= speed;
        rotate *= speed;

        if(drivingMode){
            driveFieldCentric(forward, strafe, rotate, heading);
        } else{
            driveRobotCentric(forward, strafe, rotate);
        }
        telemetry.addData("Drive Mode", state);
        telemetry.addData("Speed", speed);

    }

    public void driveRobotCentric(double y, double x, double rz) {

        double frontLeftPower  = y + x + rz;
        double frontRightPower = y - x - rz;
        double backLeftPower   = y - x + rz;
        double backRightPower  = y + x - rz;

        double max = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rz), 1);

        frontLeftPower  /= max;
        frontRightPower /= max;
        backLeftPower   /= max;
        backRightPower  /= max;

        frontLeftDrive.setPower(frontLeftPower);
        frontRightDrive.setPower(frontRightPower);
        backLeftDrive.setPower(backLeftPower);
        backRightDrive.setPower(backRightPower);

        telemetry.addData("Front left/Right", "%4.2f, %4.2f", frontLeftPower, frontRightPower);
        telemetry.addData("Back  left/Right", "%4.2f, %4.2f", backLeftPower, backRightPower);
    }


    public void driveFieldCentric(double y, double x, double rz, double heading) {

        double rX = x * Math.cos(-heading) - y * Math.sin(-heading);
        double rY = x * Math.sin(-heading) + y * Math.cos(-heading);


        double frontLeftPower  = rY + rX + rz;
        double frontRightPower = rY - rX - rz;
        double backLeftPower   = rY - rX + rz;
        double backRightPower  = rY + rX - rz;

        double max = Math.max(Math.abs(rY) + Math.abs(rX) + Math.abs(rz), 1);

        frontLeftPower  /= max;
        frontRightPower /= max;
        backLeftPower   /= max;
        backRightPower  /= max;

        frontLeftDrive.setPower(frontLeftPower);
        frontRightDrive.setPower(frontRightPower);
        backLeftDrive.setPower(backLeftPower);
        backRightDrive.setPower(backRightPower);

        telemetry.addData("Front left/Right", "%4.2f, %4.2f", frontLeftPower, frontRightPower);
        telemetry.addData("Back  left/Right", "%4.2f, %4.2f", backLeftPower, backRightPower);
    }

    public void setState(State state) {
        this.state = state;

        switch (state) {
            case FIELD_CENTRIC:
                drivingMode = true;
                break;
            case ROBOT_CENTRIC:
                drivingMode = false;
                break;
        }
    }

}
