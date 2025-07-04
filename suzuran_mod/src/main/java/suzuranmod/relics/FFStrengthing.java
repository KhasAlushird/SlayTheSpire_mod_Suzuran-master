package suzuranmod.relics;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.RelicStrings;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import basemod.abstracts.CustomRelic;
import suzuranmod.helpers.IdHelper;
import suzuranmod.helpers.ImageHelper;

public class FFStrengthing extends CustomRelic {
    public static final String ID = IdHelper.makePath("FFStrengthing");
    private static final RelicStrings RELIC_STRINGS = CardCrawlGame.languagePack.getRelicStrings(ID);
    private static final String[] DESCRIPTIONS = RELIC_STRINGS.DESCRIPTIONS;
    private static final String IMG_PATH = ImageHelper.getOtherImgPath("relics", "FFStrengthing");

    private int strengthCount = 0;
    private int dexterityCount = 0;

    public FFStrengthing() {
        super(ID,   ImageMaster.loadImage(IMG_PATH), RelicTier.SPECIAL, LandingSound.CLINK);
    }

    @Override
    public String getUpdatedDescription() {
        updateDescription();
        return this.description;
    }

    public void addStrength(int amount) {
        this.strengthCount += amount;
        updateDescription();
        flash();
    }

    public void addDexterity(int amount) {
        this.dexterityCount += amount;
        updateDescription();
        flash();
    }

  private void updateDescription() {
    if (strengthCount == 0 && dexterityCount == 0) {
        // 没有任何加成时的描述
        this.description = DESCRIPTIONS[0];
    } else {
        // 有加成时的描述 - 使用本地化字符串
        StringBuilder desc = new StringBuilder();
        desc.append(DESCRIPTIONS[1]); // "在每场战斗开始时，获得 "
        
        boolean hasBonus = false;
        
        if (dexterityCount > 0) {
            desc.append(dexterityCount).append(DESCRIPTIONS[2]); // " 点 敏捷"
            hasBonus = true;
        }
        
        if (strengthCount > 0 && dexterityCount > 0) {
            desc.append(DESCRIPTIONS[4]); // " 和 "
        }
        
        if (strengthCount > 0) {
            desc.append(strengthCount).append(DESCRIPTIONS[3]); // " 点 力量"
            hasBonus = true;
        }
        
        if (hasBonus) {
            desc.append(DESCRIPTIONS[5]); // "。"
        }
        
        this.description = desc.toString();
    }
    
    // 更新提示
    this.tips.clear();
    this.tips.add(new PowerTip(this.name, this.description));
    initializeTips();
}

    @Override
    public void onEquip() {
        this.strengthCount = 0;
        this.dexterityCount = 0;
        updateDescription();
    }

    @Override
    public void atBattleStart() {
        if (strengthCount > 0 || dexterityCount > 0) {
            flash();
            
            if (strengthCount > 0) {
                addToTop(new ApplyPowerAction(
                    AbstractDungeon.player, 
                    AbstractDungeon.player, 
                    new StrengthPower(AbstractDungeon.player, strengthCount), 
                    strengthCount
                ));
            }
            
            if (dexterityCount > 0) {
                addToTop(new ApplyPowerAction(
                    AbstractDungeon.player, 
                    AbstractDungeon.player, 
                    new DexterityPower(AbstractDungeon.player, dexterityCount), 
                    dexterityCount
                ));
            }
            
            addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        }
    }

    @Override
    public AbstractRelic makeCopy() {
        return new FFStrengthing();
    }

    // 获取当前力量值（供其他地方调用）
    public int getStrengthCount() {
        return strengthCount;
    }

    // 获取当前敏捷值（供其他地方调用）
    public int getDexterityCount() {
        return dexterityCount;
    }
}