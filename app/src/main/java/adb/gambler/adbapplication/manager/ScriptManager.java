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
//						Thread.sleep(5000);
//						connection.open("shell:input tap 705 268");
//						Thread.sleep(5000);
//						connection.open("shell:input tap 705 268");

						Thread.sleep(2000);
						connection.open("shell:input tap 30 41");
						Thread.sleep(2000);
						connection.open("shell:input tap 233 158");

						Thread.sleep(5000);
						connection.open("shell:input tap 408 105");
						Thread.sleep(2000);
						connection.open("shell:input tap 342 278");

						Thread.sleep(3000);
						connection.open("shell:input tap 408 105");
						Thread.sleep(2000);
						connection.open("shell:input tap 354 345");

						Thread.sleep(3000);
						connection.open("shell:input tap 408 105");
						Thread.sleep(2000);
						connection.open("shell:input tap 356 450");

						Thread.sleep(3000);
						connection.open("shell:input tap 1157 719");
						Thread.sleep(3000);
						connection.open("shell:input tap 109 224");

						Thread.sleep(3000);
						connection.open("shell:input tap 47 34");
						Thread.sleep(2000);
						connection.open("shell:input tap 374 160");

						Thread.sleep(5000);
						connection.open("shell:input tap 432 103");
						Thread.sleep(2000);
						connection.open("shell:input tap 353 289");

						Thread.sleep(3000);
						connection.open("shell:input tap 416 112");
						Thread.sleep(2000);
						connection.open("shell:input tap 360 352");

						Thread.sleep(3000);
						connection.open("shell:input tap 407 111");
						Thread.sleep(2000);
						connection.open("shell:input tap 365 460");

						Thread.sleep(3000);
						connection.open("shell:input tap 139 165");
						Thread.sleep(3000);
						connection.open("shell:input tap 149 290");

						Thread.sleep(3000);
						connection.open("shell:input tap 426 112");
						Thread.sleep(2000);
						connection.open("shell:input tap 355 290");

						Thread.sleep(3000);
						connection.open("shell:input tap 413 125");
						Thread.sleep(2000);
						connection.open("shell:input tap 348 332");

						Thread.sleep(3000);
						connection.open("shell:input tap 422 124");
						Thread.sleep(2000);
						connection.open("shell:input tap 351 397");

						Thread.sleep(3000);
						connection.open("shell:input tap 16 27");
						Thread.sleep(2000);
						connection.open("shell:input tap 347 312");
						Thread.sleep(3000);
						connection.open("shell:input tap 964 726");
						Thread.sleep(3000);
						connection.open("shell:input tap 1080 736");
						Thread.sleep(3000);
						connection.open("shell:input tap 111 164");

						Thread.sleep(3000);
						connection.open("shell:input tap 38 34");
						Thread.sleep(2000);
						connection.open("shell:input tap 102 142");

						Thread.sleep(3000);
						connection.open("shell:input tap 234 366");
						Thread.sleep(2000);
						connection.open("shell:input tap 433 548");
						Thread.sleep(2000);
						connection.open("shell:input tap 1155 723");
						Thread.sleep(3500);
						connection.open("shell:input tap 1174 746");
						Thread.sleep(3000);
						connection.open("shell:input tap 121 32");

						Thread.sleep(2000);
						connection.open("shell:input tap 1304 32");
						Thread.sleep(2000);
						connection.open("shell:input tap 807 435");
					}catch (Exception e){
						e.printStackTrace();
					}
				}
			}
		});
	}
}
