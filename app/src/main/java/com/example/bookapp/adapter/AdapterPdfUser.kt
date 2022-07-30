package com.example.bookapp.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.example.bookapp.filters.FilterPdfUser
import com.example.bookapp.MyApplication
import com.example.bookapp.activities.PdfDetailActivity
import com.example.bookapp.databinding.RowPdfUserBinding
import com.example.bookapp.models.ModelPdf

class AdapterPdfUser: RecyclerView.Adapter<AdapterPdfUser.HolderPdfUser>, Filterable{
    //arraylist to hold filtered pdf
    public var  filterList: ArrayList<ModelPdf>

    //context, get using constructor
    private var context: Context

    //arraylist to hold pdfs, get using constructor
    public var pdfArrayList: ArrayList<ModelPdf>

    //View binding
    private lateinit var binding: RowPdfUserBinding

    //
    private var filter: FilterPdfUser? = null

    //
    constructor(context: Context, pdfArrayList: ArrayList<ModelPdf>) {
        this.context = context
        this.pdfArrayList = pdfArrayList
        this.filterList = pdfArrayList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderPdfUser {
        //inflater/bind layout row
        binding = RowPdfUserBinding.inflate(LayoutInflater.from(context), parent, false)
        return HolderPdfUser(binding.root)

    }

    override fun onBindViewHolder(holder: HolderPdfUser, position: Int) {
        //get data, set data, handle click...
        //get data
        val model = pdfArrayList[position]
        val bookId = model.id
        val categoryId = model.categoryId
        val title = model.title
        val description = model.description
        val uid = model.uid
        val url = model.url
        val timestamp = model.timestamp

        //convert timestamp to dd//MM//yyy format
        val date = MyApplication.formatTimeStamp(timestamp)

        //set data
        holder.titleTv.text = title
        holder.descriptionTv.text = description
        holder.dateTv.text = date

        // pass null for page number/ load pdf thumbnail
        MyApplication.loadPdfFromUrlSinglePage(url, title, holder.pdfView, holder.progressBar, null)

        MyApplication.loadCategory(categoryId, holder.categoryTv)

        MyApplication.loadPdfSize(url, title, holder.sizeTv)

        //handle click, open pdf details page
        holder.itemView.setOnClickListener {
            //pass book id in intent, that will be used to get pdf info
            val intent = Intent(context, PdfDetailActivity::class.java)
            intent.putExtra("bookId", bookId)
            context.startActivity(intent)

        }


    }

    override fun getItemCount(): Int {
        return pdfArrayList.size //return list size, number of records
    }

    override fun getFilter(): Filter {
        if (filter == null) {
            filter = FilterPdfUser(filterList, this)
        }
        return filter as FilterPdfUser
    }

    /*ViewHolder class row*/
    inner class HolderPdfUser(itemView: View): RecyclerView.ViewHolder(itemView){
        //init Ui components of row...
        val pdfView = binding.pdfView
        val progressBar = binding.progressBar
        val titleTv = binding.titleTv
        val descriptionTv = binding.descriptionTv
        val categoryTv = binding.categoryTv
        val sizeTv = binding.sizeTv
        val dateTv = binding.dateTv


    }

}