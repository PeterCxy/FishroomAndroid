package net.typeblog.fishroomandroid.view

import android.Manifest
import kotlinx.android.synthetic.main.activity_main.*
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.LinearLayoutManager
import nucleus.view.NucleusAppCompatActivity
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import net.typeblog.fishroomandroid.R
import net.typeblog.fishroomandroid.presenter.MainPresenter
import net.typeblog.fishroomandroid.model.Message
import nucleus.factory.RequiresPresenter

@RequiresPresenter(MainPresenter::class)
class MainActivity : NucleusAppCompatActivity<MainPresenter>() {
    var mAdapter: MessageAdater? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Dexter.checkPermission(object : PermissionListener {
            override fun onPermissionDenied(response: PermissionDeniedResponse?) {
                finish()
            }

            override fun onPermissionGranted(response: PermissionGrantedResponse?) {
                presenter.startLoop()
            }

            override fun onPermissionRationaleShouldBeShown(permission: PermissionRequest?, token: PermissionToken?) {

            }
        }, Manifest.permission.INTERNET)

        mAdapter = MessageAdater(presenter.mList)
        main_list.adapter = mAdapter

        val manager = LinearLayoutManager(this)
        manager.stackFromEnd = true
        main_list.layoutManager = manager
    }

    fun onNewMessage() {
        mAdapter?.notifyDataSetChanged()
    }
}
