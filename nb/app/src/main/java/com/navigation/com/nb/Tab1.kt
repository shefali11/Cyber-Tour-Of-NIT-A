package com.navigation.com.nb

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import com.navigation.com.nb.R.id.imageBut


/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [Tab1.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [Tab1.newInstance] factory method to
 * create an instance of this fragment.
 */
class Tab1 : Fragment() {

    // TODO: Rename and change types of parameters
    private var mParam1: String? = null
    private var mParam2: String? = null

    private var mListener: Tab1.OnFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            mParam1 = arguments.getString(ARG_PARAM1)
            mParam2 = arguments.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val rootView = inflater!!.inflate(R.layout.fragment_tab1, container, false)
        val button1 = rootView.findViewById<View>(R.id.imageBut) as ImageButton
        button1.setOnClickListener {
            val intent = Intent(activity, cse::class.java)
            startActivity(intent)
        }
        val button2 = rootView.findViewById<View>(R.id.imageButton2) as ImageButton
        button2.setOnClickListener {
            val intent = Intent(activity, ece::class.java)
            startActivity(intent)
        }
        val button3 = rootView.findViewById<View>(R.id.imageButton4) as ImageButton
        button3.setOnClickListener {
            val intent = Intent(activity, mathsdept::class.java)
            startActivity(intent)
        }
        val button4 = rootView.findViewById<View>(R.id.imageButton3) as ImageButton
        button4.setOnClickListener {
            val intent = Intent(activity, mba::class.java)
            startActivity(intent)
        }
        val button5 = rootView.findViewById<View>(R.id.imageButton5) as ImageButton
        button5.setOnClickListener {
            val intent = Intent(activity, mechdept::class.java)
            startActivity(intent)
        }
        val button6 = rootView.findViewById<View>(R.id.imageButton6) as ImageButton
        button6.setOnClickListener {
            val intent = Intent(activity, phy_chemdept::class.java)
            startActivity(intent)
        }
        val button7 = rootView.findViewById<View>(R.id.imageButton8) as ImageButton
        button7.setOnClickListener {
            val intent = Intent(activity, chemical::class.java)
            startActivity(intent)
        }
  return rootView
//        return inflater!!.inflate(R.layout.fragment_tab1, container, false)
    }

    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
        if (mListener != null) {
            mListener!!.onFragmentInteraction(uri)
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            mListener = context
        } else {
            throw RuntimeException(context!!.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments](http://developer.android.com/training/basics/fragments/communicating.html) for more information.
     */
    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
    }

    companion object {
        // TODO: Rename parameter arguments, choose names that match
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private val ARG_PARAM1 = "param1"
        private val ARG_PARAM2 = "param2"

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Tab1.
         */
        // TODO: Rename and change types and number of parameters
        fun newInstance(param1: String, param2: String): Tab1 {
            val fragment = Tab1()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }
}// Required empty public constructor
