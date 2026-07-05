package org.firstinspires.ftc.teamcode.Mechanisms;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.util.function.DoubleSupplier;

public class Drivetrain {
    private DcMotor frontLeftDrive, backLeftDrive, frontRightDrive, backRightDrive;
    private Telemetry telemetry;

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

    public void update(double forward, double strafe, double rotate) {
        driveFieldCentric(forward, strafe, rotate, 0.0);
    }

    public void driveFieldCentric(double y, double x, double rx, double heading) {
        // TODO: Στριψε το διάνυσμα!!!!
        double frontLeftPower  = y + x + rx;
        double frontRightPower = y - x - rx;
        double backLeftPower   = y - x + rx;
        double backRightPower  = y + x - rx;

        double max = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);

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
}
