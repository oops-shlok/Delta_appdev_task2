package com.example.deltatask2;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Handler;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import java.util.Random;

public class GameView extends View {
    Context context;
    SharedPreferences sharedPreferences;
    Boolean audioState;
    Velocity velocity = new Velocity(25,32);
    Handler handler;
    final long UPDATE_MILLI = 30;
    Runnable runnable;
    Dialog dialog;
    int ballx_cor,bally_cor;
    Paint textPaint = new Paint();
    float TEXT_SIZE = 60;
    float paddleX,paddleY;
    float old_x,oldpaddle_x;
    int point=0;
    Bitmap ball,paddle;
    int dWidth,dHeight;
    MediaPlayer ball_hit,ball_miss;
    Random random = new Random();
    Rect rect = new Rect();

    public GameView(Context context) {
        super(context);
        this.context=context;
        ball= BitmapFactory.decodeResource(getResources(),R.drawable.ball);
        paddle= BitmapFactory.decodeResource(getResources(),R.drawable.paddle);
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                invalidate();
            }
        };
        ball_hit = MediaPlayer.create(context,R.raw.hit);
        ball_miss = MediaPlayer.create(context,R.raw.miss);
        textPaint.setColor(Color.LTGRAY);
        textPaint.setTextAlign(Paint.Align.LEFT);
        textPaint.setTextSize(TEXT_SIZE);
        Display display = ((Activity) getContext()).getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        dWidth = size.x;
        dHeight = size.y;
        ballx_cor=random.nextInt(dWidth);
        paddleY=(dHeight*4/5);
        paddleX=dWidth/2-paddle.getWidth()/2;
        sharedPreferences = context.getSharedPreferences("my_pref",0);
        audioState = sharedPreferences.getBoolean("audioState",true);
        dialog = new Dialog(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.BLACK);
        ballx_cor +=velocity.getX();
        bally_cor+=velocity.getY();
        if((ballx_cor>=dWidth-ball.getWidth())|| ballx_cor<=0){
            if(ball_hit != null && audioState){
                ball_hit.start();
            }
            velocity.setX(velocity.getX() * -1);
        }
        if(bally_cor<=0){
            if(ball_hit != null && audioState){
                ball_hit.start();
            }
            velocity.setY(velocity.getY() * -1);
            point++;
        }
        if(bally_cor>(paddleY+paddle.getHeight())){
            ballx_cor= 500;
            bally_cor=500;
            if(ball_miss != null && audioState){
                ball_miss.start();
            }
            velocity.setX(0);
            velocity.setY(0);
            openDialog();
        }
        if((ballx_cor+ball.getWidth() >=paddleX)&&(ballx_cor<=(paddleX+paddle.getWidth()))&&((bally_cor+ball.getHeight())>=paddleY)&&(bally_cor+ball.getHeight()<=(paddleY+paddle.getHeight()))){
            if(ball_hit != null && audioState){
                ball_hit.start();
            }
            velocity.setX(velocity.getX()+1);
            velocity.setY((velocity.getY()+1)*-1);
        }

        canvas.drawBitmap(ball,ballx_cor,bally_cor,null);
        canvas.drawBitmap(paddle,paddleX,paddleY,null);
        canvas.drawText("POINTS: "+point,380,TEXT_SIZE,textPaint);
        handler.postDelayed(runnable,UPDATE_MILLI);
    }

    private void openDialog() {
    dialog.setContentView(R.layout.gameover_dialog);
    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        Button button=dialog.findViewById(R.id.button);
        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,MainActivity.class);
                context.startActivity(intent);
            }
        });
        dialog.show();
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float touchX=event.getX();
        float touchY=event.getY();
        if(touchY>=paddleY){
            int action=event.getAction();
            if(action==MotionEvent.ACTION_DOWN){
                old_x=event.getX();
                oldpaddle_x = paddleX;
            }
            if(action==MotionEvent.ACTION_MOVE){
                float shift = old_x-touchX;
                float newpaddle_x = oldpaddle_x - shift;
                if(newpaddle_x <=0)
                    paddleX=0;
                else if(newpaddle_x>=dWidth-paddle.getWidth())
                    paddleX=dWidth-paddle.getWidth();
                else
                    paddleX=newpaddle_x;
            }
        }
        return true;
    }

    private int xVelocity() {
        int[] values = {-35,-30,-25,25,30,35};
        int index= random.nextInt(6);
        return values[index];
    }
}
