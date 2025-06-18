package suzuranmod.modcore;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.MathHelper;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.ui.panels.AbstractPanel;

import suzuranmod.character.Foxfire;
import suzuranmod.helpers.ImageHelper;
import suzuranmod.powers.BurnoutPower;

public class FoxfirePanel extends AbstractPanel {
    private static final UIStrings uiStrings;
    private Hitbox tipHitbox;
    private Texture gainEnergyImg;
    private float energyVfxAngle;
    private float energyVfxScale;
    private Color energyVfxColor;
    public static float fontScale;
    public static float energyVfxTimer;
    private static final Color ENERGY_TEXT_COLOR;
    public static int totalCount;
    public Foxfire Foxfire;
    public static final float FOXFIRE_X = 200.0F  * Settings.xScale;
    public static final float FOXFIRE_Y = 360.0F * Settings.yScale; // 可调整

    // 资源路径和速度
    public static final String[] FOXFIRE_TEXTURES = {
        ImageHelper.getImgPathWithSubType("ui","orb2","l1"),
        ImageHelper.getImgPathWithSubType("ui","orb2","l2"),
        ImageHelper.getImgPathWithSubType("ui","orb2","l3"),
        ImageHelper.getImgPathWithSubType("ui","orb2","l4"),
        ImageHelper.getImgPathWithSubType("ui","orb2","l4"),
    };
    public static final String FOXFIRE_VFX = ImageHelper.getImgPathWithSubType("ui","orb2","vfx");
    public static final float[] FOXFIRE_LAYER_SPEEDS = { -80, 72f, -80f, 60f, 0 };
    // public static final float[] FOXFIRE_LAYER_SPEEDS = { 0, 0f, 0f, 0f, 0f };

    public FoxfirePanel() {
        super(FOXFIRE_X, FOXFIRE_Y,  FOXFIRE_X, FOXFIRE_Y,FOXFIRE_X, FOXFIRE_Y,null, true);
        System.out.println("[FoxfirePanel] 构造函数被调用");
        this.tipHitbox = new Hitbox(0.0F, 0.0F, 108.0F * Settings.scale, 108.0F * Settings.scale);
        this.energyVfxAngle = 0.0F;
        this.energyVfxScale = Settings.scale;
        this.energyVfxColor = Color.WHITE.cpy();
        this.gainEnergyImg = AbstractDungeon.player != null ? AbstractDungeon.player.getEnergyImage() : null;
        this.Foxfire = new Foxfire(FOXFIRE_TEXTURES, FOXFIRE_VFX, FOXFIRE_LAYER_SPEEDS);
        totalCount = 6;
        System.out.println("[FoxfirePanel] 构造完成，totalCount=" + totalCount);
    }

    public static void setEnergy(int energy) {
        if(totalCount != energy){
            totalCount = energy;
            fontScale = 2.0F;
            energyVfxTimer = 2.0F;
        }

        System.out.println("[FoxfirePanel] setEnergy: " + energy);
    }

    public static void addEnergy(int e) {
        if (AbstractDungeon.player != null && AbstractDungeon.player.hasPower(BurnoutPower.POWER_ID)) {
        return;
    }
        setEnergy(totalCount+e);
    }

    public static void useEnergy(int e) {
        int before = totalCount;
       setEnergy(Math.max(0,totalCount-e));
       if (before > 0 && totalCount == 0 && AbstractDungeon.player != null && !AbstractDungeon.player.hasPower(BurnoutPower.POWER_ID)) {
        // 获得一层BurnoutPower
        AbstractDungeon.actionManager.addToBottom(
            new com.megacrit.cardcrawl.actions.common.ApplyPowerAction(
                AbstractDungeon.player,
                AbstractDungeon.player,
                new BurnoutPower(AbstractDungeon.player, 1),
                1
            )
        );
         // 施加-99层敏捷
        AbstractDungeon.actionManager.addToBottom(
            new com.megacrit.cardcrawl.actions.common.ApplyPowerAction(
                AbstractDungeon.player,
                AbstractDungeon.player,
                new com.megacrit.cardcrawl.powers.DexterityPower(AbstractDungeon.player, -99),
                -99
            )
        );
        // 将生命值降至1
        AbstractDungeon.actionManager.addToBottom(
            new com.megacrit.cardcrawl.actions.common.LoseHPAction(
                AbstractDungeon.player,
                AbstractDungeon.player,
                AbstractDungeon.player.currentHealth - 1
            )
        );
 
    }
    }

