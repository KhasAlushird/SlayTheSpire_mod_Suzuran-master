package suzuranmod.helpers;

public abstract class AudioHelper {

    private static final String BASE_PATH = "suzuranmod\\audios\\";

    // 获取卡牌图片路径
    public static String getAudioPath(String name) {
        return BASE_PATH + name + ".ogg" ;
    }
}