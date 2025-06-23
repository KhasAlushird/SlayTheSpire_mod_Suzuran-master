package suzuranmod.options;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.RestRoom;
import com.megacrit.cardcrawl.ui.campfire.AbstractCampfireOption;

import suzuranmod.helpers.ImageHelper;
import suzuranmod.modcore.OfudaManager;

public class OfudaCampfireOption extends AbstractCampfireOption {
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString("Suzuran:OfudaCampfireOption");
    public static final String[] TEXT = uiStrings.TEXT;
    private static final String IMG_PATH =ImageHelper.getImgPathWithSubType("ui","campfire","OfudaOption");
    private int ofudaAmount = 2;
    public OfudaCampfireOption(boolean bonus) {
        this.label = TEXT[0];
        this.usable = true;
        if (bonus == true) {
            this.description = TEXT[2]; // "获得4个灵符"
            this.ofudaAmount = 4;
        } else {
            this.description = TEXT[1]; // "获得2个灵符"
            this.ofudaAmount = 2;
        }
        this.img = ImageMaster.loadImage(IMG_PATH); // 你可以自定义图片路径
    }

    @Override
    public void useOption() {
        if (this.usable) {
            OfudaManager.addOfuda(this.ofudaAmount);
            AbstractDungeon.effectList.add(new OfudaCampfireEffect());
             ((RestRoom)AbstractDungeon.getCurrRoom()).phase = AbstractRoom.RoomPhase.COMPLETE;
        }
    }
}