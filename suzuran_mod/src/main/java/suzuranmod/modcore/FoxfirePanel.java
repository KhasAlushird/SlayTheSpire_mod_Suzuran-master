package suzuranmod.modcore;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.MathHelper;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.NoBlockPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.ui.panels.AbstractPanel;

import suzuranmod.character.Foxfire;
import suzuranmod.character.Suzuran;
import suzuranmod.character.SuzuranCard;
import suzuranmod.helpers.ImageHelper;
import suzuranmod.patches.SuzuranCardTagsPatch;
import suzuranmod.powers.BurnoutPower;
import suzuranmod.powers.FireBreathPower;
import suzuranmod.relics.AmuletInArm;

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
    public static final float FOXFIRE_X = 200.0F * Settings.xScale;
    public static final float FOXFIRE_Y = 360.0F * Settings.yScale; // 可调整
    private static final float ANIM_START_X = -200.0F * Settings.xScale;

    // 动画插值变量
    private float animX;
    private float animTargetX;
    private boolean lastSettings = false;
    private boolean isSlidingOut = false;

    // 资源路径和速度
    public static final String[] FOXFIRE_TEXTURES = {
        ImageHelper.getImgPathWithSubType("ui", "orb2", "l1"),
        ImageHelper.getImgPathWithSubType("ui", "orb2", "l2"),
        ImageHelper.getImgPathWithSubType("ui", "orb2", "l3"),
        ImageHelper.getImgPathWithSubType("ui", "orb2", "l4"),
        ImageHelper.getImgPathWithSubType("ui", "orb2", "l4"),
    };
    public static final String FOXFIRE_VFX = ImageHelper.getImgPathWithSubType("ui", "orb2", "vfx");
    public static final float[] FOXFIRE_LAYER_SPEEDS = { -80, 72f, -80f, 60f, 0 };

    public FoxfirePanel(int init_count) {
        super(
            FOXFIRE_X, FOXFIRE_Y,
            ANIM_START_X, FOXFIRE_Y,
            12.0F * Settings.scale, -12.0F * Settings.scale,
            null, true
        );
        this.tipHitbox = new Hitbox(0.0F, 0.0F, 108.0F * Settings.scale, 108.0F * Settings.scale);
        this.energyVfxAngle = 0.0F;
        this.energyVfxScale = Settings.scale;
        this.energyVfxColor = Color.WHITE.cpy();
        this.gainEnergyImg = AbstractDungeon.player != null ? AbstractDungeon.player.getEnergyImage() : null;
        this.Foxfire = new Foxfire(FOXFIRE_TEXTURES, FOXFIRE_VFX, FOXFIRE_LAYER_SPEEDS);
        totalCount = init_count;

        // 动画变量初始化
        this.animX = ANIM_START_X;
        this.animTargetX = FOXFIRE_X;
        this.lastSettings = false;
        this.isSlidingOut = false;
    }

    public static void setEnergy(int energy) {
        setEnergy(energy, false);
    }

    public static void setEnergy(int energy, boolean natural_down) {
        if (totalCount != energy) {
            totalCount = energy;
            fontScale = 2.0F;
            energyVfxTimer = 2.0F;

            updateHandCardsDescription();

            if (AbstractDungeon.player != null && AbstractDungeon.player.hasPower(FireBreathPower.POWER_ID)) {
                FireBreathPower power = (FireBreathPower) AbstractDungeon.player.getPower(FireBreathPower.POWER_ID);
                power.onTrigger(natural_down);
            }
        }
    }

    private static void updateHandCardsDescription() {
        if (AbstractDungeon.player != null && AbstractDungeon.player.hand != null) {
            for (com.megacrit.cardcrawl.cards.AbstractCard card : AbstractDungeon.player.hand.group) {
                // 检查是否是需要动态更新的卡牌
                if (card.tags.contains(SuzuranCardTagsPatch.FFRESPONCE)) {
                    ((SuzuranCard) card).update_FF_des();
                }
            }
        }
    }

    public static void addEnergy(int e) {
        addEnergy(e, false);
    }

    public static void addEnergy(int e, boolean natural_down) {
        if (AbstractDungeon.player != null && AbstractDungeon.player.hasPower(BurnoutPower.POWER_ID)) {
            return;
        }
        setEnergy(totalCount + e, natural_down);
    }

    public static void useEnergy(int e) {
        useEnergy(e, false);
    }

    public static void useEnergy(int e, boolean natural_down) {
        if(!(AbstractDungeon.player instanceof Suzuran)){
            return;
        }
        int before = totalCount;
        setEnergy(Math.max(0, totalCount - e), natural_down);

        if (totalCount <= 3) {
            if (AbstractDungeon.player != null && AbstractDungeon.player.hasRelic("SuzuranKhas:Bloom")) {
                AbstractRelic bloom = AbstractDungeon.player.getRelic("SuzuranKhas:Bloom");
                if (bloom instanceof suzuranmod.relics.Bloom) {
                    ((suzuranmod.relics.Bloom) bloom).trigger3(natural_down);
                }
            }
        }

        if (totalCount <= 1) {
            if (AbstractDungeon.player != null && AbstractDungeon.player.hasRelic("SuzuranKhas:Grow")) {
                AbstractRelic grow = AbstractDungeon.player.getRelic("SuzuranKhas:Grow");
                if (grow instanceof suzuranmod.relics.Grow) {
                    ((suzuranmod.relics.Grow) grow).trigger(natural_down);
                }
            }

            if (AbstractDungeon.player != null && AbstractDungeon.player.hasRelic("SuzuranKhas:Bloom")) {
                AbstractRelic bloom = AbstractDungeon.player.getRelic("SuzuranKhas:Bloom");
                if (bloom instanceof suzuranmod.relics.Bloom) {
                    ((suzuranmod.relics.Bloom) bloom).trigger1(natural_down);
                }
            }
        }
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
            if (AbstractDungeon.player != null && !AbstractDungeon.player.hasPower("Artifact")) {
                for (com.megacrit.cardcrawl.relics.AbstractRelic relic : AbstractDungeon.player.relics) {
                    if (relic instanceof AmuletInArm) {
                        ((AmuletInArm) relic).onBurnoutApplied();
                    }
                }
            }
            // 施加-99层无格挡power
            AbstractDungeon.actionManager.addToBottom(
                (AbstractGameAction)new ApplyPowerAction((AbstractCreature)AbstractDungeon.player, 
                (AbstractCreature)AbstractDungeon.player, 
                (AbstractPower)new NoBlockPower((AbstractCreature)AbstractDungeon.player, 99, false), 99));
                
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
    
        // 监听设置界面开关，决定动画目标位置
        boolean nowSettings = AbstractDungeon.isScreenUp && AbstractDungeon.screen == AbstractDungeon.CurrentScreen.SETTINGS;
        if (nowSettings != lastSettings) {
            if (nowSettings) {
                this.animTargetX = ANIM_START_X; // 滑到左边
            } else {
                this.animTargetX = FOXFIRE_X; // 滑回来
            }
            lastSettings = nowSettings;
        }

        if (isSlidingOut) {
             this.animTargetX = ANIM_START_X;
        // 动画滑出完成后，自动关闭滑出状态
        if (Math.abs(this.animX - ANIM_START_X) < 1.0f) {
            isSlidingOut = false;
        }
    }

  

        // 插值动画（速度和原版能量接近）
        this.animX = MathHelper.uiLerpSnap(this.animX, this.animTargetX);

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

    @Override
    public void render(SpriteBatch sb) {
        boolean inCombat = AbstractDungeon.getCurrRoom() != null && AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT;
    // 非战斗阶段但正在滑出动画时，依然渲染
        if (!inCombat && !isSlidingOut) {
            return;
     }
        this.tipHitbox.move(this.animX, this.current_y);
        renderOrb(sb);
        renderVfx(sb);
        String energyMsg = String.valueOf(totalCount);
        if (FontHelper.energyNumFontRed != null && ENERGY_TEXT_COLOR != null && energyMsg != null) {
            FontHelper.energyNumFontRed.getData().setScale(fontScale);
            FontHelper.renderFontCentered(sb, FontHelper.energyNumFontRed, energyMsg, this.animX, this.current_y, ENERGY_TEXT_COLOR);
        }
        this.tipHitbox.render(sb);
        if (this.tipHitbox.hovered && (AbstractDungeon.getCurrRoom() != null) && (AbstractDungeon.getCurrRoom()).phase == AbstractRoom.RoomPhase.COMBAT && !AbstractDungeon.isScreenUp)
            TipHelper.renderGenericTip(70.0F * Settings.scale, 580.0F * Settings.scale, uiStrings.TEXT[0], uiStrings.TEXT[1]);
    }

    private void renderOrb(SpriteBatch sb) {
        if (totalCount == 0) {
            this.Foxfire.renderOrb(sb, false, this.animX, this.current_y);
        } else {
            this.Foxfire.renderOrb(sb, true, this.animX, this.current_y);
        }
    }

    private void renderVfx(SpriteBatch sb) {
        if (energyVfxTimer != 0.0F && gainEnergyImg != null) {
            sb.setBlendFunction(770, 1);
            sb.setColor(this.energyVfxColor);
            sb.draw(this.gainEnergyImg, this.animX - 128.0F, this.current_y - 128.0F, 128.0F, 128.0F, 256.0F, 256.0F, this.energyVfxScale, this.energyVfxScale, -this.energyVfxAngle + 50.0F, 0, 0, 256, 256, true, false);
            sb.draw(this.gainEnergyImg, this.animX - 128.0F, this.current_y - 128.0F, 128.0F, 128.0F, 256.0F, 256.0F, this.energyVfxScale, this.energyVfxScale, this.energyVfxAngle, 0, 0, 256, 256, false, false);
            sb.setBlendFunction(770, 771);
        }
    }


    public static int getCurrentEnergy() {
        return (AbstractDungeon.player == null) ? 0 : totalCount;
    }

    public void slideOut() {
        this.animTargetX = ANIM_START_X;
        this.isSlidingOut = true;
    }

    static {
        uiStrings = CardCrawlGame.languagePack.getUIString("Suzuran:FoxfireEnergy");
        ENERGY_TEXT_COLOR = Color.GOLD.cpy();
        fontScale = 1.0F;
        energyVfxTimer = 0.0F;
    }
}