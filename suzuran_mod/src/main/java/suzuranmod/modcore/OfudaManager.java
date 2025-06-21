package suzuranmod.modcore;

public class OfudaManager {
    private static int ofuda = 0;
    public static final int OFUDA_MAX = 12; 

    public static int getOfuda() {
        return ofuda;
    }

    public static void setOfuda(int amount) {
        ofuda = Math.max(0, Math.min(amount, OFUDA_MAX));
    }

    public static void addOfuda(int amount) {
        setOfuda(ofuda + amount);
    }

    public static void loseOfuda(int amount) {
        setOfuda(ofuda - amount);
    }
}