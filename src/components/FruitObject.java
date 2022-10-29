package components;

import cantainer.CardSlotCantainer;
import cantainer.ImageCantainer;
import com.sun.media.jfxmedia.AudioClip;
import util.ReadResourceUtil;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;

/**
 * 卡片对象放置位置对象：记录卡片放在那个位置
 */
public class FruitObject  {

    // 卡片默认宽度
    public static final int defaultWidht = 100;
    // 卡片默认高度
    public static final int defaultHeight = 100;
    static Random RANDOM = new Random();
    // 点击卡片的声音
    static AudioClip audioClip;
    static {
        try {
            audioClip = AudioClip.load(ReadResourceUtil.readAudio("click.wav").toURI());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 存放的卡片
    Fruits fruits;
    // 所在层的x序号
    int x;
    // 所在层的y序号
    int y;
    // 所在层序号
    int level;
    // 卡片名称,有点重复了，后续优化
    String imageName;//名称，非常重要，安装名称来判断是否为同一类型
    boolean flag = true;//是否可以点击
    boolean leftFold = false;// 是否为遮挡元素
    boolean rightFold = false;// 是否为遮挡元素
    ImageCantainer imageCantainer;
    CardSlotCantainer cardSlotCantainer;

    public FruitObject(CardSlotCantainer cardSlotCantainer,Fruits fruits, int x, int y, int level) {
        this.fruits = fruits;
        this.x = x;
        this.y = y;
        this.level = level;
        this.imageName = fruits.getImageName();
        this.cardSlotCantainer = cardSlotCantainer;
    }

    public Fruits getFruits() {
        return fruits;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getImageName() {
        return imageName;
    }

    /**
     * 添加到叠卡区
     * @param imageCantainer
     * @param initX
     * @param initY
     */
    public void show(ImageCantainer imageCantainer, int initX, int initY) {
        this.imageCantainer = imageCantainer;
        // 随机生成开始坐标偏移量，实现上下层错落有致的视觉感
        boolean ranDomWidth = RANDOM.nextInt(10)%2==0;
        boolean ranDomHeight = RANDOM.nextInt(10)%2==0;
        int pointX = initX + x*defaultWidht+(ranDomWidth?defaultWidht/2:0);
        int pointY = initY + y*defaultHeight+(ranDomHeight?defaultHeight/2:0);
        // 设置卡片显示在背景面板中位置
        fruits.setBounds(pointX,pointY,defaultWidht,defaultHeight);
        // 记录卡片的空间信息
        SpaceManager.rectangle(this);
        imageCantainer.add(fruits,0);
        addClick();
    }

    /**
     * 添加到翻牌区
     * @param imageCantainer
     * @param initX
     * @param initY
     * @param offset
     * @param isLeft
     */
    public void showFold(ImageCantainer imageCantainer, int initX, int initY, int offset,boolean isLeft) {
        this.imageCantainer = imageCantainer;
        // 随机生成开始坐标偏移量，实现上下层错落有致的视觉感
        int pointX = initX + x*defaultWidht+offset;
        int pointY = initY + y*defaultHeight-defaultHeight/4;
        if(isLeft){
            this.leftFold = true;
        }else{
            this.rightFold= true;
        }
        // 设置卡片显示在背景面板中位置
        fruits.setBounds(pointX,pointY,defaultWidht,defaultHeight);
        // 记录卡片的空间信息
        SpaceManager.rectangle(this);
        imageCantainer.add(fruits,0);
        addClick();
    }

    /**
     * 卡片点击事件：点击后添加到验卡区
     */
    public void addClick(){
        fruits.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(flag) {
                    if (audioClip != null) {
                        audioClip.play();
                    }
                    cardSlotCantainer.addSlot(FruitObject.this);
                }
            }
        });
    }

    public void removeImageCantainer() {
        Rectangle visibleRect = fruits.getVisibleRect();
        SpaceManager.removeCompontFlag(this);
        imageCantainer.remove(fruits);
        imageCantainer.repaint(visibleRect);
    }

    public void removeCardSlotCantainer() {
        cardSlotCantainer.remove(fruits);
    }

    public boolean isLeftFold() {
        return leftFold;
    }

    public boolean isRightFold() {
        return rightFold;
    }

    /**
     * 设置
     * @param flag=false 不可以用
     */
    public void setFlag(boolean flag) {
        if(this.flag!=flag){//需要更新透明度
            getFruits().setAlpha(flag?1:0.2f);
        }
        this.flag = flag;
    }
}
