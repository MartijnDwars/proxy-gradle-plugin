# Proxy Gradle Plugin

Many corporate networks require outbound traffic to be routed through a proxy server.
Unix tools like `curl` and `wget` use the environment variables `http_proxy`, `https_proxy`, and `no_proxy` to control whether traffic is routed through the proxy.
The JVM, however, implements its own [networking and proxy](https://docs.oracle.com/javase/8/docs/technotes/guides/net/proxies.html) mechanism that relies on Java system properties.
System properties are awkward to use, because you cannot easily set/unset them globally.
With this plugin Gradle will behave similar to `curl` and `wget`, i.e. it will use the proxy based on `http_proxy`, `https_proxy`, and `no_proxy` variables.

## Usage

To make the plugin part of your init script, add the following to `~/.gradle/init.gradle.kts`:

```
initscript {
  repositories {
    maven {
      url = uri("https://plugins.gradle.org/m2")
    }
  }
  dependencies {
    classpath("nl.martijndwars:proxy-gradle-plugin:1.0.1")
  }
}

apply<nl.martijndwars.proxy.ProxyGradlePluginPlugin>()
```

Set the proxy environment variable (e.g. `HTTP_PROXY=http://proxy.acme.com:80`) and run Gradle; Gradle now uses the proxy server.
If you leave the corporate network, unset the environment variable (e.g. `unset HTTP_PROXY`) and run Gradle; Gradle now no longer uses the proxy server.
Enjoy!
