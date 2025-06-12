package models.files;

import models.batallas.Attack;
import models.entrenadores.Trainer;
import models.names.Name;
import models.pokemones.Pokemon;
import models.batallas.Attack.DamageType;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class saves {

    public static void diagnosticarEquipo(Trainer entrenador) {
        System.out.println("\nDIAGNÓSTICO EQUIPO: " + entrenador.getName());
        System.out.println("Tamaño equipo: " + entrenador.getTeam().size());

        for (int i = 0; i < entrenador.getTeam().size(); i++) {
            Pokemon p = entrenador.getTeam().get(i);
            System.out.println("\nPokemon " + (i + 1) + ":");
            System.out.println("Nombre: " + p.getName());
            System.out.println("Tipo: " + p.getType());
            System.out.println("HP: " + p.getHealthPoints() + "/" + p.getMaxHealthPoints());
            System.out.println("Ataques: " + p.getAttacks().size());

            for (Attack a : p.getAttacks()) {
                System.out.println("- " + a.getName());
            }
        }
    }

    public static void guardarEstadoJuego(String nombreArchivo, Trainer entrenador1, Trainer entrenador2) {
        diagnosticarEquipo(entrenador1);
        diagnosticarEquipo(entrenador2);
        try (FileWriter writer = new FileWriter(nombreArchivo)) {
            // Entrenador 1
            writer.write("Entrenador: " + entrenador1.getName() + "\n");
            for (Pokemon p : entrenador1.getTeam()) {
                writer.write("Pokemon: " + p.getName() + "\n");
                writer.write("Tipo: " + p.getType() + "\n");  // Nuevo
                writer.write("Vida: " + p.getHealthPoints() + "/" + p.getMaxHealthPoints() + "\n");
                writer.write("Defensa: " + p.getDefense() + "\n");  // Nuevo
                writer.write("DefensaEspecial: " + p.getDefenseEspecial() + "\n");  // Nuevo
                writer.write("Velocidad: " + p.getSpeed() + "\n");  // Nuevo
                writer.write("Ataques:\n");
                for (Attack ataque : p.getAttacks()) {
                    writer.write("- " + ataque.getName() +
                            " (" + ataque.getType() +
                            " - " + ataque.getDamageType() +
                            ", Power: " + ataque.getPower() + ")\n");
                }
                writer.write("\n");
            }

            // Separador
            writer.write("----------------------------------------\n");

            // Entrenador 2
            writer.write("Entrenador: " + entrenador2.getName() + "\n");
            for (Pokemon p : entrenador2.getTeam()) {
                writer.write("Pokemon: " + p.getName() + "\n");
                writer.write("Tipo: " + p.getType() + "\n");  // Nuevo
                writer.write("Vida: " + p.getHealthPoints() + "/" + p.getMaxHealthPoints() + "\n");
                writer.write("Defensa: " + p.getDefense() + "\n");  // Nuevo
                writer.write("DefensaEspecial: " + p.getDefenseEspecial() + "\n");  // Nuevo
                writer.write("Velocidad: " + p.getSpeed() + "\n");  // Nuevo
                writer.write("Ataques:\n");
                for (Attack ataque : p.getAttacks()) {
                    writer.write("- " + ataque.getName() +
                            " (" + ataque.getType() +
                            " - " + ataque.getDamageType() +
                            ", Power: " + ataque.getPower() + ")\n");
                }
                writer.write("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static Trainer[] cargarEstadoJuego(String nombreArchivo) throws Exception {
        List<Trainer> entrenadores = new ArrayList<>();
        Trainer currentTrainer = null;
        Pokemon currentPokemon = null;
        List<Attack> currentAttacks = new ArrayList<>();
        boolean inAttacksSection = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(nombreArchivo))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();

                if (line.startsWith("Entrenador: ")) {
                    // Guardar entrenador anterior si existe
                    if (currentTrainer != null) {
                        entrenadores.add(currentTrainer);
                    }

                    String nombre = line.substring("Entrenador: ".length());
                    currentTrainer = new Trainer(nombre);
                    currentPokemon = null;
                    currentAttacks.clear();
                    inAttacksSection = false;
                }
                else if (line.startsWith("Pokemon: ")) {
                    // Guardar Pokémon anterior si existe
                    if (currentPokemon != null) {
                        for (Attack a : currentAttacks) {
                            currentPokemon.addAttack(a);
                        }
                        currentTrainer.addPokemon(currentPokemon);
                        currentAttacks.clear();
                    }

                    String nombrePokemon = line.substring("Pokemon: ".length());

                    // Leer atributos del Pokémon
                    String tipo = reader.readLine().substring("Tipo: ".length());
                    String vidaLinea = reader.readLine().substring("Vida: ".length());
                    String[] vidaPartes = vidaLinea.split("/");
                    int vidaActual = Integer.parseInt(vidaPartes[0].trim());
                    int vidaMaxima = Integer.parseInt(vidaPartes[1].trim());
                    int defensa = Integer.parseInt(reader.readLine().substring("Defensa: ".length()));
                    int defensaEspecial = Integer.parseInt(reader.readLine().substring("DefensaEspecial: ".length()));
                    int velocidad = Integer.parseInt(reader.readLine().substring("Velocidad: ".length()));
                    reader.readLine(); // Saltar "Ataques:"

                    // Buscar el enum Name correspondiente
                    Name nameEnum = null;
                    for (Name n : Name.values()) {
                        if (n.toString().equalsIgnoreCase(nombrePokemon)) {
                            nameEnum = n;
                            break;
                        }
                    }

                    if (nameEnum == null) {
                        throw new Exception("Pokémon no reconocido: " + nombrePokemon);
                    }

                    // Crear Pokémon con todos los atributos
                    currentPokemon = new Pokemon(
                            nameEnum,
                            vidaMaxima,  // HP máximo
                            defensa,
                            defensaEspecial,
                            velocidad
                    );
                    currentPokemon.setHealthPoints(vidaActual);  // Establecer HP actual
                    inAttacksSection = false;
                }
                else if (inAttacksSection && line.startsWith("- ")) {
                    String attackStr = line.substring(2);
                    // Formato: "nombre (tipo - tipo_daño, Power: poder)"
                    String[] parts = attackStr.split("\\(");
                    if (parts.length < 2) continue;

                    String nombreAtaque = parts[0].trim();
                    String detalles = parts[1].replace(")", "").trim();

                    String[] detalleParts = detalles.split(",");
                    String tipo = detalleParts[0].split("-")[0].trim();
                    String tipoDano = detalleParts[0].split("-")[1].trim();
                    int poder = Integer.parseInt(detalleParts[1].split(":")[1].trim());

                    DamageType damageType = DamageType.valueOf(tipoDano.toUpperCase());
                    currentAttacks.add(new Attack(nombreAtaque, tipo, damageType, poder));
                }
            }

            // Guardar último Pokémon y último entrenador
            if (currentPokemon != null) {
                for (Attack a : currentAttacks) {
                    currentPokemon.addAttack(a);
                }
                currentTrainer.addPokemon(currentPokemon);
            }
            if (currentTrainer != null) {
                entrenadores.add(currentTrainer);
            }

            if (entrenadores.size() != 2) {
                throw new Exception("El archivo debe contener exactamente 2 entrenadores");
            }

            return new Trainer[]{entrenadores.get(0), entrenadores.get(1)};
        }
    }
}
