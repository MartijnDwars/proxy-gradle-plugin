package nl.martijndwars.proxy;

import org.gradle.api.Plugin;
import org.gradle.api.invocation.Gradle;

import java.net.ProxySelector;

public class ProxyGradlePluginPlugin implements Plugin<Gradle> {
    public void apply(Gradle gradle) {
        ProxySelector proxySelector = ProxySelector.getDefault();
        ProxySelector.setDefault(new EnvProxySelector(proxySelector));
    }
}
