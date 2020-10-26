package io.github.bananapuncher714.overdrive;

import org.bukkit.Bukkit;
import io.github.bananapuncher714.overdrive.api.NMSHandler;

public final class ReflectionUtil {
    public static final String VERSION;

    static {
        VERSION = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
    }

    private ReflectionUtil() {
    }

    public static NMSHandler getNewNMSHandler() {
        try {
            Class<?> clazz = Class.forName("io.github.bananapuncher714.overdrive." + "Handler_" + VERSION);
            return (NMSHandler) clazz.newInstance();
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }
}
