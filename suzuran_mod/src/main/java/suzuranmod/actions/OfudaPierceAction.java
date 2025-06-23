package suzuranmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;

import suzuranmod.modcore.OfudaManager;

public class OfudaPierceAction extends AbstractGameAction {
    private DamageInfo info;
    private static final float DURATION = 0.1F;
    private int hitCount = 0;
    private boolean killed = false;

    public OfudaPierceAction(AbstractCreature target, DamageInfo info) {
        this.info = info;
        setValues(target, info);
        this.actionType = ActionType.DAMAGE;
        this.duration = DURATION;
    }

    @Override
    public void update() {
        if (this.duration == DURATION && this.target != null && hitCount < 3) {
            AbstractDungeon.effectList.add(new FlashAtkImgEffect(this.target.hb.cX, this.target.hb.cY, AttackEffect.SLASH_DIAGONAL));
            this.target.damage(this.info);

            if (this.target instanceof AbstractMonster) {
                AbstractMonster m = (AbstractMonster) this.target;
                if ((m.isDying || m.currentHealth <= 0) && !m.halfDead && !m.hasPower("Minion")) {
                    killed = true;
                }
            }

            hitCount++;
            if (hitCount < 3 && !killed) {
                this.duration = DURATION;
                return;
            }
        }

        tickDuration();

        // 只在action真正结束时消耗ofuda
        if (this.isDone) {
            if (!killed && OfudaManager.getOfuda() > 0) {
                OfudaManager.loseOfuda(1);
            }
            if (AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()) {
                AbstractDungeon.actionManager.clearPostCombatActions();
            }
        }
    }
}