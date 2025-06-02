package suzuranmod.modcore;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.OverlayMenu;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class FoxfirePatch {
    public static FoxfirePanel foxfirePanel = null;

    public static void initFoxfire() {
        FoxfirePanel.totalCount = 7;
        System.out.println("[FoxfirePatch] initFoxfire called, totalCount set to 7");
    }

    public static void onEndOfTurn() {
        if (FoxfirePanel.totalCount > 0) {
            FoxfirePanel.totalCount--;
            System.out.println("[FoxfirePatch] onEndOfTurn called, totalCount now " + FoxfirePanel.totalCount);
        }
    }

    @SpirePatch(clz = AbstractPlayer.class, method = "applyStartOfCombatLogic")
    public static class StartCombatPatch {
        @SpirePostfixPatch
        public static void Postfix(AbstractPlayer __instance) {
            System.out.println("[FoxfirePatch] StartCombatPatch triggered");
            FoxfirePatch.initFoxfire();
        }
    }

    @SpirePatch(clz = GameActionManager.class, method = "callEndOfTurnActions")
    public static class EndTurnPatch {
        @SpireInsertPatch(rloc = 8)
        public static void Insert(GameActionManager __instance) {
            System.out.println("[FoxfirePatch] EndTurnPatch triggered");
            FoxfirePatch.onEndOfTurn();
        }
    }

    @SpirePatch(clz = OverlayMenu.class, method = "render")
    public static class OverlayRenderPatch {
        @SpireInsertPatch(rloc = 6)
        public static void Insert(OverlayMenu __instance, SpriteBatch sb) {
            if (AbstractDungeon.player == null) {
                System.out.println("[FoxfirePatch] OverlayRenderPatch: player is null, skip render");
                return;
            }
            if (FoxfirePatch.foxfirePanel == null) {
                System.out.println("[FoxfirePatch] OverlayRenderPatch: foxfirePanel is null, creating new");
                FoxfirePatch.foxfirePanel = new FoxfirePanel();
            }
            FoxfirePatch.foxfirePanel.update();
            FoxfirePatch.foxfirePanel.render(sb);
            System.out.println("[FoxfirePatch] OverlayRenderPatch: foxfirePanel.render called");
        }
    }
}