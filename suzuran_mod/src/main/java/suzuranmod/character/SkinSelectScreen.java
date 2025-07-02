package suzuranmod.character;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.esotericsoftware.spine.AnimationState;
import com.esotericsoftware.spine.AnimationStateData;
import com.esotericsoftware.spine.Skeleton;
import com.esotericsoftware.spine.SkeletonData;
import com.esotericsoftware.spine.SkeletonJson;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.input.InputHelper;

import basemod.BaseMod;
import basemod.abstracts.CustomSavable;
import basemod.interfaces.ISubscriber;
import suzuranmod.helpers.IdHelper;
import suzuranmod.helpers.ImageHelper;

public class SkinSelectScreen implements ISubscriber, CustomSavable<Integer> {
    private static final String[] TEXT;
    
    private static final ArrayList<Skin> SKINS = new ArrayList<>();
    
    public static SkinSelectScreen Inst;
    
    public Hitbox leftHb;
    public Hitbox rightHb;
    
    public TextureAtlas atlas;
    public Skeleton skeleton;
    public AnimationStateData stateData;
    public AnimationState state;
    
    public String curName = "";
    public String nextName = "";
    public int index;
    
    static {
        // 加载UI文本
        TEXT = CardCrawlGame.languagePack.getUIString(IdHelper.makePath("SkinSelectScreen")).TEXT;
        
        // 添加皮肤
        SKINS.add(new Skin(0, "default"));
        SKINS.add(new Skin(1, "wild"));
        
        Inst = new SkinSelectScreen();
    }
    
    public static Skin getSkin() {
        return SKINS.get(Inst.index);
    }
    
    public SkinSelectScreen() {
        this.index = 0;
        refresh();
        this.leftHb = new Hitbox(70.0F * Settings.scale, 70.0F * Settings.scale);
        this.rightHb = new Hitbox(70.0F * Settings.scale, 70.0F * Settings.scale);
        BaseMod.subscribe(this);
        BaseMod.addSaveField(IdHelper.makePath("skin"), this);
    }
    
    public void loadAnimation(String atlasUrl, String skeletonUrl, float scale) {
        if (!Gdx.files.internal(atlasUrl).exists() || !Gdx.files.internal(skeletonUrl).exists()) {
            return;
        }
        
        this.atlas = new TextureAtlas(Gdx.files.internal(atlasUrl));
        SkeletonJson json = new SkeletonJson(this.atlas);
        json.setScale(Settings.renderScale / (scale*0.75F));
        SkeletonData skeletonData = json.readSkeletonData(Gdx.files.internal(skeletonUrl));
        this.skeleton = new Skeleton(skeletonData);
        this.skeleton.setColor(Color.WHITE);
        this.stateData = new AnimationStateData(skeletonData);
        this.state = new AnimationState(this.stateData);
        AnimationState.TrackEntry e = this.state.setAnimation(0, "Idle", true);
        e.setTimeScale(1.2F);
    }
    
    public void refresh() {
        Skin skin = SKINS.get(this.index);
        this.curName = skin.name;
        loadAnimation(skin.charPath + ".atlas", skin.charPath + ".json", 1.5F);
        this.nextName = SKINS.get(nextIndex()).name;
        
        // 如果当前在游戏中，刷新角色皮肤
        if (AbstractDungeon.player instanceof Suzuran) {
            ((Suzuran) AbstractDungeon.player).refreshSkin();
        }
    }
    
    public int prevIndex() {
        return (this.index - 1 < 0) ? (SKINS.size() - 1) : (this.index - 1);
    }
    
    public int nextIndex() {
        return (this.index + 1 > SKINS.size() - 1) ? 0 : (this.index + 1);
    }
    
    public void update() {
        float centerX = Settings.WIDTH * 0.75F;
        float centerY = Settings.HEIGHT * 0.5F;
        this.leftHb.move(centerX - 200.0F * Settings.scale, centerY);
        this.rightHb.move(centerX + 200.0F * Settings.scale, centerY);
        updateInput();
    }
    
