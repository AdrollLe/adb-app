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

	public void run(AdbConnection connection){
		ThreadUtils.getSinglePool().execute(new Runnable() {
			@Override
			public void run() {
				while (true){
					try {
						Thread.sleep(5000);
						connection.open("shell:input tap 354 103");
						Thread.sleep(1000);
						connection.open("shell:input tap 360 110");

						Thread.sleep(5000);
						connection.open("shell:input tap 567 305");
						Thread.sleep(3000);
						connection.open("shell:input tap 520 305");
						Thread.sleep(1000);
						connection.open("shell:input tap 535 305");

						Thread.sleep(3000);
						connection.open("shell:input text \"15513742955\"");

						Thread.sleep(3000);
						connection.open("shell:input tap 583 398");

						Thread.sleep(1000);
						connection.open("shell:input text \"dupeng123\"");

						Thread.sleep(1000);
						connection.open("shell:input keyevent 4");

						Thread.sleep(1000);
						connection.open("shell:input tap 388 464");

						Thread.sleep(1000);
						connection.open("shell:input tap 655 546");

						Thread.sleep(3000);
						connection.open("shell:input tap 655 546");

						Thread.sleep(30000);
						connection.open("shell:input tap 1346 600");
					}catch (Exception e){
						e.printStackTrace();
					}
				}
			}
		});
	}
}
