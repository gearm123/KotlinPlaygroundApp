package com.test.funfactsapp.adapters

import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.style.TextAppearanceSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.test.funfactsapp.R
import com.test.funfactsapp.db.FunFact
import java.util.*


class FunFactAdapter :
    RecyclerView.Adapter<FunFactAdapter.DataViewHolder>(), Filterable {

    var factList: ArrayList<FunFact> = ArrayList()
    var factListFiltered: ArrayList<FunFact> = ArrayList()
    var filterPattern: CharSequence = ""


    inner class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val factText: TextView

        init {
            factText = itemView.findViewById(R.id.fact_text)
        }

        //set list item properties
        fun bind(result: FunFact) {
            setText(result, factText)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = DataViewHolder(
        LayoutInflater.from(parent.context).inflate(
            R.layout.fact_item, parent,
            false
        )
    )

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        //bind items to filtered list
        holder.bind(factListFiltered[position])
        holder.itemView.setOnClickListener {
            Toast.makeText(
                holder.itemView.context,
                "Fact is chosen at index $position ",
                Toast.LENGTH_SHORT
            ).show()

        }
    }


    override fun getItemCount(): Int = factListFiltered.size

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    fun addData(list: List<FunFact>) {
        factList = list as ArrayList<FunFact>
        factListFiltered = factList
        notifyDataSetChanged()
    }

    //Filtering using constraint input by title and body
    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charString = constraint?.toString() ?: ""
                factListFiltered = if (charString.isEmpty()) {
                    filterPattern = ""
                    factList
                } else {
                    filterPattern = constraint.toString().lowercase(Locale.ROOT).trim();
                    val filteredList = ArrayList<FunFact>()
                    factList
                        .filter {
                            (it.text.lowercase(Locale.ROOT).contains(
                                constraint.toString()
                                    .lowercase(Locale.ROOT)
                            ))
                        }
                        .forEach { filteredList.add(it) }
                    filteredList

                }
                return FilterResults().apply { values = factListFiltered }
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {

                factListFiltered = if (results?.values == null) {
                    ArrayList()
                } else {
                    results.values as ArrayList<FunFact>
                }
                notifyDataSetChanged()
            }
        }
    }

    //setting title text. highlighting part of text if needed
    private fun setText(itemView: FunFact, titleTv: TextView) {
        if (filterPattern != "") {
            val startPos: Int =
                itemView.text.lowercase(Locale.ROOT)
                    .indexOf(filterPattern.toString().lowercase(Locale.ROOT))
            if (startPos != -1) {
                titleTv.text = createSpannable(startPos, itemView.text)
            } else {
                titleTv.text = itemView.text
            }
        } else {
            titleTv.text = itemView.text
        }
    }

    //Creating spannable to highlight filtered text
    private fun createSpannable(startPos: Int, baseText: String): Spannable {
        val endPos = startPos + filterPattern.length
        val spannable: Spannable = SpannableString(baseText)
        val blueColor = ColorStateList(arrayOf(intArrayOf()), intArrayOf(Color.BLUE))
        val highlightSpan = TextAppearanceSpan(null, Typeface.BOLD, -1, blueColor, null)
        spannable.setSpan(
            highlightSpan,
            startPos,
            endPos,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        return spannable
    }
}