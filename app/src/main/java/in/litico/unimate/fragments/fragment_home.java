package in.litico.unimate.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import in.litico.unimate.R;
import in.litico.unimate.managers.manager_misc;
import in.litico.unimate.views.general_card;
import in.litico.unimate.views.schedule_class;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link fragment_home.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link fragment_home#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragment_home extends Fragment{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    general_card dh1_card;
    general_card dh2_card;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public fragment_home() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragment_home.
     */
    // TODO: Rename and change types and number of parameters
    public static fragment_home newInstance(String param1, String param2) {
        fragment_home fragment = new fragment_home();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fragment_home, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        schedule_class class_one = new schedule_class(getActivity(), "Introduction to Physics I", "C315", "10:00pm to 11:00pm");
        schedule_class class_two = new schedule_class(getActivity(), "Introduction to Electrical Engineering", "D026", "11:00pm to 12:00pm");
        general_card attendance_card = new general_card(getActivity(), "Attendance", null);

        dh1_card = new general_card(getActivity(), "DH1 - Menu", new ProgressBar(getActivity()));
        dh2_card = new general_card(getActivity(), "DH2 - Menu", new ProgressBar(getActivity()));


        LinearLayout holder_one = (LinearLayout)getView().findViewById(R.id.first_class);
        holder_one.addView(class_one);

        LinearLayout holder_two = (LinearLayout)getView().findViewById(R.id.second_class);
        holder_two.addView(class_two);

        LinearLayout holder_att = (LinearLayout)getView().findViewById(R.id.attencance_h);
        holder_att.addView(attendance_card);

        LinearLayout holder_dh1 = (LinearLayout)getView().findViewById(R.id.mess_menu_dh1);
        holder_dh1.addView(dh1_card);

        LinearLayout holder_dh2 = (LinearLayout)getView().findViewById(R.id.mess_menu_dh2);
        holder_dh2.addView(dh2_card);

        manager_misc.mess_menu(getContext(), dh1_card, dh2_card, getActivity());
    }
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
