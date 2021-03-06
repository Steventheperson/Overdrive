package rip.artemis.overdrive;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.InvocationTargetException;

public class Overdrive extends JavaPlugin {
    private NMSHandler handler;

    @Override
    public void onEnable() {
        if (!getServer().getBukkitVersion().contains("1.16.3")) {
            getServer().getConsoleSender().sendMessage(ChatColor.RED + "[!] Only supports 1.16.3, disabled");
            setEnabled(false);
        }
        handler = getNewNMSHandler();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1) {
            double tickrate;
            try {
                tickrate = Double.parseDouble(args[0]);
            } catch (Exception exception) {
                sender.sendMessage(ChatColor.RED + "Please provide a double greater than 0!");
                return false;
            }
            if (tickrate <= 0) {
                sender.sendMessage(ChatColor.RED + "The tickrate must be greater than 0!");
                return false;
            }

            long tickSpeed = (long) (1000 / tickrate);
            handler.setTickDuration(tickSpeed);
            sender.sendMessage(ChatColor.WHITE + "Tickrate set to " + ChatColor.YELLOW + (tickSpeed == 0 ? "∞" : tickrate) + ChatColor.WHITE + " tick(s) per second");
        } else {
            long duration = handler.getTickDuration();
            sender.sendMessage(ChatColor.WHITE + "Current tickrate is " + ChatColor.YELLOW + (duration == 0 ? "∞" : (((int) (1_000_000.0 / duration)) / 1000.0)) + ChatColor.WHITE + "(" + ChatColor.YELLOW + duration + "ms" + ChatColor.WHITE + ") tick(s) per second.");
        }
        return true;
    }

    public NMSHandler getNewNMSHandler() {
        try {
            Class<?> clazz = Class.forName("rip.artemis.overdrive.Handler");
            return (NMSHandler) clazz.getDeclaredConstructor().newInstance();
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
            return null;
        }
    }
}
