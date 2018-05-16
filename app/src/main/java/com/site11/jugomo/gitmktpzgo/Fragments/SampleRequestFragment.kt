package com.site11.jugomo.gitmktpzgo.Fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.exception.ApolloException
import com.site11.jugomo.gitmktpzgo.Activities.MainActivity

import com.site11.jugomo.gitmktpzgo.R
import com.site11.jugomo.gitmktpzgo.Util.GraphqlAdapter

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [SampleRequestFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [SampleRequestFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class SampleRequestFragment : Fragment() {



    var result: TextView? = null
    var graphqlAdapter: GraphqlAdapter? = null
    var listener: ApolloCall.Callback<FindQuery.Data>? = null
    var activity: MainActivity? = null


    //
    //
    //


    init {
        this.listener = object: ApolloCall.Callback<FindQuery.Data>() {

                override fun onFailure(e: ApolloException) {
                    e.printStackTrace()

                    activity!!.runOnUiThread(java.lang.Runnable {
                        result!!.text = e.toString() + "\n\n\n" +
                                "       ----> please set you AUTH_TOKEN in code!"
                    })

                    Log.e("----> E", "EX: " + e.toString())
                }

                override fun onResponse(response: com.apollographql.apollo.api.Response<FindQuery.Data>) {
                    var data = response.data()

                    activity!!.runOnUiThread(java.lang.Runnable {
                        result!!.text = data.toString()
                    })


                    Log.e("---> R", "DATA: " + data)
                }


        }

        this.graphqlAdapter = GraphqlAdapter.newInstance(listener!!)
    }

    companion object {
        fun newInstance(): SampleRequestFragment {
            val fragment = SampleRequestFragment()
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        var view = inflater.inflate(R.layout.fragment_sample_request, container, false)

        activity = view.context as MainActivity

        var project = view.findViewById<EditText>(R.id.etProjectName_FRAGMENT_SAMPLE_REQUEST)
        var repo = view.findViewById<EditText>(R.id.etRepoName_FRAGMENT_SAMPLE_REQUEST)
        var search = view.findViewById<ImageButton>(R.id.ibSearch_FRAGMENT_SAMPLE_REQUEST)
        result = view.findViewById<TextView>(R.id.tvResult_FRAGMENT_SAMPLE_REQUEST)

        search.setOnClickListener(View.OnClickListener {
            graphqlAdapter!!.request(project.text.toString(), repo.text.toString())
        })

        return view
    }

}
