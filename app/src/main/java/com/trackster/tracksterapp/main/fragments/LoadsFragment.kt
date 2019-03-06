package com.trackster.tracksterapp.main.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import com.trackster.tracksterapp.R
import com.trackster.tracksterapp.adapters.LoadsRecyclerAdapter
import com.trackster.tracksterapp.base.App
import com.trackster.tracksterapp.base.BaseBottomItemMenuFragment
import com.trackster.tracksterapp.base.BaseFragment
import com.trackster.tracksterapp.base.BaseMainViewFragment
import com.trackster.tracksterapp.main.DaggerMainComponent
import com.trackster.tracksterapp.main.MainActivityContextModule
import com.trackster.tracksterapp.main.MainContract
import com.trackster.tracksterapp.main.MainPresenterModule
import com.trackster.tracksterapp.model.Shipment
import com.trackster.tracksterapp.utils.USER_ID_KEY
import kotlinx.android.synthetic.main.fragment_loads.*

class LoadsFragment : BaseFragment(), MainContract.View, MainContract.Presenter {
    override fun updateLoads(loadsList: ArrayList<Shipment>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateView() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setLoadingIndicator(active: Boolean) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showError() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showError(message: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showSuccessMessage(message: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun handleApiError(throwable: Throwable) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun unsubscribe() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getLayoutId(): Int = 0

    override fun getProgressBar(): ProgressBar? = null


    lateinit var presenter: MainContract.Presenter
    var newsList: ArrayList<Shipment>? = null

    private var pageNum = 1
    private var isOnRefresh = false
    private var hasToolbar = false
    private lateinit var adapter: LoadsRecyclerAdapter
    private lateinit var userId: String

    companion object {
        fun newInstance(userId: String): LoadsFragment {
            val loadsFragment = LoadsFragment()

            val args = Bundle()
            args.putSerializable(USER_ID_KEY, userId)
            loadsFragment.arguments = args

            return loadsFragment
        }

        const val PAGE_SIZE = 10
    }

    private fun initRecyclerView() {
        adapter = LoadsRecyclerAdapter(activity!!)
        val linearLayoutManager = LinearLayoutManager(activity)
        recyclerView.layoutManager = linearLayoutManager
        recyclerView.adapter = adapter
        adapter.setData(newsList!!)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        recyclerView.findViewById<RecyclerView>(R.id.recyclerView)
        initRecyclerView()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun getLoads(isProgressBarRefresh: Boolean) {
//        presenter.getNewsfeed(pageNum, PAGE_SIZE, 0, userId, isProgressBarRefresh)
    }

//    override fun getLayoutId(): Int = R.layout.fragment_loads
//
//    override fun getProgressBar(): ProgressBar? = progressBar as ProgressBar?

//    override fun onActivityCreated(savedInstanceState: Bundle?) {
//        super.onActivityCreated(savedInstanceState)
//
////        newsList!!.add(0,(Shipment("prvi","sofer1","golem paket",100)))
////        newsList!!.add(0,(Shipment("vtori","sofer2","golem paket",100)))
////        newsList!!.add(0,(Shipment("treti","sofer3","golem paket",100)))
////        newsList!!.add(0,(Shipment("cetvrti","sofer4","golem paket",100)))
////        newsList!!.add(0,(Shipment("petti","sofer5","golem paket",100)))
//
////        val mainComponent = DaggerMainComponent.builder()
////            .appComponent((activity!!.application as App).getAppComponent())
////            .mainActivityContextModule(MainActivityContextModule(activity!!))
////            .mainPresenterModule(MainPresenterModule(this))
////            .build()
////        mainComponent.injectLoadsFragment(this)
////
////
////        userId = arguments?.getSerializable(USER_ID_KEY) as String
//
////        presenter = mainPresenter
//        initRecyclerView()
//    }



//    override fun onStart() {
//        super.onStart()
////        newsList!!.add(0,(Shipment("prvi","sofer1","golem paket",100)))
////        newsList!!.add(1,(Shipment("vtori","sofer2","golem paket",100)))
////        newsList!!.add(2,(Shipment("treti","sofer3","golem paket",100)))
////        newsList!!.add(3,(Shipment("cetvrti","sofer4","golem paket",100)))
////        newsList!!.add(4,(Shipment("petti","sofer5","golem paket",100)))
//
//        val mainComponent = DaggerMainComponent.builder()
//            .appComponent((activity!!.application as App).getAppComponent())
//            .mainActivityContextModule(MainActivityContextModule(activity!!))
//            .mainPresenterModule(MainPresenterModule(this))
//            .build()
//        mainComponent.injectLoadsFragment(this)
//
//
//        userId = arguments?.getSerializable(USER_ID_KEY) as String
//
////        presenter = mainPresenter
//        initRecyclerView()
//    }

//    override fun updateLoads(loadsList: ArrayList<Shipment>) {
//
//            when (newsList) {
//                null -> this.newsList = loadsList
//                else -> this.newsList?.addAll(loadsList)
//            }
//            adapter.addData(loadsList)
//
//        setViewState(adapter.isEmptyState())
//    }

//    private fun setViewState(empty: Boolean) {
//        if (empty) {
//            recyclerView?.visibility = View.GONE
//            textViewEmptyStateHomeFeed?.visibility = View.VISIBLE
//            textViewEmptyStateHomeFeed?.text = getString(R.string.no_items_home_feed)
//        } else {
//            recyclerView?.visibility = View.VISIBLE
//            textViewEmptyStateHomeFeed?.visibility = View.GONE
//        }
//    }
//
//    override fun handleApiError(throwable: Throwable) {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//    }
}