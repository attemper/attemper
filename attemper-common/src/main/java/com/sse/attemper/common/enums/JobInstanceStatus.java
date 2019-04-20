package com.sse.attemper.common.enums;

public enum JobInstanceStatus {

    /** running */
    RUNNING(0),

    /** success */
    SUCCESS(1),

    /** termination */
    TERMINATION(2),

    /** failure */
    FAILURE(3),

    /** running */
    RETRY_RUNNING(4),

    /** success */
    RETRY_SUCCESS(5),

    /** termination */
    RETRY_TERMINATION(6),

    /** failure */
    RETRY_FAILURE(7)

    ;

    private int status;

    JobInstanceStatus(int status){
        this.status = status;
    }

    public int getStatus(){
        return status;
    }

    /**
     * @param status
     * @return
     */
    public static JobInstanceStatus get(int status){
        for(JobInstanceStatus item : JobInstanceStatus.values()){
            if(item.getStatus() == status){
                return item;
            }
        }
        return null;
    }
}
