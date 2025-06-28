package suzuranmod.relics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Random;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.vfx.UpgradeShineEffect;
import com.megacrit.cardcrawl.vfx.cardManip.PurgeCardEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;

import basemod.abstracts.CustomRelic;
import suzuranmod.helpers.IdHelper;
import suzuranmod.helpers.ImageHelper;

public class NineTails extends CustomRelic {
    public static final String ID = IdHelper.makePath("NineTails");
    private static final String IMG_PATH = ImageHelper.getOtherImgPath("relics", "NineTails");
    private static final RelicTier RELIC_TIER = RelicTier.SPECIAL;
    private static final LandingSound LANDING_SOUND = LandingSound.FLAT;
    
    private static final int UPGRADE_AMT = 3;
    private boolean cardsSelected = true;

    public NineTails() {
        super(ID, ImageMaster.loadImage(IMG_PATH), RELIC_TIER, LANDING_SOUND);
    }

    @Override
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    @Override
    public void onEquip() {
        this.cardsSelected = false;
        if (AbstractDungeon.isScreenUp) {
            AbstractDungeon.dynamicBanner.hide();
            AbstractDungeon.overlayMenu.cancelButton.hide();
            AbstractDungeon.previousScreen = AbstractDungeon.screen;
        }
        (AbstractDungeon.getCurrRoom()).phase = AbstractRoom.RoomPhase.INCOMPLETE;
        
        // 删除卡牌部分
        CardGroup tmp = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        for (AbstractCard card : (AbstractDungeon.player.masterDeck.getPurgeableCards()).group) {
            tmp.addToTop(card);
        }
        
        if (tmp.group.isEmpty()) {
            this.cardsSelected = true;
            upgradeCards(); // 直接升级卡牌
            return;
        }
        
        if (tmp.group.size() <= 1) {
            deleteCards(tmp.group);
        } else {
            AbstractDungeon.gridSelectScreen.open(AbstractDungeon.player.masterDeck
                .getPurgeableCards(), 1, this.DESCRIPTIONS[1], false, false, false, true);
        }
    }

    @Override
    public void update() {
        super.update();
        if (!this.cardsSelected && AbstractDungeon.gridSelectScreen.selectedCards.size() == 1) {
            deleteCards(AbstractDungeon.gridSelectScreen.selectedCards);
        }
    }

    public void deleteCards(ArrayList<AbstractCard> group) {
        this.cardsSelected = true;
        float displayCount = 0.0F;
        
        for (Iterator<AbstractCard> i = group.iterator(); i.hasNext(); ) {
            AbstractCard card = i.next();
            card.untip();
            card.unhover();
            AbstractDungeon.topLevelEffects.add(new PurgeCardEffect(card, 
                Settings.WIDTH / 3.0F + displayCount, Settings.HEIGHT / 2.0F));
            displayCount += Settings.WIDTH / 6.0F;
            AbstractDungeon.player.masterDeck.removeCard(card);
        }
        
        AbstractDungeon.gridSelectScreen.selectedCards.clear();
        
        // 删除完成后升级卡牌
        upgradeCards();
        
        (AbstractDungeon.getCurrRoom()).phase = AbstractRoom.RoomPhase.COMPLETE;
    }

    private void upgradeCards() {
        ArrayList<AbstractCard> upgradableCards = new ArrayList<>();
        
        // 收集所有可升级的卡牌（不限制类型）
        for (AbstractCard c : AbstractDungeon.player.masterDeck.group) {
            if (c.canUpgrade()) {
                upgradableCards.add(c);
            }
        }
        
        Collections.shuffle(upgradableCards, new Random(AbstractDungeon.miscRng.randomLong()));
        
        if (!upgradableCards.isEmpty()) {
            int upgradeCount = Math.min(UPGRADE_AMT, upgradableCards.size());
            
            // 升级卡牌
            for (int i = 0; i < upgradeCount; i++) {
                AbstractCard cardToUpgrade = upgradableCards.get(i);
                cardToUpgrade.upgrade();
                AbstractDungeon.player.bottledCardUpgradeCheck(cardToUpgrade);
            }
            
            // 显示升级效果
            if (upgradeCount == 1) {
                AbstractDungeon.topLevelEffects.add(new ShowCardBrieflyEffect(
                    upgradableCards.get(0).makeStatEquivalentCopy()));
            } else if (upgradeCount == 2) {
                AbstractDungeon.topLevelEffects.add(new ShowCardBrieflyEffect(
                    upgradableCards.get(0).makeStatEquivalentCopy(), 
                    Settings.WIDTH / 2.0F - AbstractCard.IMG_WIDTH / 2.0F - 20.0F * Settings.scale, 
                    Settings.HEIGHT / 2.0F));
                AbstractDungeon.topLevelEffects.add(new ShowCardBrieflyEffect(
                    upgradableCards.get(1).makeStatEquivalentCopy(), 
                    Settings.WIDTH / 2.0F + AbstractCard.IMG_WIDTH / 2.0F + 20.0F * Settings.scale, 
                    Settings.HEIGHT / 2.0F));
            } else { // 3张或更多
                AbstractDungeon.topLevelEffects.add(new ShowCardBrieflyEffect(
                    upgradableCards.get(0).makeStatEquivalentCopy(), 
                    Settings.WIDTH / 2.0F - AbstractCard.IMG_WIDTH - 40.0F * Settings.scale, 
                    Settings.HEIGHT / 2.0F));
                AbstractDungeon.topLevelEffects.add(new ShowCardBrieflyEffect(
                    upgradableCards.get(1).makeStatEquivalentCopy(), 
                    Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F));
                AbstractDungeon.topLevelEffects.add(new ShowCardBrieflyEffect(
                    upgradableCards.get(2).makeStatEquivalentCopy(), 
                    Settings.WIDTH / 2.0F + AbstractCard.IMG_WIDTH + 40.0F * Settings.scale, 
                    Settings.HEIGHT / 2.0F));
            }
            
            AbstractDungeon.topLevelEffects.add(new UpgradeShineEffect(
                Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F));
        }
    }

    @Override
    public AbstractRelic makeCopy() {
        return new NineTails();
    }
}