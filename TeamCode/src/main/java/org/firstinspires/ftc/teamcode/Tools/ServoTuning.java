package org.firstinspires.ftc.teamcode.Tools;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

@Config
@TeleOp(name = "Servo Tuning", group = "Tools")
public class ServoTuning extends LinearOpMode {

    public static double HOOD_POSITION = 0.5;
    public static double FINGER_POSITION = 0.5;
    public static double RAMP_POSITION = 0.5;

    private Servo hoodServo;
    private Servo fingerServo;
    private Servo rampServo;

    @Override
    public void runOpMode() {

        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        hoodServo = hardwareMap.get(Servo.class, "hoodServo");
        fingerServo = hardwareMap.get(Servo.class, "shooter_finger");
        rampServo = hardwareMap.get(Servo.class, "ramp");

        waitForStart();

        while (opModeIsActive()) {
            hoodServo.setPosition(HOOD_POSITION);
            fingerServo.setPosition(FINGER_POSITION);
            rampServo.setPosition(RAMP_POSITION);

            telemetry.addData("[Hood] pos", String.format("%.2f", HOOD_POSITION));
            telemetry.addData("[Finger] pos", String.format("%.2f", FINGER_POSITION));
            telemetry.addData("[Ramp] pos", String.format("%.2f", RAMP_POSITION));
            telemetry.update();
        }
    }
}