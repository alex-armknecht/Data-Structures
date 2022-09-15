package main.forneymon.species;

public class Yolomon extends Forneymon {
    public static final int START_HEALTH = 1;
    public static final int DMG_MODIFIER = 10;
    public static final DamageType DT = DamageType.YOLO;
    
    /**
     * Creates a new Forneymon of this species with the given level
     * @param level Level in which to initialize this Forneymon
     */
    public Yolomon (int level) {
        super(START_HEALTH, DT, level);
    }
    
    /**
     * Copy constructor for Yolomon that creates a deep-copy
     * of the given other toCopy
     * @param toCopy The other to copy from
     */
    public Yolomon (Yolomon toCopy) {
        super(toCopy.getHealth(), DT, toCopy.getLevel());
    }
    
    /**
     * @see Forneymon
     * Dampymon take bonus BURNY damage
     */
    @Override
    public int takeDamage (int dmg, DamageType type) {
        if (type == DamageType.BURNY) {
            dmg += DMG_MODIFIER;
        }
        return super.takeDamage(dmg, type);
    }
    
    /**
     * Creates a copy of this Forneymon
     * @return A copy of the calling Forneymon
     */
    @Override
    public Yolomon clone() {
        return new Yolomon(this);
    }

}
