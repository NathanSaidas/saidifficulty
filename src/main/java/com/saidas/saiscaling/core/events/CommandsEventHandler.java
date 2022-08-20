package com.saidas.saiscaling.core.events;

import com.saidas.saiscaling.init.Command;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class CommandsEventHandler {
    public static void onRegisterCommand(final RegisterCommandsEvent event) {
        Command.init(event);
    }
}
