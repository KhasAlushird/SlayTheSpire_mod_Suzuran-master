package suzuranmod.modcore;

import java.nio.charset.StandardCharsets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.google.gson.Gson;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.core.Settings.GameLanguage;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.localization.CharacterStrings;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.localization.Keyword;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.localization.RelicStrings;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.rewards.RewardSave;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

import basemod.BaseMod;
import static basemod.BaseMod.logger;
import basemod.abstracts.CustomReward;
import basemod.interfaces.AddAudioSubscriber;
import basemod.interfaces.EditCardsSubscriber;
import basemod.interfaces.EditCharactersSubscriber;
import basemod.interfaces.EditKeywordsSubscriber;
import basemod.interfaces.EditRelicsSubscriber;
import basemod.interfaces.EditStringsSubscriber;
import basemod.interfaces.OnStartBattleSubscriber;
import basemod.interfaces.PostBattleSubscriber;
import basemod.interfaces.PostDungeonInitializeSubscriber;
import basemod.interfaces.PostInitializeSubscriber;
import suzuranmod.character.Suzuran;
import static suzuranmod.character.Suzuran.PlayerColorEnum.Suzuran_CHARACTER;
import static suzuranmod.character.Suzuran.PlayerColorEnum.Suzuran_COLOR;
import suzuranmod.helpers.AudioHelper;
import suzuranmod.helpers.ImageHelper;
import suzuranmod.patches.OfudaRewardTypePatch;
import suzuranmod.relics.AmuletInArm;
import suzuranmod.relics.Bloom;
import suzuranmod.relics.Grow;
import suzuranmod.relics.NineTails;
import suzuranmod.relics.RottingStick;
import suzuranmod.relics.TheFire;
import suzuranmod.relics.WakanCrystal;
import suzuranmod.rewards.OfudaRewardItem;



@SpireInitializer
public class TheCore implements EditCardsSubscriber,EditStringsSubscriber,EditCharactersSubscriber,EditRelicsSubscriber,EditKeywordsSubscriber,PostBattleSubscriber,OnStartBattleSubscriber,PostDungeonInitializeSubscriber,PostInitializeSubscriber,AddAudioSubscriber{  
    // 实现接口
    // 人物选择界面按钮的图片
    private static final String MY_CHARACTER_BUTTON =   ImageHelper.getOtherImgPath("character","Character_Button" );
    private static final String MY_CHARACTER_PORTRAIT = ImageHelper.getOtherImgPath("character","Character_Portrait" );
    // 攻击牌的背景（小尺寸）
    private static final String BG_ATTACK_512 = ImageHelper.getImgPathWithSubType("character", "cardback","bg_attack_512");
    // 能力牌的背景（小尺寸）
    private static final String BG_POWER_512 = ImageHelper.getImgPathWithSubType("character", "cardback","bg_power_512");
    // 技能牌的背景（小尺寸）
    private static final String BG_SKILL_512 = ImageHelper.getImgPathWithSubType("character", "cardback","bg_skill_512");
    // 在卡牌和遗物描述中的能量图标
    private static final String SMALL_ORB = ImageHelper.getOtherImgPath("character","small_orb");
    // 攻击牌的背景（大尺寸）
    private static final String BG_ATTACK_1024 = ImageHelper.getImgPathWithSubType("character", "cardback","bg_attack_1024");
    // 能力牌的背景（大尺寸）
    private static final String BG_POWER_1024 = ImageHelper.getImgPathWithSubType("character", "cardback","bg_power_1024");
    // 技能牌的背景（大尺寸）
    private static final String BG_SKILL_1024 = ImageHelper.getImgPathWithSubType("character", "cardback","bg_skill_1024");
    // 在卡牌预览界面的能量图标
    private static final String BIG_ORB = ImageHelper.getOtherImgPath("character","big_orb");
    // 小尺寸的能量图标（战斗中，牌堆预览）
    private static final String ENEYGY_ORB = ImageHelper.getOtherImgPath("character","energy_orb");
    public static final Color MY_COLOR = new Color(242.0F / 255.0F, 242.0F / 255.0F, 191.0F / 255.0F, 1.0F);

    public TheCore() {
        BaseMod.subscribe(this); // 告诉basemod你要订阅事件
        // 这里注册颜色
        BaseMod.addColor(Suzuran_COLOR, MY_COLOR,
         MY_COLOR, MY_COLOR,
          MY_COLOR, MY_COLOR,
           MY_COLOR, MY_COLOR,
           BG_ATTACK_512,BG_SKILL_512,
           BG_POWER_512,ENEYGY_ORB,
           BG_ATTACK_1024,BG_SKILL_1024,
           BG_POWER_1024,BIG_ORB,
           SMALL_ORB);
    }



    public static void initialize() {
        new TheCore();
    }


    @Override
    public void receivePostBattle(AbstractRoom room) {
    }

    @Override
    public void receiveOnBattleStart(AbstractRoom room) {
    }

