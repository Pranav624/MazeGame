package edu.wm.cs.cs301.pranavgonepalli.gui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import edu.wm.cs.cs301.pranavgonepalli.R;

public class MazePanel extends View {
    private Paint painter;
    private Bitmap bitmap;
    private Canvas mcanvas;

    public MazePanel(Context context){
        super(context);
        init();
    }
    public MazePanel(Context context, AttributeSet attrs){
        super(context, attrs);
        init();
    }

    private void init(){
        painter = new Paint(Paint.ANTI_ALIAS_FLAG);
        bitmap = Bitmap.createBitmap(getContext().getResources().getDisplayMetrics(), 800, 800, Bitmap.Config.ARGB_8888);
        mcanvas = new Canvas(bitmap);
    }

    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
        canvas.drawBitmap(bitmap, null, new Rect(130, 790, 930, 1590), null);
        drawTestImage(canvas);
    }

    /**
     * Draws a filled rectangle.
     * @param x
     * @param y
     * @param width
     * @param height
     * @param color
     */
    private void drawFilledRectangle(int x, int y, int width, int height, int color){
        painter.setStyle(Paint.Style.FILL);
        painter.setColor(color);
        mcanvas.drawRect(x, y, x + width, y + height, painter);
    }

    /**
     * Draws a filled circle.
     * @param x
     * @param y
     * @param radius
     * @param color
     */
    private void drawFilledCircle(int x, int y, int radius, int color){
        painter.setStyle(Paint.Style.FILL);
        painter.setColor(color);
        mcanvas.drawCircle(x, y, radius, painter);
    }

    /**
     * Draws a filled polygon.
     * @param xPoints
     * @param yPoints
     * @param nPoints
     */
    private void drawFilledPolygon(int[] xPoints, int[] yPoints, int nPoints, int color){
        painter.setStyle(Paint.Style.FILL);
        painter.setColor(color);
        Path path = new Path();
        path.moveTo(xPoints[0], yPoints[0]);
        for(int x = 1; x < nPoints; x++){
            path.lineTo(xPoints[x], yPoints[x]);
        }
        mcanvas.drawPath(path, painter);
    }
    /**
     * Draws the test image as a placeholder for project 6.
     * @param canvas
     */
    private void drawTestImage(Canvas canvas){
        mcanvas = canvas;
        drawFilledRectangle(130, 790, 800, 500, 0xff949494);
        drawFilledRectangle(130, 1290, 800, 300, 0xff000000);
        int[] xPoints = {130, 330, 330, 130};
        int[] yPoints = {790, 990, 1390, 1590};
        drawFilledPolygon(xPoints, yPoints, 4, 0xff00820f);
        int[] xPoints2 = {930, 730, 730, 930};
        int[] yPoints2 = {790, 990, 1390, 1590};
        drawFilledPolygon(xPoints2, yPoints2, 4, 0xffeeff00);
        drawFilledCircle(530, 930, 100, 0xffff0000);
    }
}
