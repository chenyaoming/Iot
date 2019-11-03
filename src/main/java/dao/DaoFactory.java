package dao;

public class DaoFactory {
    /**
     * 类级的内部类，也就是静态的成员式内部类，该内部类的实例与外部类的实例
     * 没有绑定关系，而且只有被调用到才会装载，从而实现了延迟加载
     */
    private static class SingletonHolder{
        /**
         * 静态初始化器，由JVM来保证线程安全
         */
        private static TbDeviceDao deviceDao = new TbDeviceDao();

        private static TbUserDao userDao = new TbUserDao();

        private static TbBorrowRecordDao borrowRecordDao = new TbBorrowRecordDao();
    }
    /**
     * 私有化构造方法
     */
    private DaoFactory(){
    }

    public static  TbDeviceDao getDeviceDao(){
        return SingletonHolder.deviceDao;
    }

    public static  TbUserDao getUserDao(){
        return SingletonHolder.userDao;
    }

    public static  TbBorrowRecordDao getBorrowRecordDao(){
        return SingletonHolder.borrowRecordDao;
    }
}