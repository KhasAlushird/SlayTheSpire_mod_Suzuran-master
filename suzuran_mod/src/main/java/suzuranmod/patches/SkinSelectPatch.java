package suzuranmod.patches;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.screens.charSelect.CharacterSelectScreen;

import basemod.ReflectionHacks;
import suzuranmod.character.SkinSelectScreen;
import suzuranmod.character.Suzuran;

public class SkinSelectPatch {
    
    public static boolean isSuzuranSelected() {
        return (CardCrawlGame.chosenCharacter == Suzuran.PlayerColorEnum.Suzuran_CHARACTER && 
                (Boolean) ReflectionHacks.getPrivate(CardCrawlGame.mainMenuScreen.charSelectScreen, 
                                                    CharacterSelectScreen.class, "anySelected"));
    }
    
    @SpirePatch(clz = CharacterSelectScreen.class, method = "update")
    public static class UpdatePatch {
        public static void Prefix(CharacterSelectScreen __instance) {
            if (isSuzuranSelected()) {
                SkinSelectScreen.Inst.update();
            }
        }
    }
    
    @SpirePatch(clz = CharacterSelectScreen.class, method = "render")
    public static class RenderPatch {
        public static void Postfix(CharacterSelectScreen __instance, SpriteBatch sb) {
            if (isSuzuranSelected()) {
                SkinSelectScreen.Inst.render(sb);
            }
        }
    }
}