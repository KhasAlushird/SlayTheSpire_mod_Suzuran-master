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

public class FoxfireStrike extends SuzuranCard {
    public static final String ID = IdHelper.makePath("FoxfireStrike");
    
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID); 
    public static final String NAME = CARD_STRINGS.NAME;
    public static final String IMG =ImageHelper.getCardImgPath(CardType.ATTACK, "FoxfireStrike",true);
    public static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;
    public static final int COST = 2;
    public static final int DAMAGE = 20;
    public static final int FOXFIRE_CONSUME = 1;

    public FoxfireStrike() {
        super(ID, NAME, IMG, COST, DESCRIPTION, CardType.ATTACK, Suzuran.PlayerColorEnum.Suzuran_COLOR, CardRarity.BASIC, CardTarget.ENEMY,FOXFIRE_CONSUME);
        this.baseDamage = DAMAGE;
        this.baseFoxfireConsume = FOXFIRE_CONSUME;
        this.foxfireConsume = FOXFIRE_CONSUME;
        this.tags.add(AbstractCard.CardTags.STRIKE);
        this.exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        p.useSlowAttackAnimation();
        doSuzuranBaseUse(p, m); 
        addToBot(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn),AbstractGameAction.AttackEffect.BLUNT_LIGHT));
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeDamage(7);
            this.rawDescription = CARD_STRINGS.UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new FoxfireStrike();
    }
}