package com.mzhnf.depokbookstorefix.ui.home

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.mzhnf.depokbookstorefix.R
import com.mzhnf.depokbookstorefix.activity.DetailBookActivity
import com.mzhnf.depokbookstorefix.adapter.BookArrivalAdapter
import com.mzhnf.depokbookstorefix.adapter.BookGenreAdapter
import com.mzhnf.depokbookstorefix.adapter.GenreAdapter
import com.mzhnf.depokbookstorefix.databinding.FragmentHomeBinding
import com.mzhnf.depokbookstorefix.dummy.BookGenre
import com.mzhnf.depokbookstorefix.dummy.Genre
import com.mzhnf.depokbookstorefix.model.home.DataHome
import com.mzhnf.depokbookstorefix.model.home.DataSearch
import com.mzhnf.depokbookstorefix.model.home.HomeViewModel
import com.mzhnf.depokbookstorefix.model.home.SearchViewModel
import com.mzhnf.depokbookstorefix.network.ResultCallback
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment(), ResultCallback, BookGenreAdapter.BookGenreClickListener {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var genreAdapter: GenreAdapter
    private lateinit var bookGenreAdapter: BookGenreAdapter
    private lateinit var bookArrivalAdapter: BookArrivalAdapter
    private lateinit var selectedGenre: String
    private lateinit var bookgenres: List<BookGenre>
    private lateinit var genres: List<Genre>
    private lateinit var searchView: SearchView
    private val bookList = mutableListOf<BookGenre>()
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var searchViewModel: SearchViewModel
    var progressDialog: Dialog? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        homeViewModel.setResultCallback(this)
        searchViewModel = ViewModelProvider(this).get(SearchViewModel::class.java)
        searchViewModel.setResultCallback(this)
        onShowLoading()
        homeViewModel.getHome()
        homeViewModel.homeSuccess.observe(viewLifecycleOwner, Observer { isSuccess ->
            if (isSuccess) {
                val response = homeViewModel.response.value
                if (response != null) {
                    onHomeSuccess(response.data)
                } else {
                    onHomeFailed()
                }
            }
        })

        genres = listOf(Genre("Horror"),Genre("Romance"),Genre("Novel"),Genre("Survival"))
        genreAdapter = GenreAdapter(genres)
        binding.recyclerView.adapter = genreAdapter
        binding.recyclerView.layoutManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)

        binding.searchView.clearFocus()
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                if(!query.isNullOrBlank()){
                    searchViewModel.search(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (!newText.isNullOrBlank()) {
                    searchViewModel.search(newText)
                }
                return true
            }

        })

        bookArrivalAdapter = BookArrivalAdapter()
        binding.rvBookGenre.adapter = bookArrivalAdapter
        binding.rvBookGenre.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        searchViewModel.searchSuccess.observe(viewLifecycleOwner, Observer { isSuccess->
            val response = searchViewModel.response.value
            if(isSuccess){
                onSearchSuccess(response!!.data)
            }else{

            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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

    private fun onHomeSuccess(dataHome: List<DataHome>) {
        val data = dataHome
        bookGenreAdapter = BookGenreAdapter(this)
        if (data!=null){
            bookGenreAdapter.setData(data)
            binding.rvBookNew.adapter = bookGenreAdapter
            binding.rvBookNew.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        }
    }

    private fun onSearchSuccess(dataSearch: List<DataSearch>){
        val dataSearch = dataSearch
        if (dataSearch!=null){

            bookArrivalAdapter.setData(dataSearch)
            bookArrivalAdapter.notifyDataSetChanged()
        }else{
            bookArrivalAdapter.setData(emptyList())
            bookArrivalAdapter.notifyDataSetChanged()
        }
    }

    private fun onHomeFailed() {
        Toast.makeText(requireContext(), "Gagal mengambil Data", Toast.LENGTH_SHORT).show()
    }

    override fun onShowLoading() {
        progressDialog?.show()
    }

    override fun onDismissLoading() {
        progressDialog?.dismiss()
    }

    override fun bookGenreClickListener(item: DataHome) {
        val bookId = item.id
        val title = item.title
        val author = item.author
        val publicationDate = item.publicationDate
        val image = item.image
        val genre = item.genre
        val price = item.price
        val rating = item.rating
        val intent = Intent(context, DetailBookActivity::class.java)
        intent.putExtra("bookId", bookId)
        intent.putExtra("title", title)
        intent.putExtra("author", author)
        intent.putExtra("publicationDate", publicationDate)
        intent.putExtra("image", image)
        intent.putExtra("genre", genre)
        intent.putExtra("price", price)
        intent.putExtra("rating", rating)
        startActivity(intent)
    }
}