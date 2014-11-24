package resources;

import java.net.URL;
import java.util.HashMap;

public abstract class ResourceCache {
    protected HashMap resources;
    
    public ResourceCache() {
        resources = new HashMap();
    }
    
    protected Object loadResources(String name) {
        URL url;
        url = getClass().getClassLoader().getResource(name);
        return loadResources(url);
    }
    
    protected Object getResource(String name) {
        Object res = resources.get(name);
        if (res == null) {
            res = loadResources("res/" + name);
            resources.put(name, res);
        }
        return res;
    }
    
    protected abstract Object loadResources(URL url);

}
