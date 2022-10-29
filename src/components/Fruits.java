package components;

import drager.ShapeDrager;

import javax.swing.*;
import java.awt.*;

/**
 * 卡片对象
 * @author miukoo
 * @see  <a href="https://gitee.com/miukoo/yang-liao-ge-yangy.git">project home</a>
 * @see  <a href="http://gjsm.cn/">athor website</a>
 */
public class Fruits extends ShapeDrager {

    // 卡片上放置的图形
    Image image = null;
    // 卡片名称
    String imageName;

    // 卡片圆角边框
    int arc = 10;
    // 透明度
    float alpha = 1;
    Color bgColor = new Color(138,158,58);
    public Fruits(JPanel parent,Image image,String imageName){
        super(parent);
        this.image = image;
        this.imageName = imageName;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        BasicStroke basicStroke = new BasicStroke(2);
        g2d.setStroke(basicStroke);
        if(alpha<1) {
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        }
        g2d.rotate(getRotation(),getWidth()/2,getHeight()/2);
        // 绘制底色
        g2d.setColor(bgColor);
        g2d.fillRoundRect(0, 0, (int) (getSize().width - 1), (int) (getSize().height - 1),arc,arc);
        // 绘制白色
        g2d.setColor(Color.WHITE);
        g2d.fillRoundRect(0, 0, (int) (getSize().width - 1), (int) (getSize().height - 1-5),arc,arc);
        // 绘制外边框
        g2d.setColor(bgColor);
        g2d.drawRoundRect(0, 0, (int) (getSize().width - 1), (int) (getSize().height - 1),arc,arc);
        // 绘制图片
        if(image!=null) {
            int imageWidth = image.getWidth(null);
            int imageHeight = image.getHeight(null);
            int x = (getWidth()-imageWidth)/2;
            int y = (getHeight()-5-imageHeight)/2;
            g2d.drawImage(image,x,y,imageWidth,imageHeight,null);
        }
        super.paintComponent(g);
    }

    public String getImageName() {
        return imageName;
    }

    public void setAlpha(float alpha) {
        this.alpha = alpha;
        repaint();
    }
}
