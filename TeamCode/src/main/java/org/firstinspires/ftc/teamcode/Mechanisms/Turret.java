package org.firstinspires.ftc.teamcode.Mechanisms;

import android.annotation.SuppressLint;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;

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


    // Replace these fields
    private static final double k_p = 0.01;
    private static final double k_i = 0.01;
    private static final double k_d = 0.0004;
    private static final double DEADBAND_TICKS = 1.95; // 0.3 * 6.5

    private double i = 0, p = 0, d = 0;
    private double previous_error = 0;
    private double previous_time = 0;
    private static final double MAX_INTEGRAL = 0.3;

    public Turret(HardwareMap hm, Telemetry telemetry) {
        this.telemetry = telemetry;
        turretMotor = hm.get(DcMotorEx.class, "rotary");

        turretMotor.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);

        // runs without the internal PID -> important when zeroing
        turretMotor.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);
        turretMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    public void update(boolean button, boolean resetButton) {

        if (resetButton && !resetPrevButton) {
            state = ZeroingState.ZEROING;
            turretMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        }
        resetPrevButton = resetButton;

        if (state == ZeroingState.ZEROING) {
            zeroing();
            return;
        }

        if (button && !prevButton) {
            index = (index + 1) % positions.length;
            p = 0; i = 0; d = 0;
            previous_error = 0;
        }
        prevButton = button;

        runPID(positions[index]);

        telemetry.addData("[Turret] index", index);
        telemetry.addData("[Turret] target", positions[index]);
        telemetry.addData("[Turret] pos", turretMotor.getCurrentPosition());
    }


    private void zeroing() {
        turretMotor.setPower(0.3);

        if (turretMotor.getCurrent(CurrentUnit.AMPS) >= STALL_POWER) {
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
                previous_time = System.currentTimeMillis() / 1000.0;

                turretMotor.setPower(0.8);

                state = ZeroingState.IDLE;
            }
        } else {
            timerFlag = false;
        }

        telemetry.addData("[Turret] ZEROING", "");
        telemetry.addData("[Turret] current ", String.format("%.2f", turretMotor.getCurrent(CurrentUnit.AMPS)));
    }


    private void runPID(int desire_position) {

        int current_position = turretMotor.getCurrentPosition();
        double current_time = System.currentTimeMillis() / 1000.0; // in seconds
        double current_error = desire_position - current_position;

        // Deadband — close enough, stop completely
        if (Math.abs(current_error) < DEADBAND_TICKS) {
            turretMotor.setPower(0);
            i = 0;
            previous_error = current_error;
            telemetry.addData("[Turret]", "at desired position");
            telemetry.addData("[Turret] error (deg)", String.format("%.2f", current_error / TICKS_PER_DEGREE));
            return;
        }

        p = k_p * current_error;

        i += k_i * (current_error * (current_time - previous_time));

        if (i > MAX_INTEGRAL)
            i = MAX_INTEGRAL;
        else if (i < -MAX_INTEGRAL)
            i = -MAX_INTEGRAL;

        d = k_d * (current_error - previous_error) / (current_time - previous_time);

        double power = p + i + d;
        power = Range.clip(power, -1.0, 1.0);

        turretMotor.setPower(power);

        previous_error = current_error;
        previous_time = current_time;

        telemetry.addData("[Turret] error (deg)", String.format("%.2f", current_error / TICKS_PER_DEGREE));
        telemetry.addData("[Turret] power", String.format("%.3f", power));
    }

}
