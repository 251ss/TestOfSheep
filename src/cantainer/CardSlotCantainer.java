package cantainer;

import com.sun.media.jfxmedia.AudioClip;
import components.FruitObject;

import util.ReadResourceUtil;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseListener;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;


/**
 * 验卡区
 */
public class CardSlotCantainer extends JPanel {

    static AudioClip audioClip;
    static AudioClip failClip;
    static {
        try {
            audioClip = AudioClip.load(ReadResourceUtil.readAudio("win.wav").toURI());
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            failClip = AudioClip.load(ReadResourceUtil.readAudio("fail.wav").toURI());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // 卡片间隔
    int step = 5;
    // 内外底色的间隔
    int borderSize = 10;
    // 卡片最大数
    int solt = 7;
    // 是否结束
    boolean isOver = false;

    Color bgColor = new Color(157,97,27);
    Color borderColor = new Color(198,128,48);
    // 圆角幅度
    int arc = 15;
    List<FruitObject> slot = new ArrayList<>();
    int initX;
    int initY;
    public CardSlotCantainer(ImageCantainer imageCantainer,int initX, int initY){
        initY+=20;
        initX+=-borderSize;
        this.setBorder(new EmptyBorder(0,0,0,0));
        this.setOpaque(false);
        this.setFocusable(true);
        this.setLayout(new FlowLayout());
        this.initY = initY;
        this.initX = initX;
        setBounds(initX,initY, FruitObject.defaultWidht*solt+step*2+borderSize*2,FruitObject.defaultHeight+step*2+borderSize*2);
        imageCantainer.add(this);
    }

    // 点击添加
    public void addSlot(FruitObject object){
        if(isOver){
            return;
        }
        slot.add(object);
        // 验卡区的卡片删除点击事件
        object.removeImageCantainer();
        MouseListener[] mouseListeners = object.getFruits().getMouseListeners();
        if(mouseListeners!=null){
            for (MouseListener mouseListener : mouseListeners) {
                object.getFruits().removeMouseListener(mouseListener);
            }
        }
        // 排序验卡区中的图片
        slot.sort(Comparator.comparing(FruitObject::getImageName));
        // 3张图片的判断，如果有直接消除，思路是：分组后看每组数量是否超过3张如果超过则消除
        Map<String, List<FruitObject>> map = slot.stream().collect(Collectors.groupingBy(FruitObject::getImageName));
        Set<String> keys = map.keySet();
        for (String key : keys) {
            List<FruitObject> objects = map.get(key);
            if(objects.size()==3){
                if(audioClip!=null){
                    audioClip.play();
                }
                // 消除的元素直接从集合中删除
                for (FruitObject fruitObject : objects) {
                    fruitObject.removeCardSlotCantainer();
                }
                slot.removeAll(objects);
            }
        }
        // 新添加的卡片，显示到验卡区
        redraw();
        // 判断游戏是否结束
        if(slot.size()==solt){
            isOver = true;
            failClip.play();
            JOptionPane.showMessageDialog(this.getParent(), "Game Over：槽满了","Tip",JOptionPane.ERROR_MESSAGE);
        }
    }

    public void redraw(){
        this.removeAll();
        for (int i = 0; i < slot.size(); i++) {
            FruitObject fruitObject = slot.get(i);
            int pointX =  step+i*FruitObject.defaultWidht+borderSize/2;
            fruitObject.getFruits().setBounds(pointX,borderSize,FruitObject.defaultWidht,FruitObject.defaultHeight+step);
            this.add(fruitObject.getFruits());
        }
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        BasicStroke basicStroke = new BasicStroke(borderSize);
        g2d.setStroke(basicStroke);
        // 绘制第1层底色
        g2d.setColor(bgColor);
        g2d.fillRoundRect(0, 0, (int) (getSize().width - borderSize), (int) (getSize().height - borderSize),arc,arc);
        // 绘制第2层底色
        g2d.setColor(borderColor);
        g2d.fillRoundRect(borderSize, borderSize, (int) (getSize().width - 1-borderSize*3), (int) (getSize().height - 1-borderSize*3),arc,arc);
        super.paintComponent(g);
    }

}
