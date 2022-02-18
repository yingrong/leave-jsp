function ActivityInfoRow(activityId) {
    this.checkBox = document.getElementById("activity_"+ activityId);
    this.countInput = document.getElementById("activity_"+ activityId +"_count");
}

ActivityInfoRow.prototype.isExist = function () {
    return this.checkBox && this.countInput;
}

ActivityInfoRow.prototype.isChecked = function () {
    return this.isExist() && this.checkBox.checked;
}

ActivityInfoRow.prototype.setChecked = function (isChecked) {
    if (this.isExist()) {
        this.checkBox.checked = isChecked;
    }
}

ActivityInfoRow.prototype.clearCount = function () {
    if (this.isExist()) {
        this.countInput.value = "";
    }
}
