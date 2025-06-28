package suzuranmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;

import suzuranmod.modcore.FoxfirePanel;

public class IncinerationAction extends AbstractGameAction {
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString("ExhaustAction");
    public static final String[] TEXT = uiStrings.TEXT;
    
    private AbstractPlayer p;

    public IncinerationAction() {
        this.p = AbstractDungeon.player;
        this.amount = 1; // 只选择1张牌
        this.duration = this.startDuration = Settings.ACTION_DUR_FAST;
        this.actionType = ActionType.EXHAUST;
    }

    @Override
    public void update() {
        if (this.duration == this.startDuration) {
            // 如果手牌为空，直接结束
            if (this.p.hand.size() == 0) {
                this.isDone = true;
                return;
            }
            
            // 打开手牌选择界面，选择1张牌消耗
            AbstractDungeon.handCardSelectScreen.open(TEXT[0], 1, false, false);
            tickDuration();
            return;
        }
        
        // 处理选择的卡牌
        if (!AbstractDungeon.handCardSelectScreen.wereCardsRetrieved) {
            for (AbstractCard c : AbstractDungeon.handCardSelectScreen.selectedCards.group) {
                // 移动到消耗堆
                this.p.hand.moveToExhaustPile(c);
                
                // 检查卡牌类型，如果是攻击牌或诅咒牌，获得1点狐火
                if (c.type == AbstractCard.CardType.ATTACK || c.type == AbstractCard.CardType.CURSE) {
                    FoxfirePanel.addEnergy(1, true);
                }
            }
            
            CardCrawlGame.dungeon.checkForPactAchievement();
            AbstractDungeon.handCardSelectScreen.wereCardsRetrieved = true;
        }
        
        tickDuration();
    }
}