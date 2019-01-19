package org.minutebots.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.minutebots.robot.subsystems.Intake;

public class HatchCommand extends Command {

    boolean hasPanel;

    public HatchCommand(boolean hatchPanel) {

        hasPanel = hatchPanel;

    }


    protected void initialize () {



    }


    protected void execute() {

        if(hasPanel) {

            Intake.intake.setIntakeStatus(true);
            Intake.intake.setConeStatus(true);

        }
        else {

            Intake.intake.setIntakeStatus(false);
            Intake.intake.setConeStatus(false);

        }

    }


    protected boolean isFinished() {

        return true;


    }

}
