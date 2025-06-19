package suzuranmod.character;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.AnimationState;
import com.evacipated.cardcrawl.modthespire.lib.SpireEnum;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.EnergyManager;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.cutscenes.CutscenePanel;
import com.megacrit.cardcrawl.events.city.Vampires;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.localization.CharacterStrings;
import com.megacrit.cardcrawl.screens.CharSelectInfo;

import basemod.abstracts.CustomPlayer;
import suzuranmod.cards.attack.FoxfireStrike;
import suzuranmod.cards.attack.Strike;
import suzuranmod.cards.skill.Defend;
import suzuranmod.cards.skill.Light;
import static suzuranmod.character.Suzuran.PlayerColorEnum.Suzuran_CHARACTER;
import static suzuranmod.character.Suzuran.PlayerColorEnum.Suzuran_COLOR;
import suzuranmod.helpers.ImageHelper;
import suzuranmod.modcore.TheCore;

//注意：人物选择界面的立绘在TheCore文件里面
// 继承CustomPlayer类
public class Suzuran extends CustomPlayer {
    // 火堆的人物立绘（行动前）
    private static final String MY_CHARACTER_SHOULDER_1 = ImageHelper.getOtherImgPath("character", "shoulder1");
    // 火堆的人物立绘（行动后）
    private static final String MY_CHARACTER_SHOULDER_2 = ImageHelper.getOtherImgPath("character", "shoulder2");
    // 人物死亡图像
    private static final String CORPSE_IMAGE = ImageHelper.getOtherImgPath("character", "corpse");
    // 战斗界面左下角能量图标的每个图层
    private static final String[] ORB_TEXTURES = new String[]{
            ImageHelper.getImgPathWithSubType("ui","orb","layer5"),
            ImageHelper.getImgPathWithSubType("ui","orb","layer4"),
            ImageHelper.getImgPathWithSubType("ui","orb","layer3"),
            ImageHelper.getImgPathWithSubType("ui","orb","layer2"),
            ImageHelper.getImgPathWithSubType("ui","orb","layer1"),
            ImageHelper.getImgPathWithSubType("ui","orb","layer6"),
            ImageHelper.getImgPathWithSubType("ui","orb","layer5d"),
            ImageHelper.getImgPathWithSubType("ui","orb","layer4d"),
            ImageHelper.getImgPathWithSubType("ui","orb","layer3d"),
            ImageHelper.getImgPathWithSubType("ui","orb","layer2d"),
            ImageHelper.getImgPathWithSubType("ui","orb","layer1d")
           
    };
    // 每个图层的旋转速度
    private static final float[] LAYER_SPEED = new float[]{-40.0F, -32.0F, 20.0F, -20.0F, 0.0F, -10.0F, -8.0F, 5.0F, -5.0F, 0.0F};
    // 人物的本地化文本，如卡牌的本地化文本一样，如何书写见下
    private static final CharacterStrings characterStrings = CardCrawlGame.languagePack.getCharacterString("TheCore:Suzuran");

    public Suzuran(String name) {
        super(name,Suzuran_CHARACTER,ORB_TEXTURES,ImageHelper.getImgPathWithSubType("ui","orb","vfx"), LAYER_SPEED, null, null);


        // 人物对话气泡的大小，如果游戏中尺寸不对在这里修改（libgdx的坐标轴左下为原点）
        this.dialogX = (this.drawX + 0.0F * Settings.scale);
        this.dialogY = (this.drawY + 150.0F * Settings.scale);


        // 初始化你的人物，如果你的人物只有一张图，那么第一个参数填写你人物图片的路径。
        this.initializeClass(
                null,//ImageHelper.getOtherImgPath("character", "character"), // 人物图片
                MY_CHARACTER_SHOULDER_2, MY_CHARACTER_SHOULDER_1,
                CORPSE_IMAGE, // 人物死亡图像
                this.getLoadout(),
                20.0F, -20.0F,
                200.0F, 250.0F, // 人物碰撞箱大小，越大的人物模型这个越大
                new EnergyManager(3) // 初始每回合的能量
        );
        // 如果你的人物没有动画，那么这些不需要写

        
        // 修改对话气泡的位置
        this.dialogX = (this.drawX + 0.0F * Settings.scale);
        this.dialogY = (this.drawY + 220.0F * Settings.scale);
        this.loadAnimation("suzuranmod/char_model/char_358_lisa.atlas", "suzuranmod/char_model/char_358_lisa.json", 0.9F);
        AnimationState.TrackEntry e = this.state.setAnimation(0, "Idle", true);
         e.setTime(e.getEndTime() * MathUtils.random());
         e.setTimeScale(1F);


    }

    // 初始卡组的ID，可直接写或引用变量
    public ArrayList<String> getStartingDeck() {
        ArrayList<String> retVal = new ArrayList<>();
        for(int x = 0; x<5; x++) {
            retVal.add(Strike.ID);
        }
        for(int x = 0; x<5; x++) {
            retVal.add(Defend.ID);
        }
        retVal.add(FoxfireStrike.ID);
        retVal.add(Light.ID);
        return retVal;
    }


    @Override
    public void useFastAttackAnimation() {
        this.state.setAnimation(0, "Attack", false);
        this.state.addAnimation(0, "Idle", true, 0.0F);
        this.state.getCurrent(0).setTimeScale(1.3F); // 加快动画速度
}   
    

    @Override
    public void useSlowAttackAnimation() {
        this.state.setAnimation(0, "Skill2", false);
        this.state.addAnimation(0, "Idle", true, 0.0F);
        this.state.getCurrent(0).setTimeScale(1.3F); // 加快动画速度
}

