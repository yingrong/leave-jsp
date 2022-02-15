
function LiabilityElementRow(liability) {
    this.checkBox = document.getElementById("liability_" + liability);
    this.amountInput = document.getElementById("liability_" + liability + "_amount");
}

LiabilityElementRow.prototype.isExist = function () {
    return this.checkBox && this.amountInput;
}

LiabilityElementRow.prototype.isChecked = function () {
    return this.isExist() && this.checkBox.checked;
}

LiabilityElementRow.prototype.setChecked = function (isChecked) {
    if(this.isExist()) {
        this.checkBox.checked = isChecked;
    }
}

LiabilityElementRow.prototype.clearAmount = function () {
    if(this.isExist()) {
        this.amountInput.value = "";
    }
}
