package suzuranmod.relics;

import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import basemod.abstracts.CustomRelic;
import suzuranmod.helpers.IdHelper;
import suzuranmod.helpers.ImageHelper;
import suzuranmod.modcore.FoxfirePanel;

public class TheFire extends CustomRelic {
    public static final String ID = IdHelper.makePath("TheFire");
    private static final String IMG_PATH = ImageHelper.getOtherImgPath("relics", "TheFire");
    private static final RelicTier RELIC_TIER = RelicTier.COMMON;
    private static final LandingSound LANDING_SOUND = LandingSound.HEAVY;

    private int turnCounter = 0;

    public TheFire() {
        super(ID, ImageMaster.loadImage(IMG_PATH), RELIC_TIER, LANDING_SOUND);
    }

    @Override
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    @Override
    public void atBattleStart() {
        turnCounter = 0;
        this.grayscale = false;
    }

    @Override
    public void atTurnStart() {
        turnCounter++;
        if (turnCounter == 3) {
            this.flash();
            FoxfirePanel.addEnergy(1);
            this.grayscale = true;
        }
    }

    @Override
    public void onVictory() {
        turnCounter = 0;
        this.grayscale = false;
    }

    @Override
    public AbstractRelic makeCopy() {
        return new TheFire();
    }
}