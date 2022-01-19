package me.diademiemi.whereis;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.permissions.Permission;

/**
 * where plugin
 *
 * @author diademiemi
 */
public class WhereIs extends JavaPlugin { 

    /**
     * Plugin instance
     */
    private static WhereIs plugin;

    /**
     * Plugin manager
     */
    private static PluginManager pm;


    /**
     * Run on startup, load files, create permissions and start
     */
    @Override
    public void onEnable() {
        plugin = this;

        pm = getServer().getPluginManager();

		pm.addPermission(new Permission("whereis.whereis"));

        getCommand("whereis").setExecutor(new CommandExec());
    }

    /**
     * Disable plugin
     */
    @Override
    public void onDisable() {
        plugin = null;
    }

    /**
     * Get plugin instance
     *
     * @return Plugin instance
     */
    public static WhereIs getInstance() {
        return plugin;
    }

}
