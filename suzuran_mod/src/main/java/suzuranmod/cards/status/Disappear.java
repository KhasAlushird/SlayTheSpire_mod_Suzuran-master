package suzuranmod.cards.status;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import basemod.abstracts.CustomCard;
import suzuranmod.helpers.IdHelper;
import suzuranmod.helpers.ImageHelper;
import suzuranmod.modcore.FoxfirePanel;

public class Disappear extends CustomCard {
    public static final String ID = IdHelper.makePath("Disappear");
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = CARD_STRINGS.NAME;
    private static final String IMG_PATH = ImageHelper.getCardImgPath(CardType.STATUS, "Disappear", true);
    private static final int COST = -2;
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;
    private static final CardType TYPE = CardType.STATUS;
    private static final CardColor COLOR = CardColor.COLORLESS;
    private static final CardRarity RARITY = CardRarity.SPECIAL;
    private static final CardTarget TARGET = CardTarget.NONE;

    public Disappear() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.isEthereal = true;
    }

    @Override
    public void triggerWhenDrawn() {
        // 抽到时触发：失去1点狐火（不会使狐火低于2）
        int currentFoxfire = FoxfirePanel.getCurrentEnergy();
        if (currentFoxfire > 2) {
            FoxfirePanel.useEnergy(1);
            flash(); // 闪烁效果提示玩家
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
    }

    @Override
    public void upgrade() {
    }

    @Override
    public AbstractCard makeCopy() {
        return new Disappear();
    }
}