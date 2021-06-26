# DistributedHealthWatchDemo
简介
• 此Demo展示如何在手表上调用相应接口，获取心率和步数等健康数据，配合分布式健康手机端应用，可以实现数据跨设备浏览以及心率异常

安装要求
• 安装DevEco Studio
• 设置DevEco Studio开发环境。DevEco Studio开发环境需要连接到网络，以确保该正常使用。可以根据以下两种情况配置开发环境：
	1.如果您可以直接访问Internet，则只需下载HarmonyOS SDK
	2.如果网络无法直接访问Internet，则可以通过代理服务器进行访问
• 生成密钥并申请证书

用户指南
• 下载此项目
• 打开HUAWEI DevEco Studio，单击File> Open选择此项目
• 单击Build> Build App(s)/Hap(s)>Build Debug Hap(s)以编译hap软件包
• 单击Run> Run 'entry'以运行hap包

注意
• 您可以选择在模拟器或真机上运行hap软件包。
• 如果在真机上运行它，则需要在项目的File> Project Structure> Modules> Signing Configs中配置签名和证书信息。

许可
请参阅LICENSE文件以获得更多信息。

What is it?
This demo shows how to call an API on your watch to obtain health data such as heart rate and step count. Working with a distributed health mobile app, you can browse data across devices and notify you of abnormal heart rates.

Installation requirements
• Install DevEco Studio
• Set up the DevEco Studio development environment.The DevEco Studio development environment needs to depend on the network environment. It needs to be connected to the network to ensure the normal use of the tool.The development environment can be configured according to the following two situations
	1.If you can directly access the Internet, just download the HarmonyOS SDK
	2.If the network cannot access the Internet directly, it can be accessed through a proxy server
• Generate secret key and apply for certificate

User guide
• Download this Project
• Open HUAWEI DevEco Studio, click File> Open> Then select and open this Project
• Click Build> Build App(s)/Hap(s)>Build Debug Hap(s) to compile the hap package
• Click Run> Run 'entry' to run the hap package

Note
• You can choose to run the hap package on the simulator or the phone.
• If you run it on the phone, you need to configure the signature and certificate information in the project's File> Project Structure> Modules> Signing Configs.

Licensing
Please see LICENSE for more info.