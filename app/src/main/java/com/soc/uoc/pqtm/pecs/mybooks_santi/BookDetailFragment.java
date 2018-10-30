package com.soc.uoc.pqtm.pecs.mybooks_santi;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.soc.uoc.pqtm.pecs.mybooks_santi.model.Books;
import com.soc.uoc.pqtm.pecs.mybooks_santi.utils.DownloadImageTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * A fragment representing a single book detail screen.
 * This fragment is either contained in a {@link BookListActivity}
 * in two-pane mode (on tablets) or a {@link BookDetailActivity}
 * on handsets.
 */
public class BookDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "bookId";

    /**
     * The dummy content this fragment is presenting.
     */
    private Books.BookItem mItem;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public BookDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            ArrayList<Books.BookItem> llista = (ArrayList<Books.BookItem>) getArguments().getSerializable("list");


                mItem = llista.get(Integer.parseInt(getArguments().getString(ARG_ITEM_ID)));
                Activity activity = this.getActivity();
                CollapsingToolbarLayout appBarLayout = activity.findViewById(R.id.toolbar_layout);
                if (appBarLayout != null) {
                    appBarLayout.setTitle(mItem.getTitle());
                }





    }

    // emplena la activitat detail
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.book_detail, container, false);
        SimpleDateFormat dformat = new SimpleDateFormat("dd/mm/yyyy");

        // Show the dummy content as text in a TextView.
        if (mItem != null) {
            new DownloadImageTask((ImageView) rootView.findViewById(R.id.detail_imatgeUrl)).execute(mItem.getUrl_image());


            ((TextView) rootView.findViewById(R.id.detail_autor)).setText(mItem.getAuthor());
            ((TextView) rootView.findViewById(R.id.detail_descripcio)).setText(mItem.getDescription());
            ((TextView) rootView.findViewById(R.id.detail_datapublicacio)).setText(dformat.format(mItem.getPublication_date()));
        }
        return rootView;
    }
}
