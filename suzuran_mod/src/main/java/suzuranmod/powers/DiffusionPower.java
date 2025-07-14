package suzuranmod.powers;

import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.cards.status.Dazed;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.RegenPower;
import com.megacrit.cardcrawl.powers.ThornsPower;

import suzuranmod.helpers.IdHelper;
import suzuranmod.helpers.ImageHelper;

public class DiffusionPower extends AbstractPower {
    public static final String POWER_ID = IdHelper.makePath("DiffusionPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public DiffusionPower(AbstractCreature owner, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        updateDescription();
        this.type = PowerType.BUFF;
        this.isTurnBased = false;

        String path128 = ImageHelper.getOtherImgPath("powers", "DiffusionPower_96");
        String path48 = ImageHelper.getOtherImgPath("powers", "DiffusionPower_35");
        this.region128 = new AtlasRegion(ImageMaster.loadImage(path128), 0, 0, 96, 96);
        this.region48 = new AtlasRegion(ImageMaster.loadImage(path48), 0, 0, 35, 35);
    }

    @Override
    public void updateDescription() {
            this.description = String.format(DESCRIPTIONS[0],  this.amount * 3, this.amount);
    }

    @Override
    public void atStartOfTurnPostDraw() {
        flash();
        
        // 获得2X层再生
        int regenAmount = this.amount * 3;
        AbstractDungeon.actionManager.addToBottom(
            new ApplyPowerAction(owner, owner, new RegenPower(owner, regenAmount), regenAmount)
        );
        
        // 获得2X层荆棘
        int thornsAmount = this.amount * 3;
        AbstractDungeon.actionManager.addToBottom(
            new ApplyPowerAction(owner, owner, new ThornsPower(owner, thornsAmount), thornsAmount)
        );
        
        // 将X张daze置入抽牌堆
        if (this.amount > 0) {
            AbstractDungeon.actionManager.addToBottom(
                new MakeTempCardInDiscardAction(new Dazed(), this.amount)
            );
        }
    }
}