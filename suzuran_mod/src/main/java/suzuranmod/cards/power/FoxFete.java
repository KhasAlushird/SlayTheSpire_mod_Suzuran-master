package suzuranmod.cards.power;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.RitualPower;

import suzuranmod.character.Suzuran;
import suzuranmod.character.SuzuranCard;
import suzuranmod.helpers.IdHelper;
import suzuranmod.helpers.ImageHelper;

public class FoxFete extends SuzuranCard {
    public static final String ID = IdHelper.makePath("FoxFete");
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = CARD_STRINGS.NAME;
    public static final String IMG = ImageHelper.getCardImgPath(CardType.POWER, "FoxFete", true);
    public static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;
    public static final int COST = 1;
    public static final int FOXFIRE_CONSUME = 1;
    public static final int RITUAL_AMOUNT = 1;

    public FoxFete() {
        super(ID, NAME, IMG, COST, DESCRIPTION, CardType.POWER, Suzuran.PlayerColorEnum.Suzuran_COLOR, CardRarity.UNCOMMON, CardTarget.SELF,FOXFIRE_CONSUME);
        this.baseFoxfireConsume = FOXFIRE_CONSUME;
        this.foxfireConsume = FOXFIRE_CONSUME;
        this.magicNumber = this.baseMagicNumber = RITUAL_AMOUNT;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        doSuzuranBaseUse(p, m);
        addToBot(new ApplyPowerAction(p, p, new RitualPower(p, this.magicNumber, false)));
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.isInnate = true;
            this.rawDescription = CARD_STRINGS.UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new FoxFete();
    }
}