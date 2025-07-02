package suzuranmod.cards.status;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import basemod.abstracts.CustomCard;
import suzuranmod.helpers.IdHelper;
import suzuranmod.helpers.ImageHelper;
import suzuranmod.modcore.FoxfirePanel;

public class BloomJoy extends CustomCard {
    public static final String ID = IdHelper.makePath("BloomJoy");
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = CARD_STRINGS.NAME;
    private static final String IMG_PATH = ImageHelper.getCardImgPath(CardType.STATUS, "BloomJoy", true);
    private static final int COST = 0;
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;
    private static final CardType TYPE = CardType.STATUS;
    private static final CardColor COLOR = CardColor.COLORLESS;
    private static final CardRarity RARITY = CardRarity.SPECIAL;
    private static final CardTarget TARGET = CardTarget.SELF;

    public BloomJoy() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.magicNumber = this.baseMagicNumber = 12;
        this.exhaust = true; 
    }

    @Override
    public void upgrade() {
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new DrawCardAction(p, 1));
        
        // 获得1费
        this.addToBot(new GainEnergyAction(2));
        
        // 获得1点狐火
        FoxfirePanel.addEnergy(1);
        
        // 回复12点生命值
        this.addToBot(new HealAction(p, p, this.magicNumber));
    }

    @Override
    public AbstractCard makeCopy() {
        return new BloomJoy();
    }
}