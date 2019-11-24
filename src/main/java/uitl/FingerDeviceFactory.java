package uitl;


public class FingerDeviceFactory {
    /**
     * 类级的内部类，也就是静态的成员式内部类，该内部类的实例与外部类的实例
     * 没有绑定关系，而且只有被调用到才会装载，从而实现了延迟加载
     */
    private static class SingletonHolder{
        /**
         * 静态初始化器，由JVM来保证线程安全
         */
        private static FingerDevice fingerDevice = null;

        public static FingerDevice getFingerDeviceIntance(){
            if(null != fingerDevice){
                return fingerDevice;
            }
            try {
                fingerDevice = new FingerDevice();
            }catch (Exception e){

            }
            return fingerDevice;
        }
    }
    /**
     * 私有化构造方法
     */
    private FingerDeviceFactory(){
    }

    public static FingerDevice getFingerDeviceIntance(){
        return SingletonHolder.getFingerDeviceIntance();
    }

}