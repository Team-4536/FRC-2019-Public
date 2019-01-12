package org.minutebots.robot.subsystems;

public class Drivetrain {
    private Drivetrain(){}

    private static Drivetrain drivetrain = new Drivetrain();

    public static Drivetrain getInstance(){
        return drivetrain;
    }
}
