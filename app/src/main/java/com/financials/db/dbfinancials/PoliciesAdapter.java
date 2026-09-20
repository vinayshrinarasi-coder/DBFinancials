package com.financials.db.dbfinancials;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.google.android.material.chip.Chip;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class PoliciesAdapter extends RecyclerView.Adapter<PoliciesAdapter.MyViewHolder> implements Filterable {

    private final List<Policy> poicyList;
    private List<Policy> poicyListFiltered;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView policyNumber, bank, name, amount, date, counter;
        public Chip categoryChip;

        public MyViewHolder(View view) {
            super(view);
            policyNumber = view.findViewById(R.id.policyNumber);
            bank = view.findViewById(R.id.bank);
            name = view.findViewById(R.id.name);
            amount = view.findViewById(R.id.amount);
            date = view.findViewById(R.id.date);
            counter = view.findViewById(R.id.counter);
            categoryChip = view.findViewById(R.id.categoryChip);
        }
    }

    public PoliciesAdapter(List<Policy> policyList) {
        this.poicyList = policyList;
        this.poicyListFiltered = policyList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.policy_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    public List<Policy> getList() {
        return this.poicyListFiltered;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Locale locale = new Locale("en", "IN");
        DecimalFormat decimalFormat = (DecimalFormat) DecimalFormat.getCurrencyInstance(locale);
        DecimalFormatSymbols dfs = DecimalFormatSymbols.getInstance(locale);
        dfs.setCurrencySymbol("\u20B9");
        decimalFormat.setDecimalFormatSymbols(dfs);

        Policy policy = poicyListFiltered.get(position);
        holder.policyNumber.setText(policy.getCertificateNumber());
        holder.bank.setText(policy.getBankName().length() >= 17 ? policy.getBankName().substring(0, 16) : policy.getBankName());
        holder.name.setText(policy.getHolder());
        
        String formattedAmt = decimalFormat.format(policy.getMaturityAmount());
        String amt = formattedAmt.substring(0, formattedAmt.length() - 3);
        holder.amount.setText(amt);
        holder.date.setText(policy.getReadableDateOfMaturity());
        holder.counter.setText(String.valueOf(position + 1));
        holder.categoryChip.setText(policy.getCategory() != null ? policy.getCategory() : "Bank");
    }

    @Override
    public int getItemCount() {
        return poicyListFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    poicyListFiltered = poicyList;
                } else {
                    List<Policy> filteredList = new ArrayList<>();
                    for (Policy row : poicyList) {
                        if (row.getHolder().toLowerCase().contains(charString.toLowerCase()) || 
                            row.getBankName().toLowerCase().contains(charString.toLowerCase()) || 
                            row.getCertificateNumber().toLowerCase().contains(charString.toLowerCase()) || 
                            row.getRemarks().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }
                    poicyListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = poicyListFiltered;
                return filterResults;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                poicyListFiltered = (List<Policy>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
}