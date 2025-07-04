package suzuranmod.cards.power;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import basemod.abstracts.CustomCard;
import suzuranmod.character.Suzuran;
import suzuranmod.helpers.IdHelper;
import suzuranmod.helpers.ImageHelper;
import suzuranmod.powers.FireBreathPower;

public class FireBreath extends CustomCard {
    public static final String ID = IdHelper.makePath("FireBreath");
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = CARD_STRINGS.NAME;
    public static final String IMG_PATH = ImageHelper.getCardImgPath(CardType.POWER, "FireBreath", true);
    public static final int COST = 3;
    public static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;
    public static final CardType TYPE = CardType.POWER;
    public static final CardColor COLOR = Suzuran.PlayerColorEnum.Suzuran_COLOR;
    public static final CardRarity RARITY = CardRarity.RARE;
    public static final CardTarget TARGET = CardTarget.SELF;

    private static final int BASE_AMOUNT = 1;

    public FireBreath() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.baseMagicNumber = BASE_AMOUNT;
        this.magicNumber = BASE_AMOUNT;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
                p.useSlowAttackAnimation();

        this.addToBot(new ApplyPowerAction(p, p, new FireBreathPower(p, this.magicNumber), this.magicNumber));
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeBaseCost(2);
            this.rawDescription = CARD_STRINGS.UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new FireBreath();
    }
}