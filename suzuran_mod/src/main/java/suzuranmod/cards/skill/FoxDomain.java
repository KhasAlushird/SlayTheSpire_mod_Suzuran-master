package suzuranmod.cards.skill;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.PlayTopCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import suzuranmod.character.Suzuran;
import suzuranmod.character.SuzuranCard;
import suzuranmod.helpers.IdHelper;
import suzuranmod.helpers.ImageHelper;
import suzuranmod.modcore.FoxfirePanel;

public class FoxDomain extends SuzuranCard {

    public static final String ID = IdHelper.makePath("FoxDomain");
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = CARD_STRINGS.NAME;
    private static final String IMG_PATH = ImageHelper.getCardImgPath(CardType.SKILL, "FoxDomain", true);
    private static final int COST = 1;
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;
    private static final CardType TYPE = CardType.SKILL;
    private static final CardColor COLOR = Suzuran.PlayerColorEnum.Suzuran_COLOR;
    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.SELF;

    public FoxDomain() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET, 0,2);
        this.baseBlock = 10; // 基础格挡值
        this.baseMagicNumber = this.magicNumber = 3; // 打出抽牌堆顶部的卡牌数量
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
                p.useFastAttackAnimation();

        // 获得格挡
        this.addToBot(new GainBlockAction(p, p, this.block));
        
        // 检查狐火数量，如果不超过2，打出抽牌堆顶部的M张牌
        int currentFoxfire = FoxfirePanel.getCurrentEnergy();
        if (currentFoxfire <= 2) {
            for (int i = 0; i < this.magicNumber; i++) {
                this.addToBot(
                    new PlayTopCardAction(
                        (AbstractCreature)AbstractDungeon.getCurrRoom().monsters.getRandomMonster(null, true, AbstractDungeon.cardRandomRng),
                        false
                    )
                );
            }
        }
    }


    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeBlock(3); // 升级后格挡+3
            this.upgradeMagicNumber(1); // 升级后打出4张牌
            this.initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new FoxDomain();
    }
}