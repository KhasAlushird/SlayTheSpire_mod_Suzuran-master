package suzuranmod.powers;

import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.ThornsPower;

import suzuranmod.helpers.IdHelper;
import suzuranmod.helpers.ImageHelper;

public class HealThornPower extends AbstractPower {
    public static final String POWER_ID = IdHelper.makePath("HealThornPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public HealThornPower(AbstractCreature owner, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        updateDescription();
        this.type = PowerType.BUFF;
        this.isTurnBased = false;

        String path128 = ImageHelper.getOtherImgPath("powers", "HealThornPower_96");
        String path48 = ImageHelper.getOtherImgPath("powers", "HealThornPower_35");
        this.region128 = new AtlasRegion(ImageMaster.loadImage(path128), 0, 0, 96, 96);
        this.region48 = new AtlasRegion(ImageMaster.loadImage(path48), 0, 0, 35, 35);
    }

    @Override
    public void updateDescription() {
        this.description = String.format(DESCRIPTIONS[0], this.amount);
    }

    // 受到治疗时触发
    @Override
    public int onHeal(int healAmount) {
        if (healAmount > 0) {
            flash();
            AbstractDungeon.actionManager.addToBottom(
                new com.megacrit.cardcrawl.actions.common.ApplyPowerAction(
                    owner, owner, new ThornsPower(owner, this.amount), this.amount
                )
            );
        }
        return healAmount;
    }
}