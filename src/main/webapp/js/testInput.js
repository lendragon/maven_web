
function testDisplay(reg, id, info) {
    if (!reg.test($(id).val())) {
        spanToggle(id, false, info)
        return false;
    }
    spanToggle(id, true, "✅")
    return true;
}

function spanToggle(id, flag, info) {
    $(id + " + span").removeClass();
    if (!flag) {
        $(id).css("border-color", "red")
        $(id + " + span").addClass("err");
    } else {
        $(id).css("border-color", "gray")
        $(id + " + span").addClass("right");
    }
    
    /*$(id + " + span").toggleClass("right");*/
    $(id + " + span").text(info);
}
