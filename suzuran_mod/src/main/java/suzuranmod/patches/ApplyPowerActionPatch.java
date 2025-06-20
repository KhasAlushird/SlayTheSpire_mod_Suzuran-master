//abandoned
// package suzuranmod.patches;
// import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
// import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
// import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
// import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
// import com.megacrit.cardcrawl.powers.AbstractPower;

// import suzuranmod.powers.BurnoutPower;
// import suzuranmod.relics.AmuletInArm;

// @SpirePatch(clz = ApplyPowerAction.class, method = "update")
// public class ApplyPowerActionPatch {
//     @SpireInsertPatch(rloc = 98) // 你可以根据源码调整插入点
//     public static void Insert(ApplyPowerAction __instance) {
//         // 通过反射访问 private 字段
//         try {
//             java.lang.reflect.Field field = ApplyPowerAction.class.getDeclaredField("powerToApply");
//             field.setAccessible(true);
//             AbstractPower power = (AbstractPower) field.get(__instance);

//             if (power != null
//                     && power.ID.equals(BurnoutPower.POWER_ID)
//                     && __instance.target == AbstractDungeon.player
//                     && power.amount > 0
//                     && AbstractDungeon.player.hasRelic(AmuletInArm.ID)) {
//                 for (com.megacrit.cardcrawl.relics.AbstractRelic relic : AbstractDungeon.player.relics) {
//                     if (relic instanceof AmuletInArm) {
//                         ((AmuletInArm) relic).onBurnoutApplied();
//                     }
//                 }
//             }
//         } catch (Exception e) {
//             e.printStackTrace();
//         }
//     }
// }