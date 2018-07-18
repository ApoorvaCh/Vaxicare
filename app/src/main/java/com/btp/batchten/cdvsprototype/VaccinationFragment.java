package com.btp.batchten.cdvsprototype;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link VaccinationFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link VaccinationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VaccinationFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    // TODO: Rename and change types of parameters

    private static final String ARG_SECTION_NUMBER = "section_number";

    private OnFragmentInteractionListener mListener;

    public VaccinationFragment() {
        // Required empty public constructor
    }



    // TODO: Rename and change types and number of parameters
    public static VaccinationFragment newInstance(int sectionNumber) {
        VaccinationFragment fragment = new VaccinationFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView;

        rootView = inflater.inflate(R.layout.fragment_vaccination, container, false);
        return rootView;
    }


    @Override
    public void onViewCreated(View v, Bundle b){
        Button addChild = (Button) getActivity().findViewById(R.id.addChild);
        Button viewChart = (Button) getActivity().findViewById(R.id.viewChart);
        Button pendindList = (Button) getActivity().findViewById(R.id.openPendingListButton);
        TextView appUserTextView = (TextView) getActivity().findViewById(R.id.appUserTextView);
        setRetainInstance(true);

        if(MainActivity.appUser == true) {
            addChild.setVisibility(View.GONE);
            float i = pendindList.getY();
            pendindList.setVisibility(View.GONE);
            viewChart.setY(i);
            appUserTextView.setText("User");

        }
        else{
           // addChild.setVisibility(View.VISIBLE);
           // pendindList.setVisibility(View.VISIBLE);
            appUserTextView.setText("Anganwadi Helper");
        }
    }




    /*
    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }
    */

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
