package org.firstinspires.ftc.teamcode.pedro;

import com.pedropathing.algorithm.Foresight;
import com.pedropathing.algorithm.ForesightConfig;
import com.pedropathing.controllers.Controller;
import com.pedropathing.follower.Follower;
import com.pedropathing.math.Matrix;
import com.pedropathing.math.Vector2D;
import com.pedropathing.revhub.drivetrains.Mecanum;
import com.pedropathing.revhub.drivetrains.MecanumConfig;
import com.pedropathing.revhub.localizers.PinpointConfig;
import com.pedropathing.revhub.localizers.PinpointLocalizer;
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class Constants {
    public static Follower create(HardwareMap h) {
        return new Follower(
                new PinpointLocalizer(h, localizerConfig),
                new Mecanum(h, drivetrainConfig),
                new Foresight(foresightConfig)
        );
    }

    public static MecanumConfig drivetrainConfig = new MecanumConfig(c -> {
        c.frontLeftName.set("motorFL");
        c.frontRightName.set("motorFR");
        c.backLeftName.set("motorBL");
        c.backRightName.set("motorBR");
        c.frontLeftDirection.set(DcMotorSimple.Direction.REVERSE);
        c.frontRightDirection.set(DcMotorSimple.Direction.FORWARD);
        c.backLeftDirection.set(DcMotorSimple.Direction.REVERSE);
        c.backRightDirection.set(DcMotorSimple.Direction.FORWARD);
    });

    public static PinpointConfig localizerConfig = new PinpointConfig(c -> {
        c.name.set("pinpoint");
        c.podType.set(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD);
        c.xPodOffset.set(-5.198131170798475);
        c.yPodOffset.set(-0.07643587945953129);
        c.xPodDirection.set(GoBildaPinpointDriver.EncoderDirection.REVERSED);
        c.yPodDirection.set(GoBildaPinpointDriver.EncoderDirection.REVERSED);
        c.globalDistanceUnit.set(DistanceUnit.INCH);
        c.offsetUnits.set(DistanceUnit.INCH);
    });

    public static ForesightConfig foresightConfig = new ForesightConfig(
            c -> {
                Controller primaryTranslationalForward = Controller.proportional(0.31676250892421737);
                Controller secondaryTranslationalForward = Controller.proportional(0.1170352829973752);
                Controller primaryTranslationalLateral = Controller.proportional(0.4774404027255061);
                Controller secondaryTranslationalLateral = Controller.proportional(0.17640147136455647);

                c.forwardTranslational.set(Controller.piecewise(secondaryTranslationalForward).put(2.5, primaryTranslationalForward));
                c.strafeTranslational.set(Controller.piecewise(secondaryTranslationalLateral).put(2.5, primaryTranslationalLateral));

                c.coast.set(Controller.proportionalFeedforward(0.011662621268733757));
                c.brake.set(Controller.proportionalFeedforward(0.009913228078423694));

                c.headingFeedback.set(Controller.proportional(5.03542981752208));
                c.headingBrakeCoefficients.set(Vector2D.cartesian(0.045114156272454416, 0.006931401443050861));

                c.linearBrakeCoefficients.set(Matrix.diag(0.11516194845947211, 0.038574047745477694));
                c.quadraticBrakeCoefficients.set(Matrix.diag(0.0014704465972406062, 0.0024820045216106808));

                c.maxAchievableForwardVelocity.set(84.1918203593809);
                c.maxAchievableStrafeVelocity.set(66.46959650760493);
                c.naturalForwardDeceleration.set(25.10496515872676);
                c.naturalStrafeDeceleration.set(59.16174768567209);
            }
    );
}