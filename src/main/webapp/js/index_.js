// 获取景点信息
$(requestForSpot(1))

// 页面初始信息
$(() => {
    $("h2").text("您好, 请登录");
    $("#user").prop("href", "./login.html")
})

// 获取用户信息
$(() => {
    $.get("user/isLogin", (res) => {
        // 设置h2的内容
        if (res) {
            $("h2").text(res["username"] + ", 欢迎您!");
            $("#user").prop("href", "./userIndex.html")

            $("#regist").remove();
            $("#login").remove();
            $("#user").after("<a id='myCollect' href='javascript:;' onclick='myCollect()'>我的收藏</a>");
            $("#user").after("<a id='unLogin' href='javascript:;' onclick='unLogin()'>退出登录</a>");
        }
    }, "json")
});

// 向服务器发送get请求请求当前的景点数据
function requestForSpot(page) {
    $("#content").empty();
    
    $.get("spot/spot", page ? "currentPage=" + page : "", (res) => {
        $.each(res[0], (index, elem) => {
            let isCollect = false;
            let collectStr = "";
            let collectCount = 0;

            // 判断是否已经收藏
            $.each(res[2], (i, e) => {
                if (e == elem["sid"]) {
                    collectCount++;
                    isCollect = true;
                    collectStr = "<input id='sid" + elem["sid"] + "' onclick='unCollect(" + elem["sid"] + ")'" +
                        " class='collectBtn' type='button' value='已收藏'>";
                    return false;
                }
            });
            if (!isCollect) {
                collectStr = "<input id='sid" + elem["sid"] + "' onclick='collect(" + elem["sid"] + ")'" +
                    " class='collectBtn' type='button' value='收藏'>";
            }


            $("#content").append("<span class='" +
                (isCollect ? "" : "noCollect") +
                "'><div class='spot " +
                "'><a href='./spot.html?sid=" + elem["sid"] +"' >" +
                "<div class='spotImage'><image src='./image/1.jpg' alt='图片' height='100%'/></div>" +
                "<p>" + elem["name"] + "</p>" +
                "<p>" + elem["position"] + "</p>" +
                "<p>" +
                (elem["remarks"].length > 7 ?
                    (elem["remarks"].substring(0, 6) + "...") :
                    elem["remarks"]) +
                "</p></div></a><span class='collectOuter'>" +
                collectStr +
                "</span></span>");
        })

        let pageNode = createPageNode(res[1]["allRecord"], res[1]["onePageRecord"], res[1]["currentPage"]);

        $("#content").append(pageNode);

    }, "json");
}

// 创建分页组件
function createPageNode(allRecord, onePageRecord, currentPage) {
    let pageNode = "<nav aria-label=\"Page navigation\">" +
        "<ul class=\"pagination\" id='page'><li>";

    if (currentPage === 1) {
        pageNode += "<a href='javascript:;' aria-label=\"Previous\"> ";
    } else {
        pageNode += "<a href=\"#\" onclick='requestForSpot(" + (currentPage - 1) + ")' aria-label=\"Previous\"> ";
    }

    pageNode += "<span aria-hidden=\"true\">&laquo;</span></a></li>"

    for (let i = 1; i <= (allRecord / onePageRecord + 1); i++) {
        if (i === currentPage) {
            pageNode += "<li class='active'><a href='javascript:;'>" + i + "<span class=\"sr-only\">(current)</span></a></li>"
        } else {
            pageNode += "<li><a href='#' onclick= requestForSpot(" + i + ")>" + i + "</a></li>"
        }
    }

    pageNode += "<li>";

    if (currentPage === parseInt(allRecord / onePageRecord + 1)) {
        pageNode += "<a href='javascript:;' aria-label=\"Next\"> ";
    } else {
        pageNode += "<a href=\"#\" onclick='requestForSpot(" + (currentPage + 1) + ")' aria-label=\"Next\">";
    }

    pageNode += "<span aria-hidden=\"true\">&raquo;</span></a></li></ul></nav>";
    
    return pageNode;
}

// 收藏请求
function collect(sid) {
    $.post("user/collect", "sid=" + sid, (res) => {
        // 获取服务器返回的结果
        // 如果成功, 则将收藏变为已收藏
        if (res.result) {
            let id = "#sid" + sid;
            $(id).val("已收藏");
            $(id).attr("onclick", "unCollect(" + sid + ")");
        } else if (res["info"] === "请先登录") {
            location.href = "./login.html";
        }
    }, "json");
}

// 取消收藏
function unCollect(sid) {
    $.post("user/unCollect", "sid=" + sid, (res) => {
        // 获取服务器返回的结果
        // 如果成功, 则将收藏变为已收藏
        if (res.result) {
            let id = "#sid" + sid;
            $(id).val("收藏");
            $(id).attr("onclick", "collect(" + sid + ")");
        }
    }, "json");

}

function myCollect() {
    $(".noCollect").each((index, elem) => {
        $(elem).css("display", "none");
    })
    console.log($(".noCollect").length);
    $("#myCollect").text("首页");
    $("#myCollect").attr("onclick", "returnIndex()");
    $("nav").css("display", "none");
}

function returnIndex() {
    $(".noCollect").each((index, elem) => {
        $(elem).css("display", "inline-block");
    })

    $("#myCollect").text("我的收藏");
    $("#myCollect").attr("onclick", "myCollect()");
    $("nav").css("display", "block");
}

// 退出登录
function unLogin() {
    $.post("user/unLogin", (res) => {
        // 如果退出成功
        if (res.result) {
            console.log(res.info);
            // 返回登录界面
            location.href = "login.html";
        }
    });
}