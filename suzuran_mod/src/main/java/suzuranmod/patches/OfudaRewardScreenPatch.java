package suzuranmod.patches;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.screens.CombatRewardScreen;

import suzuranmod.rewards.OfudaRewardItem;

@SpirePatch(clz = CombatRewardScreen.class, method = "setupItemReward")
public class OfudaRewardScreenPatch {
    @SpirePostfixPatch
    public static void moveOfudaRewardToLast(CombatRewardScreen __instance) {
        if (__instance.rewards != null) {
            List<OfudaRewardItem> ofudaList = new ArrayList<>();
            Iterator it = __instance.rewards.iterator();
            while (it.hasNext()) {
                Object r = it.next();
                if (r instanceof OfudaRewardItem) {
                    ofudaList.add((OfudaRewardItem) r);
                    it.remove();
                }
            }
            __instance.rewards.addAll(ofudaList);
        }
    }
}