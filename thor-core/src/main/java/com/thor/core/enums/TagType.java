package com.thor.core.enums;

/**
 * @author ldang
 */
public enum TagType {

    /** 角色 */
    ROLE(0),

    /** 用户组 */
    GROUP(1),

    /** 岗位 */
    POST(2)

    ;

    private int type;

    private TagType(int type){
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
    public static TagType get(int type){
        for(TagType tagType : TagType.values()){
            if(tagType.getType() == type){
                return tagType;
            }
        }
        return null;
    }
}
