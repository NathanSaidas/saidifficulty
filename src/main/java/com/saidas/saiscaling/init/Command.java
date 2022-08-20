package com.saidas.saiscaling.init;

import com.mojang.brigadier.CommandDispatcher;
import com.saidas.saiscaling.core.commands.BaseCommand;
import com.saidas.saiscaling.core.commands.SaiDifficultyCommand;
import net.minecraft.command.CommandSource;
import net.minecraftforge.event.RegisterCommandsEvent;

import java.util.ArrayList;

public class Command {
    private static final ArrayList<BaseCommand> commands = new ArrayList<BaseCommand>();

    public static void init(final RegisterCommandsEvent event) {
        CommandDispatcher<CommandSource> dispatcher = event.getDispatcher();

        commands.add(new SaiDifficultyCommand("saidifficulty", 2, true));

        commands.forEach((command) -> {
            if(command.isEnabled() && command.setExecution() != null) {
                dispatcher.register(command.getBuilder());
            }
        });
    }
}
