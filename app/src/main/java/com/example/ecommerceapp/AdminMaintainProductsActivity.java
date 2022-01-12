package com.example.ecommerceapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.ecommerceapp.Model.Products;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class AdminMaintainProductsActivity extends AppCompatActivity {
    private Button applyChangesBtn, deleteBtn;
    private EditText name, price, description;
    private ImageView imageView;
    private String productID = "";

    private DatabaseReference productRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_maintain_products);

        productID = getIntent().getStringExtra("pid");

        productRef = FirebaseDatabase.getInstance().getReference().child("Products").child(productID);

        applyChangesBtn = (Button) findViewById(R.id.apply_changes_btn);
        deleteBtn = (Button) findViewById(R.id.delete_product_btn);
        name = (EditText) findViewById(R.id.product_name_maintain);
        price = (EditText) findViewById(R.id.product_price_maintain);
        description = (EditText) findViewById(R.id.product_description_maintain);
        imageView = (ImageView) findViewById(R.id.product_image_maintain);

        displaySpecificProductInfo();

        applyChangesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                applyChanges();
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteProduct();
            }
        });
    }

    private void deleteProduct() {
        productRef.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Intent intent = new Intent(AdminMaintainProductsActivity.this, AdminCategoryActivity.class);
                    startActivity(intent);
                    finish();

                    Toast.makeText(AdminMaintainProductsActivity.this,
                            "Deleted product successfully.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void applyChanges() {
        String pName = name.getText().toString();
        String pPrice = price.getText().toString();
        String pDescription = description.getText().toString();

        if (TextUtils.isEmpty(pDescription))
        {
            Toast.makeText(this, "Please write product description.", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(pPrice))
        {
            Toast.makeText(this, "Please write product price.", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(pName))
        {
            Toast.makeText(this, "Please write product name.", Toast.LENGTH_SHORT).show();
        }
        else
        {
            HashMap<String, Object> productMap = new HashMap<>();
            productMap.put("pid", productID);
            productMap.put("description", pDescription);
            productMap.put("price", pPrice);
            productMap.put("pname", pName);

            productRef.updateChildren(productMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(AdminMaintainProductsActivity.this,
                                "Changes applied successfully.", Toast.LENGTH_SHORT).show();


                        Intent intent = new Intent(AdminMaintainProductsActivity.this, AdminCategoryActivity.class);
                        startActivity(intent);
                    }
                }
            });
        }
    }

    private void displaySpecificProductInfo() {
        productRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    Products products = snapshot.getValue(Products.class);

                    name.setText(products.getPname());
                    description.setText(products.getDescription());
                    price.setText(products.getPrice());

                    Picasso.get().load(products.getImage()).into(imageView);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}