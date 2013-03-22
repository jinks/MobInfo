package net.mended_drum.MobInfo;

import java.util.ArrayList;
import java.util.HashSet;
import net.minecraft.server.v1_5_R2.BiomeMeta;
import net.minecraft.server.v1_5_R2.EnumCreatureType;
import net.minecraft.server.v1_5_R2.IChunkProvider;
import net.minecraft.server.v1_5_R2.MathHelper;
import net.minecraft.server.v1_5_R2.WorldServer;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_5_R2.CraftWorld;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;

public final class MobInfo extends JavaPlugin implements Listener {

   public void onEnable() {
      this.getLogger().info("MobInfo Enabled!");
      this.getServer().getPluginManager().registerEvents(this, this);
   }

   public void onDisable() {
      this.getLogger().info("MobInfo Disabled!");
      PlayerInteractEvent.getHandlerList().unregister((JavaPlugin)this);
   }

   @EventHandler
   public void onPlayerInteractBlock(PlayerInteractEvent evt) {
      if(evt.getPlayer().getItemInHand().getTypeId() == Material.AIR.getId()) {
         ArrayList spawnList = new ArrayList();
         Location bloc = evt.getPlayer().getTargetBlock((HashSet)null, 200).getLocation();
         int x = MathHelper.floor(bloc.getX());
         int y = MathHelper.floor(bloc.getY());
         int z = MathHelper.floor(bloc.getZ());
         CraftWorld refWorld = (CraftWorld)evt.getPlayer().getWorld();
         WorldServer theWorld = refWorld.getHandle();
         IChunkProvider cp = theWorld.chunkProviderServer;
         spawnList.addAll(cp.getMobsFor(EnumCreatureType.MONSTER, x, y, z));
         if(!spawnList.equals((Object)null) && !spawnList.isEmpty()) {
            String datastring = "";

            for(int i = 0; i < spawnList.size(); ++i) {
               BiomeMeta entry = null;

               try {
                  entry = (BiomeMeta)spawnList.get(i);
                  String e = entry.b.getName();
                  datastring = datastring + e + "; ";
               } catch (SecurityException ex) {
                  this.getLogger().warning("Class resolution Error! " + entry);
                  ex.printStackTrace();
               }
            }

            evt.getPlayer().sendMessage("Mobs: " + datastring);
         }
      }

   }
}
