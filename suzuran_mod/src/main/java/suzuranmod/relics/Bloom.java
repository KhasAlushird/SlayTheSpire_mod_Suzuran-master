package suzuranmod.relics;

import java.util.ArrayList;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import static com.megacrit.cardcrawl.events.AbstractEvent.logMetricRelicSwap;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.powers.DrawCardNextTurnPower;
import com.megacrit.cardcrawl.powers.EnergizedPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.ui.campfire.AbstractCampfireOption;

import basemod.abstracts.CustomRelic;
import suzuranmod.helpers.IdHelper;
import suzuranmod.helpers.ImageHelper;
import suzuranmod.options.OfudaCampfireOption;

public class Bloom extends CustomRelic {
    public static final String ID = IdHelper.makePath("Bloom");
    private static final String IMG_PATH = ImageHelper.getOtherImgPath("relics", "Bloom");
    private static final RelicTier RELIC_TIER = RelicTier.BOSS;
    private static final LandingSound LANDING_SOUND = LandingSound.FLAT;
    private boolean usedThisCombat1 = false;
    private boolean usedThisCombat2 = false;

    public Bloom() {
        super(ID, ImageMaster.loadImage(IMG_PATH), RELIC_TIER, LANDING_SOUND);
    }

    @Override
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    @Override
    public void atPreBattle() {
        usedThisCombat1 = false;
        usedThisCombat2 = false;
        this.grayscale = false;
        this.pulse = true;
        beginPulse();
    }


    public void trigger1(boolean natural_down) {
        if (usedThisCombat1) return;
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

        usedThisCombat1 = true;
        if (usedThisCombat2) {
            this.grayscale = true;
        }
    }

    public void trigger3(boolean natural_down) {
        if (usedThisCombat2) return ; 
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

        usedThisCombat2 = true;
        if (usedThisCombat1) {
            this.grayscale = true;
        }
    }

    @Override
    public void atBattleStart() {
        AbstractCreature p = AbstractDungeon.player;
        super.atBattleStart();
        addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        usedThisCombat1 = false;
        usedThisCombat2 = false;
        this.grayscale = false;
    }

    @Override
    public void onVictory() {
        this.pulse = false;
        usedThisCombat1 = false;
        usedThisCombat2 = false;
        this.grayscale = false;
    }

    @Override
    public void onEquip() {
        int relicAtIndex = -1;
        int relicsize = AbstractDungeon.player.relics.size();
        for (int i = 0; i < relicsize; i++) {
            if (AbstractDungeon.player.relics.get(i).relicId.equals("SuzuranKhas:Grow")) {
                relicAtIndex = i;
                break;
            }
        }
        if (relicAtIndex != -1) {
            AbstractDungeon.player.relics.get(relicAtIndex).onUnequip();
            AbstractRelic bloom = RelicLibrary.getRelic("SuzuranKhas:Bloom").makeCopy();
            bloom.instantObtain(AbstractDungeon.player, relicAtIndex, false);
            logMetricRelicSwap("Bloom", "Swapped Relic", bloom, AbstractDungeon.player.relics.get(relicAtIndex));
        }
        // 检查是否有多余的 Bloom
        int bloomCount = 0;
        int lastBloomIndex = -1;
        for (int i = 0; i < AbstractDungeon.player.relics.size(); i++) {
            if (AbstractDungeon.player.relics.get(i).relicId.equals("SuzuranKhas:Bloom")) {
                bloomCount++;
                lastBloomIndex = i;
            }
        }
        // 如果有多余的 Bloom，将最后一个替换为 Circlet
        if (bloomCount > 1 && lastBloomIndex != -1) {
            AbstractDungeon.player.relics.get(lastBloomIndex).onUnequip();
            AbstractRelic circlet = RelicLibrary.getRelic("Circlet").makeCopy();
            circlet.instantObtain(AbstractDungeon.player, lastBloomIndex, false);
            logMetricRelicSwap("Bloom", "Swapped Relic", circlet, AbstractDungeon.player.relics.get(lastBloomIndex));
        }
    }

    @Override
    public boolean canSpawn() {
        return AbstractDungeon.player.hasRelic("SuzuranKhas:Grow");
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
        return new Bloom();
    }
}