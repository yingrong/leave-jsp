var packageConfig = {
    10010: "十万用户团建礼包",
    10086: "百万用户团建礼包"
};

var activityConfig = {
    1: "冬奥两日游",
    2: "户外探险一日游",
    3: "唱歌",
    4: "吃饭",
    5: "住宿",
    11: "自由三日飞"
};

var mutexActivityConfig = {
    10010: {
        1: 2,
        2: 1
    },
    10086: {
        1: 11,
        11: 1
    }
};

var reliedActivityConfig = {
    10010: {
        5: 1
    }
}

var relierActivityConfig = {
    10010: {
        1: 5
    },
    10086: {
        999: 12
    }
}
