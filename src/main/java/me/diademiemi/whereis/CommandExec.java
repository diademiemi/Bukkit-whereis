package me.diademiemi.whereis;

import java.lang.reflect.Field;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.CommandMap;
import org.bukkit.command.Command;
import org.bukkit.command.FormattedCommandAlias;
import org.bukkit.command.MultipleCommandAlias;
import org.bukkit.command.PluginIdentifiableCommand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

/**
 * Command class for where command
 *
 * @author diademiemi
 */
public class CommandExec implements CommandExecutor {

    /**
     * Apply colour codes and line breaks
     *
     * @param msg   Message to format
     * @return  Formatted message with colours
     */
    public static String format(String msg) {
        return ChatColor.translateAlternateColorCodes('&', msg);
    }
    /**
     * Method to handle commands
     *
     * @param sender    Entity sending the command
     * @param command   Command
     * @param label Command label used
     * @param args  List of arguments
     * @return  Boolean of if command was successful
     */
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Listen to the whereis command
        if (label.equalsIgnoreCase("whereis")) {
            if (args.length > 0) {
                if (sender.hasPermission("whereis.whereis")) {
                    try {
                        // Attempt to get the command map from the server
                        final Field bukkitCommandMap = Bukkit.getServer().getClass().getDeclaredField("commandMap");

                        bukkitCommandMap.setAccessible(true);
                        CommandMap commandMap = (CommandMap) bukkitCommandMap.get(Bukkit.getServer());
                        try { 
                            // Attempt to get command from command map
                            Command cmd = commandMap.getCommand(args[0]);
                            // Create string for formatting
                            StringBuilder info = new StringBuilder(format(String.format("&f&l%s: ", args[0].toString())));

                            if (cmd instanceof FormattedCommandAlias || cmd instanceof MultipleCommandAlias) {
                                // If the alias has one command
                                if (Bukkit.getServer().getCommandAliases().get(args[0]).length == 1) {
                                    info.append(format("&7&oSimple alias\n"));
                                    info.append(format(String.format("&8Command: &7&o%s&r", Bukkit.getServer().getCommandAliases().get(args[0])[0])));
                                // If the alias has multiple commands
                                } else {
                                    info.append(format("&7&oMulti-command Alias&r"));
                                    for (String a : Bukkit.getServer().getCommandAliases().get(args[0])) {
                                        info.append(format(String.format("\n&8- &7&o%s&r", a)));
                                    }    
                                }

                            // Default to plugin command or the command being provided by the server
                            } else {
                                try {
                                    // Attempt to get a plugin command
                                    PluginIdentifiableCommand pcmd = (PluginIdentifiableCommand) cmd;
                                    info.append(format("&7&oPlugin command&r\n"));
                                    info.append(format(String.format("&8Plugin: &7&o%s&r\n", pcmd.getPlugin().getName())));
                                    info.append(format(String.format("&8Main command: &7&o%s&r", cmd.getName())));
                                // Fall back to server command
                                } catch (Exception e) {
                                    info.append(format("&7&oServer command&r\n"));
                                    info.append(format(String.format("&8Main command: &7&o%s&r", cmd.getName())));   
    
                                }
                            }
                            sender.sendMessage(info.toString());
                        // Report that no command was found
                        } catch (Exception e) {
                            sender.sendMessage(format("&8No command found!"));
                        }
                    // If the command map couldn't be obtained
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    sender.sendMessage("&8Permission denied");
                }
            }
        }
        // Return true if there were no errors
        return true;
    }
}
