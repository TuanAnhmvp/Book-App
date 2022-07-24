package com.example.bookapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.example.bookapp.databinding.RowPdfAdminBinding

class AdapterPdfAdmin : RecyclerView.Adapter<AdapterPdfAdmin.HolderPdfAdmin>, Filterable {


    //
    private val filerList : ArrayList<ModelPdf>
    //filter object
    private var filter : FilterPdfAdmin? = null

    //viewBinding
    private lateinit var binding: RowPdfAdminBinding

    //context
    private var context: Context

    //arrayllist to hold pdfs
    public var pdfArrayList: ArrayList<ModelPdf>

    //constructor
    constructor(context: Context, pdfArrayList: ArrayList<ModelPdf>) : super() {
        this.context = context
        this.pdfArrayList = pdfArrayList
        this.filerList = pdfArrayList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderPdfAdmin {
        //bind/inflate layout row_pdf
        binding = RowPdfAdminBinding.inflate(LayoutInflater.from(context), parent, false)
        return HolderPdfAdmin(binding.root)
    }

    override fun onBindViewHolder(holder: HolderPdfAdmin, position: Int) {
        //get data, set data, handle click...
        //get data
        val model = pdfArrayList[position]
        val pdfId = model.id
        val categoryId = model.categoryId
        val title = model.title
        val description = model.description
        val pdfUrl = model.url
        val timestamp = model.timestamp

        //convert timestamp to dd//MM//yyy format
        val formattedDate = MyApplication.formatTimeStamp(timestamp)

        //set data
        holder.titleTv.text = title
        holder.descriptionTv.text = description
        holder.dateTv.text = formattedDate

        //load details like category, pdf from url, pdf size

        //load categoy
        MyApplication.loadCategory(categoryId, holder.categoryTv)

        // pas null for page numbera/ load pdf thumbnail
        MyApplication.loadPdfFromUrlSinglePage(pdfUrl, title, holder.pdfView, holder.progressBar, null)

        //load pdf size
        MyApplication.loadPdfSize(pdfUrl, title, holder.sizeTv)

    }

    override fun getItemCount(): Int {
        return pdfArrayList.size //items count
    }

    //view holder class for row_pdf
    inner class HolderPdfAdmin(itemView: View) : RecyclerView.ViewHolder(itemView){
        //ui views of row_pdf
        val pdfView = binding.pdfView
        val progressBar = binding.progressBar
        val titleTv = binding.titleTv
        val descriptionTv = binding.descriptionTv
        val categoryTv = binding.categoryTv
        val sizeTv = binding.sizeTv
        val dateTv = binding.dateTv
        val moreBtn = binding.moreBtn
    }

    override fun getFilter(): Filter {
        if (filter == null) {
            filter = FilterPdfAdmin(filerList, this)
        }
        return filter as FilterPdfAdmin
    }
}