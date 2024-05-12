package com.example.askmathadmin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ReportsFragment extends Fragment {

    private RecyclerView recyclerView;
    private ReportedPostAdapter adapter;
    private List<ReportedPost> reportedPosts;
    private FirebaseFirestore db;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db = FirebaseFirestore.getInstance();
        reportedPosts = new ArrayList<>();
        fetchReportedPosts();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reports, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewreport);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ReportedPostAdapter(reportedPosts, new ReportedPostAdapter.OnPostOptionsClickListener() {
            @Override
            public void onPostOptionsClicked(View view, int position, ReportedPost post) {
                showPostOptionsMenu(view, position);
            }
        });
        recyclerView.setAdapter(adapter);

        return view;
    }

    private void fetchReportedPosts() {
        reportedPosts.clear();

        db.collection("reports")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            String description = documentSnapshot.getString("description");
                            String postId = documentSnapshot.getString("postId");
                            String reportReason = documentSnapshot.getString("reportReason");
                            String reporterId = documentSnapshot.getString("reporterId");
                            String title = documentSnapshot.getString("title");

                            ReportedPost reportedPost = new ReportedPost(description, postId, reportReason, reporterId, title);
                            reportedPosts.add(reportedPost);
                        }
                        adapter.notifyDataSetChanged();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle failure
                    }
                });
    }

    private void showPostOptionsMenu(View view, int position) {
        PopupMenu popupMenu = new PopupMenu(requireContext(), view);
        popupMenu.inflate(R.menu.post_options_menu);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.action_edit) {
                    // Handle edit action
                    // You can add your edit functionality here
                    return true;
                } else if (itemId == R.id.action_delete) {
                    // Handle delete action
                    deleteReportedPost(position);
                    return true;
                }
                return false;
            }
        });
        popupMenu.show();
    }

    private void deleteReportedPost(int position) {
        ReportedPost post = reportedPosts.get(position);
        String postId = post.getPostId();

        if (postId != null) {
            db.collection("reports").document(postId).delete()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            reportedPosts.remove(position);
                            adapter.notifyItemRemoved(position);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(requireContext(), "Failed to delete report: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(requireContext(), "Report ID is null, unable to delete report", Toast.LENGTH_SHORT).show();
        }
    }
}
