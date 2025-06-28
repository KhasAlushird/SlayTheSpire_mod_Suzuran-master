package suzuranmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

import suzuranmod.powers.CircleOfLifePower;

@SpirePatch(
    clz = AbstractCard.class,
    method = "freeToPlay"
)
public class CircleOfLifePatch {
    public static SpireReturn<Boolean> Prefix(AbstractCard __instance) {

        if (AbstractDungeon.player != null && 
            AbstractDungeon.currMapNode != null && 
            (AbstractDungeon.getCurrRoom()).phase == AbstractRoom.RoomPhase.COMBAT && 
            AbstractDungeon.player.hasPower(CircleOfLifePower.POWER_ID) && 
            __instance.type == AbstractCard.CardType.POWER) {
            
            CircleOfLifePower power = (CircleOfLifePower) AbstractDungeon.player.getPower(CircleOfLifePower.POWER_ID);
            if (power.amount > 0) {
                return SpireReturn.Return(true);
            }
        }
        
        return SpireReturn.Continue(); 
    }
}