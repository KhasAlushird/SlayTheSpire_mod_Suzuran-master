package suzuranmod.rewards;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.potions.GhostInAJar;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.OddlySmoothStone;
import com.megacrit.cardcrawl.relics.Vajra;

import basemod.abstracts.CustomReward;
import suzuranmod.helpers.ImageHelper;
import suzuranmod.modcore.OfudaManager;
import suzuranmod.patches.OfudaRewardTypePatch;
import suzuranmod.relics.NineTails;

public class OfudaRewardItem extends CustomReward {
    public int ofudaCount;
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString("Suzuran:OfudaRewardTitle");
    private static final String TITLE_TEMPLATE = uiStrings.TEXT[0];

    public OfudaRewardItem(int ofudaCount) {
        super(
             ImageMaster.loadImage(ImageHelper.getImgPathWithSubType("ui", "toppanel", "ofuda_reward")),
        String.format(TITLE_TEMPLATE, ofudaCount, OfudaManager.OFUDA_MAX),
        OfudaRewardTypePatch.SUZURAN_OFUDA
        );
        this.ofudaCount = ofudaCount;
    }

    @Override
    public boolean claimReward() {
        int count = this.ofudaCount;
        if (count > 0) {
            AbstractPlayer p = AbstractDungeon.player;

            // 1/12: 获得10金币
            if (count >= 1) {
                p.gainGold(10);
                for (int i = 0; i < 10; i++) {
                    AbstractDungeon.effectList.add(new com.megacrit.cardcrawl.vfx.GainPennyEffect(p, p.hb.cX, p.hb.cY, AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY, true));
                }
            }
            // 2/12: 获得15金币
            if (count >= 2) {
                p.gainGold(15);
                for (int i = 0; i < 15; i++) {
                    AbstractDungeon.effectList.add(new com.megacrit.cardcrawl.vfx.GainPennyEffect(p, p.hb.cX, p.hb.cY, AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY, true));
                }
            }
            // 3/12: 恢复15点生命值
            if (count >= 3) {
                p.heal(15);
            }
            // 4/12: 获得35金币
            if (count >= 4) {
                p.gainGold(35);
                for (int i = 0; i < 35; i++) {
                    AbstractDungeon.effectList.add(new com.megacrit.cardcrawl.vfx.GainPennyEffect(p, p.hb.cX, p.hb.cY, AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY, true));
                }
            }
            // 5/12: 获得随机一个普通遗物
            if (count >= 5) {
                AbstractRelic relic = AbstractDungeon.returnRandomRelic(AbstractRelic.RelicTier.COMMON);
                relic.instantObtain();
            }
            // 6/12: 获得 Oddly Smooth Stone（永久+1敏捷）
            if (count >= 6) {
                AbstractRelic relic = new OddlySmoothStone();
                if (relic != null) {
                    relic.instantObtain();
                }
            }
            // 7/12: 获得一瓶罐装幽灵
            if (count >= 7) {
                p.obtainPotion(new GhostInAJar());
            }
            // 8/12: 获得 Vajra（永久+1力量）
            if (count >= 8) {
                AbstractRelic relic = new Vajra();
                relic.instantObtain();
            }
            // 9/12: 获得随机一个罕见遗物
            if (count >= 9) {
                AbstractRelic relic = AbstractDungeon.returnRandomRelic(AbstractRelic.RelicTier.RARE);
                relic.instantObtain();
            }
            // 10/12: 获得随机一个稀有遗物
            if (count >= 10) {
                AbstractRelic relic = AbstractDungeon.returnRandomRelic(AbstractRelic.RelicTier.RARE);
                relic.instantObtain();
            }
            // 11/12: 恢复最大生命值的50%并将最大生命值提升15
            if (count >= 11) {
                p.heal((int)(p.maxHealth * 0.5));
                p.increaseMaxHp(15, true);
            }
            // 12/12: 获得九尾
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