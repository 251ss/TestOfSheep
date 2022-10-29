import cantainer.CardSlotCantainer;
import cantainer.ImageCantainer;
import components.FruitObject;
import components.Fruits;
import util.ReadResourceUtil;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 游戏主程序
 */
public class UmlPanel extends JPanel {
    ImageCantainer imageCantainer = new ImageCantainer();
    public UmlPanel(){
        this.setLayout(null);
        this.add(imageCantainer);
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                reDrawImagePanel();
            }
        });
        initContent();
    }

    public void initContent(){
        int maxLevel = 10;//多少层
        int maxWidth = 6;// 跨度个数
        int maxHeight = 5;// 最大宽度
        int maxFlop = 30;//翻牌数量;
        Random random = new Random();
        // 如果需要随机皮肤，修改为true即可
        List<String> list = ReadResourceUtil.readSkin(false);
        int typeSize = list.size();// 多少种类
        System.out.println("种类："+typeSize);
        int groupNumber = (int) Math.ceil((maxLevel*maxWidth*maxHeight+maxFlop)/(3f * typeSize));// 求得每种种类的个数
        System.out.println("每种组数："+groupNumber);
        int groupCount = groupNumber*3;
        System.out.println("每种总数："+groupCount);
        System.out.println("共计数量："+(typeSize*groupCount+maxFlop));
        // 绘制卡槽
        int initX = 100;
        int initY = 50;
        CardSlotCantainer cardSlotCantainer = new CardSlotCantainer(imageCantainer,initX+((maxWidth-7)* FruitObject.defaultWidht)/2,+initY+FruitObject.defaultHeight*(maxHeight+2));
        // 随机生成卡片集合：注意打乱顺序
        List<FruitObject> objects = new ArrayList<>();
        for (String temp : list) {
            try {
                BufferedImage bufferedImage = ImageIO.read(ReadResourceUtil.getUri("/"+temp));
                int count = groupCount+(maxFlop>0?random.nextInt(maxFlop):0);
                for (int i = 0; i < count; i++) {
                    int size = objects.size()-1;
                    Fruits fruits = new Fruits(imageCantainer,bufferedImage,temp);
                    fruits.setPreferredSize(new Dimension(100, 100));
                    int index = 0;
                    if(size>10) {
                        index = random.nextInt(size);
                    }
                    objects.add(index, new FruitObject(cardSlotCantainer,fruits, 0, 0, 0));
                    maxFlop--;
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        System.out.println("实际数量："+objects.size());
        // 给每个对象设置坐标
        int index = 0;
        for (int i = 0; i < maxLevel; i++) {
            for (int x = 0; x < maxWidth; x++) {
                for (int y = 0; y < maxHeight; y++) {
                    FruitObject fruitObject = objects.get(index++);
                    fruitObject.setX(x);
                    fruitObject.setY(y);
                    fruitObject.setLevel(i);
                    fruitObject.show(imageCantainer,initX-FruitObject.defaultWidht/4,initY-FruitObject.defaultHeight/4);
                }
            }
        }
        System.out.println("重叠数量："+index);
        //  绘制翻牌区
        int size = objects.size();
        if(index<size){
            int step = 5;
            int lenght = (size-index)/2;
            for (int i = 0; i < lenght; i++) {//绘制左边
                FruitObject fruitObject = objects.get(index++);
                fruitObject.setX(0);
                fruitObject.setY(maxHeight+1);
                fruitObject.setLevel(i);
                fruitObject.showFold(imageCantainer,initX+(lenght*step),initY,-i*step,true);
            }
            System.out.println("左翻牌区数："+lenght+"\t"+index);
            lenght = size-index;
            for (int i = 0 ; i < lenght; i++) {//绘制左边
                FruitObject fruitObject = objects.get(index++);
                fruitObject.setX(maxWidth-1);
                fruitObject.setY(maxHeight+1);
                fruitObject.setLevel(i);
                fruitObject.showFold(imageCantainer,initX-(lenght*step),initY,i*step,false);
            }
            System.out.println("右翻牌区数："+lenght+"\t"+index);
        }
    }

    private void reDrawImagePanel(){
        int leftX = 15;
        int width = getWidth()-leftX-15;
        int height = getHeight()-leftX-15;
        imageCantainer.setBounds(leftX, leftX,width,height);
        imageCantainer.setPreferredSize(new Dimension(width, height));
        imageCantainer.setLayout(null);
    }

    public static void main(String[] args) {
        JFrame jFrame = new JFrame("yang了yang");
        jFrame.setLayout(new BorderLayout());
        jFrame.setPreferredSize(new Dimension(900,1000));
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.add(new UmlPanel(),BorderLayout.CENTER);
        jFrame.setVisible(true);
        jFrame.pack();
    }

}
