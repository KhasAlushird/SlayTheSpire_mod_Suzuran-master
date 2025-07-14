package suzuranmod.patches;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.ui.panels.TopPanel;

import suzuranmod.character.Suzuran;
import suzuranmod.helpers.ImageHelper;
import suzuranmod.modcore.OfudaManager;

@SpirePatch(clz = TopPanel.class, method = "render")
public class OfudaPatch {
    private static final String OFUDA_IMG_PATH = ImageHelper.getImgPathWithSubType("ui","toppanel","ofuda");
    private static final Texture OFUDA_IMG = ImageMaster.loadImage(OFUDA_IMG_PATH);
    private static final float ICON_SIZE = 52.0F * Settings.scale;
    private static final float ICON_OFFSET_X = -40F * Settings.scale; // 图标整体左移
    private static final float ICON_OFFSET_Y = 18F * Settings.scale;

    private static float ofudaX = 0;
    private static float ofudaY = 0;
    private static float ofudaW = ICON_SIZE;
    private static float ofudaH = ICON_SIZE;

    private static final int OFUDA_MAX = 12; // 灵符上限

    // 本地化KEY
    private static final String OFUDA_TIP_KEY = "Suzuran:OfudaTip";
    private static final UIStrings ofudaTipStrings = CardCrawlGame.languagePack.getUIString(OFUDA_TIP_KEY);

    @SpirePostfixPatch
    public static void renderOfuda(TopPanel __instance, SpriteBatch sb) {
        // 1. 只有角色为Suzuran时才渲染
        if (!(AbstractDungeon.player instanceof Suzuran)) {
            return;
        }

        // 图标和说明框位置
        float x = Settings.WIDTH - 350.0F * Settings.scale + ICON_OFFSET_X;
        float y = Settings.HEIGHT - 80.0F * Settings.scale + ICON_OFFSET_Y;
        ofudaX = x;
        ofudaY = y;

        // 数字位置（不随图标左移）
        float numberX = Settings.WIDTH - 350.0F * Settings.scale + ICON_SIZE + 28.0F * Settings.scale;
        float numberY = y + ICON_SIZE * 0.8F;

        boolean hovered = InputHelper.mX >= ofudaX && InputHelper.mX <= ofudaX + ofudaW
                && InputHelper.mY >= ofudaY && InputHelper.mY <= ofudaY + ofudaH;

        OfudaManager.updateScale();
        
        float scale = hovered ? 1.2f : OfudaManager.ofudaScale;
        float drawSize = ICON_SIZE * scale;
        float drawX = x - (drawSize - ICON_SIZE) / 2f;
        float drawY = y - (drawSize - ICON_SIZE) / 2f;

        if (hovered) {
            sb.setColor(Color.valueOf("B2FFFF"));
        } else {
            sb.setColor(Color.WHITE);
        }
        sb.draw(OFUDA_IMG, drawX, drawY, drawSize, drawSize);

        // 3. 显示“当前/上限”，数字不随图标左移
        String countStr = OfudaManager.getOfuda() + "/" + OFUDA_MAX;
        FontHelper.renderFontRightTopAligned(
                sb, FontHelper.topPanelAmountFont,
                countStr,
                numberX, numberY,
                Color.CYAN
        );

        // 2. 悬停时显示说明框（多语种支持，使用 queuePowerTips）
        if (hovered) {
            java.util.ArrayList<PowerTip> tips = new java.util.ArrayList<>();
            // HEADER
            tips.add(new PowerTip("#y" + ofudaTipStrings.TEXT[0], ofudaTipStrings.TEXT[1]));
            // BODY（奖励列表）
            for (int i = 2; i < ofudaTipStrings.TEXT.length-4; i+=2) {
                // 你可以根据需要决定是否加 #y 或其它格式
                tips.add(new PowerTip(ofudaTipStrings.TEXT[i], ofudaTipStrings.TEXT[i+1]));
            }

            int tailRewardCount = OfudaManager.getTailRewardCount();
            String tail_String;
            if (tailRewardCount == 0) {
                tail_String =  ofudaTipStrings.TEXT[25]; // "获得遗物 三尾 (下一次将获得 六尾)"
            } else if (tailRewardCount == 1) {
                tail_String = ofudaTipStrings.TEXT[26]; // "获得遗物 六尾 (下一次将获得 九尾)"
            } else {
                tail_String = ofudaTipStrings.TEXT[27]; // "获得遗物 九尾"
            }
            tips.add(new PowerTip(ofudaTipStrings.TEXT[24],tail_String ));

            com.megacrit.cardcrawl.helpers.TipHelper.queuePowerTips(
                x + ICON_SIZE * 1.2f, y + ICON_SIZE * 0.8f, tips
            );
        }
    }
}