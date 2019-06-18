package com.github.attemper.common.enums;

public enum JobStatus {

    ENABLED(0),

    DISABLED(1),

    ;

    private int status;

    JobStatus(int status) {
        this.status = status;
    }

    public int getStatus(){
        return status;
    }

    public static JobStatus get(int status) {
        for (JobStatus item : JobStatus.values()) {
            if(item.getStatus() == status){
                return item;
            }
        }
        return null;
    }
}
