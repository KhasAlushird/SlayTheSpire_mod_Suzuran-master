package suzuranmod.cards.skill;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.IntangiblePlayerPower;

import suzuranmod.character.Suzuran;
import suzuranmod.character.SuzuranCard;
import suzuranmod.helpers.IdHelper;
import suzuranmod.helpers.ImageHelper;
import suzuranmod.modcore.FoxfirePanel;

public class VastSurvival extends SuzuranCard {

    public static final String ID = IdHelper.makePath("VastSurvival");
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = CARD_STRINGS.NAME;
    private static final String IMG_PATH = ImageHelper.getCardImgPath(CardType.SKILL, "VastSurvival", true);
    private static final int COST = 0;
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;
    private static final CardType TYPE = CardType.SKILL;
    private static final CardColor COLOR = Suzuran.PlayerColorEnum.Suzuran_COLOR;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;

    public VastSurvival() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET, 0,1);
        this.baseBlock = 1; // 基础格挡值
        this.baseMagicNumber = this.magicNumber = 1; // 抽牌数量和无实体层数
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
                p.useFastAttackAnimation();

        // 抽1张牌
        this.addToBot(new DrawCardAction(p, this.magicNumber));
        
        // 获得格挡
        this.addToBot(new GainBlockAction(p, p, this.block));
        
        // 检查狐火数量，如果不高于1，获得无实体
        int currentFoxfire = FoxfirePanel.getCurrentEnergy();
        if (currentFoxfire <= 1) {
            this.addToBot(new ApplyPowerAction(p, p, new IntangiblePlayerPower(p, this.magicNumber), this.magicNumber));
        }
    }


    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeBlock(2); // 升级后格挡+3
            this.initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new VastSurvival();
    }
}