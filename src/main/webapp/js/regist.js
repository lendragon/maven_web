
$(() => { // 绑定监听事件
    $("#username").blur(usernameCheck);
    $("#confirmPw").blur(confirmPwCheck);
    $("form").submit(submitCheck);
});

/* 4到16的字符串 */
function usernameCheck() {  // 校验用户名
    let reg = /.{4,16}/;
    return testDisplay(reg, "#username", "用户名长度应为4到16");

}

function confirmPwCheck() {  // 校验确认密码
    if ($("#confirmPw").val() !== $("#password").val()) {
        spanToggle("#confirmPw", false, "密码不一致");
        return false;
    }
    spanToggle("#confirmPw", true, "✅");
    return true;
}

function submitCheck() {  // 提交校验
    // 校验表单项
    if (!(usernameCheck() && accountCheck() && passwordCheck() && confirmPwCheck())) {
        return false;
    }

    let flag = false;
    // 使用ajax异步提交表单校验验证码和账号重复问题
    $.post("user/regist", $("form").serialize(), (res) => {
        if (res["result"]) {
            // 如果成功
            flag = true;
        }
        alert(res["info"]);
    }, "json")

    return flag;
}

