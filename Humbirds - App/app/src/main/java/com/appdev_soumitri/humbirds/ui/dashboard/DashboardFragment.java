package com.appdev_soumitri.humbirds.ui.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.appdev_soumitri.humbirds.LoginActivity;
import com.appdev_soumitri.humbirds.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.Objects;

public class DashboardFragment extends Fragment {

    private DashboardViewModel dashboardViewModel;

    private TextView dispName,dispEmail;
    private Button btnLogout;

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                ViewModelProviders.of(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);

        dispEmail=root.findViewById(R.id.dispEmail);
        dispName=root.findViewById(R.id.dispName);

        final FirebaseAuth auth=FirebaseAuth.getInstance();
        final FirebaseUser currentUser = auth.getCurrentUser();

        if(currentUser==null) {
            startActivity(new Intent(getContext(), LoginActivity.class));
            requireActivity().finish();
        }

        else {

            final String uID = currentUser.getUid();
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
            Query checkUser = reference.orderByChild("userID").equalTo(uID);

            checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {

                        String _name = snapshot.child(uID).child("name").getValue(String.class);
                        String _email = snapshot.child(uID).child("email").getValue(String.class);

                        dispEmail.setText(_email);
                        dispName.setText(_name);

                        Log.d("Status:" , "details displayed");
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }

        btnLogout=root.findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // sign out user
                auth.signOut();
                Toast.makeText(getContext(), "Successfully Logged out !", Toast.LENGTH_SHORT).show();
                Log.d("Auth: ","signed out");
                startActivity(new Intent(getContext(), LoginActivity.class));
                requireActivity().finish();
            }
        });

        return root;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}