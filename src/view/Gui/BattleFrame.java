package view.Gui;

//BIEN


import controller.controller;
import models.batallas.Attack;
import models.entrenadores.Trainer;
import models.pokemones.Pokemon;
import view.View;

import javax.swing.*;
import java.awt.*;

import static view.Gui.TrainerImages.scaleImage;

public class BattleFrame extends JFrame implements View {
    private final Trainer entrenador1;
    private final Trainer entrenador2;
    private Pokemon pokemon1;
    private Pokemon pokemon2;
    private final JTextArea battleLog;
    private final JPanel pokemonPanel1;
    private final JPanel pokemonPanel2;
    private final JProgressBar healthBar1;
    private final JProgressBar healthBar2;
    private final JButton[] attackButtons;
    private final JButton switchPokemonButton;
    private boolean isEntrenador1Turn = true;
    private final JButton saveGameButton;
    private final JButton loadGameButton;

    public BattleFrame(Trainer entrenador1, Trainer entrenador2, JButton saveGameButton, JButton loadGameButton) {
        this.entrenador1 = entrenador1;
        this.entrenador2 = entrenador2;
        this.saveGameButton = saveGameButton;
        this.loadGameButton = loadGameButton;
        ImageIcon trainer1Icon = scaleImage(TrainerImages.getTrainer1Image(), 150, 150);
        ImageIcon trainer2Icon = scaleImage(TrainerImages.getTrainer2Image(), 150, 150);

        JLabel trainer1Label = new JLabel(trainer1Icon);
        JLabel trainer2Label = new JLabel(trainer2Icon);

        // ALL THE CONFIGURATIONS
        setTitle("Batalla Pokémon");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 800);
        setLayout(new BorderLayout());

        // MAIN PANEL INITIAL
        JPanel battlePanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        // panel of pokemon 2
        pokemonPanel2 = new JPanel(new BorderLayout());
        healthBar2 = new JProgressBar(0, 100);
        pokemonPanel2.add(new JLabel("Pokemon 2"), BorderLayout.NORTH);
        pokemonPanel2.add(healthBar2, BorderLayout.SOUTH);
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.NORTHEAST;
        battlePanel.add(pokemonPanel2, gbc);

        // panel od pokemon 1
        pokemonPanel1 = new JPanel(new BorderLayout());
        healthBar1 = new JProgressBar(0, 100);
        pokemonPanel1.add(new JLabel("Pokemon 1"), BorderLayout.NORTH);
        pokemonPanel1.add(healthBar1, BorderLayout.SOUTH);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.SOUTHWEST;
        battlePanel.add(pokemonPanel1, gbc);


