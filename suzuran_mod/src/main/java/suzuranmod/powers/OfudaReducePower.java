package suzuranmod.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

import suzuranmod.helpers.IdHelper;
import suzuranmod.helpers.ImageHelper;
import suzuranmod.modcore.OfudaManager;

public class OfudaReducePower extends AbstractPower {
    public static final String POWER_ID = IdHelper.makePath("OfudaReducePower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public OfudaReducePower(AbstractCreature owner, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        updateDescription();
        this.type = PowerType.BUFF;
        this.isTurnBased = false;

        String path128 = ImageHelper.getOtherImgPath("powers", "OfudaReducePower_96");
        String path48 = ImageHelper.getOtherImgPath("powers", "OfudaReducePower_35");
        this.region128 = new com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion(
            com.megacrit.cardcrawl.helpers.ImageMaster.loadImage(path128), 0, 0, 96, 96);
        this.region48 = new com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion(
            com.megacrit.cardcrawl.helpers.ImageMaster.loadImage(path48), 0, 0, 35, 35);
    }

    @Override
    public void updateDescription() {
        this.description = String.format(DESCRIPTIONS[0], this.amount);
    }

    // 回合结束时移除该能力
    @Override
    public void atEndOfRound() {
        
            flash();
            AbstractDungeon.actionManager.addToBottom(
                new com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction(this.owner, this.owner, this.ID)
            );
        
    }

    // 受到伤害时，扣除灵符并移除该能力
    @Override
    public int onLoseHp(int damageAmount) {
        if (damageAmount > 0) {
            flash();
            OfudaManager.loseOfuda(this.amount);
            AbstractDungeon.actionManager.addToBottom(
                new com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction(this.owner, this.owner, this.ID)
            );
        }
        return damageAmount;
    }
}