package suzuranmod.cards.power;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import basemod.abstracts.CustomCard;
import suzuranmod.character.Suzuran;
import suzuranmod.helpers.IdHelper;
import suzuranmod.helpers.ImageHelper;
import suzuranmod.powers.DrawAddPower;

public class MiracleGrowth extends CustomCard {
    public static final String ID = IdHelper.makePath("MiracleGrowth");
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = CARD_STRINGS.NAME;
    public static final String IMG_PATH = ImageHelper.getCardImgPath(CardType.POWER, "MiracleGrowth",true);
    public static final int COST = 0;
    public static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;
    public static final CardType TYPE = CardType.POWER;
    public static final CardColor COLOR = Suzuran.PlayerColorEnum.Suzuran_COLOR;
    public static final CardRarity RARITY = CardRarity.UNCOMMON;
    public static final CardTarget TARGET = CardTarget.SELF;

    private static final int BASE_MAGIC = 15;
    // private static final int DRAW_CARD = 1;
    // private static final int UPGRADE_MAGIC = ;
    // private static final int MIN_HP = 25;

    public MiracleGrowth() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.baseMagicNumber = BASE_MAGIC;
        this.magicNumber = this.baseMagicNumber;
        this.isInnate = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        p.useSlowAttackAnimation();

        int draw_card = 1;
        int hpLoss ;
        if (this.upgraded){
            hpLoss = Math.min(this.magicNumber, Math.max(0, p.currentHealth - (int) (0.5*p.maxHealth)));
        }
        else{
            hpLoss = Math.min(this.magicNumber, p.currentHealth-1);
        }
        
        if (hpLoss > 0) {
            this.addToBot(new LoseHPAction(p, p, hpLoss));
        }
        this.addToBot(new DrawCardAction(p, draw_card));
        this.addToBot(new ApplyPowerAction(p, p, new DrawAddPower(p, 1), 1));
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            // this.upgradeMagicNumber(UPGRADE_MAGIC);
            this.rawDescription = CARD_STRINGS.UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new MiracleGrowth();
    }
}