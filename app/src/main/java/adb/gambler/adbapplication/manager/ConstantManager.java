package adb.gambler.adbapplication.manager;

/**
 * <p> File description: <p>
 * <p> Creator: Gambler   <p>
 * <p> Created date: 2023/7/13 <p>
 * * * * * * * * * * * * * * * * * * * * * *
 * Thinking is more important than coding. *
 * * * * * * * * * * * * * * * * * * * * * *
 */
public class ConstantManager {

    private static String localWifi;
    private static String networkIP;

    public static String getWifi() {
        return localWifi;
    }

    public static void setWifi(String wifi) {
        localWifi = wifi;
    }

    public static String getIp() {
        return networkIP;
    }

    public static void setIp(String ip) {
        networkIP = ip;
    }
}
