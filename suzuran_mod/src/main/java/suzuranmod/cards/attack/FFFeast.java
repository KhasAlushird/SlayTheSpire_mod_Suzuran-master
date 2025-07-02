package suzuranmod.cards.attack;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageRandomEnemyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import suzuranmod.character.Suzuran;
import suzuranmod.character.SuzuranCard;
import suzuranmod.helpers.IdHelper;
import suzuranmod.helpers.ImageHelper;
import suzuranmod.modcore.FoxfirePanel;
import suzuranmod.patches.SuzuranCardTagsPatch;

public class FFFeast extends SuzuranCard {

    public static final String ID = IdHelper.makePath("FFFeast");
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = CARD_STRINGS.NAME;
    private static final String IMG_PATH = ImageHelper.getCardImgPath(CardType.ATTACK, "FFFeast", true);
    private static final int COST = 2;
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;
    private static final CardType TYPE = CardType.ATTACK;
    private static final CardColor COLOR = Suzuran.PlayerColorEnum.Suzuran_COLOR;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ALL_ENEMY;

    public FFFeast() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET,0);
        this.baseDamage = 14;
        this.tags.add(SuzuranCardTagsPatch.FFRESPONCE);
        this.magicNumber = this.baseMagicNumber = 3;
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeDamage(4);
            this.rawDescription = CARD_STRINGS.UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 计算攻击次数：基础1次 + 每3点狐火额外1次
        int currentFoxfire = FoxfirePanel.getCurrentEnergy();
        int additionalAttacks = currentFoxfire / this.magicNumber;
        int totalAttacks = 1 + additionalAttacks;
        
        // 执行多次随机攻击
        for (int i = 0; i < totalAttacks; i++) {
            this.addToBot(new DamageRandomEnemyAction(
                new DamageInfo(p, this.damage, this.damageTypeForTurn), 
                AbstractGameAction.AttackEffect.FIRE
            ));
        }
    }

    @Override
    public void applyPowers() {
        super.applyPowers();
        
        // 动态更新描述显示当前的攻击次数
        update_FF_des();
    }


    

    @Override
    public void update_FF_des() {
        int currentFoxfire = FoxfirePanel.getCurrentEnergy();
        int additionalAttacks = currentFoxfire / this.magicNumber;
        
        this.rawDescription = CARD_STRINGS.DESCRIPTION + CARD_STRINGS.EXTENDED_DESCRIPTION[0] + additionalAttacks + CARD_STRINGS.EXTENDED_DESCRIPTION[1];
        this.initializeDescription();
    }

    @Override
    public void onMoveToDiscard() {
        // 当卡牌移动到弃牌堆时，重置描述
        this.rawDescription = CARD_STRINGS.DESCRIPTION;
        this.initializeDescription();
    }



    @Override
    public AbstractCard makeCopy() {
        return new FFFeast();
    }
}