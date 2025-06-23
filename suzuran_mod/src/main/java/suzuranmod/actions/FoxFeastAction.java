package suzuranmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;

import suzuranmod.powers.FoxCursePower;

public class FoxFeastAction extends AbstractGameAction {
    private final DamageInfo info;
    private final int foxCurseAmount;

    public FoxFeastAction(AbstractMonster target, DamageInfo info, int foxCurseAmount) {
        this.setValues(target, info);
        this.info = info;
        this.foxCurseAmount = foxCurseAmount;
        this.actionType = ActionType.DAMAGE;
        this.duration = Settings.ACTION_DUR_FAST;
    }

    @Override
    public void update() {
        if (this.duration == Settings.ACTION_DUR_FAST && this.target != null && this.target instanceof AbstractMonster) {
            AbstractMonster m = (AbstractMonster) this.target;
            // 播放攻击特效
            AbstractDungeon.effectList.add(new FlashAtkImgEffect(m.hb.cX, m.hb.cY, AttackEffect.SLASH_HEAVY));
            // 造成伤害
            m.damage(this.info);
            // 如果造成了未被格挡的伤害
            if (m.lastDamageTaken > 0 && !m.isDeadOrEscaped()) {
                // 施加foxcurse
                addToBot(new com.megacrit.cardcrawl.actions.common.ApplyPowerAction(
                        m, this.source, new FoxCursePower(m, foxCurseAmount), foxCurseAmount));
            }
        }
        tickDuration();
    }
}