package org.firstinspires.ftc.teamcode.Mechanisms;

import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Beam {
    private DigitalChannel beam;
    private Telemetry telemetry;

    private int counter = 0;
    private boolean artifact3Detected = false;
    private boolean prevState = true;

    private long BREAK_TIME = 200;
    private boolean timeFlag = false;
    private long timeStart = 0;

    public Beam(HardwareMap hm, Telemetry telemetry) {
        this.telemetry = telemetry;
        beam = hm.get(DigitalChannel.class, "beam");
    }

    public void update() {
        boolean state = beam.getState();

        // when the beam breaks
        if (!state && prevState) {
            timeFlag = true;
            timeStart = System.currentTimeMillis();
        }

        // when the beam connects again
        if (state && !prevState) {
            if (timeFlag) {
                if (System.currentTimeMillis() - timeStart >= BREAK_TIME) {
                    artifact3Detected = true;
                }
                timeFlag = false;
            }
        }

        prevState = state;

        // beamSensor.getState() : False if the beam is broken, True if it is not
        telemetry.addData("[Beam] State", beam.getState() ? "CONNECTED" : "BROKEN");
    }

    public boolean getDetectFlag(){ return artifact3Detected;}
}