    @Override
    public void playDeathAnimation() {
        this.state.setAnimation(0, "Die", false);
}

    // 初始遗物的ID，可以先写个原版遗物凑数
    public ArrayList<String> getStartingRelics() {
        ArrayList<String> retVal = new ArrayList<>();
        retVal.add("SuzuranKhas:Grow");
        return retVal;
    }

    public CharSelectInfo getLoadout() {
        return new CharSelectInfo(
                characterStrings.NAMES[0], // 人物名字
                characterStrings.TEXT[0], // 人物介绍
                90, // 当前血量
                90, // 最大血量
                0, // 初始充能球栏位
                65, // 初始携带金币
                5, // 每回合抽牌数量
                this, // 别动
                this.getStartingRelics(), // 初始遗物
                this.getStartingDeck(), // 初始卡组
                false // 别动
        );
    }

    // 人物名字（出现在游戏左上角）
    @Override
    public String getTitle(PlayerClass playerClass) {
        return characterStrings.NAMES[0];
    }

    // 你的卡牌颜色（这个枚举在最下方创建）
    @Override
    public AbstractCard.CardColor getCardColor() {
        return Suzuran_COLOR;
    }

    // 翻牌事件出现的你的职业牌（一般设为打击）
    @Override
    public AbstractCard getStartCardForEvent() {
        return new Strike();
    }

    // 卡牌轨迹颜色
    @Override
    public Color getCardTrailColor() {
        return TheCore.MY_COLOR;
    }

    // 高进阶带来的生命值损失
    @Override
    public int getAscensionMaxHPLoss() {
        return 12;
    }

    // 卡牌的能量字体，没必要修改
    @Override
    public BitmapFont getEnergyNumFont() {
        return FontHelper.energyNumFontBlue;
    }

    // 人物选择界面点击你的人物按钮时触发的方法，这里为屏幕轻微震动
    @Override
    public void doCharSelectScreenSelectEffect() {
        CardCrawlGame.screenShake.shake(ScreenShake.ShakeIntensity.MED, ScreenShake.ShakeDur.SHORT, false);
    }

    // 碎心图片
    @Override
    public ArrayList<CutscenePanel> getCutscenePanels() {
        ArrayList<CutscenePanel> panels = new ArrayList<>();
        // 有两个参数的，第二个参数表示出现图片时播放的音效
        panels.add(new CutscenePanel( ImageHelper.getOtherImgPath("character", "Victory1"), "ATTACK_MAGIC_FAST_1"));
        panels.add(new CutscenePanel(ImageHelper.getOtherImgPath("character", "Victory2")));
        panels.add(new CutscenePanel(ImageHelper.getOtherImgPath("character", "Victory3")));
        return panels;
    }

    // 自定义模式选择你的人物时播放的音效
    @Override
    public String getCustomModeCharacterButtonSoundKey() {
        return "ATTACK_HEAVY";
    }

    // 游戏中左上角显示在你的名字之后的人物名称
    @Override
    public String getLocalizedCharacterName() {
        return characterStrings.NAMES[0];
    }

    // 创建人物实例，照抄
    @Override
    public AbstractPlayer newInstance() {
        return new Suzuran(this.name);
    }

    // 第三章面对心脏说的话（例如战士是“你握紧了你的长刀……”之类的）
    @Override
    public String getSpireHeartText() {
        return characterStrings.TEXT[1];
    }

    // 打心脏的颜色，不是很明显
    @Override
    public Color getSlashAttackColor() {
        return TheCore.MY_COLOR;
    }

    // 吸血鬼事件文本，主要是他（索引为0）和她（索引为1）的区别（机器人另外）
    @Override
    public String getVampireText() {
        return Vampires.DESCRIPTIONS[1];
    }

    // 卡牌选择界面选择该牌的颜色
    @Override
    public Color getCardRenderColor() {
        return TheCore.MY_COLOR;
    }

    // 第三章面对心脏造成伤害时的特效
    @Override
    public AbstractGameAction.AttackEffect[] getSpireHeartSlashEffect() {
        return new AbstractGameAction.AttackEffect[]{AbstractGameAction.AttackEffect.POISON, AbstractGameAction.AttackEffect.FIRE, AbstractGameAction.AttackEffect.SLASH_DIAGONAL, AbstractGameAction.AttackEffect.SLASH_HEAVY, AbstractGameAction.AttackEffect.FIRE, AbstractGameAction.AttackEffect.SLASH_DIAGONAL};
    }

    // 以下为原版人物枚举、卡牌颜色枚举扩展的枚举，需要写，接下来要用

    // 注意此处是在 MuelSyse 类内部的静态嵌套类中定义的新枚举值
    // 不可将该定义放在外部的 MuelSyse 类中，具体原因见《高级技巧 / 01 - Patch / SpireEnum》
    public static class PlayerColorEnum {
        @SpireEnum
        public static PlayerClass Suzuran_CHARACTER;

        // ***将CardColor和LibraryType的变量名改为你的角色的颜色名称，确保不会与其他mod冲突***
        // ***并且名称需要一致！***
        @SpireEnum
        public static AbstractCard.CardColor Suzuran_COLOR;
    }

    public static class PlayerLibraryEnum {
        // ***将CardColor和LibraryType的变量名改为你的角色的颜色名称，确保不会与其他mod冲突***
        // ***并且名称需要一致！***

        // 这个变量未被使用（呈现灰色）是正常的
        @SpireEnum
        public static CardLibrary.LibraryType Suzuran_COLOR;
    }
}
