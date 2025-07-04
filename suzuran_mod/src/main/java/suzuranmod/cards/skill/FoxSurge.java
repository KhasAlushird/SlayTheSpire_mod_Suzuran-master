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

public class FoxSurge extends SuzuranCard {
    public static final String ID = IdHelper.makePath("FoxSurge");
    
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID); 
    public static final String NAME = CARD_STRINGS.NAME;
    public static final String IMG =ImageHelper.getCardImgPath(CardType.SKILL, "FoxSurge",true);
    public static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;
    public static final int COST = 0;
    public static final int FOXFIRE_CONSUME = 2;

    public FoxSurge() {
        super(ID, NAME, IMG, COST, DESCRIPTION, CardType.SKILL, Suzuran.PlayerColorEnum.Suzuran_COLOR, CardRarity.UNCOMMON, CardTarget.SELF,FOXFIRE_CONSUME);
        this.magicNumber =this.baseMagicNumber= 4;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
                p.useFastAttackAnimation();

        doSuzuranBaseUse(p, m); 
        addToBot(new com.megacrit.cardcrawl.actions.common.DrawCardAction(p, this.magicNumber));
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
        return new FoxSurge();
    }
}