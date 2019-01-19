package org.minutebots.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.minutebots.robot.subsystems.Intake;

public class HatchCommand extends Command {

    boolean hasPanel;

    public HatchCommand(boolean hasHatchState) {

        hasPanel = hasHatchState;

    }


    protected void initialize () {



    }


    protected void execute() {

        if(hasPanel) {

            Intake.intake.setIntakeStatus(true);
            Intake.intake.setConeStatus(true);

        }
        //TODO: Consideration should be made for when the cone actually needs to be extended and retracted, possible a separate command
        else {

            Intake.intake.setIntakeStatus(false);
            Intake.intake.setConeStatus(false);

        }

    }


    protected boolean isFinished() {

        return true;


    }

}
