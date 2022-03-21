package com.tw.api_maintenance.after.domain.error_handling;

public class MutexActivityErrorDetail extends ErrorDetail {

    private Long packageItemId;
    private Long packageId;
    private String packageName;
    private Long currentActivityItemId;
    private Long currentActivityId;
    private String currentActivityName;
    private Long mutexActivityId;
    private String mutexActivityName;

    public MutexActivityErrorDetail(Long packageItemId, Long packageId, String packageName,
                             Long currentActivityItemId, Long currentActivityId, String currentActivityName,
                             Long mutexActivityId, String mutexActivityName) {
        this.packageItemId = packageItemId;
        this.packageId = packageId;
        this.packageName = packageName;
        this.currentActivityItemId = currentActivityItemId;
        this.currentActivityId = currentActivityId;
        this.currentActivityName = currentActivityName;
        this.mutexActivityId = mutexActivityId;
        this.mutexActivityName = mutexActivityName;
    }

    public Long getPackageItemId() {
        return packageItemId;
    }

    public Long getPackageId() {
        return packageId;
    }

    public String getPackageName() {
        return packageName;
    }

    public Long getMutexActivityId() {
        return mutexActivityId;
    }

    public String getMutexActivityName() {
        return mutexActivityName;
    }

    public Long getCurrentActivityItemId() {
        return currentActivityItemId;
    }

    public Long getCurrentActivityId() {
        return currentActivityId;
    }

    public String getCurrentActivityName() {
        return currentActivityName;
    }
}
