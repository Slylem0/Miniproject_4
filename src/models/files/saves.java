package models.files;

import models.batallas.Attack;
import models.entrenadores.Trainer;
import models.pokemones.Pokemon;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class saves {
    public static void guardarEstadoJuego(String nombreArchivo, Trainer entrenador1, Trainer entrenador2) {
        try (FileWriter writer = new FileWriter(nombreArchivo)) {
            // Entrenador 1
            writer.write("Entrenador: " + entrenador1.getName() + "\n");
            for (Pokemon p : entrenador1.getTeam()) {
                writer.write("Pokemon: " + p.getName() + " (" + p.getType() + ")\n");
                writer.write("Vida: " + p.getHealthPoints() + "/" + p.getMaxHealthPoints() + "\n");
                writer.write("Ataques:\n");
                for (Attack ataque : p.getAttacks()) {
                    writer.write("- " + ataque.getName() +
                            " (" + ataque.getType() +
                            " - " + ataque.getDamageType() +
                            ") Poder: " + ataque.getPower() + "\n");
                }
                writer.write("\n");
            }

            // Separador
            writer.write("----------------------------------------\n");

            // Entrenador 2
            writer.write("Entrenador: " + entrenador2.getName() + "\n");
            for (Pokemon p : entrenador2.getTeam()) {
                writer.write("Pokemon: " + p.getName() + " (" + p.getType() + ")\n");
                writer.write("Vida: " + p.getHealthPoints() + "/" + p.getMaxHealthPoints() + "\n");
                writer.write("Ataques:\n");
                for (Attack ataque : p.getAttacks()) {
                    writer.write("- " + ataque.getName() +
                            " (" + ataque.getType() +
                            " - " + ataque.getDamageType() +
                            ") Poder: " + ataque.getPower() + "\n");
                }
                writer.write("\n");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
