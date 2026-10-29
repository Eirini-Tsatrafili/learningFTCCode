package org.firstinspires.ftc.teamcode.Tools;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.util.Range;

@Config
public class PIDF {

    public static double k_p = 0.008;
    public static double k_i = 0.003;
    public static double k_d = 0.0007;
    public static double k_f = 0.0;
    public static double MAX_INTEGRAL = 0.3;

    private double p = 0, i = 0, d = 0, f = 0;
    private double previous_error = 0;
    private double previous_time = 0;

    public double calculate(double desire_position, double current_position) {

        double current_time = System.currentTimeMillis() / 1000.0;
        double current_error = desire_position - current_position;


        p = k_p * current_error;

        i += k_i * (current_error * (current_time - previous_time));

        if (i > MAX_INTEGRAL)
            i = MAX_INTEGRAL;
        else if (i < -MAX_INTEGRAL)
            i = -MAX_INTEGRAL;

        d = k_d * (current_error - previous_error) / (current_time - previous_time);

        f = k_f * Math.signum(current_error);

        double power = p + i + d + f;
        power = Range.clip(power, -1.0, 1.0);

        previous_error = current_error;
        previous_time = current_time;

        return power;
    }

    public void reset() {
        p = 0; i = 0; d = 0; f = 0;
        previous_error = 0;
        previous_time = System.currentTimeMillis() / 1000.0;
    }
}