package de.codelix.firsttimespawn;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerListener implements Listener {

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        if (FirstTimeSpawn.INSTANCE.isInsideSpawn(event.getEntity().getLocation())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getPlayer().isOp()) return;
        if (event.getClickedBlock() == null) return;
        if (FirstTimeSpawn.INSTANCE.isInsideSpawn(event.getClickedBlock().getLocation())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEntityEvent event) {
        if (event.getPlayer().isOp()) return;
        if (FirstTimeSpawn.INSTANCE.isInsideSpawn(event.getRightClicked().getLocation())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (event.getPlayer().isOp()) return;
        if (FirstTimeSpawn.INSTANCE.isInsideSpawn(event.getBlock().getLocation())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (event.getPlayer().isOp()) return;
        if (FirstTimeSpawn.INSTANCE.isInsideSpawn(event.getBlock().getLocation())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onExplode(BlockExplodeEvent event) {
        if (FirstTimeSpawn.INSTANCE.isInsideSpawn(event.getBlock().getLocation())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPistonExtend(BlockPistonExtendEvent event) {
        for (Block block : event.getBlocks()) {
            if (FirstTimeSpawn.INSTANCE.isInsideSpawn(block.getLocation())) {
                event.setCancelled(true);
                break;
            }
        }
    }

    @EventHandler
    public void on(BlockPistonRetractEvent event) {
        for (Block block : event.getBlocks()) {
            if (FirstTimeSpawn.INSTANCE.isInsideSpawn(block.getLocation())) {
                event.setCancelled(true);
                break;
            }
        }
    }

    @EventHandler
    public void onBlockIgnite(BlockIgniteEvent event) {
        if (FirstTimeSpawn.INSTANCE.isInsideSpawn(event.getBlock().getLocation())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void on(BlockPhysicsEvent event) {
        if (FirstTimeSpawn.INSTANCE.isInsideSpawn(event.getBlock().getLocation())) {
            event.setCancelled(true);
        }
    }



}
