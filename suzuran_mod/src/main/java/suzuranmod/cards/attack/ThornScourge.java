package suzuranmod.cards.attack;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ThornsPower;

import basemod.abstracts.CustomCard;
import suzuranmod.helpers.IdHelper;
import suzuranmod.helpers.ImageHelper;

public class ThornScourge extends CustomCard {

    public static final String ID = IdHelper.makePath("ThornScourge");
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = CARD_STRINGS.NAME;
    private static final String IMG_PATH = ImageHelper.getCardImgPath(CardType.ATTACK, "ThornScourge", true);
    private static final int COST = 0;
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;
    private static final CardType TYPE = CardType.ATTACK;
    private static final CardColor COLOR = AbstractCard.CardColor.COLORLESS;
    private static final CardRarity RARITY = CardRarity.SPECIAL;
    private static final CardTarget TARGET = CardTarget.ENEMY;

    public ThornScourge() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.baseDamage = 12;
        this.magicNumber = this.baseMagicNumber = 1; 
        this.exhaust = true;
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(1); // 升级后多抽1张牌
            this.rawDescription = CARD_STRINGS.UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 造成伤害
        p.useFastAttackAnimation();
        this.addToBot(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn), 
                     AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
        
        // 抽M张牌
        this.addToBot(new DrawCardAction(p, this.magicNumber));
        
        // 获得2点荆棘
        this.addToBot(new ApplyPowerAction(p, p, new ThornsPower(p, 2), 2));
        
        // 流失4点生命值
        this.addToBot(new LoseHPAction(p, p, 4));
    }

    @Override
    public AbstractCard makeCopy() {
        return new ThornScourge();
    }
}