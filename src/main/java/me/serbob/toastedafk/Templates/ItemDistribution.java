package me.serbob.toastedafk.Templates;

import me.serbob.toastedafk.ToastedAFK;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ItemDistribution {
    private static final Random RANDOM = new Random();

    public static void distributeCommands(Player player) {
        ConfigurationSection probSection = ToastedAFK.instance.getConfig().getConfigurationSection("probability_commands");
        if (probSection == null) {
            return;
        }

        List<List<String>> commands = new ArrayList<>();
        List<Double> probabilities = new ArrayList<>();

        for (String key : probSection.getKeys(false)) {
            ConfigurationSection commandSection = probSection.getConfigurationSection(key);
            if (commandSection != null) {
                commands.add(commandSection.getStringList("commands"));
                probabilities.add(commandSection.getDouble("chance"));
            }
        }

        if (commands.isEmpty() || probabilities.isEmpty()) {
            return;
        }

        normalizeProbabilities(probabilities);

        List<String> selectedCommands = getRandomItem(commands, probabilities);
        CoreHelpers.executeCommands(player, selectedCommands);
    }

    private static void normalizeProbabilities(List<Double> probabilities) {
        double totalProbability = probabilities.stream().mapToDouble(Double::doubleValue).sum();
        for (int i = 0; i < probabilities.size(); i++) {
            probabilities.set(i, probabilities.get(i) / totalProbability * 100);
        }
    }

    private static List<String> getRandomItem(List<List<String>> commands, List<Double> probabilities) {
        if (commands.size() != probabilities.size()) {
            throw new IllegalArgumentException("Commands and probabilities must have the same size");
        }

        double randomValue = RANDOM.nextDouble() * 100;
        double cumulativeProbability = 0.0;

        for (int i = 0; i < commands.size(); i++) {
            cumulativeProbability += probabilities.get(i);
            if (randomValue < cumulativeProbability) {
                return commands.get(i);
            }
        }

        // If we reach here, return the last item (due to potential floating-point inaccuracies)
        return commands.get(commands.size() - 1);
    }
}