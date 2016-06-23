package net.typeblog.fishroomandroid.view

import android.Manifest
import kotlinx.android.synthetic.main.activity_main.*
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.LinearLayoutManager
import android.widget.Toast
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
import net.typeblog.fishroomandroid.model.SendMessageRequestBody
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
        main_list.addItemDecoration(DividerItemDecoration(this, null))

        button_send.setOnClickListener{
            presenter.sendMessage(SendMessageRequestBody(null, text.text.toString()))
        }

    }

    fun sendResult(result:Boolean) {
        if (!result){
            Toast.makeText(this,R.string.send_fail,Toast.LENGTH_SHORT)
        }
        else{
            text.text.clear()
        }
    }

    fun onNewMessage() {
        mAdapter?.notifyDataSetChanged()
        main_list.smoothScrollToPosition(mAdapter?.itemCount!!)
    }
}
