package com.mzhnf.depokbookstorefix.ui.cart

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.mzhnf.depokbookstorefix.DepokBookStore
import com.mzhnf.depokbookstorefix.R
import com.mzhnf.depokbookstorefix.adapter.CartAdapter
import com.mzhnf.depokbookstorefix.adapter.GenreAdapter
import com.mzhnf.depokbookstorefix.adapter.GetCartAdapter
import com.mzhnf.depokbookstorefix.databinding.FragmentCartBinding
import com.mzhnf.depokbookstorefix.model.cart.CartByUserViewModel
import com.mzhnf.depokbookstorefix.model.cart.DataCartByUser
import com.mzhnf.depokbookstorefix.model.home.SearchViewModel
import com.mzhnf.depokbookstorefix.model.login.Data
import com.mzhnf.depokbookstorefix.network.ResultCallback
import dagger.hilt.android.AndroidEntryPoint
import java.text.NumberFormat
import java.util.Locale

@AndroidEntryPoint
class CartFragment : Fragment(), CartAdapter.ItemAdapterCallback, ResultCallback, GetCartAdapter.BookGenreClickListener {
    private var cartList: ArrayList<DataCartByUser> = ArrayList()
    private var cartList2: ArrayList<DataCartByUser> = ArrayList()
    private var _binding: FragmentCartBinding? = null
    private val binding get() = _binding!!
    private lateinit var cartByUserViewModel: CartByUserViewModel
    var progressDialog: Dialog? = null
    private lateinit var cartAdapter: CartAdapter
    private lateinit var getCartAdapter: GetCartAdapter
    private var totalKuantitas: Int = 0
    private var totalBayar: Int = 0
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCartBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        cartByUserViewModel = ViewModelProvider(this).get(CartByUserViewModel::class.java)
        cartByUserViewModel.setResultCallback(this)
        val depokBookStore = activity?.application as DepokBookStore
        val user = depokBookStore.getUser()
        val userResponse = Gson().fromJson(user, Data::class.java)
        val token = depokBookStore.getToken()
        val authorizeToken = "Bearer $token"
        onShowLoading()
        cartByUserViewModel.getCart(userResponse.id.toString(),authorizeToken)
        cartByUserViewModel.getCartSuccess.observe(viewLifecycleOwner, Observer { isSuccess ->
            if (isSuccess) {
                val response = cartByUserViewModel.response.value
                if (response != null) {
                    onCartSuccess(response.data)
                }
            } else {

            }
        })
//        cartAdapter = CartAdapter(
//            cartList,
//            this,
//            this::onTotalKuantitasUpdated,
//            this::onTotalBayarUpdated
//        )
//        binding.rvCart.adapter = cartAdapter
//        binding.rvCart.layoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClick(data: DataCartByUser) {
//        val index = cartList2.indexOfFirst { it.id == data.id }
//        if (index != -1) {
//            cartList2[index] = data
//        } else {
//            cartList2.add(data)
//        }
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

    private fun onCartSuccess(cartData: List<DataCartByUser>){
        val data = cartData
        Log.d("data", data.toString())
        cartAdapter = CartAdapter(
            cartList,
            this,
            this::onTotalKuantitasUpdated,
            this::onTotalBayarUpdated
        )
//        getCartAdapter = GetCartAdapter(this)
        if (data!=null){
            cartAdapter.setData(data)
            binding.rvCart.adapter = cartAdapter
            binding.rvCart.layoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
//            getCartAdapter.setData(data)
//            binding.rvCart.adapter = getCartAdapter
//            binding.rvCart.layoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)

        }
    }

    override fun onShowLoading() {
        progressDialog?.show()
    }

    override fun onDismissLoading() {
        progressDialog?.dismiss()
    }

    private fun onTotalKuantitasUpdated(total: Int) {
        totalKuantitas = total
        binding.tvTotalquantity.text = total.toString()
    }

    private fun onTotalBayarUpdated(total: Int) {
        totalBayar = total
        val formattedPrice = NumberFormat.getCurrencyInstance(Locale("id", "ID")).format(totalBayar.toInt())
        binding.tvTotalpay.text = formattedPrice
    }

    override fun bookGenreClickListener(item: DataCartByUser) {
        TODO("Not yet implemented")
    }
}