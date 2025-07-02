package suzuranmod.powers;

import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

import suzuranmod.helpers.IdHelper;
import suzuranmod.helpers.ImageHelper;
import suzuranmod.modcore.FoxfirePanel;

public class FFafterPower extends AbstractPower {
    public static final String POWER_ID = IdHelper.makePath("FFafterPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public FFafterPower(AbstractCreature owner, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        updateDescription();
        this.type = PowerType.BUFF;
        this.isTurnBased = false;

        String path128 = ImageHelper.getOtherImgPath("powers", "FFafterPower_96");
        String path48 = ImageHelper.getOtherImgPath("powers", "FFafterPower_35");
        this.region128 = new AtlasRegion(ImageMaster.loadImage(path128), 0, 0, 96, 96);
        this.region48 = new AtlasRegion(ImageMaster.loadImage(path48), 0, 0, 35, 35);
    }


    @Override
    public void updateDescription() {
        this.description = String.format(DESCRIPTIONS[0], this.amount);
    }

    @Override
    public void atEndOfTurnPreEndTurnCards(boolean isPlayer) {
        if (isPlayer && this.amount > 0) {
            // 先获得foxfire
            FoxfirePanel.addEnergy(this.amount,true);
            // 然后移除自身
            AbstractDungeon.actionManager.addToBottom(
                new com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction(
                    this.owner, this.owner, this.ID
                )
            );
        }
    }
}