package id.standherealone.yuubaca.core.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import id.standherealone.yuubaca.R
import id.standherealone.yuubaca.core.PicassoClient
import id.standherealone.yuubaca.model.Buku
import id.standherealone.yuubaca.ui.detail.DetailActivity

class BukuAdapter(var context: Context) : RecyclerView.Adapter<BukuAdapter.MyViewHolder>() {

    var bukuList: List<Buku> = listOf()
    var bukuListFiltered: List<Buku> = listOf()

    fun setBukuList(context: Context, bukuList: List<Buku>) {
        this.context = context!!
        if (bukuList == null) {
            this.bukuList = bukuList
            this.bukuListFiltered = bukuList
            notifyItemChanged(0, bukuListFiltered.size)
        } else {
            val result = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
                override fun getOldListSize(): Int {
                    return this@BukuAdapter.bukuList.size
                }

                override fun getNewListSize(): Int {
                    return bukuList.size
                }

                override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                    return this@BukuAdapter.bukuList.get(oldItemPosition)
                        .judul === bukuList[newItemPosition].judul
                }

                override fun areContentsTheSame(
                    oldItemPosition: Int,
                    newItemPosition: Int
                ): Boolean {
                    val newBook: Buku = this@BukuAdapter.bukuList.get(oldItemPosition)
                    val oldBook: Buku = bukuList[newItemPosition]
                    return newBook.judul === oldBook.judul
                }
            })
            this.bukuList = bukuList
            this.bukuListFiltered = bukuList
            result.dispatchUpdatesTo(this)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.items_buku, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: BukuAdapter.MyViewHolder, position: Int) {
        holder.titleBook!!.text = bukuListFiltered.get(position).judul
        Glide.with(context).load(bukuListFiltered.get(position).gambar)
            .apply(RequestOptions.centerCropTransform()).into(holder.image!!)

        val buku: Buku = bukuListFiltered.get(position)

        val images: String = buku.gambar
        val judul: String = buku.judul
        val penulis: String = buku.author
        val isi: String = buku.deskripsi
        val file: String = buku.file

        // Bind
        holder.image!!.setImageURI((Uri.parse(images)))
        holder.titleBook!!.text = judul
        holder.authorBook!!.text = penulis
        holder.descBook!!.text = isi
        holder.fileBook!!.text = file

        // Library picasso for handling cache image & when data cant load
        PicassoClient.downloadImage(context, bukuListFiltered.get(position).gambar, holder.image)

        holder.setItemClickListener(object : ItemClickListener {
            override fun onItemClick(pos: Int) {
                openDetailActivity(images, judul, penulis, isi, file)
            }
        })
    }

    override fun getItemCount(): Int {
        return if (bukuList != null) {
            bukuListFiltered.size
        } else {
            0
        }
    }

    // open activity
    private fun openDetailActivity(vararg details: String) {
        val i = Intent(context, DetailActivity::class.java)
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        i.putExtra("images", details[0])
        i.putExtra("title", details[1])
        i.putExtra("author", details[2])
        i.putExtra("sipnosis", details[3])
        i.putExtra("file", details[4])
        context.startActivity(i)
    }

    fun getFilter(): Filter? {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                val charString = charSequence.toString()
                if (charString.isEmpty()) {
                    bukuListFiltered = bukuList
                } else {
                    val filteredList: MutableList<Buku> = ArrayList<Buku>()
                    for (books in bukuList) {
                        if (books.judul.lowercase().contains(charString.lowercase())) {
                            filteredList.add(books)
                        }
                    }
                    bukuListFiltered = filteredList
                }
                val filterResults = FilterResults()
                filterResults.values = bukuListFiltered
                return filterResults
            }

            override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
                bukuListFiltered = filterResults.values as ArrayList<Buku>
                notifyDataSetChanged()
            }
        }
    }

    fun setBukuListItems(bukuList: List<Buku>) {
        this.bukuList = bukuList;
        notifyDataSetChanged()
    }

    interface ItemClickListener {
        fun onItemClick(pos: Int)
    }

    inner class MyViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!),
        View.OnClickListener {

        private lateinit var itemClickListener: ItemClickListener

        var image: ImageView? = null
        var titleBook: TextView? = null
        var authorBook: TextView? = null
        var descBook: TextView? = null
        var fileBook: TextView? = null

        init {
            image = itemView!!.findViewById<View>(R.id.thumbnail) as ImageView
            titleBook = itemView.findViewById<View>(R.id.title) as TextView
            authorBook = itemView.findViewById<View>(R.id.author) as TextView
            descBook = itemView.findViewById<View>(R.id.descbook) as TextView
            fileBook = itemView.findViewById<View>(R.id.filebook) as TextView
            itemView.setOnClickListener(this)
        }

        override fun onClick(view: View) {
            this.itemClickListener.onItemClick(this.layoutPosition)
        }

        fun setItemClickListener(itemClickListener: ItemClickListener) {
            this.itemClickListener = itemClickListener
        }
    }
}