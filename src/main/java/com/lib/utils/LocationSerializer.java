package com.lib.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class LocationSerializer {

    public static String serialize(Location l) {
        return String.valueOf(l.getWorld().getName()) + ',' +
                l.getX() + ',' +
                l.getY() + ',' +
                l.getZ() + ',' +
                l.getYaw() + ',' +
                l.getPitch();
    }

    public static Location deserialize(String s) {
        String[] location = s.split(",");
        return new Location(
                Bukkit.getWorld(location[0]),
                Double.parseDouble(location[1]),
                Double.parseDouble(location[2]),
                Double.parseDouble(location[3]),
                Float.parseFloat(location[4]),
                Float.parseFloat(location[5]));
    }

    public static Location validate(Location l) {
        if (l.getWorld() == null) {
            l = new Location(Bukkit.getWorlds().get(0), l.getBlockX(), l.getBlockY(), l.getZ(), 1.0F, 1.0F);
            return l;
        }else{
            return l;
        }
    }
}
