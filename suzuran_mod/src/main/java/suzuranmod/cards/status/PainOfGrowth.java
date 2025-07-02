package suzuranmod.cards.status;


import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import basemod.abstracts.CustomCard;
import suzuranmod.helpers.IdHelper;
import suzuranmod.helpers.ImageHelper;

public class PainOfGrowth extends CustomCard {
    public static final String ID = IdHelper.makePath("PainOfGrowth");
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = CARD_STRINGS.NAME;
    private static final String IMG_PATH = ImageHelper.getCardImgPath(CardType.STATUS, "PainOfGrowth", true);
    private static final int COST = 5;
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;
    private static final CardType TYPE = CardType.STATUS;
    private static final CardColor COLOR = CardColor.COLORLESS;
    private static final CardRarity RARITY = CardRarity.SPECIAL;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final int COST_REDUCE = 1;

    public PainOfGrowth() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.exhaust = true; 
        this.cardsToPreview = new BloomJoy(); // 预览要添加的卡牌
        
    }

    @Override
    public void upgrade() {
    }

    @Override
    public void tookDamage() {
        int amt = COST_REDUCE*-1;
        int tmpCost = this.cost;
        int diff = this.cost - this.costForTurn;
        tmpCost += amt;
        if (tmpCost < 0)
        tmpCost = 0; 
        if (tmpCost != this.cost) {
        this.isCostModified = true;
        this.cost = tmpCost;
        this.costForTurn = this.cost - diff;
        if (this.costForTurn < 0)
            this.costForTurn = 0; 
        } 

  
  }



    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 将一张BloomJoy置入手牌
        AbstractCard bloomJoy = new BloomJoy();
        this.addToBot(new MakeTempCardInHandAction(bloomJoy, 1));
    }

    @Override
    public AbstractCard makeCopy() {
        return new PainOfGrowth();
    }


}