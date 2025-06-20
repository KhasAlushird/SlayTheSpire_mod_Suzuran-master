package suzuranmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.actions.unique.RegenAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import suzuranmod.character.SuzuranCard;

@SpirePatch(clz = RegenAction.class, method = "update")
public class RegenActionPatch {
    @SpirePostfixPatch
    public static void afterRegen(RegenAction __instance) {
        int healAmount = __instance.amount;
        if (__instance.isDone && healAmount > 0 && AbstractDungeon.player != null && __instance.target == AbstractDungeon.player) {
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