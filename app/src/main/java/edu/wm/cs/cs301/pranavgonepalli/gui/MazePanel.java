package edu.wm.cs.cs301.pranavgonepalli.gui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

/**
 * Class that draws the custom view for the maze.
 */
public class MazePanel extends View implements P7PanelF22 {
    private Paint painter;
    private Bitmap bitmap;
    private Canvas myCanvas;
    private boolean isOperational;

    public MazePanel(Context context){
        super(context);
        init();
    }
    public MazePanel(Context context, AttributeSet attrs){
        super(context, attrs);
        init();
    }

    /**
     * Set up the painter, bitmap, and canvas.
     */
    private void init(){
        painter = new Paint();
        bitmap = Bitmap.createBitmap(1000, 1000, Bitmap.Config.ARGB_8888);
        myCanvas = new Canvas(bitmap);
        isOperational = true;
    }

    /**
     * Draw the custom view.
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
        canvas.drawBitmap(bitmap, 0, 0, painter);
        //addBackground(100);
        //drawTestImage(canvas);
    }

    @Override
    public void commit(){
        invalidate();
    }

    @Override
    public boolean isOperational(){
        return isOperational;
    }

    @Override
    public void setColor(int argb){
        painter.setColor(argb);
    }

    @Override
    public int getColor(){
        return painter.getColor();
    }

    @Override
    public void addBackground(float percentToExit){
        setColor(0xff000000);
        addFilledRectangle(0, 0, 1000, 500);
        setColor(0xff1a1a1a);
        addFilledRectangle(0, 500, 1000, 500);
    }

    /**
     * Draws a filled rectangle.
     * @param x is the x-coordinate of the top left corner
     * @param y is the y-coordinate of the top left corner
     * @param width is the width of the rectangle
     * @param height is the height of the rectangle
     */
    @Override
    public void addFilledRectangle(int x, int y, int width, int height){
        painter.setStyle(Paint.Style.FILL);
        myCanvas.drawRect(x, y, x + width, y + height, painter);
    }

    /**
     * Draws a filled polygon.
     * @param xPoints are the x-coordinates of points for the polygon
     * @param yPoints are the y-coordinates of points for the polygon
     * @param nPoints is the number of points, the length of the arrays
     */
    @Override
    public void addFilledPolygon(int[] xPoints, int[] yPoints, int nPoints){
        painter.setStyle(Paint.Style.FILL);
        Path path = new Path();
        path.moveTo(xPoints[0], yPoints[0]);
        for(int x = 1; x < nPoints; x++){
            path.lineTo(xPoints[x], yPoints[x]);
        }
        myCanvas.drawPath(path, painter);
    }

    /**
     * Draws a polygon.
     * @param xPoints are the x-coordinates of points for the polygon
     * @param yPoints are the y-coordinates of points for the polygon
     * @param nPoints is the number of points, the length of the arrays
     */
    @Override
    public void addPolygon(int[] xPoints, int[] yPoints, int nPoints){
        Path path = new Path();
        path.moveTo(xPoints[0], yPoints[0]);
        for(int x = 1; x < nPoints; x++){
            path.lineTo(xPoints[x], yPoints[x]);
        }
        myCanvas.drawPath(path, painter);
    }

    /**
     * Draws a line.
     * @param startX is the x-coordinate of the starting point
     * @param startY is the y-coordinate of the starting point
     * @param endX is the x-coordinate of the end point
     * @param endY is the y-coordinate of the end point
     */
    @Override
    public void addLine(int startX, int startY, int endX, int endY){
        painter.setStyle(Paint.Style.STROKE);
        painter.setStrokeWidth(5);
        myCanvas.drawLine(startX, startY, endX, endY, painter);
    }

    /**
     * Draws a filled oval.
     * @param x is the x-coordinate of the top left corner
     * @param y is the y-coordinate of the top left corner
     * @param width is the width of the oval
     * @param height is the height of the oval
     */
    @Override
    public void addFilledOval(int x, int y, int width, int height){
        painter.setStyle(Paint.Style.FILL);
        myCanvas.drawOval(x, y, x + width, y + height, painter);
    }

    /**
     * Draws an arc.
     * @param x the x coordinate of the upper-left corner of the arc to be drawn.
     * @param y the y coordinate of the upper-left corner of the arc to be drawn.
     * @param width the width of the arc to be drawn.
     * @param height the height of the arc to be drawn.
     * @param startAngle the beginning angle.
     * @param arcAngle the angular extent of the arc, relative to the start angle.
     */
    @Override
    public void addArc(int x, int y, int width, int height, int startAngle, int arcAngle){
        myCanvas.drawArc(x, y, x + width, x + height, startAngle, arcAngle, true, painter);
    }

    /**
     * Draws a string.
     * @param x the x coordinate
     * @param y the y coordinate
     * @param str the string
     */
    @Override
    public void addMarker(float x, float y, String str){
        myCanvas.drawText(str, x, y, painter);
    }

    @Override
    public void setRenderingHint(P7RenderingHints hintKey, P7RenderingHints hintValue){
        painter.setAntiAlias(true);
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
        myCanvas.drawCircle(x, y, radius, painter);
    }


    /**
     * Draws the test image as a placeholder for project 6.
     * @param canvas
     */
    private void drawTestImage(Canvas canvas){
        myCanvas = canvas;
        setColor(0xff949494);
        addFilledRectangle(0, 0, 1000, 600);
        setColor(0xff000000);
        addFilledRectangle(0, 600, 1000, 400);
        int[] xPoints = {0, 200, 200, 0};
        int[] yPoints = {0, 200, 800, 1000};
        setColor(0xff00820f);
        addFilledPolygon(xPoints, yPoints, 4);
        int[] xPoints2 = {1000, 800, 800, 1000};
        int[] yPoints2 = {0, 200, 800, 1000};
        setColor(0xffeeff00);
        addFilledPolygon(xPoints2, yPoints2, 4);
        setColor(0xffff0000);
        addFilledOval(450, 50, 100, 100);
    }
}
