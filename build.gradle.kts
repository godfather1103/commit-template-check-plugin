plugins {
    id("org.jetbrains.intellij") version "1.13.3"
}

group = "${property("pluginGroup")}"
version = "${property("pluginVersion")}"

repositories {
    mavenLocal()
    maven { url = uri("https://maven.aliyun.com/nexus/content/groups/public") }
    mavenCentral()
}

java.sourceCompatibility = JavaVersion.VERSION_11
java.targetCompatibility = JavaVersion.VERSION_11

tasks.compileJava {
    options.encoding = "UTF-8"
}

dependencies {
    // https://mvnrepository.com/artifact/com.squareup.okhttp3/okhttp
    implementation("com.squareup.okhttp3:okhttp:3.12.0")
    // https://mvnrepository.com/artifact/com.google.code.gson/gson
    implementation("com.google.code.gson:gson:2.8.6")
    implementation("io.vavr:vavr:0.10.4")
    testImplementation("junit:junit:4.12")
}
val ideaVersion = System.getProperty(
    "ideaVersion",
    property("ideaVersion") as String
)
val ideaType = System.getProperty(
    "ideaType",
    property("ideaType") as String
)
val publishChannel = project.findProperty("publishChannel") as String
val first = ideaVersion.split("[.-]".toRegex()).first().toInt()
val yearVersion = first.let { if (it > 2000) it % 100 else it / 10 }
val noVersion = if (first < 2000) first % 10 else ideaVersion
    .substring(ideaVersion.indexOf(".") + 1)
    .toInt()
intellij {
    version.set(ideaVersion)
    type.set(ideaType)
    updateSinceUntilBuild.set(false)
    pluginName.set("${property("pluginName")}")
    sandboxDir.set("idea-sandbox/${ideaVersion}")
}
tasks {
    patchPluginXml {
        sinceBuild.set("${yearVersion}${noVersion}.0")
        pluginId.set("commit-template-check-plugin")
        pluginDescription.set(
            """
<h2>English Readme：</h2>
<p>Create a commit message with the following template,It also provides the operation of checking the format of commit:</p>
<pre>
&lt;type&gt;(&lt;scope&gt;): &lt;subject&gt;
&lt;BLANK LINE&gt;
&lt;body&gt;
&lt;BLANK LINE&gt;
&lt;footer&gt;
</pre>
<p>Starting from version 1.7.7, it supports adding JIRA information to scope.</p>
<p>The plug-in is based on <a href="https://plugins.jetbrains.com/plugin/9861-git-commit-template">Git Commit Template</a></p>

<h2>中文说明：</h2>
<p>该插件可以按照如下模板去生成commit的内容，并提供了检测commit的格式的操作:</p>
<pre>
&lt;type&gt;(&lt;scope&gt;): &lt;subject&gt;
&lt;BLANK LINE&gt;
&lt;body&gt;
&lt;BLANK LINE&gt;
&lt;footer&gt;
</pre>
<p>从1.7.7版本开始支持添加jira信息到scope中。</p>
<p>该插件是在<a href="https://plugins.jetbrains.com/plugin/9861-git-commit-template">Git Commit Template</a>的基础上开发完成</p>

<h2>捐赠(Donate)</h2>
<pre>
你的馈赠将助力我更好的去贡献，谢谢！
Your gift will help me to contribute better, thank you!

<a href="https://paypal.me/godfather1103?locale.x=zh_XC">PayPal</a>

支付宝(Alipay)
<img src="https://plugins.jetbrains.com/files/17512/screenshot_434da076-114d-435c-a832-a86584ff29db" alt="支付宝" width="200" height="300" align="bottom" />
<img src="https://plugins.jetbrains.com/files/17512/screenshot_96f642c8-e168-4141-a29c-78166dc61ac5" alt="支付宝" width="200" height="300" align="bottom" />

微信(WeChat)
<img src="https://plugins.jetbrains.com/files/17512/screenshot_da480b29-acfa-47dd-b9fe-d8c9ce1f624b" alt="微信支付" width="300" height="320" align="bottom" />
</pre>
        """.trimIndent()
        )
        changeNotes.set(
            """
        <ul>
        1.8.8
        <li>添加logo</li>
        <li>add logo</li>
        <li>移除过时的方法调用</li>
        <li>remove Deprecated method</li>
        </ul>
        """.trimIndent()
        )
    }

    publishPlugin {
        project.findProperty("ORG_GRADLE_PROJECT_intellijPublishToken")?.let {
            token.set(it as String)
        }
        if (publishChannel.isNotEmpty()) {
            channels.set(listOf(publishChannel))
        } else if (ideaVersion.contains("EAP-SNAPSHOT")) {
            channels.set(listOf("beta"))
        }
    }

    signPlugin {
        project.findProperty("signing.certificateChainFile")?.let {
            certificateChainFile.set(file(it as String))
        }
        project.findProperty("signing.privateKeyFile")?.let {
            privateKeyFile.set(file(it as String))
        }
        project.findProperty("signing.password")?.let {
            password.set(it as String)
        }
    }

    initializeIntelliJPlugin {
        offline.set(true)
    }

    downloadZipSigner {
        cliPath.set("${project.projectDir.absolutePath}/tools/marketplace-zip-signer-cli.jar")
    }
}