package com.cncoderx.andhub.glide;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;
import com.bumptech.glide.module.AppGlideModule;
import com.cncoderx.andhub.R;
import com.cncoderx.andhub.utils.Constants;

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

    @BindingAdapter("imageUrl")
    public static void loadImage(ImageView view, String url) {
        GlideApp.with(view.getContext()).load(url).placeholder(R.mipmap.identicon).into(view);
    }
}
