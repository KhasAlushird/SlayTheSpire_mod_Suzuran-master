package suzuranmod.cards.attack;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.NoBlockPower;

import suzuranmod.character.Suzuran;
import suzuranmod.character.SuzuranCard;
import suzuranmod.helpers.IdHelper;
import suzuranmod.helpers.ImageHelper;

public class Leapup extends SuzuranCard {
    public static final String ID = IdHelper.makePath("Leapup");
    
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID); 
    public static final String NAME = CARD_STRINGS.NAME;
    public static final String IMG =ImageHelper.getCardImgPath(CardType.ATTACK, "Leapup",true);
    public static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;
    public static final int COST = 2;
    public static final int DAMAGE = 30;
    public static final int FOXFIRE_GAIN = 1;

    public Leapup() {
        super(ID, NAME, IMG, COST, DESCRIPTION, CardType.ATTACK, Suzuran.PlayerColorEnum.Suzuran_COLOR, CardRarity.UNCOMMON, CardTarget.ENEMY,0);
        this.baseDamage = DAMAGE;
        this.baseFoxfireGain = FOXFIRE_GAIN;
        this.foxfireGain = FOXFIRE_GAIN;
        this.magicNumber = this.baseMagicNumber = 2;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        doSuzuranBaseUse(p, m); 
        addToBot(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn)));
        addToBot((AbstractGameAction)new ApplyPowerAction((AbstractCreature)p, (AbstractCreature)p, (AbstractPower)new NoBlockPower((AbstractCreature)p, this.magicNumber, false), this.magicNumber));
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeDamage(10);
            this.rawDescription = CARD_STRINGS.UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new Leapup();
    }
}