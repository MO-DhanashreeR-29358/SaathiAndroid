package com.mosl.tabapp.slider

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import com.mosl.tabapp.R

class ImageSliderAdapter(
  val context: Context,
  val imageList: Array<Int>
) : PagerAdapter() {

  override fun instantiateItem(
    container: ViewGroup,
    position: Int
  ): Any {
    val imageView: ImageView

    val layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    val view = layoutInflater.inflate(R.layout.item_image_slider, container, false);

    imageView = view.findViewById(R.id.imageView)
    imageView.setImageResource(imageList[position]);
    container.addView(view);

    return view

  }

  override fun isViewFromObject(
    view: View,
    `object`: Any
  ): Boolean {
    return view == `object`
  }

  override fun getCount(): Int {
    return imageList.size
  }

  override fun destroyItem(
    container: ViewGroup,
    position: Int,
    `object`: Any
  ) {
    val view = `object` as View
    container.removeView(view)
  }
}