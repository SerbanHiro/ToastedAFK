package me.serbob.toastedafk.utils;

import org.bukkit.Location;

public class RegionUtils {
    /**public static boolean getPlayersInRegion(Location playerslocation, String checkingRegion, JavaPlugin plugin1) {
        com.sk89q.worldedit.util.Location loc = com.sk89q.worldedit.bukkit.BukkitAdapter.adapt(playerslocation);
        com.sk89q.worldguard.protection.regions.RegionContainer container = com.sk89q.worldguard.WorldGuard.getInstance().getPlatform().getRegionContainer();
        com.sk89q.worldguard.protection.regions.RegionQuery query = container.createQuery();
        com.sk89q.worldguard.protection.ApplicableRegionSet set = query.getApplicableRegions(loc);

        int players = 0;

        for(com.sk89q.worldguard.protection.regions.ProtectedRegion region : set.getRegions()) {
            if(region.getId().equalsIgnoreCase(checkingRegion)) {
                for(Player player : plugin1.getServer().getOnlinePlayers()) {
                    com.sk89q.worldedit.math.BlockVector3 min = region.getMinimumPoint();
                    com.sk89q.worldedit.math.BlockVector3 max = region.getMaximumPoint();


                    Location locMin = new Location(playerslocation.getWorld(), min.getX(), min.getY(), min.getZ());
                    Location locMax = new Location(playerslocation.getWorld(), max.getX(), max.getY(), max.getZ());

                    if(playerInCubiod(player.getLocation(), locMin, locMax)) {
                        players++;
                    }
                }

            }
        }
        return players>0;
    }*/
    public static boolean playerInCubiod(Location location, Location loc1, Location loc2) {
        if(loc1 == null || loc2 == null)
            return false;
        if(loc1.getWorld() != loc2.getWorld())
            return false;
        int x1 = Math.min(loc1.getBlockX(), loc2.getBlockX());
        int y1 = Math.min(loc1.getBlockY(), loc2.getBlockY());
        int z1 = Math.min(loc1.getBlockZ(), loc2.getBlockZ());
        int x2 = Math.max(loc1.getBlockX(), loc2.getBlockX());
        int y2 = Math.max(loc1.getBlockY(), loc2.getBlockY());
        int z2 = Math.max(loc1.getBlockZ(), loc2.getBlockZ());
        int px = location.getBlockX();
        int py = location.getBlockY();
        int pz = location.getBlockZ();
        if(loc1.getWorld() == location.getWorld()) {
            if(px >= x1 && px <= x2) {
                if(py >= y1 && py <= y2) {
                    if(pz >= z1 && pz <= z2) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
