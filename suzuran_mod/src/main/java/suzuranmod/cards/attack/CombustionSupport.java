package suzuranmod.cards.attack;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;

import suzuranmod.character.Suzuran;
import suzuranmod.character.SuzuranCard;
import suzuranmod.helpers.IdHelper;
import suzuranmod.helpers.ImageHelper;
import suzuranmod.modcore.FoxfirePanel;
import suzuranmod.patches.SuzuranCardTagsPatch;

public class CombustionSupport extends SuzuranCard {

    public static final String ID = IdHelper.makePath("CombustionSupport");
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = CARD_STRINGS.NAME;
    private static final String IMG_PATH = ImageHelper.getCardImgPath(CardType.ATTACK, "CombustionSupport", true);
    private static final int COST = 3;
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;
    private static final CardType TYPE = CardType.ATTACK;
    private static final CardColor COLOR = Suzuran.PlayerColorEnum.Suzuran_COLOR;
    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.ENEMY;

    public CombustionSupport() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET,0);
        this.baseDamage = 18;
        this.magicNumber = this.baseMagicNumber = 2; // 获得的力量
        this.tags.add(SuzuranCardTagsPatch.FFRESPONCE);
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeDamage(5);
            this.upgradeMagicNumber(1); // 升级后多获得1点力量
            this.rawDescription = CARD_STRINGS.UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        p.useFastAttackAnimation();
        // 造成伤害
        this.addToBot(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn), 
                     AbstractGameAction.AttackEffect.FIRE));
        
        // 获得力量
        this.addToBot(new ApplyPowerAction(p, p, new StrengthPower(p, this.magicNumber), this.magicNumber));
        
        // 根据狐火数量获得能量：每2点狐火获得1点能量
        int currentFoxfire = FoxfirePanel.getCurrentEnergy();
        int energyToGain = currentFoxfire / 2;
        
        if (energyToGain > 0) {
            this.addToBot(new GainEnergyAction(energyToGain));
        }
    }

    @Override
    public void applyPowers() {
        super.applyPowers();
        
        // 动态更新描述显示当前能获得的能量
        update_FF_des();
       
    }

    @Override
    public void update_FF_des(){
        int currentFoxfire = FoxfirePanel.getCurrentEnergy();
        int energyToGain = currentFoxfire / 2;
        
        this.rawDescription = CARD_STRINGS.DESCRIPTION + CARD_STRINGS.EXTENDED_DESCRIPTION[0] + energyToGain + CARD_STRINGS.EXTENDED_DESCRIPTION[1];
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
        return new CombustionSupport();
    }
}