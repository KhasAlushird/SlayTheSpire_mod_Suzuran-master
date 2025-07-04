package suzuranmod.actions;

import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.badlogic.gdx.Gdx;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.EmptyDeckShuffleAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.SoulGroup;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.PlayerTurnEffect;

import suzuranmod.patches.SuzuranCardTagsPatch;

public class DrawFoxfireCardsAction extends AbstractGameAction {
    private boolean shuffleCheck = false;
    private static final Logger logger = LogManager.getLogger(DrawFoxfireCardsAction.class.getName());
    public static ArrayList<AbstractCard> drawnCards = new ArrayList<>();
    private boolean clearDrawHistory = true;
    private AbstractGameAction followUpAction = null;

    public DrawFoxfireCardsAction(AbstractCreature source, int amount, boolean endTurnDraw) {
        if (endTurnDraw) {
            AbstractDungeon.topLevelEffects.add(new PlayerTurnEffect());
        }
        setValues(AbstractDungeon.player, source, amount);
        this.actionType = ActionType.DRAW;
        if (Settings.FAST_MODE) {
            this.duration = Settings.ACTION_DUR_XFAST;
        } else {
            this.duration = Settings.ACTION_DUR_FASTER;
        }
    }

    public DrawFoxfireCardsAction(AbstractCreature source, int amount) {
        this(source, amount, false);
    }

    public DrawFoxfireCardsAction(int amount, boolean clearDrawHistory) {
        this(amount);
        this.clearDrawHistory = clearDrawHistory;
    }

    public DrawFoxfireCardsAction(int amount) {
        this(null, amount, false);
    }

    public DrawFoxfireCardsAction(int amount, AbstractGameAction action) {
        this(amount, action, true);
    }

    public DrawFoxfireCardsAction(int amount, AbstractGameAction action, boolean clearDrawHistory) {
        this(amount, clearDrawHistory);
        this.followUpAction = action;
    }

    @Override
    public void update() {
        if (this.clearDrawHistory) {
            this.clearDrawHistory = false;
            drawnCards.clear();
        }
        
        if (AbstractDungeon.player.hasPower("No Draw")) {
            AbstractDungeon.player.getPower("No Draw").flash();
            endActionWithFollowUp();
            return;
        }
        
        if (this.amount <= 0) {
            endActionWithFollowUp();
            return;
        }

        // 关键修改：计算狐火牌数量而不是总牌数
        int foxfireInDeck = countFoxfireCards(AbstractDungeon.player.drawPile.group);
        int foxfireInDiscard = countFoxfireCards(AbstractDungeon.player.discardPile.group);
        
        if (SoulGroup.isActive()) {
            return;
        }
        
        if (foxfireInDeck + foxfireInDiscard == 0) {
            endActionWithFollowUp();
            return;
        }
        
        if (AbstractDungeon.player.hand.size() == 10) {
            AbstractDungeon.player.createHandIsFullDialog();
            endActionWithFollowUp();
            return;
        }

        if (!this.shuffleCheck) {
            if (this.amount + AbstractDungeon.player.hand.size() > 10) {
                int handSizeAndDraw = 10 - this.amount + AbstractDungeon.player.hand.size();
                this.amount += handSizeAndDraw;
                AbstractDungeon.player.createHandIsFullDialog();
            }
            
            // 如果抽牌堆中狐火牌不够，需要洗牌
            if (this.amount > foxfireInDeck) {
                int tmp = this.amount - foxfireInDeck;
                addToTop(new DrawFoxfireCardsAction(tmp, this.followUpAction, false));
                addToTop(new EmptyDeckShuffleAction());
                if (foxfireInDeck != 0) {
                    addToTop(new DrawFoxfireCardsAction(foxfireInDeck, false));
                }
                this.amount = 0;
                this.isDone = true;
                return;
            }
            this.shuffleCheck = true;
        }

        this.duration -= Gdx.graphics.getDeltaTime();
        if (this.amount != 0 && this.duration < 0.0F) {
            if (Settings.FAST_MODE) {
                this.duration = Settings.ACTION_DUR_XFAST;
            } else {
                this.duration = Settings.ACTION_DUR_FASTER;
            }
            
            this.amount--;
            
            // 关键修改：寻找并抽取狐火牌
            AbstractCard foxfireCard = findFoxfireCard();
            if (foxfireCard != null) {
                drawnCards.add(foxfireCard);
                AbstractDungeon.player.drawPile.removeCard(foxfireCard);
                AbstractDungeon.player.hand.addToTop(foxfireCard);
                AbstractDungeon.player.hand.refreshHandLayout();
            } else {
                logger.warn("No foxfire cards found in drawpile mid-DrawFoxfireCardsAction");
                endActionWithFollowUp();
                return;
            }
            
            if (this.amount == 0) {
                endActionWithFollowUp();
            }
        }
    }

    // 计算指定牌堆中狐火牌的数量
    private int countFoxfireCards(ArrayList<AbstractCard> cardGroup) {
        int count = 0;
        for (AbstractCard card : cardGroup) {
            if (card.hasTag(SuzuranCardTagsPatch.FOXFIRE)||card.hasTag(SuzuranCardTagsPatch.DYINGFLAME)) {
                count++;
            }
        }
        return count;
    }

    // 从抽牌堆中找到一张狐火牌
    private AbstractCard findFoxfireCard() {
        ArrayList<AbstractCard> foxfireCards = new ArrayList<>();
        for (AbstractCard card : AbstractDungeon.player.drawPile.group) {
            if (card.hasTag(SuzuranCardTagsPatch.FOXFIRE)||card.hasTag(SuzuranCardTagsPatch.DYINGFLAME)) {
                foxfireCards.add(card);
            }
        }
        
        if (foxfireCards.isEmpty()) {
            return null;
        }
        
        // 随机选择一张狐火牌
        return foxfireCards.get(AbstractDungeon.cardRandomRng.random(foxfireCards.size() - 1));
    }

    private void endActionWithFollowUp() {
        this.isDone = true;
        if (this.followUpAction != null) {
            addToTop(this.followUpAction);
        }
    }
}