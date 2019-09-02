package com.github.attemper.common.enums;

public enum InstanceStatus {

    /** running */
    RUNNING(0),

    /** success */
    SUCCESS(1),

    /**
     * FAILURE
     */
    FAILURE(2),

    /**
     * terminated
     */
    TERMINATED(3),

    ;

    private int status;

    InstanceStatus(int status) {
        this.status = status;
    }

    public int getStatus(){
        return status;
    }

    public static InstanceStatus get(int status) {
        for (InstanceStatus item : InstanceStatus.values()) {
            if(item.getStatus() == status){
                return item;
            }
        }
        return null;
    }
}
