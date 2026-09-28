package org.firstinspires.ftc.teamcode.Mechanisms;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;
import org.firstinspires.ftc.teamcode.Tools.PIDF;

public class Turret {

    private DcMotorEx turretMotor;
    private Telemetry telemetry;

    private double TICKS_PER_DEGREE = 6.5;

    private int CENTER_OFFSET = 156; // DEAD CENTER

    private int LEFT = (int) ((-30 - CENTER_OFFSET) * TICKS_PER_DEGREE);
    private int CENTER = (int) (-CENTER_OFFSET * TICKS_PER_DEGREE);
    private int RIGHT = (int) ((30 - CENTER_OFFSET) * TICKS_PER_DEGREE);
    private int[] positions = { LEFT, CENTER, RIGHT };

    public enum ZeroingState { ZEROING, IDLE }
    public ZeroingState state = ZeroingState.ZEROING;

    private int STALL_TIME = 200;
    private int STALL_POWER = 2;
    private long timeStart = 0;
    private boolean timerFlag = false;


    private int index = 1;
    private boolean prevButton = false;
    private boolean resetPrevButton = false;
    private final PIDF pidf;

    public Turret(HardwareMap hm, Telemetry telemetry) {
        this.telemetry = telemetry;

        turretMotor = hm.get(DcMotorEx.class, "rotary");
        turretMotor.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);

        // runs without the internal PID -> important when zeroing
        turretMotor.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);
        turretMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        pidf = new PIDF();
    }

    public void update(boolean button, boolean resetButton) {

        if (resetButton && !resetPrevButton) {
            state = ZeroingState.ZEROING;
            turretMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            pidf.reset();
        }
        resetPrevButton = resetButton;

        if (state == ZeroingState.ZEROING) {
            zeroing();
            return;
        }

        if (button && !prevButton) {
            index = (index + 1) % positions.length;
            pidf.reset();
        }
        prevButton = button;

        runPID(positions[index]);

        telemetry.addData("[Turret] index", index);
        telemetry.addData("[Turret] target", positions[index]);
        telemetry.addData("[Turret] pos", turretMotor.getCurrentPosition());
    }


    private void zeroing() {
        turretMotor.setPower(0.3);

        if (turretMotor.getCurrent(CurrentUnit.AMPS) >= STALL_POWER && state != ZeroingState.IDLE) {
            if (!timerFlag) {

                timerFlag = true;
                timeStart = System.currentTimeMillis();

            } else if (System.currentTimeMillis() - timeStart >= STALL_TIME) {

                turretMotor.setPower(0);

                // reset the reference point
                turretMotor.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);

                // set the target
                turretMotor.setTargetPosition(CENTER);

                // set mode back to run to pos
                turretMotor.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);
                turretMotor.setPower(0.8);

                state = ZeroingState.IDLE;
                pidf.reset();
            }
        } else {
            timerFlag = false;
        }
    }


    private void runPID(int desire_position) {

        int current_position = turretMotor.getCurrentPosition();
        double current_error = desire_position - current_position;

        double power = pidf.calculate(desire_position, current_position);
        turretMotor.setPower(power);

        telemetry.addData("[Turret] error (deg)", String.format("%.2f", current_error / TICKS_PER_DEGREE));
        telemetry.addData("[Turret] power", String.format("%.3f", power));
    }

}
