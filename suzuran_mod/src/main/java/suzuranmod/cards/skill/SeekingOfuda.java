package suzuranmod.cards.skill;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import suzuranmod.character.Suzuran;
import suzuranmod.character.SuzuranCard;
import suzuranmod.helpers.IdHelper;
import suzuranmod.helpers.ImageHelper;

public class SeekingOfuda extends SuzuranCard {
    public static final String ID = IdHelper.makePath("SeekingOfuda");
    
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID); 
    public static final String NAME = CARD_STRINGS.NAME;
    public static final String IMG =ImageHelper.getCardImgPath(CardType.SKILL, "SeekingOfuda",true);
    public static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;
    public static final int COST = 3;

    public SeekingOfuda() {
        super(ID, NAME, IMG, COST, DESCRIPTION, CardType.SKILL, Suzuran.PlayerColorEnum.Suzuran_COLOR, CardRarity.COMMON, CardTarget.SELF,0);
        this.magicNumber =this.baseMagicNumber= 1;
        this.exhaust = true;
        this.baseBlock = 10;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        gainOfuda(this.magicNumber);
        this.addToBot(new GainBlockAction(p, p, this.block));
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
        return new SeekingOfuda();
    }
}