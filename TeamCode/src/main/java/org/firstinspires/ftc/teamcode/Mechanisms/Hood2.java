package org.firstinspires.ftc.teamcode.Mechanisms;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.ServoImplEx;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Hood2 {
    private ServoImplEx hoodServo;
    private Telemetry telemetry;

    private double MIN_HOOD = 0.2, MAX_HOOD = 0.8;

    private double pos = 0.0;

    public Hood2(HardwareMap hm, Telemetry telemetry) {
        this.telemetry = telemetry;
        hoodServo = hm.get(ServoImplEx.class, "hoodServo");
        hoodServo.setPosition(pos);
    }

    public void update(double joystick) {

        double speed = 0.05;

        pos += joystick * speed;
        pos = Range.clip(pos, 0, 1);

        setPos(pos);
        telemetry.addData("[Hood] State", "%.2f", pos);
    }

    public void setPos(double position) {
        hoodServo.setPosition(Range.scale(position, 0, 1, MIN_HOOD, MAX_HOOD));
    }
}
