package cantainer;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.Random;

/**
 * 游戏背景区
 */
public class ImageCantainer extends JPanel implements ScaleFunction  {

    // 背景颜色
    Color backgroudColor = Color.WHITE;
    // 间距距
    int step = 20;
    // 缩放比例，每走一格+-5
    int scale = 100;

    public ImageCantainer(){
        this.setLayout(null);
        this.setBorder(new EmptyBorder(20,20,20,20));
        this.setBackground(Color.RED);
        this.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
    }

    // 背景色
    Color bgColor = new Color(195,254,139);
    // 背景图标色
    Color grassColor = new Color(95,154,39);
    Random random =  new Random();

    public void drawX(Graphics g){
        int size = 20;
        int width = (int) (this.getWidth()/(scale/100F));
        int height = (int) (this.getHeight()/(scale/100F));
        int skip = (int) (step*scale/100F);
        Graphics2D graphics = (Graphics2D)g;
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        // 背景色
        graphics.setColor(bgColor);
        graphics.fillRect(0,0,width,height);
        // 设置画笔颜色或大小
        graphics.setColor(grassColor);
        BasicStroke basicStroke = new BasicStroke(2);
        graphics.setStroke(basicStroke);
        // 随机绘制各种大小图形
        for (int i = 0; i < width; i+=skip) {
            graphics.drawRect(random.nextInt(width),random.nextInt(height),random.nextInt(size),random.nextInt(size));
            graphics.drawArc(random.nextInt(width),random.nextInt(height),random.nextInt(size),random.nextInt(size),random.nextInt(360),random.nextInt(360));
            graphics.drawOval(random.nextInt(width),random.nextInt(height),random.nextInt(size),random.nextInt(size));
            graphics.drawRoundRect(random.nextInt(width),random.nextInt(height),random.nextInt(size),random.nextInt(size),random.nextInt(360),random.nextInt(360));
        }
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        Graphics2D g = (Graphics2D)graphics;
        clearPane(g);
        AffineTransform transform = new AffineTransform(g.getTransform());
        g.setTransform(transform);
        g.scale(scale/100F,scale/100F);
        graphics.setColor(backgroudColor);
        graphics.fillRect(0,0,getWidth(),getHeight());
        drawX(graphics);
        transform.concatenate(g.getTransform());
    }

    // 清空背景
    private void clearPane(Graphics2D g) {
        g.clearRect(0, 0, getWidth(), getHeight());
    }

    /**
     * 重置大小
     */
    @Override
    public void reset(){
        scale=100;
        scale(0);
    }

    /**
     * 放大或者缩写
     * @param step
     */
    @Override
    public void scale(int step){
        scale+=step;
        if(scale<20){
            scale=20;
        }
        if(scale>500){
            scale=500;
        }
        applyZoom();
    }

    private void applyZoom() {
        setPreferredSize(new Dimension((int) (scale/100F * getWidth()), (int) (scale/100F * getHeight())));
        validate();
        repaint();
    }

}
