package view.Gui;

import controller.controller;
import models.entrenadores.Trainer;
import models.files.saves;
import models.pokemones.Pokemon;

import javax.swing.*;
import java.awt.*;
import java.util.function.BiConsumer;

public class BattleSetupFrame extends JFrame {
    private final JTextField trainer1Field;
    private final JTextField trainer2Field;
    private final JTextArea team1Area;
    private final JTextArea team2Area;
    private final JButton assignTeamsButton;
    private final JButton startBattleButton;
    private Trainer entrenador1;
    private Trainer entrenador2;
    private BiConsumer<String, String> onTeamsAssigned;
    private final JButton loadButton;
    private final JButton viewPokemonsButton; // Nuevo botón

    public BattleSetupFrame() {
        // Configuración básica del frame
        setTitle("Configuración de Batalla Pokémon");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLayout(new BorderLayout());

        // Panel superior para los nombres
        JPanel topPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        topPanel.add(new JLabel("Entrenador 1:"));
        trainer1Field = new JTextField();
        topPanel.add(trainer1Field);

        topPanel.add(new JLabel("Entrenador 2:"));
        trainer2Field = new JTextField();
        topPanel.add(trainer2Field);

        // Panel central para mostrar los equipos
        JPanel centerPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        team1Area = new JTextArea();
        team1Area.setEditable(false);
        team2Area = new JTextArea();
        team2Area.setEditable(false);

        centerPanel.add(new JScrollPane(team1Area));
        centerPanel.add(new JScrollPane(team2Area));

        // Panel inferior para los botones
        JPanel bottomPanel = new JPanel(new FlowLayout());
        assignTeamsButton = new JButton("Asignar Equipos Aleatorios");
        startBattleButton = new JButton("¡Comenzar Batalla!");
        loadButton = new JButton("Cargar Partida");
        viewPokemonsButton = new JButton("Ver Pokémons");
        bottomPanel.add(viewPokemonsButton); // Agregarlo al panel de botones

        bottomPanel.add(loadButton);
        startBattleButton.setEnabled(false);

        bottomPanel.add(assignTeamsButton);
        bottomPanel.add(startBattleButton);

        // Agregar todos los paneles al frame
        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        // Configurar los listeners de los botones
        setupListeners();
    }



    private void setupListeners() {
        assignTeamsButton.addActionListener(e -> {
            if (trainer1Field.getText().trim().isEmpty() || trainer2Field.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Por favor, ingresa los nombres de ambos entrenadores",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Asignar equipos aleatorios
            asignarEquiposAleatorios();
            startBattleButton.setEnabled(true);
        });

        startBattleButton.addActionListener(e -> {
            iniciarBatalla();
        });
        loadButton.addActionListener(e -> cargarPartida());
        viewPokemonsButton.addActionListener(e -> PokemonCatalogFrame.showCatalog());
    }

    private void cargarPartida() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            try {
                java.io.File file = fileChooser.getSelectedFile();
                Trainer[] entrenadores = saves.cargarEstadoJuego(file.getAbsolutePath());

                if (entrenadores != null) {
                    entrenador1 = entrenadores[0];
                    entrenador2 = entrenadores[1];

                    // Actualizar interfaz
                    trainer1Field.setText(entrenador1.getName());
                    trainer2Field.setText(entrenador2.getName());

                    team1Area.setText("Equipo de " + entrenador1.getName() + ":\n");
                    for (Pokemon p : entrenador1.getTeam()) {
                        team1Area.append("- " + p.getName() + " (HP: " + p.getHealthPoints() + "/" + p.getMaxHealthPoints() + ")\n");
                    }

                    team2Area.setText("Equipo de " + entrenador2.getName() + ":\n");
                    for (Pokemon p : entrenador2.getTeam()) {
                        team2Area.append("- " + p.getName() + " (HP: " + p.getHealthPoints() + "/" + p.getMaxHealthPoints() + ")\n");
                    }

                    startBattleButton.setEnabled(true);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "Error al cargar la partida: " + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void asignarEquiposAleatorios() {

        entrenador1 = new Trainer(trainer1Field.getText());
        entrenador2 = new Trainer(trainer2Field.getText());

        controller.TeamBuilder.generateRandomTeam(entrenador1);
        controller.TeamBuilder.generateRandomTeam(entrenador2);


        team1Area.setText("Equipo de " + entrenador1.getName() + ":\n");
        team1Area.append("- " + entrenador1.toString() + "\n");


        team2Area.setText("Equipo de " + entrenador2.getName() + ":\n");
        team2Area.append("- " + entrenador2.toString() + "\n");
        if (onTeamsAssigned != null) {
            notificarTeamsAssigned();
        }
    }

    private void iniciarBatalla() {
        BattleFrame battleFrame = new BattleFrame(entrenador1, entrenador2);
        battleFrame.setVisible(true);
        this.dispose();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new BattleSetupFrame().setVisible(true);
        });
    }
    // En src/view/Gui/BattleSetupFrame.java

    public void setOnTeamsAssigned(BiConsumer<String, String> listener) {
        this.onTeamsAssigned = listener;
    }

    public void notificarTeamsAssigned() {
        if (onTeamsAssigned != null) {
            onTeamsAssigned.accept(trainer1Field.getText(), trainer2Field.getText());
        }
    }
}