package adb.gambler.adbapplication.manager;

import com.blankj.utilcode.util.ThreadUtils;

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
		ThreadUtils.getSinglePool().execute(new Runnable() {
			@Override
			public void run() {
				while (runFlag.get()){
					try {
						Thread.sleep(10000);
						connection.open("shell:input tap 688 38");
						Thread.sleep(4000);
						connection.open("shell:input tap 326 274");
						Thread.sleep(4000);
						connection.open("shell:input tap 457 300");
//						Thread.sleep(4000);
//						connection.open("shell:input tap 998 128");
						Thread.sleep(4000);
						connection.open("shell:input tap 1057 763");
						Thread.sleep(2000);
						connection.open("shell:input tap 794 528");

						Thread.sleep(4000);
						connection.open("shell:input tap 1091 789");
						Thread.sleep(4000);
						connection.open("shell:input tap 1032 464");

						Thread.sleep(4000);
						connection.open("shell:input tap 1033 518");
					}catch (Exception e){
						e.printStackTrace();
					}
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
