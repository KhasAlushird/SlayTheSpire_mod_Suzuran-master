package suzuranmod.potions;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.potions.AbstractPotion;

import basemod.ReflectionHacks;
import suzuranmod.helpers.IdHelper;
import suzuranmod.modcore.FoxfirePanel;

public class FoxfirePotion extends AbstractPotion {
    public static final String POTION_ID = IdHelper.makePath("FoxfirePotion");
    
    private static final PotionStrings potionStrings = CardCrawlGame.languagePack.getPotionString(IdHelper.makePath("FoxfirePotion"));
    
    public FoxfirePotion() {
        super(potionStrings.NAME, IdHelper.makePath("FoxfirePotion"), 
              AbstractPotion.PotionRarity.RARE, 
              AbstractPotion.PotionSize.FAIRY, 
              AbstractPotion.PotionColor.FIRE);
        
        // 设置药水贴图
        ReflectionHacks.setPrivate(this, AbstractPotion.class, "containerImg", 
                                  new Texture("suzuranmod/images/potions/FoxfirePotion.png"));
        
        this.labOutlineColor = Color.CLEAR; // 青色轮廓，符合狐火主题
        this.isThrown = false; // 不需要投掷，对自己使用
        this.targetRequired = false; // 不需要选择目标
    }
    
    @Override
    public void initializeData() {
        this.potency = getPotency();
        this.description = potionStrings.DESCRIPTIONS[0] + this.potency + potionStrings.DESCRIPTIONS[1];
        this.tips.clear();
        this.tips.add(new PowerTip(this.name, this.description));
    }
    
    @Override
    public void use(AbstractCreature target) {
        // 获得狐火，受到神圣树皮等遗物加成
        int foxfireToGain = this.potency;
        

        FoxfirePanel.addEnergy(foxfireToGain);
        
        // 播放使用效果（可选）
    }
    
    @Override
    public int getPotency(int ascensionLevel) {
        return 3; // 基础获得3点狐火
    }
    
    @Override
    public AbstractPotion makeCopy() {
        return new FoxfirePotion();
    }
}