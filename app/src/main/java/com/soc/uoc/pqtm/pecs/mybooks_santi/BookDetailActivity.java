package com.soc.uoc.pqtm.pecs.mybooks_santi;


import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.soc.uoc.pqtm.pecs.mybooks_santi.model.BookContent;

import java.util.ArrayList;

/**
 * An activity representing a single book detail screen. This
 * activity is only used on narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link BookListActivity}.
 */
public class BookDetailActivity extends AppCompatActivity {
    private WebView webView;
    private FloatingActionButton fab;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);
        webView=findViewById(R.id.web_view);
        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

              webView.setVisibility(View.VISIBLE);
              webView.setWebViewClient(new Callback());
              webView.loadUrl("file:///android_asset/form.html");
              fab.setVisibility(View.GONE);



            }
        });



        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html
        //
        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putInt(BookDetailFragment.ARG_ITEM_ID,
                    getIntent().getIntExtra(BookDetailFragment.ARG_ITEM_ID, 0));
            BookDetailFragment fragment = new BookDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.book_detail_container, fragment)
                    .commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. Use NavUtils to allow users
            // to navigate up one level in the application structure. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class Callback extends WebViewClient {
        private String name,num,date;

        @Override
        public boolean shouldOverrideUrlLoading (WebView view, String url){
          //Valida els camps del formulari

            Uri uri=Uri.parse(url);
            name= uri.getQueryParameter("name");
            num=uri.getQueryParameter("num");
            date=uri.getQueryParameter("date");

            if(name != "" && num !="" && date !="" ){
                Snackbar.make(view, "Compra del llibre realitzada ", Snackbar.LENGTH_LONG).show();

                view.setVisibility(View.INVISIBLE);
                fab.setVisibility(View.VISIBLE);

            }else{
                Snackbar.make(view, "Ha d’omplir la informació de\n" +
                        "tots els camps", Snackbar.LENGTH_LONG).show();

                view.reload();
            }

           return true;



        }

    }
}
