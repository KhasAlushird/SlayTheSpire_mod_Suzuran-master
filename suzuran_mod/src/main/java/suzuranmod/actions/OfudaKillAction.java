package suzuranmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;

import suzuranmod.modcore.OfudaManager;

public class OfudaKillAction extends AbstractGameAction {
    private DamageInfo info;
    private static final float DURATION = 0.1F;
    private final int ofudaAmount;

    public OfudaKillAction(AbstractCreature target, DamageInfo info, int ofudaAmount) {
        this.info = info;
        setValues(target, info);
        this.actionType = ActionType.DAMAGE;
        this.duration = DURATION;
        this.ofudaAmount = ofudaAmount;
    }

    @Override
    public void update() {
        if (this.duration == DURATION && this.target != null) {
            AbstractDungeon.effectList.add(new FlashAtkImgEffect(this.target.hb.cX, this.target.hb.cY, AttackEffect.NONE));
            this.target.damage(this.info);
            if (this.target instanceof AbstractMonster) {
                AbstractMonster m = (AbstractMonster) this.target;
                if ((m.isDying || m.currentHealth <= 0) && !m.halfDead && !m.hasPower("Minion")) {
                    OfudaManager.addOfuda(ofudaAmount);
                }
            }
            if (AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()) {
                AbstractDungeon.actionManager.clearPostCombatActions();
            }
        }
        tickDuration(); // 关键修复
    }
}