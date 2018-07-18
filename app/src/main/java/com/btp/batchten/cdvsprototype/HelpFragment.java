package com.btp.batchten.cdvsprototype;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.widget.SearchView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

import static android.content.DialogInterface.*;



public class HelpFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    private OnFragmentInteractionListener mListener;

    public HelpFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static HelpFragment newInstance(int sectionNumber) {
        HelpFragment fragment = new HelpFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    class Vaccine{
        String name;
        String link;
        ArrayList<String> ages;

        Vaccine(String name, String link, String agesArray){
            this.name = name;
            this.link = link;
            String tempArray[] = agesArray.split(",");
            this.ages = new ArrayList<String>(Arrays.asList(tempArray));
        }

    }

    class ListAdapter extends BaseAdapter {

        ArrayList<Vaccine> vaccineObjects;
        ArrayList<Vaccine> searchResults;

        filter_here filter;

        ListAdapter(){

            vaccineObjects = new ArrayList<>();

            for(int i=0; i<MainActivity.vaccineNames.size(); i++){
                vaccineObjects.add(new Vaccine(MainActivity.vaccineNames.get(i), MainActivity.vaccineLinks.get(i), MainActivity.vaccineAges.get(i)));
            }

            searchResults = new ArrayList<Vaccine>(vaccineObjects);

            filter = new filter_here();


        }


        @Override
        public int getCount() {
            return searchResults.size();
        }

        @Override
        public Object getItem(int i) {
            return searchResults.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }



        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = layoutInflater.inflate(R.layout.vaccine_list_item, viewGroup, false);
            TextView textView = v.findViewById(R.id.vaccineListItemTextView);
            textView.setTag(searchResults.get(i).link);
            textView.setText(searchResults.get(i).name);
            return v;
        }

        public class filter_here extends Filter {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                // TODO Auto-generated method stub

                FilterResults Result = new FilterResults();
                // if constraint is empty return the original names
                if(constraint.length() == 0 ){
                    Result.values = vaccineObjects;
                    Result.count = vaccineObjects.size();
                    return Result;
                }

                ArrayList<Vaccine> Filtered_Names = new ArrayList<Vaccine>();
                String filterString = constraint.toString().toLowerCase();
                String filterableString;

                for(int i = 0; i<vaccineObjects.size(); i++){
                    filterableString = vaccineObjects.get(i).name;
                    if(filterableString.toLowerCase().contains(filterString)){
                        Filtered_Names.add(vaccineObjects.get(i));
                    }
                }
                Result.values = Filtered_Names;
                Result.count = Filtered_Names.size();

                return Result;
            }

            @Override
            protected void publishResults(CharSequence constraint,FilterResults results) {
                // TODO Auto-generated method stub
                searchResults = (ArrayList<Vaccine>) results.values;
                notifyDataSetChanged();
            }

        }

        public Filter getFilter() {
            // TODO Auto-generated method stub
            return filter;
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_help, container, false);





        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        final ListView listView= (ListView) view.findViewById(R.id.help_list_view);
        final ListAdapter listAdapter;
        setRetainInstance(true);
        listView.setAdapter(listAdapter = new  ListAdapter());
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView textView= view.findViewById(R.id.vaccineListItemTextView);
                String link = (String) textView.getTag();
                Intent intent =new Intent(getContext(), WebActivity.class);
                intent.putExtra("link",link);
                startActivity(intent);
            }
        });

        MenuItem menuItem = MainActivity.activityMenu.findItem(R.id.menuSearch);
        SearchView searchView = (SearchView) menuItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                listAdapter.getFilter().filter(newText);
                return false;
            }
        });
    }



    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    /*
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }
    */

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
