package com.github.attemper.executor.util;

import java.net.URL;
import java.net.URLClassLoader;

public class CustomURLClassLoader extends URLClassLoader {

    public CustomURLClassLoader(URL[] urls) {
        super(urls);
    }

    public void uploadURL(URL url) {
        super.addURL(url);
    }
}