    @Override
    public void receivePostDungeonInitialize() {
        suzuranmod.modcore.OfudaManager.resetOfuda();
    }

    

     

    // 当basemod开始注册mod卡牌时，便会调用这个函数
    @Override
    public void receiveEditCards() {

        //(shortcut : shift alt down)
        //attack
        basemod.BaseMod.addCard(new suzuranmod.cards.attack.Strike());
        basemod.BaseMod.addCard(new suzuranmod.cards.attack.FoxfireStrike());
        basemod.BaseMod.addCard(new suzuranmod.cards.attack.Pullout());
        basemod.BaseMod.addCard(new suzuranmod.cards.attack.Leapup());
        basemod.BaseMod.addCard(new suzuranmod.cards.attack.LightBurn());
        basemod.BaseMod.addCard(new suzuranmod.cards.attack.Glimmer());
        basemod.BaseMod.addCard(new suzuranmod.cards.attack.BraceUp());
        basemod.BaseMod.addCard(new suzuranmod.cards.attack.FFCombo());
        basemod.BaseMod.addCard(new suzuranmod.cards.attack.OfudaKill());
        basemod.BaseMod.addCard(new suzuranmod.cards.attack.OfudaDance());
        basemod.BaseMod.addCard(new suzuranmod.cards.attack.FoxFeast());
        basemod.BaseMod.addCard(new suzuranmod.cards.attack.OfudaPierce());
        basemod.BaseMod.addCard(new suzuranmod.cards.attack.WakanStrike());
        basemod.BaseMod.addCard(new suzuranmod.cards.attack.Denotation());
       

        //skill
        basemod.BaseMod.addCard(new suzuranmod.cards.skill.Defend());
        basemod.BaseMod.addCard(new suzuranmod.cards.skill.Light());
        basemod.BaseMod.addCard(new suzuranmod.cards.skill.FoxSurge());
        basemod.BaseMod.addCard(new suzuranmod.cards.skill.Quenching());
        basemod.BaseMod.addCard(new suzuranmod.cards.skill.EmerCare());
        basemod.BaseMod.addCard(new suzuranmod.cards.skill.FFHaze());
        basemod.BaseMod.addCard(new suzuranmod.cards.skill.FoxProtect());
        basemod.BaseMod.addCard(new suzuranmod.cards.skill.Twinkling());
        basemod.BaseMod.addCard(new suzuranmod.cards.skill.Will_o_t_s());
        basemod.BaseMod.addCard(new suzuranmod.cards.skill.CircleHealing());
        basemod.BaseMod.addCard(new suzuranmod.cards.skill.ThornWrapped());
        basemod.BaseMod.addCard(new suzuranmod.cards.skill.SeekingOfuda());
        basemod.BaseMod.addCard(new suzuranmod.cards.skill.RemoveDisaster());
        basemod.BaseMod.addCard(new suzuranmod.cards.skill.FFHomology());
        basemod.BaseMod.addCard(new suzuranmod.cards.skill.LonelyVigil());
       
        //basemod.BaseMod.addCard(new suzuranmod.cards.skill.MindControl());    abondoned

        //status
       
        //powers
       basemod.BaseMod.addCard(new suzuranmod.cards.power.HealThorn());
       basemod.BaseMod.addCard(new suzuranmod.cards.power.MiracleGrowth());
       basemod.BaseMod.addCard(new suzuranmod.cards.power.Pruning());
       basemod.BaseMod.addCard(new suzuranmod.cards.power.FoxDeal());
       basemod.BaseMod.addCard(new suzuranmod.cards.power.FoxFete());
       basemod.BaseMod.addCard(new suzuranmod.cards.power.FullMoon());

        //Curses
       

        
    }

