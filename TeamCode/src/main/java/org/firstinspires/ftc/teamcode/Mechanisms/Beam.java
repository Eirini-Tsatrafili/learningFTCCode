package org.firstinspires.ftc.teamcode.Mechanisms;

import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Beam {
    private DigitalChannel beam;
    private Telemetry telemetry;

    private int counter = 0;
    private boolean prevState = true;

    public Beam(HardwareMap hm, Telemetry telemetry) {
        this.telemetry = telemetry;
        beam = hm.get(DigitalChannel.class, "beam");
    }

    public void update() {

        boolean state = beam.getState();

        if (!state && prevState) {
            counter++;
        }
        prevState = state;

        // beamSensor.getState() : False if the beam is broken, True if it is not
        telemetry.addData("[Beam] State", beam.getState() ? "CONNECTED" : "BROKEN");
    }

    public int getBreakCount() {
        return counter;
    }

    public void resetCount() {
        counter = 0;
    }
}