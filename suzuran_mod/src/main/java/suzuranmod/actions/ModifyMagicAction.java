package suzuranmod.actions;

import java.util.UUID;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.helpers.GetAllInBattleInstances;

public class ModifyMagicAction extends AbstractGameAction {
    private UUID uuid;
    private int amount;

    public ModifyMagicAction(UUID targetUUID, int amount) {
        this.uuid = targetUUID;
        this.amount = amount;
    }

    @Override
    public void update() {
        for (AbstractCard c : GetAllInBattleInstances.get(this.uuid)) {
            c.baseMagicNumber += this.amount;
            c.magicNumber = c.baseMagicNumber;
            if (c.baseMagicNumber < 0)
                c.baseMagicNumber = 0;
        }
        this.isDone = true;
    }
}