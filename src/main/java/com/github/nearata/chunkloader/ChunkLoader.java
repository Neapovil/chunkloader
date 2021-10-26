package com.github.nearata.chunkloader;

import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.LiteralArgument;

public final class ChunkLoader extends JavaPlugin
{
    private static ChunkLoader instance;

    @Override
    public void onEnable()
    {
        instance = this;

        new CommandAPICommand("chunkloader")
                .withPermission("chunkloader.add")
                .withArguments(new LiteralArgument("add"))
                .executesPlayer((player, args) -> {
                    final Chunk chunk = player.getLocation().getChunk();
                    final int x = chunk.getX();
                    final int z = chunk.getZ();
                    final World world = player.getWorld();

                    if (world.isChunkForceLoaded(x, z))
                    {
                        CommandAPI.fail("This chunk is already force loaded.");
                    }

                    world.setChunkForceLoaded(x, z, true);
                    player.sendMessage("Chunk loader added.");
                })
                .register();

        new CommandAPICommand("chunkloader")
                .withPermission("chunkloader.remove")
                .withArguments(new LiteralArgument("remove"))
                .executesPlayer((player, args) -> {
                    final Chunk chunk = player.getLocation().getChunk();
                    final int x = chunk.getX();
                    final int z = chunk.getZ();
                    final World world = player.getWorld();

                    if (!world.isChunkForceLoaded(x, z))
                    {
                        CommandAPI.fail("This chunk is not force loaded.");
                    }

                    world.setChunkForceLoaded(x, z, false);

                    player.sendMessage("Chunk loader removed.");
                })
                .register();

        new CommandAPICommand("chunkloader")
                .withPermission("chunkloader.check")
                .withArguments(new LiteralArgument("check"))
                .executesPlayer((player, args) -> {
                    final Chunk chunk = player.getLocation().getChunk();
                    final int x = chunk.getX();
                    final int z = chunk.getZ();
                    final World world = player.getWorld();

                    if (!world.isChunkForceLoaded(x, z))
                    {
                        CommandAPI.fail("This chunk is not force loaded.");
                    }

                    player.sendMessage("This chunk is force loaded.");
                })
                .register();

        new CommandAPICommand("chunkloader")
                .withPermission("chunkloader.clear")
                .withArguments(new LiteralArgument("clear"))
                .executesPlayer((player, args) -> {
                    this.getServer().getWorlds().forEach(world -> {
                        world.getForceLoadedChunks().forEach(chunk -> {
                            chunk.setForceLoaded(false);
                        });
                    });

                    player.sendMessage("Removed ALL forced chunk loaders, if any.");
                })
                .register();
    }

    @Override
    public void onDisable()
    {
    }

    public static ChunkLoader getInstance()
    {
        return instance;
    }
}
