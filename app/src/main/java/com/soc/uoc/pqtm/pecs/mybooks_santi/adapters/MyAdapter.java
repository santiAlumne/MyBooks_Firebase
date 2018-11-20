package com.soc.uoc.pqtm.pecs.mybooks_santi.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.soc.uoc.pqtm.pecs.mybooks_santi.BookDetailActivity;
import com.soc.uoc.pqtm.pecs.mybooks_santi.BookDetailFragment;
import com.soc.uoc.pqtm.pecs.mybooks_santi.BookListActivity;
import com.soc.uoc.pqtm.pecs.mybooks_santi.R;
import com.soc.uoc.pqtm.pecs.mybooks_santi.model.BookContent;

import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder>{
    private List<BookContent.BookItem> mValues;

    private boolean mTwoPane ;
    private  BookListActivity activity;

    public void setBooks(List<BookContent.BookItem> books) {
        mValues = books;
        notifyDataSetChanged();
    }

    private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            BookContent.BookItem item = (BookContent.BookItem) view.getTag();
            if (mTwoPane) {
                Bundle arguments = new Bundle();

                arguments.putString(BookDetailFragment.ARG_ITEM_ID, String.valueOf(item.getTitle()));
                BookDetailFragment fragment = new BookDetailFragment();
                fragment.setArguments(arguments);
                activity.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.book_detail_container, fragment)
                        .commit();
            } else {
                Context context = view.getContext();
                Intent intent = new Intent(context, BookDetailActivity.class);
                Bundle arguments = new Bundle();

                intent.putExtras(arguments);
                intent.putExtra(BookDetailFragment.ARG_ITEM_ID, String.valueOf(item.getTitle()));
                activity.startActivity(intent);
            }
        }
    };

    public MyAdapter(List<BookContent.BookItem> items){mValues=items;}

    public MyAdapter(BookListActivity mContext, ArrayList<BookContent.BookItem> mValues, boolean mTwoPane) {
        this.activity = mContext;
        this.mValues = mValues;
        this.mTwoPane = mTwoPane;
    }

    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.book_list_content_parells, parent, false);
            return new ViewHolder(view);
        }
        else {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.book_list_content_senars, parent, false);
            return new ViewHolder(view);
        }
    }

    @Override
    public int getItemCount(){
        return this.mValues != null ? this.mValues.size() : 0;
        //return mValues.size();
        //return 0;
    }

    @Override
    public int getItemViewType(int position) {
        if (position % 2 == 0) {
            return 0;
        } else {
            return 1;
        }
    }

    @Override
    public void onBindViewHolder(MyAdapter.ViewHolder holder, final int position) {
        holder.mItem=mValues.get(position);
        holder.mTitleView.setText(String.valueOf(mValues.get(position).getTitle()));
        holder.mAuthorView.setText(mValues.get(position).getAuthor());
        holder.mView.setTag(position);
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentPos=(int) v.getTag();
                if(mTwoPane){
                    Bundle arguments=new Bundle();
                    arguments.putInt(BookDetailFragment.ARG_ITEM_ID,currentPos);
                    BookDetailFragment fragment=new BookDetailFragment();
                    fragment.setArguments(arguments);
                    activity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.book_detail_container,fragment)
                            .commit();
                }else{
                    Context context=v.getContext();
                    Intent intent=new Intent(context, BookDetailActivity.class);
                    intent.putExtra(BookDetailFragment.ARG_ITEM_ID,currentPos);
                    context.startActivity(intent);
                }

            }
        });

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;

        public TextView mTitleView, mAuthorView;
        public BookContent.BookItem mItem;

        public ViewHolder(View view) {
            super(view);
            mView=view;
            mTitleView = view.findViewById(R.id.titol);
            mAuthorView = view.findViewById(R.id.detail_autor);
        }

        @Override

        public String toString(){
            return super.toString()+ " '"+mTitleView.getText()+ " '";
        }

    }
}
