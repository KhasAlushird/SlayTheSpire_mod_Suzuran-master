package suzuranmod.rewards;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.potions.GhostInAJar;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.BottledFlame;
import com.megacrit.cardcrawl.relics.BottledLightning;
import com.megacrit.cardcrawl.relics.BottledTornado;
import com.megacrit.cardcrawl.relics.Pear;

import basemod.abstracts.CustomReward;
import suzuranmod.helpers.ImageHelper;
import suzuranmod.modcore.OfudaManager;
import suzuranmod.patches.OfudaRewardTypePatch;
import suzuranmod.relics.FFStrengthing;
import suzuranmod.relics.NineTails;
import suzuranmod.relics.SixTails;
import suzuranmod.relics.ThreeTails;

public class OfudaRewardItem extends CustomReward {
    public int ofudaCount;
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString("Suzuran:OfudaRewardTitle");
    private static final String TITLE_TEMPLATE = uiStrings.TEXT[0];

    public OfudaRewardItem(int ofudaCount) {
        super(
             ImageMaster.loadImage(ImageHelper.getImgPathWithSubType("ui", "toppanel", "settle_ofuda_reward")),
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
            // 3/12: 恢复12点生命值
            if (count >= 3) {
                p.heal(12);
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
            // 6/12: （永久+1敏捷）
            if (count >= 6) {
                if (p.hasRelic(FFStrengthing.ID)) {
                ((FFStrengthing) p.getRelic(FFStrengthing.ID)).addDexterity(1);
            } else {
                AbstractRelic relic = new FFStrengthing();
                relic.instantObtain();
                ((FFStrengthing) p.getRelic(FFStrengthing.ID)).addDexterity(1);
            }
            }
            // 7/12: 获得一瓶罐装幽灵
            if (count >= 7) {
                p.obtainPotion(new GhostInAJar());
            }
            // 8/12: 永久+1力量）
            if (count >= 8) {
                if (p.hasRelic(FFStrengthing.ID)) {
                ((FFStrengthing) p.getRelic(FFStrengthing.ID)).addStrength(1);
            } else {
                AbstractRelic relic = new FFStrengthing();
                relic.instantObtain();
                ((FFStrengthing) p.getRelic(FFStrengthing.ID)).addStrength(1);
            }
            }

            // 10/12: 获得随机一个罕见遗物
            if (count >= 10) {
                AbstractRelic relic;
                int attempt = 0;
                do {
                    relic = AbstractDungeon.returnRandomRelic(AbstractRelic.RelicTier.UNCOMMON);
                    attempt++;
                } while (attempt<50&&count >= 12 && OfudaManager.getTailRewardCount() >= 2 && 
                        (relic instanceof BottledTornado || relic instanceof BottledFlame || relic instanceof BottledLightning));

                if (attempt>=50){
                    relic = new  Pear();
                }
                
                relic.instantObtain();
            }

            // //Debug
            // if (count >=10){
            //     AbstractRelic relic = new BottledTornado();
            //     relic.instantObtain();
            // }

            

            // 11/12: 恢复最大生命值的50%并将最大生命值提升12
            if (count >= 11) {
                p.heal((int)(p.maxHealth * 0.5));
                p.increaseMaxHp(12, true);
            }
            // 12/12: 获得九尾
            if (count >= 12) {
                AbstractRelic relic = getTailReward();
                relic.instantObtain();
                OfudaManager.incrementTailRewardCount();
            }

            if(count>=9){
                OfudaManager.setOfuda(2);
            }
            else{
                OfudaManager.setOfuda(0);
            }
        }
        this.isDone = true;
        return true;
    }
     private AbstractRelic getTailReward() {
        int tailRewardCount = OfudaManager.getTailRewardCount();
        
        if (tailRewardCount == 0) {
            return new ThreeTails();  // 第一次：三尾
        } else if (tailRewardCount == 1) {
            return new SixTails();    // 第二次：六尾
        } else {
            return new NineTails();   // 第三次及以后：九尾
        }
    }

    }