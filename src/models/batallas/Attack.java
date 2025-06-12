package models.batallas;

public class Attack {
    public enum DamageType {
        PHYSICAL, SPECIAL
    }

    private String name;
    private DamageType damageType;
    private int power;
    private String type;

    //builder
    public Attack(String name, String type, String damageType, int power) {
        this.name = name;
        this.damageType = DamageType.valueOf(damageType.toUpperCase());
        this.power = power;
        this.type = type;
    }

    // Getters
    public String getName() {
        return name;
    }

    public String getType(){
        return type;
    }

    public DamageType getDamageType() {
        return damageType;
    }

    public int getPower() {
        return power;
    }

    // Display
    @Override
    public String toString() {
        return name + " | " + damageType + " | Power: " + power;
    }
}

