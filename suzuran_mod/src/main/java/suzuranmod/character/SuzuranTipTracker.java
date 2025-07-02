package suzuranmod.character;

import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.Prefs;
import com.megacrit.cardcrawl.helpers.SaveHelper;
import com.megacrit.cardcrawl.helpers.TipTracker;

public class SuzuranTipTracker {
    // 提示键名
    public static final String SUZURAN_FOXFIRE_TIP = "SUZURAN_FOXFIRE_TIP";
    public static final String SUZURAN_OFUDA_TIP = "SUZURAN_OFUDA_TIP";
    
    // 使用Prefs而不是Preferences
    private static Prefs pref;
    
    // 初始化方法，在TheCore中调用
    public static void initialize() {
        pref = SaveHelper.getPrefs("SuzuranTips"); // 创建自己的偏好设置文件
        
        // 向TipTracker添加我们的提示键
        TipTracker.tips.put(SUZURAN_FOXFIRE_TIP, pref.getBoolean(SUZURAN_FOXFIRE_TIP, false));
        TipTracker.tips.put(SUZURAN_OFUDA_TIP, pref.getBoolean(SUZURAN_OFUDA_TIP, false));
    }
    
    // 检查是否应该显示狐火提示 - 第一场战斗
    public static void checkFoxfireTip() {
        if (!TipTracker.tips.containsKey(SUZURAN_FOXFIRE_TIP)) {
            // 如果键不存在，重新初始化
            initialize();
        }
        
        if (!((Boolean)TipTracker.tips.get(SUZURAN_FOXFIRE_TIP)).booleanValue()) {
            showFoxfireTip();
            neverShowAgain(SUZURAN_FOXFIRE_TIP);
        }
    }
    
    // 检查是否应该显示灵符提示 - 第二场战斗
    public static void checkOfudaTip() {
        if (!TipTracker.tips.containsKey(SUZURAN_OFUDA_TIP)) {
            // 如果键不存在，重新初始化
            initialize();
        }
        
        if (!((Boolean)TipTracker.tips.get(SUZURAN_OFUDA_TIP)).booleanValue()) {
            showOfudaTip();
            neverShowAgain(SUZURAN_OFUDA_TIP);
        }
    }
    
    // 模仿原版的neverShowAgain方法
    private static void neverShowAgain(String key) {
        if (pref == null) {
            initialize();
        }
        
        pref.putBoolean(key, true);
        TipTracker.tips.put(key, true);
        pref.flush();
    }
    
    private static void showFoxfireTip() {
        try {
            // 获取本地化文本
            String[] tipText = CardCrawlGame.languagePack.getUIString("SuzuranKhas:Tips").TEXT;
            
            // 使用TalkAction显示狐火机制提示
            AbstractDungeon.actionManager.addToBottom(
                new TalkAction(true, tipText[0], 6.0F, 6.0F)
            );
            
        } catch (Exception e) {
            // 如果本地化失败，使用默认文本
            AbstractDungeon.actionManager.addToBottom(
                new TalkAction(true, "Foxfire Mechanic NL Foxfire is Suzuran's core resource! Start with 7, decreases by 1 each turn.", 6.0F, 6.0F)
            );
        }
    }
    
    private static void showOfudaTip() {
        try {
            String[] tipText = CardCrawlGame.languagePack.getUIString("SuzuranKhas:Tips").TEXT;
            
            // 使用TalkAction显示灵符机制提示
            AbstractDungeon.actionManager.addToBottom(
                new TalkAction(true, tipText[1], 7.0F, 7.0F)
            );
            
        } catch (Exception e) {
            AbstractDungeon.actionManager.addToBottom(
                new TalkAction(true, "Ofuda System NL Collect Ofuda through various means to exchange for rewards after combat.", 7.0F, 7.0F)
            );
        }
    }
    
    // 重置所有提示（调试用）
    public static void resetAllTips() {
        if (pref == null) {
            initialize();
        }
        
        pref.putBoolean(SUZURAN_FOXFIRE_TIP, false);
        pref.putBoolean(SUZURAN_OFUDA_TIP, false);
        pref.flush();
        
        TipTracker.tips.put(SUZURAN_FOXFIRE_TIP, false);
        TipTracker.tips.put(SUZURAN_OFUDA_TIP, false);
    }
}