        //AQUIIIIII

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        battlePanel.add(trainer2Label, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.SOUTHEAST;
        battlePanel.add(trainer1Label, gbc);


        // Panel de control (abajo)

        // Panel de control (abajo) - LAYOUT CORREGIDO
        JPanel controlPanel = new JPanel(new BorderLayout());
        controlPanel.setPreferredSize(new Dimension(1000, 250)); // Dar altura específica

        // Panel superior de botones
        JPanel buttonPanel = new JPanel(new BorderLayout());

        // Panel de botones de ataque
        JPanel attackButtonsPanel = new JPanel(new GridLayout(2, 2, 5, 5));
        attackButtons = new JButton[4];
        for (int i = 0; i < 4; i++) {
            attackButtons[i] = new JButton("Ataque " + (i + 1));
            attackButtons[i].setPreferredSize(new Dimension(120, 40));
            attackButtonsPanel.add(attackButtons[i]);
        }

        // Botón de cambio de Pokémon
        switchPokemonButton = new JButton("Cambiar Pokémon");
        switchPokemonButton.setPreferredSize(new Dimension(150, 85));

        // Organizar botones
        buttonPanel.add(attackButtonsPanel, BorderLayout.CENTER);
        buttonPanel.add(switchPokemonButton, BorderLayout.EAST);
        buttonPanel.setPreferredSize(new Dimension(1000, 100));

        // Log de batalla con tamaño fijo
        battleLog = new JTextArea(8, 40); // Aumentar filas visibles
        battleLog.setEditable(false);
        battleLog.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        battleLog.setBackground(Color.WHITE);
        battleLog.setBorder(BorderFactory.createLoweredBevelBorder());



        JScrollPane scrollPane = new JScrollPane(battleLog);
        scrollPane.setPreferredSize(new Dimension(1000, 150)); // Tamaño fijo
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        // Agregar componentes al panel de control
        controlPanel.add(buttonPanel, BorderLayout.NORTH);
        controlPanel.add(scrollPane, BorderLayout.CENTER);

        // Agregar al frame principal
        add(battlePanel, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);

        // Asegurar que el frame tenga el tamaño correcto
        setMinimumSize(new Dimension(1000, 800));

        // Panel de botones adicionales
        JPanel extraButtonsPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        saveGameButton = new JButton("Guardar Partida");
        loadGameButton = new JButton("Cargar Partida");
        saveGameButton.setPreferredSize(new Dimension(150, 40));
        loadGameButton.setPreferredSize(new Dimension(150, 40));

        extraButtonsPanel.add(saveGameButton);
        extraButtonsPanel.add(loadGameButton);

        // Modificar el buttonPanel para incluir los nuevos botones:
        JPanel rightButtonsPanel = new JPanel(new BorderLayout());
        rightButtonsPanel.add(switchPokemonButton, BorderLayout.NORTH);
        rightButtonsPanel.add(extraButtonsPanel, BorderLayout.CENTER);

        buttonPanel.add(attackButtonsPanel, BorderLayout.CENTER);
        buttonPanel.add(rightButtonsPanel, BorderLayout.EAST); // Cambiar esta línea


        // start of battle
        iniciarBatalla();
        setupListeners();
    }

private void ejecutarAtaque(int attackIndex) {

    Pokemon attacker = isEntrenador1Turn ? pokemon1 : pokemon2;
    Pokemon defender = isEntrenador1Turn ? pokemon2 : pokemon1;

    if (attackIndex >= attacker.getAttacks().size()) {
        battleLog.append("¡Ataque no disponible!\n");
        scrollToBottom();
        return;
    }

    Attack attack = attacker.getAttacks().get(attackIndex);
    int damage = controller.BattleManager.calculateDamage(attack, attacker, defender);
    defender.receiveDamage(damage);

    // MENSAJE DE ATAQUE MÁS CLARO
    battleLog.append(String.format("%s usa %s y causa %d de daño a %s!\n",
            attacker.getName(), attack.getName(), damage, defender.getName()));

    battleLog.append(String.format("%s tiene ahora %d HP restantes.\n",
            defender.getName(), defender.getHealthPoints()));

    // Actualizar la interfaz
    actualizarInterfaz();
    scrollToBottom();

    // Verificar si la batalla continúa
    if (!verificarEstadoBatalla()) {
        cambiarTurno();
    }

}

private void cambiarTurno() {
    isEntrenador1Turn = !isEntrenador1Turn;
    actualizarControles();

    System.out.println("Turno cambiado a: " + (isEntrenador1Turn ? entrenador1.getName() : entrenador2.getName()));

    // besure of all the buttons are there
    for (int i = 0; i < 4; i++) {
        Pokemon pokemonActual = isEntrenador1Turn ? pokemon1 : pokemon2;
        if (i < pokemonActual.getAttacks().size()) {
            attackButtons[i].setEnabled(true);
        } else {
            attackButtons[i].setEnabled(false);
        }
    }
    switchPokemonButton.setEnabled(true);
}

private void actualizarControles() {

    boolean esJugadorActual = isEntrenador1Turn;

    // Habilitar botones de ataque solo para el jugador actual
    for (int i = 0; i < 4; i++) {
        Pokemon pokemonActual = isEntrenador1Turn ? pokemon1 : pokemon2;
        if (i < pokemonActual.getAttacks().size()) {
            Attack attack = pokemonActual.getAttacks().get(i);
            attackButtons[i].setText(attack.getName() + " (" + attack.getPower() + ")");
            attackButtons[i].setEnabled(esJugadorActual);
        } else {
            attackButtons[i].setText("N/A");
            attackButtons[i].setEnabled(false);
        }
    }

    switchPokemonButton.setEnabled(esJugadorActual);

    // SOLO agregar mensaje de turno si no es la inicialización
    if (pokemon1 != null && pokemon2 != null) {
        battleLog.append("\n--- Turno de " + (isEntrenador1Turn ? entrenador1.getName() : entrenador2.getName()) + " ---\n");
        scrollToBottom();
    }

}

    private void cambiarPokemon() {
        Trainer currentTrainer = isEntrenador1Turn ? entrenador1 : entrenador2;
        Pokemon newPokemon = selectInitialPokemon(currentTrainer);

        if (newPokemon != null) {
            if (isEntrenador1Turn) {
                pokemon1 = newPokemon;
            } else {
                pokemon2 = newPokemon;
            }

            battleLog.append(String.format("%s cambia a %s! (%d HP)\n",
                    currentTrainer.getName(), newPokemon.getName(), newPokemon.getHealthPoints()));

            actualizarInterfaz();
            scrollToBottom();
            cambiarTurno();
        }
    }

