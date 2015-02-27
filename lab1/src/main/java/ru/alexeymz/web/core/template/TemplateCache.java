package ru.alexeymz.web.core.template;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public final class TemplateCache {
    private final ClassLoader resourceLoader;
    private final boolean isCacheEnabled;
    private final Map<String, ViewTemplate> cache = new HashMap<>();

    public TemplateCache(ClassLoader resourceLoader, boolean enableCache) {
        this.resourceLoader = resourceLoader;
        this.isCacheEnabled = enableCache;
    }

    public ViewTemplate get(String name) throws IOException {
        ViewTemplate template = cache.get(name);
        if (template == null) {
            template = ViewTemplate.fromResource(resourceLoader, name);
            if (isCacheEnabled) {
                cache.put(name, template);
            }
        }
        return template;
    }
}
