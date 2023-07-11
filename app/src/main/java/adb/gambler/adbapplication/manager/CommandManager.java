package adb.gambler.adbapplication.manager;

/**
 * <p> File description: <p>
 * <p> Creator: Gambler   <p>
 * <p> Created date: 2023/7/11 <p>
 * * * * * * * * * * * * * * * * * * * * * *
 * Thinking is more important than coding. *
 * * * * * * * * * * * * * * * * * * * * * *
 */
public class CommandManager {

    public static String click(float x, float y){
        return "shell:input tap " + x + " " + y;
    }

    public static String move(float startX, float startY, float endX, float endY){
        return "shell:input swipe " + startX + " " + startY + " " + endX + " " + endY;
    }

    public static String event(int code){
        return "shell:input keyevent " + code;
    }

    public static final String COMMAND_BACK = "shell:input keyevent 4";
    public static final String COMMAND_HOME = "shell:input keyevent 3";

    public static final String COMMAND_NUMBER_0 = "shell:input keyevent 7";
    public static final String COMMAND_NUMBER_1 = "shell:input keyevent 8";
    public static final String COMMAND_NUMBER_2 = "shell:input keyevent 9";
    public static final String COMMAND_NUMBER_3 = "shell:input keyevent 10";
    public static final String COMMAND_NUMBER_4 = "shell:input keyevent 11";
    public static final String COMMAND_NUMBER_5 = "shell:input keyevent 12";
    public static final String COMMAND_NUMBER_6 = "shell:input keyevent 13";
    public static final String COMMAND_NUMBER_7 = "shell:input keyevent 14";
    public static final String COMMAND_NUMBER_8 = "shell:input keyevent 15";
    public static final String COMMAND_NUMBER_9 = "shell:input keyevent 16";
}
