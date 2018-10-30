package com.soc.uoc.pqtm.pecs.mybooks_santi;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.soc.uoc.pqtm.pecs.mybooks_santi.model.Books;

import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Iterator;


/**
 * An activity representing a list of books. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link BookDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class BookListActivity extends AppCompatActivity {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */

    private boolean mTwoPane;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private static ArrayList<Books.BookItem> mValues;
    private static FirebaseAuth mAuth;
    private static final String TAG = BookListActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list);
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myRef = FirebaseDatabase.getInstance().getReference("books");
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        if (findViewById(R.id.book_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

         //Fa login amb el servidor
        mAuth.signInWithEmailAndPassword("santiestudiantitic@gmail.com", "firebase")
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "Funciona!!!!");

                            //demana la llista al servidor
                            myRef.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    GenericTypeIndicator<ArrayList<Books.BookItem>> t = new GenericTypeIndicator<ArrayList<Books.BookItem>>() {
                                    };
                                    mValues = dataSnapshot.getValue(t);
                                    Iterator<Books.BookItem> iterBooks = mValues.iterator();
                                    int i = 0;
                                    while (iterBooks.hasNext()) {
                                        iterBooks.next().setBookId(i);
                                        i++;
                                    }

                                    View recyclerView = findViewById(R.id.book_list);
                                    assert recyclerView != null;
                                    setupRecyclerView((RecyclerView) recyclerView);

                                }

                                @Override
                                public void onCancelled(DatabaseError error) {
                                    // Failed to read value
                                    Log.w(TAG, "Failed to read value.", error.toException());
                                }
                            });

                        } else {
                            Log.w(TAG, "No funciona", task.getException());
                        }
                    }
                });
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(this, mValues, mTwoPane));
    }

    public static class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private ArrayList<Books.BookItem> mValues;
        private final BookListActivity mContext;
        private boolean mTwoPane;
        private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Books.BookItem item = (Books.BookItem) view.getTag();
                if (mTwoPane) {
                    Bundle arguments = new Bundle();
                    arguments.putSerializable("list", mValues);
                    arguments.putString(BookDetailFragment.ARG_ITEM_ID, String.valueOf(item.getBookId()));
                    BookDetailFragment fragment = new BookDetailFragment();
                    fragment.setArguments(arguments);
                    mContext.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.book_detail_container, fragment)
                            .commit();
                } else {
                    Context context = view.getContext();
                    Intent intent = new Intent(context, BookDetailActivity.class);
                    Bundle arguments = new Bundle();
                    arguments.putSerializable("list", mValues);
                    intent.putExtras(arguments);
                    intent.putExtra(BookDetailFragment.ARG_ITEM_ID, String.valueOf(item.getBookId()));
                    mContext.startActivity(intent);
                }
            }
        };

        public SimpleItemRecyclerViewAdapter(BookListActivity mContext, ArrayList<Books.BookItem> mValues, boolean mTwoPane) {
            this.mContext = mContext;
            this.mValues = mValues;
            this.mTwoPane = mTwoPane;
        }

        @Override
        public SimpleItemRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == 0) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.book_list_content_parells, parent, false);
                return new SimpleItemRecyclerViewAdapter.ViewHolder(view);
            } else {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.book_list_content_senars, parent, false);
                return new SimpleItemRecyclerViewAdapter.ViewHolder(view);
            }
        }

        @Override
        public int getItemCount() {
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
        public void onBindViewHolder(SimpleItemRecyclerViewAdapter.ViewHolder holder, final int position) {

            holder.mTitleView.setText(String.valueOf(mValues.get(position).getTitle()));
            holder.mAuthorView.setText(mValues.get(position).getAuthor());
            holder.itemView.setTag(mValues.get(position));
            holder.itemView.setOnClickListener(mOnClickListener);

        }

        public class ViewHolder extends RecyclerView.ViewHolder {


            private TextView mTitleView, mAuthorView;


            public ViewHolder(View view) {
                super(view);
                mTitleView = view.findViewById(R.id.titol);
                mAuthorView = view.findViewById(R.id.detail_autor);
            }

        }
    }


}
