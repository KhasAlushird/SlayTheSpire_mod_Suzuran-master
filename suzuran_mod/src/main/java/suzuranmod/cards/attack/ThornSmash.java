package suzuranmod.cards.attack;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ThornsPower;

import suzuranmod.character.Suzuran;
import suzuranmod.character.SuzuranCard;
import suzuranmod.helpers.IdHelper;
import suzuranmod.helpers.ImageHelper;

public class ThornSmash extends SuzuranCard {

    public static final String ID = IdHelper.makePath("ThornSmash");
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = CARD_STRINGS.NAME;
    private static final String IMG_PATH = ImageHelper.getCardImgPath(CardType.ATTACK, "ThornSmash", true);
    private static final int COST = 1;
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;
    private static final CardType TYPE = CardType.ATTACK;
    private static final CardColor COLOR = Suzuran.PlayerColorEnum.Suzuran_COLOR;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;

    public ThornSmash() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET, 0);
        this.baseDamage = 0; // 伤害基于荆棘数量
        this.magicNumber = this.baseMagicNumber = 2; // 荆棘数量的倍数
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 计算荆棘伤害
        int thornsAmount = 0;
        if (p.hasPower(ThornsPower.POWER_ID)) {
            thornsAmount = p.getPower(ThornsPower.POWER_ID).amount;
        }
        
        this.baseDamage = thornsAmount * this.magicNumber;
        this.calculateCardDamage(m);
        
        // 造成伤害
        this.addToBot(
            new DamageAction(
                m,
                new DamageInfo(p, this.damage, this.damageTypeForTurn),
                AbstractGameAction.AttackEffect.BLUNT_HEAVY
            )
        );
        
        
        // 重置描述
        this.rawDescription = CARD_STRINGS.DESCRIPTION;
        this.initializeDescription();
    }

    @Override
    public void applyPowers() {
        // 获取当前荆棘数量
        int thornsAmount = 0;
        if (com.megacrit.cardcrawl.dungeons.AbstractDungeon.player != null && 
            com.megacrit.cardcrawl.dungeons.AbstractDungeon.player.hasPower(ThornsPower.POWER_ID)) {
            thornsAmount = com.megacrit.cardcrawl.dungeons.AbstractDungeon.player.getPower(ThornsPower.POWER_ID).amount;
        }
        
        this.baseDamage = thornsAmount * this.magicNumber;
        super.applyPowers();
        
        // 更新描述显示预计伤害
        this.rawDescription = CARD_STRINGS.DESCRIPTION;
        if (CARD_STRINGS.EXTENDED_DESCRIPTION != null && CARD_STRINGS.EXTENDED_DESCRIPTION.length > 0) {
            this.rawDescription += CARD_STRINGS.EXTENDED_DESCRIPTION[0] + (thornsAmount * this.magicNumber);
            if (CARD_STRINGS.EXTENDED_DESCRIPTION.length > 1) {
                this.rawDescription += CARD_STRINGS.EXTENDED_DESCRIPTION[1];
            }
        }
        this.initializeDescription();
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        // 计算伤害前先更新基础伤害
        int thornsAmount = 0;
        if (com.megacrit.cardcrawl.dungeons.AbstractDungeon.player != null && 
            com.megacrit.cardcrawl.dungeons.AbstractDungeon.player.hasPower(ThornsPower.POWER_ID)) {
            thornsAmount = com.megacrit.cardcrawl.dungeons.AbstractDungeon.player.getPower(ThornsPower.POWER_ID).amount;
        }
        
        this.baseDamage = thornsAmount * this.magicNumber;
        super.calculateCardDamage(mo);
        
        // 更新描述
        this.rawDescription = CARD_STRINGS.DESCRIPTION;
        if (CARD_STRINGS.EXTENDED_DESCRIPTION != null && CARD_STRINGS.EXTENDED_DESCRIPTION.length > 0) {
            this.rawDescription += CARD_STRINGS.EXTENDED_DESCRIPTION[0] + (thornsAmount * this.magicNumber);
            if (CARD_STRINGS.EXTENDED_DESCRIPTION.length > 1) {
                this.rawDescription += CARD_STRINGS.EXTENDED_DESCRIPTION[1];
            }
        }
        this.initializeDescription();
    }

    @Override
    public void onMoveToDiscard() {
        // 当卡牌移动到弃牌堆时，重置描述
        this.rawDescription = CARD_STRINGS.DESCRIPTION;
        this.initializeDescription();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeBaseCost(0);
            this.rawDescription = CARD_STRINGS.UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new ThornSmash();
    }
}