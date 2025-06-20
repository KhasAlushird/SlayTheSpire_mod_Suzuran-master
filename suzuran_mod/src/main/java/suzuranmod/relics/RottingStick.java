package suzuranmod.relics;

import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import basemod.abstracts.CustomRelic;
import suzuranmod.helpers.IdHelper;
import suzuranmod.helpers.ImageHelper;

public class RottingStick extends CustomRelic {
    public static final String ID = IdHelper.makePath("RottingStick");
    private static final String IMG_PATH = ImageHelper.getOtherImgPath("relics", "RottingStick");
    private static final RelicTier RELIC_TIER = RelicTier.UNCOMMON;
    private static final LandingSound LANDING_SOUND = LandingSound.CLINK;

    public RottingStick() {
        super(ID, ImageMaster.loadImage(IMG_PATH), RELIC_TIER, LANDING_SOUND);
    }

    @Override
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    // 监听治疗事件
    @Override
    public int onPlayerHeal(int healAmount) {
        if (healAmount > 0 && AbstractDungeon.getMonsters() != null && !AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
            // 随机选择一个存活的敌人
            AbstractMonster m = AbstractDungeon.getMonsters().getRandomMonster(true);
            if (m != null) {
                this.flash();
                addToTop(new RelicAboveCreatureAction(m, this));
                addToTop(new DamageAction(
                        m,
                        new DamageInfo(AbstractDungeon.player, healAmount, DamageInfo.DamageType.THORNS),
                        com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect.POISON
                ));
            }

        }
        return healAmount;
    }

    @Override
    public AbstractRelic makeCopy() {
        return new RottingStick();
    }
}