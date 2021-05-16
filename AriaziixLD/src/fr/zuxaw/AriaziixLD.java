package fr.zuxaw;

import fr.zuxaw.Command.CommandAriaziixLD;
import fr.zuxaw.listeners.DeathListeners;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

public class AriaziixLD extends JavaPlugin {

        public static boolean actived=true;
        File file = new File(this.getDataFolder(),"lastDeath.yml");
        YamlConfiguration configuration = YamlConfiguration.loadConfiguration(file);


        private Logger logger = Logger.getLogger("Minecraft");

        private PluginManager pluginManager;

        public void onEnable(){
            logger.info(ChatColor.YELLOW + "AriaziixLD enable");


            // On ecris si les commandes sont activés ou non
            configuration.set(".enable",actived);
            try {
                configuration.save(file);
            } catch (IOException e) {
                e.printStackTrace();
            }

            //command
            getCommand("ld").setExecutor(new CommandAriaziixLD(this));


            //event
            pluginManager = Bukkit.getServer().getPluginManager();
            pluginManager.registerEvents(new DeathListeners(this),this);

        }


        public void onDisable(){
            logger.info(ChatColor.YELLOW + "AriaziixLD disable");
        }

}
