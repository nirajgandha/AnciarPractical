package com.niraj.anciarpractical.custom_ui

import android.graphics.Canvas
import android.view.View
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.view.ViewGroup

class StickyHeaderItemDecorator(private val adapter: StickyAdapter<*, *>) : ItemDecoration() {
    private var currentStickyPosition = RecyclerView.NO_POSITION
    private var recyclerView: RecyclerView? = null
    private var currentStickyHolder: RecyclerView.ViewHolder? = null
    fun attachToRecyclerView(recyclerView: RecyclerView?) {
        if (this.recyclerView == recyclerView) {
            return
        }
        recyclerView?.let { destroyCallbacks(it) }
        this.recyclerView = recyclerView
        if (recyclerView != null) {
            currentStickyHolder = adapter.onCreateHeaderViewHolder(recyclerView)
            fixLayoutSize()
            setupCallbacks()
        }
    }

    private fun setupCallbacks() {
        recyclerView!!.addItemDecoration(this)
    }

    private fun destroyCallbacks(recyclerView: RecyclerView) {
        recyclerView.removeItemDecoration(this)
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDrawOver(c, parent, state)
        val layoutManager = parent.layoutManager ?: return
        var topChildPosition = RecyclerView.NO_POSITION
        if (layoutManager is LinearLayoutManager) {
            topChildPosition = layoutManager.findFirstVisibleItemPosition()
        } else if (layoutManager is StaggeredGridLayoutManager) {
            val into = IntArray(3)
            layoutManager.findFirstVisibleItemPositions(into)
            topChildPosition = into[0]
        } else {
            val topChild = parent.getChildAt(0)
            if (topChild != null) {
                topChildPosition = parent.getChildAdapterPosition(topChild)
            }
        }
        if (topChildPosition == RecyclerView.NO_POSITION) {
            return
        }
        updateStickyHeader(topChildPosition)
        drawHeader(c)
    }

    private fun updateStickyHeader(topChildPosition: Int) {
        val headerPositionForItem = adapter.getHeaderPositionForItem(topChildPosition)
        if (headerPositionForItem != currentStickyPosition && headerPositionForItem != RecyclerView.NO_POSITION) {
            adapter.onBindHeaderViewHolder(currentStickyHolder, headerPositionForItem)
            currentStickyPosition = headerPositionForItem
        } else if (headerPositionForItem != RecyclerView.NO_POSITION) {
            adapter.onBindHeaderViewHolder(currentStickyHolder, headerPositionForItem)
        }
    }

    private fun drawHeader(c: Canvas) {
        c.save()
        c.translate(0f, 0f)
        currentStickyHolder!!.itemView.draw(c)
        c.restore()
    }

    private fun fixLayoutSize() {
        recyclerView!!.viewTreeObserver.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                recyclerView!!.viewTreeObserver.removeOnGlobalLayoutListener(this)
                // Specs for parent (RecyclerView)
                val widthSpec =
                    View.MeasureSpec.makeMeasureSpec(recyclerView!!.width, View.MeasureSpec.EXACTLY)
                val heightSpec = View.MeasureSpec.makeMeasureSpec(
                    recyclerView!!.height,
                    View.MeasureSpec.UNSPECIFIED
                )

                // Specs for children (headers)
                val childWidthSpec = ViewGroup.getChildMeasureSpec(
                    widthSpec,
                    recyclerView!!.paddingLeft + recyclerView!!.paddingRight,
                    currentStickyHolder!!.itemView.layoutParams.width
                )
                val childHeightSpec = ViewGroup.getChildMeasureSpec(
                    heightSpec,
                    recyclerView!!.paddingTop + recyclerView!!.paddingBottom,
                    currentStickyHolder!!.itemView.layoutParams.height
                )
                currentStickyHolder!!.itemView.measure(childWidthSpec, childHeightSpec)
                currentStickyHolder!!.itemView.layout(
                    0, 0,
                    currentStickyHolder!!.itemView.measuredWidth,
                    currentStickyHolder!!.itemView.measuredHeight
                )
            }
        })
    }
}