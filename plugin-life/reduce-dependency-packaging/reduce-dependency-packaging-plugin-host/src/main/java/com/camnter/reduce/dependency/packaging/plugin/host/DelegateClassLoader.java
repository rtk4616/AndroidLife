package com.camnter.reduce.dependency.packaging.plugin.host;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * @author CaMnter
 */

public class DelegateClassLoader extends ClassLoader {

    private static final String TAG = DelegateClassLoader.class.getSimpleName();

    private final ClassLoader originalClassLoader;
    private final Map<String, PluginClassloader> bundleClassloaderMap;


    public DelegateClassLoader(@NonNull final Context context,
                               @NonNull final ClassLoader pathClassLoader) {
        super(pathClassLoader.getParent());
        this.originalClassLoader = pathClassLoader;
        this.bundleClassloaderMap = new HashMap<>();
        this.loadDex(context, "reduce-dependency-packaging-one.apk");
        this.loadDex(context, "reduce-dependency-packaging-two.apk");
        this.loadDex(context, "reduce-dependency-packaging-three.apk");
    }


    private void loadDex(@NonNull final Context context, @NonNull final String fullName) {
        try {
            String dir = null;
            final File cacheDir = context.getExternalCacheDir();
            final File filesDir = context.getExternalFilesDir("");
            if (cacheDir != null) {
                dir = cacheDir.getAbsolutePath();
            } else {
                if (filesDir != null) {
                    dir = filesDir.getAbsolutePath();
                }
            }
            if (TextUtils.isEmpty(dir)) return;

            // assets 的 reduce-dependency-packaging-one.apk 拷贝到 /storage/sdcard0/Android/data/[package name]/cache
            // 或者  /storage/sdcard0/Android/data/[package name]/files
            // assets 的 reduce-dependency-packaging-two.apk 拷贝到 /storage/sdcard0/Android/data/[package name]/cache
            // 或者  /storage/sdcard0/Android/data/[package name]/files
            // assets 的 reduce-dependency-packaging-three.apk 拷贝到 /storage/sdcard0/Android/data/[package name]/cache
            // 或者  /storage/sdcard0/Android/data/[package name]/files
            final File dexPath = new File(dir + File.separator + fullName);
            AssetsUtils.copyAssets(context, fullName, dexPath.getAbsolutePath());

            // /data/data/[package name]/app_reduce-dependency-packaging-one
            // /data/data/[package name]/app_reduce-dependency-packaging-two
            // /data/data/[package name]/app_reduce-dependency-packaging-three
            final String name = fullName.substring(0, fullName.lastIndexOf("."));
            final File optimizedDirectory = context.getDir(name, Context.MODE_PRIVATE);

            final PluginClassloader pluginClassloader = new PluginClassloader(
                dexPath.getAbsolutePath(),
                optimizedDirectory.getAbsolutePath(),
                null,
                this.originalClassLoader
            );
            this.bundleClassloaderMap.put(name, pluginClassloader);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Finds the class with the specified <a href="#name">binary name</a>.
     * This method should be overridden by class loader implementations that
     * follow the delegation model for loading classes, and will be invoked by
     * the {@link #loadClass <tt>loadClass</tt>} method after checking the
     * parent class loader for the requested class.  The default implementation
     * throws a <tt>ClassNotFoundException</tt>.
     *
     * @param name The <a href="#name">binary name</a> of the class
     * @return The resulting <tt>Class</tt> object
     * @throws ClassNotFoundException If the class could not be found
     * @since 1.2
     */
    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        Class<?> clazz = null;
        for (Map.Entry<String, PluginClassloader> entry : this.bundleClassloaderMap.entrySet()) {
            final String apkName = entry.getKey();
            final PluginClassloader pluginClassloader = entry.getValue();
            try {
                clazz = pluginClassloader.findClass(name);
            } catch (ClassNotFoundException e) {
                Log.e(TAG,
                    TAG + "   [findClass]   [apkName] = " + apkName +
                        "， can't find " + name);
                e.printStackTrace();
            }
            if (clazz != null) break;
        }
        return clazz != null ? clazz : super.findClass(name);
    }

}
