package models.names;

import models.batallas.Attack; // Importar la clase Attack

public enum AttackName {
    // Grass
    LeafBlade("Leaf Blade", "Grass", Attack.DamageType.PHYSICAL, 90),
    GigaDrain("Giga Drain", "Grass", Attack.DamageType.SPECIAL, 75),
    EnergyBall("Energy Ball", "Grass", Attack.DamageType.SPECIAL, 80),
    VineWhip("Vine Whip", "Grass", Attack.DamageType.PHYSICAL, 45),
    RazorLeaf("Razor Leaf", "Grass", Attack.DamageType.PHYSICAL, 55),
    SolarBeam("Solar Beam", "Grass", Attack.DamageType.SPECIAL, 120),
    SeedBomb("Seed Bomb", "Grass", Attack.DamageType.PHYSICAL, 80),

    // Fire
    Flamethrower("Flamethrower", "Fire", Attack.DamageType.SPECIAL, 90),
    FireBlast("Fire Blast", "Fire", Attack.DamageType.SPECIAL, 110),
    FirePunch("Fire Punch", "Fire", Attack.DamageType.PHYSICAL, 75),
    Ember("Ember", "Fire", Attack.DamageType.SPECIAL, 40),
    HeatWave("Heat Wave", "Fire", Attack.DamageType.SPECIAL, 95),
    FlameWheel("Flame Wheel", "Fire", Attack.DamageType.PHYSICAL, 60),
    LavaPlume("Lava Plume", "Fire", Attack.DamageType.SPECIAL, 80),

    // Water
    Surf("Surf", "Water", Attack.DamageType.SPECIAL, 90),
    HydroPump("Hydro Pump", "Water", Attack.DamageType.SPECIAL, 110),
    WaterGun("Water Gun", "Water", Attack.DamageType.SPECIAL, 40),
    AquaTail("Aqua Tail", "Water", Attack.DamageType.PHYSICAL, 90),
    BubbleBeam("Bubble Beam", "Water", Attack.DamageType.SPECIAL, 65),
    Scald("Scald", "Water", Attack.DamageType.SPECIAL, 80),
    Waterfall("Waterfall", "Water", Attack.DamageType.PHYSICAL, 80),

    // Electric
    Thunderbolt("Thunderbolt", "Electric", Attack.DamageType.SPECIAL, 90),
    Thunder("Thunder", "Electric", Attack.DamageType.SPECIAL, 110),
    Spark("Spark", "Electric", Attack.DamageType.PHYSICAL, 65),
    ThunderShock("ThunderShock", "Electric", Attack.DamageType.SPECIAL, 40),
    WildCharge("Wild Charge", "Electric", Attack.DamageType.PHYSICAL, 90),
    Discharge("Discharge", "Electric", Attack.DamageType.SPECIAL, 80),
    VoltTackle("Volt Tackle", "Electric", Attack.DamageType.PHYSICAL, 120),

    // Psychic
    Psybeam("Psybeam", "Psychic", Attack.DamageType.SPECIAL, 65),
    Psychic("Psychic", "Psychic", Attack.DamageType.SPECIAL, 90),
    ZenHeadbutt("Zen Headbutt", "Psychic", Attack.DamageType.PHYSICAL, 80),
    Confusion("Confusion", "Psychic", Attack.DamageType.SPECIAL, 50),
    FutureSight("Future Sight", "Psychic", Attack.DamageType.SPECIAL, 120),
    Extrasensory("Extrasensory", "Psychic", Attack.DamageType.SPECIAL, 80),
    PsychoCut("Psycho Cut", "Psychic", Attack.DamageType.PHYSICAL, 70),

    // Fighting
    CloseCombat("Close Combat", "Fighting", Attack.DamageType.PHYSICAL, 120),
    KarateChop("Karate Chop", "Fighting", Attack.DamageType.PHYSICAL, 50),
    BrickBreak("Brick Break", "Fighting", Attack.DamageType.PHYSICAL, 75),
    CrossChop("Cross Chop", "Fighting", Attack.DamageType.PHYSICAL, 100),
    FocusPunch("Focus Punch", "Fighting", Attack.DamageType.PHYSICAL, 150),
    DrainPunch("Drain Punch", "Fighting", Attack.DamageType.PHYSICAL, 75),
    MachPunch("Mach Punch", "Fighting", Attack.DamageType.PHYSICAL, 40),

    // Dark
    Bite("Bite", "Dark", Attack.DamageType.PHYSICAL, 60),
    Crunch("Crunch", "Dark", Attack.DamageType.PHYSICAL, 80),
    DarkPulse("Dark Pulse", "Dark", Attack.DamageType.SPECIAL, 80),
    NightSlash("Night Slash", "Dark", Attack.DamageType.PHYSICAL, 70),
    FoulPlay("Foul Play", "Dark", Attack.DamageType.PHYSICAL, 95),
    Thief("Thief", "Dark", Attack.DamageType.PHYSICAL, 60),
    Assurance("Assurance", "Dark", Attack.DamageType.PHYSICAL, 60),

    // Steel
    IronTail("Iron Tail", "Steel", Attack.DamageType.PHYSICAL, 100),
    FlashCannon("Flash Cannon", "Steel", Attack.DamageType.SPECIAL, 80),
    MetalClaw("Metal Claw", "Steel", Attack.DamageType.PHYSICAL, 50),
    BulletPunch("Bullet Punch", "Steel", Attack.DamageType.PHYSICAL, 40),
    SteelWing("Steel Wing", "Steel", Attack.DamageType.PHYSICAL, 70),
    SmartStrike("Smart Strike", "Steel", Attack.DamageType.PHYSICAL, 70),
    IronHead("Iron Head", "Steel", Attack.DamageType.PHYSICAL, 80),

    // Dragon
    DragonClaw("Dragon Claw", "Dragon", Attack.DamageType.PHYSICAL, 80),
    DragonPulse("Dragon Pulse", "Dragon", Attack.DamageType.SPECIAL, 85),
    Outrage("Outrage", "Dragon", Attack.DamageType.PHYSICAL, 120),
    DragonTail("Dragon Tail", "Dragon", Attack.DamageType.PHYSICAL, 60),
    DracoMeteor("Draco Meteor", "Dragon", Attack.DamageType.SPECIAL, 130),
    Twister("Twister", "Dragon", Attack.DamageType.SPECIAL, 40),
    DragonBreath("Dragon Breath", "Dragon", Attack.DamageType.SPECIAL, 60);

    private final String attackName;
    private final String type;
    private final Attack.DamageType damageType; // Cambiado a enum
    private final int power;

    AttackName(String attackName, String type, Attack.DamageType damageType, int power) {
        this.attackName = attackName;
        this.type = type;
        this.damageType = damageType; // Ahora es enum
        this.power = power;
    }

    public String getAttackName() {
        return attackName;
    }

    public String getType() {
        return type;
    }

    public Attack.DamageType getDamageType() { // Devuelve enum
        return damageType;
    }

    public int getPower() {
        return power;
    }
}