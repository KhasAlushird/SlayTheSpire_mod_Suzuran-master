package suzuranmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import suzuranmod.modcore.OfudaManager;
import suzuranmod.rewards.OfudaRewardItem;

public class OfudaRewardAction extends AbstractGameAction {
    private int delayFrames = 13; // 延迟2帧，可根据需要调整

    @Override
    public void update() {
        if (delayFrames > 0) {
            delayFrames--;
        } else {
            if (OfudaManager.getOfuda() > 0 && AbstractDungeon.getCurrRoom().rewards != null) {
                AbstractDungeon.getCurrRoom().rewards.add(new OfudaRewardItem(OfudaManager.getOfuda()));
            }
            this.isDone = true;
        }
    }
}