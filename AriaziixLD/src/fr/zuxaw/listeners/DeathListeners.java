package fr.zuxaw.listeners;

import fr.zuxaw.AriaziixLD;
import fr.zuxaw.msg.AriaziixAlert;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class DeathListeners implements Listener {
    AriaziixLD ariaziixLD;

    public DeathListeners(AriaziixLD ariaziixLD) {
        this.ariaziixLD = ariaziixLD;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    //Détecte la mort d'un joueur
    public void DeathPoint (PlayerDeathEvent event){
        if(AriaziixLD.actived == true){
            Player DeadPlayer = event.getEntity(); // récupère le joueur mort
            Location DeadPos = DeadPlayer.getLocation(); // récupère sa position
            AriaziixAlert alert = new AriaziixAlert();
            DeadPlayer.sendMessage(alert.ariaziixAlert("Tu es mort en x:"+DeadPos.getBlockX()+" y:"+DeadPos.getBlockY()+" z:"+DeadPos.getBlockZ()));


            // Création du fichier ou lecture
            File file = new File(ariaziixLD.getDataFolder(),"lastDeath.yml");
            YamlConfiguration configuration = YamlConfiguration.loadConfiguration(file);


            UUID uuid = DeadPlayer.getUniqueId(); // récupère l'id du joueur mort

            String key ="players."+uuid.toString();
            String key2 = key + ".last_death";
            String key3 = key + ".last_death_before";

            ConfigurationSection configurationSection = configuration.getConfigurationSection(key2);

            if(configurationSection != null){
                // lecture
                World world = Bukkit.getWorld(configurationSection.getString("world"));
                double x = configurationSection.getDouble("x");
                double y = configurationSection.getDouble("y");
                double z = configurationSection.getDouble("z");

                // écriture
                configuration.set(key3 + ".world",world.getName());
                configuration.set(key3 + ".x",x);
                configuration.set(key3 + ".y",y);
                configuration.set(key3 + ".z",z);
            }

            configuration.set(key2 + ".world",DeadPos.getWorld().getName());
            configuration.set(key2 + ".x",DeadPos.getBlockX());
            configuration.set(key2 + ".y", DeadPos.getBlockX());
            configuration.set(key2 + ".z", DeadPos.getBlockZ());

            try {
                configuration.save(file); // on sauvegarde si il y a pas d'erreur
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
