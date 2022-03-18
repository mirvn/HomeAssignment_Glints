package com.glints.homeassignment.view.dashboard.adapter

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.glints.homeassignment.R
import com.glints.homeassignment.databinding.ItemListHistoryBinding
import com.glints.homeassignment.model.TransactionHistory
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class TransactionHistoryAdapter : RecyclerView.Adapter<TransactionHistoryAdapter.ListViewHolder>() {
    val listTransaction = ArrayList<TransactionHistory>()

    fun setDataHistory(dataHistory: ArrayList<TransactionHistory>) {
        listTransaction.clear()
        listTransaction.addAll(dataHistory)
        Log.d("TAG", "listTransaction: $listTransaction")
        notifyDataSetChanged()
    }

    inner class ListViewHolder(val binding: ItemListHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(historyTransaction: TransactionHistory) {
            try {
                val dtStart = historyTransaction.transactionDate.substringBeforeLast("T")
                val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val date = sdf.parse(dtStart)
                val sdf2 = SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault())
                val parsedDate = sdf2.format(date)
                binding.tvDateHistory.text = parsedDate.toString()
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            binding.tvNameHistory.text = historyTransaction.accountHolder
            binding.tvAccountNoHistory.text = historyTransaction.accountNo
            binding.tvType.text = historyTransaction.transactionType

            if (historyTransaction.transactionType == itemView.context.getString(R.string.received)) {
                binding.tvAmountHistory.setTextColor(Color.GREEN)
                binding.tvAmountHistory.text = "+${historyTransaction.amount}"
            } else {
                binding.tvAmountHistory.setTextColor(Color.RED)
                binding.tvAmountHistory.text = "-${historyTransaction.amount}"
            }
            /*binding.tvAmountHistory.text = historyTransaction.amount.toString()*/
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val listTransactionHistory =
            ItemListHistoryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return ListViewHolder(listTransactionHistory)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val transactionHistory = listTransaction[position]
        holder.bind(transactionHistory)
    }

    override fun getItemCount(): Int {
        return listTransaction.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
}