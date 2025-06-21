package suzuranmod.rewards;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.potions.SmokeBomb;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import basemod.abstracts.CustomReward;
import suzuranmod.helpers.ImageHelper;
import suzuranmod.modcore.OfudaManager;
import suzuranmod.patches.OfudaRewardTypePatch;
import suzuranmod.relics.NineTails;

public class OfudaRewardItem extends CustomReward {
    public int ofudaCount;

    public OfudaRewardItem(int ofudaCount) {
        super(
            ImageMaster.loadImage(ImageHelper.getImgPathWithSubType("ui", "toppanel", "ofuda_reward")),
            "结算并清零灵符: " + ofudaCount + "/" + OfudaManager.OFUDA_MAX,
            OfudaRewardTypePatch.SUZURAN_OFUDA
        );
        this.ofudaCount = ofudaCount;
    }

    @Override
    public boolean claimReward() {
        int count = this.ofudaCount;
        if (count > 0) {
            AbstractPlayer p = AbstractDungeon.player;
            if (count >= 1) p.gainGold(10);
            if (count >= 2) p.gainGold(15);
            if (count >= 4) p.gainGold(35);
            if (count >= 7) p.gainGold(100);

            if (count >= 3) p.obtainPotion(AbstractDungeon.returnRandomPotion());
            if (count >= 6) p.obtainPotion(new SmokeBomb());

            if (count >= 5) {
                AbstractRelic relic = AbstractDungeon.returnRandomRelic(AbstractRelic.RelicTier.COMMON);
                relic.instantObtain();
            }
            if (count >= 8) {
                AbstractRelic relic = AbstractDungeon.returnRandomRelic(AbstractRelic.RelicTier.RARE);
                relic.instantObtain();
            }

            if (count >= 10) {
                p.increaseMaxHp(15, true);
                p.heal(p.maxHealth);
            }
            // if (count >= 11) {
            //     AbstractDungeon.getCurrRoom().phase = com.megacrit.cardcrawl.rooms.AbstractRoom.RoomPhase.INCOMPLETE;
            //     AbstractDungeon.gridSelectScreen.open(AbstractDungeon.player.masterDeck.getPurgeableCards(), 3, "选择最多3张牌移除", false, false, false, false);
            // }
            if (count >= 12) {
                AbstractRelic relic = new NineTails();
                relic.instantObtain();
            }

            OfudaManager.setOfuda(0);
        }
        this.isDone = true;
        return true;
    }
}