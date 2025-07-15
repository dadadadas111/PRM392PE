package com.example.prm392pe.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392pe.R;
import com.example.prm392pe.adapters.ProductAdapter;
import com.example.prm392pe.models.Product;

import java.util.ArrayList;
import java.util.List;

/**
 * Fragment demonstrating RecyclerView with product list
 */
public class ProductListFragment extends Fragment implements ProductAdapter.OnProductClickListener {
    
    private RecyclerView recyclerView;
    private ProductAdapter adapter;
    
    public static ProductListFragment newInstance() {
        return new ProductListFragment();
    }
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_product_list, container, false);
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        setupRecyclerView(view);
        loadSampleData();
    }
    
    private void setupRecyclerView(View view) {
        recyclerView = view.findViewById(R.id.recycler_view_products);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        
        adapter = new ProductAdapter(getContext());
        adapter.setOnProductClickListener(this);
        recyclerView.setAdapter(adapter);
    }
    
    private void loadSampleData() {
        List<Product> products = generateSampleProducts();
        adapter.updateProducts(products);
    }
    
    private List<Product> generateSampleProducts() {
        List<Product> products = new ArrayList<>();
        
        products.add(new Product(1, "Smartphone", "Latest Android smartphone with 128GB storage", 599.99, 
                "", "Electronics", 15, true));
        
        products.add(new Product(2, "Laptop", "High-performance laptop for gaming and work", 1299.99, 
                "", "Electronics", 8, true));
        
        products.add(new Product(3, "Headphones", "Wireless noise-cancelling headphones", 249.99, 
                "", "Audio", 25, true));
        
        products.add(new Product(4, "Coffee Maker", "Automatic coffee maker with timer", 89.99, 
                "", "Kitchen", 12, true));
        
        products.add(new Product(5, "Book", "Programming guide for Android development", 45.99, 
                "", "Books", 30, true));
        
        products.add(new Product(6, "Tablet", "10-inch tablet with stylus support", 399.99, 
                "", "Electronics", 5, false));
        
        products.add(new Product(7, "Watch", "Smartwatch with fitness tracking", 199.99, 
                "", "Wearables", 20, true));
        
        products.add(new Product(8, "Camera", "Digital camera with 4K video recording", 799.99, 
                "", "Photography", 7, true));
        
        return products;
    }
    
    @Override
    public void onProductClick(Product product, int position) {
        Toast.makeText(getContext(), "Clicked: " + product.getName(), Toast.LENGTH_SHORT).show();
        
        // Example of updating product
        product.setQuantity(product.getQuantity() - 1);
        if (product.getQuantity() <= 0) {
            product.setAvailable(false);
            product.setQuantity(0);
        }
        adapter.updateProduct(position, product);
    }
    
    @Override
    public void onProductLongClick(Product product, int position) {
        Toast.makeText(getContext(), "Long clicked: " + product.getName() + "\nRemoving item...", Toast.LENGTH_SHORT).show();
        adapter.removeProduct(position);
    }
    
    public void addNewProduct() {
        Product newProduct = new Product(
                (int) (System.currentTimeMillis() % 10000),
                "New Product",
                "Dynamically added product",
                99.99,
                "",
                "New",
                10,
                true
        );
        adapter.addProduct(newProduct);
        
        // Scroll to the newly added item
        recyclerView.scrollToPosition(adapter.getItemCount() - 1);
    }
    
    public void clearAllProducts() {
        adapter.clearProducts();
    }
}
