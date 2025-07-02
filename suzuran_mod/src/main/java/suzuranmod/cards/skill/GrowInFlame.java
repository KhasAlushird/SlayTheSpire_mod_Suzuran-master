package suzuranmod.cards.skill;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.ExhaustAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import basemod.abstracts.CustomCard;
import suzuranmod.cards.status.PainOfGrowth;
import suzuranmod.character.Suzuran;
import suzuranmod.helpers.IdHelper;
import suzuranmod.helpers.ImageHelper;

public class GrowInFlame extends CustomCard {

    public static final String ID = IdHelper.makePath("GrowInFlame");
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = CARD_STRINGS.NAME;
    private static final String IMG_PATH = ImageHelper.getCardImgPath(CardType.SKILL, "GrowInFlame", true);
    private static final int COST = 0;
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;
    private static final CardType TYPE = CardType.SKILL;
    private static final CardColor COLOR = Suzuran.PlayerColorEnum.Suzuran_COLOR;
    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.SELF;

    public GrowInFlame() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.cardsToPreview = new PainOfGrowth(); 
        this.exhaust = true;
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.exhaust = false;
            this.rawDescription = CARD_STRINGS.UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 选择并消耗1张牌
        this.addToBot(new ExhaustAction(1, false, false, false));
        
        // 抽1张牌
        this.addToBot(new DrawCardAction(p, 1));
        
        // 将1张PainOfGrowth置入抽牌堆
        AbstractCard painOfGrowth = new PainOfGrowth();
        this.addToBot(new MakeTempCardInDrawPileAction(painOfGrowth, 1, true, true));
    }

    @Override
    public AbstractCard makeCopy() {
        return new GrowInFlame();
    }
}