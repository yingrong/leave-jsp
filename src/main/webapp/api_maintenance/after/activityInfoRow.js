function ActivityInfoRow(activityItemId) {
    this.checkBox = document.getElementById("activity_item_"+ activityItemId);
    this.countInput = document.getElementById("activity_item_"+ activityItemId +"_count");
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

ActivityInfoRow.prototype.isCountBetween = function (min, max) {
    if (this.isExist()) {
        var count = Number(this.countInput.value);
        return !isNaN(count) && count >= min && count <= max;
    }
}

ActivityInfoRow.prototype.getCount = function () {
    if(this.isExist()) {
        return Number(this.countInput.value);
    }
}
