package com.chengtech.chengtechmt;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RelativeLayout;

import com.unnamed.b.atv.model.TreeNode;
import com.unnamed.b.atv.view.AndroidTreeView;

public class TestActivity2 extends AppCompatActivity {

    RelativeLayout containerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test3);
        containerView = (RelativeLayout) findViewById(R.id.activity_test3);

        TreeNode root = TreeNode.root();
        TreeNode group1 = new TreeNode("group1");
        TreeNode child1 = new TreeNode("child1");
        TreeNode child2 = new TreeNode("child2");
        group1.addChildren(child1,child2);
        root.addChild(group1);
        AndroidTreeView treeView = new AndroidTreeView(this,root);
        containerView.addView(treeView.getView());
    }
}
