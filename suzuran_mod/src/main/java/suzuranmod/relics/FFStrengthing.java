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

    public FFStrengthing() {
        super(ID, ImageMaster.loadImage(IMG_PATH), RelicTier.SPECIAL, LandingSound.CLINK);
        this.counter = -2; // 初始化counter为负数
    }

    @Override
    public String getUpdatedDescription() {
        updateDescription();
        return this.description;
    }

    public void addStrength(int amount) {
        int strength = getStrengthCount() + amount;
        int dexterity = getDexterityCount();
        this.counter = encodeValues(strength, dexterity);
        updateDescription();
        flash();
    }

    public void addDexterity(int amount) {
        int strength = getStrengthCount();
        int dexterity = getDexterityCount() + amount;
        this.counter = encodeValues(strength, dexterity);
        updateDescription();
        flash();
    }

    // 获取力量值（低16位）
    public int getStrengthCount() {
        if (this.counter == -2) return 0; // 初始状态
        // 先转换为正数，再取低16位
        int positiveValue = -(this.counter + 1);
        return positiveValue & 0xFFFF;
    }

    // 获取敏捷值（高16位）
    public int getDexterityCount() {
        if (this.counter == -2) return 0; // 初始状态
        // 先转换为正数，再取高16位
        int positiveValue = -(this.counter + 1);
        return (positiveValue >> 16) & 0xFFFF;
    }

    // 编码两个值到一个int中，确保结果为负数
    private int encodeValues(int strength, int dexterity) {
        // 确保值在有效范围内（0-65535）
        strength = Math.max(0, Math.min(strength, 0xFFFF));
        dexterity = Math.max(0, Math.min(dexterity, 0xFFFF));
        
        // 正常编码
        int encoded = (dexterity << 16) | strength;
        
        // 转换为负数：-(encoded + 1)
        // 这样确保counter始终为负数，且可以完美还原
        return -(encoded + 1);
    }

    private void updateDescription() {
        int strengthCount = getStrengthCount();
        int dexterityCount = getDexterityCount();
        
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
        // 如果是新获得的遗物，保持为-2
        if (this.counter == -1) {
            this.counter = -2;
        }
        updateDescription();
    }

    @Override
    public void atBattleStart() {
        updateDescription();
        int strengthCount = getStrengthCount();
        int dexterityCount = getDexterityCount();
        
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
        FFStrengthing copy = new FFStrengthing();
        copy.counter = this.counter; // 复制计数值
        return copy;
    }
}