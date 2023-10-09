package com.mzhnf.depokbookstorefix.ui.book

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.mzhnf.depokbookstorefix.DepokBookStore
import com.mzhnf.depokbookstorefix.R
import com.mzhnf.depokbookstorefix.activity.DetailOrderActivity
import com.mzhnf.depokbookstorefix.adapter.BookGenreAdapter
import com.mzhnf.depokbookstorefix.adapter.GetOrderAdapter
import com.mzhnf.depokbookstorefix.databinding.FragmentBookBinding
import com.mzhnf.depokbookstorefix.model.home.SearchViewModel
import com.mzhnf.depokbookstorefix.model.login.Data
import com.mzhnf.depokbookstorefix.model.order.DataOrderByUser
import com.mzhnf.depokbookstorefix.model.order.OrderByUserViewModel
import com.mzhnf.depokbookstorefix.network.ResultCallback
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BookFragment : Fragment(), ResultCallback,GetOrderAdapter.BookGenreClickListener{

    private var _binding: FragmentBookBinding? = null
    private val binding get() = _binding!!
    private lateinit var getOrderAdapter: GetOrderAdapter
    private lateinit var orderByUserViewModel: OrderByUserViewModel
    var progressDialog: Dialog? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBookBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        orderByUserViewModel = ViewModelProvider(this).get(OrderByUserViewModel::class.java)
        orderByUserViewModel.setResultCallback(this)
        val depokBookStore = activity?.application as DepokBookStore
        val user = depokBookStore.getUser()
        val userResponse = Gson().fromJson(user, Data::class.java)
        val token = depokBookStore.getToken()
        val authorizeToken = "Bearer $token"
        onShowLoading()
        orderByUserViewModel.getOrder(userResponse.id.toString(),authorizeToken)
        orderByUserViewModel.getOrderSuccess.observe(viewLifecycleOwner, Observer { isSuccess->
            if (isSuccess){
                val response = orderByUserViewModel.response.value
                if (response!=null){
                    onOrderSuccess(response.data)
                }
            }
        })
    }
    private fun initView() {
        progressDialog = Dialog(requireContext())
        val dialogLayout = layoutInflater.inflate(R.layout.dialog_loader, null)

        progressDialog?.let {
            it.setContentView(dialogLayout)
            it.setCancelable(false)
            it.window?.setBackgroundDrawableResource(android.R.color.transparent)
        }
    }

    private fun onOrderSuccess(data: List<DataOrderByUser>) {
        val data = data
        getOrderAdapter = GetOrderAdapter(this)
        if (data!=null){
            getOrderAdapter.setData(data)
            binding.rvOrder.adapter = getOrderAdapter
            binding.rvOrder.layoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun bookGenreClickListener(item: DataOrderByUser) {
        val orderId = item.id
        val email = item.user.email
        val username = item.user.username
        val address = item.user.address
        val statusBayar = item.paymentStatus
        val orderDate = item.orderDate
        val totalKuantitas = item.quantity
        val totalBayar = item.totalPrice
        val deliveryStatus = item.deliveryStatus
        val intent = Intent(requireContext(),DetailOrderActivity::class.java)
        intent.putExtra("orderId", orderId)
        intent.putExtra("email", email)
        intent.putExtra("username", username)
        intent.putExtra("address", address)
        intent.putExtra("statusBayar", statusBayar)
        intent.putExtra("orderDate", orderDate)
        intent.putExtra("totalKuantitas", totalKuantitas)
        intent.putExtra("totalBayar", totalBayar)
        intent.putExtra("deliveryStatus", deliveryStatus)
        startActivity(intent)
    }

    override fun onShowLoading() {
        progressDialog?.show()
    }

    override fun onDismissLoading() {
        progressDialog?.dismiss()
    }
}