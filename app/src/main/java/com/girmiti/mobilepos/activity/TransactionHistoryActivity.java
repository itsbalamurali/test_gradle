package com.girmiti.mobilepos.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.girmiti.mobilepos.R;
import com.girmiti.mobilepos.logger.Logger;
import com.girmiti.mobilepos.store.DataStoreHelper;
import com.girmiti.mobilepos.store.Transaction;
import com.girmiti.mobilepos.util.Constants;
import com.girmiti.mobilepos.util.CurrencyFormat;
import com.girmiti.mobilepos.util.Utils;

import java.util.ArrayList;
import java.util.List;

import static com.girmiti.mobilepos.logger.Logger.getNewLogger;

/**
 * Created by Girish on 22-10-2016.
 */
public class TransactionHistoryActivity extends BaseActivity {

    private ListView transactionList;
    private DataStoreHelper db;
    private List<Transaction> transactionData = null;
    private List<Transaction> transactionDataSearch;
    private SearchView searchView;
    private HistoryAdapter historyAdapter;
    private TextView noTrasaction;
    private LinearLayout searchViewLayout;
    public static final String DATETIMEFORMAT = "dd/MM/yyyy   hh:mm:ss a";
    private final Logger logger = getNewLogger(TransactionHistoryActivity.class.getName());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        TextView toolBar;
        LinearLayout linearLayout;
        ImageView menuIcn;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_history);

        db = new DataStoreHelper(this);
        transactionDataSearch = new ArrayList<>();
        historyAdapter = new HistoryAdapter();
        linearLayout = (LinearLayout) findViewById(R.id.toolbar_new);
        menuIcn = (ImageView) linearLayout.findViewById(R.id.menuicon);
        transactionList = (ListView) findViewById(R.id.transaction_history);
        searchView = (SearchView) findViewById(R.id.search);
        searchView.setQueryHint(getString(R.string.search_hint));

        noTrasaction = (TextView) findViewById(R.id.no_trasaction);
        searchViewLayout = (LinearLayout) findViewById(R.id.searchViewLayout);

        searchView.setIconifiedByDefault(false);
        searchView.setIconified(false);

        toolBar = (TextView) linearLayout.findViewById(R.id.tvToolbar);
        toolBar.setText(R.string.history_name);

        menuIcn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menu.toggle();
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String searchText) {

                searchTransaction(searchText);

                return false;
            }
        });

        transactionList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(TransactionHistoryActivity.this, TransactionHistoryDetails.class);
                Bundle b = new Bundle();
                b.putSerializable("transaction", transactionData.get(position));
                intent.putExtras(b);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
            }
        });

        settingMenu(TransactionHistoryActivity.this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        try {
            transactionData = db.retrieveTransactions();
        } catch (Exception e) {
            logger.severe("Error in Fetching Transaction from DB" + e);
        }

        if (transactionData == null) {
            Toast.makeText(this, getString(R.string.error_retrieve), Toast.LENGTH_SHORT).show();
            return;
        }

        displayEmptyView(transactionData);

        historyAdapter.setData(transactionData);
        transactionList.setAdapter(historyAdapter);
        searchView.setQuery("", false);
        searchViewLayout.requestFocus();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(TransactionHistoryActivity.this, SlidingMenuActivity.class);
        startActivity(intent);
        finish();
    }

    private void searchTransaction(String key) {

        if (transactionDataSearch != null)
            transactionDataSearch.clear();

        if (transactionData != null) {
            String value = key.toLowerCase();
            for (int i = 0; i < transactionData.size(); i++) {
                if (transactionData.get(i).getTransactionType().toLowerCase().contains(value) && transactionDataSearch != null) {
                    transactionDataSearch.add(transactionData.get(i));
                }
            }

            if (!key.equals("") && transactionDataSearch != null && transactionDataSearch.size() > 0) {
                displayEmptyView(transactionDataSearch);
                historyAdapter.setData(transactionDataSearch);
            } else {
                displayEmptyView(transactionData);
                historyAdapter.setData(transactionData);
            }
            historyAdapter.notifyDataSetChanged();
        }
    }

    private void displayEmptyView(List<Transaction> transactionDataList) {

        if (transactionDataList.size() > 0) {
            transactionList.setVisibility(View.VISIBLE);
            noTrasaction.setVisibility(View.GONE);
        } else {
            noTrasaction.setVisibility(View.VISIBLE);
            transactionList.setVisibility(View.GONE);
        }
    }

    private class HistoryAdapter extends BaseAdapter {
        List<Transaction> transactionListData = new ArrayList<>();

        public void setData(List<Transaction> transactionListData) {
            this.transactionListData = transactionListData;

        }

        @Override
        public int getCount() {
            return transactionListData.size();
        }

        @Override
        public Transaction getItem(int position) {
            return transactionListData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            final ViewHolder holder;

            if (view == null) {
                holder = new ViewHolder();
                view = LayoutInflater.from(TransactionHistoryActivity.this).inflate(R.layout.transaction_history_row, null);
                holder.date = (TextView) view.findViewById(R.id.dataTextView);
                holder.time = (TextView) view.findViewById(R.id.timeTextView);
                holder.transaction = (TextView) view.findViewById(R.id.transaction_name);
                holder.status = (TextView) view.findViewById(R.id.status);
                holder.amountTxt = (TextView) view.findViewById(R.id.amount_txt);
                view.setTag(holder);

            } else {
                holder = (ViewHolder) view.getTag();
            }

            if (getItem(position).getTxnDateTime() != null) {
                String date = (getItem(position).getTxnDateTime());
                holder.date.setText(date);
            }

            holder.time.setText(getItem(position).getStatus());
            holder.transaction.setText(getItem(position).getTransactionType());
            if(getItem(position).getTotalamount()!=null || !getItem(position).getTotalamount().equals( "" )) {
                holder.amountTxt.setText(CurrencyFormat.getFormattedCurrency(String.valueOf(Double.valueOf(Utils.formatAmount(getItem(position).getTotalamount())) / Constants.HUNDRED)));
            } else {
                holder.amountTxt.setText(CurrencyFormat.getFormattedCurrency(String.valueOf(Double.valueOf(Utils.formatAmount(getString( R.string.default_amount ))))));

            }
            return view;
        }

        private class ViewHolder {
            TextView date;
            TextView time;
            TextView transaction;
            TextView status;
            TextView amountTxt;
        }
    }
}