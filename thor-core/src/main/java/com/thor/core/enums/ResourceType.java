package com.thor.core.enums;

/**
 * @author ldang
 */
public enum ResourceType {

    /** 文件夹 */
    FOLDER(0),

    /** 菜单 */
    MENU(1),

    /** 按钮 */
    BUTTON(2),

    /** 区块 */
    BLOCK(3)
    ;

    private int type;

    private ResourceType(int type){
        this.type = type;
    }

    public int getType(){
        return type;
    }

    /**
     * 获取枚举对象
     * @param type
     * @return
     */
    public static ResourceType get(int type){
        for(ResourceType resourceType : ResourceType.values()){
            if(resourceType.getType() == type){
                return resourceType;
            }
        }
        return null;
    }
}
