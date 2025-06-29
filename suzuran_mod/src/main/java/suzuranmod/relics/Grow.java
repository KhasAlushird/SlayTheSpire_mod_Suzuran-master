package suzuranmod.relics;

import java.util.ArrayList;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.powers.DrawCardNextTurnPower;
import com.megacrit.cardcrawl.powers.EnergizedPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.MonsterRoomElite;
import com.megacrit.cardcrawl.ui.campfire.AbstractCampfireOption;

import basemod.abstracts.CustomRelic;
import suzuranmod.helpers.IdHelper;
import suzuranmod.helpers.ImageHelper;
import suzuranmod.modcore.FoxfirePanel;
import suzuranmod.options.OfudaCampfireOption;
import suzuranmod.powers.OfudaCompensationPower;

public class Grow extends CustomRelic {
    public static final String ID = IdHelper.makePath("Grow");
    private static final String IMG_PATH = ImageHelper.getOtherImgPath("relics", "Grow");
    private static final RelicTier RELIC_TIER = RelicTier.STARTER;
    private static final LandingSound LANDING_SOUND = LandingSound.CLINK;
    private boolean usedThisCombat = false;

    public Grow() {
        super(ID, ImageMaster.loadImage(IMG_PATH), RELIC_TIER, LANDING_SOUND);
    }

    @Override
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    @Override
    public void atPreBattle() {
        usedThisCombat = false;
        this.grayscale = false;
        this.pulse = true;
        beginPulse();
    }


    public void trigger(boolean natural_down){
          if(usedThisCombat){
            return;
        }
         
        if (AbstractDungeon.player != null && FoxfirePanel.getCurrentEnergy() <= 1) {
            // 只在首次获得时触发
            this.flash();
            this.pulse = false;
            addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            if(natural_down){
                addToBot(new com.megacrit.cardcrawl.actions.common.ApplyPowerAction(
                    AbstractDungeon.player, AbstractDungeon.player, new EnergizedPower(AbstractDungeon.player,3), 3
                ));

                addToBot(new com.megacrit.cardcrawl.actions.common.ApplyPowerAction(
                    AbstractDungeon.player, AbstractDungeon.player, new DrawCardNextTurnPower(AbstractDungeon.player,3), 3
                ));

                
            }
            else{
                addToTop(new GainEnergyAction(3));
                addToTop(new DrawCardAction(AbstractDungeon.player, 3));    
            }

            usedThisCombat = true;
            this.grayscale = true;
        }
    }

    
    // @Override
    // public void atTurnStart(){
    //     if(usedThisCombat){
    //         return;
    //     }
    //     if (AbstractDungeon.player != null && FoxfirePanel.getCurrentEnergy() <= 1) {
    //         // 只在首次获得时触发
    //         this.flash();
    //         this.pulse = false;
    //         addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, this));
    //         addToTop(new GainEnergyAction(3));
    //         addToTop(new DrawCardAction(AbstractDungeon.player, 3));
    //         usedThisCombat = true;
    //         this.grayscale = true;
    //     }
    // }
    // @Override
    // public void onCardDraw(AbstractCard card) {
    //     if (!usedThisCombat && (card.type == AbstractCard.CardType.CURSE || card.type == AbstractCard.CardType.STATUS)) {
    //         flash();
    //         this.pulse = false;
    //         addToTop(new DrawCardAction(AbstractDungeon.player, 3));
    //         addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, this));
    //         usedThisCombat = true;
    //         this.grayscale = true;
    //     }
    // }

    @Override
    public void atBattleStart() {
        AbstractCreature p = AbstractDungeon.player;
        super.atBattleStart();
        addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        usedThisCombat = false;
        this.grayscale = false;
         if (AbstractDungeon.getCurrRoom() instanceof MonsterRoomElite) {
            addToBot(new ApplyPowerAction(
            AbstractDungeon.player, AbstractDungeon.player, 
            new OfudaCompensationPower(AbstractDungeon.player, 1), 1
        ));
        }

    }

    @Override
    public void onVictory() {
        this.pulse = false;
        usedThisCombat = false;
        this.grayscale = false;
    }

    @Override
    public void addCampfireOption(ArrayList<AbstractCampfireOption> options) {
        if(AbstractDungeon.player.hasRelic("SuzuranKhas:WakanCrystal")){
            options.add(new OfudaCampfireOption(true));
        }
        else{
            options.add(new OfudaCampfireOption(false));
        }
        
  }

    @Override
    public AbstractRelic makeCopy() {
        return new Grow();
    }
}