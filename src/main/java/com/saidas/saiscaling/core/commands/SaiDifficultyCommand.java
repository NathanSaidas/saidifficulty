package com.saidas.saiscaling.core.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.saidas.saiscaling.SaiDifficultyMod;
import com.saidas.saiscaling.config.LootTableConfig;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.loot.LootTable;
import net.minecraft.util.text.StringTextComponent;

public class SaiDifficultyCommand extends BaseCommand {

    public SaiDifficultyCommand(String name, int permissionLevel, boolean enabled) {
        super(name, permissionLevel, enabled);
    }

    @Override
    public LiteralArgumentBuilder<CommandSource> setExecution() {
        return builder
                .then(Commands.literal("reload")
                        .then(Commands.literal("loottables")
                                .executes(source -> reloadLootTables(source.getSource()))
                        )
                );
    }


    public int reloadLootTables(CommandSource source) {
        if(!LootTableConfig.load()) {
            source.sendFailure(new StringTextComponent("Failed to load the LootTableConfig."));
        }
        else {
            source.sendSuccess(new StringTextComponent("Successfully loaded the LootTableConfig. New mobs spawned will have updated loot tables."), true);
        }
        return Command.SINGLE_SUCCESS;
    }
}
