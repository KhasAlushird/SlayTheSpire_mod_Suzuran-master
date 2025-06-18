package suzuranmod.character;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;

import basemod.abstracts.CustomEnergyOrb;
import suzuranmod.helpers.ImageHelper;

public class Foxfire extends CustomEnergyOrb {
    private Texture border;

    private static final float ORB_IMG_SCALE = 1.30F * Settings.scale;

    public Foxfire(String[] orbTexturePaths, String orbVfxPath, float[] layerSpeeds) {
        super(orbTexturePaths, orbVfxPath, layerSpeeds);
        // 加载边框图片
        String borderPath = ImageHelper.getImgPathWithSubType("ui","orb2","border");
        System.out.println("[Foxfire] borderPath=" + borderPath);
        try {
            border = new Texture(borderPath);
            System.out.println("[Foxfire] border loaded successfully");
        } catch (Exception e) {
            System.out.println("[Foxfire] border load failed: " + e.getMessage());
            border = null;
        }
    }

    @Override
    public void renderOrb(SpriteBatch sb, boolean enabled, float current_x, float current_y) {
        System.out.println("[Foxfire] renderOrb called, enabled=" + enabled + ", x=" + current_x + ", y=" + current_y);
        sb.setColor(com.badlogic.gdx.graphics.Color.WHITE);
        if (enabled) {
            for (int i = 0; i < this.energyLayers.length; i++) {
                System.out.println("[Foxfire] Drawing energyLayer " + i);
                sb.draw(this.energyLayers[i], current_x - 64.0F, current_y - 64.0F, 64.0F, 64.0F, 128.0F, 128.0F, ORB_IMG_SCALE, ORB_IMG_SCALE, this.angles[i], 0, 0, 128, 128, false, false);
            }
        } 
        // else {
        //     for (int i = 0; i < this.noEnergyLayers.length; i++) {
        //         System.out.println("[Foxfire] Drawing noEnergyLayer " + i);
        //         sb.draw(this.noEnergyLayers[i], current_x - 64.0F, current_y - 64.0F, 64.0F, 64.0F, 128.0F, 128.0F, ORB_IMG_SCALE, ORB_IMG_SCALE, this.angles[i], 0, 0, 128, 128, false, false);
        //     }
        // }
        // sb.draw(this.baseLayer, current_x - 64.0F, current_y - 64.0F, 64.0F, 64.0F, 128.0F, 128.0F, ORB_IMG_SCALE, ORB_IMG_SCALE, 0.0F, 0, 0, 128, 128, false, false);
        // 渲染边框
        if (border != null) {
            System.out.println("[Foxfire] Drawing border");
            sb.draw(
                border,
                current_x - 64.0F, current_y - 64.0F,
                64.0F, 64.0F, // originX, originY
                128.0F, 128.0F, // width, height
                ORB_IMG_SCALE, ORB_IMG_SCALE, // scaleX, scaleY
                0.0F, // rotation
                0, 0, 128, 128, // srcX, srcY, srcWidth, srcHeight
                false, false // flipX, flipY
            );
        } else {
            System.out.println("[Foxfire] border is null, not drawing border");
        }
    }
}