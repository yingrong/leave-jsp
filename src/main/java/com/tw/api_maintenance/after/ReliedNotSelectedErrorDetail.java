package com.tw.api_maintenance.after;

public class ReliedNotSelectedErrorDetail extends ErrorDetail {

    private final Long packageItemId;
    private final Long packageId;
    private final String packageName;
    private final Long currentActivityItemId;
    private final Long currentActivityId;
    private final String currentActivityName;
    private final Long reliedActivityId;
    private final String reliedActivityName;

    public Long getPackageItemId() {
        return packageItemId;
    }

    public Long getPackageId() {
        return packageId;
    }

    public String getPackageName() {
        return packageName;
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

    public Long getReliedActivityId() {
        return reliedActivityId;
    }

    public String getReliedActivityName() {
        return reliedActivityName;
    }

    ReliedNotSelectedErrorDetail(Long packageItemId, Long packageId, String packageName,
                                 Long currentActivityItemId, Long currentActivityId, String currentActivityName,
                                 Long reliedActivityId, String reliedActivityName){


        this.packageItemId = packageItemId;
        this.packageId = packageId;
        this.packageName = packageName;
        this.currentActivityItemId = currentActivityItemId;
        this.currentActivityId = currentActivityId;
        this.currentActivityName = currentActivityName;
        this.reliedActivityId = reliedActivityId;
        this.reliedActivityName = reliedActivityName;
    }
}
