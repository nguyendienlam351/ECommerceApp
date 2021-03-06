package com.example.ecommerceapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.ecommerceapp.Model.AdminOrders;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AdminNewOrdersActivity extends AppCompatActivity {
    private RecyclerView orderList;
    private DatabaseReference orderRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_new_orders);

        orderRef = FirebaseDatabase.getInstance().getReference().child("Orders");

        orderList = (RecyclerView) findViewById(R.id.order_list);

        orderList.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<AdminOrders> options = new FirebaseRecyclerOptions.Builder<AdminOrders>()
                .setQuery(orderRef, AdminOrders.class).build();
        FirebaseRecyclerAdapter<AdminOrders, AdminOrdersViewHolder> adapter
                = new FirebaseRecyclerAdapter<AdminOrders, AdminOrdersViewHolder>(options) {
            @SuppressLint("RecyclerView")
            @Override
            protected void onBindViewHolder(@NonNull AdminOrdersViewHolder holder, int position, @NonNull AdminOrders model) {
                holder.userName.setText("Name: " + model.getName());
                holder.userPhoneNumber.setText("Phone: " + model.getPhone());
                holder.userTotalPrice.setText("Total Amount: " + model.getTotalAmount() + "$");
                holder.userDateTime.setText("Order at: " + model.getDate() + "\t" + model.getTime());
                holder.userShippingAddress.setText("Shipping Address: " + model.getAddress() + ", " + model.getCity());

                holder.showOrderBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String uID = getRef(position).getKey();

                        Intent intent = new Intent(AdminNewOrdersActivity.this, AdminUserProductsActivity.class);
                        intent.putExtra("uid", uID);
                        startActivity(intent);
                    }
                });

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        CharSequence options[] = new CharSequence[]{
                                "Yes",
                                "No"
                        };

                        AlertDialog.Builder builder = new AlertDialog.Builder(AdminNewOrdersActivity.this);
                        builder.setTitle("Have you shipped this order products?");
                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if(i == 0){
                                    String uID = getRef(position).getKey();

                                    removerOrder(uID);
                                }
                                else {
                                    finish();
                                }
                            }
                        });
                        builder.show();
                    }
                });
            }

            @NonNull
            @Override
            public AdminOrdersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_layout, parent, false);
                return new AdminOrdersViewHolder(view);
            }
        };

        orderList.setAdapter(adapter);
        adapter.startListening();
    }

    public static class AdminOrdersViewHolder extends RecyclerView.ViewHolder {
        public TextView userName, userPhoneNumber, userTotalPrice, userDateTime, userShippingAddress;
        public Button showOrderBtn;

        public AdminOrdersViewHolder(@NonNull View itemView) {
            super(itemView);

            userName = (TextView) itemView.findViewById(R.id.order_user_name);
            userPhoneNumber = (TextView) itemView.findViewById(R.id.order_phone_number);
            userTotalPrice = (TextView) itemView.findViewById(R.id.order_total_price);
            userDateTime = (TextView) itemView.findViewById(R.id.order_date_time);
            userShippingAddress = (TextView) itemView.findViewById(R.id.order_address_city);
            showOrderBtn = (Button) itemView.findViewById(R.id.show_all_products);
        }
    }


    private void removerOrder(String uID) {
        orderRef.child(uID).removeValue();

    }
}