package com.example.bookapp.filters

import android.widget.Filter
import com.example.bookapp.adapter.AdapterPdfUser
import com.example.bookapp.models.ModelPdf

class FilterPdfUser: Filter {
    //arraylist
    var filterList: ArrayList<ModelPdf>

    //adapter in which filter need to be implement
    var adapterPdfUser: AdapterPdfUser

    //constructor
    constructor(filterList: ArrayList<ModelPdf>, adapterPdfUser: AdapterPdfUser) : super() {
        this.filterList = filterList
        this.adapterPdfUser = adapterPdfUser
    }

    override fun performFiltering(constraint: CharSequence?): FilterResults {
        var constraint: CharSequence? = constraint // value to search
        val results = FilterResults()
        // value to be searched should not be null and not empty
        if (constraint != null && constraint.isNotEmpty()) {
            //change to upper case, or lower case avoid case sensitivity
            constraint = constraint.toString().lowercase()
            var filteredModels = ArrayList<ModelPdf>()
            for (i in filterList.indices){
                //validate if match
                if (filterList[i].title.lowercase().contains(constraint)){
                    //searched value is similar to value in list , add to filtered list
                    filteredModels.add(filterList[i])
                }
            }
            //return filtered list and size
            results.count = filteredModels.size
            results.values = filteredModels
        }
        else {
            //searched value is either null or empty, return all data
            results.count = filterList.size
            results.values = filterList
        }
        return results
    }

    override fun publishResults(constraint: CharSequence?, results: FilterResults) {
        //apply filter changes
        adapterPdfUser.pdfArrayList = results.values as ArrayList<ModelPdf> /* = java.util.ArrayList<com.example.bookapp.models.ModelPdf> */

        //notify changes
        adapterPdfUser.notifyDataSetChanged()
    }
}