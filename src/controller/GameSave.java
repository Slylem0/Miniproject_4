package controller;
import models.entrenadores.Trainer;
import models.pokemones.Pokemon;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class GameSave {

    public static class SaveData {
        public String trainer1Name;
        public String trainer2Name;
        public String currentPokemon1;
        public String currentPokemon2;
        public int pokemon1HP;
        public int pokemon2HP;
        public boolean isTrainer1Turn;
        public String battleLog;
        public String saveDate;
        public List<String> trainer1Team;
        public List<String> trainer2Team;
        public List<Integer> trainer1HP;
        public List<Integer> trainer2HP;


        public SaveData() {
            trainer1Team = new ArrayList<>();
            trainer2Team = new ArrayList<>();
            trainer1HP = new ArrayList<>();
            trainer2HP = new ArrayList<>();
        }
    }

    public static boolean saveGame(Trainer trainer1, Trainer trainer2,
                                   Pokemon currentPokemon1, Pokemon currentPokemon2,
                                   boolean isTrainer1Turn, String battleLogText) {
        try {
            // Crear directorio saves si no existe
            File saveDir = new File("saves");
            if (!saveDir.exists()) {
                saveDir.mkdirs();
            }

            // Generar nombre de archivo con fecha
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
            String fileName = "saves/partida_" + timestamp + ".txt";

            SaveData saveData = new SaveData();
            saveData.trainer1Name = trainer1.getName();
            saveData.trainer2Name = trainer2.getName();
            saveData.currentPokemon1 = String.valueOf(currentPokemon1.getName());
            saveData.currentPokemon2 = String.valueOf(currentPokemon2.getName());
            saveData.pokemon1HP = currentPokemon1.getHealthPoints();
            saveData.pokemon2HP = currentPokemon2.getHealthPoints();
            saveData.isTrainer1Turn = isTrainer1Turn;
            saveData.battleLog = battleLogText;
            saveData.saveDate = LocalDateTime.now().toString();

            // Guardar información de todos los pokémon
            for (Pokemon p : trainer1.getTeam()) {
                saveData.trainer1Team.add(p.getName().toString());
                saveData.trainer1HP.add(p.getHealthPoints());
            }

            for (Pokemon p : trainer2.getTeam()) {
                saveData.trainer2Team.add(p.getName().toString());
                saveData.trainer2HP.add(p.getHealthPoints());
            }

            // Escribir al archivo
            try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {
                writer.println("POKEMON_BATTLE_SAVE_V1.0");
                writer.println("SAVE_DATE=" + saveData.saveDate);
                writer.println("TRAINER1_NAME=" + saveData.trainer1Name);
                writer.println("TRAINER2_NAME=" + saveData.trainer2Name);
                writer.println("CURRENT_POKEMON1=" + saveData.currentPokemon1);
                writer.println("CURRENT_POKEMON2=" + saveData.currentPokemon2);
                writer.println("POKEMON1_HP=" + saveData.pokemon1HP);
                writer.println("POKEMON2_HP=" + saveData.pokemon2HP);
                writer.println("IS_TRAINER1_TURN=" + saveData.isTrainer1Turn);

                writer.println("TRAINER1_TEAM_SIZE=" + saveData.trainer1Team.size());
                for (int i = 0; i < saveData.trainer1Team.size(); i++) {
                    writer.println("T1_POKEMON_" + i + "=" + saveData.trainer1Team.get(i) + "," + saveData.trainer1HP.get(i));
                }

                writer.println("TRAINER2_TEAM_SIZE=" + saveData.trainer2Team.size());
                for (int i = 0; i < saveData.trainer2Team.size(); i++) {
                    writer.println("T2_POKEMON_" + i + "=" + saveData.trainer2Team.get(i) + "," + saveData.trainer2HP.get(i));
                }

                writer.println("BATTLE_LOG_START");
                writer.println(saveData.battleLog);
                writer.println("BATTLE_LOG_END");
            }

            return true;

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static SaveData loadGame(String fileName) {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            SaveData saveData = new SaveData();
            String line;
            boolean readingLog = false;
            StringBuilder logBuilder = new StringBuilder();

            while ((line = reader.readLine()) != null) {
                if (line.equals("BATTLE_LOG_START")) {
                    readingLog = true;
                    continue;
                } else if (line.equals("BATTLE_LOG_END")) {
                    readingLog = false;
                    saveData.battleLog = logBuilder.toString();
                    continue;
                }

                if (readingLog) {
                    logBuilder.append(line).append("\n");
                    continue;
                }

                if (line.startsWith("SAVE_DATE=")) {
                    saveData.saveDate = line.substring(10);
                } else if (line.startsWith("TRAINER1_NAME=")) {
                    saveData.trainer1Name = line.substring(14);
                } else if (line.startsWith("TRAINER2_NAME=")) {
                    saveData.trainer2Name = line.substring(14);
                } else if (line.startsWith("CURRENT_POKEMON1=")) {
                    saveData.currentPokemon1 = line.substring(17);
                } else if (line.startsWith("CURRENT_POKEMON2=")) {
                    saveData.currentPokemon2 = line.substring(17);
                } else if (line.startsWith("POKEMON1_HP=")) {
                    saveData.pokemon1HP = Integer.parseInt(line.substring(12));
                } else if (line.startsWith("POKEMON2_HP=")) {
                    saveData.pokemon2HP = Integer.parseInt(line.substring(12));
                } else if (line.startsWith("IS_TRAINER1_TURN=")) {
                    saveData.isTrainer1Turn = Boolean.parseBoolean(line.substring(17));
                } else if (line.startsWith("T1_POKEMON_")) {
                    String[] parts = line.substring(line.indexOf("=") + 1).split(",");
                    saveData.trainer1Team.add(parts[0]);
                    saveData.trainer1HP.add(Integer.parseInt(parts[1]));
                } else if (line.startsWith("T2_POKEMON_")) {
                    String[] parts = line.substring(line.indexOf("=") + 1).split(",");
                    saveData.trainer2Team.add(parts[0]);
                    saveData.trainer2HP.add(Integer.parseInt(parts[1]));
                }
            }

            return saveData;

        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List<String> getSaveFiles() {
        File saveDir = new File("saves");
        List<String> saveFiles = new ArrayList<>();

        if (saveDir.exists() && saveDir.isDirectory()) {
            File[] files = saveDir.listFiles((dir, name) -> name.endsWith(".txt"));
            if (files != null) {
                for (File file : files) {
                    saveFiles.add(file.getName());
                }
            }
        }

        return saveFiles;
    }
}

