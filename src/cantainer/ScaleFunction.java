package cantainer;

public interface ScaleFunction {
    /**
     * 缩放一步，可以是负数
     * @param step
     */
    public void scale(int step);

    /**
     * 重置样式为100
     */
    public void reset();

}
