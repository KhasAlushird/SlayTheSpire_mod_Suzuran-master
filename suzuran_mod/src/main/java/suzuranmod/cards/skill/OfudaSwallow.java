package suzuranmod.cards.skill;

import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
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
import suzuranmod.modcore.OfudaManager;

public class OfudaSwallow extends CustomCard {

    public static final String ID = IdHelper.makePath("OfudaSwallow");
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = CARD_STRINGS.NAME;
    private static final String IMG_PATH = ImageHelper.getCardImgPath(CardType.SKILL, "OfudaSwallow", true);
    private static final int COST = 0;
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;
    private static final CardType TYPE = CardType.SKILL;
    private static final CardColor COLOR = Suzuran.PlayerColorEnum.Suzuran_COLOR;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;

    public OfudaSwallow() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.magicNumber = this.baseMagicNumber = 4; // 流失的生命值
        this.exhaust = true;
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(-2); // 升级后减少2点生命流失
            this.rawDescription = CARD_STRINGS.UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
                p.useSlowAttackAnimation();

        // 计算触发次数：1次基础 + 灵符数量
        int repeat = 1 + OfudaManager.getOfuda();
        int max_hp_loss = p.currentHealth - 1;
        
        for (int i = 0; i < repeat; i++) {
            
            int thisHpLoss = Math.min(this.magicNumber, max_hp_loss);
            max_hp_loss -= thisHpLoss;
            
            if (thisHpLoss > 0) {
                this.addToBot(new LoseHPAction(p, p, thisHpLoss));
            }
            
            // 获得1点能量
            this.addToBot(new GainEnergyAction(1));
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new OfudaSwallow();
    }
}