package suzuranmod.cards.skill;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ThornsPower;

import basemod.abstracts.CustomCard;
import suzuranmod.actions.ProliferationAction;
import suzuranmod.character.Suzuran;
import suzuranmod.helpers.IdHelper;
import suzuranmod.helpers.ImageHelper;

public class Proliferation extends CustomCard {

    public static final String ID = IdHelper.makePath("Proliferation");
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = CARD_STRINGS.NAME;
    private static final String IMG_PATH = ImageHelper.getCardImgPath(CardType.SKILL, "Proliferation", true);
    private static final int COST = 2;
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;
    private static final CardType TYPE = CardType.SKILL;
    private static final CardColor COLOR = Suzuran.PlayerColorEnum.Suzuran_COLOR;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;

    public Proliferation() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.magicNumber = this.baseMagicNumber = 5; // 升级后获得的荆棘数量
        this.exhaust = true;
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.rawDescription = CARD_STRINGS.UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        p.useSlowAttackAnimation();


        
        //将荆棘翻倍
        this.addToBot(new ProliferationAction());
        if (this.upgraded) {
            // 升级后：先获得5点荆棘
            this.addToBot(new ApplyPowerAction(p, p, new ThornsPower(p, this.magicNumber), this.magicNumber));
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new Proliferation();
    }
}