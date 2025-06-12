# Pokémon Battle System – Mini Project 4

## Authors

- **Pablo Nicolás Marín** – 2459440  
- **Nicolle López Colonia** – 2259630  
- **Juan Fernando Jiménez** – 2459394  

## Project Description

This project is a Pokémon battle system with a graphical interface, developed following the **MVC (Model-View-Controller)** architecture. Two trainers can battle using their Pokémon while the system handles advanced features such as exception management, data persistence, and advanced data structures.

## Features

### 🧠 Model (Business Logic)

- **Core Classes**: Pokémon, Attack, Trainer, Battle
- **Custom Exceptions**:
  - `PokemonFaintedException`: Thrown when trying to use a Pokémon with HP ≤ 0.
  - `AttackNotAvailableException`: Thrown when attempting to use an unavailable attack.
- **Advanced Data Structures**:
  - **Stack**: Used to keep track of the last used moves in the battle (attack history).
  - **Linked List**: Used to manage the turn order based on Pokémon speed.
  - **HashMap**: Used for fast retrieval of Pokémon by name or type.
- **Persistence**:
  - The system allows saving and loading the game state (trainers, Pokémon, etc.) via `.txt` files.

### 🖥️ View (Graphical Interface)

- Interactive battle GUI that:
  - Displays Pokémon stats and current battle status.
  - Shows a **panel for move history**, reflecting the stack structure.
  - Provides options to **save and load** the game progress.
  - (Optional) Visualizes damage data with a chart library (e.g., JFreeChart).

## Technical Requirements

- Clean, well-documented code with descriptive naming.
- Strict adherence to the **MVC design pattern**.
- GitHub collaboration:
  - Each team member contributed through individual commits.
  - A Kanban board (To-Do, In Progress, Done) was used to organize tasks.

## Usage

1. Compile and run the main class from the `View` package.
2. Use the GUI to select trainers and Pokémon, and start the battle.
3. Save or load the game as needed using the provided menu options.

## Evaluation Notes

The project was developed collaboratively and each member is fully prepared to answer questions related to any part of the implementation. The design ensures modularity and clarity to facilitate understanding during evaluation.

---

Feel free to copy this into your GitHub repository as your `README.md`. Let me know if you'd like it translated to Spanish too!
