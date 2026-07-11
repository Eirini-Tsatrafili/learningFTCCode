package org.firstinspires.ftc.teamcode.Mechanisms;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Turret {

    private DcMotor turretMotor;
    private Telemetry telemetry;

    private double TICKS_PER_DEGREE = 6.5;

    private int LEFT = (int) (-30 * TICKS_PER_DEGREE);
    private int CENTER = 0;
    private int RIGHT = (int) (30 * TICKS_PER_DEGREE);
    private int[] positions = { LEFT, CENTER, RIGHT };

    private int index = 1;
    private boolean prevButton = false;

    public Turret(HardwareMap hm, Telemetry telemetry) {
        this.telemetry = telemetry;
        turretMotor = hm.get(DcMotor.class, "rotary");

        turretMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        turretMotor.setTargetPosition(CENTER);
        turretMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        turretMotor.setPower(0.5);
    }

    public void update(boolean button) {
        if (button && !prevButton) {
            index = (index + 1) % positions.length;
            turretMotor.setTargetPosition(positions[index]);
        }
        prevButton = button;

        telemetry.addData("[Turret] index", index);
        telemetry.addData("[Turret] target", turretMotor.getTargetPosition());
        telemetry.addData("[Turret] pos", turretMotor.getCurrentPosition());
    }
}
