package com.example.bookapp.filters

import android.widget.Filter
import com.example.bookapp.adapter.AdapterCategory
import com.example.bookapp.models.ModelCategory

class FilterCategory : Filter {
    //arraylist in which ,want to search
    private var filterList: ArrayList<ModelCategory>

    //adapter in which filter need to be implemented
    private var adapterCategory: AdapterCategory

    //constuctor
    constructor(filterList: ArrayList<ModelCategory>, adapterCategory: AdapterCategory) : super() {
        this.filterList = filterList
        this.adapterCategory = adapterCategory


    }

    override fun performFiltering(constraint: CharSequence?): FilterResults {
        var constraint = constraint
        var results = FilterResults()

        //value should not be null and not empty

        if (constraint != null && constraint.isNotEmpty()) {
            //change to upper case, or lower case to avoid case sensitivity
            constraint = constraint.toString().uppercase()
            val filteredModels: ArrayList<ModelCategory> = ArrayList()
            for (i in 0 until filterList.size) {
                //validate
                if(filterList[i].category.uppercase().contains(constraint)){
                    //add to filtered list
                    filteredModels.add(filterList[i])
                }
            }
            results.count = filteredModels.size
            results.values = filteredModels
        }
        else{
            //search value is either null or empty
            results.count = filterList.size
            results.values = filterList
        }

        return results
    }

    override fun publishResults(constraint: CharSequence?, results: FilterResults) {
        //apply filter change
        adapterCategory.categoryArrayList = results.values as ArrayList<ModelCategory>
        //notify change
        adapterCategory.notifyDataSetChanged()
    }
}


