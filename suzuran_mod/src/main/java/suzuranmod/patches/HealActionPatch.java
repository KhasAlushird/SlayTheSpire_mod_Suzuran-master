package suzuranmod.patches;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import suzuranmod.character.SuzuranCard;

@SpirePatch(clz = HealAction.class, method = "update")
public class HealActionPatch {
    @SpirePostfixPatch
    public static void afterHeal(HealAction __instance) {
        int healAmount = __instance.amount;
        if (__instance.isDone&&healAmount > 0 && AbstractDungeon.player != null) {
            // 遍历所有牌堆
            for (AbstractCard c : AbstractDungeon.player.hand.group) {
                if (c instanceof SuzuranCard) {
                    ((SuzuranCard) c).onPlayerHeal(healAmount);
                }
            }
            for (AbstractCard c : AbstractDungeon.player.drawPile.group) {
                if (c instanceof SuzuranCard) {
                    ((SuzuranCard) c).onPlayerHeal(healAmount);
                }
            }
            for (AbstractCard c : AbstractDungeon.player.discardPile.group) {
                if (c instanceof SuzuranCard) {
                    ((SuzuranCard) c).onPlayerHeal(healAmount);
                }
            }
            for (AbstractCard c : AbstractDungeon.player.exhaustPile.group) {
                if (c instanceof SuzuranCard) {
                    ((SuzuranCard) c).onPlayerHeal(healAmount);
                }
            }
        }
    }
}