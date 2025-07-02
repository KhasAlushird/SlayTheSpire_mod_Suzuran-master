package suzuranmod.cards.attack;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
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

public class FFCombo extends SuzuranCard {

    public static final String ID = IdHelper.makePath("FFCombo");
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = CARD_STRINGS.NAME;
    private static final String IMG_PATH = ImageHelper.getCardImgPath(CardType.ATTACK, "FFCombo", true);
    private static final int COST = 1;
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;
    private static final CardType TYPE = CardType.ATTACK;
    private static final CardColor COLOR = Suzuran.PlayerColorEnum.Suzuran_COLOR;
    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;

    private static final int BASE_DAMAGE = 7;

    public FFCombo() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET,0);
        this.baseDamage = BASE_DAMAGE;
        this.tags.add(SuzuranCardTagsPatch.FFRESPONCE);
        this.magicNumber = this.baseMagicNumber = 2;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        p.useSlowAttackAnimation();
        int foxfire = FoxfirePanel.getCurrentEnergy();
        this.addToBot(
            new DamageAction(
                m,
                new DamageInfo(p, this.damage, this.damageTypeForTurn),AbstractGameAction.AttackEffect.SLASH_HORIZONTAL)
        );
        this.addToBot(
            new DamageAction(
                m,
                new DamageInfo(p, foxfire*this.magicNumber, this.damageTypeForTurn),AbstractGameAction.AttackEffect.BLUNT_HEAVY)
        );
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeDamage(2);
            this.upgradeMagicNumber(1);
            this.initializeDescription();
            update_FF_des();
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
        int additionalDamages = currentFoxfire * this.magicNumber;
        
        this.rawDescription = CARD_STRINGS.DESCRIPTION + CARD_STRINGS.EXTENDED_DESCRIPTION[0] + additionalDamages + CARD_STRINGS.EXTENDED_DESCRIPTION[1];
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
        return new FFCombo();
    }
}