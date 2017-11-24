package com.cncoderx.github.glide;

import android.content.Context;

import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;
import com.bumptech.glide.module.AppGlideModule;
import com.cncoderx.github.utils.Constants;

/**
 * @author cncoderx
 */
@GlideModule
public class DefaultGlideModule extends AppGlideModule {

    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        builder.setDiskCache(new InternalCacheDiskCacheFactory(
                context, Constants.IMAGE_CACHE_DIR, Constants.IMAGE_CACHE_SIZE));
    }

    @Override
    public boolean isManifestParsingEnabled() {
        return false;
    }
}
