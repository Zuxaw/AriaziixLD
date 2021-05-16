package fr.zuxaw.Command;

import fr.zuxaw.AriaziixLD;
import fr.zuxaw.msg.AriaziixAlert;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.command.*;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class CommandAriaziixLD implements CommandExecutor, TabExecutor {
    AriaziixLD ariaziixLD;
    public CommandAriaziixLD(AriaziixLD ariaziixLD) {
        this.ariaziixLD = ariaziixLD;
    }




    // Va chercher la mort x dans le fichier yml
    private void LastDeath(Player sender,String death,String index,UUID uuid){
        File file = new File(ariaziixLD.getDataFolder(),"lastDeath.yml");
        YamlConfiguration configuration = YamlConfiguration.loadConfiguration(file);
        AriaziixAlert alert = new AriaziixAlert();
        OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);
        String key ="players."+uuid.toString();
        String key2 = key + "."+death;
        ConfigurationSection configurationSection = configuration.getConfigurationSection(key2);
        if(configurationSection != null){
            World world = Bukkit.getWorld(configurationSection.getString("world"));
            double x = configurationSection.getDouble("x");
            double y = configurationSection.getDouble("y");
            double z = configurationSection.getDouble("z");
            sender.sendMessage(alert.ariaziixAlert(player.getName()+ " " + index + " mort aux coordonnées x: "+x+" y: "+y+" z: "+z));
        } else {
            sender.sendMessage(alert.ariaziixAlert(player.getName()+" n'a pas de dernière mort a sont actif"));
        }
    }
    @Override
    public boolean onCommand(CommandSender Sender, Command command, String label, String[] args) {
        File file = new File(ariaziixLD.getDataFolder(),"lastDeath.yml");
        YamlConfiguration configuration = YamlConfiguration.loadConfiguration(file);

        if(command.getName().equalsIgnoreCase("LD")){
            if(Sender instanceof Player){
                Player sender = (Player) Sender;
                AriaziixAlert alert = new AriaziixAlert();
                if (args.length == 1){
                    switch (args[0]){
                        case "disable":
                            if(sender.isOp()){
                                AriaziixLD.actived = false;
                                sender.sendMessage(alert.ariaziixAlert("Vous avez desactiver les messages de mort"));
                                configuration.set(".enable",AriaziixLD.actived);
                            }
                            break;
                        case "enable":
                            if (sender.isOp()){
                                AriaziixLD.actived = true;
                                sender.sendMessage(alert.ariaziixAlert("Vous avez activer les messages de mort"));
                                configuration.set(".enable",AriaziixLD.actived);
                            }
                            break;
                        case "LastDeath":
                            LastDeath(sender,"last_death","a sa dernière",sender.getUniqueId());
                            break;
                        case "BeforeLastDeath":
                            LastDeath(sender,"last_death_before","a sont avant dernière",sender.getUniqueId());
                            break;
                    }
                }
                if (sender.isOp() && args.length == 2){
                    OfflinePlayer joueur = Bukkit.getOfflinePlayer(args[1]);
                    System.out.println("UUID :"+joueur.getUniqueId());
                    if(joueur!=null){
                        switch (args[0]){
                            case "LastDeath":
                                LastDeath(sender,"last_death","a sa dernière",joueur.getUniqueId());
                                break;
                            case "BeforeLastDeath":
                                LastDeath(sender,"last_death_before","a sont avant dernière",joueur.getUniqueId());
                                break;
                        }
                    }
                }
                try {
                    configuration.save(file);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
            return true;
        }

        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> tabComple = new ArrayList<>();

        if(args.length == 1){
            List<String> COMMANDS = new ArrayList<>();
            COMMANDS.add("LastDeath");
            COMMANDS.add("BeforeLastDeath");
            if(sender.isOp()){
                COMMANDS.add("enable");
                COMMANDS.add("disable");
            }
            StringUtil.copyPartialMatches(args[0], COMMANDS ,tabComple);
        }

        // Player Complete en 2eme argument
        if (args.length == 2 && sender.isOp()){
            List<String> player = new ArrayList<String>();
            for (OfflinePlayer p : Bukkit.getOfflinePlayers()) {
                player.add(p.getName());
            }
            StringUtil.copyPartialMatches(args[1],player,tabComple);
        }


        return tabComple;
    }
}
