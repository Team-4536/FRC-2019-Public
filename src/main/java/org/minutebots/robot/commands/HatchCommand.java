package org.minutebots.robot.commands;

import edu.wpi.first.wpilibj.command.InstantCommand;
import org.minutebots.robot.subsystems.Intake;

public class HatchCommand extends InstantCommand {

    private boolean hasPanel;

    public HatchCommand(boolean hasHatchState) {
        requires(Intake.getInstance());
        hasPanel = hasHatchState;
    }

    protected void execute() {
        if (hasPanel) {
            Intake.getInstance().setIntakeStatus(true);
            Intake.getInstance().setConeStatus(true);
        }
        //TODO: Consideration should be made for when the cone actually needs to be extended and retracted, possible a separate command
        else {
            Intake.getInstance().setIntakeStatus(false);
            Intake.getInstance().setConeStatus(false);
        }
    }
}
