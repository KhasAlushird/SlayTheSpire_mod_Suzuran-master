package suzuranmod.events;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.localization.EventStrings;

import suzuranmod.helpers.IdHelper;
import suzuranmod.helpers.ImageHelper;
import suzuranmod.modcore.OfudaManager;

public class ForgottenTorii extends AbstractImageEvent {
    public static final String ID = IdHelper.makePath("ForgottenTorii");
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);
    public static final String NAME = eventStrings.NAME;
    public static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    public static final String[] OPTIONS = eventStrings.OPTIONS;

    private int screenNum = 0;

    public ForgottenTorii() {
        super(NAME, DESCRIPTIONS[0], ImageHelper.get_event_img("ForgottenTorii"));

        this.imageEventText.setDialogOption(OPTIONS[0]); // 获得2个灵符
        this.imageEventText.setDialogOption(OPTIONS[1]); // 将生命值降低至15，获得5个灵符
        this.imageEventText.setDialogOption(OPTIONS[2]); // 什么都不做，离开
    }

    @Override
    protected void buttonEffect(int buttonPressed) {
        AbstractPlayer p = AbstractDungeon.player;
        switch (screenNum) {
            case 0:
                switch (buttonPressed) {
                    case 0:
                        // 选项1：获得2个灵符
                        OfudaManager.addOfuda(2);
                        
                        this.imageEventText.updateBodyText(DESCRIPTIONS[1]);
                        this.imageEventText.clearAllDialogs();
                        this.imageEventText.setDialogOption(OPTIONS[3]); // 离开
                        screenNum = 1;
                        break;

                    case 1:
                        // 选项2：将生命值降低至15，获得5个灵符
                        int hpToLose = p.currentHealth - 15;
                        if (hpToLose > 0) {
                            AbstractDungeon.player.damage(new DamageInfo(null, hpToLose));
                            CardCrawlGame.sound.play("ATTACK_POISON");
                        }
                        
                        // 获得6个灵符
                        OfudaManager.addOfuda(5);
                        
                        this.imageEventText.updateBodyText(DESCRIPTIONS[2]);
                        this.imageEventText.clearAllDialogs();
                        this.imageEventText.setDialogOption(OPTIONS[3]); // 离开
                        screenNum = 1;
                        break;

                    case 2:
                        // 选项3：什么都不做，离开
                        this.imageEventText.updateBodyText(DESCRIPTIONS[3]);
                        this.imageEventText.clearAllDialogs();
                        this.imageEventText.setDialogOption(OPTIONS[3]); // 离开
                        screenNum = 1;
                        break;
                }
                break;
            case 1:
                this.openMap();
                break;
        }
    }
}