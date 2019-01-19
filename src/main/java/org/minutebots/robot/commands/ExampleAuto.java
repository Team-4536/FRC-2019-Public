package org.minutebots.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;
import org.minutebots.robot.subsystems.Intake;

public class ExampleAuto extends CommandGroup {
    ExampleAuto(){
        addSequential(Intake.extend()); //This is valid code, as these methods generate and return commands
        addSequential(Intake.retract());
    }
}
