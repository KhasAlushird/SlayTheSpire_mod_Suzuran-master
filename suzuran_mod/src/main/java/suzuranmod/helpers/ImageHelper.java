package suzuranmod.helpers;

import com.megacrit.cardcrawl.cards.AbstractCard.CardType;

public abstract class ImageHelper {

    private static final String BASE_PATH = "suzuranmod\\images\\";

    // 获取卡牌图片路径
    public static String getCardImgPath(CardType t, String name,boolean completed) {
        String type;
        switch (t) {
            case ATTACK:
                type = "attack";
                break;
            case POWER:
                type = "power";
                break;
            case STATUS:
                type = "status";
                break;
            case CURSE:
                type = "curse";
                break;
            case SKILL:
                type = "skill";
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + t);
        }
        if(!completed) return BASE_PATH + "cards\\" + "temp" + "\\" +"locked_"+ type + ".png";
        return BASE_PATH + "cards\\" + type + "\\" + name + ".png";
    }

    // 获取通用图片路径
    public static String getOtherImgPath(String resourceType, String name) {
        return BASE_PATH + resourceType  + "\\" + name + ".png";
    }

    public static String getImgPathWithSubType(String resourceType,String SubType, String name) {
        return BASE_PATH + resourceType + "\\"  + SubType + "\\"  + name + ".png";
    }

    public static String get_event_img(String Imgname) {
        return BASE_PATH + "events\\" + Imgname + ".png";
    }
}