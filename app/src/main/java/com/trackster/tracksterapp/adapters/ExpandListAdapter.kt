package com.trackster.tracksterapp.adapters

import android.support.design.widget.CoordinatorLayout.Behavior.setTag
import android.R
import android.content.Context
import android.widget.CheckBox
import android.content.Context.LAYOUT_INFLATER_SERVICE
import android.support.v4.content.ContextCompat.getSystemService
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.BaseExpandableListAdapter
import com.trackster.tracksterapp.listeners.TotalListener


class ExpandListAdapter(internal var mContext: Context) : BaseExpandableListAdapter() {

    private var mGroupList: ArrayList<ArrayList<String>> = ArrayList()

    /*
     *  Raw Data
     */
    internal var testChildData = arrayOf("10", "20", "30", "40", "50")
    internal var testgroupData = arrayOf("Apple", "Banana", "Mango", "Orange", "Pineapple", "Strawberry")
    internal var selectedChildCheckBoxStates: ArrayList<ArrayList<Boolean>> = ArrayList()
    internal var selectedParentCheckBoxesState: ArrayList<Boolean> = ArrayList()

    internal lateinit var mListener: TotalListener

    fun setmListener(mListener: TotalListener) {
        this.mListener = mListener
    }

    fun setmGroupList(mGroupList: ArrayList<ArrayList<String>>) {
        this.mGroupList = mGroupList
    }

    internal inner class ViewHolder {
        var groupName: CheckBox? = null
        var dummyTextView: TextView? = null // View to expand or shrink the list
        var childCheckBox: CheckBox? = null
    }

    init {

        //Add raw data into Group List Array
        for (i in testgroupData.indices) {
            val prices = ArrayList<String>()
            for (j in testChildData.indices) {
                prices.add(testChildData[j])
            }
            mGroupList.add(i, prices)
        }

        //initialize default check states of checkboxes
        initCheckStates(false)
    }

    /**
     * Called to initialize the default check states of items
     * @param defaultState : false
     */
    private fun initCheckStates(defaultState: Boolean) {
        for (i in 0 until mGroupList.size) {
            selectedParentCheckBoxesState.add(i, defaultState)
            val childStates = ArrayList<Boolean>()
            for (j in 0 until mGroupList[i].size) {
                childStates.add(defaultState)
            }

            selectedChildCheckBoxStates.add(i, childStates)
        }
    }

    override fun getChild(groupPosition: Int, childPosition: Int): Any {
        return mGroupList[groupPosition].get(childPosition)
    }

    override fun getChildId(groupPosition: Int, childPosition: Int): Long {
        return childPosition.toLong()
    }

    override fun getChildrenCount(groupPosition: Int): Int {
        return mGroupList[groupPosition].size
    }

    override fun getGroup(groupPosition: Int): Any {
        return mGroupList[groupPosition]
    }

    override fun getGroupCount(): Int {
        return mGroupList.size
    }

    override fun getGroupId(groupPosition: Int): Long {
        return groupPosition.toLong()
    }


    override fun hasStableIds(): Boolean {
        return false
    }

    override fun getGroupView(groupPosition: Int, isExpanded: Boolean, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        val holder: ViewHolder
        if (convertView == null) {
            val inflater = mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = inflater.inflate(com.trackster.tracksterapp.R.layout.group_layout, null)
            holder = ViewHolder()
            holder.groupName = convertView!!.findViewById(com.trackster.tracksterapp.R.id.group_chk_box)
            holder.dummyTextView = convertView!!.findViewById(com.trackster.tracksterapp.R.id.dummy_txt_view)
            convertView!!.setTag(holder)
        } else {
            holder = convertView!!.getTag() as ViewHolder
        }

        holder.groupName!!.text = testgroupData[groupPosition]
        if (selectedParentCheckBoxesState.size <= groupPosition) {
            selectedParentCheckBoxesState.add(groupPosition, false)
        } else {
            holder.groupName!!.isChecked = selectedParentCheckBoxesState[groupPosition]
        }



        holder.groupName!!.setOnClickListener {
            //                //Callback to expansion of group item
            if (!isExpanded)
                mListener.expandGroupEvent(groupPosition, isExpanded)

            val state = selectedParentCheckBoxesState[groupPosition]
            Log.d("TAG", "STATE = $state")
            selectedParentCheckBoxesState.removeAt(groupPosition)
            selectedParentCheckBoxesState.add(groupPosition, if (state) false else true)

            for (i in 0 until mGroupList[groupPosition].size) {

                selectedChildCheckBoxStates[groupPosition].removeAt(i)
                selectedChildCheckBoxStates[groupPosition].add(i, if (state) false else true)
            }
            notifyDataSetChanged()
            showTotal(groupPosition)
        }


        //callback to expand or shrink list view from dummy text click
        holder.dummyTextView!!.setOnClickListener(object : View.OnClickListener {
           override fun onClick(v: View) {
                //                //Callback to expansion of group item
                mListener.expandGroupEvent(groupPosition, isExpanded)
            }
        })

        return convertView
    }

    override fun getChildView(
        groupPosition: Int,
        childPosition: Int,
        isLastChild: Boolean,
        convertView: View?,
        parent: ViewGroup
    ): View {
        var convertView = convertView

        val holder: ViewHolder
        if (convertView == null) {
            val inflater = mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = inflater.inflate(com.trackster.tracksterapp.R.layout.child_layout, null)
            holder = ViewHolder()
            holder.childCheckBox = convertView!!.findViewById(com.trackster.tracksterapp.R.id.child_check_box)
            convertView!!.setTag(holder)
        } else {
            holder = convertView!!.getTag() as ViewHolder
        }


        holder.childCheckBox!!.setText(mGroupList[groupPosition].get(childPosition))
        if (selectedChildCheckBoxStates.size <= groupPosition) {
            val childState = ArrayList<Boolean>()
            for (i in 0 until mGroupList[groupPosition].size) {
                if (childState.size > childPosition)
                    childState.add(childPosition, false)
                else
                    childState.add(false)
            }
            if (selectedChildCheckBoxStates.size > groupPosition) {
                selectedChildCheckBoxStates.add(groupPosition, childState)
            } else
                selectedChildCheckBoxStates.add(childState)
        } else {
            holder.childCheckBox!!.isChecked = selectedChildCheckBoxStates[groupPosition].get(childPosition)
        }
        holder.childCheckBox!!.setOnClickListener(object : View.OnClickListener {

          override  fun onClick(v: View) {
                val state = selectedChildCheckBoxStates[groupPosition].get(childPosition)
                selectedChildCheckBoxStates[groupPosition].removeAt(childPosition)
                selectedChildCheckBoxStates[groupPosition].add(childPosition,
                    if (state) false
                    else true)

                showTotal(groupPosition)

            }
        })

        return convertView
    }

    /**
     * Called to reflect the sum of checked prices
     * @param groupPosition : group position of list
     */
    private fun showTotal(groupPosition: Int) {
        //Below code is to get the sum of checked prices
        var sum = 0
        for (j in 0 until selectedChildCheckBoxStates.size) {
            Log.d("TAG", "J = $j")
            for (i in 0 until selectedChildCheckBoxStates[groupPosition].size) {
                Log.d("TAG", "I = $i")

                if (selectedChildCheckBoxStates[j].get(i)) {
                    sum += Integer.parseInt(mGroupList[j].get(i))
                }
            }
        }
        mListener.onTotalChanged(sum)
    }

    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean {
        return true
    }
}