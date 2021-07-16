# RemoteInputDemo
简介
• 此Demo用于展示跨设备的输入能力。您可以方便地通过手机输入文字到电视搜索栏，然后找到并选择你想要的电影。为了方便测试，我们在这个示例中使用手机来模拟电视端。

安装要求
• 安装DevEco Studio
• 设置DevEco Studio开发环境。DevEco Studio开发环境需要连接到网络，以确保该正常使用。可以根据以下两种情况配置开发环境：
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

## What is it?
The RemoteInputDemo application is to show cross-device inputmethod capability.You can expediently use phone to type words to TV search bar,then find and select the movie that you want to see.For convenience of your test,We use phone to simulate TV side in this sample.

## Installation requirements
• Install DevEco Studio
• Set up the DevEco Studio development environment.The DevEco Studio development environment needs to depend on the network environment. It needs to be connected to the network to ensure the normal use of the tool.The development environment can be configured according to the following two situations
	1.If you can directly access the Internet, just download the HarmonyOS SDK
	2.If the network cannot access the Internet directly, it can be accessed through a proxy server
• Generate secret key and apply for certificate

## User guide
• Download this Project
• Open HUAWEI DevEco Studio, click File> Open> Then select and open this Project
• Click Build> Build App(s)/Hap(s)>Build Debug Hap(s) to compile the hap package
• Click Run> Run 'entry' to run the hap package

## Note
• You can choose to run the hap package on the simulator or the phone.
• If you run it on the phone, you need to configure the signature and certificate information in the project's File> Project Structure> Modules> Signing Configs.


## Device Detection
For the HarmonyOS devices (TV, Phone, tablet, etc..) to find each other:
1. The Bluetooth on both devices should be enabled
2. They need to be [logged to the same Huawei ID](https://consumer.huawei.com/en/support/content/en-us00770240/)



## The Result
These are the screens of a phone and a TV (simulated on a tablet).
The device detection is called when the Text input field is focused and shows a dialog with nearby devices.

![](https://im4.ezgif.com/tmp/ezgif-4-a8350f47888c.gif)


![](https://im4.ezgif.com/tmp/ezgif-4-b94ec1483628.gif)





---

Licensing
Please see LICENSE for more info.
