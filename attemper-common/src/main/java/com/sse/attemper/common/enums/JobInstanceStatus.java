package com.sse.attemper.common.enums;

public enum JobInstanceStatus {

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

    /**
     * paused
     */
    PAUSED(4)

    ;

    private int status;

    JobInstanceStatus(int status) {
        this.status = status;
    }

    public int getStatus(){
        return status;
    }

    public static JobInstanceStatus get(int status) {
        for (JobInstanceStatus item : JobInstanceStatus.values()) {
            if(item.getStatus() == status){
                return item;
            }
        }
        return null;
    }
}
