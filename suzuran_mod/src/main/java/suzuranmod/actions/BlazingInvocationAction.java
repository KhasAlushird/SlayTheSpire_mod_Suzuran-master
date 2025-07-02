package suzuranmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;

import suzuranmod.modcore.FoxfirePanel;

public class BlazingInvocationAction extends AbstractGameAction {
    private int damage;
    private DamageInfo.DamageType damageType;
    private AttackEffect attackEffect;
    private int foxfireLimit; // 狐火恢复上限

    public BlazingInvocationAction(AbstractCreature target, AbstractCreature source, int damage, 
                                  DamageInfo.DamageType type, AttackEffect effect, int foxfireLimit) {
        setValues(target, source, damage);
        this.damage = damage;
        this.damageType = type;
        this.attackEffect = effect;
        this.foxfireLimit = foxfireLimit;
        this.duration = Settings.ACTION_DUR_FAST;
    }

    @Override
    public void update() {
        if (this.duration == Settings.ACTION_DUR_FAST) {
            if (this.target != null && this.target instanceof AbstractMonster) {
                AbstractMonster monster = (AbstractMonster) this.target;
                if (!monster.isDying && monster.currentHealth > 0 && !monster.isEscaping) {
                    // 播放攻击特效
                    AbstractDungeon.effectList.add(new FlashAtkImgEffect(monster.hb.cX, monster.hb.cY, this.attackEffect));
                    
                    // 造成伤害
                    monster.damage(new DamageInfo(this.source, this.damage, this.damageType));
                    
                    // 如果造成了实际伤害
                    if (monster.lastDamageTaken > 0) {
                        // 计算实际可以恢复的狐火数量（不超过上限）
                        int foxfireToRestore = Math.min(monster.lastDamageTaken, this.foxfireLimit);
                        

                        
                        // 恢复狐火
                        if (foxfireToRestore > 0) {
                            FoxfirePanel.addEnergy(foxfireToRestore);
                        }
                    }
                }
            }
            tickDuration();
        }
        
        this.isDone = true;
        if (this.isDone) {
            addToTop(new WaitAction(0.1F));
        }
    }
}