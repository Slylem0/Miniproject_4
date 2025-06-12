package controller;

import models.batallas.Attack;
import models.entrenadores.Trainer;
import models.names.AttackName;
import models.names.Name;
import models.pokemones.Pokemon;
import view.Gui.BattleSetupFrame;

import java.util.*;

import static controller.controller.Battle.hasAdvantage;

public class controller {

    private BattleSetupFrame battleSetupFrame;

    public controller() {
        this.battleSetupFrame = new BattleSetupFrame();

        this.battleSetupFrame.setVisible(true);

    }


    public static class Battle {
        public static void startBattle(Trainer trainer1, Trainer trainer2, Scanner scanner) {
            Pokemon pokemon1 = choosePokemon(trainer1, scanner);
            Pokemon pokemon2 = choosePokemon(trainer2, scanner);

            while (true) {
                // Determinar el orden de los turnos basado en la velocidad
                Pokemon firstPokemon = BattleManager.determineFirstAttacker(pokemon1, pokemon2);
                Pokemon secondPokemon = (firstPokemon == pokemon1) ? pokemon2 : pokemon1;
                Trainer firstTrainer = (firstPokemon == pokemon1) ? trainer1 : trainer2;
                Trainer secondTrainer = (firstPokemon == pokemon1) ? trainer2 : trainer1;

                System.out.println("\n" + firstPokemon.getName() + " moves first due to higher speed!");

            // Turno del primer Pokémon
            Pokemon resultDefender = executeTurn(firstPokemon, secondPokemon, firstTrainer, secondTrainer, scanner);
            // Actualizar las referencias correctas
            if (secondPokemon == pokemon1) {
                pokemon1 = resultDefender;
            } else {
                pokemon2 = resultDefender;
            }

            if (!secondTrainer.hasAlivePokemon()) {
                announceWinner(firstTrainer);
                break;
            }

            // Turno del segundo Pokémon
            Pokemon resultAttacker = executeTurn(resultDefender, firstPokemon, secondTrainer, firstTrainer, scanner);
            // Actualizar las referencias correctas
            if (firstPokemon == pokemon1) {
                pokemon1 = resultAttacker;
            } else {
                pokemon2 = resultAttacker;
            }

            if (!firstTrainer.hasAlivePokemon()) {
                announceWinner(secondTrainer);
                break;
            }
        }
    }


        private static Pokemon executeTurn(Pokemon attacker, Pokemon defender, Trainer attackerTrainer,
                                           Trainer defenderTrainer, Scanner scanner) {
            System.out.println("\n" + attackerTrainer.getName() + "'s turn!");
            System.out.println("1. Attack");
            System.out.println("2. Switch Pokemon");
            System.out.print("Choose your action: ");

            int choice = scanner.nextInt();
            if (choice == 1) {
                executeAttack(attacker, defender, attackerTrainer, scanner);
                if (!defender.isAlive()) {
                    System.out.println(defender.getName() + " fainted!");
                    return choosePokemon(defenderTrainer, scanner);
                }
                return defender;
            } else {
                return choosePokemon(attackerTrainer, scanner);
            }
        }


        private static void executeAttack(Pokemon attacker, Pokemon defender, Trainer attackerTrainer, Scanner scanner) {
            System.out.println("\n" + attackerTrainer.getName() + "'s " + attacker.getName() + "'s turn!");

            // Display attacks with their type
            List<Attack> attacks = attacker.getAttacks();
            for (int i = 0; i < attacks.size(); i++) {
                Attack attack = attacks.get(i);
                System.out.printf("%d. %s (%s - %s, Power: %d)%n",
                        i + 1,
                        attack.getName(),
                        attack.getType(),
                        attack.getDamageType(),
                        attack.getPower()
                );
            }

            // Select attack
            System.out.print("Choose attack (1-" + attacks.size() + "): ");
            int choice = scanner.nextInt() - 1;
            Attack selectedAttack = attacks.get(choice);

            // Calculate and apply damage
            int damage = BattleManager.calculateDamage(selectedAttack, attacker, defender);
            defender.receiveDamage(damage);

            // Display attack results
            System.out.printf("%s used %s! ", attacker.getName(), selectedAttack.getName());
            if (hasAdvantage(selectedAttack.getType(), defender.getType())) {
                System.out.print("It's super effective! ");
            }
            System.out.printf("Dealt %d damage!%n", damage);
            System.out.printf("%s has %d HP remaining!%n", defender.getName(), defender.getHealthPoints());

        }

        private static void announceWinner(Trainer winner) {
            System.out.println("\n" + winner.getName() + " wins the battle!");
        }

        private static Pokemon choosePokemon(Trainer trainer, Scanner scanner) {
            while (true) {
                System.out.println("\n" + trainer.getName() + ", choose your Pokémon:");
                List<Pokemon> team = trainer.getTeam();
                for (int i = 0; i < team.size(); i++) {
                    Pokemon pokemon = team.get(i);
                    System.out.printf("%d. %s (HP: %d/%d)%n",
                            i + 1,
                            pokemon.getName(),
                            pokemon.getHealthPoints(),
                            pokemon.getHealthPoints()
                    );
                }

                int choice = scanner.nextInt() - 1;
                if (choice >= 0 && choice < team.size()) {
                    Pokemon chosen = team.get(choice);
                    if (chosen.isAlive()) {
                        return chosen;
                    } else {
                        System.out.println("That Pokémon has fainted! Choose another one.");
                    }
                } else {
                    System.out.println("Invalid choice! Try again.");
                }
            }

        }
        public static boolean hasAdvantage(String attackType, String defenderType) {
            return switch (attackType) {
                case "Fire" -> defenderType.equals("Grass") || defenderType.equals("Steel");
                case "Water" -> defenderType.equals("Fire");
                case "Grass" -> defenderType.equals("Water");
                case "Electric" -> defenderType.equals("Water");
                case "Psychic" -> defenderType.equals("Fighting");
                case "Fighting" -> defenderType.equals("Dark") || defenderType.equals("Steel");
                case "Dark" -> defenderType.equals("Psychic");
                case "Steel" -> defenderType.equals("Dragon");
                case "Dragon" -> defenderType.equals("Dragon");
                default -> false;
            };
        }


    }

