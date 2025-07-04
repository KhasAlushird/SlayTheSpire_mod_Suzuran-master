package suzuranmod.cards.skill;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import suzuranmod.character.Suzuran;
import suzuranmod.character.SuzuranCard;
import suzuranmod.helpers.IdHelper;
import suzuranmod.helpers.ImageHelper;
import suzuranmod.powers.FoxCursePower;

public class Will_o_t_s extends SuzuranCard {
    public static final String ID = IdHelper.makePath("Will_o_t_s");
    
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID); 
    public static final String NAME = CARD_STRINGS.NAME;
    public static final String IMG =ImageHelper.getCardImgPath(CardType.SKILL, "Will_o_t_s",true);
    public static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;
    public static final int COST = 1;
    public static final int FOXFIRE_CONSUME = 1;

    public Will_o_t_s() {
        super(ID, NAME, IMG, COST, DESCRIPTION, CardType.SKILL, Suzuran.PlayerColorEnum.Suzuran_COLOR, CardRarity.RARE, CardTarget.ENEMY,FOXFIRE_CONSUME);
        this.exhaust = true;
        this.magicNumber = this.baseMagicNumber = 5;
        this.isInnate = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
                p.useFastAttackAnimation();

        doSuzuranBaseUse(p, m); 
    addToBot(new ApplyPowerAction(m, p, new FoxCursePower(m, this.magicNumber), this.magicNumber));    }

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
        return new Will_o_t_s();
    }
}