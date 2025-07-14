package suzuranmod.relics;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import basemod.abstracts.CustomRelic;
import suzuranmod.helpers.IdHelper;
import suzuranmod.helpers.ImageHelper;

public class SixTails extends CustomRelic {
    public static final String ID = IdHelper.makePath("SixTails");
    private static final String IMG_PATH = ImageHelper.getOtherImgPath("relics", "SixTails");
    private static final RelicTier RELIC_TIER = RelicTier.SPECIAL;
    private static final LandingSound LANDING_SOUND = LandingSound.FLAT;

    public SixTails() {
        super(ID, ImageMaster.loadImage(IMG_PATH), RELIC_TIER, LANDING_SOUND);
    }

    @Override
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    @Override
    public void atTurnStart(){
        flash();
    }

    @Override
    public void onEquip() {
        AbstractDungeon.player.energy.energyMaster++;
  }
    @Override
    public void onUnequip() {
        AbstractDungeon.player.energy.energyMaster--;
  }

    @Override
    public AbstractRelic makeCopy() {
        return new SixTails();
    }
}