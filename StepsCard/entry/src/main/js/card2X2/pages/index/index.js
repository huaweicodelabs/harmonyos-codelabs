export default {
    data: {
        // 进度条百分比
        percent: 0,
        // 步数
        steps: 0
    },
    actions: {
        // 跳转路由
        routerEvent: {
            action: "router",
            bundleName: "com.huawei.cookbook",
            abilityName: "com.huawei.cookbook.MainAbility",
            params: {
                message:"steps"
            }
        }
    },
}

