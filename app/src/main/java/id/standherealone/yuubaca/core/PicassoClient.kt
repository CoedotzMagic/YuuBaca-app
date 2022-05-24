package id.standherealone.yuubaca.core

import android.content.Context
import android.widget.ImageView
import com.squareup.picasso.Picasso
import id.standherealone.yuubaca.R

object PicassoClient {
    fun downloadImage(c: Context?, url: String?, img: ImageView?) {
        if (url != null && url.isNotEmpty()) {
            Picasso.get()
                .load(url).placeholder(R.mipmap.ic_launcher)
                .resize(500, 800)
                .into(img)
        } else {
            Picasso.get().load(R.mipmap.ic_launcher)
                .resize(500, 800)
                .into(img)
        }
    }
}