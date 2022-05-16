package id.standherealone.yuubaca.core.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

import id.standherealone.yuubaca.R
import id.standherealone.yuubaca.model.Buku

class BukuAdapter (val context: Context) : RecyclerView.Adapter<BukuAdapter.MyViewHolder>() {

    var bukuList : List<Buku> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.items_buku,parent,false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return bukuList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.titleBook.text = bukuList.get(position).judul
        Glide.with(context).load(bukuList.get(position).gambar)
            .apply(RequestOptions.centerCropTransform()).into(holder.image)

        val buku: Buku = bukuList.get(position)

        val images: String = buku.gambar
        val judul: String = buku.judul
        val penulis: String = buku.author
        val isi: String = buku.deskripsi
        val file: String = buku.file

        // Bind
        holder.image.setImageURI((Uri.parse(images)))
        holder.titleBook.text = judul
        holder.authorBook.text = penulis
        holder.descBook.text = isi
        holder.fileBook.text = file
    }

    fun setBukuListItems(bukuList: List<Buku>){
        this.bukuList = bukuList;
        notifyDataSetChanged()
    }

    class MyViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {

        val image: ImageView = itemView!!.findViewById(R.id.thumbnail)
        val titleBook: TextView = itemView!!.findViewById(R.id.title)
        val authorBook: TextView = itemView!!.findViewById(R.id.author)
        val descBook: TextView = itemView!!.findViewById(R.id.descbook)
        val fileBook: TextView = itemView!!.findViewById(R.id.filebook)

    }
}