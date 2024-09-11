package adb.gambler.adbapplication.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.blankj.utilcode.util.ThreadUtils;

import adb.gambler.adbapplication.R;
import adb.gambler.adbapplication.manager.ScriptManager;
import adb.gambler.jadb.lib.AdbConnection;

/**
 * <p> File description: <p>
 * <p> Creator: Gambler   <p>
 * <p> Created date: 2023/7/7 <p>
 * * * * * * * * * * * * * * * * * * * * * *
 * Thinking is more important than coding. *
 * * * * * * * * * * * * * * * * * * * * * *
 */
public class ControlView extends View{

	private Context context;
	private AdbConnection connection;

	private ViewGroup rootView;
	private Paint paint, lastPaint;
	private Path path, lastPath;

	private float startX, startY, endX, endY;

	public ControlView(Context context, AdbConnection connection) {
		super(context);
		this.context = context;
		this.connection = connection;

		rootView = (ViewGroup) LayoutInflater.from(context).inflate(R.layout.view_control, null);
		rootView.addView(this);
		init();
		initScript();
	}

	public View getView(){
		return rootView;
	}

	private void init(){
		paint = new Paint();
		paint.setColor(Color.RED);
		paint.setStrokeWidth(20);
		paint.setStyle(Paint.Style.STROKE);

		path = new Path();
		lastPath = new Path();

		lastPaint = new Paint();
		lastPaint.setColor(Color.GRAY);
		lastPaint.setStrokeWidth(20);
		lastPaint.setStyle(Paint.Style.STROKE);

		// 设置TouchListener，监听用户的操作
		setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:
						// 用户按下手指时，开始新的路径
						if (!path.isEmpty()){
							lastPath.reset();
							lastPath.addPath(path);
							path.reset();
						}
						path.moveTo(event.getX(), event.getY());

						startX = event.getX();
						startY = event.getY();
						break;
					case MotionEvent.ACTION_MOVE:
						// 用户移动手指时，将路径加入到现有路径中
						if (!path.isEmpty()){
							lastPath.addPath(path);
						}
						path.lineTo(event.getX(), event.getY());
						break;
					case MotionEvent.ACTION_UP:
						endX = event.getX();
						endY = event.getY();

						ThreadUtils.getCachedPool().execute(new Runnable() {
							@Override
							public void run() {
								try {
									if (Math.abs(startX - endX) < 10 && Math.abs(startY - endY) < 10){
										connection.open("shell:input tap " + endX + " " + endY);
									}else {
										connection.open("shell:input swipe " + startX + " " + startY + " " + endX + " " + endY);
									}
								}catch (Exception e){
									e.printStackTrace();
								}
							}
						});
						break;
					default:
						return false;
				}
				// 重绘View，将路径绘制出来
				invalidate();
				return true;
			}
		});
	}

	private void initScript(){
		ScriptManager.getInstance().run(connection);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		// 将路径绘制出来
		canvas.drawPath(lastPath, lastPaint);
		canvas.drawPath(path, paint);
	}
}
