package drager;

import javax.swing.*;
import java.awt.*;

/**
 * 卡片超类
 * @author miukoo
 * @see  <a href="https://gitee.com/miukoo/yang-liao-ge-yangy.git">project home</a>
 * @see  <a href="http://gjsm.cn/">athor website</a>
 */
public abstract class ShapeDrager extends JPanel {
    // 旋转的角度
    double rotation = 0;
    // 是否图标模式
    boolean iconType = false;

    public ShapeDrager(JComponent parent){
        this(parent,false);
    }

    public ShapeDrager(JComponent parent, boolean iconType){
        this.iconType = iconType;
        this.setFocusable(true);
        if(!iconType) {
            this.setCursor(new Cursor(Cursor.MOVE_CURSOR));
        }else{
            this.setCursor(new Cursor(Cursor.HAND_CURSOR));
        }
        initShape();
    }

    private void initShape(){
        this.setOpaque(false);
        this.setLayout(new BorderLayout());
    }

    public double getRotation() {
        return rotation;
    }
}
