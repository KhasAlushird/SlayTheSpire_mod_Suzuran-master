package suzuranmod.cards.attack;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import suzuranmod.character.Suzuran;
import suzuranmod.character.SuzuranCard;
import suzuranmod.helpers.IdHelper;
import suzuranmod.helpers.ImageHelper;

public class Denotation extends SuzuranCard {
    public static final String ID = IdHelper.makePath("Denotation");
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = CARD_STRINGS.NAME;
    public static final String IMG = ImageHelper.getCardImgPath(CardType.ATTACK, "Denotation", false);
    public static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;
    public static final int COST = 1;
    public static final int DAMAGE = 25;
    public static final int FOXFIRE_CONSUME = 3;
    public static final int DRAW = 1;
    public static final int UPGRADE_DRAW = 1;

    public Denotation() {
        super(ID, NAME, IMG, COST, DESCRIPTION, CardType.ATTACK, Suzuran.PlayerColorEnum.Suzuran_COLOR, CardRarity.UNCOMMON, CardTarget.ENEMY);
        this.baseDamage = DAMAGE;
        this.baseFoxfireConsume = FOXFIRE_CONSUME;
        this.foxfireConsume = FOXFIRE_CONSUME;
        this.magicNumber = this.baseMagicNumber = DRAW;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        doSuzuranBaseUse(p, m);
        addToBot(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_HEAVY));
        addToBot(new DrawCardAction(p, this.magicNumber));
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(UPGRADE_DRAW);
            this.rawDescription = CARD_STRINGS.UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new Denotation();
    }
}