package suzuranmod.modcore;

import basemod.abstracts.CustomSavable;

public class OfudaManager implements CustomSavable<Integer>{
    private static int ofuda = 0;
    public static final int OFUDA_MAX = 12; 
    private static int tailRewardCount = 0;

    // 新增：动画缩放变量
    public static float ofudaScale = 1.0f;
    private static final float SCALE_MAX = 1.4f;
    private static final float SCALE_SPEED = 0.08f;

    public static int getOfuda() {
        return ofuda;
    }

    public static void resetOfuda() {
        setOfuda(0);
    }
    public static void incrementTailRewardCount() {
        tailRewardCount++;
}
    public static int getTailRewardCount() {
        return tailRewardCount;
}

    public static void resetTailRewardCount() {
        tailRewardCount = 0;
    }

    public static void setOfuda(int amount) {
        int old = ofuda;
        ofuda = Math.max(0, Math.min(amount, OFUDA_MAX));
        if (ofuda != old) {
            ofudaScale = SCALE_MAX; // 触发动画
        }
    }

    public static void addOfuda(int amount) {
        setOfuda(ofuda + amount);
    }

    public static void loseOfuda(int amount) {
        setOfuda(ofuda - amount);
    }

    // 新增：每帧缩放恢复
    public static void updateScale() {
        if (ofudaScale > 1.0f) {
            ofudaScale -= SCALE_SPEED;
            if (ofudaScale < 1.0f) ofudaScale = 1.0f;
        }
    }

    @Override
    public Integer onSave() {
         return (tailRewardCount << 16) | (ofuda & 0xFFFF);
    }

    @Override
    public void onLoad(Integer loaded) {
        if (loaded != null) {
            
            ofuda = loaded & 0xFFFF;  // 取低16位
            tailRewardCount = (loaded >> 16) & 0xFFFF;  // 取高16位
        } else {
            ofuda = 0;
            tailRewardCount = 0;
        }
    }
}