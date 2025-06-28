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
import com.megacrit.cardcrawl.screens.VictoryScreen;

import suzuranmod.character.Suzuran;
import suzuranmod.relics.SixTails;

public class FoxfirePatch {
    public static FoxfirePanel foxfirePanel = null;

    // public static void initFoxfire() {
    //     FoxfirePanel.totalCount = 7;
    //     System.out.println("[FoxfirePatch] initFoxfire called, totalCount set to 7");
    // }

    public static void initFoxfire() {
        if (!(AbstractDungeon.player instanceof Suzuran)) {
            foxfirePanel = null;
            return;
    }
        if (AbstractDungeon.getCurrRoom() == null || AbstractDungeon.getMonsters() == null) {
            return;
        }
        int initialFoxfire = 7;
        boolean isEliteOrBoss = (AbstractDungeon.getCurrRoom()).eliteTrigger;
        for (AbstractMonster m : (AbstractDungeon.getMonsters()).monsters) {
            if (m.type == AbstractMonster.EnemyType.BOSS)
                isEliteOrBoss = true;
        }

        if (AbstractDungeon.player != null && AbstractDungeon.player.hasRelic(SixTails.ID)) {
            initialFoxfire += 1; // 六尾遗物额外提供1点狐火
        }

        if (isEliteOrBoss) {
            initialFoxfire += 3;
        }
        // 每次都new，保证每场战斗动画都重置
        foxfirePanel = new FoxfirePanel(initialFoxfire);
}

    public static void onEndOfTurn() {
        if (FoxfirePanel.totalCount > 0) {
            FoxfirePanel.useEnergy(1,true);
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
                return;
            }
            if (FoxfirePatch.foxfirePanel == null) {
                // 这里可以不再调用 initFoxfire()，只在战斗开始时 new
                return;
            }
            FoxfirePatch.foxfirePanel.update();
            FoxfirePatch.foxfirePanel.render(sb);
         }
    }

   @SpirePatch(clz = VictoryScreen.class, method = SpirePatch.CONSTRUCTOR)
    public static class FoxfireVictoryPatch {
        public FoxfireVictoryPatch() {
            if (FoxfirePatch.foxfirePanel != null) {
                FoxfirePatch.foxfirePanel.slideOut();
            }
    }
}

   
}