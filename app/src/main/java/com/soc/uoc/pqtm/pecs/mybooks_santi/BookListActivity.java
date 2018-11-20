package com.soc.uoc.pqtm.pecs.mybooks_santi;

import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

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
import com.orm.SugarContext;
import com.soc.uoc.pqtm.pecs.mybooks_santi.adapters.MyAdapter;
import com.soc.uoc.pqtm.pecs.mybooks_santi.model.BookContent;

import java.util.ArrayList;
import java.util.List;

import static android.content.Intent.ACTION_DELETE;
import static android.content.Intent.ACTION_VIEW;

/**
 * An activity representing a list of books. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link BookDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class BookListActivity extends AppCompatActivity  {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private static final String TAG="BookListActivity";
    private boolean mTwoPane;
    MyAdapter adapter;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private  ArrayList<BookContent.BookItem> mValues;
    private static FirebaseAuth mAuth;
    private BookListActivity activity;
    private static final String BOOK = "BOOK";





    private void signIn() {
        mAuth.signInWithEmailAndPassword("santiestudiantitic@gmail.com", "firebase")
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInWithEmail:success");
                            downloadBooks();
                        } else {
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            getBooksFromDB();
                        }
                    }
                });
    }

    private void downloadBooks() {
        myRef = database.getReference("books");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                GenericTypeIndicator<ArrayList<BookContent.BookItem>> t = new GenericTypeIndicator<ArrayList<BookContent.BookItem>>() {};
                mValues = dataSnapshot.getValue(t);
                for (BookContent.BookItem bookItem :mValues){
                    if(!BookContent.exists(bookItem)){
                        bookItem.save();
                    }
                }
                //adapter.setBooks(mValues);
                adapter.setBooks(mValues);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
                getBooksFromDB();
            }
        });

    }




    private void getBooksFromDB() {
        List<BookContent.BookItem> values= BookContent.getBooks();
        adapter.setBooks(values);
    }

    private void listBooks (){
        RecyclerView recyclerView = findViewById(R.id.book_list);
        RecyclerView.LayoutManager layoutManager= new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new MyAdapter(new ArrayList<BookContent.BookItem>());
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        //Servei en escolta
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (getIntent() != null && getIntent().getAction() != null) {
            Integer bookPosition = Integer.valueOf(getIntent().getStringExtra(BOOK));
            String book = getIntent().getStringExtra(BOOK);

            //les duesopcions

            if (getIntent().getAction().equalsIgnoreCase(ACTION_DELETE)) {

                Toast.makeText(BookListActivity.this, "Acció eliminar", Toast.LENGTH_SHORT).show();
                BookContent.delete(bookPosition);
                adapter.setBooks(BookContent.getBooks());
                listBooks();
            } else if (getIntent().getAction().equalsIgnoreCase(ACTION_VIEW)) {
                Toast.makeText(BookListActivity.this, "Acció Veure detall", Toast.LENGTH_SHORT).show();

                if (mTwoPane) {

                    Bundle arguments = new Bundle();
                    arguments.putString(BookDetailFragment.ARG_ITEM_ID, book);
                    BookDetailFragment fragment = new BookDetailFragment();
                    fragment.setArguments(arguments);
                    FragmentManager manager = getSupportFragmentManager();
                    manager.beginTransaction()
                            .replace(R.id.book_detail_container, fragment)
                            .commit();
                } else {

                    intent = new Intent(getApplicationContext(), BookDetailActivity.class);
                    intent.putExtra(BookDetailFragment.ARG_ITEM_ID, book);
                    startActivity(intent);
                }
            }
            notificationManager.cancelAll();
        }


    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list);
        SugarContext.init(this);
        mAuth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        signIn();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();





            }
        });
        listBooks();

        if (findViewById(R.id.book_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

        FirebaseAuth.AuthStateListener authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                firebaseAuth.getCurrentUser();
            }
        };
        mAuth.addAuthStateListener(authStateListener);


















    }







}
