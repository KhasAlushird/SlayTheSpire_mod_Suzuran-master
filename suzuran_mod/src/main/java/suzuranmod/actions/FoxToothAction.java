package suzuranmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import com.megacrit.cardcrawl.vfx.combat.FlyingOrbEffect;

public class FoxToothAction extends AbstractGameAction {
    private int damage;
    private DamageInfo.DamageType damageType;
    private AttackEffect attackEffect;

    public FoxToothAction(AbstractCreature target, AbstractCreature source, int damage, DamageInfo.DamageType type, AttackEffect effect) {
        setValues(target, source, damage);
        this.damage = damage;
        this.damageType = type;
        this.attackEffect = effect;
        this.duration = Settings.ACTION_DUR_FAST;
    }

    @Override
    public void update() {
        if (this.duration == Settings.ACTION_DUR_FAST) {
            if (this.target != null && this.target instanceof AbstractMonster) {
                AbstractMonster monster = (AbstractMonster) this.target;
                if (!monster.isDying && monster.currentHealth > 0 && !monster.isEscaping) {
                    AbstractDungeon.effectList.add(new FlashAtkImgEffect(monster.hb.cX, monster.hb.cY, this.attackEffect));
                    monster.damage(new DamageInfo(this.source, this.damage, this.damageType));
                    if (monster.lastDamageTaken > 0) {
                        for (int i = 0; i < monster.lastDamageTaken / 2 && i < 10; i++) {
                            addToBot(new VFXAction(new FlyingOrbEffect(monster.hb.cX, monster.hb.cY)));
                        }
                        addToBot(new HealAction(this.source, this.source, monster.lastDamageTaken));
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