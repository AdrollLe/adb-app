package adb.gambler.adbapplication.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p> File description: <p>
 * <p> Creator: Gambler   <p>
 * <p> Created date: 2023/7/19 <p>
 * * * * * * * * * * * * * * * * * * * * * *
 * Thinking is more important than coding. *
 * * * * * * * * * * * * * * * * * * * * * *
 */
public class NetworkUtils {

    public static String getHostIp() {
        URL url;
        HttpURLConnection connection;
        try {
            // TODO: 2024/2/19 amazon在某些设备无法发送请求
            url = new URL("https://checkip.amazonaws.com/");
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            // 读取网站返回的 IP 地址字符串
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String ipAddress = reader.readLine();
            // 关闭连接和输入流
            reader.close();
            connection.disconnect();

            // 提取 IP 地址字符串
            String regex = "\\b(?:\\d{1,3}\\.){3}\\d{1,3}\\b";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(ipAddress);
            if (matcher.find()) {
                return matcher.group();
            } else {
                return "";
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