    private void actualizarInterfaz() {
        updatePokemonDisplay(pokemonPanel1, pokemon1, healthBar1);
        updatePokemonDisplay(pokemonPanel2, pokemon2, healthBar2);
        actualizarControles();

        revalidate();
        repaint();
    }

    private boolean verificarEstadoBatalla() {
        if (!pokemon1.isAlive()) {
            if (entrenador1.getTeam().stream().anyMatch(Pokemon::isAlive)) {
                JOptionPane.showMessageDialog(this, entrenador1.getName() + ", tu Pokémon ha sido derrotado. Elige otro.");
                pokemon1 = selectInitialPokemon(entrenador1);
                actualizarInterfaz();
                return false;
            } else {
                anunciarGanador(entrenador2);
                return true;
            }
        }

        if (!pokemon2.isAlive()) {
            if (entrenador2.getTeam().stream().anyMatch(Pokemon::isAlive)) {
                JOptionPane.showMessageDialog(this, entrenador2.getName() + ", tu Pokémon ha sido derrotado. Elige otro.");
                pokemon2 = selectInitialPokemon(entrenador2);
                actualizarInterfaz();
                return false;
            } else {
                anunciarGanador(entrenador1);
                return true;
            }
        }

        return false; // Ambos siguen vivos
    }

    private void anunciarGanador(Trainer winner) {
        JOptionPane.showMessageDialog(this,
                "¡" + winner.getName() + " ha ganado la batalla!",
                "Fin de la Batalla",
                JOptionPane.INFORMATION_MESSAGE);
        dispose();
    }


    private void iniciarBatalla() {
        pokemon1 = selectInitialPokemon(entrenador1);
        pokemon2 = selectInitialPokemon(entrenador2);

        battleLog.append("=== ¡LA BATALLA HA COMENZADO! ===\n");
        battleLog.append(String.format("%s envía a %s! (%d HP)\n",
                entrenador1.getName(), pokemon1.getName(), pokemon1.getHealthPoints()));
        battleLog.append(String.format("%s envía a %s! (%d HP)\n",
                entrenador2.getName(), pokemon2.getName(), pokemon2.getHealthPoints()));

        actualizarInterfaz();
        scrollToBottom();
    }

    private Pokemon selectInitialPokemon(Trainer trainer) {
        Pokemon[] options = trainer.getTeam().stream()
                .filter(Pokemon::isAlive)
                .toArray(Pokemon[]::new);

        if (options.length == 0) {
            return null;
        }

        return (Pokemon) JOptionPane.showInputDialog(
                this,
                trainer.getName() + ", elige tu Pokémon:",
                "Selección de Pokémon",
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]
        );
    }

private void setupListeners() {

    for (int i = 0; i < attackButtons.length; i++) {
        final int attackIndex = i;
        attackButtons[i].addActionListener(e -> {
            System.out.println("Botón de ataque " + (attackIndex+1) + " presionado por " + 
                (isEntrenador1Turn ? entrenador1.getName() : entrenador2.getName()));
            ejecutarAtaque(attackIndex);
        });
    }


    switchPokemonButton.addActionListener(e -> {
        System.out.println("Botón de cambio presionado por " + 
            (isEntrenador1Turn ? entrenador1.getName() : entrenador2.getName()));
        cambiarPokemon();
    });
}

private void scrollToBottom() {
        SwingUtilities.invokeLater(() -> {
            battleLog.setCaretPosition(battleLog.getDocument().getLength());
        });
    }

private void updatePokemonDisplay(JPanel panel, Pokemon pokemon, JProgressBar healthBar) {

    if (pokemon == null) {
        return;
    }

    ((JLabel)panel.getComponent(0)).setText(pokemon.getName() + " (" + pokemon.getHealthPoints() + " HP)");
    

    int maxHealth = pokemon.getMaxHealthPoints(); // Asumiendo que existe este método
    if (maxHealth <= 0) maxHealth = 1;
    
    int healthPercentage = (pokemon.getHealthPoints() * 100) / maxHealth;
    healthBar.setValue(healthPercentage);
    healthBar.setString(pokemon.getHealthPoints() + "/" + maxHealth + " HP");
    healthBar.setStringPainted(true);
    

    if (healthPercentage > 50) {
        healthBar.setForeground(Color.GREEN);
    } else if (healthPercentage > 25) {
        healthBar.setForeground(Color.YELLOW);
    } else {
        healthBar.setForeground(Color.RED);
    }
}

}