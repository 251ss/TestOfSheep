package components;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 记录每层卡片的空间信息，用于实现上下层是否可点击的动态刷新
 */
public class SpaceManager {
    // Map<X坐标点, Set<层数,HashMap<Y坐标点Integer,FruitObject>>>
    static Map<Integer, Map<Integer, List<FruitObject>>> LEAVL_DATA_X1 = new HashMap<>();
    static Map<Integer, Map<Integer, List<FruitObject>>> LEAVL_DATA_X2 = new HashMap<>();
    static Map<Integer, Map<Integer, List<FruitObject>>> LEAVL_DATA_X3 = new HashMap<>();
    static Map<Integer, Map<Integer, List<FruitObject>>> LEAVL_DATA_Y1 = new HashMap<>();
    static Map<Integer, Map<Integer, List<FruitObject>>> LEAVL_DATA_Y2 = new HashMap<>();
    static Map<Integer, Map<Integer, List<FruitObject>>> LEAVL_DATA_Y3 = new HashMap<>();

    /**
     * 记录组件坐标重叠数据
     */
    public static void rectangle(FruitObject fruitObject){
        Rectangle bounds  = fruitObject.fruits.getBounds();
        if(fruitObject.isLeftFold()){
            bounds.x=0;bounds.y=0;
        }
        if(fruitObject.isRightFold()){
            bounds.x=1;bounds.y=1;
        }
        int x1 = bounds.x;
        int x2 = bounds.x+FruitObject.defaultWidht/2;
        int x3 = bounds.x+FruitObject.defaultHeight;
        int y1 = bounds.y;
        int y2 = bounds.y+FruitObject.defaultHeight/2;
        int y3 = bounds.y+FruitObject.defaultHeight;
        putToCache(x1,fruitObject,LEAVL_DATA_X1);
        putToCache(x2,fruitObject,LEAVL_DATA_X2);
        putToCache(x3,fruitObject,LEAVL_DATA_X3);
        putToCache(y1,fruitObject,LEAVL_DATA_Y1);
        putToCache(y2,fruitObject,LEAVL_DATA_Y2);
        putToCache(y3,fruitObject,LEAVL_DATA_Y3);
        updateCompontFlag(fruitObject,false,bounds);
    }

    private static void putToCache(int point,FruitObject fruitObject,Map<Integer, Map<Integer, List<FruitObject>>> LEAVL_DATA){
        Map<Integer, List<FruitObject>> hashLevel = LEAVL_DATA.get(fruitObject.level);
        if(hashLevel==null){
            hashLevel = new HashMap<>();
        }
        List<FruitObject> objects = hashLevel.get(point);
        if(objects==null){
            objects = new ArrayList<>();
        }
        objects.add(fruitObject);
        hashLevel.put(point,objects);
        LEAVL_DATA.put(fruitObject.level,hashLevel);
    }

    /**
     * 消除卡片时，计算下一层点亮的卡片
     * @param fruitObject
     */
    public static void removeCompontFlag(FruitObject fruitObject){
        Rectangle bounds  = fruitObject.fruits.getBounds();
        if(fruitObject.isLeftFold()){
            bounds.x=0;bounds.y=0;
        }
        if(fruitObject.isRightFold()){
            bounds.x=1;bounds.y=1;
        }
        int x1 = bounds.x;
        int x2 = bounds.x+FruitObject.defaultWidht/2;
        int x3 = bounds.x+FruitObject.defaultHeight;
        int y1 = bounds.y;
        int y2 = bounds.y+FruitObject.defaultHeight/2;
        int y3 = bounds.y+FruitObject.defaultHeight;
        deleteLevelFlag(x1,fruitObject,LEAVL_DATA_X1);
        deleteLevelFlag(x2,fruitObject,LEAVL_DATA_X2);
        deleteLevelFlag(x3,fruitObject,LEAVL_DATA_X3);
        deleteLevelFlag(y1,fruitObject,LEAVL_DATA_Y1);
        deleteLevelFlag(y2,fruitObject,LEAVL_DATA_Y2);
        deleteLevelFlag(y3,fruitObject,LEAVL_DATA_Y3);
        updateCompontFlag(fruitObject,true,bounds);
    }

