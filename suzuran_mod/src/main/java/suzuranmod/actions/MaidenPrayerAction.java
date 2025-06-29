package suzuranmod.actions;

import java.util.ArrayList;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class MaidenPrayerAction extends AbstractGameAction {
    private int numberOfCards;

    public MaidenPrayerAction(int numberOfCards) {
        this.numberOfCards = numberOfCards;
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = Settings.ACTION_DUR_FAST;
    }

    @Override
    public void update() {
        AbstractPlayer p = AbstractDungeon.player;
        
        // 检查手牌是否已满
        if (p.hand.size() == 10) {
            p.createHandIsFullDialog();
            this.isDone = true;
            return;
        }

        // 检查消耗堆是否为空
        if (p.exhaustPile.isEmpty()) {
            this.isDone = true;
            return;
        }

        // 筛选出可以召回的卡牌（排除状态牌和诅咒牌）
        ArrayList<AbstractCard> validCards = new ArrayList<>();
        for (AbstractCard card : p.exhaustPile.group) {
            if (card.type != AbstractCard.CardType.STATUS && card.type != AbstractCard.CardType.CURSE) {
                validCards.add(card);
            }
        }

        // 如果没有有效的卡牌可以召回
        if (validCards.isEmpty()) {
            this.isDone = true;
            return;
        }

        // 计算实际可以选择的卡牌数量
        int cardsToSelect = Math.min(this.numberOfCards, validCards.size());
        
        // 随机选择卡牌
        ArrayList<AbstractCard> selectedCards = new ArrayList<>();
        ArrayList<AbstractCard> validCardsCopy = new ArrayList<>(validCards);
        
        for (int i = 0; i < cardsToSelect; i++) {
            AbstractCard randomCard = validCardsCopy.get(AbstractDungeon.cardRandomRng.random(validCardsCopy.size() - 1));
            selectedCards.add(randomCard);
            validCardsCopy.remove(randomCard);
        }
        
        // 将选中的卡牌回到手牌并设置为0费
        for (AbstractCard card : selectedCards) {
            card.unfadeOut();
            card.unhover();
            card.fadingOut = false;
            
            p.hand.addToHand(card);
            card.setCostForTurn(0);
            card.isCostModified = true;
            
            p.exhaustPile.removeCard(card);
        }
        
        p.hand.refreshHandLayout();
        this.isDone = true;
    }
}