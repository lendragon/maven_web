
$(() => { // 绑定监听事件
    $("#account").blur(accountCheck);
    $("#password").blur(passwordCheck);
    $("form").submit(submitCheck);
    $("#verify").click(() => {
        // 点击切换验证码
        $("#verify").attr("src", "verify/getVerify?time=" + Date());
    })
});

function accountCheck() {  // 校验账号
    let reg = /[0-9A-Za-z]{4,16}/;
    return testDisplay(reg, "#account", "账号应为4到16位的数字或字母");
}

function passwordCheck() {  // 校验密码
    let reg = /\w{4,16}/;
    return testDisplay(reg, "#password", "密码应为4到16位,且至少包含数字、字母");
}


function submitCheck() {  // 提交校验
    // 校验表单项
    if (!(accountCheck() && passwordCheck())) {
        return false;
    }

    $.post("user/login", $("form").serialize(), (res) => {
        alert(res["info"]);
        if (res["result"]) {
            // 成功, 则跳转
            location.href = "index.html";
        } else {
            // 切换验证码
            $("#verify").attr("src", "verify/getVerify?time=" + Date());
        }

    }, "json")

    return false;
}