    private static void deleteLevelFlag(int point,FruitObject fruitObject,Map<Integer, Map<Integer, List<FruitObject>>> LEAVL_DATA){
        Map<Integer, List<FruitObject>> hashLevel = LEAVL_DATA.get(fruitObject.getLevel());
        if(hashLevel==null){
            return;
        }
        List<FruitObject> objects = hashLevel.get(point);//删除对应层次对应点的坐标
        if(objects==null){
            hashLevel.remove(point);
            if(hashLevel.size()==0){
                LEAVL_DATA.remove(fruitObject.getLevel());
            }else {
                LEAVL_DATA.put(fruitObject.getLevel(), hashLevel);
            }
            return;
        }
        objects.remove(fruitObject);//删除对应层次对应点的坐标
        if(objects.isEmpty()){
            hashLevel.remove(point);
            if(hashLevel.size()==0){
                LEAVL_DATA.remove(fruitObject.getLevel());
            }else {
                LEAVL_DATA.put(fruitObject.getLevel(), hashLevel);
            }
        }
    }

    /**
     * 更新底层节点的状态,false修改为不可点，反之可点
     * @param fruitObject
     */
    private static void updateCompontFlag(FruitObject fruitObject,boolean flag,Rectangle bounds){
        int level = fruitObject.getLevel();
        int x1 = bounds.x;
        int x2 = bounds.x+FruitObject.defaultWidht/2;
        int x3 = bounds.x+FruitObject.defaultHeight;
        int y1 = bounds.y;
        int y2 = bounds.y+FruitObject.defaultHeight/2;
        int y3 = bounds.y+FruitObject.defaultHeight;
        for (int i = 0; i<level; i++) {
            updateLevelFlag(x1,x2,x3,y1,y2,y3,x1,i,LEAVL_DATA_X1,flag);
            updateLevelFlag(x1,x2,x3,y1,y2,y3,x2,i,LEAVL_DATA_X2,flag);
            updateLevelFlag(x1,x2,x3,y1,y2,y3,x3,i,LEAVL_DATA_X3,flag);
            updateLevelFlag(x1,x2,x3,y1,y2,y3,y1,i,LEAVL_DATA_Y1,flag);
            updateLevelFlag(x1,x2,x3,y1,y2,y3,y2,i,LEAVL_DATA_Y2,flag);
            updateLevelFlag(x1,x2,x3,y1,y2,y3,y3,i,LEAVL_DATA_Y3,flag);
        }
    }

    private static void updateLevelFlag(int x1,int x2,int x3,int y1,int y2,int y3,int point,int level,Map<Integer, Map<Integer, List<FruitObject>>> LEAVL_DATA,boolean flag){
        Map<Integer, List<FruitObject>> hashLevel = LEAVL_DATA.get(level);
        if(hashLevel==null){
            return;
        }
        List<FruitObject> objects = hashLevel.get(point);
        if(objects==null){
            return;
        }
        if(flag) {// 当更新的时候需要强制检查
            flag = !LEAVL_DATA.containsKey(level + 1);//上一层都没了，可以点击
        }
        //上一层存在，但是没有遮挡可以点击
        if(!flag){
            flag = !(isExsit(x1,level+1,LEAVL_DATA_X1)
                    && isExsit(x2,level+1,LEAVL_DATA_X2)
                    && isExsit(x3,level+1,LEAVL_DATA_X3)
                    && isExsit(y1,level+1,LEAVL_DATA_Y1)
                    && isExsit(y2,level+1,LEAVL_DATA_Y2)
                    && isExsit(y3,level+1,LEAVL_DATA_Y3));
        }
        for (FruitObject object : objects) {
            object.setFlag(flag);
        }
    }

    private static boolean isExsit(int point,int level,Map<Integer, Map<Integer, List<FruitObject>>> LEAVL_DATA){
        Map<Integer, List<FruitObject>> hashLevel = LEAVL_DATA.get(level);
        if(hashLevel==null){
            return false;
        }
        List<FruitObject> objects = hashLevel.get(point);
        if(objects==null||objects.isEmpty()){
            return false;
        }
        return true;
    }


}
