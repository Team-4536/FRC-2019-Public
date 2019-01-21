package org.minutebots.lib;

import edu.wpi.first.wpilibj.DoubleSolenoid;

public class VirtualSolenoid extends DoubleSolenoid {

    private int fc = 0, rc = 0;

    /**
     * Constructor. Uses the default PCM ID (defaults to 0).
     *
     * @param forwardChannel The forward channel number on the PCM (0..7).
     * @param reverseChannel The reverse channel number on the PCM (0..7).
     */
    public VirtualSolenoid(int forwardChannel, int reverseChannel) {
        super(forwardChannel, reverseChannel);
        fc = forwardChannel;
        rc = reverseChannel;
    }

    /**
     * Set the value of a solenoid.
     *
     * @param value The value to set (Off, Forward, Reverse)
     */
    @Override
    public void set(Value value) {
        switch (value){
            case kForward:
                System.out.println("Solenoid " + fc + ", " + rc + " extended");
                break;
            case kOff:
                System.out.println("Solenoid " + fc + ", " + rc + " turned off.");
                break;
            case kReverse:
                System.out.println("Solenoid " + fc + ", " + rc + " retracted.");
                break;
        }
    }
}
