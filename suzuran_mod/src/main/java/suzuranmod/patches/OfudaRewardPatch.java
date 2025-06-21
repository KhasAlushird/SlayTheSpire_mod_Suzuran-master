package suzuranmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

import suzuranmod.modcore.OfudaManager;
import suzuranmod.rewards.OfudaRewardItem;

public class OfudaRewardPatch {
    @SpirePatch(clz = AbstractRoom.class, method = "endBattle")
    public static class AddOfudaReward {
        @SpirePostfixPatch
        public static void addOfudaReward(AbstractRoom __instance) {
            if (OfudaManager.getOfuda() > 0 && AbstractDungeon.getCurrRoom().rewards != null) {
                AbstractDungeon.getCurrRoom().rewards.add(new OfudaRewardItem(OfudaManager.getOfuda()));
            }
        }
    }
}