package br.edu.ifam.ragnarok.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.edu.ifam.ragnarok.R
import br.edu.ifam.ragnarok.adapters.ProductAdapter
import br.edu.ifam.ragnarok.models.Product
import com.google.firebase.database.*

class HomeFragment : Fragment() {
    var adapter: ProductAdapter? = null
    var recyclerView: RecyclerView? = null
    var productList = ArrayList<Product>()
    private var database: FirebaseDatabase? = null
    private var reference: DatabaseReference? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_home, container, false)

        database = FirebaseDatabase.getInstance()
        reference = database?.getReference("ragnarok-2bd2d-default-rtdb")

        val firebaseListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                productList.clear()

                val child = snapshot.children
                child.forEach {

                    var products = Product(
                        it.child("image").value.toString(),
                        it.child("name").value.toString(),
                        it.child("price").value.toString()
                    )

                    productList.add(products)

                }
                adapter?.notifyDataSetChanged()

            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("test", error.message)
            }

        }
        reference?.addValueEventListener(firebaseListener)

        recyclerView = view.findViewById(R.id.recycler_view)
        recyclerView?.setHasFixedSize(true)
        recyclerView?.layoutManager = GridLayoutManager(
            context,
            2,
            GridLayoutManager.VERTICAL,
            false
        )

        adapter = ProductAdapter(productList)
        recyclerView?.adapter = adapter

        return view
    }
}