package suzuranmod.relics;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.powers.IntangiblePlayerPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import basemod.abstracts.CustomRelic;
import suzuranmod.helpers.IdHelper;
import suzuranmod.helpers.ImageHelper;

public class AmuletInArm extends CustomRelic {
    public static final String ID = IdHelper.makePath("AmuletInArm");
    private static final String IMG_PATH = ImageHelper.getOtherImgPath("relics", "AmuletInArm");
    private static final RelicTier RELIC_TIER = RelicTier.RARE;
    private static final LandingSound LANDING_SOUND = LandingSound.CLINK;

    private boolean triggeredThisCombat = false;

    public AmuletInArm() {
        super(ID, ImageMaster.loadImage(IMG_PATH), RELIC_TIER, LANDING_SOUND);
    }

    @Override
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    @Override
    public void atPreBattle() {
        triggeredThisCombat = false;
        this.grayscale = false;
        this.pulse = true;
        beginPulse();
    }

    public void onBurnoutApplied() {
        if (!triggeredThisCombat&& AbstractDungeon.player != null) {
            this.flash();
            this.pulse = false; // 触发后关闭pulse
            addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new IntangiblePlayerPower(AbstractDungeon.player, 1), 1));
            addToTop(new GainBlockAction(AbstractDungeon.player, AbstractDungeon.player, 5));
            triggeredThisCombat = true;
            this.grayscale = true;
        }
    }

    @Override
    public void onVictory() {
        triggeredThisCombat = false;
        this.grayscale = false;
        this.pulse = false;
    }

    @Override
    public AbstractRelic makeCopy() {
        return new AmuletInArm();
    }

}