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

import adb.gambler.adbapplication.R;
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

	public ControlView(Context context, AdbConnection connection) {
		super(context);
		this.context = context;
		this.connection = connection;

		rootView = (ViewGroup) LayoutInflater.from(context).inflate(R.layout.view_control, null);
		rootView.addView(this);
		init();
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
						break;
					case MotionEvent.ACTION_MOVE:
						// 用户移动手指时，将路径加入到现有路径中
						if (!path.isEmpty()){
							lastPath.addPath(path);
						}
						path.lineTo(event.getX(), event.getY());
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

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		// 将路径绘制出来
		canvas.drawPath(lastPath, lastPaint);
		canvas.drawPath(path, paint);
	}
}
