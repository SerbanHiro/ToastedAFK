package me.serbob.toastedafk.Templates;

import me.serbob.toastedafk.ToastedAFK;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static me.serbob.toastedafk.Managers.ValuesManager.playerStats;

public class ItemDistribution {
    private static final Random random = new Random();
    public static void distributeCommands(Player player) {

        List<String> commands = new ArrayList<>();
        List<Double> probabilities = new ArrayList<>();
        for(String key:ToastedAFK.instance.getConfig().getConfigurationSection("probability_commands").getKeys(false)) {
            commands.add(ToastedAFK.instance.getConfig().getString("probability_commands."+key+".command"));
            probabilities.add(ToastedAFK.instance.getConfig().getDouble("probability_commands."+key+".chance"));
        }

        // Normalize probabilities to ensure they add up to 100%
        double totalProbability = probabilities.stream().mapToDouble(Double::doubleValue).sum();
        probabilities.replaceAll(aDouble -> aDouble / totalProbability * 100);

        String command = getRandomItem(commands, probabilities);
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(),command.replace("{player}",player.getName()));
    }
    // Helper method to retrieve a random item based on probabilities
    private static String getRandomItem(List<String> commands, List<Double> probabilities) {
        if (commands.size() != probabilities.size()) {
            throw new IllegalArgumentException("Items and probabilities must have the same size");
        }

        double totalProbability = probabilities.stream().mapToDouble(Double::doubleValue).sum();
        double randomValue = random.nextDouble() * totalProbability;

        double cumulativeProbability = 0.0;
        for (int i = 0; i < commands.size(); i++) {
            cumulativeProbability += probabilities.get(i);
            if (randomValue < cumulativeProbability) {
                return commands.get(i);
            }
        }

        // This line will only be reached if the probabilities do not sum up to 100%
        throw new IllegalStateException("Probabilities do not sum up to 100%");
    }
}
