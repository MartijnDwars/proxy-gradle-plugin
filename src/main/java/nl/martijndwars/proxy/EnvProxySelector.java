package nl.martijndwars.proxy;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.ProxySelector;
import java.net.SocketAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class EnvProxySelector extends ProxySelector {
  private final ProxySelector defaultProxySelector;
  private final String httpProxy;
  private final String httpsProxy;
  private final String noProxy;

  public EnvProxySelector(ProxySelector defaultProxySelector) {
    this.defaultProxySelector = defaultProxySelector;
    this.httpProxy = getEnvInsensitive("http_proxy");
    this.httpsProxy = getEnvInsensitive("https_proxy");
    this.noProxy = getEnvInsensitive("no_proxy");

    System.out.println("httpProxy = " + httpProxy);
    System.out.println("httpsProxy = " + httpsProxy);
    System.out.println("noProxy = " + httpProxy);
  }

  private static String getEnvInsensitive(String name) {
    Map<String, String> environment = System.getenv();
    if (environment.containsKey(name.toLowerCase())) {
      return environment.get(name.toLowerCase());
    }
    if (environment.containsKey(name.toUpperCase())) {
      return environment.get(name.toUpperCase());
    }
    return null;
  }

  @Override
  public List<Proxy> select(URI uri) {
    if (isHttp(uri) && httpProxy == null) {
      return Collections.singletonList(Proxy.NO_PROXY);
    }
    if (isHttps(uri) && httpsProxy == null) {
      return Collections.singletonList(Proxy.NO_PROXY);
    }
    if (isNoProxyMatch(uri)) {
      return Collections.singletonList(Proxy.NO_PROXY);
    }
    if (isHttp(uri) && httpProxy != null) {
      return Collections.singletonList(createProxy(this.httpProxy));
    }
    if (isHttps(uri) && httpsProxy != null) {
      return Collections.singletonList(createProxy(this.httpsProxy));
    }
    return Collections.singletonList(Proxy.NO_PROXY);
  }

  private boolean isNoProxyMatch(URI uri) {
    if (noProxy == null) {
      return false;
    }
    String[] hosts = noProxy.split(",");
    for (String host : hosts) {
      if (host.equals(uri.getHost())) {
        return true;
      }
    }
    return false;
  }

  private static Proxy createProxy(String httpsProxy) {
    try {
      URI uri = new URI(httpsProxy);
      return new Proxy(Proxy.Type.HTTP, new InetSocketAddress(uri.getHost(), uri.getPort()));
    } catch (URISyntaxException e) {
      throw new RuntimeException("Cannot parse proxy URL.", e);
    }
  }

  private static boolean isHttp(URI uri) {
    return "http".equals(uri.getScheme());
  }

  private static boolean isHttps(URI uri) {
    return "https".equals(uri.getScheme());
  }

  @Override
  public void connectFailed(URI uri, SocketAddress socketAddress, IOException exception) {
    if (uri == null || socketAddress == null || exception == null) {
      throw new IllegalArgumentException("One of the arguments is null.");
    }
  }
}
