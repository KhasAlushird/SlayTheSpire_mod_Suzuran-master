package suzuranmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;

import suzuranmod.modcore.FoxfirePanel;

public class FFHazeAction extends AbstractGameAction {
    private AbstractPlayer p;
    private boolean freeToPlayOnce;
    private int energyOnUse;
    private boolean openedSelection = false;
    private boolean upgraded = false;

    public FFHazeAction(AbstractPlayer p, boolean freeToPlayOnce, int energyOnUse,boolean upgraded) {
        this.p = p;
        this.freeToPlayOnce = freeToPlayOnce;
        this.duration = Settings.ACTION_DUR_FAST;
        this.actionType = ActionType.CARD_MANIPULATION;
        this.energyOnUse = energyOnUse;
        this.upgraded = upgraded;
    }

    @Override
    public void update() {
        int effect =0;
        if (this.energyOnUse != -1) {
            effect = this.energyOnUse;
        }
        if (this.p.hasRelic("Chemical X")) {
            effect += 2;
            this.p.getRelic("Chemical X").flash();
        }

        if (!openedSelection) {
            if (effect > 0 && p.hand.size() > 0) {
                AbstractDungeon.handCardSelectScreen.open("选择要消耗的牌", effect, true, true);
                addToBot(new WaitAction(0.25F));
                openedSelection = true;
                return;
            } else {
                this.isDone = true;
                return;
            }
        }

        if (!AbstractDungeon.handCardSelectScreen.wereCardsRetrieved) {
            int exhaustCount = 0;
            for (AbstractCard card : AbstractDungeon.handCardSelectScreen.selectedCards.group) {
                p.hand.moveToExhaustPile(card);
                exhaustCount++;
            }
            if(this.upgraded){
                exhaustCount ++;
            }
            if (exhaustCount > 0) {
                FoxfirePanel.addEnergy(exhaustCount);
                addToTop(new ApplyPowerAction(p, p, new StrengthPower(p, 2 * exhaustCount), 2 * exhaustCount));
            }
            AbstractDungeon.handCardSelectScreen.wereCardsRetrieved = true;
            AbstractDungeon.handCardSelectScreen.selectedCards.group.clear();

            if (!this.freeToPlayOnce) {
                this.p.energy.use(EnergyPanel.totalCount);
            }
            this.isDone = true;
        }
        tickDuration();
    }
}