    private void updateInput() {
        if (CardCrawlGame.chosenCharacter == Suzuran.PlayerColorEnum.Suzuran_CHARACTER) {
            this.leftHb.update();
            this.rightHb.update();
            
            if (this.leftHb.clicked) {
                this.leftHb.clicked = false;
                CardCrawlGame.sound.play("UI_CLICK_1");
                this.index = prevIndex();
                refresh();
            }
            
            if (this.rightHb.clicked) {
                this.rightHb.clicked = false;
                CardCrawlGame.sound.play("UI_CLICK_1");
                this.index = nextIndex();
                refresh();
            }
            
            if (InputHelper.justClickedLeft) {
                if (this.leftHb.hovered) {
                    this.leftHb.clickStarted = true;
                }
                if (this.rightHb.hovered) {
                    this.rightHb.clickStarted = true;
                }
            }
        }
    }
    
    public void render(SpriteBatch sb) {
        Color RC = Color.valueOf("bacdbaff");
        float centerX = Settings.WIDTH * 0.75F;
        float centerY = Settings.HEIGHT * 0.5F;
        
        renderSkin(sb, centerX, centerY);
        
        // 渲染标题
        FontHelper.renderFontCentered(sb, FontHelper.cardTitleFont, TEXT[0], 
                                     centerX, centerY + 250.0F * Settings.scale, Color.WHITE, 1.5F);
        
        // 渲染当前皮肤名称
        FontHelper.renderFontCentered(sb, FontHelper.cardTitleFont, this.curName, centerX, centerY, RC);
        
        // 渲染左箭头
        if (this.leftHb.hovered) {
            sb.setColor(Color.LIGHT_GRAY);
        } else {
            sb.setColor(Color.WHITE);
        }
        sb.draw(ImageMaster.CF_LEFT_ARROW, 
                this.leftHb.cX - 24.0F, this.leftHb.cY - 24.0F, 
                24.0F, 24.0F, 48.0F, 48.0F, 
                Settings.scale, Settings.scale, 0.0F, 
                0, 0, 48, 48, false, false);
        
        // 渲染右箭头
        if (this.rightHb.hovered) {
            sb.setColor(Color.LIGHT_GRAY);
        } else {
            sb.setColor(Color.WHITE);
        }
        sb.draw(ImageMaster.CF_RIGHT_ARROW, 
                this.rightHb.cX - 24.0F, this.rightHb.cY - 24.0F, 
                24.0F, 24.0F, 48.0F, 48.0F, 
                Settings.scale, Settings.scale, 0.0F, 
                0, 0, 48, 48, false, false);
        
        this.rightHb.render(sb);
        this.leftHb.render(sb);
    }
    
    public void renderSkin(SpriteBatch sb, float x, float y) {
        if (this.atlas == null || this.skeleton == null) {
            return;
        }
        
        this.state.update(Gdx.graphics.getDeltaTime());
        this.state.apply(this.skeleton);
        this.skeleton.updateWorldTransform();
        this.skeleton.setPosition(x, y);
        
        sb.end();
        CardCrawlGame.psb.begin();
        com.megacrit.cardcrawl.core.AbstractCreature.sr.draw(CardCrawlGame.psb, this.skeleton);
        CardCrawlGame.psb.end();
        sb.begin();
    }
    
    public static class Skin {
        public String charPath;
        public String shoulder;
        public String portrait;
        public String name;
        
        public Skin(int index, String skinId) {
            this.charPath = ImageHelper.get_char_res("models", skinId);
            this.shoulder = ImageHelper.get_char_res("shoulders", skinId);
            this.portrait = ImageHelper.get_char_res("portraits", skinId);
            this.name = TEXT[index + 1]; // TEXT[0]是标题，从TEXT[1]开始是皮肤名称
        }
    }
    
    @Override
    public void onLoad(Integer savedIndex) {
        this.index = savedIndex;
        refresh();
    }
    
    @Override
    public Integer onSave() {
        return this.index;
    }
}