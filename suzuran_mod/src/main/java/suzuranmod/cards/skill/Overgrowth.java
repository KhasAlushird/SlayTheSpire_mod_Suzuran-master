package suzuranmod.cards.skill;

import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
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

public class Overgrowth extends CustomCard {

    public static final String ID = IdHelper.makePath("Overgrowth");
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = CARD_STRINGS.NAME;
    private static final String IMG_PATH = ImageHelper.getCardImgPath(CardType.SKILL, "Overgrowth", true);
    private static final int COST = 0;
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;
    private static final CardType TYPE = CardType.SKILL;
    private static final CardColor COLOR = Suzuran.PlayerColorEnum.Suzuran_COLOR;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;

    public Overgrowth() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.magicNumber = this.baseMagicNumber = 1; // 初始放置的PainOfGrowth数量
        this.retain = true; // 自带保留
        this.cardsToPreview = new PainOfGrowth(); // 预览要添加的卡牌
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
    public void onRetained() {
       upgradeMagicNumber(1);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 将M张PainOfGrowth置入弃牌堆
        for (int i = 0; i < this.magicNumber; i++) {
            AbstractCard painOfGrowth = new PainOfGrowth();
            this.addToBot(new MakeTempCardInDiscardAction(painOfGrowth, 1));
        }
    }

    @Override
    public AbstractCard makeCopy() {
        Overgrowth copy = new Overgrowth();
        copy.magicNumber = this.magicNumber;
        return copy;
    }

}