    @Override
    public void receiveEditStrings() {
        String lang;
        if (Settings.language == GameLanguage.ZHS) {
            lang = "zhs"; // 如果语言设置为简体中文，则加载ZHS文件夹的资源
        } else {
            lang = "eng"; // 如果没有相应语言的版本，默认加载英语
        }

        logger.info("Loading localization files for language: " + lang);

        try {
            String cardsPath = "suzuranmod/localization/" + lang + "/cards.json";
            if (Gdx.files.internal(cardsPath).exists()) {
                BaseMod.loadCustomStringsFile(CardStrings.class, cardsPath);
                logger.info("Loaded cards.json");
            } else {
                logger.error("File not found: " + cardsPath);
            }

            String charactersPath = "suzuranmod/localization/" + lang + "/characters.json";
            if (Gdx.files.internal(charactersPath).exists()) {
                BaseMod.loadCustomStringsFile(CharacterStrings.class, charactersPath);
                logger.info("Loaded characters.json");
            } else {
                logger.error("File not found: " + charactersPath);
            }

            String powersPath = "suzuranmod/localization/" + lang + "/powers.json";
            if (Gdx.files.internal(powersPath).exists()) {
                BaseMod.loadCustomStringsFile(PowerStrings.class, powersPath);
                logger.info("Loaded powers.json");
            } else {
                logger.error("File not found: " + powersPath);
            }

            String relicsPath = "suzuranmod/localization/" + lang + "/relics.json";
            if (Gdx.files.internal(relicsPath).exists()) {
                BaseMod.loadCustomStringsFile(RelicStrings.class, relicsPath);
                logger.info("Loaded relics.json");
            } else {
                logger.error("File not found: " + relicsPath);
            }

            String potionsPath = "suzuranmod/localization/" + lang + "/potions.json";
            if (Gdx.files.internal(potionsPath).exists()) {
                BaseMod.loadCustomStringsFile(PotionStrings.class, potionsPath);
                logger.info("Loaded potions.json");
            } else {
                logger.error("File not found: " + potionsPath);
            }

            String eventsPath = "suzuranmod/localization/" + lang + "/events.json";
            if (Gdx.files.internal(eventsPath).exists()) {
                BaseMod.loadCustomStringsFile(EventStrings.class,eventsPath);
                logger.info("Loaded events.json");
            } else {
                logger.error("File not found: " + eventsPath);
            }
            
            String uiPath = "suzuranmod/localization/" + lang + "/ui.json";
            if (Gdx.files.internal(uiPath).exists()) {
                BaseMod.loadCustomStringsFile(UIStrings.class,uiPath);
                logger.info("Loaded ui.json");
            } else {
                logger.error("File not found: " + uiPath);
            }

        } catch (Exception e) {
            logger.error("Failed to load localization files for language: " + lang, e);
        }
    }
    
    @Override
    public void receiveEditCharacters() {
        // 向basemod注册人物
        BaseMod.addCharacter(new Suzuran(CardCrawlGame.playerName), MY_CHARACTER_BUTTON, MY_CHARACTER_PORTRAIT, Suzuran_CHARACTER);
    }

    @Override
    public void receivePostInitialize() {
        BaseMod.registerCustomReward(
            OfudaRewardTypePatch.SUZURAN_OFUDA,
            (RewardSave save) -> new OfudaRewardItem(save.amount),
            (CustomReward reward) -> new RewardSave(
                reward.type.toString(),
                null,
                ((OfudaRewardItem)reward).ofudaCount,
                0
            )
            
        );
        BaseMod.addSaveField("SuzuranOfuda", new OfudaManager());
    }

    @Override
    public void receiveAddAudio() {
        String path1 = AudioHelper.getAudioPath("select1");
        String path2 = AudioHelper.getAudioPath("select2");
        BaseMod.addAudio("SUZURAN_SELECT1",path1);
        BaseMod.addAudio("SUZURAN_SELECT2", path2);
  }
   
    @Override
    public void receiveEditRelics() {

        //register relics here
        // RelicType表示是所有角色都能拿到的遗物，还是一个角色的独有遗物
        //BaseMod.addRelicToCustomPool(new EcoSpecimen(),MuelSyse.PlayerColorEnum.MUEL_COLOR);
        BaseMod.addRelicToCustomPool(new Grow(),Suzuran.PlayerColorEnum.Suzuran_COLOR);
        BaseMod.addRelicToCustomPool(new Bloom(),Suzuran.PlayerColorEnum.Suzuran_COLOR);
        BaseMod.addRelicToCustomPool(new RottingStick(),Suzuran.PlayerColorEnum.Suzuran_COLOR);
        BaseMod.addRelicToCustomPool(new AmuletInArm(),Suzuran.PlayerColorEnum.Suzuran_COLOR);
        BaseMod.addRelicToCustomPool(new TheFire(),Suzuran.PlayerColorEnum.Suzuran_COLOR);
        BaseMod.addRelicToCustomPool(new NineTails(),Suzuran.PlayerColorEnum.Suzuran_COLOR);
        BaseMod.addRelicToCustomPool(new WakanCrystal(),Suzuran.PlayerColorEnum.Suzuran_COLOR);


        //register potions here
        //BaseMod.addPotion(CushionPotion.class, Color.GREEN, Color.YELLOW, Color.CLEAR, "MuelSyseKhas:CushionPotion", MY_CHARACTER);
       


}



@Override
public void receiveEditKeywords() {
    Gson gson = new Gson();
    String lang = "eng"; 
    if (Settings.language == Settings.GameLanguage.ZHS) {
        lang = "zhs";
    }
    logger.info("Loading keywords for language: " + lang);

    String json = Gdx.files.internal("suzuranmod/localization/" + lang + "/keywords.json")
            .readString(String.valueOf(StandardCharsets.UTF_8));
    Keyword[] keywords = gson.fromJson(json, Keyword[].class);
    if (keywords != null) {
        for (Keyword keyword : keywords) {
            // 这个id要全小写
            BaseMod.addKeyword("suzurankhas", keyword.NAMES[0], keyword.NAMES, keyword.DESCRIPTION);
           logger.info("Loaded keyword: " + keyword.NAMES[0]);
        }
    } else {
         logger.warn("No keywords found for language: " + lang);
     }
}
}
