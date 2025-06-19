package suzuranmod.character;

import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import basemod.abstracts.CustomCard;
import suzuranmod.modcore.FoxfirePanel;
import suzuranmod.powers.BurnoutPower;

public abstract class SuzuranCard extends CustomCard {
    public boolean upgradedHeal = false;

    public int baseFoxfireConsume = 0;
    public int foxfireConsume = 0;
    public boolean upgradedFoxfireConsume = false;
    public int baseFoxfireGain = 0;
    public int foxfireGain = 0;
    public boolean upgradedFoxfireGain = false;

    public SuzuranCard(String id, String name, String img, int cost, String rawDescription, CardType type, CardColor color, CardRarity rarity, CardTarget target) {
        super(id, name, img, cost, rawDescription, type, color, rarity, target);
        this.baseHeal = 0;
        this.heal = this.baseHeal;
        this.baseFoxfireConsume = 0;
        this.foxfireConsume = this.baseFoxfireConsume;
    }

    // 升级治疗量
    public void upgradeHeal(int amount) {
        this.baseHeal += amount;
        this.heal = this.baseHeal;
        this.upgradedHeal = true;
    }

    // 升级狐火消耗
    public void upgradeFoxfireConsume(int new_consume) {
        this.baseFoxfireConsume = new_consume;
        this.foxfireConsume = this.baseFoxfireConsume;
        this.upgradedFoxfireConsume = true;
    }
    public void upgradeFoxfireGain(int amount) {
        this.baseFoxfireGain += amount;
        this.foxfireGain = this.baseFoxfireGain;
        this.upgradedFoxfireGain = true;
    }

    // 卡牌能否使用
    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        if (this.foxfireConsume ==0) return true;
        // 存在BurnoutPower时不能使用
        if (p.hasPower(BurnoutPower.POWER_ID)) {
            this.cantUseMessage = "你处于燃尽状态，无法使用此牌。";
            return false;
        }
        return super.canUse(p, m);
    }

    @Override
    public void triggerOnGlowCheck() {
        AbstractPlayer p = AbstractDungeon.player;
        boolean glow =  this.foxfireConsume > 0 && (p == null || !p.hasPower(BurnoutPower.POWER_ID));
        if (glow) {
        this.glowColor = AbstractCard.GOLD_BORDER_GLOW_COLOR.cpy();
        } else {
        this.glowColor = AbstractCard.BLUE_BORDER_GLOW_COLOR.cpy();
        } 
    }

    protected void doSuzuranBaseUse(AbstractPlayer p, AbstractMonster m) {
        // 治疗（直接用父类的heal/baseHeal字段）
        if (this.heal > 0) {
            addToBot(new HealAction(p, p, this.heal));
        }
        // 狐火消耗
        if (this.foxfireConsume > 0) {
            int current = FoxfirePanel.getCurrentEnergy();
            int consume = Math.min(this.foxfireConsume, Math.max(0, current - 1)); // 最低保留1点
            if (consume > 0) {
                FoxfirePanel.useEnergy(consume);
            }
        }
        if (this.foxfireGain > 0) {
            FoxfirePanel.addEnergy(this.foxfireGain);
        }
    }

    // 复制方法
    @Override
    public abstract AbstractCard makeCopy();
}