# ParallelHorizon
简介
• 此Demo主要利用平行视界实现应用内双窗口，将两个Ability左右显示在同一个应用的不同窗口。 左侧页面展示图片列表，可根据屏幕宽度自适应调节图片布局；右侧页面展示选择的图片，可对图片进行裁剪、缩放、镜像、保存、流转等操作。流转是将右侧窗口对应的Ability迁移到另一台设备，另一台设备也可图片进行裁剪、缩放、镜像之后将Ability迁移回流转设备。

安装要求
• 安装DevEco Studio
• 设置DevEco Studio开发环境。DevEco Studio开发环境需要连接到网络，以确保该正常使用。可以根据以下两种情况配置开发环境：
	1.如果您可以直接访问Internet，则只需下载HarmonyOS SDK
	2.如果网络无法直接访问Internet，则可以通过代理服务器进行访问
• 生成密钥并申请证书

用户指南
• 下载此项目
• 打开HUAWEI DevEco Studio，单击File> Open选择此Codelab
• 单击Build> Build App(s)/Hap(s)>Build Debug Hap(s)以编译hap软件包
• 单击Run> Run 'entry'以运行hap包

注意
• 您可以选择在模拟器或真机上运行hap软件包。
• 如果在真机上运行它，则需要在项目的File> Project Structure> Modules> Signing Configs中配置签名和证书信息。

许可
请参阅LICENSE文件以获得更多信息。

What is it?
This demo mainly uses parallel horizons to implement dual windows in an application, and displays two Abilities left and right in different windows of the same application. The image list is displayed on the left. You can adjust the image layout based on the screen width. The selected image is displayed on the right. You can crop, zoom, mirror, save, and transfer the image. Transfer: Migrate the Ability corresponding to the right pane to another device. The other device can also crop, zoom, and mirror images and then migrate the Ability back to the device.

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