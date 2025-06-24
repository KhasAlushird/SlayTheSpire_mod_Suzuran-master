package suzuranmod.cards.skill;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import basemod.abstracts.CustomCard;
import suzuranmod.character.Suzuran;
import suzuranmod.helpers.IdHelper;
import suzuranmod.helpers.ImageHelper;
import suzuranmod.modcore.OfudaManager;

public class RemoveDisaster extends CustomCard {

    public static final String ID = IdHelper.makePath("RemoveDisaster");
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = CARD_STRINGS.NAME;
    private static final String IMG_PATH = ImageHelper.getCardImgPath(CardType.SKILL, "RemoveDisaster", false);
    private static final int COST = 1;
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;
    private static final CardType TYPE = CardType.SKILL;
    private static final CardColor COLOR = Suzuran.PlayerColorEnum.Suzuran_COLOR;
    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.SELF;

    private static final int BASE_BLOCK = 8;
    private static final int BONUS_BLOCK = 35;

    public RemoveDisaster() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.baseBlock = BASE_BLOCK;
        this.exhaust = true;
        this.selfRetain = true;
        this.magicNumber = this.baseMagicNumber = BONUS_BLOCK;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
         p.useFastAttackAnimation();
        this.addToBot(new GainBlockAction(p, p, this.block));
        if (OfudaManager.getOfuda() > 0) {
            OfudaManager.loseOfuda(1);
            this.addToBot(new GainBlockAction(p, p, this.magicNumber));
        }
    }

    @Override
    public void triggerOnGlowCheck() {
        AbstractPlayer p = AbstractDungeon.player;
        boolean glow =  OfudaManager.getOfuda() > 0;
        if (glow) {
        this.glowColor = AbstractCard.GOLD_BORDER_GLOW_COLOR.cpy();
        } else {
        this.glowColor = AbstractCard.BLUE_BORDER_GLOW_COLOR.cpy();
        } 
    }
    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeBlock(3);
            this.rawDescription = CARD_STRINGS.UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new RemoveDisaster();
    }
}