package adb.gambler.adbapplication.manager;

import com.blankj.utilcode.util.ThreadUtils;

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

	private volatile int num = 8;

	public void run(AdbConnection connection){
		ThreadUtils.getSinglePool().execute(new Runnable() {
			@Override
			public void run() {
				while (true){
					if (num > 14){
						num = 8;
					}else {
						num++;
					}

					try {
						Thread.sleep(3000);
						connection.open("shell:input tap 116 272");

						Thread.sleep(4000);
						connection.open("shell:input tap 791 720");

						Thread.sleep(4000);
						connection.open("shell:input tap 478 451");

						Thread.sleep(2000);
						connection.open("shell:input tap 1248 637");
						connection.open("shell:input tap 1248 637");

						Thread.sleep(1000);
						connection.open("shell:input keyevent " + num);

						Thread.sleep(1500);
						connection.open("shell:input keyevent 4");

						Thread.sleep(300);
						connection.open("shell:input tap 893 734");
					}catch (Exception e){
						e.printStackTrace();
					}
				}
			}
		});
	}
}
