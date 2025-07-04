package suzuranmod.cards.attack;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageRandomEnemyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DoubleTapPower;

import suzuranmod.character.Suzuran;
import suzuranmod.character.SuzuranCard;
import suzuranmod.helpers.IdHelper;
import suzuranmod.helpers.ImageHelper;
import suzuranmod.modcore.FoxfirePanel;

public class Pyroblast extends SuzuranCard {

    public static final String ID = IdHelper.makePath("Pyroblast");
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = CARD_STRINGS.NAME;
    private static final String IMG_PATH = ImageHelper.getCardImgPath(CardType.ATTACK, "Pyroblast", true);
    private static final int COST = 1;
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;
    private static final CardType TYPE = CardType.ATTACK;
    private static final CardColor COLOR = Suzuran.PlayerColorEnum.Suzuran_COLOR;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ALL_ENEMY;

    public Pyroblast() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET, 0,4);
        this.baseDamage = 13; // 基础伤害
        this.baseMagicNumber = this.magicNumber = 1; // DoubleTapPower的层数
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 随机对一名敌人造成伤害
        this.addToBot(
            new DamageRandomEnemyAction(
                new DamageInfo(p, this.damage, this.damageTypeForTurn),
                AbstractGameAction.AttackEffect.FIRE
            )
        );

        // 检查狐火数量，如果不高于4，获得DoubleTapPower
        int currentFoxfire = FoxfirePanel.getCurrentEnergy();
        if (currentFoxfire <= 4) {
            this.addToBot(
                new ApplyPowerAction(p, p, new DoubleTapPower(p, this.magicNumber), this.magicNumber)
            );
        }
    }


    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeDamage(1); // 升级后伤害+1
            this.upgradeMagicNumber(1); // 升级后DoubleTapPower层数+1
            this.initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new Pyroblast();
    }
}