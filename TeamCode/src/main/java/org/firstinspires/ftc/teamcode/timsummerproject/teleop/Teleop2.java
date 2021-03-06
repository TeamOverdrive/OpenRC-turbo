package org.firstinspires.ftc.teamcode.timsummerproject.teleop;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.teamcode.timsummerproject.subsystems.DriveTrain;

@TeleOp(name = "Teleop",group = "TeleOp")
public class Teleop2 extends LinearOpMode {

    private DcMotor motorBackLeft, motorBackRight, motorFrontLeft, motorFrontRight;

    DriveTrain drive = new DriveTrain();

    public void runOpMode() throws InterruptedException {

        initMotors();

        BNO055IMU imu;
        Orientation angles;

        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.calibrationDataFile = "BNO055IMUCalibration.json";
        parameters.loggingEnabled = true;
        parameters.loggingTag = "IMU";

        imu = hardwareMap.get(BNO055IMU.class, "imu");
        imu.initialize(parameters);

        waitForStart();

        while (opModeIsActive()) {

            angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
            teleDrive(angles);
            if (gamepad1.left_bumper) {
                motorFrontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
                drive.FrontLeft = 0;
                drive.BackLeft = 0;
            } else {
                motorFrontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
            }
            if (gamepad1.right_bumper) {
                motorFrontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
                drive.FrontRight = 0;
                drive.BackRight = 0;
            } else {
                motorFrontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
            }
            if (gamepad1.right_bumper && gamepad1.left_bumper) {
                motorBackLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
                motorBackRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            } else {
                motorBackLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
                motorBackRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
            }

            drive.FrontLeft += gamepad1.left_trigger;
            drive.BackLeft += gamepad1.left_trigger;
            drive.FrontRight += gamepad1.right_trigger;
            drive.BackRight += gamepad1.right_trigger;

            update();

        }
    }
    public void initMotors() {

        motorBackLeft = hardwareMap.get(DcMotor.class, "left_back");
        motorBackRight = hardwareMap.get(DcMotor.class, "right_back");
        motorFrontLeft = hardwareMap.get(DcMotor.class, "left_front");
        motorFrontRight = hardwareMap.get(DcMotor.class, "right_front");

        motorBackRight.setDirection(DcMotor.Direction.REVERSE);
        motorFrontRight.setDirection(DcMotor.Direction.REVERSE);

    }
    public void teleDrive(Orientation angles) {
        double relativeAngle;
        relativeAngle = (Math.atan2(-gamepad1.left_stick_y, gamepad1.left_stick_x) - Math.PI / 4) - Math.toRadians(angles.firstAngle);
        if (Math.abs(relativeAngle) > Math.PI) {
            if (relativeAngle > 0)
                relativeAngle = -(Math.PI * 2 - Math.abs(relativeAngle));
            else if (relativeAngle > 0)
                relativeAngle = Math.PI * 2 - Math.abs(relativeAngle);
        }

        drive.move(relativeAngle, Math.sqrt(gamepad1.left_stick_x * gamepad1.left_stick_x +  gamepad1.left_stick_y * gamepad1.left_stick_y),
                gamepad1.right_stick_x);
    }
    public void update() {
        motorFrontLeft.setPower(drive.FrontLeft);
        motorFrontRight.setPower(drive.FrontRight);
        motorBackLeft.setPower(drive.BackLeft);
        motorBackRight.setPower(drive.BackRight);

    }
}

