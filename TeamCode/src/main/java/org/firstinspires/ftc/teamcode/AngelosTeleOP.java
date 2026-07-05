package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Mechanisms.Drivetrain;
import org.firstinspires.ftc.teamcode.Mechanisms.Hood;

@TeleOp(name="Basic: Omni Linear OpMode", group="Linear OpMode")
public class AngelosTeleOP extends LinearOpMode {
    private Drivetrain drivetrain;
    private Hood hood;

    @Override
    public void runOpMode() {
        drivetrain = new Drivetrain(hardwareMap, telemetry);
        hood = new Hood(hardwareMap, telemetry);

        waitForStart();

        while (opModeIsActive()) {
            drivetrain.update(-gamepad1.left_stick_y, gamepad1.left_stick_x, gamepad1.right_stick_x);
            hood.update(gamepad1.a);
            telemetry.update();
        }
    }}
