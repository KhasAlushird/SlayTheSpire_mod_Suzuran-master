package suzuranmod.cards.attack;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import basemod.abstracts.CustomCard;
import suzuranmod.character.Suzuran;
import suzuranmod.helpers.IdHelper;
import suzuranmod.helpers.ImageHelper;

public class Sting extends CustomCard {

    public static final String ID = IdHelper.makePath("Sting");
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = CARD_STRINGS.NAME;
    private static final String IMG_PATH = ImageHelper.getCardImgPath(CardType.ATTACK, "Sting", true);
    private static final int COST = 3;
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;
    private static final CardType TYPE = CardType.ATTACK;
    private static final CardColor COLOR = Suzuran.PlayerColorEnum.Suzuran_COLOR;
    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.ENEMY;

    public Sting() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.baseDamage = 15;
        this.baseBlock = 15;
        this.magicNumber = this.baseMagicNumber = 3; 
        this.cardsToPreview = new ThornScourge(); 
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeDamage(5);
            this.upgradeBlock(5);
            this.rawDescription = CARD_STRINGS.UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 造成伤害
        this.addToBot(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn), 
                     AbstractGameAction.AttackEffect.BLUNT_LIGHT));
        
        // 获得格挡
        this.addToBot(new GainBlockAction(p, p, this.block));
        
        // 将M张ThornScourge置入弃牌堆
        for (int i = 0; i < this.magicNumber; i++) {
            AbstractCard thornScourge = new ThornScourge();
            this.addToBot(new MakeTempCardInDiscardAction(thornScourge, 1));
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new Sting();
    }
}