    public void update() {
        System.out.println("[FoxfirePanel] update() called, totalCount=" + totalCount);
        System.out.println("[FoxfirePanel] update() called, fontScale=" + fontScale);
        this.Foxfire.updateOrb(totalCount);
        updateVfx();
        if (fontScale != 1.0F)
            fontScale = MathHelper.scaleLerpSnap(fontScale, 1.0F);
        this.tipHitbox.update();
        if (this.tipHitbox.hovered && !AbstractDungeon.isScreenUp)
            AbstractDungeon.overlayMenu.hoveredTip = true;
    }

    private void updateVfx() {
        if (energyVfxTimer != 0.0F) {
            this.energyVfxColor.a = Interpolation.exp10In.apply(0.5F, 0.0F, 1.0F - energyVfxTimer / 2.0F);
            this.energyVfxAngle += Gdx.graphics.getDeltaTime() * -30.0F;
            this.energyVfxScale = Settings.scale * Interpolation.exp10In.apply(1.0F, 0.1F, 1.0F - energyVfxTimer / 2.0F);
            energyVfxTimer -= Gdx.graphics.getDeltaTime();
            if (energyVfxTimer < 0.0F) {
                energyVfxTimer = 0.0F;
                this.energyVfxColor.a = 0.0F;
            }
        }
    }

    public void render(SpriteBatch sb) {
            // 只在战斗房间渲染
        if (AbstractDungeon.getCurrRoom() == null || AbstractDungeon.getCurrRoom().phase != AbstractRoom.RoomPhase.COMBAT) {
            return;
        }
        System.out.println("[FoxfirePanel] render() called, totalCount=" + totalCount);
        this.tipHitbox.move(this.current_x, this.current_y);
        renderOrb(sb);
        renderVfx(sb);
        String energyMsg = String.valueOf(totalCount);
        // if (AbstractDungeon.player != null)
        //     AbstractDungeon.player.getEnergyNumFont().getData().setScale(fontScale);
        // 判空，防止NPE
        if (FontHelper.energyNumFontRed != null && ENERGY_TEXT_COLOR != null && energyMsg != null) {
            FontHelper.energyNumFontRed.getData().setScale(fontScale);
            FontHelper.renderFontCentered(sb, FontHelper.energyNumFontRed, energyMsg, this.current_x, this.current_y, ENERGY_TEXT_COLOR);
        } else {
            System.out.println("[FoxfirePanel] FontHelper.energyNumFontRed or ENERGY_TEXT_COLOR is null!");
        }
        this.tipHitbox.render(sb);
        if (this.tipHitbox.hovered && (AbstractDungeon.getCurrRoom() != null) && (AbstractDungeon.getCurrRoom()).phase == AbstractRoom.RoomPhase.COMBAT && !AbstractDungeon.isScreenUp)
            TipHelper.renderGenericTip(70.0F * Settings.scale, 580.0F * Settings.scale, uiStrings.TEXT[0], uiStrings.TEXT[1]);
}
    private void renderOrb(SpriteBatch sb) {
        System.out.println("[FoxfirePanel] renderOrb() called, totalCount=" + totalCount);
        if (totalCount == 0) {
            this.Foxfire.renderOrb(sb, false, this.current_x, this.current_y);
        } else {
            this.Foxfire.renderOrb(sb, true, this.current_x, this.current_y);
        }
    }

    private void renderVfx(SpriteBatch sb) {
        if (energyVfxTimer != 0.0F && gainEnergyImg != null) {
            sb.setBlendFunction(770, 1);
            sb.setColor(this.energyVfxColor);
            sb.draw(this.gainEnergyImg, this.current_x - 128.0F, this.current_y - 128.0F, 128.0F, 128.0F, 256.0F, 256.0F, this.energyVfxScale, this.energyVfxScale, -this.energyVfxAngle + 50.0F, 0, 0, 256, 256, true, false);
            sb.draw(this.gainEnergyImg, this.current_x - 128.0F, this.current_y - 128.0F, 128.0F, 128.0F, 256.0F, 256.0F, this.energyVfxScale, this.energyVfxScale, this.energyVfxAngle, 0, 0, 256, 256, false, false);
            sb.setBlendFunction(770, 771);
        }
    }

    public static int getCurrentEnergy() {
        return (AbstractDungeon.player == null) ? 0 : totalCount;
    }

    static {
        uiStrings = CardCrawlGame.languagePack.getUIString("Suzuran:FoxfireEnergy"); // 你需要在本地化文件中添加对应key
        ENERGY_TEXT_COLOR = Color.GOLD.cpy();
        fontScale = 1.0F;
        totalCount = 6;
        energyVfxTimer = 0.0F;
        System.out.println("[FoxfirePanel] static block loaded");
    }
}