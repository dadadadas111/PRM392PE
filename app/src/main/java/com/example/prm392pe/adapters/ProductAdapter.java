package com.example.prm392pe.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.prm392pe.R;
import com.example.prm392pe.models.Product;

import java.util.ArrayList;
import java.util.List;

/**
 * RecyclerView adapter for displaying products
 */
public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    
    private Context context;
    private List<Product> productList;
    private OnProductClickListener listener;
    
    public interface OnProductClickListener {
        void onProductClick(Product product, int position);
        void onProductLongClick(Product product, int position);
    }
    
    public ProductAdapter(Context context) {
        this.context = context;
        this.productList = new ArrayList<>();
    }
    
    public ProductAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList != null ? productList : new ArrayList<>();
    }
    
    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.bind(product);
    }
    
    @Override
    public int getItemCount() {
        return productList.size();
    }
    
    public void setOnProductClickListener(OnProductClickListener listener) {
        this.listener = listener;
    }
    
    public void updateProducts(List<Product> newProducts) {
        this.productList.clear();
        if (newProducts != null) {
            this.productList.addAll(newProducts);
        }
        notifyDataSetChanged();
    }
    
    public void addProduct(Product product) {
        if (product != null) {
            productList.add(product);
            notifyItemInserted(productList.size() - 1);
        }
    }
    
    public void removeProduct(int position) {
        if (position >= 0 && position < productList.size()) {
            productList.remove(position);
            notifyItemRemoved(position);
        }
    }
    
    public void updateProduct(int position, Product product) {
        if (position >= 0 && position < productList.size() && product != null) {
            productList.set(position, product);
            notifyItemChanged(position);
        }
    }
    
    public Product getProduct(int position) {
        if (position >= 0 && position < productList.size()) {
            return productList.get(position);
        }
        return null;
    }
    
    public void clearProducts() {
        productList.clear();
        notifyDataSetChanged();
    }
    
    public class ProductViewHolder extends RecyclerView.ViewHolder {
        
        private ImageView productImage;
        private TextView productName;
        private TextView productDescription;
        private TextView productPrice;
        private TextView productCategory;
        private TextView productQuantity;
        
        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            
            productImage = itemView.findViewById(R.id.img_product);
            productName = itemView.findViewById(R.id.tv_product_name);
            productDescription = itemView.findViewById(R.id.tv_product_description);
            productPrice = itemView.findViewById(R.id.tv_product_price);
            productCategory = itemView.findViewById(R.id.tv_product_category);
            productQuantity = itemView.findViewById(R.id.tv_product_quantity);
            
            // Set click listeners
            itemView.setOnClickListener(v -> {
                if (listener != null && getAdapterPosition() != RecyclerView.NO_POSITION) {
                    listener.onProductClick(productList.get(getAdapterPosition()), getAdapterPosition());
                }
            });
            
            itemView.setOnLongClickListener(v -> {
                if (listener != null && getAdapterPosition() != RecyclerView.NO_POSITION) {
                    listener.onProductLongClick(productList.get(getAdapterPosition()), getAdapterPosition());
                    return true;
                }
                return false;
            });
        }
        
        public void bind(Product product) {
            productName.setText(product.getName());
            productDescription.setText(product.getDescription());
            productPrice.setText(product.getFormattedPrice());
            productCategory.setText(product.getCategory());
            productQuantity.setText("Qty: " + product.getQuantity());
            
            // Load image using Glide
            if (product.getImageUrl() != null && !product.getImageUrl().isEmpty()) {
                Glide.with(context)
                        .load(product.getImageUrl())
                        .placeholder(R.drawable.ic_launcher_foreground) // Default placeholder
                        .error(R.drawable.ic_launcher_foreground) // Error image
                        .into(productImage);
            } else {
                productImage.setImageResource(R.drawable.ic_launcher_foreground);
            }
            
            // Set alpha based on availability
            float alpha = product.isAvailable() ? 1.0f : 0.5f;
            itemView.setAlpha(alpha);
        }
    }
}
