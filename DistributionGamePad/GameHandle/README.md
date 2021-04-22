# GameHandle

简介
GameHandle是此项目的手机端，使用了HarmonyOS的分布式能力。
启动GameHandle后，它会模拟游戏手柄，您可以选择连接GameApplication端，连接完成后就可以在GameApplication中控制游戏。

安装要求 
• 安装DevEco Studio和Node.js 
• 设置DevEco Studio开发环境。 DevEco Studio开发环境需要连接到网络，以确保该正常使用。可以根据以下两种情况配置开发环境： 
    1.如果您可以直接访问Internet，则只需下载HarmonyOS SDK 
    2.如果网络无法直接访问Internet，则可以通过代理服务器进行访问 
• 生成密钥并申请证书

用户指南 
• 下载此项目 
• 打开HUAWEI DevEco Studio，单击File> Open选择此ComponentCodelab 
• 单击Build> Build App(s)/Hap(s)>Build Debug Hap(s)以编译hap软件包 
• 单击Run> Run 'entry'以运行hap包

注意 
• 您可以选择在模拟器或真机上运行hap软件包。 
• 如果在真机上运行它，则需要在项目的File> Project Structure> Modules> Signing Configs中配置签名和证书信息。

许可 
请参阅LICENSE文件以获得更多信息。