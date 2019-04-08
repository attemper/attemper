package com.sse.attemper.common.enums;

/**
 * @author ldang
 */
public enum UserStatus {

    /** 正常使用 */
    NORMAL(0),

    /** 冻结，不可正常使用 */
    FROZEN(1),

    /** 注销，即逻辑删除 */
    DELETED(2)

    ;

    private int status;

    private UserStatus(int status){
        this.status = status;
    }

    public int getStatus(){
        return status;
    }

    /**
     * 根据status获取枚举对象
     * @param status
     * @return
     */
    public static UserStatus get(int status){
        for(UserStatus _userStatus : UserStatus.values()){
            if(_userStatus.getStatus() == status){
                return _userStatus;
            }
        }
        return null;
    }
}
