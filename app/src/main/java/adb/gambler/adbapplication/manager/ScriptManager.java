package adb.gambler.adbapplication.manager;

import com.blankj.utilcode.util.ThreadUtils;
import com.blankj.utilcode.util.ToastUtils;

import java.util.concurrent.atomic.AtomicBoolean;

import adb.gambler.jadb.lib.AdbConnection;

/**
 * <p> File description: <p>
 * <p> Creator: Gambler   <p>
 * <p> Created date: 2023/7/10 <p>
 * * * * * * * * * * * * * * * * * * * * * *
 * Thinking is more important than coding. *
 * * * * * * * * * * * * * * * * * * * * * *
 */
public class ScriptManager {

	private static ScriptManager INSTANCE;

	private AtomicBoolean runFlag = new AtomicBoolean(true);

	public static ScriptManager getInstance(){
		if (null == INSTANCE){
			synchronized (ScriptManager.class){
				if (null == INSTANCE){
					INSTANCE = new ScriptManager();
				}
			}
		}

		return INSTANCE;
	}

	public void run(AdbConnection connection){
		runFlag.set(true);
		ThreadUtils.getCachedPool().execute(new Runnable() {
			@Override
			public void run() {
				try {
					while (runFlag.get()){
						// 打开应用
						Thread.sleep(10000);
						connection.open("shell:monkey -p com.sunmidebug.PRJ231013stfzjsfeyrgxdgxypzcghq6mh -c android.intent.category.LAUNCHER 1");

						// 进入自动测试页面
						Thread.sleep(5000);
						connection.open("shell:input tap 990 304");

						// 绑定设备
						Thread.sleep(4000);
						connection.open("shell:input tap 991 52");
						Thread.sleep(100);
						connection.open("shell:input tap 1020 117");
						Thread.sleep(100);
						connection.open("shell:input tap 1057 185");
						Thread.sleep(100);
						connection.open("shell:input tap 1010 259");
						Thread.sleep(100);
						connection.open("shell:input tap 1024 614");
						Thread.sleep(100);
						connection.open("shell:input tap 867 750");
						Thread.sleep(100);
						connection.open("shell:input tap 915 886");

						// 点击打印
						Thread.sleep(2000);
						connection.open("shell:input tap 1020 337");
						Thread.sleep(200);
						connection.open("shell:input tap 1026 397");
						Thread.sleep(200);
						connection.open("shell:input tap 1035 456");
						Thread.sleep(200);
						connection.open("shell:input tap 1024 541");
						Thread.sleep(200);
						connection.open("shell:input tap 1024 692");
						Thread.sleep(200);
						connection.open("shell:input tap 994 821");
						Thread.sleep(200);
						connection.open("shell:input tap 962 942");

						// 滑动屏幕，绑定并打印
						Thread.sleep(1000);
						connection.open("shell:input swipe 945 878 915 391");
						Thread.sleep(1500);
						connection.open("shell:input tap 926 926");
						Thread.sleep(2000);
						connection.open("shell:input tap 1011 984");

						// 休眠50分钟，断开Wi-Fi和蓝牙
						Thread.sleep(1000 * 50 * 60);
						connection.open("shell:input keyevent 3");
						Thread.sleep(10000);
						connection.open("shell:input tap 542 154");
						Thread.sleep(5000);
						connection.open("shell:input tap 1756 138");
						Thread.sleep(2000);
						connection.open("shell:input tap 199 303");
						Thread.sleep(1000);
						connection.open("shell:input tap 799 348");
						Thread.sleep(1000);
						connection.open("shell:input tap 911 152");
						Thread.sleep(1000);
						connection.open("shell:input tap 1804 136");
						// 断开10分钟
						Thread.sleep(10 * 1000 * 60);
						connection.open("shell:input tap 1804 136");
						Thread.sleep(3000);
						connection.open("shell:input tap 294 186");
						Thread.sleep(2000);
						connection.open("shell:input tap 1756 138");
						Thread.sleep(10000);
						connection.open("shell:input keyevent 3");
						Thread.sleep(1000);

						// 关闭setting页，打开应用
						connection.open("shell:am force-stop com.sunmidebug.PRJ231013stfzjsfeyrgxdgxypzcghq6mh");
						Thread.sleep(3000);
						connection.open("shell:monkey -p com.sunmidebug.PRJ231013stfzjsfeyrgxdgxypzcghq6mh -c android.intent.category.LAUNCHER 1");
						Thread.sleep(10000);

						// 进入自动测试页面
						Thread.sleep(10000);
						connection.open("shell:input tap 990 304");
						// 重新进入之后打印测试
						Thread.sleep(2000);
						connection.open("shell:input tap 1020 337");
						Thread.sleep(200);
						connection.open("shell:input tap 1026 397");
						Thread.sleep(200);
						connection.open("shell:input tap 1035 456");
						Thread.sleep(200);
						connection.open("shell:input tap 1024 541");
						Thread.sleep(200);
						connection.open("shell:input tap 1024 692");
						Thread.sleep(200);
						connection.open("shell:input tap 994 821");
						Thread.sleep(200);
						connection.open("shell:input tap 962 942");

						// 休眠1小时，重新走绑定逻辑
						Thread.sleep(1000 * 60 * 60);
						connection.open("shell:input tap 991 52");
						Thread.sleep(100);
						connection.open("shell:input tap 1020 117");
						Thread.sleep(100);
						connection.open("shell:input tap 1057 185");
						Thread.sleep(100);
						connection.open("shell:input tap 1010 259");
						Thread.sleep(100);
						connection.open("shell:input tap 1024 614");
						Thread.sleep(100);
						connection.open("shell:input tap 867 750");
						Thread.sleep(100);
						connection.open("shell:input tap 915 886");

						// 滑动屏幕，绑定并打印
						Thread.sleep(1000);
						connection.open("shell:input swipe 945 878 915 391");
						Thread.sleep(1500);
						connection.open("shell:input tap 926 926");
						Thread.sleep(2000);
						connection.open("shell:input tap 1011 984");
					}
				}catch (Exception e){
					ToastUtils.showLong(e.getMessage());
					e.printStackTrace();
				}
			}
		});
	}

	public void stop(){
		runFlag.set(false);
	}

	public void recordStart(){

	}
}
