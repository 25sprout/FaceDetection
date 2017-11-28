package com.google.android.gms.samples.vision.face.facetracker.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Movie;
import android.graphics.Paint;
import android.graphics.Shader;
import android.support.v4.content.ContextCompat;

import com.google.android.gms.samples.vision.face.facetracker.R;
import com.google.android.gms.samples.vision.face.facetracker.ui.camera.GraphicOverlay;
import com.google.android.gms.vision.face.Face;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by bill on 2017/11/28.
 */

public class GifView extends GraphicOverlay.Graphic {
    Face face;
    Movie movie;
    long moviestart;
    private Paint bg;
    private boolean isFullscreen;
    private String[] fileNames = new String[]{"raining.gif", "raining2.gif", "raining3.gif"};

    public GifView(Context context, GraphicOverlay overlay) {
        super(overlay);
        int random = (int) (Math.random() * 2);
        loadGifAsset(context, fileNames[random]);
        bg = new Paint();
        bg.setShader(new LinearGradient(0, 0, 0, 1f, ContextCompat.getColor(context, R.color.dark),
                ContextCompat.getColor(context, R.color.grey), Shader.TileMode.MIRROR));

    }

    public void updateFace(Face face) {
        this.face = face;
        postInvalidate();
    }

    public void setFullScreen(boolean isFullscreen) {
        this.isFullscreen = isFullscreen;
    }

    public void loadGifAsset(Context context, String filename) {
        InputStream is;
        try {
            is = context.getResources().getAssets().open(filename);
            movie = Movie.decodeStream(is);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void draw(Canvas canvas) {
        if (movie == null) {
            return;
        }
        //bg.setAlpha(160);
        float x = translateX(face.getPosition().x + 40);
        float y = translateY(face.getPosition().y - 20);
        float faceWidth = translateX(face.getWidth());
        float faceHeight = translateY(face.getHeight());
        long now = android.os.SystemClock.uptimeMillis();
        if (isFullscreen) {
        } else {
            canvas.drawRect(x - 20, y, x + faceWidth + 20, y + faceHeight, bg);
        }

        if (moviestart == 0) moviestart = now;

        int relTime;
        relTime = (int) ((now - moviestart) % movie.duration());
        movie.setTime(relTime);
        if (faceWidth / 2 < movie.width() / 2) {
            movie.draw(canvas, x - (movie.width() / 2 - faceWidth / 2), y);
        } else {
            movie.draw(canvas, x, y);
        }

        //postInvalidate();
        //this.invalidate();
    }

    /*@Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (movie == null) {
            return;
        }

        long now = android.os.SystemClock.uptimeMillis();

        if (moviestart == 0) moviestart = now;

        int relTime;
        relTime = (int) ((now - moviestart) % movie.duration());
        movie.setTime(relTime);
        movie.draw(canvas, 10, 10);
        this.invalidate();
    }*/
}

