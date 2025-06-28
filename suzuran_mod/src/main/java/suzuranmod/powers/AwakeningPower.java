package suzuranmod.powers;

import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

import suzuranmod.helpers.IdHelper;
import suzuranmod.helpers.ImageHelper;

public class AwakeningPower extends AbstractPower {
    public static final String POWER_ID = IdHelper.makePath("AwakeningPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    
    // 用于实现独立结算的偏移量，模仿炸弹的机制
    private static int healIdOffset = 0;

    public AwakeningPower(AbstractCreature owner, int turns) {
        this.name = NAME;

        this.ID = POWER_ID + healIdOffset;
        healIdOffset++;
        this.owner = owner;
        this.amount = turns;
        updateDescription();
        this.type = PowerType.BUFF;
        this.isTurnBased = false; 

        String path128 = ImageHelper.getOtherImgPath("powers", "AwakeningPower_96");
        String path48 = ImageHelper.getOtherImgPath("powers", "AwakeningPower_35");
        this.region128 = new AtlasRegion(ImageMaster.loadImage(path128), 0, 0, 96, 96);
        this.region48 = new AtlasRegion(ImageMaster.loadImage(path48), 0, 0, 35, 35);
    }

    @Override
    public void updateDescription() {
        if (this.amount == 1) {
            this.description = DESCRIPTIONS[1]; // "在下回合开始时，将生命值恢复至上限。"
        } else {
            this.description = String.format(DESCRIPTIONS[0], this.amount); // "在{0}回合后，将生命值恢复至上限。"
        }
    }

    @Override
    public void atStartOfTurnPostDraw() {
        if (!AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
            // 减少1层
            addToBot(new ReducePowerAction(this.owner, this.owner, this, 1));
            
            // 如果这是最后1层，触发完全治疗
            if (this.amount <= 1) {
                flash();
                int healAmount = this.owner.maxHealth - this.owner.currentHealth;
                if (healAmount > 0) {
                    addToBot(new HealAction(this.owner, this.owner, healAmount));
                }
            }
        }
    }
}