    public static class BattleManager {
        private static final double TYPE_ADVANTAGE_BONUS = 1.3;

        public static Pokemon determineFirstAttacker(Pokemon pokemon1, Pokemon pokemon2) {
            return pokemon1.getSpeed() >= pokemon2.getSpeed() ? pokemon1 : pokemon2;
        }

        public static int calculateDamage(Attack attack, Pokemon attacker, Pokemon defender) {
            // Base damage calculation
            double baseDamage = attack.getPower();

            // Type advantage
            if (hasAdvantage(attack.getType(), defender.getType())) {
                baseDamage *= TYPE_ADVANTAGE_BONUS;
            }

            // Apply defense reduction based on attack type
            if (attack.getDamageType() == Attack.DamageType.PHYSICAL) {
                baseDamage = baseDamage * 100 / (100 + defender.getDefense());
            } else { // SPECIAL
                baseDamage = baseDamage * 100 / (100 + defender.getDefenseEspecial());
            }

            return Math.max(1, (int) baseDamage);
        }
    }

    public static class TeamBuilder {

        public static void createTeam(Scanner scanner, Trainer trainer) {
            System.out.println("Trainer: " + trainer.getName());

            for (int i = 0; i < 3; i++) {
                System.out.println("- Choose Pokémon " + (i + 1));
                Name[] names = Name.values();
                for (int j = 0; j < names.length; j++) {
                    System.out.println((j + 1) + ". " + names[j]);
                }

                int choice = scanner.nextInt();
                Name selectedName = names[choice - 1];
                String type = selectedName.getType();

                System.out.print("Enter HP for " + selectedName + ": ");
                int hp = scanner.nextInt();

                System.out.println("Enter the defense of " + selectedName + ": ");
                int defense = scanner.nextInt();

                System.out.println("Enter the defense especial of " + selectedName + ": ");
                int defenseEspecial = scanner.nextInt();

                System.out.println("Enter the speed of " + selectedName + ": ");
                int speed = scanner.nextInt();


                List<AttackName> availableAttacks = getAttacksByType(type);
                System.out.println("\nAvailable attacks for type: " + type);
                for (int j = 0; j < availableAttacks.size(); j++) {
                    AttackName a = availableAttacks.get(j);
                    System.out.println((j + 1) + ". " + a.getAttackName() + " (Power: " + a.getPower() + ", Type: " + a.getDamageType() + ")");
                }

                List<Attack> selectedAttacks = new ArrayList<>();
                System.out.println("Choose 4 attacks by number:");
                for (int j = 0; j < 4; j++) {
                    System.out.print("Attack " + (j + 1) + ": ");
                    int attackIndex = scanner.nextInt() - 1;
                    while (attackIndex < 0 || attackIndex >= availableAttacks.size()) {
                        System.out.print("Invalid choice. Choose again: ");
                        attackIndex = scanner.nextInt() - 1;
                    }
                    AttackName chosen = availableAttacks.get(attackIndex);
                    selectedAttacks.add(new Attack(
                            chosen.getAttackName(),
                            chosen.getType(),
                            chosen.getDamageType(),
                            chosen.getPower()
                    ));
                    availableAttacks.remove(attackIndex);
                }

                Pokemon pokemon = new Pokemon(selectedName, hp, defense, defenseEspecial, speed);
                for (Attack a : selectedAttacks) {
                    pokemon.addAttack(a);
                }

                trainer.addPokemon(pokemon);
            }
        }

        public static void generateRandomTeam(Trainer trainer) {
            Random random = new Random();
            Name[] allNames = Name.values();

            for (int i = 0; i < 3; i++) {
                Name randomName = allNames[random.nextInt(allNames.length)];
                String type = randomName.getType();
                int hp = 100 + random.nextInt(101);
                int defense = 100 + random.nextInt(101);
                int defenseEspecial = 100 + random.nextInt(101);
                int speed = 100 + random.nextInt(101);

                List<AttackName> attacksByType = getAttacksByType(type);
                Collections.shuffle(attacksByType);

                List<Attack> selectedAttacks = new ArrayList<>();
                for (int j = 0; j < 4 && j < attacksByType.size(); j++) {
                    AttackName attack = attacksByType.get(j);
                    selectedAttacks.add(new Attack(
                            attack.getAttackName(),
                            attack.getType(),
                            attack.getDamageType(),
                            attack.getPower()
                    ));
                }

                Pokemon pokemon = new Pokemon(randomName, hp, defense, defenseEspecial, speed);
                for (Attack a : selectedAttacks) {
                    pokemon.addAttack(a);
                }

                trainer.addPokemon(pokemon);
            }
        }

        private static List<AttackName> getAttacksByType(String type) {
            List<AttackName> filtered = new ArrayList<>();
            for (AttackName attack : AttackName.values()) {
                if (attack.getType().equalsIgnoreCase(type)) {
                    filtered.add(attack);
                }
            }
            return filtered.subList(0, Math.min(7, filtered.size()));
        }
    }
}
