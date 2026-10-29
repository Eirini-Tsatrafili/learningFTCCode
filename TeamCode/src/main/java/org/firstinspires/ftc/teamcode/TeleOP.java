package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Mechanisms.AutoShooter;
import org.firstinspires.ftc.teamcode.Mechanisms.Beam;
import org.firstinspires.ftc.teamcode.Mechanisms.Drivetrain;
import org.firstinspires.ftc.teamcode.Mechanisms.Hood;
import org.firstinspires.ftc.teamcode.Mechanisms.Hood2;
import org.firstinspires.ftc.teamcode.Mechanisms.Intake;
import org.firstinspires.ftc.teamcode.Mechanisms.RobotIMU;
import org.firstinspires.ftc.teamcode.Mechanisms.Turret;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;

@TeleOp(name="Basic: Omni Linear OpMode", group="Linear OpMode")
public class TeleOP extends LinearOpMode {
    private Drivetrain drivetrain;
//    private Hood hood;
    private Hood2 hood2;
    private RobotIMU imu;
    private Intake intake;
    private Beam beam;
    private Turret turret;
    private AutoShooter autoShooter;

    @Override
    public void runOpMode() {

        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        drivetrain = new Drivetrain(hardwareMap, telemetry);
//        hood = new Hood(hardwareMap, telemetry);
        imu = new RobotIMU(hardwareMap, telemetry);
        beam = new Beam(hardwareMap, telemetry);
        intake = new Intake(hardwareMap, telemetry, beam);
        hood2 = new Hood2(hardwareMap, telemetry);
        turret = new Turret(hardwareMap, telemetry);
        autoShooter = new AutoShooter(hardwareMap, telemetry, intake, beam);

        waitForStart();

        while (opModeIsActive()) {

            if (gamepad1.options) {
                imu.resetYaw();
            }

            drivetrain.update(-gamepad1.left_stick_y, gamepad1.left_stick_x, gamepad1.right_stick_x,
                              imu.getHeading(), gamepad1.y, gamepad1.right_trigger);

//            hood.update(gamepad1.a);

            // i know this is problematic but i don't have a second gamepad :(
            hood2.update(gamepad1.right_stick_y);

            beam.update();
            intake.update(gamepad1.right_bumper);
            turret.update(gamepad1.a, gamepad1.b);

            autoShooter.update(gamepad1.left_bumper);

            telemetry.update();
        }
    }}
