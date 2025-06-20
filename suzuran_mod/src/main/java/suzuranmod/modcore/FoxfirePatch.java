package suzuranmod.modcore;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.OverlayMenu;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class FoxfirePatch {
    public static FoxfirePanel foxfirePanel = null;

    // public static void initFoxfire() {
    //     FoxfirePanel.totalCount = 7;
    //     System.out.println("[FoxfirePatch] initFoxfire called, totalCount set to 7");
    // }

    public static void initFoxfire() {
        if (AbstractDungeon.getCurrRoom() == null || AbstractDungeon.getMonsters() == null) {
        // System.out.println("[FoxfirePatch] initFoxfire: 当前房间或怪物列表为null，跳过初始化");
        return;
    }
        int initialFoxfire = 7;
        boolean isEliteOrBoss = (AbstractDungeon.getCurrRoom()).eliteTrigger;
        for (AbstractMonster m : (AbstractDungeon.getMonsters()).monsters) {
            if (m.type == AbstractMonster.EnemyType.BOSS)
                isEliteOrBoss = true;
        }
        if (isEliteOrBoss) {
            initialFoxfire += 3;
        }
        foxfirePanel = new FoxfirePanel(initialFoxfire);
        // System.out.println("[FoxfirePatch] 新建 FoxfirePanel, 初始狐火: " + initialFoxfire);
    }

    public static void onEndOfTurn() {
        if (FoxfirePanel.totalCount > 0) {
            FoxfirePanel.useEnergy(1);
            // System.out.println("[FoxfirePatch] onEndOfTurn called, totalCount now " + FoxfirePanel.totalCount);
        }
    }

    @SpirePatch(clz = AbstractPlayer.class, method = "applyStartOfCombatLogic")
    public static class StartCombatPatch {
        @SpirePostfixPatch
        public static void Postfix(AbstractPlayer __instance) {
            // System.out.println("[FoxfirePatch] StartCombatPatch triggered");
            FoxfirePatch.initFoxfire();
        }
    }

    @SpirePatch(clz = GameActionManager.class, method = "callEndOfTurnActions")
    public static class EndTurnPatch {
        @SpireInsertPatch(rloc = 8)
        public static void Insert(GameActionManager __instance) {
            // System.out.println("[FoxfirePatch] EndTurnPatch triggered");
            FoxfirePatch.onEndOfTurn();
        }
    }

    @SpirePatch(clz = OverlayMenu.class, method = "render")
    public static class OverlayRenderPatch {
        @SpireInsertPatch(rloc = 6)
        public static void Insert(OverlayMenu __instance, SpriteBatch sb) {
            if (AbstractDungeon.player == null) {
                // System.out.println("[FoxfirePatch] OverlayRenderPatch: player is null, skip render");
                return;
            }
            if (FoxfirePatch.foxfirePanel == null) {
                // System.out.println("[FoxfirePatch] OverlayRenderPatch: foxfirePanel is null, creating new");
                initFoxfire();
            }
            if(FoxfirePatch.foxfirePanel != null){
                FoxfirePatch.foxfirePanel.update();
                FoxfirePatch.foxfirePanel.render(sb);
            }

            // System.out.println("[FoxfirePatch] OverlayRenderPatch: foxfirePanel.render called");
        }
    }
}