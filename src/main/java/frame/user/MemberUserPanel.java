package frame.user;


public class MemberUserPanel extends UserPanel{

    @Override
    public Integer getUserType() {
        //借用人和归还人管理
        return 1;
    }
}
