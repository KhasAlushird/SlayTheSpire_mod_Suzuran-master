package suzuranmod.cards.attack;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageRandomEnemyAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.FlameBarrierEffect;

import suzuranmod.cards.status.Disappear;
import suzuranmod.character.Suzuran;
import suzuranmod.character.SuzuranCard;
import suzuranmod.helpers.IdHelper;
import suzuranmod.helpers.ImageHelper;

public class FlameWave extends SuzuranCard {

    public static final String ID = IdHelper.makePath("FlameWave");
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = CARD_STRINGS.NAME;
    private static final String IMG_PATH = ImageHelper.getCardImgPath(CardType.ATTACK, "FlameWave", true);
    private static final int COST = 3;
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;
    private static final CardType TYPE = CardType.ATTACK;
    private static final CardColor COLOR = Suzuran.PlayerColorEnum.Suzuran_COLOR;
    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.ALL_ENEMY;

    public FlameWave() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET,0);
        this.baseDamage = 9;
        this.magicNumber = this.baseMagicNumber = 2; 
        this.foxfireGain = this.magicNumber;
        this.cardsToPreview = new Disappear();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.rawDescription = CARD_STRINGS.UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        p.useSlowAttackAnimation();
        this.doSuzuranBaseUse(p, m);
        // 添加火焰特效
        this.addToBot(new VFXAction(new FlameBarrierEffect(p.hb.cX, p.hb.cY)));
        
        // 对随机敌人造成伤害3次
        int repeat = 3;
        if (this.upgraded) repeat++;
        for (int i = 0; i < repeat; i++) {
            this.addToBot(new DamageRandomEnemyAction(
                new DamageInfo(p, this.damage, this.damageTypeForTurn), 
                AbstractGameAction.AttackEffect.FIRE
            ));
        }
        
        
        // 将M张Disappear置入弃牌堆
        for (int i = 0; i < this.magicNumber; i++) {
            AbstractCard disappear = new Disappear();
            this.addToBot(new MakeTempCardInDiscardAction(disappear, 1));
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new FlameWave();
    }
}