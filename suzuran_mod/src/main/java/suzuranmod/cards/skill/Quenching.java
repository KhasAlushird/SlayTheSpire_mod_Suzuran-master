package suzuranmod.cards.skill;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import suzuranmod.character.Suzuran;
import suzuranmod.character.SuzuranCard;
import suzuranmod.helpers.IdHelper;
import suzuranmod.helpers.ImageHelper;

public class Quenching extends SuzuranCard {
    public static final String ID = IdHelper.makePath("Quenching");
    
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID); 
    public static final String NAME = CARD_STRINGS.NAME;
    public static final String IMG =ImageHelper.getCardImgPath(CardType.ATTACK, "Quenching",false);
    public static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;
    public static final int COST = 0;
    public static final int FOXFIRE_CONSUME = 2;

    public Quenching() {
        super(ID, NAME, IMG, COST, DESCRIPTION, CardType.SKILL, Suzuran.PlayerColorEnum.Suzuran_COLOR, CardRarity.UNCOMMON, CardTarget.SELF);
        this.baseFoxfireConsume = FOXFIRE_CONSUME;
        this.foxfireConsume = FOXFIRE_CONSUME;
        this.exhaust = true;
        this.magicNumber = 3;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        doSuzuranBaseUse(p, m); 
         addToBot(new com.megacrit.cardcrawl.actions.common.GainEnergyAction(this.magicNumber));
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(1);
            this.rawDescription = CARD_STRINGS.UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new Quenching();
    }
}