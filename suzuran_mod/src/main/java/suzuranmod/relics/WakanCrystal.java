package suzuranmod.relics;

import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import basemod.abstracts.CustomRelic;
import suzuranmod.helpers.IdHelper;
import suzuranmod.helpers.ImageHelper;
import suzuranmod.modcore.OfudaManager;

public class WakanCrystal extends CustomRelic {
    public static final String ID = IdHelper.makePath("WakanCrystal");
    private static final String IMG_PATH = ImageHelper.getOtherImgPath("relics", "WakanCrystal");
    private static final RelicTier RELIC_TIER = RelicTier.BOSS;
    private static final LandingSound LANDING_SOUND = LandingSound.CLINK;

    public WakanCrystal() {
        super(ID, ImageMaster.loadImage(IMG_PATH), RELIC_TIER, LANDING_SOUND);
    }

    @Override
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    // 拾取时立即获得2个ofuda
    @Override
    public void onEquip() {
        super.onEquip();
        OfudaManager.addOfuda(1);
        this.flash();
    }

    @Override
    public AbstractRelic makeCopy() {
        return new WakanCrystal();
    }
}