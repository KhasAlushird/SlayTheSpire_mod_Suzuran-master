package suzuranmod.powers;

import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

import suzuranmod.helpers.IdHelper;
import suzuranmod.helpers.ImageHelper;

public class FoxCursePower extends AbstractPower {
    public static final String POWER_ID = IdHelper.makePath("FoxCursePower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public FoxCursePower(AbstractCreature owner, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        updateDescription();
        this.type = PowerType.DEBUFF;
        this.isTurnBased = false;

        String path128 = ImageHelper.getOtherImgPath("powers", "FoxCursePower_96");
        String path48 = ImageHelper.getOtherImgPath("powers", "FoxCursePower_35");
        this.region128 = new AtlasRegion(ImageMaster.loadImage(path128), 0, 0, 96, 96);
        this.region48 = new AtlasRegion(ImageMaster.loadImage(path48), 0, 0, 35, 35);
    }

    @Override
    public void updateDescription() {
        this.description = String.format(DESCRIPTIONS[0], this.amount);
    }


    public void trigger(){
        flash();
            // 对所有者造成等同于层数的伤害
             // 用THORNS类型造成伤害（可被格挡，不受易伤加成）
            AbstractDungeon.actionManager.addToBottom(
                new DamageAction(
                    owner,
                    new DamageInfo(owner, this.amount, DamageInfo.DamageType.THORNS),
                    com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect.FIRE
                )
            );
            // 增加10层
            this.amount += 8;
            updateDescription();
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
            trigger();
    }

    @Override
    public void stackPower(int stackAmount) {
        if (this.amount>0){
            this.trigger();
        }
        this.fontScale = 8.0F;
        this.amount += stackAmount;
        updateDescription();
        // 叠加时额外触发一次
}
}