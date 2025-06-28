package suzuranmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;

public class RemodelingAction extends AbstractGameAction {
    private AbstractPlayer p;
    private boolean freeToPlayOnce;
    private int energyOnUse;
    private boolean upgraded;
    private static  int healAmount = 8;

    public RemodelingAction(AbstractPlayer p, boolean freeToPlayOnce, int energyOnUse, boolean upgraded,int healAmount) {
        this.p = p;
        this.freeToPlayOnce = freeToPlayOnce;
        this.duration = Settings.ACTION_DUR_FAST;
        this.actionType = ActionType.HEAL;
        this.energyOnUse = energyOnUse;
        this.upgraded = upgraded;
        this.healAmount = healAmount;
    }

    @Override
    public void update() {
        int effect = 0;
        
        // 计算使用的能量
        if (this.energyOnUse != -1) {
            effect = this.energyOnUse;
        }
        
        // 检查化学X遗物
        if (this.p.hasRelic("Chemical X")) {
            effect += 2;
            this.p.getRelic("Chemical X").flash();
        }

        // 计算恢复次数
        int healTimes = effect;
        if (this.upgraded) {
            healTimes += 1; // 升级后额外+1次
        }

        // 执行恢复
        if (healTimes > 0) {
            for (int i = 0; i < healTimes; i++) {
                addToBot(new HealAction(p, p, healAmount));
            }
        }

        // 消耗能量
        if (!this.freeToPlayOnce) {
            this.p.energy.use(EnergyPanel.totalCount);
        }

        this.isDone = true;
        tickDuration();
    }
}