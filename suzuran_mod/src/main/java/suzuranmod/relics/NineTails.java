package suzuranmod.relics;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import basemod.abstracts.CustomRelic;
import suzuranmod.helpers.IdHelper;
import suzuranmod.helpers.ImageHelper;
import suzuranmod.modcore.FoxfirePanel;

public class NineTails extends CustomRelic {
    public static final String ID = IdHelper.makePath("NineTails");
    private static final String IMG_PATH = ImageHelper.getOtherImgPath("relics", "NineTails");
    private static final RelicTier RELIC_TIER = RelicTier.SPECIAL;
    private static final LandingSound LANDING_SOUND = LandingSound.FLAT;

    public NineTails() {
        super(ID, ImageMaster.loadImage(IMG_PATH), RELIC_TIER, LANDING_SOUND);
    }

    @Override
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }


    @Override
    public void atBattleStart() {
        this.flash();
        AbstractDungeon.player.gainEnergy(1);
        FoxfirePanel.addEnergy(1);
    }

    @Override
    public void atTurnStart() {
        this.flash();
        this.addToBot(new DrawCardAction(AbstractDungeon.player, 1));
    }

    @Override
    public AbstractRelic makeCopy() {
        return new NineTails();
    }
}