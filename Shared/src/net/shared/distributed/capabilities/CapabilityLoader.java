package net.shared.distributed.capabilities;

import net.shared.distributed.api.DistributedPlugin;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Iterator;
import java.util.Objects;
import java.util.ServiceLoader;

public class CapabilityLoader {

    public final String externalDirectory;

    public CapabilityLoader() {
        this("plugins");
    }

    public CapabilityLoader(String externalDirectory) {
        this.externalDirectory = externalDirectory;
        try {
            Load();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    protected void Load() throws MalformedURLException {
        File file = new File(externalDirectory);

        if(!file.exists()) {
            file.mkdirs();
            return;
        }

        File[] files = Objects.requireNonNull(file.listFiles(this::IsJar));

        URL[] urls = new URL[files.length];
        for (int i = 0; i < files.length; i++)
            urls[i] = files[i].toURI().toURL();

        URLClassLoader ucl = new URLClassLoader(urls);
        ServiceLoader<DistributedPlugin> sl = ServiceLoader.load(DistributedPlugin.class, ucl);
        Iterator<DistributedPlugin> iterator = sl.iterator();
        iterator.forEachRemaining(this::LoadPlugin);
    }

    protected void LoadPlugin(DistributedPlugin plugin) {
        System.out.printf("%s loaded.\n", plugin.Name());
    }

    protected boolean IsJar(File file) {
        return file.getPath().toLowerCase().endsWith(".jar");
    }

}
